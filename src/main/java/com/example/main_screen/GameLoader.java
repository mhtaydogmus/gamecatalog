package com.example.main_screen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLoader {
    private static final String JSON_FILE_PATH = "src/main/resources/json/games.json";

    private static final Gson gson = new Gson();

    public static List<Game> loadGames() {
        try {
            File file = new File(JSON_FILE_PATH);

            file.getParentFile().mkdirs();
            if (!file.exists()) {
                Files.write(Paths.get(JSON_FILE_PATH), "[]".getBytes());
                return new ArrayList<>();
            }

            // Read JSON file using FileReader
            FileReader fileReader = new FileReader(file);
            Type listType = new TypeToken<List<Game>>() {}.getType();
            return gson.fromJson(fileReader, listType);

        } catch (IOException e) {
            System.err.println("Error loading games: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static boolean saveGames(List<Game> games) {
        try {
            File directory = new File(JSON_FILE_PATH).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Write JSON file using FileWriter
            FileWriter fileWriter = new FileWriter(new File(JSON_FILE_PATH));
            gson.toJson(games, fileWriter);
            fileWriter.flush();
            return true;

        } catch (IOException e) {
            System.err.println("Error saving games: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addGame(Game newGame) {
        List<Game> games = loadGames();
        games.add(newGame);
        return saveGames(games);
    }

    public static boolean deleteGame(Game gameToDelete) {
        List<Game> games = loadGames();
        boolean removed = games.removeIf(game -> game.getTitle().equals(gameToDelete.getTitle()));
        if (removed) {
            return saveGames(games);
        }
        return false;
    }
}
