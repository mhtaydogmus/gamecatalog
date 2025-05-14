package com.example.main_screen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class GameLoader {
    private static final Gson gson = new Gson();

    private static final File USER_DATA_FILE = new File(System.getProperty("user.home"), "GameCatalogApp/games.json");
    private static final String DEFAULT_RESOURCE_PATH = "/default_games.json";

    public static List<Game> loadGames() {
        try {
            USER_DATA_FILE.getParentFile().mkdirs();

            if (!USER_DATA_FILE.exists()) {
                try (InputStream input = GameLoader.class.getResourceAsStream(DEFAULT_RESOURCE_PATH)) {
                    if (input != null) {
                        Files.copy(input, USER_DATA_FILE.toPath());
                        System.out.println("Copied default games.json to user folder.");
                    } else {
                        Files.write(USER_DATA_FILE.toPath(), "[]".getBytes());
                        System.out.println("Created empty games.json.");
                    }
                }
            }

            try (FileReader reader = new FileReader(USER_DATA_FILE)) {
                Type listType = new TypeToken<List<Game>>() {}.getType();
                List<Game> games = gson.fromJson(reader, listType);
                System.out.println("Loaded " + (games != null ? games.size() : 0) + " games.");
                return games != null ? games : Collections.emptyList();
            }

        } catch (IOException e) {
            System.err.println("Error loading games: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static boolean saveGames(List<Game> games) {
        try {
            USER_DATA_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(USER_DATA_FILE)) {
                gson.toJson(games, writer);
            }
            return true;

        } catch (IOException e) {
            System.err.println("Error saving games: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public void ichangedsmthGithubPushit(){
        int nothing=0;
        return;
    }
}
