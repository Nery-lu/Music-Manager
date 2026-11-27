package services;

import database.DatabaseConnection;
import model.User;
import org.junit.jupiter.api.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repositories.models.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class UserServicesTest {

    private Connection connection;
    private UserRepository userRepository;
    private UserServices userServices;
    private BCryptPasswordEncoder encoder;
    private int createdUserId;

    private User existingUser;

    private final ByteArrayOutputStream output =
            new ByteArrayOutputStream();

    private PrintStream originalOut;

    @BeforeEach
    void setUp() throws Exception {
        connection = DatabaseConnection.getConnection();

        userRepository = new UserRepository(connection);
        encoder = new BCryptPasswordEncoder();
        userServices = new UserServices(userRepository, encoder);

        originalOut = System.out;
        System.setOut(new PrintStream(output));

        String email = "existing_" + System.nanoTime() + "@test.com";

        existingUser = new User(
                email,
                encoder.encode("Existing123"),
                "Existing User"
        );

        existingUser.setId(userRepository.save(existingUser));
    }

    @AfterEach
    void tearDown() throws Exception {
        System.setOut(originalOut);

        if (existingUser != null && existingUser.getId() > 0) {
            userRepository.delete(existingUser.getId());
        }

        if (createdUserId > 0) {
            userRepository.delete(createdUserId);
        }

        connection.close();
    }


    @Test
    void shouldRejectDuplicateEmailAndCreateAccountWithNewEmail() throws Exception {

        String newEmail = "new_" + System.nanoTime() + "@test.com";

        String input = existingUser.getEmail() + "\n"
                + newEmail + "\n"
                + "ValidPass123\n"
                + "ValidPass123\n"
                + "New User\n";

        Scanner scanner = new Scanner(input);

        int userId = userServices.createAccount(scanner);

        createdUserId = userId;

        assertTrue(userId > 0);

        User savedUser = userRepository.findByEmail(newEmail);

        assertNotNull(savedUser);
        assertEquals("New User", savedUser.getName());

        assertTrue(output.toString().contains(
                "An account is already registered under this email"
        ));

        assertTrue(encoder.matches(
                "ValidPass123",
                savedUser.getPasswordHash()
        ));

        userRepository.delete(userId);
    }


    @Test
    void shouldRejectInvalidEmailAndAcceptValidEmail() throws SQLException {

        String newEmail = "valid_" + System.nanoTime() + "@test.com";

        String input = "invalid-email\n"
                + newEmail + "\n"
                + "ValidPass123\n"
                + "ValidPass123\n"
                + "New User\n";

        Scanner scanner = new Scanner(input);

        int userId = userServices.createAccount(scanner);

        createdUserId = userId;

        assertTrue(userId > 0);

        User savedUser = userRepository.findByEmail(newEmail);

        assertNotNull(savedUser);
        assertEquals("New User", savedUser.getName());

        assertTrue(output.toString().contains(
                "Invalid email, please try another one"
        ));

        userRepository.delete(userId);
    }

    @Test
    void shouldRejectInvalidPasswordAndAcceptValidPassword() throws SQLException {

        String newEmail = "password_" + System.nanoTime() + "@test.com";

        String input = newEmail + "\n"
                + "invalid\n"
                + "ValidPass123\n"
                + "ValidPass123\n"
                + "New User\n";

        Scanner scanner = new Scanner(input);

        int userId = userServices.createAccount(scanner);
        createdUserId = userId;

        assertTrue(userId > 0);

        User savedUser = userRepository.findByEmail(newEmail);

        assertNotNull(savedUser);

        assertTrue(output.toString().contains(
                "Invalid password, please try another one"
        ));
    }

    @Test
    void shouldRejectDifferentPasswordConfirmation() throws SQLException {

        String newEmail = "confirmation_" + System.nanoTime() + "@test.com";

        String input = newEmail + "\n"
                + "ValidPass123\n"
                + "Different123\n"
                + "ValidPass123\n"
                + "New User\n";

        Scanner scanner = new Scanner(input);

        int userId = userServices.createAccount(scanner);
        createdUserId = userId;

        assertTrue(userId > 0);

        User savedUser = userRepository.findByEmail(newEmail);

        assertNotNull(savedUser);

        assertTrue(output.toString().contains(
                "Something went wrong, please confirm if both passwords are the same"
        ));
    }

    @Test
    void shouldRejectUnknownEmailAndAcceptExistingEmail() throws SQLException {

        String input = "unknown@test.com\n"
                + existingUser.getEmail() + "\n"
                + "Existing123\n";

        Scanner scanner = new Scanner(input);

        User loggedUser = userServices.login(scanner);

        assertNotNull(loggedUser);
        assertEquals(existingUser.getId(), loggedUser.getId());

        assertTrue(output.toString().contains(
                "Email not found, please try again."
        ));
    }

    @Test
    void shouldRejectWrongPasswordAndAcceptCorrectPassword() throws SQLException {

        String input = existingUser.getEmail() + "\n"
                + "WrongPassword123\n"
                + "Existing123\n";

        Scanner scanner = new Scanner(input);

        User loggedUser = userServices.login(scanner);

        assertNotNull(loggedUser);
        assertEquals(existingUser.getId(), loggedUser.getId());

        assertTrue(output.toString().contains(
                "Wrong password, try again"
        ));
    }


    @Test
    void shouldAllowRetryAfterThreeUnknownEmails() throws SQLException {

        String input = "unknown1@test.com\n"
                + "unknown2@test.com\n"
                + "unknown3@test.com\n"
                + "2\n"
                + existingUser.getEmail() + "\n"
                + "Existing123\n";

        Scanner scanner = new Scanner(input);

        User loggedUser = userServices.login(scanner);

        assertNotNull(loggedUser);
        assertEquals(existingUser.getId(), loggedUser.getId());

        String result = output.toString();

        assertTrue(result.contains("Email not found, please try again."));
        assertTrue(result.contains("New here? Create your account now!"));
        assertTrue(result.contains("Login successful!"));
    }


    @Test
    void shouldAllowAccountCreationAfterThreeUnknownEmails() throws SQLException {

        String newEmail = "login_create_" + System.nanoTime() + "@test.com";

        String input = "unknown1@test.com\n"
                + "unknown2@test.com\n"
                + "unknown3@test.com\n"
                + "1\n"
                + newEmail + "\n"
                + "ValidPass123\n"
                + "ValidPass123\n"
                + "New User\n";

        Scanner scanner = new Scanner(input);

        User loggedUser = userServices.login(scanner);

        assertNull(loggedUser);

        User savedUser = userRepository.findByEmail(newEmail);

        assertNotNull(savedUser);
        assertEquals("New User", savedUser.getName());

        assertTrue(output.toString().contains(
                "New here? Create your account now!"
        ));

        assertTrue(output.toString().contains(
                "Account created successfully!"
        ));

        createdUserId = savedUser.getId();
    }

}
