# The Last Cowboy - Stardew Valley inspired Game in Java

This is a game project developed in Java for the UTFPR Programming Techniques (OOP) subject. The game is inspired by Stardew Valley, a popular farming simulation game. In this game, the player will have to kill different enemies that will be on the player path.

## Getting Started
To run the game, you will need to have Java 15 or later and Maven installed on your computer. You can download the latest version of Java from the official website: `https://www.java.com/en/download/` and Maven: `https://maven.apache.org/install.html`.

After installing Java, you can download the source code of the game from the GitHub repository: `https://github.com/thiagorbernardo/The-Last-Cowboy`

To run the game, navigate to the project folder and run the following command in the terminal:
```
mvn install
mvn exec:java
```

To create the .jar with dependencies run:
```
mvn clean compile assembly:single
java -jar target\the-last-cowboy-1.0-jar-with-dependencies.jar
```

## How to Play
When you start the game, you will be greeted with a main menu. From here, you can start a new game, load a saved game, see the rank, or exit the game.

Once you start a new game, you will be taken to the first level. You can move your character using the arrow keys and interact with objects by pressing the spacebar.

You can kill enemies in the game by pressing spacebar and getting super powers to help you defeat the 3 levels.

## Contributing
If you would like to contribute to the game project, feel free to fork the repository and submit a pull request. Please make sure to follow the coding conventions and submit unit tests with your code.

## Credits
The game was developed by Thiago Ramos, João Ferreira and Frank Bloemer for the UTFPR Programming Techniques subject. The game was inspired by Stardew Valley, developed by ConcernedApe.
