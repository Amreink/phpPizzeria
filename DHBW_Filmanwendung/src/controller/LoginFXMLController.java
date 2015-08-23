package controller;

import classes.SQLite;
import classes.User;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class LoginFXMLController implements Initializable {

    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtLogin;
    @FXML
    private Label lblOnlineStatus;

    //Login-Methode 
    //prüft den loginname ob dieser in der datenbank verfügbar ist
    //wechselt die view
    //übergibt den user vom login-controller an den main-controller
    @FXML
    public void login() throws SQLException {

        String username = null;
        String userid = null;

        SQLite sql = new SQLite();
        List rs = sql.select("User", "*", "Username = '" + txtLogin.getText() + "'");

        for (Object entrySet : rs) {
            Map<String, Object> row = (Map<String, Object>) entrySet;
            username = (String) row.get("Username");
            userid = Integer.toString((int) row.get("UserID"));
        }

        if (username != null && username.contentEquals(txtLogin.getText())) {
            try {
                Stage stage = null;
                Parent root = null;
                FXMLLoader fxmlLoader = null;
                //Lädt die MainFXML.fxml
                stage = (Stage) btnLogin.getScene().getWindow();
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainFXML.fxml"));
                root = fxmlLoader.load();
                
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                
                MainFXMLController mainController = (MainFXMLController) fxmlLoader.getController();
                mainController.datenuebergabe(new User(userid, username));
                //Falscher Benutzer
            } catch (IOException ex) {
                Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            txtLogin.setText(
                    "Falscher Benutzername");
            txtLogin.setStyle(
                    "-fx-text-fill:red;");
        }

    }

    //Enter ersetzt Mausclick
    @FXML
    public void Enterpress() throws SQLException {
        login();
    }

    //Klick in das Textfeld leert dieses
    @FXML
    public void Feldleeren() {
        txtLogin.setText("");
    }

    //POP-UP der RegistFXML.fxml
    //registrier-fenster
    @FXML
    public void Regist() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/RegistFXML.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Registrierung");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/youtube1_1.png")));
        stage.show();
    }

    //Anzeige des Status der OMBD-Datenbank
    @Override
    public void initialize(URL location, ResourceBundle resources
    ) {
        if (netIsAvailable()) {
            lblOnlineStatus.setText("OMDB ist Online");
            lblOnlineStatus.setStyle("-fx-text-fill:green");
        } else {
            lblOnlineStatus.setText("OMDB ist Offline");
            lblOnlineStatus.setStyle("-fx-text-fill:red;");
        }
    }

    //methode um eine URL auf erreichbarkeit zur prüfen
    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.omdbapi.com/");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

}
