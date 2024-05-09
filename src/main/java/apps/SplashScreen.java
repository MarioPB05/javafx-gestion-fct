package apps;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.Utils;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Application {

    private static final int TIMER_DELAY = 10;
    private static final int TOTAL_TIME = 2000;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/splash.fxml")));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);


        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Gestión FCT");
        stage.initStyle(StageStyle.UNDECORATED);

        stage.show();

        ProgressBar progressBar = (ProgressBar) scene.lookup("#progressBar");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            private final long startTime = System.currentTimeMillis();
            private final double[] progress = new double[1]; // Array to hold progress value

            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                progress[0] = (double) elapsedTime / TOTAL_TIME;

                if (progress[0] >= 1.0) {
                    progress[0] = 1.0;
                    timer.cancel();
                    Platform.runLater(() -> {
                        stage.close();
                        Utils.infoLogger("Aplicación iniciada correctamente.");
                        Utils.openWindow(Utils.WindowType.HOME, null);
                    });
                }

                Platform.runLater(() -> progressBar.setProgress(progress[0]));
            }
        };

        timer.schedule(task, TIMER_DELAY, TIMER_DELAY);
    }

    public static void main(String[] args) {
        Utils.initMessage();
        launch(args);
    }

}
