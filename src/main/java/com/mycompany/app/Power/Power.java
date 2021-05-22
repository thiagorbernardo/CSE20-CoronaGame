package com.mycompany.app.Power;

import com.mycompany.app.Save.Data;


public interface Power {
    Data use(Data oldData);
    void executionTime();
}
