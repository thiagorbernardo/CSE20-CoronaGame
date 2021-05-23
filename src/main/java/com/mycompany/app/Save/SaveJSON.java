package com.mycompany.app.Save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SaveJSON implements SaveDAO {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String filePath = "src/main/resources/save/save.json";

    private FileManager fileManager = new FileManager("src/main/resources/save/save.json", "");

    /**
     * Reading save file
     *
     * @return a Save object
     */
    @Override
    public Save read() {
        this.fileManager.createFile();

        Save save = null;
        try {
            FileReader fileReader = new FileReader(this.filePath, StandardCharsets.UTF_8);

            save = this.gson.fromJson(fileReader, new TypeToken<Save>() {
            }.getType());

            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return save;
    }

    /**
     * Saving a object of save
     *
     * @param save object
     */
    @Override
    public void save(Save save) {
        this.fileManager.saveString(this.gson.toJson(save), false);
    }
}
