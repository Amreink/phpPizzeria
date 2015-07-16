/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw_filmanwendung;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 *
 * @author Artur
 */
public class FXMLDocumentController implements Initializable {
    
    MovieList movies = new MovieList();
    OMDB omdb = new OMDB();
    
    @FXML
    private Label label;
    private TextField searchbar;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        label.setText("lol");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
