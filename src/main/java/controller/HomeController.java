package controller;

import javafx.application.Platform;
import javafx.event.Event;
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
    public void closeWindow(Event event) {
        Platform.exit();
    }

    @Override
    public void initData(Object data) {}

    public void openCompaniesWindow() {
        Utils.openWindow(Utils.WindowType.COMPANIES, this);
    }

    public void openStudentsWindow() {
        Utils.openWindow(Utils.WindowType.STUDENTS, this);
    }

    public void openStudentsExportWindow() {
        Utils.openWindow(Utils.WindowType.STUDENT_FORM, this);
    }

    public void openTutorsWindow() {
        Utils.openWindow(Utils.WindowType.TUTORS, this);
    }

    public void openAssignmentsWindow() {
        Utils.openWindow(Utils.WindowType.ASSIGNMENTS, this);
    }

}
