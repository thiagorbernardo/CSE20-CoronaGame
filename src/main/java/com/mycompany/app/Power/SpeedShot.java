package com.mycompany.app.Power;

import com.mycompany.app.Save.Data;

public class SpeedShot implements Power{
    /**
     * Using a power, Polimorphism
     *
     * @param oldData player old data to modify
     * @return a data modified
     */
    @Override
    public Data use(Data oldData) {
        oldData.setFireRate(oldData.getFireRate() - 70);
        return oldData;
    }
}
