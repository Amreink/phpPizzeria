package controller;

import classes.Movie;
import classes.MovieList;
import classes.SQLite;
import classes.TableRow;
import classes.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

public class PopupFXMLController implements Initializable {

    MovieList movies = MovieList.getInstance();
    SQLite sql = SQLite.getInstance();

    Movie movie = null;
    User user = null;
    boolean refreshtable = true;

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
    private Rating rating;

    //methode um das popup-fenster zu schließen
    @FXML
    private void closePopup() {
        Stage stage = (Stage) btnSchließen.getScene().getWindow();
        stage.close();
    }

    //methode um movie zu bewerten
    //user-rating
    @FXML
    private void onRate() {
        Movie movie = movies.getMovieById(this.movie.getId());
        movie.setUserRating((Double.toString(rating.getRating())));
        movies.updateMovie(movie);
        if (sql.exsists("Movielist", "UserID, imdbID", "UserID = '" + user.getId() + "' and imdbID = '" + movie.getImdbID() + "'") > 0) {
            sqlUpdateMovielist(movie);
        }
    }

    //methode um movie-daten in das popup-fenster zu laden
    private void loadMoviePopup(Movie movie) {

        String imageUrl = null;

        if (movie.getPoster().startsWith("http")) {
            imageUrl = movie.getPoster();
            popupImage.setImage(new Image(imageUrl));
        }

        popupPlot.setText(movie.getPlot());
        rating.setRating(Double.parseDouble(movie.getUserRating()));

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
        infoList.add(new TableRow("IMDB Rating", movie.getImdbRating()));
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

    //nimmt daten von einem anderen controller entgegen
    public void datenuebergabeMovie(Movie movie, User user) {
        this.movie = movie;
        this.user = user;
        loadMoviePopup(this.movie);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
}
