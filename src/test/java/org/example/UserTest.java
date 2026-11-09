package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

   private User user;

   @BeforeEach
    void setUp() {

       user = new User();
   }

   @Test
    void shouldCreatePlaylist() {

       boolean result1 = user.createPlaylist("test", "test playlist");

       assertTrue(result1);
   }


}
