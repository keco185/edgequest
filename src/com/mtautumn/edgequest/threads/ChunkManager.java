package com.mtautumn.edgequest.threads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.GameSaves;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.Chunk;
import com.mtautumn.edgequest.dataObjects.ChunkLocation;

public class ChunkManager extends Thread {
	private static final boolean verboseMode = false;
	public void run() {
		while (SystemData.running) {
			if (!SystemData.isGameOnLaunchScreen) {
				ensureDirectoryExists();
				updateLoadedChunks();
			}
			try {
				Thread.sleep(SettingsData.tickLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void updateLoadedChunks() {
		ArrayList<ChunkLocation> activeChunks = new ArrayList<>();
		for (int x = SystemData.minTileX - 10; x <= SystemData.maxTileX + 10; x+=10) {
			for (int y = SystemData.minTileY - 10; y <= SystemData.maxTileY + 10; y+=10) {
				activeChunks.add(new ChunkLocation(x, y, DataManager.savable.dungeonLevel));
				activeChunks.add(new ChunkLocation(x, y, DataManager.savable.dungeonLevel + 1));
				if (DataManager.savable.dungeonLevel > -1) {
					activeChunks.add(new ChunkLocation(x, y, DataManager.savable.dungeonLevel - 1));
				}
			}
		}
		if (!SystemData.blockGenerationLastTick) {
			for (String location : DataManager.savable.loadedChunks.keySet()) {
				if (!isLocationActive(location, activeChunks)) {
					unloadChunk(location);
				}
			}
		}
		for (ChunkLocation location : activeChunks) {
			loadChunk(location);
		}

	}
	private static boolean isLocationActive(String location, ArrayList<ChunkLocation> activeChunks) {
		for (ChunkLocation activeLocation : activeChunks) {
			if (activeLocation.name().equals(location)) {
				return true;
			}
		}
		return false;
	}
	private static void unloadChunk(String location) {
		if (DataManager.savable.loadedChunks.containsKey(location)) {
			Chunk chunk = DataManager.savable.loadedChunks.get(location);
			if (!chunk.inUse) {
				if (verboseMode) System.out.println("Saving chunk: " + location);
				saveChunk(chunk, location);
				DataManager.savable.loadedChunks.remove(location);
			}
		}
	}
	private static void saveChunk(Chunk chunk, String location) {
		try {
			FileOutputStream fout = new FileOutputStream(GameSaves.getLocal() + "world_" + DataManager.savable.saveName + "/" + location);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(chunk);
			oos.close();
			fout.close();
			if (verboseMode) System.out.println("Chunk saved: " + location);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveChunks() {
		for (String location : DataManager.savable.loadedChunks.keySet()) {
			saveChunk(DataManager.savable.loadedChunks.get(location), location);
		}
	}
	public static void loadChunk(ChunkLocation location) {
		if (!isChunkLoaded(location)) {
			if (doesSavedChunkExist(location)) {
				if (verboseMode) System.out.println("Loading chunk: " + location.name());
				openChunkFile(location);
				if (verboseMode) System.out.println("Chunk loaded: " + location.name());
			} else {
				if (verboseMode) System.out.println("Creating chunk: " + location.name());
				DataManager.savable.loadedChunks.put(location.name(), new Chunk(location.x(),location.y(),location.level()));
				if (verboseMode) System.out.println("Chunk created: " + location.name());

			}
		}
	}
	private static boolean isChunkLoaded(ChunkLocation location) {
		String locale = location.name();
		for (String loadedLocation : DataManager.savable.loadedChunks.keySet()) {
			if (loadedLocation.equals(locale)) {
				return true;
			}
		}
		return false;
	}
	private static boolean doesSavedChunkExist(ChunkLocation location) {
		try {
			File chunkFile = new File(GameSaves.getLocal() + "world_" + DataManager.savable.saveName + "/" + location.name());
			return chunkFile.exists();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}
	private static void openChunkFile(ChunkLocation location) {
		try {
			FileInputStream fin = new FileInputStream(GameSaves.getLocal() + "world_" + DataManager.savable.saveName + "/" + location.name());
			ObjectInputStream ois = new ObjectInputStream(fin);
			Chunk loadedChunk = (Chunk) ois.readObject();
			DataManager.savable.loadedChunks.put(location.name(), loadedChunk);
			ois.close();
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private static void ensureDirectoryExists() {
		try {
			File dir = new File(GameSaves.getLocal() + "world_" + DataManager.savable.saveName);

			if (!dir.exists()) {
				if (verboseMode) System.out.println("creating directory: " + dir.getName());
				dir.mkdir();       
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Chunk getChunk(ChunkLocation location) {
		if (!isChunkLoaded(location)) {
			loadChunk(location);
		}
		if (DataManager.savable.loadedChunks.containsKey(location.name())) {
			return DataManager.savable.loadedChunks.get(location.name());
		}
		if (verboseMode) System.out.println("Chunk not found: " + location);
		return new Chunk(location.x(),location.y(),location.level());

	}
}
