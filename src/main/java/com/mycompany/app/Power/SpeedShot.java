package com.mycompany.app.Power;
import com.mycompany.app.Save.Data;

public class SpeedShot implements Power{
    @Override
    public Data use(Data oldData) {

        oldData.setFireRate(oldData.getFireRate()/2);

        return oldData;
    }

}
