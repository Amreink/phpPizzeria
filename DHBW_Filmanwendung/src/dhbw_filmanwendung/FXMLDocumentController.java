/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Artur
 */
public class FXMLDocumentController implements Initializable {

    MovieList movies = new MovieList();
    OMDB omdb = new OMDB();

    private static Semaphore searchSemaphore = new Semaphore(1, true);
    Thread searchThread;

    @FXML
    private ListView searchlist;
    @FXML
    private TextField searchbar;
    @FXML
    private ImageView detailImage;
    @FXML
    private TextArea detailPlot;

    @FXML
    private void onSearch() {
        if (!searchbar.getText().isEmpty()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showResults(searchbar.getText());
                }
            });
        } else {
            searchlist.setItems(null);
            searchlist.setVisible(false);
        }
    }

    @FXML
    private void onCenterDragOver() {
        searchlist.setVisible(false);
    }

    @FXML
    private void onSearchlistClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Movie movie = (Movie) searchlist.getSelectionModel().getSelectedItem();
            if (movie != null) {
                loadMovie(movie.getId());                
            }
        }
    }

    private void showResults(String title) {
        try {
            ArrayList<Movie> results_array = omdb.searchByTitle(title);
            if (results_array != null) {
                searchlist.setVisible(true);
                ObservableList<Movie> results_list = FXCollections.observableList(results_array);
                searchlist.setItems(results_list);
            };
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadMovie(String id) {
        String imageUrl = null;
        try {
            Movie movie = omdb.searchById(id);
            if (movie != null) {
                if (movie.getPoster().startsWith("http")) {
                    imageUrl = movie.getPoster();
                } else {
                    imageUrl = "http://ozarktech.com/wp-content/uploads/2014/05/image-not-available-grid.jpg";
                }
                detailImage.setImage(new Image(imageUrl));
                detailPlot.setText(movie.getPlot());
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchlist.setVisible(false);
    }

}
