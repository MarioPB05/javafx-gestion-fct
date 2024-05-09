package controller;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.Utils;

public class HomeController implements ControllerInterface {

    public Button btnAssignments;

    @Override
    public Stage getStage() {
        return (Stage) btnAssignments.getScene().getWindow();
    }

    @Override
    public void closeWindow() {
        Platform.exit();
    }

    public void openCompaniesWindow() {
        Utils.openWindow(Utils.WindowType.COMPANIES, this);
    }

    public void openStudentsWindow() {
        Utils.openWindow(Utils.WindowType.STUDENTS, this);
    }

    public void openTutorsWindow() {
        Utils.openWindow(Utils.WindowType.TUTORS, this);
    }

    public void openAssignmentsWindow() {
        Utils.openWindow(Utils.WindowType.ASSIGNMENTS, this);
    }

}
