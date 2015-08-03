/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    public void login() {

        String test = txtLogin.getText();
        System.out.println(test);

        //if (test.contentEquals("arth")) {
        Stage stage = null;
        Parent root = null;

        try {
            stage = (Stage) btnLogin.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainFXML.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //} else {
        txtLogin.setText("Falscher Benutzername");
        txtLogin.setStyle("-fx-text-fill:red;");
        //}

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
