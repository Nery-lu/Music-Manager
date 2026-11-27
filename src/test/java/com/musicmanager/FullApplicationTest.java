
package com.musicmanager;

import database.DatabaseConnection;
import model.User;
import org.junit.jupiter.api.Test;
import repositories.models.UserRepository;
import repositories.relationships.FavoriteMusicRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FullApplicationTest {


    @Test
    void shouldExecuteCompleteApplicationFlow() throws Exception {

        String email = "test-" + UUID.randomUUID() + "@example.com";
        String password = "TestPass123";

        String input = String.join("\n",
                "1",                 // Create account
                email,
                password,
                password,
                "Test User",

                "2",                 // Login
                email,
                password,

                "5",                 // Search
                "Raindance",
                "1",                 // Select first result
                "4",                 // Add to favorites

                "4",                 // Open Favorites
                "3",                 // Remove music
                "Raindance",
                "4",                 // Return to UserMenu

                "2",                 // Create playlist
                "Test Playlist",
                "Test description",

                "1",                 // Open playlists
                "1",                 // Access a playlist
                "Test Playlist",

                "2",                 // Add music
                "Raindance",
                "1",

                "2",                 // Try adding the same music again
                "Raindance",
                "1",

                "3",                 // Remove music
                "Raindance",

                "4",                 // Return from PlaylistMenu

                "3",                 // Delete playlist
                "Test Playlist",

                "6",                 // Logout
                "3"                  // Exit application
        ) + "\n";

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            Scanner scanner = new Scanner(new StringReader(input));

            Main.run(scanner);

        } finally {
            System.setOut(originalOut);

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "DELETE FROM users WHERE email = ?")) {

                statement.setString(1, email);
                statement.executeUpdate();
            }
        }

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("Account created successfully!"));
        assertTrue(result.contains("Login successful!"));
        assertTrue(result.contains("Raindance favorited successfully!"));
        assertTrue(result.contains("Raindance was removed successfully!"));
        assertTrue(result.contains("added successfully!"));
        assertTrue(result.contains("This music is already in the playlist"));
    }
}


