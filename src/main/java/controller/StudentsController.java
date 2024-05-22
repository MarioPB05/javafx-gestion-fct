package controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Student;
import utils.Utils;

public class StudentsController implements ControllerInterface {

    @FXML
    private Button btnHome;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<Student, String> colStudentBirthdate;
    @FXML
    private TableColumn<Student, String> colStudentDNI;
    @FXML
    private TableColumn<Student, String> colStudentName;
    @FXML
    private TableColumn<Student, String> colStudentSurname;
    @FXML
    private TableView<Student> tblStudents;

    @FXML
    private TextField txtSearchStudent;

    private ObservableList<Student> students;
    private ObservableList<Student> studentsFiltered;

    public void initialize() {
        initializeButtons();
        intializeTable();
    }

    public void intializeTable() {
        Utils.configureColumn(colStudentName, Utils.createStringProperty(Student::getName));
        Utils.configureColumn(colStudentSurname, Utils.createStringProperty(Student::getSurname));
        Utils.configureColumn(colStudentDNI, Utils.createStringProperty(Student::getDni));
        Utils.configureColumn(colStudentBirthdate, Utils.createStringProperty(s -> Utils.formatDate(s.getBirthdate())));

        this.students = Student.getStudents();
        tblStudents.setItems(this.students);
    }

    public void initializeButtons() {
        Utils.setButtonIcon(btnRemove, "/icons/student_remove.png");
        Utils.setButtonIcon(btnHome, "/icons/home.png");
        Utils.setButtonIcon(btnSearch, "/icons/search.png", 20, 20);

        Utils.setButtonTooltip(btnRemove, "Eliminar alumno");
        Utils.setButtonTooltip(btnHome, "Volver al inicio");
        Utils.setButtonTooltip(btnSearch, "Buscar alumno");

        btnHome.setOnAction(this::closeWindow);
        btnRemove.setOnAction(e -> removeStudent());
        btnSearch.setOnAction(e -> searchStudent());
    }

    private void searchStudent() {
        String search = txtSearchStudent.getText().toLowerCase();
        this.studentsFiltered = this.students.filtered(s -> s.getName().toLowerCase().contains(search) || s.getSurname().toLowerCase().contains(search) || s.getDni().toLowerCase().contains(search));
        tblStudents.setItems(this.studentsFiltered);
    }

    private void removeStudent() {
        Student student = tblStudents.getSelectionModel().getSelectedItem();
        if (student == null) {
            Utils.showAlert("Debes seleccionar un alumno", "No has seleccionado ningún alumno", Alert.AlertType.WARNING);
            return;
        }

        if (Utils.showConfirmationAlert("¿Estás seguro de que quieres eliminar a " + student.getName() + " " + student.getSurname() + "?", "No podrás deshacer esta acción")) {
            if (student.delete()) {
                Utils.showAlert("Alumno eliminado", "El alumno ha sido eliminado correctamente", Alert.AlertType.INFORMATION);
                this.students.remove(student);
            } else {
                Utils.showAlert("Error al eliminar el alumno", "Ha ocurrido un error al eliminar el alumno", Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public Stage getStage() {
        return (Stage) btnHome.getScene().getWindow();
    }

    @Override
    public void closeWindow(Event event) {
        Utils.openWindow(Utils.WindowType.HOME, this);
    }

    @Override
    public void initData(Object data) {

    }

}
