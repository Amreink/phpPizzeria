package controller;

import classes.Movie;
import classes.MovieList;
import classes.OMDB;
import classes.PdfExport;
import classes.SQLite;
import classes.TableRow;
import classes.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainFXMLController implements Initializable {

    MovieList movies;
    OMDB omdb = new OMDB();
    SQLite sql = new SQLite();

    User user = null;

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

    public MainFXMLController() {
        this.movies = MovieList.getInstance();
    }

    //Öffnet ein POP-UP in der Favliste
    @FXML
    public void onFavPressed(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            Movie movie = (Movie) favoriteTable.getSelectionModel().getSelectedItem();
            if (movie != null) {

                FXMLLoader fxmlLoader = null;
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/PopupFXML.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.setTitle("Details");
                stage.show();

                PopupFXMLController popupController = (PopupFXMLController) fxmlLoader.getController();
                popupController.datenuebergabeMovie(movie);
            }
        }
    }

    //Öffnet ein  POP-UP in der Merkliste
    @FXML
    public void onBookmarkPressed(MouseEvent event) throws IOException {

        if (event.getClickCount() == 2) {
            Movie movie = (Movie) bookmarkTable.getSelectionModel().getSelectedItem();
            if (movie != null) {
                FXMLLoader fxmlLoader = null;
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/PopupFXML.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.setTitle("Details");
                stage.show();

                PopupFXMLController popupController = (PopupFXMLController) fxmlLoader.getController();
                popupController.datenuebergabeMovie(movie);
            }
        }
    }

    //Öffnet ein Pop-up,welcher den Trailer eines Filmes abspielt
    @FXML
    private void onPlay() throws IOException {
        PdfExport pdf = new PdfExport();
        pdf.exportMovie(currentMovie);
    }

    //Was tue ich genau? Threads für die Livesearch?
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

    private void sqlInsertToMovie(Movie movie) {
        sql.insert("Movie", movie.getMap());
    }

    private void sqlInsertToMovielist(Movie movie) {
        Map<String, String> movielist = new HashMap<>();
        movielist.put("UserID", user.getId());
        movielist.put("imdbID", movie.getImdbID());
        movielist.put("MerkList", Boolean.toString(movie.isBookmark()));
        movielist.put("FavList", Boolean.toString(movie.isFavourite()));
        movielist.put("UserRate", movie.getUserRating());
        sql.insert("Movielist", movielist);
    }

    private void sqlUpdateMovielist(Movie movie) {
        Map<String, String> movielist = new HashMap<>();
        movielist.put("UserID", user.getId());
        movielist.put("imdbID", movie.getImdbID());
        movielist.put("MerkList", Boolean.toString(movie.isBookmark()));
        movielist.put("FavList", Boolean.toString(movie.isFavourite()));
        movielist.put("UserRate", movie.getUserRating());
        sql.update("Movielist", movielist, "imdbID = '" + movie.getImdbID() + "'");
    }

    //Setzt einen Film auf die Favoritenliste, oder entfernt ihn.
    @FXML
    private void onFav() {
        if (currentMovie != null) {
            this.currentMovie.setFavourite(true);
            movies.addMovie(this.currentMovie);
            loadMovie(currentMovie.getId());
            if (sql.exsists("Movie", "imdbID", "imdbID = '" + currentMovie.getImdbID() + "'") < 1) {
                sqlInsertToMovie(currentMovie);
            }
            if (sql.exsists("Movielist", "UserID, imdbID", "UserID = '" + user.getId() + "' and imdbID = '" + currentMovie.getImdbID() + "'") > 0) {
                sqlUpdateMovielist(currentMovie);
            } else {
                sqlInsertToMovielist(currentMovie);
            }

        }
    }

    //Setzt einen Film auf die Merkliste, oder entfernt ihn.
    @FXML
    private void onBookmark() {
        if (currentMovie != null) {
            this.currentMovie.setBookmark(true);
            movies.addMovie(this.currentMovie);
            loadMovie(currentMovie.getId());
            if (sql.exsists("Movie", "imdbID", "imdbID = '" + currentMovie.getImdbID() + "'") < 1) {
                sqlInsertToMovie(currentMovie);
            }
            if (sql.exsists("Movielist", "UserID, imdbID", "UserID = '" + user.getId() + "' and imdbID = '" + currentMovie.getImdbID() + "'") > 0) {
                sqlUpdateMovielist(currentMovie);
            } else {
                sqlInsertToMovielist(currentMovie);
            }
        }
    }

    //Lade die Filme in die Favoritenliste
    @FXML
    private void loadFavorites() {

        ArrayList<Movie> favorites = new ArrayList();

        for (Object element : movies.movies) {
            Movie movie = (Movie) element;
            if (movie.isFavourite() == true) {
                favorites.add(movie);
            }
        }

        ObservableList fav = FXCollections.observableList(favorites);

        TableColumn titleCol = new TableColumn("Titel");
        TableColumn yearCol = new TableColumn("Jahr");
        TableColumn genreCol = new TableColumn("Genre");
        TableColumn runCol = new TableColumn("Laufzeit");
        TableColumn ratingCol = new TableColumn("IMDB Wertung");
        TableColumn lookedCol = new TableColumn("Gesehen");
        TableColumn userRatCol = new TableColumn("Benutzer Wertung");

        titleCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("Title"));
        yearCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("Year"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("Genre"));
        runCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("Runtime"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("imdbRating"));
        userRatCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("userRating"));
        lookedCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("looked"));

        favoriteTable.getColumns().setAll(titleCol, yearCol, genreCol, runCol, userRatCol, ratingCol, lookedCol);
        favoriteTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        favoriteTable.setItems(fav);
    }

    //Lade die Filme in die Merkliste
    @FXML
    private void loadBookmarks() {

        ArrayList<Movie> bookmarks = new ArrayList();

        for (Object element : movies.movies) {
            Movie movie = (Movie) element;
            if (movie.isBookmark() == true) {
                bookmarks.add(movie);
            }
        }

        ObservableList bookmark = FXCollections.observableList(bookmarks);
        TableColumn titleCol = new TableColumn("Titel");
        TableColumn yearCol = new TableColumn("Jahr");
        TableColumn genreCol = new TableColumn("Genre");
        TableColumn runCol = new TableColumn("Laufzeit");
        TableColumn ratingCol = new TableColumn("Rating");
        TableColumn userRatCol = new TableColumn("Benutzer Wertung");

        titleCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("Title"));
        yearCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("Year"));
        genreCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("Genre"));
        runCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("Runtime"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("imdbRating"));
        userRatCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("userRating"));

        bookmarkTable.getColumns().setAll(titleCol, yearCol, genreCol, userRatCol, runCol, ratingCol);
        bookmarkTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bookmarkTable.setItems(bookmark);
    }

    //Wofür bin ich zuständig?
    @FXML
    private void onCenterDragOver() {
        searchlist.setVisible(false);
    }

    //???
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

    //Zeige die Suchergebnisse aus der omdb
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

    //Lade die Detailansicht
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

//                if (movie.isLooked()) {
//                    imageRow1.setVisible(true);
//                } else {
//                    imageRow1.setVisible(false);
//                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadMovies() {
        List movielistRS = sql.select("Movielist", "*", "UserID = '" + user.getId() + "'");

        for (Object movielistElement : movielistRS) {
            Map<String, Object> movielistRow = (Map<String, Object>) movielistElement;
            Movie movie = new Movie();
            movie.setImdbID((String) movielistRow.get("imdbID"));
            movie.setBookmark(Boolean.parseBoolean((String) movielistRow.get("MerkList")));
            movie.setFavourite(Boolean.parseBoolean((String) movielistRow.get("FavList")));
            movie.setUserRating((String) movielistRow.get("userRate"));
            List movieRS = sql.select("Movie", "*", "imdbID = '" + movie.getImdbID() + "'");
            for (Object movieElement : movieRS) {
                Map<String, Object> movieRow = (Map<String, Object>) movieElement;
                //movie.setActors((String) movieRow.get("Actors"));
                movie.setGenre((String) movieRow.get("Genre"));
                movie.setImdbRating((String) movieRow.get("imdbRating"));
                movie.setPlot((String) movieRow.get("Plot"));
                movie.setPoster((String) movieRow.get("Poster"));
                movie.setReleased((String) movieRow.get("Released"));
                movie.setRuntime((String) movieRow.get("Runtime"));
                movie.setTitle((String) movieRow.get("Title"));
                movie.setYear((String) movieRow.get("Year"));
                movie.setDirector((String) movieRow.get("Director"));
            }
            movies.addMovie(movie);
        }
    }

    //initialmethode wird aufgerufen wenn controller geladen wird.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    //???
    public void datenuebergabe(User user) {
        lblWelcome.setText("Hallo " + user.getName());
        this.user = user;
        loadMovies();
    }

    @FXML
    public void pdfListExportFav() {
        PdfExport pdf = new PdfExport();
        ArrayList<Movie> array = new ArrayList();

        for (Object element : movies.movies) {
            Movie movie = (Movie) element;
            if (movie.isFavourite() == true) {
                array.add(movie);
            }
        }
        if (array.size() > 0) {
            pdf.exportMovies(array, "favouritenliste");
        }
    }

    @FXML
    public void pdfListExportBook() {
        PdfExport pdf = new PdfExport();
        ArrayList<Movie> array = new ArrayList();

        for (Object element : movies.movies) {
            Movie movie = (Movie) element;
            if (movie.isBookmark() == true) {
                array.add(movie);
            }
        }
        if (array.size() > 0) {
            pdf.exportMovies(array, "merkliste");
        }
    }

    @FXML
    public void FavoriteDelet() {
        Movie movie = (Movie) favoriteTable.getSelectionModel().getSelectedItem();
        movie.setFavourite(false);
        movies.updateMovie(movie);
        loadFavorites();
    }

    @FXML
    public void BookmarkDelet() {
        Movie movie = (Movie) bookmarkTable.getSelectionModel().getSelectedItem();
        movie.setBookmark(false);
        movies.updateMovie(movie);
        loadBookmarks();
    }

    @FXML
    public void onDetailClose() {
        detailImage.setImage(null);
        detailPlot.setText(null);
        detailTable.setItems(null);
        searchbar.setText("");
        imageRow1.setVisible(false);
        imageRow2.setVisible(false);
        imageRow3.setVisible(false);
        currentMovie = null;
    }

    @FXML
    public void movieLooked() {
        Movie movie = (Movie) favoriteTable.getSelectionModel().getSelectedItem();
        if (movie != null) {
            if (movie.isLooked()) {
                movie.setLooked(false);
            } else {
                movie.setLooked(true);
            }
            movies.updateMovie(movie);
            loadFavorites();
        }
    }

    @FXML
    public void movieToFav() {
        Movie movie = (Movie) bookmarkTable.getSelectionModel().getSelectedItem();
        if (movie != null) {
            movie.setBookmark(false);
            movie.setFavourite(true);
            movies.updateMovie(movie);
            loadBookmarks();
            if (sql.exsists("Movielist", "UserID, imdbID", "UserID = '" + user.getId() + "' and imdbID = '" + movie.getImdbID() + "'") > 0) {
                sqlUpdateMovielist(movie);
            }
        }
    }
}
