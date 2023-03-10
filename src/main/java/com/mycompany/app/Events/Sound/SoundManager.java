package com.mycompany.app.Events.Sound;


import com.almasb.fxgl.app.services.FXGLAssetLoaderService;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundManager implements SoundListener {

    private FXGLAssetLoaderService loaderService = FXGL.getAssetLoader();
    private AudioPlayer audioPlayer = FXGL.getAudioPlayer();
    private Map<SoundNames, Sound> soundMap = new HashMap<>();
    private Map<MusicsNames, Music> musicMap = new HashMap<>();

    /**
     * Play a sound using FXGL audio player
     * @param soundName sound to play
     */
    @Override
    public void playSound(SoundNames soundName) {
        this.audioPlayer.playSound(this.soundMap.get(soundName));
    }

    /**
     * Load a a list of sounds to FXGL asset loader
     * @param sounds List to load
     */
    @Override
    public void loadSounds(List<SoundNames> sounds) {
        for (SoundNames sound : sounds) {
            Sound soundLoaded = this.loaderService.loadSound(sound.name().toLowerCase() + ".wav");
            this.soundMap.put(sound, soundLoaded);
        }
    }

    /**
     * Load a a list of musics to FXGL asset loader
     * @param musics List to load
     */
    @Override
    public void loadMusics(List<MusicsNames> musics) {
        for (MusicsNames music : musics) {
            Music musicLoaded = this.loaderService.loadMusic(music.name().toLowerCase() + ".mp3");
            this.musicMap.put(music, musicLoaded);
        }
    }

    /**
     * Play a music using FXGL audio player
     * @param musicName music to play
     */
    @Override
    public void playMusic(MusicsNames musicName) {
        this.audioPlayer.playMusic(this.musicMap.get(musicName));
    }

    /**
     * Stop all current sounds and musics
     */
    @Override
    public void stopAll() {
        this.audioPlayer.stopAllSoundsAndMusic();
    }

    /**
     * Stop all current sounds
     */
    @Override
    public void stopAllSounds() {
        this.audioPlayer.stopAllSounds();
    }

    /**
     * Stop all current musics
     */
    @Override
    public void stopAllMusics() {
        this.audioPlayer.stopAllMusic();
    }

    /**
     * Pause a current music
     */
    @Override
    public void pauseMusics() {
        this.audioPlayer.pauseAllMusic();
    }
}
