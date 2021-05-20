package com.mycompany.app.Events.Sound;

import java.util.List;

public interface SoundListener {
    void playSound(SoundNames sound);

    void loadSounds(List<SoundNames> sounds);

    void playMusic(MusicsNames music);

    void loadMusics(List<MusicsNames> musics);

    void stopAll();

    void stopAllSounds();

    void stopAllMusics();

    void pauseMusics();
}
