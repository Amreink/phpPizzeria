/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Movie;
import classes.TableRow;
import classes.MovieList;
import classes.OMDB;
import classes.SQLite;
import classes.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Timo
 */
public class PopupFXMLController implements Initializable {

    MovieList movies = new MovieList();
    OMDB omdb = new OMDB();

    Movie movie = null;

    private Movie currentMovie;

    @FXML
    private Button btnSchließen;
    @FXML
    private Button btnRate;
    @FXML
    private Button btnLooked;
    @FXML
    private Label lblDetails;
    @FXML
    private TableView popupTable;
    @FXML
    private TextArea popupPlot;
    @FXML
    private ImageView popupImage;

    @FXML
    private void closePopup() {
        Stage stage = (Stage) btnSchließen.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onLooked() {
        System.out.println("Test");
    }

    @FXML
    private void onRate() {
        System.out.println("Test");
    }

    private void loadMoviePopup(Movie movie) {
        
        String imageUrl = null;

        if (movie.getPoster().startsWith("http")) {
            imageUrl = movie.getPoster();
        } else {
            imageUrl = "http://ozarktech.com/wp-content/uploads/2014/05/image-not-available-grid.jpg";
        }
        popupImage.setImage(new Image(imageUrl));
        popupPlot.setText(movie.getPlot());

        Pane header = (Pane) popupTable.lookup("TableHeaderRow");
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
        header.setManaged(false);

        List infoList = new ArrayList();
        infoList.add(new TableRow("Titel", movie.getTitle()));
        infoList.add(new TableRow("Dauer", movie.getRuntime()));
        infoList.add(new TableRow("Regiseur", movie.getDirector()));
        infoList.add(new TableRow("Schauspieler", movie.getActors()));
        infoList.add(new TableRow("Genre", movie.getGenre()));
        infoList.add(new TableRow("Veröffentlicht", movie.getReleased()));
        infoList.add(new TableRow("Jahr", movie.getYear()));
        infoList.add(new TableRow("Bewertung", movie.getImdbRating()));

        ObservableList data = FXCollections.observableList(infoList);
        popupTable.setItems(data);
        TableColumn nameCol = new TableColumn();
        TableColumn valueCol = new TableColumn();
        nameCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("name"));
        nameCol.setVisible(true);
        valueCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("value"));
        valueCol.setVisible(true);
        popupTable.getColumns().setAll(nameCol, valueCol);

    }

    public void datenuebergabeMovie(Movie movie) {
        this.movie = movie;
        System.out.println(movie.getTitle());
        //loadMoviePopup(this.movie);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
}
