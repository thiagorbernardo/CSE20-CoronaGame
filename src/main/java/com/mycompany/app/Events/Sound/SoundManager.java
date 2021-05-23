package com.mycompany.app.Events.Sound;


import com.almasb.fxgl.app.services.FXGLAssetLoaderService;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SoundManager implements SoundListener {

    private FXGLAssetLoaderService loaderService = FXGL.getAssetLoader();
    private AudioPlayer audioPlayer = FXGL.getAudioPlayer();
    Map<SoundNames, Sound> soundMap = new HashMap<>();
    Map<MusicsNames, Music> musicMap = new HashMap<>();

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

    @Override
    public void loadMusics(List<MusicsNames> musics) {
        for (MusicsNames music : musics) {
            Music musicLoaded = this.loaderService.loadMusic(music + ".mp3");
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


    @Override
    public void stopAll() {
        this.audioPlayer.stopAllSoundsAndMusic();
    }

    @Override
    public void stopAllSounds() {
        this.audioPlayer.stopAllSounds();
    }

    @Override
    public void stopAllMusics() {
        this.audioPlayer.stopAllMusic();
    }

    @Override
    public void pauseMusics() {
        this.audioPlayer.pauseAllMusic();
    }
}
