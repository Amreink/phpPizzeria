package classes;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OMDB {

    //--------------------------------------------------------------------------
    //Singelton-Pattern 
    //--------------------------------------------------------------------------
    private static OMDB instance = null;

    public OMDB() {
    }

    public static OMDB getInstance() {
        if (instance == null) {
            instance = new OMDB();
        }
        return instance;
    }
    //--------------------------------------------------------------------------

    //sucht einen film anhand von titel und jahr in der OMDB datenbank und gibt 
    //ein arraylist mit gefunden ergebnissen zurück
    public ArrayList searchByTitle(String movie_title, String year) throws MalformedURLException, IOException {

        String movieReplace = movie_title.replace(" ", "%");

        String sURL = "http://www.omdbapi.com/?s=" + movieReplace + "&y=" + year;

        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        SearchResponse movies = new Gson().fromJson(new InputStreamReader((InputStream) request.getContent()), SearchResponse.class);
        return movies.Search;
    }

    //sucht einen film anhand der imdbID in der OMDB datenbank und gibt ein
    //movie objekt zurück
    public Movie searchById(String id) throws MalformedURLException, IOException {

        String sURL = "http://www.omdbapi.com/?i=" + id + "&plot=full";

        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        Movie movie = new Gson().fromJson(new InputStreamReader((InputStream) request.getContent()), Movie.class);

        return movie;
    }
}

//wird für die json Struktur gebraucht
//siehe OMDB antwort /search/arraylist mit ergebnisen 
class SearchResponse {

    ArrayList<Movie> Search;
}
