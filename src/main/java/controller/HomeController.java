package controller;

import javafx.application.Platform;
import javafx.stage.Stage;

public class HomeController implements ControllerInterface {
    @Override
    public Stage getStage() {
        return null;
    }

    @Override
    public void closeWindow() {
        Platform.exit();
    }
}
