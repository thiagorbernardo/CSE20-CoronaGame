package com.mycompany.app.Save;

import java.util.List;

public interface RankingDAO {
    List<Ranking> read();

    List<Ranking> getTopPlayers();

    void save(Ranking newRanking);

}
