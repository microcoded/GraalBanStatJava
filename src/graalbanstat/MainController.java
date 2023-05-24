package graalbanstat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class MainController {
    @FXML
    private TextArea outputTxtArea;
    @FXML
    private ComboBox<String> monthBox;
    private Stage stage;
    private File selectedFile;
    private boolean ran = false;
    private CalculateBans cb;

    public void setStage(Stage stg) {
        this.stage = stg;
    }

    @FXML
    private Button selectButton;

    private final MainModel model = new MainModel();

    @FXML
    public void initialize() {
        monthBox.getItems().addAll("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        try {
            monthBox.getSelectionModel().select(model.getPreviousMonthAbbr());
        } catch (Exception e) {
            // Date could not be automatically set from system for some reason.
            // Default text is shown instead.
        }
    }

    @FXML
    public void aboutPressed() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closePressed() {
        model.exit();
    }

    @FXML
    public void select() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Bans File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                selectButton.setText(selectedFile.getName());
            }
        } catch (Exception e) {
            // Catch when box is exited but file isn't selected
        }
    }

    @FXML
    public void run() {
        if (selectedFile != null) {
            cb = new CalculateBans(selectedFile, monthBox.getSelectionModel().getSelectedItem());
            outputTxtArea.setText(cb.main());
            ran = true;
        } else {
            errorWindow("Please select bans file before continuing.");
        }
    }

    @FXML
    public void copyText() {
        String text = outputTxtArea.getText();
        Clipboard cb = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        cb.setContent(content);
    }

    private void errorWindow(String err) {
        Alert alert = new Alert(AlertType.NONE, err, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    @FXML
    public void generateGraph() {
        if (ran) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graph.fxml"));
                Parent root1 = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                GraphController controller = fxmlLoader.getController();
                controller.initData(new GraphGenerator(cb.mg));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Chart");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            errorWindow("Please calculate bans before continuing.");
        }
    }

    @FXML
    public void guidePressed() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(URI.create("https://docs.google.com/document/d/1Za3SflbXKOh0TprHQhDC88q_x87FeP5gzbQxKhZKJc8/edit"));
                }
            }
        } catch (Exception e) {
            // yolo
        }
    }

    public void openCSV() {
        if (ran) {
            CSVGenerator csvG = new CSVGenerator(cb.mg.getCalcMonthBans(), cb.mg.getCalcMonthWarns(), cb.mg.getMY());
            csvG.main();
        } else {
            errorWindow("Please calculate bans before continuing.");
        }
    }

}
