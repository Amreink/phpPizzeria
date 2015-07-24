/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Artur
 */
public class FXMLDocumentController implements Initializable {

    MovieList movies = new MovieList();
    OMDB omdb = new OMDB();

    Movie currentMovie;

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
    private TitledPane test;
    @FXML
    private TableView detailTable;
    @FXML
    private TabPane tabPane;

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

                currentMovie = movie;
                SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
                selectionModel.select(0);
                
                if (movie.getPoster().startsWith("http")) {
                    imageUrl = movie.getPoster();
                } else {
                    imageUrl = "http://ozarktech.com/wp-content/uploads/2014/05/image-not-available-grid.jpg";
                }
                detailImage.setImage(new Image(imageUrl));
                detailImage.fitWidthProperty().bind(test.widthProperty());
                detailImage.fitHeightProperty().bind(test.heightProperty());
                detailPlot.setText(movie.getPlot());

                Pane header = (Pane) detailTable.lookup("TableHeaderRow");
                header.setMaxHeight(0);
                header.setMinHeight(0);
                header.setPrefHeight(0);
                header.setVisible(false);
                header.setManaged(false);

                List infoList = new ArrayList();
                infoList.add(new TableRow("Titel", movie.getTitle()));
                infoList.add(new TableRow("Dauer", movie.getRuntime()));
                infoList.add(new TableRow("Regiseur", movie.getDirector()));
                infoList.add(new TableRow("Genre", movie.getGenre()));
                infoList.add(new TableRow("Veröffentlicht", movie.getReleased()));
                infoList.add(new TableRow("Jahr", movie.getYear()));
                infoList.add(new TableRow("Bewertung", movie.getImdbRating()));
                infoList.add(new TableRow("Geschaut", Boolean.toString(movie.isLooked())));
                infoList.add(new TableRow("Favourit", Boolean.toString(movie.isFavourite())));
                infoList.add(new TableRow("Gemerkt", Boolean.toString(movie.isBookmark())));

                ObservableList data = FXCollections.observableList(infoList);
                detailTable.setItems(data);
                TableColumn nameCol = new TableColumn();
                TableColumn valueCol = new TableColumn();
                nameCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("name"));
                nameCol.setVisible(true);
                valueCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("value"));
                valueCol.setVisible(true);
                detailTable.getColumns().setAll(nameCol, valueCol);

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
