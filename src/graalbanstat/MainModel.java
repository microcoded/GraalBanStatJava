package graalbanstat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class MainModel extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainModel.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainController controller = fxmlLoader.getController();

        stage.setTitle("GraalBanStat");
        stage.setScene(scene);
        controller.setStage(stage);
        stage.show();
        stage.maxWidthProperty().bind(stage.widthProperty());
        stage.minWidthProperty().bind(stage.widthProperty());
    }

    public void exit() {
        Platform.exit();
    }

    public String getPreviousMonthAbbr() {
        LocalDate previousMonth = LocalDate.now().minusMonths(1);
        return previousMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    public static void main(String[] args) {
        launch(MainModel.class);
    }
}

