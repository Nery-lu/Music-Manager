package com.musicmanager;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

   private User user;
   private String email;
   private String password;
   private String userName;

   @BeforeEach
    void setUp() {

       user = new User(email, password, userName);
   }

   @Test
    void shouldCreatePlaylist() {

       boolean result1 = user.createPlaylist("test", "test playlist");

       assertTrue(result1);
   }


}
