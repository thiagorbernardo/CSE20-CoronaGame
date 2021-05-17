package com.mycompany.app.Save;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private String path;
    private String header;

    public FileManager(String path, String header) {
        this.path = path;
        this.header = header;
    }

    public void createFile() {
        File file = new File(this.path);

        if (!file.exists()) {
            try {
                if(!file.getParentFile().exists())
                    file.getParentFile().mkdirs();

                file.createNewFile();

                FileWriter fileWriter = new FileWriter(this.path, StandardCharsets.UTF_8);

                fileWriter.write(this.header);

                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveString(String content, Boolean append) {
        this.createFile();
        try {
            FileWriter fileWriter = new FileWriter(this.path, StandardCharsets.UTF_8, append);

            fileWriter.append(content);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
