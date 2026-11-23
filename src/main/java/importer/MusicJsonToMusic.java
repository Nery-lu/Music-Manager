package importer;
import model.Artist;
import model.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicJsonToMusic {

    public static Music fromMusicJsonToMusic(MusicJson musicJson) {

        List<Artist> artists = new ArrayList<>();

        for (int i = 0; i < musicJson.getArtist().size(); i++) {
            Artist artist = new Artist(musicJson.getArtist().get(i));
            artists.add(artist);
        }

        Music music = new Music(musicJson.getTitle(), artists, musicJson.getLyrics(),  musicJson.getDuration());
        music.setAlbum(musicJson.getAlbum());

        return music;
    }
}
