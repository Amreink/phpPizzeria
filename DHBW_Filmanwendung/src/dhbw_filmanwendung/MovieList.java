/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import java.util.ArrayList;

/**
 *
 * @author Artur
 */

public class MovieList {

    public ArrayList movies = new ArrayList<Movie>();

    private static MovieList instance = null;

    protected MovieList() {
        // Exists only to defeat instantiation.
    }

    public static MovieList getInstance() {
        if (instance == null) {
            instance = new MovieList();
        }
        return instance;
    }

    public boolean addMovie(Movie movie) {
        if (movieExists(movie) < 0) {
            return this.movies.add(movie);
        }
        return false;
    }

    // TODO Remove Methode ausprogrammieren
    
    public int movieExists(Movie movie) {
        for (Object test : movies) {
            Movie test2 = (Movie) test;
            if (test2.getId().equals(movie.getId())) {
                return movies.indexOf(test2);
            }
        }
        return -1;
    }

    public int getSize() {
        return this.movies.size();
    }

    public Movie getMovieByObject(Movie movie) {
        Movie new_movie = null;
        int index = movieExists(movie);
        if (index > -1) {
            new_movie = (Movie) this.movies.get(index);
        }
        return new_movie;
    }
}
