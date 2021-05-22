package com.mycompany.app.Save;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RankingTXT implements RankingDAO {
    private String filePath = "src/main/resources/ranking/ranking.txt";

    private FileManager fileManager = new FileManager("src/main/resources/ranking/ranking.txt", "");

    @Override
    public List<Ranking> read() {

        this.fileManager.createFile();

        List<Ranking> rankingList = new ArrayList<>();
        try {
            File file = new File(this.filePath);

            FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);

            BufferedReader br = new BufferedReader(fileReader);

            Pattern rankingRegex = Pattern.compile("(.+),(\\d+)");

            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = rankingRegex.matcher(line);
                if (matcher.find())
                    rankingList.add(new Ranking(matcher.group(1), Double.parseDouble(matcher.group(2))));

            }
            fileReader.close();
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

        StringBuilder rankingComplete = new StringBuilder();

        rankingComplete.append(newRanking.name).append(",").append(newRanking.points).append("\n");

        this.fileManager.saveString(rankingComplete.toString(), true);
    }

}
