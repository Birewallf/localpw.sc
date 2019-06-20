package bwg;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private ToggleButton exit;



    public void onExit() {
        System.exit(0);
    }
    public void onMin() {
        Stage stage = (Stage)exit.getScene().getWindow();
        // is stage minimizable into task bar. (true | false)
        stage.setIconified(true);
    }
}
