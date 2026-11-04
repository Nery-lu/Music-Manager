package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MusicCatalog {
    public static Map<String, ArrayList<Music>> loadMusicCatalog() {

        Map<String, ArrayList<Music>> allMusics = new HashMap<>();

        InputStream input =
                MusicCatalog.class.getResourceAsStream("/music-library.json");

        if (input == null) {
            throw new IllegalStateException(
                    "Could not find music-library.json");
        }

        try (Reader reader = new InputStreamReader(
                input, StandardCharsets.UTF_8)) {

            Gson gson = new Gson();

            Type musicListType =
                    new TypeToken<ArrayList<Music>>() {
                    }.getType();

            ArrayList<Music> songs =
                    gson.fromJson(reader, musicListType);

            if (songs != null) {
                for (Music music : songs) {
                    addMusic(allMusics, music);
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Error reading music-library.json", e);
        }

        return allMusics;
    }

    private static void addMusic(
            Map<String, ArrayList<Music>> allMusics,
            Music music) {

        allMusics.computeIfAbsent(
                music.getTitle(),
                title -> new ArrayList<>()
        ).add(music);
    }
}

