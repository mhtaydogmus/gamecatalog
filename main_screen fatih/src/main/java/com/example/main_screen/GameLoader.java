package com.example.main_screen;

import com.example.main_screen.Game;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GameLoader {
    public static List<Game> loadGames(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Loading JSON from resources folder
            return objectMapper.readValue(GameLoader.class.getClassLoader().getResourceAsStream(filePath), new TypeReference<List<Game>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();  // Return an empty list if something goes wrong
        }
    }
}

