/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import classes.SQLite;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Artur
 */
public class DHBW_Filmanwendung extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginFXML.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMinHeight(840);
        // stage.setMaxHeight(840);
        stage.setMinWidth(1300);

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/youtube1_1.png")));
        stage.setTitle("DHBW Filmanwendung");

        stage.show();

        new SQLite().createTable();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
