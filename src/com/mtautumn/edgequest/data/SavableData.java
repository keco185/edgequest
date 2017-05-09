package com.mtautumn.edgequest.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mtautumn.edgequest.DamagePost;
import com.mtautumn.edgequest.dataObjects.FootPrint;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.particles.Particle;
import com.mtautumn.edgequest.projectiles.Projectile;

public class SavableData implements Serializable {
	private static final long serialVersionUID = 1L;
	public int time = 800;
	public Map<String, Short> map = new ConcurrentHashMap<String, Short>(60000);
	public Map<String, LightSource> lightMap = new ConcurrentHashMap<String, LightSource>();
	public Map<String, Short> playerStructuresMap = new ConcurrentHashMap<String, Short>();
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
	//public int hotBarSelection = 0;
	public ItemSlot[][] backpackItems = new ItemSlot[7][6];
	public ItemSlot leftEquipt() {
		return backpackItems[6][0];
	}
	public ItemSlot rightEquipt() {
		return backpackItems[6][1];
	}
	public ItemSlot mouseItem = new ItemSlot();
	public String saveName = "";
}