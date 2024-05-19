package controller;

import javafx.event.Event;
import javafx.stage.Stage;

public interface ControllerInterface {

    Stage getStage();

    void closeWindow(Event event);

    void initData(Object data);

}
