package controller;

import enums.JourneyType;
import enums.Modality;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Company;
import model.CompanyManager;
import model.CompanyTutor;
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

    private Company currentCompany;
    private boolean isEditMode = false;

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
        if (data == null) return;

        if (data instanceof Company company) {
            txtCompanyCIF.setText(company.getCif());
            txtCompanyName.setText(company.getName());
            txtCompanyAddress.setText(company.getAddress());
            txtCompanyPostalCode.setText(company.getPostalCode());
            txtCompanyCity.setText(company.getCity());
            choiceCompanyJourney.setValue(company.getJourneyType());
            choiceCompanyModality.setValue(company.getModality());
            txtCompanyEmail.setText(company.getEmail());

            txtManagerDNI.setText(company.getCompanyManager().getDni());
            txtManagerName.setText(company.getCompanyManager().getName());
            txtManagerSurname.setText(company.getCompanyManager().getSurname());

            txtTutorDNI.setText(company.getCompanyTutor().getDni());
            txtTutorName.setText(company.getCompanyTutor().getName());
            txtTutorSurname.setText(company.getCompanyTutor().getSurname());
            txtTutorPhone.setText(company.getCompanyTutor().getPhone());

            currentCompany = company;
            isEditMode = true;
        }
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

    private boolean isValidProvinceCode(String provinceCode) {
        // Códigos de provincia válidos: https://es.wikipedia.org/wiki/Anexo:Provincias_de_Espa%C3%B1a_por_c%C3%B3digo_postal
        String[] validCodes = {"01", "02", "03", "53", "54", "04", "05", "06", "07", "57", "08", "58", "59", "60", "61", "62", "63", "64", "09", "10", "11", "72", "12", "13", "14", "56", "15", "70", "16", "17", "55", "18", "19", "20", "71", "21", "22", "23", "24", "25", "26", "27", "28", "78", "79", "80", "81", "82", "83", "84", "29", "92", "93", "30", "73", "31", "32", "33", "74", "34", "35", "76", "36", "94", "37", "38", "75", "39", "40", "41", "91", "42", "43", "77", "44", "45", "46", "96", "97", "98", "47", "48", "95", "49", "50", "99", "51", "52"};
        for (String code : validCodes) {
            if (code.equals(provinceCode)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCIF(String cif) {
        // Validación con base en:
        //  -> https://es.wikipedia.org/wiki/C%C3%B3digo_de_identificaci%C3%B3n_fiscal
        //  -> http://www.aplicacionesinformaticas.com/programas/gratis/cif.php

        // Verificar la longitud
        if (cif == null || cif.length() != 9) {
            return false;
        }

        // Verificar el tipo de organización
        char organizationType = cif.charAt(0);
        if (!"ABCDEFGHJNPQRSUVW".contains(String.valueOf(organizationType))) {
            return false;
        }

        // Verificar el código de la provincia
        String provinceCode = cif.substring(1, 3);
        if (!isValidProvinceCode(provinceCode)) return false;

        // Verificar el número correlativo
        String randomCode = cif.substring(3, 8);
        if (Utils.isNotNumeric(randomCode)) return false;

        // Verificar el dígito de control
        char controlDigit = cif.charAt(8);
        if (organizationType == 'K' || organizationType == 'P' || organizationType == 'Q' || organizationType == 'S') {
            // El dígito de control debe ser una letra
            return Character.isLetter(controlDigit);
        } else if (organizationType == 'A' || organizationType == 'B' || organizationType == 'E' || organizationType == 'H') {
            // El dígito de control debe ser un número
            return Character.isDigit(controlDigit);
        }

        // El dígito de control debe ser un número o una letra
        return Character.isLetterOrDigit(controlDigit);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean validateCompanyForm() {
        if (Utils.empty(txtCompanyCIF.getText()) || !checkCIF(txtCompanyCIF.getText())) {
            Utils.showAlert("CIF incorrecto", "El CIF introducido no es válido", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtCompanyName.getText())) {
            Utils.showAlert("Nombre de empresa incorrecto", "El nombre de la empresa no puede estar vacío", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtCompanyAddress.getText())) {
            Utils.showAlert("Dirección de empresa incorrecta", "La dirección de la empresa no puede estar vacía", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtCompanyPostalCode.getText()) || Utils.isNotNumeric(txtCompanyPostalCode.getText())) {
            Utils.showAlert("Código postal incorrecto", "El código postal introducido no es válido", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtCompanyCity.getText())) {
            Utils.showAlert("Ciudad incorrecta", "La ciudad no puede estar vacía", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtCompanyEmail.getText()) || !isValidEmail(txtCompanyEmail.getText())) {
            Utils.showAlert("Email incorrecto", "El email introducido no es válido", Alert.AlertType.ERROR);
            return false;
        }

        // Comprobar que se ha seleccionado un tipo de jornada
        if (choiceCompanyJourney.getValue() == null) {
            Utils.showAlert("Jornada no seleccionada", "Debes seleccionar un tipo de jornada", Alert.AlertType.ERROR);
            return false;
        }

        // Comprobar que se ha seleccionado una modalidad
        if (choiceCompanyModality.getValue() == null) {
            Utils.showAlert("Modalidad no seleccionada", "Debes seleccionar una modalidad", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean validateManagerForm() {
        if (Utils.empty(txtManagerDNI.getText())) {
            Utils.showAlert("DNI incorrecto", "El DNI del representante legal no puede estar vacío", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtManagerName.getText())) {
            Utils.showAlert("Nombre incorrecto", "El nombre del representante legal no puede estar vacío", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtManagerSurname.getText())) {
            Utils.showAlert("Apellidos incorrectos", "Los apellidos del representante legal no pueden estar vacíos", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean validateTutorForm() {
        if (Utils.empty(txtTutorDNI.getText())) {
            Utils.showAlert("DNI incorrecto", "El DNI del tutor no puede estar vacío", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtTutorName.getText())) {
            Utils.showAlert("Nombre incorrecto", "El nombre del tutor no puede estar vacío", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtTutorSurname.getText())) {
            Utils.showAlert("Apellidos incorrectos", "Los apellidos del tutor no pueden estar vacíos", Alert.AlertType.ERROR);
            return false;
        }

        if (Utils.empty(txtTutorPhone.getText())) {
            Utils.showAlert("Teléfono incorrecto", "El teléfono del tutor no puede estar vacío", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean validateForm() {
        return validateCompanyForm() && validateManagerForm() && validateTutorForm();
    }

    private CompanyManager getCompanyManager() {
        String managerDNI = txtManagerDNI.getText();
        String managerName = txtManagerName.getText();
        String managerSurname = txtManagerSurname.getText();
        return new CompanyManager(managerDNI, managerName, managerSurname);
    }

    private CompanyTutor getCompanyTutor() {
        String tutorDNI = txtTutorDNI.getText();
        String tutorName = txtTutorName.getText();
        String tutorSurname = txtTutorSurname.getText();
        String tutorPhone = txtTutorPhone.getText();
        return new CompanyTutor(tutorDNI, tutorName, tutorSurname, tutorPhone);
    }

    private Company getCompany() {
        String cif = txtCompanyCIF.getText();
        String name = txtCompanyName.getText();
        String address = txtCompanyAddress.getText();
        String postalCode = txtCompanyPostalCode.getText();
        String city = txtCompanyCity.getText();
        JourneyType journeyType = choiceCompanyJourney.getValue();
        Modality modality = choiceCompanyModality.getValue();
        String email = txtCompanyEmail.getText();

        return new Company(cif, name, address, postalCode, city, journeyType, modality, email, getCompanyManager(), getCompanyTutor());
    }

    private void save() {
        if (!validateForm()) return;

        Company company = getCompany();

        if (isEditMode) {
            company.setId(currentCompany.getId());
            company.getCompanyManager().setId(currentCompany.getCompanyManager().getId());
            company.getCompanyTutor().setId(currentCompany.getCompanyTutor().getId());
        }

        if (company.checkExistence() && !isEditMode) {
            Utils.showAlert("Empresa ya existente", "Ya existe una empresa con el CIF introducido", Alert.AlertType.ERROR);
            return;
        }

        if (company.save()) {
            if (isEditMode) {
                Utils.showAlert("Empresa modificada correctamente", "La empresa se ha modificado correctamente", Alert.AlertType.INFORMATION);
            }else {
                Utils.showAlert("Empresa creada correctamente", "La empresa se ha creado correctamente", Alert.AlertType.INFORMATION);
            }
        }else {
            Utils.showAlert("Error al crear la empresa", "Ha ocurrido un error al intentar crear la empresa", Alert.AlertType.ERROR);
        }

        Utils.openWindow(Utils.WindowType.COMPANIES, this);
    }

    public void initialize() {
        initializeChoiceBoxes();

        btnSave.setOnAction(e -> save());
        btnCancel.setOnAction(this::closeWindow);
    }

}
