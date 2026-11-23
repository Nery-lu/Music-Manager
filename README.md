# Music Manager

Music Manager is a Java-based application designed to provide a complete music management
experience. It allows users to manage their personal music libraries, create and organize
playlists, manage favorite songs, search for music and artists, and securely manage their
accounts.

## Features:

### ***User Management:***

- User registration and login;
- Password hashing using BCrypt;
- Persistent user data storage(initially with
JSON and, in the future, with a relational database);

### ***Music Management:***

- Load and manage music catalog;
- Organize music by title and artists;
- Store music's information, such as duration and lyrics;

### ***Playlist and Favorites:***

- Create and manage personal playlists;
- Add and remove songs from playlists;
- favorite and unfavorite songs
(add and remove them from user's Favorites playlist);

### ***Search***

- Search songs by title (prefix-based search);
- Search songs by artists (prefix-based search);
- Search songs by lyrics (substring search);
- Case-insensitive search across all search criteria;


## Technologies Used:

- Java 21 - Main programming language;
- Maven - Dependency management and build automation;
- Gson - JSON parsing and data persistence;
- PostgreSQL - Relational database;
- JDBC - Database connectivity;
- Spring Security Crypto - Password hashing using BCrypt;
- JUnit 6 - Unit testing;
- 

## Project Structure

```text
Music-Manager/
├── database/
│   └── schema.sql         # PostgreSQL database schema
├── src/
│   ├── main/
│   │   ├── java/          # Application source code
│   │   └── resources/     # Music catalog JSON file
│   └── test/
│       └── java/          # Unit tests
├── pom.xml                # Maven configuration and dependencies
└── README.md              # Project documentation
```

## Database

The project includes a PostgreSQL database schema and a JDBC connection utility.

- **PostgreSQL:** The database schema defines tables for users, music, artists, playlists, favorites, and their relationships.
- **JDBC:** A connection utility is implemented to establish connections to the PostgreSQL database.
- **JSON Persistence:** User data and the music catalog are currently stored and managed using JSON files.

The PostgreSQL database structure and connection 
setup are available
(It's what I'm working on at the moment);


## Getting Started

### Prerequisites

Before running the project, make sure you have installed:

- **Java 21** or later.
- **Maven** (or use the Maven Wrapper included in the project).

### Cloning the Repository

```bash
git clone https://github.com/Nery-lu/Music-Manager.git
cd Music-Manager
```

### Running the Application

From the project's root directory, run:

```powershell
mvn exec:java "-Dexec.mainClass=com.musicmanager.Main"
```

## Running Tests

The project uses **JUnit 6** for unit testing.

To run the test suite, execute the following command from the project's root directory:

```bash
mvn clean test
```

Maven will compile the project, execute the automated tests, and display the test results in the terminal.


Disclaimer

The lyrics included in the music catalog are
fictional texts created specifically for testing
and demonstrating the music search functionality.
They are not the original lyrics of the songs listed in the catalog.

