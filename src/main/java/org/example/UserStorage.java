package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {

    private static final Path USERS_FILE =
            Paths.get("data", "users.json");

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();


    // Loads the users from the JSON file
    public static Map<String, User> loadUsers() {

        if (!Files.exists(USERS_FILE)) {
            return new HashMap<>();
        }

        Type usersType =
                new TypeToken<Map<String, User>>() {}.getType();

        try (Reader reader = Files.newBufferedReader(
                USERS_FILE, StandardCharsets.UTF_8)) {

            Map<String, User> users =
                    GSON.fromJson(reader, usersType);

            return users != null ? users : new HashMap<>();

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Error reading users.json", e);
        }
    }


    // Saves the current users to the JSON file
    public static void saveUsers(Map<String, User> users) {

        try {
            Files.createDirectories(USERS_FILE.getParent());

            try (Writer writer = Files.newBufferedWriter(
                    USERS_FILE, StandardCharsets.UTF_8)) {

                GSON.toJson(users, writer);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Error saving users.json", e);
        }
    }
}
