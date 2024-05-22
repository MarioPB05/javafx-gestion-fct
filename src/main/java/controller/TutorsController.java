package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Tutor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.Utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class TutorsController implements ControllerInterface {

    @FXML
    private Button btnImport;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<Tutor, String> colTutorEmail;

    @FXML
    private TableColumn<Tutor, String> colTutorName;

    @FXML
    private TableColumn<Tutor, String> colTutorPhone;

    @FXML
    private TableColumn<Tutor, String> colTutorSurname;

    @FXML
    private TableView<Tutor> tblTutors;

    @FXML
    private TextField txtSearchTutor;

    private ObservableList<Tutor> tutors;
    private ObservableList<Tutor> tutorsFiltered;

    public void initialize() {
        initializeTable();
        initializeButtons();
    }

    private void initializeTable() {
        Utils.configureColumn(colTutorName, Utils.createStringProperty(Tutor::getName));
        Utils.configureColumn(colTutorSurname, Utils.createStringProperty(Tutor::getSurname));
        Utils.configureColumn(colTutorEmail, Utils.createStringProperty(Tutor::getEmail));
        Utils.configureColumn(colTutorPhone, Utils.createStringProperty(Tutor::getPhone));

        this.tutors = Tutor.getTutors();
        tblTutors.setItems(this.tutors);
    }

    private void initializeButtons() {
        Utils.setButtonIcon(btnImport, "/icons/import.png");
        Utils.setButtonIcon(btnRemove, "/icons/student_remove.png");
        Utils.setButtonIcon(btnHome, "/icons/home.png");
        Utils.setButtonIcon(btnSearch, "/icons/search.png", 20, 20);

        Utils.setButtonTooltip(btnImport, "Importar tutores");
        Utils.setButtonTooltip(btnRemove, "Eliminar tutor");
        Utils.setButtonTooltip(btnHome, "Volver al inicio");
        Utils.setButtonTooltip(btnSearch, "Buscar alumno");

        btnHome.setOnAction(this::closeWindow);
        btnImport.setOnAction(event -> importTutors());
        btnRemove.setOnAction(event -> removeTutor());
        btnSearch.setOnAction(event -> searchTutor());
    }

    private void importTutors() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Tutores");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);
                document.getDocumentElement().normalize();

                NodeList nList = document.getElementsByTagName("tutor");

                ObservableList<Tutor> tutors = FXCollections.observableArrayList();

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        String name = eElement.getElementsByTagName("nombre").item(0).getTextContent();
                        String surname = eElement.getElementsByTagName("apellidos").item(0).getTextContent();
                        String email = eElement.getElementsByTagName("correo").item(0).getTextContent();
                        String phone = eElement.getElementsByTagName("telefono").item(0).getTextContent();

                        Tutor tutor = new Tutor(name, surname, email, phone);
                        tutors.add(tutor);
                    }
                }

                tutors.forEach(Tutor::save);

                this.tutors.addAll(tutors);

                Utils.showAlert("Éxito", "Tutores importados correctamente", javafx.scene.control.Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                Utils.errorLogger(e.getMessage());
            }
        }
    }

    private void removeTutor() {
        Tutor tutor = tblTutors.getSelectionModel().getSelectedItem();

        if (tutor == null) {
            Utils.showAlert("Tutor no seleccionado", "Debes seleccionar un tutor para poder eliminarlo", Alert.AlertType.ERROR);
            return;
        }

        if (Utils.showConfirmationAlert("Eliminar tutor", "¿Estás seguro de que deseas eliminar el tutor seleccionado?")) {
            if (tutor.delete()) {
                Utils.showAlert("Tutor eliminado", "El tutor ha sido eliminado correctamente", Alert.AlertType.INFORMATION);
                tutors.remove(tutor);
                return;
            }

            Utils.showAlert("Error al eliminar el tutor", "Ha ocurrido un error al eliminar el tutor", Alert.AlertType.ERROR);
        }
    }

    private void searchTutor() {
        String search = txtSearchTutor.getText();
        tutorsFiltered = tutors.filtered(t -> t.getName().toLowerCase().contains(search.toLowerCase()) ||
                t.getSurname().toLowerCase().contains(search.toLowerCase()) ||
                t.getEmail().toLowerCase().contains(search.toLowerCase()) ||
                t.getPhone().toLowerCase().contains(search.toLowerCase()));
        tblTutors.setItems(tutorsFiltered);
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
