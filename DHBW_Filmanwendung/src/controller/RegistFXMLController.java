/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.SQLite;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Arth
 */
public class RegistFXMLController implements Initializable {

    @FXML
    private TextField txtUserName;
    @FXML
    private Button btnRegist;

    @FXML
    private void regist() {
        System.out.println(txtUserName.getText());
        
        Map<String, String> map = new HashMap<>();
        map.put("username", txtUserName.getText());
        
        SQLite sql = new SQLite();
        sql.insert("User", map);
        Stage stage = (Stage) btnRegist.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
