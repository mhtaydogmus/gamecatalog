package com.example.main_screen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GameLoader {

    private static final String FILE_PATH = "src/main/resources/json/games.json";

    public static List<Game> loadGames() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(FILE_PATH);
            return objectMapper.readValue(file, new TypeReference<List<Game>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Save games to JSON file
    public static void saveGames(List<Game> games) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(FILE_PATH), games);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
