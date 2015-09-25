package classes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MovieList")
public class MovieList {

    @XmlElement(name = "movie", type = Movie.class)
    public List<Movie> movies = new ArrayList<>();

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //Singelton-Pattern 
    //bewirkt das die Klasse Movielist nur einmal instanziert werden kann 
    //wird die Movielist einmal instanziert so ist sie in der Java VM vorhanden
    //bzw geladen und kann dann immer wieder überall aufgerufen werden und alle 
    //daten die in sie geladen wurden sind vorhanden
    //
    //wird benötigt um movies aus dem maincontroller und popupcontroller heraus
    //zu bearbeiten
    //--------------------------------------------------------------------------
    private static MovieList instance = null;

    public MovieList() {
    }

    public static MovieList getInstance() {
        if (instance == null) {
            instance = new MovieList();
        }
        return instance;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    //fügt einen movie dem arraylist hinzu
    //gibt bei erfolg true zurück
    public boolean addMovie(Movie movie) {
        if (movieExists(movie) < 0) {
            return this.movies.add(movie);
        }
        return false;
    }

    //aktualisiert einen movie
    public void updateMovie(Movie movie) {
        int index = movieExists(movie);
        movies.set(index, movie);
    }

    //prüft ob ein movie schon im arraylist vorhanden ist
    //falls verfügbar gibt diese methode den index des movies im arraylist
    //zurück ansonsten gibt es -1 zurück
    public int movieExists(Movie movie) {
        for (Object test : movies) {
            Movie test2 = (Movie) test;
            if (test2.getId().equals(movie.getId())) {
                return movies.indexOf(test2);
            }
        }
        return -1;
    }

    //gibt die größe des arraylists zurück
    public int getSize() {
        return this.movies.size();
    }

    //gibt einen geforderten movie zurück falls dieser in der arraylist
    //verfügbar ist
    //parameter: movie objekt
    public Movie getMovieByObject(Movie movie) {
        Movie new_movie = null;
        int index = movieExists(movie);
        if (index > -1) {
            new_movie = (Movie) this.movies.get(index);
        }
        return new_movie;
    }

    //gibt einen geforderten movie zurück falls dieser in der arraylist
    //verfügbar ist
    //parameter: id des movies
    public Movie getMovieById(String id) {
        Movie new_movie;
        for (Object object : movies) {
            new_movie = (Movie) object;
            if (new_movie.getId().equals(id)) {
                return new_movie;
            }
        }
        return null;
    }

    //löscht ein movie aus dem arraylist
    public void deleteMovie(Movie movie) {
        if (movieExists(movie) != -1) {
            movies.remove(movie);
        }
    }
}
