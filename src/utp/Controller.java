package utp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField loginText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button loginButton;
    @FXML
    private TextField registerLoginText;
    @FXML
    private PasswordField registerPasswordText;
    @FXML
    private PasswordField repeatPasswordText;
    @FXML
    private Button registerButton;
    @FXML
    private TextArea loginInfoText;
    @FXML
    private TextArea registerInfoText;

    @FXML
    void initialize(){
        
    }

    @FXML
    void onActionHandler(ActionEvent e){
        if(e.getSource() == loginButton){

        }
        if(e.getSource() == registerButton){

        }
    }

}
