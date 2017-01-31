package com.mtautumn.edgequest.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mtautumn.edgequest.DamagePost;
import com.mtautumn.edgequest.Entity;
import com.mtautumn.edgequest.FootPrint;
import com.mtautumn.edgequest.ItemDrop;
import com.mtautumn.edgequest.ItemSlot;
import com.mtautumn.edgequest.Particle;
import com.mtautumn.edgequest.projectiles.Projectile;

public class SavableData implements Serializable {
	private static final long serialVersionUID = 1L;
	public int time = 800;
	public Map<String, Short> map = new ConcurrentHashMap<String, Short>(60000);
	public Map<String, Byte> lightMap = new ConcurrentHashMap<String, Byte>();
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
	public double dryness = 0.5;
	public long seed = 7;
	public int dungeonX = 0;
	public int dungeonY = 0;
	public int dungeonLevel = -1;
	public int lastDungeonLevel = -1;
	public int entityID = 0;
	public int hotBarSelection = 0;
	public ItemSlot[][] backpackItems = new ItemSlot[8][6];
	public ItemSlot mouseItem = new ItemSlot();
	public String saveName = "";
}