/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Artur
 */
public class OMDB {

    private static OMDB instance = null;

    protected OMDB() {
        // Exists only to defeat instantiation.
    }

    public static OMDB getInstance() {
        if (instance == null) {
            instance = new OMDB();
        }
        return instance;
    }

    public ArrayList searchByTitle(String movie_title) throws MalformedURLException, IOException {

        String sURL = "http://www.omdbapi.com/?s=" + movie_title;

        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        Gson gson = new Gson();
        SearchResponse movies = new Gson().fromJson(new InputStreamReader((InputStream) request.getContent()), SearchResponse.class);
        return movies.Search;
    }

    public Movie searchById(String id) throws MalformedURLException, IOException {

        String sURL = "http://www.omdbapi.com/?i=" + id;

        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        Gson gson = new Gson();
        Movie movie = gson.fromJson(new InputStreamReader((InputStream) request.getContent()), Movie.class);

        return movie;
    }
}

class SearchResponse {

    ArrayList<Movie> Search;
}
