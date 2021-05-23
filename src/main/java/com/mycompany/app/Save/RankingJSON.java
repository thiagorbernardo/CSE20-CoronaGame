package com.mycompany.app.Save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RankingJSON implements RankingDAO {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String filePath = "src/main/resources/ranking/ranking.json";

    private FileManager fileManager = new FileManager("src/main/resources/ranking/ranking.json", "[]");

    /**
     * Reading a file of ranking
     *
     * @return a list of player ranking completes
     */
    @Override
    public List<Ranking> read() {

        this.fileManager.createFile();

        List<Ranking> rankingList = null;
        try {
            FileReader fileReader = new FileReader(this.filePath, StandardCharsets.UTF_8);

            rankingList = this.gson.fromJson(fileReader, new TypeToken<List<Ranking>>() {
            }.getType());

            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rankingList;
    }

    /**
     * Getting top 5 players
     *
     * @return a list of top 5 players
     */
    @Override
    public List<Ranking> getTopPlayers() {

        List<Ranking> rankingList = this.read();

        rankingList.sort(Comparator.comparingDouble(o -> -o.points));

        if (rankingList.size() > 5)
            return rankingList.subList(0, 5);

        return rankingList;
    }

    /**
     * Saving a ranking
     *
     * @param newRanking new Ranking to append in file
     */
    @Override
    public void save(Ranking newRanking) {
        List<Ranking> rankingList = this.read();

        rankingList.add(newRanking);

        this.fileManager.saveString(this.gson.toJson(rankingList), false);
    }
}
