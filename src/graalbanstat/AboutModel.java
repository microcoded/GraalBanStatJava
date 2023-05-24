package graalbanstat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AboutModel extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(AboutModel.class.getResource("about.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(AboutModel.class);
    }
}
