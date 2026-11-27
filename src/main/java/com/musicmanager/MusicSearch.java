package com.musicmanager;

import model.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicSearch {

    public static List<Music> searchMusic(
            String search,
            Map<Integer, Music> allMusics) {

        List<Music> musicFound = new ArrayList<>();

        if (search == null || search.isBlank()) {
            return musicFound;
        }

        String searchTerm = search.toLowerCase().trim();

        for (Music music : allMusics.values()) {

            boolean matchesTitle =
                    music.getTitle() != null &&
                            music.getTitle().toLowerCase().contains(searchTerm);

            boolean matchesArtist =
                    music.getArtist() != null &&
                            music.getArtist().stream()
                                    .anyMatch(artist ->
                                            artist != null &&
                                                    artist.getName() != null &&
                                                    artist.getName().toLowerCase().contains(searchTerm)
                                    );

            boolean matchesLyrics =
                    music.getLyrics() != null &&
                            music.getLyrics().toLowerCase().contains(searchTerm);

            if (matchesTitle || matchesArtist || matchesLyrics) {
                musicFound.add(music);
            }
        }

        return musicFound;
    }
}