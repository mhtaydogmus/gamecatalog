package com.example.main_screen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLoader {

    // File path for storing games data
    private static final String JSON_FILE_PATH = "src/main/resources/json/games.json";

    /**
     * Loads games from the JSON file
     * @return List of Game objects
     */
    public static List<Game> loadGames() {
        try {
            File file = new File(JSON_FILE_PATH);

            // Create directory if it doesn't exist
            file.getParentFile().mkdirs();

            // If file doesn't exist, create an empty one
            if (!file.exists()) {
                Files.write(Paths.get(JSON_FILE_PATH), "[]".getBytes());
                return new ArrayList<>();
            }

            // Read and parse the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(file, new TypeReference<List<Game>>() {});

        } catch (IOException e) {
            System.err.println("Error loading games: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Saves games to the JSON file
     * @param games List of games to save
     * @return true if successful, false otherwise
     */
    public static boolean saveGames(List<Game> games) {
        try {
            // Create directory if it doesn't exist
            File directory = new File(JSON_FILE_PATH).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Write the games list to the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), games);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving games: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a new game to the existing list and saves it
     * @param newGame Game to add
     * @return true if successful, false otherwise
     */
    public static boolean addGame(Game newGame) {
        List<Game> games = loadGames();
        games.add(newGame);
        return saveGames(games);
    }

    /**
     * Deletes a game from the list and saves the updated list
     * @param gameToDelete Game to be deleted
     * @return true if successful, false otherwise
     */
    public static boolean deleteGame(Game gameToDelete) {
        List<Game> games = loadGames();
        boolean removed = games.removeIf(game -> game.getTitle().equals(gameToDelete.getTitle()));
        if (removed) {
            return saveGames(games);
        }
        return false;
    }
}