package com.mycompany.app.Save;

import com.mycompany.app.Power.PowerType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveTXT implements SaveDAO {
    private String filePath = "src/main/resources/save/save.txt";

    private FileManager fileManager = new FileManager("src/main/resources/save/save.txt", "");

    private Map<String, String> saveMap = new HashMap<>();

    /**
     * Reading save file
     *
     * @return a Save object
     */
    @Override
    public Save read() {
        this.fileManager.createFile();

        try {
            File file = new File(this.filePath);

            FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);

            BufferedReader br = new BufferedReader(fileReader);

            Pattern saveRegex = Pattern.compile("(.+):(.+)");

            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = saveRegex.matcher(line);
                if (matcher.find()) {
                    this.saveMap.put(matcher.group(1), matcher.group(2));
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.mapToSave();
    }

    /**
     * Saving a object of save
     *
     * @param save a Save object
     */
    @Override
    public void save(Save save) {
        StringBuilder savingString = new StringBuilder();

        /* Saving world data*/
        savingString.append("createdAt").append(":").append(save.createdAt).append("\n");
        savingString.append("isMultiplayer").append(":").append(save.isMultiplayer).append("\n");
        savingString.append("level").append(":").append(save.level).append("\n");

        savingString.append("P1Data.points").append(":").append(save.P1Data.getPoints()).append("\n");
        savingString.append("P1Data.speed").append(":").append(save.P1Data.getSpeed()).append("\n");
        if (save.P1Data.getActivePower() != null)
            savingString.append("P1Data.activePower").append(":").append(save.P1Data.getActivePower()).append("\n");
        savingString.append("P1Data.lastShot").append(":").append(save.P1Data.getLastShot()).append("\n");
        savingString.append("P1Data.life").append(":").append(save.P1Data.getLife()).append("\n");
        savingString.append("P1Data.fireRate").append(":").append(save.P1Data.getFireRate()).append("\n");
        savingString.append("P1Data.isInvincible").append(":").append(save.P1Data.getInvincibility()).append("\n");

        if (save.P2Data != null) {
            savingString.append("P2Data.points").append(":").append(save.P2Data.getPoints()).append("\n");
            savingString.append("P2Data.speed").append(":").append(save.P2Data.getSpeed()).append("\n");
            if (save.P2Data.getActivePower() != null)
                savingString.append("P2Data.activePower").append(":").append(save.P2Data.getActivePower()).append("\n");
            savingString.append("P2Data.lastShot").append(":").append(save.P2Data.getLastShot()).append("\n");
            savingString.append("P2Data.life").append(":").append(save.P2Data.getLife()).append("\n");
            savingString.append("P2Data.fireRate").append(":").append(save.P2Data.getFireRate()).append("\n");
            savingString.append("P2Data.isInvincible").append(":").append(save.P2Data.getInvincibility()).append("\n");
        }

        this.fileManager.saveString(savingString.toString(), false);
    }

    /**
     * Creating a save object with a map of player data based on a map of string
     * @return a Save object
     */
    private Save mapToSave() {
        if (this.saveMap.size() == 0)
            return null;

        Save save = new Save(false, 1, null);

        save.setCreatedAt(Long.parseLong(this.saveMap.get("createdAt")));
        save.setMultiplayer(Boolean.parseBoolean(this.saveMap.get("isMultiplayer")));
        save.setLevel(Integer.parseInt(this.saveMap.get("level")));

        Data p1Data = new Data(
                Double.parseDouble(this.saveMap.get("P1Data.points")),
                Integer.parseInt(this.saveMap.get("P1Data.speed")),
                this.saveMap.get("P1Data.activePower") != null ? PowerType.valueOf(this.saveMap.get("P1Data.activePower")) : null,
                Double.parseDouble(this.saveMap.get("P1Data.lastShot")),
                Integer.parseInt(this.saveMap.get("P1Data.life")),
                Double.parseDouble(this.saveMap.get("P1Data.fireRate")),
                Boolean.parseBoolean(this.saveMap.get("P1Data.isInvincible"))
        );

        save.setP1Data(p1Data);

        if (save.isMultiplayer) {
            Data p2Data = new Data(
                    Double.parseDouble(this.saveMap.get("P2Data.points")),
                    Integer.parseInt(this.saveMap.get("P2Data.speed")),
                    this.saveMap.get("P2Data.activePower") != null ? PowerType.valueOf(this.saveMap.get("P2Data.activePower")) : null,
                    Double.parseDouble(this.saveMap.get("P2Data.lastShot")),
                    Integer.parseInt(this.saveMap.get("P2Data.life")),
                    Double.parseDouble(this.saveMap.get("P2Data.fireRate")),
                    Boolean.parseBoolean(this.saveMap.get("P2Data.isInvincible"))
            );

            save.setP2Data(p2Data);
        }

        return save;
    }
}
