package com.musicmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.User;

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
        return loadUsers(USERS_FILE);
    }

    static Map<String, User> loadUsers(Path usersFile) {

        if (!Files.exists(usersFile)) {
            return new HashMap<>();
        }

        Type usersType =
                new TypeToken<Map<String, User>>() {}.getType();

        try (Reader reader = Files.newBufferedReader(
                usersFile, StandardCharsets.UTF_8)) {

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
        saveUsers(users, USERS_FILE);
    }

    static void saveUsers(Map<String, User> users, Path usersFile) {

        try {
            Files.createDirectories(usersFile.getParent());

            try (Writer writer = Files.newBufferedWriter(
                    usersFile, StandardCharsets.UTF_8)) {

                GSON.toJson(users, writer);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Error saving users.json", e);
        }
    }
}
