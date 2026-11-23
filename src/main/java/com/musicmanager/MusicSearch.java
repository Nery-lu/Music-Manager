package com.musicmanager;

import model.Artist;
import model.Music;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MusicSearch {

    public static Set<Music> searchMusic(
            String search,
            Map<String, ArrayList<Music>> allMusics) {

        Set<Music> musicFound = new HashSet<>();

        if (search == null || search.isBlank()) {
            return musicFound;
        }

        for (Map.Entry<String, ArrayList<Music>> entry
                : allMusics.entrySet()) {

            ArrayList<Music> musics = entry.getValue();

            for (Music music : musics) {

                // Searching in titles: prefix only
                String title = music.getTitle();

                if (title != null && search.length() <= title.length()) {

                    boolean matches = true;

                    for (int index = 0; index < search.length(); index++) {

                        if (Character.toLowerCase(title.charAt(index))
                                != Character.toLowerCase(search.charAt(index))) {

                            matches = false;
                            break;
                        }
                    }

                    if (matches) {
                        musicFound.add(music);
                    }
                }

                // Searching in artists: prefix only
                if (music.getArtist() != null) {

                    for (Artist artist : music.getArtist()) {

                        if (artist == null
                                || search.length() > artist.getName().length()) {
                            continue;
                        }

                        boolean matches = true;

                        for (int index = 0; index < search.length(); index++) {

                            if (Character.toLowerCase(artist.getName().charAt(index))
                                    != Character.toLowerCase(search.charAt(index))) {

                                matches = false;
                                break;
                            }
                        }

                        if (matches) {
                            musicFound.add(music);
                            break;
                        }
                    }
                }

                // Searching in lyrics: any position
                String lyrics = music.getLyrics();

                if (lyrics != null
                        && search.length() <= lyrics.length()) {

                    for (int index = 0;
                         index <= lyrics.length() - search.length();
                         index++) {

                        int j = 0;

                        while (j < search.length()
                                && Character.toLowerCase(
                                lyrics.charAt(index + j))
                                == Character.toLowerCase(search.charAt(j))) {

                            j++;
                        }

                        if (j == search.length()) {
                            musicFound.add(music);
                            break;
                        }
                    }
                }
            }
        }

        return musicFound;

    }
}
