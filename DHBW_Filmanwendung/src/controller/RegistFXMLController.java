package controller;

import classes.SQLite;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistFXMLController implements Initializable {

    @FXML
    private TextField txtUserName;
    @FXML
    private Button btnRegist;
    @FXML
    private Button btnClose;

    
    //Registriert einen neuen Benutzer, wenn Feld nicht leer und speichert den User in der SQLite-DB
    @FXML
    private void regist() {
        if (!txtUserName.getText().isEmpty()) {
            SQLite sql = new SQLite();

            //selektiert zu dem eingegeben namen alle einträge in der datenbank
            List rs = sql.select("User", "Username", "Username = '" + txtUserName.getText() + "'");
            String username = "";
            for (Object entrySet : rs) {
                Map<String, Object> row = (Map<String, Object>) entrySet;
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    username = (String) entry.getValue();
                }
            }

            //Überprüft ob der Name schon vergeben ist.
            if (!username.contentEquals(txtUserName.getText()) && txtUserName.getText() != null ) {
                Map<String, String> map = new HashMap<>();
                map.put("username", txtUserName.getText());

                sql.insert("User", map);
                Stage stage = (Stage) btnRegist.getScene().getWindow();
                stage.close();
            } else {
                txtUserName.setText("Benutzername ist schon vergeben!");
            }
        }
    }
    //Enter kann Mausclick ersetzen
    @FXML
    public void enterPressReg() throws SQLException{
        regist();
    }
    //Klick in das Textfeld leert dieses
    @FXML
    public void emptyFieldReg() {
        txtUserName.setText("");
    }
    //Schließt RegistFXML.fxml
    @FXML
    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
    //Was tue ich?
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
