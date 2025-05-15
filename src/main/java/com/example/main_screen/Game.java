package com.example.main_screen;

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

    public Game(String title, String genre, String developer, String publisher, List<String> platforms,
                List<String> translators, String steamid, String releaseYear, String playtime,
                String format, List<String> language, double rating, List<String> tags, String image) {
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

    // getter
    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTags() {
        return tags;
    }

    public double getRating() {
        return rating;
    }

    public List<String> getLanguage() {
        return language;
    }

    public String getFormat() {
        return format;
    }

    public String getPlaytime() {
        return playtime;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getSteamid() {
        return steamid;
    }

    public List<String> getTranslators() {
        return translators;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getGenre() {
        return genre;
    }

    public String getFullImagePath() {
        return "file:" + System.getProperty("user.home") + "/GameCatalogApp/" + this.image;
    }



    // setters
    public void setImage(String newImage) {
        this.image = newImage;
    }

    public void setTags(List<String> newTags) {
        this.tags = newTags;
    }

    public void setRating(double newRating) {
        this.rating = newRating;
    }

    public void setLanguage(List<String> newLanguage) {
        this.language = newLanguage;
    }

    public void setFormat(String newFormat) {
        this.format = newFormat;
    }

    public void setPlaytime(String newPlaytime) {
        this.playtime = newPlaytime;
    }

    public void setReleaseYear(String newReleaseYear) {
        this.releaseYear = newReleaseYear;
    }

    public void setSteamid(String newSteamId) {
        this.steamid = newSteamId;
    }

    public void setTranslators(List<String> newTranslators) {
        this.translators = newTranslators;
    }

    public void setPlatforms(List<String> newPlatforms) {
        this.platforms = newPlatforms;
    }

    public void setPublisher(String newPublisher) {
        this.publisher = newPublisher;
    }

    public void setDeveloper(String newdeveloper) {
        this.developer = newdeveloper;
    }

    public void setTitle(String newtitle) {
        this.title = newtitle;
    }

    public void setGenre(String newGenre) {
        this.genre = newGenre;
    }
    public void ichangedsmthGithubPushit(){
        int nothing=0;
        return;
    }
}
