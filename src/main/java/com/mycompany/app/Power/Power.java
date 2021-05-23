package com.mycompany.app.Power;

import com.mycompany.app.Save.Data;

public interface Power {
    /**
     * Using a power, Polimorphism
     * @param oldData player old data to modify
     * @return a data modified
     */
    Data use(Data oldData);
}
