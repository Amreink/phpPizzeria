/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 *
 * @author Artur
 */
public class FXMLDocumentController implements Initializable {

    MovieList movies = new MovieList();
    OMDB omdb = new OMDB();

    Thread searchThread;
    Thread loadMovie;

    @FXML
    private Label label;
    @FXML
    private TextField searchbar;
    @FXML
    private ListView<Movie> list;
    @FXML
    private ImageView image;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
    }

    @FXML
    private void test(javafx.scene.input.MouseEvent test) {
        list.setVisible(false);
        list.setItems(null);
    }

    private void showResults(String title) throws IOException {

        ArrayList<Movie> results_array = omdb.searchByTitle(title);
        ObservableList<Movie> results_list = FXCollections.observableList(results_array);
        list.setItems(results_list);
        list.setCellFactory(new Callback<ListView<Movie>, ListCell<Movie>>() {

            @Override
            public ListCell<Movie> call(ListView<Movie> p) {

                ListCell<Movie> cell = new ListCell<Movie>() {

                    @Override
                    protected void updateItem(Movie t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getTitle());
                        }
                    }

                };
                return cell;
            }
        });
    }

    private void loadMovie(String id) throws IOException {
        Movie movie = omdb.searchById(id);
        System.out.println(movie.getPoster());
        try {
            URL imageUrl = new URL(movie.getPoster());
            WritableImage test = SwingFXUtils.toFXImage(ImageIO.read(imageUrl), null);
            image.setImage(test);
        } catch (MalformedURLException ex) {
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Movie>() {

            @Override
            public void changed(ObservableValue<? extends Movie> observable, Movie oldValue, Movie newValue) {
                // Your action here
                System.out.println("Selected item: " + newValue.getTitle() + newValue.getId());
                searchbar.setText(newValue.getTitle());
                loadMovie = new Thread(new Runnable() {
                    public void run() {
                        try {
                            loadMovie(newValue.getId());
                        } catch (IOException ex) {
                            //  Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                loadMovie.start();
            }
        });
        // Listen for TextField text changes
        searchbar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    list.setVisible(false);
                    list.setItems(null);
                } else {
                    searchThread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                showResults(newValue);
                            } catch (IOException ex) {
                                //  Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    searchThread.start();
                    list.setVisible(true);
                }
            }
        });
    }

}
