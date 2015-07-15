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

    private ArrayList movies = new ArrayList<Movie>();

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
        if (this.movies.contains(movie)) {
            return this.movies.add(movie);
        }
        return false;
    }

    public boolean removeMovie(Movie movie) {
        if (this.movies.contains(movie)) {
            return this.movies.remove(movie);
        }
        return false;
    }

    public boolean movieExists(Movie movie) {
        if (this.movies.contains(movie)) {
            return true;
        }
        return false;
    }

    public int getSize() {
        return this.movies.size();
    }

    public Movie getMovieById(int id) {
        Movie movie = (Movie) this.movies.get(id);
        return movie;
    }

    public Movie getMovieByObject(Movie movie) {
        Movie new_movie = null;
        if (this.movies.contains(movie)) {
            int index = this.movies.indexOf(movie);
            new_movie = (Movie) this.movies.get(index);
        }
        return new_movie;
    }
}
