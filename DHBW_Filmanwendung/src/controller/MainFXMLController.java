/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Movie;
import classes.MovieList;
import classes.OMDB;
import classes.SQLite;
import classes.TableRow;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author Artur
 */
public class MainFXMLController implements Initializable {

    MovieList movies = new MovieList();
    OMDB omdb = new OMDB();
    SQLite sql = new SQLite();

    String user = null;

    private Movie currentMovie;

    @FXML
    private ListView searchlist;
    @FXML
    private TextField searchbar;
    @FXML
    private ImageView detailImage;
    @FXML
    private TextArea detailPlot;
    @FXML
    private TableView detailTable;
    @FXML
    private TabPane tabPane;
    @FXML
    private ImageView imageRow1;
    @FXML
    private ImageView imageRow2;
    @FXML
    private ImageView imageRow3;
    @FXML
    private StackPane imagePane;
    @FXML
    private Button btnDetailPlay;
    @FXML
    private WebView webView;
    @FXML
    private Label lblWelcome;
    @FXML
    private TableView favoriteTable;
    @FXML
    private TableView bookmarkTable;

    @FXML
    private void onPlay() throws IOException {

        Stage trailerStage = new Stage();

        Scene scene = new Scene(new Group());
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.autosize();
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        webEngine.load("http://www.imdb.com/video/imdb/vi996454425/imdb/embed?autoplay=false");

        root.getChildren().addAll(browser);
        scene.setRoot(root);

        trailerStage.setScene(scene);
        trailerStage.setMinHeight(500);
        trailerStage.setMinWidth(1000);

        trailerStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/youtube1_1.png")));
        trailerStage.setTitle("DHBW Filmanwendung");

        trailerStage.show();

    }

    @FXML
    private void onSearch() {
        if (!searchbar.getText().isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String year = "";
                            String title = "";
                            String[] res = searchbar.getText().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                            if (res.length > 1) {
                                for (String t : res) {
                                    if (t.matches(".*\\d.*")) {
                                        year = t;
                                    } else {
                                        title = t;
                                    }
                                }
                            } else {
                                title = res[0];
                            }
                            showResults(title, year);
                        }
                    });
                }
            }).start();
        } else {
            searchlist.setItems(null);
            searchlist.setVisible(false);
        }

    }

    @FXML
    private void onFav() {
        if (currentMovie != null) {
            if (currentMovie.isFavourite()) {
                Movie movie2 = movies.getMovieByObject(currentMovie);
                movie2.setFavourite(false);
                loadMovie(currentMovie.getId());
            } else {
                this.currentMovie.setFavourite(true);
                movies.addMovie(this.currentMovie);
                loadMovie(currentMovie.getId());
                //sql.insert("movie", currentMovie.getMap());
            }
        }
    }

    @FXML
    private void onBookmark() {
        if (currentMovie != null) {
            if (currentMovie.isBookmark()) {
                Movie movie3 = movies.getMovieByObject(currentMovie);
                movie3.setBookmark(false);
                loadMovie(currentMovie.getId());
            } else {
                this.currentMovie.setBookmark(true);
                movies.addMovie(this.currentMovie);
                loadMovie(currentMovie.getId());
            }
        }
    }

    @FXML
    private void loadFavorites() {
        ObservableList fav = FXCollections.observableArrayList(movies.movies);
        favoriteTable.setItems(fav);
    }

    @FXML
    private void loadBookmark() {

    }

    @FXML
    private void onCenterDragOver() {
        searchlist.setVisible(false);
    }

    @FXML
    private void onSearchlistClick(MouseEvent event
    ) {
        if (event.getClickCount() == 2) {
            Movie movie = (Movie) searchlist.getSelectionModel().getSelectedItem();
            if (movie != null) {
                searchbar.setText(movie.getTitle());
                loadMovie(movie.getId());
            }
        }
    }

    private void showResults(String title, String year) {
        try {
            ArrayList<Movie> results_array = omdb.searchByTitle(title, year);
            if (results_array != null) {
                searchlist.setVisible(true);
                ObservableList<Movie> results_list = FXCollections.observableList(results_array);
                searchlist.setItems(results_list);
            };
        } catch (IOException ex) {
            Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadMovie(String id) {
        String imageUrl = null;
        Movie movie;
        try {

            movie = movies.getMovieById(id);

            if (movie == null) {
                movie = omdb.searchById(id);
            }

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
                infoList.add(new TableRow("Schauspieler", movie.getActors()));
                infoList.add(new TableRow("Genre", movie.getGenre()));
                infoList.add(new TableRow("Veröffentlicht", movie.getReleased()));
                infoList.add(new TableRow("Jahr", movie.getYear()));
                infoList.add(new TableRow("Bewertung", movie.getImdbRating()));
                //infoList.add(new TableRow("Geschaut", Boolean.toString(movie.isLooked())));
                //infoList.add(new TableRow("Favourit", Boolean.toString(movie.isFavourite())));
                //infoList.add(new TableRow("Gemerkt", Boolean.toString(movie.isBookmark())));

                ObservableList data = FXCollections.observableList(infoList);
                detailTable.setItems(data);
                TableColumn nameCol = new TableColumn();
                TableColumn valueCol = new TableColumn();
                nameCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("name"));
                nameCol.setVisible(true);
                valueCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("value"));
                valueCol.setVisible(true);
                detailTable.getColumns().setAll(nameCol, valueCol);

                if (movie.isFavourite()) {
                    imageRow2.setVisible(true);
                } else {
                    imageRow2.setVisible(false);
                }

                if (movie.isBookmark()) {
                    imageRow3.setVisible(true);
                } else {
                    imageRow3.setVisible(false);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //detailImage.fitHeightProperty().bind(imagePane.heightProperty());
        //detailImage.fitWidthProperty().bind(imagePane.widthProperty());

    }

    public void datenuebergabe(String user) {

        lblWelcome.setText("Hallo " + user);
        this.user = user;

    }
}
