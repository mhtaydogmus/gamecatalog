package com.example.main_screen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Game {

    private String releaseYear;
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
                @JsonProperty("releaseYear") String releaseYear, @JsonProperty("playtime") String playtime,
                @JsonProperty("format") String format, @JsonProperty("language") List<String> language,
                @JsonProperty("rating") double rating, @JsonProperty("tags") List<String> tags, @JsonProperty("image") String image) {
        this.title = title;
        this.genre = genre;
        this.developer = developer;
        this.publisher = publisher;
        this.platforms = platforms;
        this.translators = translators;
        this.steamid = steamid;
        this.releaseYear = releaseYear;
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
        return releaseYear;
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


    public void setTitle(String newtitle) {
        this.title = newtitle;
    }

    public void setGenre(String newGenre) {
        this.genre = newGenre;
    }

    public void setDeveloper(String newdeveloper) {
        this.developer = newdeveloper;
    }

    public void setPublisher(String newPublisher) {
        this.publisher = newPublisher;
    }

    public void setPlatforms(List<String> newPlatforms) {
        this.platforms = newPlatforms;
    }

    public void setTranslators(List<String> newTranslators) {
        this.translators = newTranslators;
    }

    public void setSteamid(String newSteamId) {
        this.steamid = newSteamId;
    }

    public void setReleaseYear(String newReleaseYear) {
        this.releaseYear = newReleaseYear;
    }

    public void setPlaytime(String newPlaytime) {
        this.playtime = newPlaytime;
    }

    public void setFormat(String newFormat) {
        this.format = newFormat;
    }

    public void setLanguage(List<String> newLanguage) {
        this.language = newLanguage;
    }

    public void setRating(double newRating) {
        this.rating = newRating;
    }

    public void setTags(List<String> newTags) {
        this.tags = newTags;
    }

    public void setImage(String newImage) {
        this.image = newImage;
    }

}
