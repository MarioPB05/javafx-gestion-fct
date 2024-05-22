package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Student;
import utils.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentController implements ControllerInterface {

    @FXML
    public TabPane tabsForm;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnExport;
    @FXML
    private Button btnRemove;

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
    private DatePicker txtStudentBirthdate;
    @FXML
    private TextField txtStudentDNI;
    @FXML
    private TextField txtStudentName;
    @FXML
    private TextField txtStudentSurname;

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private Boolean isEditing = false;

    public void initialize() {
        setMinDate();
        initializeTable();
        intializeEvents();
    }

    public void initializeTable() {
        Utils.configureColumn(colStudentName, Utils.createStringProperty(Student::getName));
        Utils.configureColumn(colStudentSurname, Utils.createStringProperty(Student::getSurname));
        Utils.configureColumn(colStudentDNI, Utils.createStringProperty(Student::getDni));
        Utils.configureColumn(colStudentBirthdate, Utils.createStringProperty(s -> Utils.formatDate(s.getBirthdate())));
    }

    public void intializeEvents() {
        btnAdd.setOnAction(event -> addStudent());
        btnCancel.setOnAction(this::closeWindow);
        btnEdit.setOnAction(event -> editStudent());
        btnExport.setOnAction(event -> exportStudent());
        btnRemove.setOnAction(event -> removeStudent());
    }

    private void setMinDate() {
        LocalDate minDate = LocalDate.now();
        txtStudentBirthdate.setValue(minDate);

        final Callback<DatePicker, DateCell> dayCellFactory;

        dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isAfter(minDate) || item.isEqual(minDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ff5353;-fx-text-fill: white;");
                }
            }
        };

        txtStudentBirthdate.setDayCellFactory(dayCellFactory);
    }

    private Student getSelectedStudent() {
        return tblStudents.getSelectionModel().getSelectedItem();
    }

    private void reloadTable() {
        tblStudents.setItems(students);
    }

    private void clearForm() {
        txtStudentDNI.clear();
        txtStudentName.clear();
        txtStudentSurname.clear();
        txtStudentBirthdate.setValue(null);
    }

    private Boolean validateForm() {
        if (Utils.empty(txtStudentName.getText())) {
            Utils.showAlert("Error", "El campo nombre es obligatorio", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtStudentSurname.getText())) {
            Utils.showAlert("Error", "El campo apellidos es obligatorio", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtStudentDNI.getText())) {
            Utils.showAlert("Error", "El campo DNI es obligatorio", Alert.AlertType.ERROR);
            return false;
        }

        if (txtStudentBirthdate.getValue() == null) {
            Utils.showAlert("Error", "El campo fecha de nacimiento es obligatorio", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    public void addStudent() {
        if (!validateForm()) return;

        Student student = new Student(
                txtStudentDNI.getText(),
                txtStudentName.getText(),
                txtStudentSurname.getText(),
                txtStudentBirthdate.getValue()
        );

        if (!isEditing && students.stream().anyMatch(s -> s.getDni().equals(student.getDni()))) {
            Utils.showAlert("Error", "Ya existe un estudiante con ese DNI", Alert.AlertType.ERROR);
            return;
        }

        if (isEditing) {
            Student selectedStudent = getSelectedStudent();
            students.remove(selectedStudent);
        }

        students.add(student);

        if (isEditing) {
            btnAdd.setText("Añadir Alumno");
            isEditing = false;

            Utils.showAlert("Éxito", "Alumno editado correctamente", Alert.AlertType.INFORMATION);
        }else {
            Utils.showAlert("Éxito", "Alumno añadido correctamente", Alert.AlertType.INFORMATION);
        }

        clearForm();
        reloadTable();
    }

    public void editStudent() {
        btnAdd.setText("Editar Alumno");
        isEditing = true;

        Student student = getSelectedStudent();

        if (student == null) {
            Utils.showAlert("Error", "Debes seleccionar un alumno", Alert.AlertType.ERROR);
            return;
        }

        txtStudentDNI.setText(student.getDni());
        txtStudentName.setText(student.getName());
        txtStudentSurname.setText(student.getSurname());
        txtStudentBirthdate.setValue(student.getBirthdate());

        tabsForm.getSelectionModel().select(0);
    }

    public void removeStudent() {
        Student student = getSelectedStudent();

        if (student == null) {
            Utils.showAlert("Error", "Debes seleccionar un alumno", Alert.AlertType.ERROR);
            return;
        }

        if (Utils.showConfirmationAlert("Eliminar alumno", "¿Estás seguro de que deseas eliminar este alumno?")) {
            students.remove(student);
        }
    }

    public void exportStudent() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Alumnos");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DAT Files", "*.dat"));

        java.io.File file = fileChooser.showSaveDialog(getStage());
        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                oos.writeObject(new ArrayList<>(students));
                oos.flush();
            } catch (IOException e) {
                Utils.errorLogger(e.getMessage());
            }
        }

        Utils.showAlert("Éxito", "Alumnos exportados correctamente", Alert.AlertType.INFORMATION);
        Utils.openWindow(Utils.WindowType.HOME, this);
    }

    @Override
    public Stage getStage() {
        return (Stage) btnAdd.getScene().getWindow();
    }

    @Override
    public void closeWindow(Event event) {
        Utils.openWindow(Utils.WindowType.HOME, this);
    }

    @Override
    public void initData(Object data) {

    }

}
