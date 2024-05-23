package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Company;
import model.Student;
import model.Tutor;
import utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;

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

    public void importStudents() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Alumnos");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DAT Files", "*.dat"));

        java.io.File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                ObservableList<Student> students = FXCollections.observableArrayList((ArrayList<Student>) ois.readObject());

                students.forEach(Student::save);

                Utils.showAlert("Éxito", "Alumnos importados correctamente", javafx.scene.control.Alert.AlertType.INFORMATION);
            } catch (IOException | ClassNotFoundException e) {
                Utils.errorLogger(e.getMessage());
            }
        }
    }

    public void openTutorsWindow() {
        Utils.openWindow(Utils.WindowType.TUTORS, this);
    }

    public void openAssignmentsWindow() {
        ObservableList<Student> students = FXCollections.observableArrayList(Objects.requireNonNull(Student.getStudents()));
        ObservableList<Tutor> tutors = FXCollections.observableArrayList(Objects.requireNonNull(Tutor.getTutors()));
        ObservableList<Company> companies = FXCollections.observableArrayList(Objects.requireNonNull(Company.getAllCompanies()));

        if (students.isEmpty()) {
            Utils.showAlert("No hay alumnos", "Debes añadir alumnos antes de poder gestionar las asignaciones", javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        if (tutors.isEmpty()) {
            Utils.showAlert("No hay tutores", "Debes añadir tutores antes de poder gestionar las asignaciones", javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        if (companies.isEmpty()) {
            Utils.showAlert("No hay empresas", "Debes añadir empresas antes de poder gestionar las asignaciones", javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        Utils.openWindow(Utils.WindowType.ASSIGNMENTS, this);
    }

}
