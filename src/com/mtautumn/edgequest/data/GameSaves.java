package com.mtautumn.edgequest.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

import com.mtautumn.edgequest.Character;
import com.mtautumn.edgequest.EdgeQuest;
import com.mtautumn.edgequest.Entity;

public class GameSaves {
	public static void saveGame(String saveFile, DataManager dataManager) throws IOException, URISyntaxException {
		try {
			SavableData saveClass = dataManager.savable;
			FileOutputStream fout = new FileOutputStream(getLocal() + saveFile + ".egqst");
			@SuppressWarnings("resource")
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(saveClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadGame(String saveFile, DataManager dataManager) throws ClassNotFoundException, IOException, URISyntaxException {
		FileInputStream fin = new FileInputStream(getLocal() + saveFile + ".egqst");
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(fin);
		SavableData loadedSM = (SavableData) ois.readObject();
		dataManager.savable = loadedSM;
		
		// Set everything up
		Entity characterEntity = null;
		for (int i = 0; i < dataManager.savable.entities.size(); i++) {
			dataManager.savable.entities.get(i).initializeClass(dataManager);
			if (dataManager.savable.entities.get(i).getType() == Entity.EntityType.character) {
				characterEntity = dataManager.savable.entities.get(i);
			}
		}
		dataManager.characterManager.characterEntity = (Character) characterEntity;
		dataManager.system.requestGenUpdate = true;
		dataManager.system.requestScreenUpdate = true;
		
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
