package controller;

import enums.JourneyType;
import enums.Modality;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utils.Utils;

public class CompanyController implements ControllerInterface {

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;

    @FXML
    private ChoiceBox<JourneyType> choiceCompanyJourney;
    @FXML
    private ChoiceBox<Modality> choiceCompanyModality;

    @FXML
    private TextField txtCompanyAddress;
    @FXML
    private TextField txtCompanyCIF;
    @FXML
    private TextField txtCompanyCity;
    @FXML
    private TextField txtCompanyEmail;
    @FXML
    private TextField txtCompanyName;
    @FXML
    private TextField txtCompanyPostalCode;

    @FXML
    private TextField txtManagerDNI;
    @FXML
    private TextField txtManagerName;
    @FXML
    private TextField txtManagerSurname;

    @FXML
    private TextField txtTutorDNI;
    @FXML
    private TextField txtTutorName;
    @FXML
    private TextField txtTutorPhone;
    @FXML
    private TextField txtTutorSurname;

    @Override
    public Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }

    @Override
    public void closeWindow(Event event) {
        boolean result = Utils.showConfirmationAlert("¿Estás seguro de que quieres salir?", "Los datos introducidos o modificados no serán guardados");

        if (result) {
            Utils.openWindow(Utils.WindowType.COMPANIES, this);
        }else {
            event.consume();
        }
    }

    @Override
    public void initData(Object data) {

    }

    private void initializeChoiceBoxes() {
        choiceCompanyJourney.setConverter(new StringConverter<>() {
            @Override
            public String toString(JourneyType journeyType) {
                if (journeyType == null) return "";
                return journeyType.getName();
            }

            @Override
            public JourneyType fromString(String string) {
                return null;
            }
        });

        choiceCompanyJourney.getItems().addAll(JourneyType.values());

        choiceCompanyModality.setConverter(new StringConverter<>() {
            @Override
            public String toString(Modality modality) {
                if (modality == null) return "";
                return modality.getName();
            }

            @Override
            public Modality fromString(String string) {
                return null;
            }
        });

        choiceCompanyModality.getItems().addAll(Modality.values());
    }

    public void initialize() {
        initializeChoiceBoxes();

        btnCancel.setOnAction(this::closeWindow);
    }

}
