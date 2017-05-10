package com.mtautumn.edgequest.entities;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.utils.PathFinder;
import com.mtautumn.edgequest.utils.PathFinder.IntCoord;

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
	public EntityType entityType;
	protected String nameTag = "";
	public double posX;
	public double posY;
	public double frameX, frameY;
	public double moveSpeed = 1.0;
	protected int destinationX, destinationY;
	protected double rotation;
	protected PathFinder aStar;
	public ArrayList<IntCoord> path;
	protected long lastUpdate;
	public boolean slide = true;
	public double lastSpeedX, lastSpeedY;
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
			return entityTexture + "." + entityTexture + "walk" + walkAnimation[SystemData.animationClock % walkAnimation.length];
		}
		return entityTexture + "." + entityTexture + "still" + stillAnimation[SystemData.animationClock % stillAnimation.length];
	}
	public Entity(String texture, EntityType type) {
		this.entityID = DataManager.savable.entityID++;
		this.entityTexture = texture;
		this.entityType = type;
		if (type == EntityType.character) {
			moveSpeed = SettingsData.moveSpeed;
		}
	}
	public Entity(String texture, EntityType type, double posX, double posY, double rotation, int dungeonLevel) {
		this.entityID = DataManager.savable.entityID++;
		this.entityTexture = texture;
		this.entityType = type;
		this.posX = posX;
		this.posY = posY;
		this.rotation = rotation;
		this.dungeonLevel = dungeonLevel;
		if (type == EntityType.character) {
			moveSpeed = SettingsData.moveSpeed;
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
	public void death() {
		
	}
	public void setDestination(int x, int y) {
		destinationX = x;
		destinationY = y;
		aStar = new PathFinder();
		path = aStar.findPath((int) Math.floor(posX), (int) Math.floor(posY), destinationX, destinationY, dungeonLevel);
	}
	public void reCalculatePath() {
		if (aStar != null) {
			path = aStar.findPath((int) Math.floor(posX), (int) Math.floor(posY), destinationX, destinationY, dungeonLevel);
		}
	}
	public void update() {
		if (lastUpdate == 0L) {
			lastUpdate = System.currentTimeMillis();
		}
		if (path != null) {
			if (path.size() > 0) {
				if (approachPoint(path.get(path.size() - 1), 30) || isImpassible(path.get(path.size() - 1))) { //returns true if arrived at point
					path.remove(path.size() - 1);
				}
			}
		}
		lastUpdate = System.currentTimeMillis();
	}
	protected boolean isImpassible(IntCoord point) {
		if (DataManager.world.isStructBlock(this, point.x, point.y)) {
			return !SystemData.blockIDMap.get(DataManager.world.getStructBlock(this, point.x, point.y)).isPassable;
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
		if (DataManager.world.isStructBlock(this,entityX, entityY)) {
			return (SystemData.blockIDMap.get(DataManager.world.getStructBlock(this,entityX, entityY)).isPassable);
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
	public void initializeClass() {
		if (entityType == EntityType.character) {
			moveSpeed = SettingsData.moveSpeed;
		}
	}
	public boolean isOnIce() {
		if (DataManager.world.isGroundBlock(this, (int) Math.floor(posX), (int) Math.floor(posY))) {
			return SystemData.blockIDMap.get(DataManager.world.getGroundBlock(this, (int) Math.floor(posX), (int) Math.floor(posY))).isName("ice");
		}
		return false;
	}
	public double distanceToPlayer() {
		return Math.sqrt(Math.pow(CharacterManager.characterEntity.getX() - posX, 2) + Math.pow(CharacterManager.characterEntity.getY() - posY, 2));
	}
	public BlockItem getBlock() {
		if (DataManager.world.isGroundBlock(this, (int) Math.floor(posX), (int) Math.floor(posY))) {
			return SystemData.blockIDMap.get(DataManager.world.getGroundBlock(this, (int) Math.floor(posX), (int) Math.floor(posY)));
		}
		return null;
	}
	public BlockItem getRelativeGroundBlock(int deltaX, int deltaY) {
		return SystemData.blockIDMap.get(getRelativeGroundBlockID(deltaX, deltaY));
	}
	public BlockItem getRelativeStructureBlock(int deltaX, int deltaY) {
		return SystemData.blockIDMap.get(getRelativeStructureBlockID(deltaX, deltaY));
	}
	public short getRelativeGroundBlockID(int deltaX, int deltaY) {
		return DataManager.world.getGroundBlock(this, (int) Math.floor(getX() + deltaX), (int) Math.floor(getY() + deltaY));
	}
	public short getRelativeStructureBlockID(int deltaX, int deltaY) {
		return DataManager.world.getStructBlock(this, (int) Math.floor(posX + deltaX), (int) Math.floor(posY + deltaY));
	}

	protected boolean isInFOV(double x, double y, double fov) {
		double angle = Math.atan2(y-posY, x-posX);
		double difference = Math.abs(angle - rotation);
		if (difference < fov / 2.0) {
			return true;
		}
		if (angle < 0) {
			angle += Math.PI * 2;
		}
		double angle2 = rotation;
		if (angle2 < 0) {
			angle2 += Math.PI * 2;
		}
		difference = Math.abs(angle - angle2);
		return difference < fov / 2.0;
	}
	protected boolean isLineOfSightFOV(double x, double y, double fov) {
		return isInFOV(x, y, fov) && isLineOfSight(x, y);
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
		if (DataManager.world.isStructBlock(checkLocation)) {
			return !SystemData.blockIDMap.get(DataManager.world.getStructBlock(checkLocation)).isPassable;
		}
		return false;
	}
}
