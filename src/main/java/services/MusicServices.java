package services;

import model.Artist;
import model.Music;
import repositories.models.MusicRepository;
import repositories.models.ArtistRepository;
import repositories.relationships.MusicArtistRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class MusicServices {

    private final Connection connection;
    private final MusicRepository musicRepository;
    private final ArtistRepository artistRepository;
    private final MusicArtistRepository musicArtistRepository;

    public MusicServices(
            Connection connection,
            MusicRepository musicRepository,
            ArtistRepository artistRepository,
            MusicArtistRepository musicArtistRepository) {

        this.connection = connection;
        this.musicRepository = musicRepository;
        this.artistRepository = artistRepository;
        this.musicArtistRepository = musicArtistRepository;
    }

    public boolean addMusic(Music music) throws SQLException {

        if (music.getArtist() == null || music.getArtist().isEmpty()) {
            throw new IllegalArgumentException(
                    "Music must have at least one artist."
            );
        }

        boolean previousAutoCommit = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            int musicId = musicRepository.save(music);

            if (musicId == -1) {
                connection.rollback();
                return false;
            }

            for (Artist artist : music.getArtist()) {

                Artist savedArtist =
                        artistRepository.findByName(artist.getName());

                if (savedArtist == null) {
                    int artistId = artistRepository.save(artist);

                    if (artistId == -1) {
                        savedArtist =
                                artistRepository.findByName(artist.getName());

                        if (savedArtist == null) {
                            throw new SQLException(
                                    "Could not register artist: "
                                            + artist.getName()
                            );
                        }
                    } else {
                        savedArtist = artist;
                    }
                }

                if (!musicArtistRepository.exists(savedArtist, music)) {
                    musicArtistRepository.addArtistToMusic(
                            savedArtist, music
                    );
                }
            }

            connection.commit();
            return true;

        } catch (SQLException | RuntimeException e) {

            connection.rollback();
            throw e;

        } finally {
            connection.setAutoCommit(previousAutoCommit);
        }
    }
}
