package com.example.main_screen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Game {

    private String release_year;
    private String title;
    private String genre;
    private String developer;
    private String publisher;
    private List<String> platforms;
    private List<String> translators;
    private String steamid;
    private String playtime;
    private String format;
    private List<String> language;
    private double rating;
    private List<String> tags;
    private String image;

    @JsonCreator
    public Game(@JsonProperty("title") String title, @JsonProperty("genre") String genre, @JsonProperty("developer") String developer,
                @JsonProperty("publisher") String publisher, @JsonProperty("platforms") List<String> platforms,
                @JsonProperty("translators") List<String> translators, @JsonProperty("steamid") String steamid,
                @JsonProperty("release_year") String release_year, @JsonProperty("playtime") String playtime,
                @JsonProperty("format") String format, @JsonProperty("language") List<String> language,
                @JsonProperty("rating") double rating, @JsonProperty("tags") List<String> tags, @JsonProperty("image") String image) {
        this.title = title;
        this.genre = genre;
        this.developer = developer;
        this.publisher = publisher;
        this.platforms = platforms;
        this.translators = translators;
        this.steamid = steamid;
        this.release_year = release_year;
        this.playtime = playtime;
        this.format = format;
        this.language = language;
        this.rating = rating;
        this.tags = tags;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public List<String> getTranslators() {
        return translators;
    }

    public String getSteamid() {
        return steamid;
    }

    public String getReleaseYear() {
        return release_year;
    }

    public String getPlaytime() {
        return playtime;
    }

    public String getFormat() {
        return format;
    }

    public List<String> getLanguage() {
        return language;
    }

    public double getRating() {
        return rating;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getImage() {
        return image;
    }

    public String getDescription(List<String> tags) {
        StringBuilder tagsDescription = new StringBuilder();

        for (int i = 0; i < tags.size(); i++) {
            tagsDescription.append(tags.get(i));
            if (i < tags.size() - 1) {
                tagsDescription.append(", ");
            }
        }

        return tagsDescription.toString();
    }
}
