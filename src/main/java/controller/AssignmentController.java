package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Assignment;
import model.Company;
import model.Student;
import model.Tutor;
import utils.Utils;

public class AssignmentController implements ControllerInterface {

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;

    @FXML
    private ComboBox<Company> choiceCompany;
    @FXML
    private ComboBox<Student> choiceStudent;
    @FXML
    private ComboBox<Tutor> choiceTutor;

    private ObservableList<Assignment> assigments = FXCollections.observableArrayList();

    public void initialize() {
        initializeButtons();
    }

    private void initializeButtons() {
        btnCancel.setOnAction(this::closeWindow);
        btnAdd.setOnAction(e -> save());
    }

    private void intializeDropdowns() {
        ObservableList<Student> students = Student.getStudents();

        if (students != null) {
            students.removeIf(student -> assigments.stream().anyMatch(assigment -> assigment.getStudent().equals(student)));
        }

        if (students == null || students.isEmpty()) {
            Utils.showAlert("Error", "No hay estudiantes disponibles", Alert.AlertType.ERROR);

            btnAdd.setDisable(true);
            choiceStudent.setDisable(true);
        }

        choiceStudent.setItems(students);

        ObservableList<Company> companies = Company.getAllCompanies();

        if (companies != null) {
            companies.removeIf(company -> assigments.stream().anyMatch(assigment -> assigment.getCompany().equals(company)));
        }

        if (companies == null || companies.isEmpty()) {
            Utils.showAlert("Error", "No hay empresas disponibles", Alert.AlertType.ERROR);

            btnAdd.setDisable(true);
            choiceCompany.setDisable(true);
        }

        choiceCompany.setItems(companies);

        ObservableList<Tutor> tutors = Tutor.getTutors();

        if (tutors != null) {
            tutors.removeIf(tutor -> assigments.stream().anyMatch(assigment -> assigment.getTutor().equals(tutor)));
        }

        if (tutors == null || tutors.isEmpty()) {
            Utils.showAlert("Error", "No hay tutores disponibles", Alert.AlertType.ERROR);

            btnAdd.setDisable(true);
            choiceTutor.setDisable(true);
        }

        choiceTutor.setItems(tutors);
    }

    private void save() {
        if (choiceCompany.getValue() == null || choiceStudent.getValue() == null || choiceTutor.getValue() == null) {
            Utils.showAlert("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
            return;
        }

        Assignment assigment = new Assignment(choiceStudent.getValue(), choiceCompany.getValue(), choiceTutor.getValue());

        if (assigment.save()) {
            Utils.showAlert("Éxito", "Asignación creada correctamente", Alert.AlertType.INFORMATION);
            Utils.openWindow(Utils.WindowType.ASSIGNMENTS, this);
        } else {
            Utils.showAlert("Error", "No se ha podido crear la asignación", Alert.AlertType.ERROR);
        }

    }

    @Override
    public Stage getStage() {
        return (Stage) btnAdd.getScene().getWindow();
    }

    @Override
    public void closeWindow(Event event) {
        Utils.openWindow(Utils.WindowType.ASSIGNMENTS, this);
    }

    @Override
    public void initData(Object data) {
        if (data == null) return;

        if (data instanceof ObservableList<?>) {
            assigments = (ObservableList<Assignment>) data;
        }

        intializeDropdowns();
    }
}
