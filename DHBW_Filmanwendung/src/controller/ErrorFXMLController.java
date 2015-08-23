/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Timo
 */
public class ErrorFXMLController implements Initializable {

    @FXML
    private Button btnCloseError;
    @FXML
    private Label lblError;

    int label = 0;

    @FXML
    private void onCloseError() {
        Stage stage = (Stage) btnCloseError.getScene().getWindow();
        stage.close();
    }

    public void datenuebergabeError(int label) {
        if (label == 0) {
            lblError.setText("Kein Film in Liste!");
            this.label = label;
        }
        if (label == 1) {
            lblError.setText("Kein Film ausgewählt!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
