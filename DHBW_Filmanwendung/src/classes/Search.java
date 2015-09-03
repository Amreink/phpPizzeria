/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.ArrayList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author arth
 */
public class Search extends Service<ArrayList<Movie>> {

    private String title;
    private String year;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    protected Task<ArrayList<Movie>> createTask() {
        return new Task<ArrayList<Movie>>() {
            @Override
            protected ArrayList<Movie> call() throws Exception {
                OMDB omdb = new OMDB();
                ArrayList<Movie> results_array = omdb.searchByTitle(title, year);
                return results_array;
            }
        };
    }
}
