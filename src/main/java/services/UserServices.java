package services;

import model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repositories.models.UserRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class UserServices {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserServices (UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public int createAccount (Scanner scanner) throws SQLException {

        String email;

        System.out.println("\nChoose an Email:");
        System.out.print("Email: ");
        email = scanner.nextLine();

        while (!isValidEmail(email) || userRepository.findByEmail(email) != null) {

            if (userRepository.findByEmail(email) == null) {
                System.out.println(
                        "An account is already registered under this email, please try another one");
            } else {
                System.out.println(
                        "Invalid email, please try another one:");
            }

            System.out.print("Email: ");
            email = scanner.nextLine();
        }

        System.out.println("\nChoose a password");
        System.out.println("Your password must have:");
        System.out.println("- 8 characters or more;");
        System.out.println("- letters and numbers;");
        System.out.println("- At least 1 uppercase letter;");
        System.out.print("\nPassword: ");

        String password = scanner.nextLine();

        while (!isValidPassword(password)) {
            System.out.println("Invalid password, please try another one");
            System.out.print("Password: ");
            password = scanner.nextLine();
        }

        System.out.println("\nConfirm password");
        System.out.print("Password: ");

        String passwordConf = scanner.nextLine();

        while (!passwordConf.equals(password)) {
            System.out.println(
                    "Something went wrong, please confirm if both passwords are the same");
            System.out.print("Password: ");
            passwordConf = scanner.nextLine();
        }

        System.out.println("Now, choose a username for your account");
        System.out.print("Name: ");
        String userName = scanner.nextLine();

        String passwordHash = encoder.encode(password);

        User user = new User(email, passwordHash, userName);

        int userId = userRepository.save(user);

        if (userId != -1) {
            System.out.println("\nAccount created successfully!");
        }

        return userId;
    }

    public User login (Scanner scanner) throws SQLException {

        System.out.println("\nEnter your Email");
        System.out.print("Email: ");

        String email = scanner.nextLine();
        int i = 0;

        while (userRepository.findByEmail(email) == null) {

            i++;

            System.out.println("Email not found, please try again.");

            if (i >= 3) {

                boolean choosingAction = true;

                while (choosingAction) {

                    System.out.println("\nNew here? Create your account now!");
                    System.out.println("1 - Create Account");
                    System.out.println("2 - Continue trying");
                    System.out.print("Option: ");

                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1":
                            createAccount(scanner);
                            return null;

                        case "2":
                            i = 0;
                            choosingAction = false;
                            break;

                        default:
                            System.out.println("Invalid choice, try another one");
                            break;
                    }
                }
            }

            System.out.print("Email: ");
            email = scanner.nextLine();
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        while (!encoder.matches(password, userRepository.findByEmail(email).getPasswordHash())) {

            System.out.println("Wrong password, try again");
            System.out.print("Password: ");
            password = scanner.nextLine();
        }

        System.out.println("\nLogin successful!");
        return userRepository.findByEmail(email);
    }

    private static boolean isValidPassword (String password){
        return password != null
                && password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    private static boolean isValidEmail (String email){
        return email != null
                && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
}
