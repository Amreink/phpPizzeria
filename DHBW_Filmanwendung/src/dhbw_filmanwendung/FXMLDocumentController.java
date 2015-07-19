/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
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

    Movie aktMovie;

    @FXML
    private TextField searchbar;
    @FXML
    private ListView<Movie> list;
    @FXML
    private ImageView image;
    @FXML
    private TableView movieInfoTable;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn valueCol;
    @FXML
    private TextArea movieBeschreibung;
    @FXML
    private TableView merkliste;
    @FXML
    private TableView favouritenTable;

    @FXML
    private void merken(ActionEvent event) {
        aktMovie.setBookmark(true);
        movies.addMovie(aktMovie);
        System.out.println(movies.getSize());
    }

    @FXML
    private void fav(ActionEvent event) {
        aktMovie.setFavourite(true);
        movies.addMovie(aktMovie);
        System.out.println(movies.getSize());
    }

    @FXML
    private void test(javafx.scene.input.MouseEvent test) {
        list.setVisible(false);
        list.setItems(null);
    }

    @FXML
    private void changeToFav() {

        List movieList = new ArrayList();
        for (Object element : movies.movies) {
            Movie movieElement = (Movie) element;
            if (movieElement.isFavourite()) {
                movieList.add(movieElement);
            }
        }

        ObservableList data = FXCollections.observableList(movieList);
        favouritenTable.setItems(data);

        TableColumn titleCol = new TableColumn("Titel");
        titleCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        TableColumn genreCol = new TableColumn("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("genre"));
        TableColumn directorCol = new TableColumn("Regiseur");
        directorCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("director"));
        TableColumn yearCol = new TableColumn("Jahr");
        yearCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("year"));
        TableColumn runtimeCol = new TableColumn("Dauer");
        runtimeCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("runtime"));

        favouritenTable.getColumns().setAll(titleCol, genreCol, directorCol, yearCol, runtimeCol);

    }

    @FXML
    private void changeToMerkliste() throws MalformedURLException, IOException {

        List movieList = new ArrayList();
        for (Object element : movies.movies) {
            Movie movieElement = (Movie) element;
            if (movieElement.isBookmark()) {
                movieList.add(movieElement);
            }
        }
        ObservableList data = FXCollections.observableList(movieList);
        merkliste.setItems(data);

        TableColumn coverCol = new TableColumn("Cover");
        coverCol.setCellFactory(new Callback<TableColumn<Movie, Image>, TableCell<Movie, Image>>() {

            @Override
            public TableCell<Movie, Image> call(TableColumn<Movie, Image> param) {
                //Set up the ImageView
                final ImageView imageview = new ImageView();
                imageview.setFitHeight(100);
                imageview.setFitWidth(100);

                //Set up the Table
                TableCell<Movie, Image> cell = new TableCell<Movie, Image>() {
                    public void updateItem(Movie item, boolean empty) throws MalformedURLException, IOException {
                        if (item != null) {
                            URL imageUrl = new URL(item.getPoster());
                            WritableImage poster = SwingFXUtils.toFXImage(ImageIO.read(imageUrl), null);
                            imageview.setImage(poster);
                            setGraphic(imageview);
                        }
                    }
                };
                return cell;
            }
        });
        TableColumn titleCol = new TableColumn("Titel");
        titleCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        TableColumn genreCol = new TableColumn("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("genre"));
        TableColumn directorCol = new TableColumn("Regiseur");
        directorCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("director"));
        TableColumn yearCol = new TableColumn("Jahr");
        yearCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("year"));
        TableColumn runtimeCol = new TableColumn("Dauer");
        runtimeCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("runtime"));

        merkliste.getColumns().setAll(coverCol, titleCol, genreCol, directorCol, yearCol, runtimeCol);
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
        }
        );
    }

    private void loadMovie(String id) throws IOException {
        Movie movie = omdb.searchById(id);

        if (movies.movieExists(movie) > -1) {
            movie = movies.getMovieByObject(movie);
        }

        aktMovie = movie;

        Pane header = (Pane) movieInfoTable.lookup("TableHeaderRow");
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
        infoList.add(new TableRow("Bewertung", Double.toString(movie.getImdbRating())));
        infoList.add(new TableRow("Geschaut", Boolean.toString(movie.isLooked())));
        infoList.add(new TableRow("Favourit", Boolean.toString(movie.isFavourite())));
        infoList.add(new TableRow("Gemerkt", Boolean.toString(movie.isBookmark())));

        ObservableList data = FXCollections.observableList(infoList);
        movieInfoTable.setItems(data);
        nameCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("name"));
        nameCol.setVisible(true);
        valueCol.setCellValueFactory(new PropertyValueFactory<TableRow, String>("value"));
        valueCol.setVisible(true);
        movieInfoTable.getColumns().setAll(nameCol, valueCol);

        movieBeschreibung.setText(movie.getPlot());

        try {
            URL imageUrl = new URL(movie.getPoster());
            WritableImage poster = SwingFXUtils.toFXImage(ImageIO.read(imageUrl), null);
            image.setImage(poster);
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
                //System.out.println("Selected item: " + newValue.getTitle() + newValue.getId());
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
                                list.setVisible(true);
                            } catch (IOException ex) {
                                //  Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    searchThread.start();
                }
            }
        });
    }

}
