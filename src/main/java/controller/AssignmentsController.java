package controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Assignment;
import utils.Utils;

public class AssignmentsController implements ControllerInterface {

    @FXML
    private Button btnCreate;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnRemove;

    @FXML
    private TableColumn<Assignment, String> colCompany;
    @FXML
    private TableColumn<Assignment, String> colCompanyManager;
    @FXML
    private TableColumn<Assignment, String> colCompanyTutor;
    @FXML
    private TableColumn<Assignment, String> colStudent;
    @FXML
    private TableColumn<Assignment, String> colTutor;
    @FXML
    private TableView<Assignment> tblAssignment;

    private ObservableList<Assignment> assigments;

    public void initialize() {
        initializeButtons();
        intializeTable();
    }

    private void initializeButtons() {
        Utils.setButtonIcon(btnCreate, "/icons/add.png");
        Utils.setButtonIcon(btnHome, "/icons/home.png");
        Utils.setButtonIcon(btnRemove, "/icons/remove.png");

        Utils.setButtonTooltip(btnCreate, "Crear asignación");
        Utils.setButtonTooltip(btnHome, "Volver al inicio");
        Utils.setButtonTooltip(btnRemove, "Eliminar asignación");

        btnHome.setOnAction(this::closeWindow);
        btnCreate.setOnAction(e -> Utils.openWindow(Utils.WindowType.ASSIGMENT_FORM, this, assigments));
        // btnRemove.setOnAction(e -> remove());
    }

    private void intializeTable() {
        Utils.configureColumn(colCompany, Utils.createStringProperty(assigment -> String.valueOf(assigment.getCompany())));
        Utils.configureColumn(colCompanyManager, Utils.createStringProperty(assigment -> String.valueOf(assigment.getCompany().getCompanyManager())));
        Utils.configureColumn(colCompanyTutor, Utils.createStringProperty(assigment -> String.valueOf(assigment.getCompany().getCompanyTutor())));
        Utils.configureColumn(colStudent, Utils.createStringProperty(assigment -> String.valueOf(assigment.getStudent())));
        Utils.configureColumn(colTutor, Utils.createStringProperty(assigment -> String.valueOf(assigment.getTutor())));

        this.assigments = Assignment.getAssigments();
        tblAssignment.setItems(this.assigments);
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
