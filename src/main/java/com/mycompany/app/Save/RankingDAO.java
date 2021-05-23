package com.mycompany.app.Save;

import java.util.List;

public interface RankingDAO {

    /**
     * Reading a file of ranking
     * @return a list of player ranking completes
     */
    List<Ranking> read();

    /**
     * Getting top 5 players
     * @return a list of top 5 players
     */
    List<Ranking> getTopPlayers();

    /**
     * Saving a ranking
     * @param newRanking new Ranking to append in file
     */
    void save(Ranking newRanking);

}
