package com.mycompany.app.Power;
import com.mycompany.app.Save.Data;
public class MoveSpeed implements Power {

    private Data data;

    @Override
    public Data use(Data oldData) {
        oldData.setSpeed(oldData.getSpeed()*2);
        return oldData;
    }

    @Override
    public void executionTime() {

    }
}
