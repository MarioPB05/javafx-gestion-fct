package utils;

import controller.ControllerInterface;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Utils {

    /**
     * Inicializa el mensaje de bienvenida de la aplicación.
     */
    public static void initMessage() {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "****************************************************");
        System.out.println("*                                                  *");
        System.out.println("*                   GESTIÓN FCT                    *");
        System.out.println("*                                                  *");
        System.out.println("****************************************************" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "              Autor: Mario Perdiguero" + ConsoleColors.RESET);

        System.out.println();

        infoLogger("Iniciando la aplicación...");
    }

    /**
     * Devuelve la fecha y hora actual en formato dd-MM-yyyy HH:mm:ss formateada para la consola.
     * @return Fecha y hora actual en formato dd-MM-yyyy HH:mm:ss.
     */
    private static String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String timeStamp = dateFormat.format(new Date());
        return ConsoleColors.PURPLE + "(" + timeStamp + ") " + ConsoleColors.RESET;
    }

    /**
     * Muestra un mensaje de información en la consola.
     * @param message Mensaje a mostrar.
     */
    public static void infoLogger(String message) {
        System.out.println(getTimestamp() + ConsoleColors.GREEN + "[INFO] " + message + ConsoleColors.RESET);
    }

    /**
     * Muestra un mensaje de advertencia en la consola.
     * @param message Mensaje a mostrar.
     */
    public static void warningLogger(String message) {
        System.out.println(getTimestamp() + ConsoleColors.YELLOW + "[WARNING] " + message + ConsoleColors.RESET);
    }

    /**
     * Muestra un mensaje de error en la consola.
     * @param message Mensaje a mostrar.
     */
    public static void errorLogger(String message) {
        System.out.println(getTimestamp() + ConsoleColors.RED + "[ERROR] " + message + ConsoleColors.RESET);
    }

    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);

        // Obtener la ventana del Alert
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Agregar un icono al Alert
        stage.getIcons().add(new Image(Objects.requireNonNull(Utils.class.getResourceAsStream("/images/app-icon.png"))));
        alert.showAndWait();
    }

    public static boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);

        // Obtener la ventana del Alert
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Añadir botones personalizados
        ButtonType buttonTypeYes = new ButtonType("Continuar", ButtonType.OK.getButtonData());
        ButtonType buttonTypeNo = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Agregar un icono al Alert
        stage.getIcons().add(new Image(Objects.requireNonNull(Utils.class.getResourceAsStream("/images/app-icon.png"))));

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }

    /**
     * Enumerado con los tipos de ventana disponibles.
     */
    @Getter
    public enum WindowType {
        HOME("Gestión FCT", "/views/home.fxml"),
        COMPANIES("Gestión FCT | Empresas", "/views/companies.fxml"),
        STUDENTS("Gestión FCT | Alumnos", "/views/students.fxml"),
        TUTORS("Gestión FCT | Tutores", "/views/tutors.fxml"),
        ASSIGNMENTS("Gestión FCT | Asignaciones", "/views/assignments.fxml"),

        COMPANY_FORM("Gestión FCT | Formulario de Empresa", "/views/company_form.fxml"),
        STUDENT_FORM("Gestión FCT | Formulario de Alumno", "/views/student_form.fxml"),
        ASSIGMENT_FORM("Gestión FCT | Formulario de Asignación", "/views/assignment_form.fxml");

        private final String title;
        private final String path;

        WindowType(String title, String path) {
            this.title = title;
            this.path = path;
        }
    }

    /**
     * Devuelve el FXMLLoader correspondiente al tipo de ventana solicitado.
     * @param windowType Tipo de ventana solicitado.
     * @return FXMLLoader correspondiente al tipo de ventana solicitado.
     */
    private static FXMLLoader getFXMLLoader(WindowType windowType) {
        return new FXMLLoader(Utils.class.getResource(windowType.getPath()));
    }

    /**
     * Abre una nueva ventana en función del tipo de ventana solicitado.
     * @param windowType Tipo de ventana solicitado.
     * @param currentController Controlador actual.
     */
    public static void openWindow(WindowType windowType, ControllerInterface currentController) {
        openWindow(windowType, currentController, null);
    }

    /**
     * Abre una nueva ventana en función del tipo de ventana solicitado y le pasa datos al nuevo controlador.
     * @param windowType Tipo de ventana solicitado.
     * @param currentController Controlador actual.
     * @param data Datos a pasar al nuevo controlador.
     */
    public static void openWindow(WindowType windowType, ControllerInterface currentController, Object data) {
        warningLogger("Abriendo ventana: " + windowType.getTitle());

        try {
            FXMLLoader loader = getFXMLLoader(windowType);
            Parent root = loader.load();

            ControllerInterface controller = loader.getController();

            Image icon = new Image(Objects.requireNonNull(Utils.class.getResourceAsStream("/images/app-icon.png")));

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(icon);
            stage.setTitle(windowType.getTitle());
            stage.setOnCloseRequest(controller::closeWindow);

            stage.setOnShown(e -> {
                if (data != null) {
                    controller.initData(data);
                }
            });

            if (currentController != null) {
                Stage myStage = currentController.getStage();

                if (myStage == null) {
                    throw new NullPointerException("No se ha podido obtener la ventana actual.");
                }

                myStage.close();
            }

            stage.show();

            infoLogger("Ventana abierta correctamente.");
        }catch (IOException | NullPointerException e) {
            Utils.errorLogger(e.getMessage());
        }
    }

    public static ConexionDB getDatabaseConnection() {
        return new ConexionDB("jdbc:mariadb://localhost/gestion_fct_db", "root", "1234");
    }

    public static void setButtonIcon(Button button, String iconPath) {
        Image icon = new Image(Objects.requireNonNull(Utils.class.getResourceAsStream(iconPath)));
        ImageView imageView = new ImageView(icon);

        imageView.setFitHeight(40);
        imageView.setFitWidth(40);

        button.setGraphic(imageView);
    }

    public static void setButtonIcon(Button button, String iconPath, int width, int height) {
        Image icon = new Image(Objects.requireNonNull(Utils.class.getResourceAsStream(iconPath)));
        ImageView imageView = new ImageView(icon);

        imageView.setFitHeight(height);
        imageView.setFitWidth(width);

        button.setGraphic(imageView);
    }

    public static void setButtonTooltip(Button button, String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setShowDelay(Duration.millis(200));

        button.setTooltip(tooltip);
    }

    public static boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotNumeric(String str) {
        return !isNumeric(str);
    }

    public static <S, T> void configureColumn(TableColumn<S, T> column, Callback<S, ObservableValue<T>> valueFactory) {
        column.setCellValueFactory(cellData -> valueFactory.call(cellData.getValue()));
    }

    public static <S> Callback<S, ObservableValue<Integer>> createIntegerProperty(Function<S, Integer> extractor) {
        return item -> new SimpleObjectProperty<>(extractor.apply(item));
    }

    public static <S> Callback<S, ObservableValue<String>> createStringProperty(Function<S, String> extractor) {
        return item -> new SimpleObjectProperty<>(extractor.apply(item));
    }

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "";
    }

    public static String formatDateDatabase(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }

}
