package com.mtautumn.edgequest.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

import com.mtautumn.edgequest.EdgeQuest;
import com.mtautumn.edgequest.entities.Character;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.threads.ChunkManager;

public class GameSaves {
	public static void saveGame() throws IOException, URISyntaxException {
		try {
			SavableData saveClass = DataManager.savable;
			FileOutputStream fout = new FileOutputStream(getLocal() + "world_" + DataManager.savable.saveName + "/world.egqst");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(saveClass);
			oos.close();
			fout.close();
			ChunkManager.saveChunks();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadGame(String saveFile) throws ClassNotFoundException, IOException, URISyntaxException {
		FileInputStream fin = new FileInputStream(getLocal() + "world_" + saveFile + "/world.egqst");
		ObjectInputStream ois = new ObjectInputStream(fin);
		SavableData loadedSM = (SavableData) ois.readObject();
		DataManager.savable = loadedSM;
		
		// Set everything up
		Entity characterEntity = null;
		for (int i = 0; i < DataManager.savable.entities.size(); i++) {
			DataManager.savable.entities.get(i).initializeClass();
			if (DataManager.savable.entities.get(i).getType() == Entity.EntityType.character) {
				characterEntity = DataManager.savable.entities.get(i);
			}
		}
		CharacterManager.characterEntity = (Character) characterEntity;
		SystemData.requestGenUpdate = true;
		SystemData.requestScreenUpdate = true;
		ois.close();
		fin.close();
		
	}
	public static String getLocal() throws URISyntaxException {
		String baseLocal = EdgeQuest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		if (baseLocal.substring(baseLocal.length() - 1).equals("/") || baseLocal.substring(baseLocal.length() - 1).equals("\\")) {
			baseLocal = baseLocal.substring(0, baseLocal.length() - 1);
		}
		boolean removing = true;
		while (removing) {
			if (!baseLocal.substring(baseLocal.length() - 1).equals("/") && !baseLocal.substring(baseLocal.length() - 1).equals("\\")) {
				baseLocal = baseLocal.substring(0, baseLocal.length() - 1);
			} else {
				removing = false;
			}
		}
		if (baseLocal.substring(baseLocal.length() - 1).equals("/") || baseLocal.substring(baseLocal.length() - 1).equals("\\")) {
			baseLocal = baseLocal.substring(0, baseLocal.length() - 1);
		}
		removing = true;
		while (removing) {
			if (!baseLocal.substring(baseLocal.length() - 1).equals("/") && !baseLocal.substring(baseLocal.length() - 1).equals("\\")) {
				baseLocal = baseLocal.substring(0, baseLocal.length() - 1);
			} else {
				removing = false;
			}
		}
		File saveDir = new File(baseLocal + "saves/");
		if (!saveDir.exists()) {
		    System.out.println("creating save folder");
		    try{
		        saveDir.mkdir();
		    } 
		    catch(SecurityException se){
		    	System.err.println("Unable to create save folder at: " + baseLocal + "saves/");
		    }        
		}
		return baseLocal + "saves/";
	}
}
