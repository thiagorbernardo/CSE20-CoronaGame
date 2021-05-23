package com.mycompany.app.Save;

public interface SaveDAO {
    /**
     * Reading save file
     * @return a Save object
     */
    Save read();

    /**
     * Saving a object of save
     * @param save object
     */
    void save(Save save);
}
