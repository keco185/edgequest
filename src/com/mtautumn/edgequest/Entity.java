package com.mtautumn.edgequest;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import com.mtautumn.edgequest.PathFinder.IntCoord;
import com.mtautumn.edgequest.data.DataManager;

public class Entity implements Externalizable {
	private static final long serialVersionUID = 1L;
	public static enum EntityType {

		character,
		player,
		villager,
		pet,
		passiveCreature,
		hostileCreature

	}
	protected int entityID;
	public int dungeonLevel = -1;
	protected float moveRot;
	protected String entityTexture;
	protected EntityType entityType;
	protected String nameTag = "";
	protected double posX, posY;
	public double frameX, frameY;
	public double moveSpeed = 1.0;
	protected int destinationX, destinationY;
	protected double rotation;
	protected PathFinder aStar;
	public ArrayList<IntCoord> path;
	public DataManager dm;
	protected long lastUpdate;
	public boolean slide = false;
	protected double lastSpeedX, lastSpeedY;
	public int[] stillAnimation = new int[]{0};
	public int[] walkAnimation = new int[]{0};
	public double lastPosX = 0;
	public double lastPosY = 0;
	protected boolean wasWalking = false;
	public int maxHealth = 1;
	public int health = 1;

	public String getTexture() {
		boolean walking = wasWalking;
		if (lastPosX != posX || lastPosY != posY) {
			wasWalking = true;
		} else {
			wasWalking = false;
		}
		if (lastPosX != posX || lastPosY != posY || walking) {
			lastPosX = posX;
			lastPosY = posY;
			return entityTexture + "walk" + walkAnimation[dm.system.animationClock % walkAnimation.length];
		}
		return entityTexture + "still" + stillAnimation[dm.system.animationClock % stillAnimation.length];
	}
	public Entity(String texture, EntityType type, DataManager dm) {
		this.entityID = dm.savable.entityID++;
		this.entityTexture = texture;
		this.entityType = type;
		this.dm = dm;
		if (type == EntityType.character) {
			moveSpeed = dm.settings.moveSpeed;
		}
	}
	public Entity(String texture, EntityType type, double posX, double posY, double rotation, int dungeonLevel, DataManager dm) {
		this.entityID = dm.savable.entityID++;
		this.entityTexture = texture;
		this.entityType = type;
		this.posX = posX;
		this.posY = posY;
		this.rotation = rotation;
		this.dm = dm;
		this.dungeonLevel = dungeonLevel;
		if (type == EntityType.character) {
			moveSpeed = dm.settings.moveSpeed;
		}
	}
	public Entity() {

	}

	public double getX() {
		return posX;
	}
	public double getY() {
		return posY;
	}
	public double getRot() {
		return rotation;
	}
	public float getMoveRot() {
		return moveRot;
	}
	public int getID() {
		return entityID;
	}
	public String getTextureName() {
		return entityTexture;
	}
	public EntityType getType() {
		return entityType;
	}
	public String getNameTag() {
		return nameTag;
	}
	public boolean hasNameTag() {
		return !nameTag.equals("");
	}
	public void setX(double x) {
		posX = x;
	}
	public void setY(double y) {
		posY = y;
	}
	public void setPos(double x, double y) {
		posX = x;
		posY = y;
	}
	public void setRot(byte rot) {
		rotation = rot;
	}
	public void changeNameTag(String tag) {
		nameTag = tag;
	}
	public void setDestination(int x, int y) {
		destinationX = x;
		destinationY = y;
		aStar = new PathFinder(dm);
		path = aStar.findPath((int) posX, (int) posY, destinationX, destinationY, dungeonLevel, dm);
	}
	public void reCalculatePath() {
		if (aStar != null) {
			path = aStar.findPath((int) posX, (int) posY, destinationX, destinationY, dungeonLevel, dm);
		}
	}
	public void update() {
		if (lastUpdate == 0L) lastUpdate = System.currentTimeMillis();
		if (path != null) {
			if (path.size() > 0) {
				if (approachPoint(path.get(path.size() - 1), System.currentTimeMillis() - lastUpdate) || isImpassible(path.get(path.size() - 1))) { //returns true if arrived at point
					path.remove(path.size() - 1);
				}
			}
		}
		lastUpdate = System.currentTimeMillis();
	}
	protected boolean isImpassible(IntCoord point) {
		if (dm.world.isStructBlock(this, point.x, point.y)) {
			return !dm.system.blockIDMap.get(dm.world.getStructBlock(this, point.x, point.y)).isPassable;
		}
		return false;
	}
	public void move(double deltaX, double deltaY) {
		if (slide) {
			if (Math.abs(deltaX - lastSpeedX) > 0.012) {
				if (Math.signum(deltaX) != 0 || isOnIce()) {
					if (isOnIce()) {
						deltaX = lastSpeedX + Math.signum(deltaX - lastSpeedX) * Math.abs(deltaX - lastSpeedX) / 20.0;
					} else {
						deltaX = lastSpeedX + Math.signum(deltaX - lastSpeedX) * Math.abs(deltaX - lastSpeedX) / 10.0;
					}
				} else {
					deltaX = lastSpeedX + Math.signum(deltaX - lastSpeedX) * 0.012;
				}
			}
			if (Math.abs(deltaY - lastSpeedY) > 0.012) {
				if (Math.signum(deltaY) != 0 || isOnIce()) {
					if (isOnIce()) {
						deltaY = lastSpeedY + Math.signum(deltaY - lastSpeedY) * Math.abs(deltaY - lastSpeedY) / 20.0;
					} else {
						deltaY = lastSpeedY + Math.signum(deltaY - lastSpeedY) * Math.abs(deltaY - lastSpeedY) / 10.0;
					}
				} else {
					deltaY = lastSpeedY + Math.signum(deltaY - lastSpeedY) * 0.012;
				}
			}
			lastSpeedX = deltaX;
			lastSpeedY = deltaY;
		}
		if (checkMoveProposal(deltaX, true)) {
			posX += deltaX;
		}
		if (checkMoveProposal(deltaY, false)) {
			posY += deltaY;
		}
		moveRot = (float) Math.atan2(deltaY, deltaX);
	}
	public void move(double deltaX, double deltaY, double rot) {
		Vector2f moveVec = new Vector2f((float) -deltaY, (float) deltaX);
		moveVec.add(rot * 57);
		double dX = moveVec.getX();
		double dY = moveVec.getY();
		if (slide) {
			if (Math.abs(dX - lastSpeedX) > 0.012) {
				if (Math.signum(dX) != 0 || isOnIce()) {
					if (isOnIce()) {
						dX = lastSpeedX + Math.signum(dX - lastSpeedX) * Math.abs(dX - lastSpeedX) / 20.0;
					} else {
						dX = lastSpeedX + Math.signum(dX - lastSpeedX) * Math.abs(dX - lastSpeedX) / 10.0;
					}
				} else {
					dX = lastSpeedX + Math.signum(dX - lastSpeedX) * 0.012;
				}
			}
			if (Math.abs(dY - lastSpeedY) > 0.012) {
				if (Math.signum(dY) != 0 || isOnIce()) {
					if (isOnIce()) {
						dY = lastSpeedY + Math.signum(dY - lastSpeedY) * Math.abs(dY - lastSpeedY) / 20.0;
					} else {
						dY = lastSpeedY + Math.signum(dY - lastSpeedY) * Math.abs(dY - lastSpeedY) / 10.0;
					}
				} else {
					dY = lastSpeedY + Math.signum(dY - lastSpeedY) * 0.012;
				}
			}
			lastSpeedX = dX;
			lastSpeedY = dY;
		}
		if (checkMoveProposal(dX, true)) {
			posX += dX;
		}
		if (checkMoveProposal(dY, false)) {
			posY += dY;
		}
		moveRot = (float) rotation;
	}
	private boolean approachPoint(IntCoord point, long timeStep) {
		double ptX = point.x + 0.5;
		double ptY = point.y + 0.5;
		double xSpeed = Math.signum(ptX - posX) * Double.valueOf(timeStep) / 1000.0 * moveSpeed;
		double ySpeed = Math.signum(ptY - posY) * Double.valueOf(timeStep) / 1000.0 * moveSpeed;
		if (xSpeed != 0 && ySpeed != 0) {
			xSpeed *= 0.7071067812;
			ySpeed *= 0.7071067812;
		}
		if ((posX + xSpeed > ptX && posX < ptX) || (posX + xSpeed < ptX && posX > ptX)) {
			xSpeed = ptX - posX;
		}
		if ((posY + ySpeed > ptY && posY < ptY) || (posY + ySpeed < ptY && posY > ptY)) {
			ySpeed = ptY - posY;
		}

		if (checkMoveProposal(xSpeed, true)) {
			posX += xSpeed;
		}
		if (checkMoveProposal(ySpeed, false)) {
			posY += ySpeed;
		}
		updateRotation(xSpeed, ySpeed);
		return (Math.abs(posX - ptX) < 0.01 && Math.abs(posY - ptY) < 0.01);
	}
	private boolean checkMoveProposal(double speed, boolean isX) {
		int entityX;
		int entityY;
		if (isX) {
			entityX = (int) Math.floor(speed + posX);
			entityY = (int) Math.floor(posY);
		} else {
			entityY = (int) Math.floor(speed + posY);
			entityX = (int) Math.floor(posX);
		}
		if (dm.world.isStructBlock(this,entityX, entityY)) {
			return (dm.system.blockIDMap.get(dm.world.getStructBlock(this,entityX, entityY)).isPassable);
		}
		return true;
	}
	public void updateRotation(double xSpeed, double ySpeed) {
		double newRotation = Math.atan2(ySpeed, xSpeed);
		double newRotation2 = (newRotation > 0) ? newRotation - 6.2831853072 : newRotation + 6.2831853072;
		if (Math.abs(newRotation - rotation) < Math.abs(newRotation2 - rotation)) { //use newRotation
			if (Math.abs(newRotation - rotation) < 0.2) {
				rotation = newRotation;
			} else {
				rotation += Math.signum(newRotation - rotation) * Math.abs(newRotation - rotation) / 3.0;
			}
		} else { //use newRotation2
			if (Math.abs(newRotation2 - rotation) < 0.2) {
				rotation = newRotation2;
			} else {
				rotation += Math.signum(newRotation2 - rotation) * Math.abs(newRotation2 - rotation) / 3.0;
			}
		}
		if (rotation > Math.PI) {
			rotation -= 6.2831853072;
		} else if (rotation < -Math.PI) {
			rotation += 6.2831853072;
		}
	}
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(entityID);
		out.writeObject(entityTexture);
		out.writeObject(entityType);
		out.writeObject(nameTag);
		out.writeDouble(posX);
		out.writeDouble(posY);
		out.writeDouble(moveSpeed);
		out.writeInt(destinationX);
		out.writeInt(destinationY);
		out.writeDouble(rotation);
		out.writeObject(path);
		out.writeInt(dungeonLevel);
		out.writeInt(maxHealth);
		out.writeInt(health);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		entityID = in.readInt();
		entityTexture = (String) in.readObject();
		entityType = (EntityType) in.readObject();
		nameTag = (String) in.readObject();
		posX = in.readDouble();
		posY = in.readDouble();
		moveSpeed = in.readDouble();
		destinationX = in.readInt();
		destinationY = in.readInt();
		rotation = in.readDouble();
		path = (ArrayList<IntCoord>) in.readObject();
		dungeonLevel = in.readInt();
		maxHealth = in.readInt();
		health = in.readInt();

	}
	public void initializeClass(DataManager dm) {
		this.dm = dm;
		if (entityType == EntityType.character) {
			moveSpeed = dm.settings.moveSpeed;
		}
	}
	public boolean isOnIce() {
		if (dm.world.isGroundBlock(this, (int) posX, (int) posY)) {
			return dm.system.blockIDMap.get(dm.world.getGroundBlock(this, (int) posX, (int) posY)).isName("ice");
		}
		return false;
	}
	public double distanceToPlayer() {
		return Math.sqrt(Math.pow(dm.characterManager.characterEntity.getX() - getX(), 2) + Math.pow(dm.characterManager.characterEntity.getY() - getY(), 2));
	}
	public BlockItem getBlock() {
		if (dm.world.isGroundBlock(this, (int) getX(), (int) getY())) {
			return dm.system.blockIDMap.get(dm.world.getGroundBlock(this, (int) getX(), (int) getY()));
		}
		return null;
	}
	public BlockItem getRelativeGroundBlock(int deltaX, int deltaY) {
		return dm.system.blockIDMap.get(dm.world.getGroundBlock(this, (int) getX() + deltaX, (int) getY() + deltaY));
	}
	public BlockItem getRelativeStructureBlock(int deltaX, int deltaY) {
		return dm.system.blockIDMap.get(dm.world.getStructBlock(this, (int) posX + deltaX, (int) posY + deltaY));
	}


	protected boolean isLineOfSight(double x, double y) {
		double checkingPosX = posX;
		double checkingPosY = posY;
		double deltaX = (x-posX);
		double deltaY = (y-posY);
		boolean answer = true;
		if (deltaX != 0 || deltaY != 0) {
			while(isInBetween(posX,x,checkingPosX) && isInBetween(posY,y,checkingPosY)) {
				if (isBlockOpaque((int)Math.floor(checkingPosX), (int)Math.floor(checkingPosY))) {
					//if (!(Math.abs(checkingPosX % 1.0) < 0.001 && Math.abs(checkingPosY % 1.0) < 0.001)) {
					answer = false;
					//}
				}
				double xNextLine;
				double yNextLine;
				if (deltaX > 0) {
					xNextLine = (Math.ceil(checkingPosX) - checkingPosX) / deltaX;
				} else if (deltaX < 0) {
					xNextLine = (checkingPosX - Math.floor(checkingPosX)) / deltaX;
				} else {
					xNextLine = 100;
				}
				if (xNextLine == 0) {
					xNextLine = 1.0 / deltaX;
				}
				if (deltaY > 0) {
					yNextLine = (Math.ceil(checkingPosY) - checkingPosY) / deltaY;
				} else if (deltaY < 0) {
					yNextLine = (checkingPosY - Math.floor(checkingPosY)) / deltaY;
				} else {
					yNextLine = 100;
				}
				if (yNextLine == 0) {
					yNextLine = 1.0 / deltaY;
				}
				xNextLine = Math.abs(xNextLine);
				yNextLine = Math.abs(yNextLine);
				if ((Math.abs(checkingPosX % 1.0) < 0.001 && Math.abs(checkingPosY % 1.0) < 0.001)) {
					xNextLine = 0.01;
					yNextLine = 0.01;
				}
				if (Math.abs(xNextLine) < Math.abs(yNextLine)) {
					checkingPosX += xNextLine * deltaX;
					checkingPosY += xNextLine * deltaY;
				} else {
					checkingPosX += yNextLine * deltaX;
					checkingPosY += yNextLine * deltaY;
				}

			}
		}
		return answer;
	}
	private static boolean isInBetween(double num1, double num2, double numCheck) {
		if (num1 <= numCheck && num2 >= numCheck) { return true; }
		return (num1 >= numCheck && num2 <= numCheck);
	}
	private boolean isBlockOpaque(int x, int y) {
		Location checkLocation = new Location(this);
		checkLocation.x = x;
		checkLocation.y = y;
		if (dm.world.isStructBlock(checkLocation)) {
			return !dm.system.blockIDMap.get(dm.world.getStructBlock(checkLocation)).isPassable;
		}
		return false;
	}
}
