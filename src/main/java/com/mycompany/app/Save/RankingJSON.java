package com.mycompany.app.Save;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RankingJSON implements RankingDAO {
    private Gson gson = new Gson();
    private String filePath = new File("").getAbsolutePath() + "/src/main/resources/ranking/ranking.json";
    private FileReader fileReader;

    @Override
    public List<Ranking> read() {

        this.setFileReader();

        List<Ranking> rankingList = this.gson.fromJson(this.fileReader, new TypeToken<List<Ranking>>() {
        }.getType());

        try {
            this.fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rankingList;
    }

    @Override
    public List<Ranking> getTopPlayers() {

        List<Ranking> rankingList = this.read();

        rankingList.sort(Comparator.comparingDouble(o -> -o.points));

        if (rankingList.size() > 5)
            return rankingList.subList(0, 5);

        return rankingList;
    }

    @Override
    public void save(Ranking newRanking) {
        List<Ranking> rankingList = this.read();

        rankingList.add(newRanking);

        try {
            FileWriter fileWriter = new FileWriter(this.filePath, StandardCharsets.UTF_8);
            fileWriter.append(this.gson.toJson(rankingList));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFileReader() {
        try {
            this.fileReader = new FileReader(this.filePath, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
