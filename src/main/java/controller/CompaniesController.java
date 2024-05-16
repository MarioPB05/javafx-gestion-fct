package controller;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Utils;

import java.util.Arrays;


public class CompaniesController implements ControllerInterface {

    public Button btnCreate;
    public Button btnRemove;
    public Button btnHome;
    public Button btnSearch;
    public VBox modalitiesContainer;
    public VBox journeysContainer;

    public void initialize() {
        initializeButtons();
        initializeModalities();
        initializeJourneys();
    }

    private void initializeButtons() {
        Utils.setButtonIcon(btnCreate, "/icons/company_create.png");
        Utils.setButtonIcon(btnRemove, "/icons/company_remove.png");
        Utils.setButtonIcon(btnHome, "/icons/home.png");
        Utils.setButtonIcon(btnSearch, "/icons/search.png", 20, 20);

        Utils.setButtonTooltip(btnCreate, "Añadir empresa");
        Utils.setButtonTooltip(btnRemove, "Eliminar empresa");
        Utils.setButtonTooltip(btnHome, "Volver al inicio");
        Utils.setButtonTooltip(btnSearch, "Buscar empresa");

        btnHome.setOnAction(e -> Utils.openWindow(Utils.WindowType.HOME, this));
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

    @Override
    public Stage getStage() {
        return (Stage) btnCreate.getScene().getWindow();
    }

    @Override
    public void closeWindow() {
        Utils.openWindow(Utils.WindowType.HOME, this);
    }

}
