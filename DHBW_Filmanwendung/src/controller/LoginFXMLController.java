/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.SQLite;
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

/**
 * FXML Controller class
 *
 * @author Artur
 */
public class LoginFXMLController implements Initializable {

    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtLogin;
    @FXML
    private Label lblOnlineStatus;

    @FXML
    public void login() throws SQLException {

        String username = null;

        SQLite sql = new SQLite();
        List rs = sql.select("User", "Username", "Username = '" + txtLogin.getText() + "'");

        for (Object entrySet : rs) {
            Map<String, Object> row = (Map<String, Object>) entrySet;
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                username = (String) entry.getValue();
            }
        }

        if (username != null && username.contentEquals(txtLogin.getText())) {
            Stage stage = null;
            Parent root = null;
            FXMLLoader fxmlLoader = null;

            try {
                stage = (Stage) btnLogin.getScene().getWindow();
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainFXML.fxml"));
                root = fxmlLoader.load();
            } catch (IOException ex) {
                Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            MainFXMLController meinController = (MainFXMLController) fxmlLoader.getController();
            meinController.datenuebergabe(username);

        } else {
            txtLogin.setText(
                    "Falscher Benutzername");
            txtLogin.setStyle(
                    "-fx-text-fill:red;");
        }

    }
    
    @FXML
    public void Enterpress() throws SQLException{
        login();
    }

    @FXML
    public void Feldleeren() {
        txtLogin.setText("");
    }

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
