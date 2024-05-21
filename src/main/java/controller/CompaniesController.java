package controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Company;
import utils.Utils;

import java.util.Arrays;


public class CompaniesController implements ControllerInterface {

    public Button btnCreate;
    public Button btnEdit;
    public Button btnRemove;
    public Button btnHome;
    public Button btnSearch;
    public VBox modalitiesContainer;
    public VBox journeysContainer;
    public TableView<Company> tblCompanies;

    public void initialize() {
        initializeButtons();
        initializeModalities();
        initializeJourneys();
        initializeTable();
    }

    private void initializeTable() {
        TableColumn<Company, String> colCIF = new TableColumn<>("CIF");
        TableColumn<Company, String> colName = new TableColumn<>("Nombre");
        TableColumn<Company, String> colAddress = new TableColumn<>("Dirección");
        TableColumn<Company, String> colPostalCode = new TableColumn<>("C.P.");
        TableColumn<Company, String> colCity = new TableColumn<>("Localidad");
        TableColumn<Company, String> colJourney = new TableColumn<>("Jornada");
        TableColumn<Company, String> colModality = new TableColumn<>("Modalidad");
        TableColumn<Company, String> colEmail = new TableColumn<>("Email");

        TableColumn<Company, String> colManagerDNI = new TableColumn<>("DNI del responsable");
        TableColumn<Company, String> colManagerName = new TableColumn<>("Nombre del responsable");
        TableColumn<Company, String> colManagerSurname = new TableColumn<>("Apellidos del responsable");

        TableColumn<Company, String> colTutorDNI = new TableColumn<>("DNI del tutor");
        TableColumn<Company, String> colTutorName = new TableColumn<>("Nombre del tutor");
        TableColumn<Company, String> colTutorSurname = new TableColumn<>("Apellidos del tutor");
        TableColumn<Company, String> colTutorPhone = new TableColumn<>("Teléfono del tutor");

        // Configuramos las columnas de la empresa
        Utils.configureColumn(colCIF, Utils.createStringProperty(Company::getCif));
        Utils.configureColumn(colName, Utils.createStringProperty(Company::getName));
        Utils.configureColumn(colAddress, Utils.createStringProperty(Company::getAddress));
        Utils.configureColumn(colPostalCode, Utils.createStringProperty(Company::getPostalCode));
        Utils.configureColumn(colCity, Utils.createStringProperty(Company::getCity));
        Utils.configureColumn(colEmail, Utils.createStringProperty(Company::getEmail));
        Utils.configureColumn(colModality, Utils.createStringProperty(company -> company.getModality().getName()));
        Utils.configureColumn(colJourney, Utils.createStringProperty(company -> company.getJourneyType().getName()));

        // Configuramos las columnas del responsable
        Utils.configureColumn(colManagerDNI, Utils.createStringProperty(company -> company.getCompanyManager().getDni()));
        Utils.configureColumn(colManagerName, Utils.createStringProperty(company -> company.getCompanyManager().getName()));
        Utils.configureColumn(colManagerSurname, Utils.createStringProperty(company -> company.getCompanyManager().getSurname()));

        // Configuramos las columnas del tutor
        Utils.configureColumn(colTutorDNI, Utils.createStringProperty(company -> company.getCompanyTutor().getDni()));
        Utils.configureColumn(colTutorName, Utils.createStringProperty(company -> company.getCompanyTutor().getName()));
        Utils.configureColumn(colTutorSurname, Utils.createStringProperty(company -> company.getCompanyTutor().getSurname()));
        Utils.configureColumn(colTutorPhone, Utils.createStringProperty(company -> company.getCompanyTutor().getPhone()));

        // Añadimos las columnas a la tabla
        tblCompanies.getColumns().addAll(
                colCIF, colName, colAddress, colPostalCode, colEmail, colModality, colJourney,
                colManagerDNI, colManagerName, colManagerSurname,
                colTutorDNI, colTutorName, colTutorSurname, colTutorPhone
        );

        ObservableList<Company> companies = Company.getAllCompanies();
        tblCompanies.setItems(companies);
    }

    private void initializeButtons() {
        Utils.setButtonIcon(btnCreate, "/icons/company_create.png");
        Utils.setButtonIcon(btnEdit, "/icons/company_edit.png");
        Utils.setButtonIcon(btnRemove, "/icons/company_remove.png");
        Utils.setButtonIcon(btnHome, "/icons/home.png");
        Utils.setButtonIcon(btnSearch, "/icons/search.png", 20, 20);

        Utils.setButtonTooltip(btnCreate, "Añadir empresa");
        Utils.setButtonTooltip(btnEdit, "Editar empresa");
        Utils.setButtonTooltip(btnRemove, "Eliminar empresa");
        Utils.setButtonTooltip(btnHome, "Volver al inicio");
        Utils.setButtonTooltip(btnSearch, "Buscar empresa");

        btnHome.setOnAction(e -> Utils.openWindow(Utils.WindowType.HOME, this));
        btnCreate.setOnAction(e -> Utils.openWindow(Utils.WindowType.COMPANY_FORM, this));
        btnEdit.setOnAction(e -> editCompany());
    }

    private void initializeModalities() {
        ToggleGroup toggleGroup = new ToggleGroup();

        Arrays.stream(enums.Modality.values()).forEach(modality -> {
            // Crear un radio button
            RadioButton radioButton = new RadioButton(modality.getName());
            radioButton.setStyle("-fx-min-width: 100px;");
            radioButton.setToggleGroup(toggleGroup);

            // Añadir el radio button al contenedor
            modalitiesContainer.getChildren().add(radioButton);
        });

        RadioButton defaultRadioButton = new RadioButton("Todas");
        defaultRadioButton.setStyle("-fx-min-width: 100px;");
        defaultRadioButton.setToggleGroup(toggleGroup);
        defaultRadioButton.setSelected(true);

        modalitiesContainer.getChildren().add(defaultRadioButton);
    }

    private void initializeJourneys() {
        ToggleGroup toggleGroup = new ToggleGroup();

        Arrays.stream(enums.JourneyType.values()).forEach(journeyType -> {
            // Crear un radio button
            RadioButton radioButton = new RadioButton(journeyType.getName());
            radioButton.setStyle("-fx-min-width: 100px;");
            radioButton.setToggleGroup(toggleGroup);

            // Añadir el radio button al contenedor
            journeysContainer.getChildren().add(radioButton);
        });

        RadioButton defaultRadioButton = new RadioButton("Todas");
        defaultRadioButton.setStyle("-fx-min-width: 100px;");
        defaultRadioButton.setToggleGroup(toggleGroup);
        defaultRadioButton.setSelected(true);

        journeysContainer.getChildren().add(defaultRadioButton);
    }

    private Company getSelectedCompany() {
        return tblCompanies.getSelectionModel().getSelectedItem();
    }

    public void removeCompany() {
        Company company = getSelectedCompany();

        if (company == null) {
            Utils.showAlert("Error", "Debes seleccionar una empresa para poder eliminarla.", Alert.AlertType.ERROR);
            return;
        }

        if (Utils.showConfirmationAlert("Eliminar empresa", "¿Estás seguro de que deseas eliminar la empresa seleccionada?")) {
            // TODO: Eliminar la empresa
        }
    }

    public void editCompany() {
        Company company = getSelectedCompany();

        if (company == null) {
            Utils.showAlert("Error", "Debes seleccionar una empresa para poder editarla.", Alert.AlertType.ERROR);
            return;
        }

        Utils.openWindow(Utils.WindowType.COMPANY_FORM, this, company);
    }

    @Override
    public Stage getStage() {
        return (Stage) btnCreate.getScene().getWindow();
    }

    @Override
    public void closeWindow(Event event) {
        Utils.openWindow(Utils.WindowType.HOME, this);
    }

    @Override
    public void initData(Object data) {}

}
