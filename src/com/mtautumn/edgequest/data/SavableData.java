package com.mtautumn.edgequest.data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mtautumn.edgequest.DamagePost;
import com.mtautumn.edgequest.dataObjects.Chunk;
import com.mtautumn.edgequest.dataObjects.FootPrint;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.particles.Particle;
import com.mtautumn.edgequest.projectiles.Projectile;

public class SavableData implements Externalizable {
	private static final long serialVersionUID = 1L;
	public int time = 800;
	public Map<String, Chunk> loadedChunks = new ConcurrentHashMap<String, Chunk>();
	public Map<String, LightSource> lightMap = new ConcurrentHashMap<String, LightSource>();
	public Map<String, int[]> dungeonStairs = new ConcurrentHashMap<String, int[]>();
	public ArrayList<FootPrint> footPrints = new ArrayList<FootPrint>();
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public ArrayList<DamagePost> damagePosts = new ArrayList<DamagePost>();
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<Particle> precipitationParticles = new ArrayList<Particle>();
	public ArrayList<String> generatedRegions = new ArrayList<String>();
	public ArrayList<ItemDrop> itemDrops = new ArrayList<ItemDrop>();
	public ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
	public double dryness = 0.5;
	public long seed = 7;
	public int dungeonX = 0;
	public int dungeonY = 0;
	public int dungeonLevel = -1;
	public int lastDungeonLevel = -1;
	public int entityID = 0;
	public ItemSlot[][] backpackItems = new ItemSlot[7][6];
	public ItemSlot leftEquipt() {
		return backpackItems[6][0];
	}
	public ItemSlot rightEquipt() {
		return backpackItems[6][1];
	}
	public ItemSlot mouseItem = new ItemSlot();
	public String saveName = "";
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(time);
		out.writeObject(lightMap);
		out.writeObject(dungeonStairs);
		out.writeObject(footPrints);
		out.writeObject(entities);
		out.writeObject(projectiles);
		out.writeObject(damagePosts);
		out.writeObject(particles);
		out.writeObject(precipitationParticles);
		out.writeObject(generatedRegions);
		out.writeObject(itemDrops);
		out.writeObject(lightSources);
		out.writeDouble(dryness);
		out.writeLong(seed);
		out.writeInt(dungeonX);
		out.writeInt(dungeonY);
		out.writeInt(dungeonLevel);
		out.writeInt(lastDungeonLevel);
		out.writeInt(entityID);
		out.writeObject(backpackItems);
		out.writeObject(mouseItem);
		out.writeObject(saveName);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		time = in.readInt();
		lightMap = (Map<String, LightSource>) in.readObject();
		dungeonStairs = (Map<String, int[]>) in.readObject();
		footPrints = (ArrayList<FootPrint>) in.readObject();
		entities = (ArrayList<Entity>) in.readObject();
		projectiles = (ArrayList<Projectile>) in.readObject();
		damagePosts = (ArrayList<DamagePost>) in.readObject();
		particles = (ArrayList<Particle>) in.readObject();
		precipitationParticles = (ArrayList<Particle>) in.readObject();
		generatedRegions = (ArrayList<String>) in.readObject();
		itemDrops = (ArrayList<ItemDrop>) in.readObject();
		lightSources = (ArrayList<LightSource>) in.readObject();
		dryness = in.readDouble();
		seed = in.readLong();
		dungeonX = in.readInt();
		dungeonY = in.readInt();
		dungeonLevel = in.readInt();
		lastDungeonLevel = in.readInt();
		entityID = in.readInt();
		backpackItems = (ItemSlot[][]) in.readObject();
		mouseItem = (ItemSlot) in.readObject();
		saveName = (String) in.readObject();
	}
}