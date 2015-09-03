package classes;

import java.util.HashMap;
import java.util.Map;

public class Movie {

    private String imdbID;
    private String Title;
    private String Year;
    private String Runtime;
    private String Genre;
    private String Poster;
    private String Director;
    private String Released;
    private String Plot;
    private String imdbRating;
    private String Actors;
    private String userRating = "0";
    private boolean favourite;
    private boolean bookmark;
    private boolean looked = false;

    public String getId() {
        return imdbID;
    }

    public void setId(String id) {
        this.imdbID = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String Runtime) {
        this.Runtime = Runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String Genre) {
        this.Genre = Genre;
    }

    public String getPoster() {
        if (Poster.startsWith("http")) {
            return Poster;
        } else {
            return "icon/x-button.png";
        }
    }

    public void setPoster(String Poster) {
        this.Poster = Poster;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String Director) {
        this.Director = Director;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String Released) {
        this.Released = Released;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String Plot) {
        this.Plot = Plot;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public boolean isFavourite() {
        return this.favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isBookmark() {
        return this.bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public boolean isLooked() {
        return this.looked;
    }

    public void setLooked(boolean looked) {
        this.looked = looked;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String Actors) {
        this.Actors = Actors;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    //toString-Methode bei der ausgabe der klasse movie wird der Titel
    //des Movies zurück gegeben
    @Override
    public String toString() {
        return this.Title;
    }

    //gibt Filmdaten als map zurück
    public Map getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("imdbID", imdbID);
        map.put("Title", Title);
        map.put("Year", Year);
        map.put("Runtime", Runtime);
        map.put("Genre", Genre);
        map.put("Poster", Poster);
        map.put("Director", Director);
        map.put("Actors", Actors);
        map.put("Released", Released);
        map.put("Plot", Plot);
        map.put("imdbRating", imdbRating);
        return map;
    }

}
