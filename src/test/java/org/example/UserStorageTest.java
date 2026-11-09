package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {

    @TempDir
    Path tempDir;

    private Path usersFile;

    @BeforeEach
    void setUp() {
        usersFile = tempDir.resolve("users.json");
    }

    @Test
    void shouldReturnEmptyMapWhenFileDoesNotExist() {


        Map<String, User> users = UserStorage.loadUsers(usersFile);



        assertNotNull(users);
        assertTrue(users.isEmpty());
    }


    @Test
    void shouldReturnEmptyMapWhenFileIsEmpty() throws IOException {


        Files.createFile(usersFile);


        Map<String, User> users = UserStorage.loadUsers(usersFile);


        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void shouldReturnEmptyMapWhenJsonContainsNull() throws IOException {


        Files.writeString(usersFile, "null", StandardCharsets.UTF_8);


        Map<String, User> users = UserStorage.loadUsers(usersFile);


        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void shouldSaveEmptyMapSuccessfully() throws IOException {


        Map<String, User> users = new HashMap<>();


        UserStorage.saveUsers(users, usersFile);


        assertTrue(Files.exists(usersFile));

        String content = Files.readString(usersFile, StandardCharsets.UTF_8);

        assertEquals("{}", content.trim());

        Map<String, User> loadedUsers = UserStorage.loadUsers(usersFile);

        assertTrue(loadedUsers.isEmpty());
    }

}
