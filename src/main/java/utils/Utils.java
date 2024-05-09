package utils;

import controller.ControllerInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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

    /**
     * Enumerado con los tipos de ventana disponibles.
     */
    public enum WindowType {
        HOME("Gestión FCT", "/views/home.fxml"),
        COMPANIES("Gestión FCT | Empresas", "/views/companies.fxml"),
        STUDENTS("Gestión FCT | Alumnos", "/views/students.fxml"),
        TUTORS("Gestión FCT | Tutores", "/views/tutors.fxml"),
        ASSIGNMENTS("Gestión FCT | Asignaciones", "/views/assignments.fxml");

        private final String title;
        private final String path;

        WindowType(String title, String path) {
            this.title = title;
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public String getPath() {
            return path;
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
        warningLogger("Abriendo ventana: " + windowType.getTitle());

        try {
            FXMLLoader loader = getFXMLLoader(windowType);
            Parent root = loader.load();

            ControllerInterface controller = loader.getController();
            Image icon = new Image(Objects.requireNonNull(Utils.class.getResourceAsStream("/images/app-icon.png")));

            Scene scene = new Scene(root, 1280, 720);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(icon);
            stage.setTitle(windowType.getTitle());
            stage.setOnCloseRequest(e -> controller.closeWindow());

            stage.show();

            infoLogger("Ventana abierta correctamente.");

            if (currentController != null) {
                Stage myStage = currentController.getStage();

                if (myStage == null) {
                    throw new NullPointerException("No se ha podido obtener la ventana actual.");
                }

                myStage.close();
            }
        }catch (IOException | NullPointerException e) {
            Utils.errorLogger(e.getMessage());
        }
    }

    public static ConexionDB getDatabaseConnection() {
        return new ConexionDB("jdbc:mariadb://localhost/rent_car_db", "root", "1234");
    }

}
