package by.bsuir.misoi.passportscanner.main;

import by.bsuir.misoi.passportscanner.filters.*;
import by.bsuir.misoi.passportscanner.services.TransformService;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static by.bsuir.misoi.passportscanner.utils.Constants.FILTERS;
import static by.bsuir.misoi.passportscanner.utils.Constants.FONT;
import static by.bsuir.misoi.passportscanner.utils.Constants.RED_COLOR;

public class MainController implements Initializable {

    public static final String PREVIEW_FXML = "/preview.fxml";
    public static final double WINDOW_WIDTH = 800;
    public static final double WINDOW_HEIGHT = 600;
    public static final String TITLE = "preview";
    private String imagePath;
    private Main main;

    @FXML
    private JFXButton transformButton;
    @FXML
    private JFXButton uploadButton;
    @FXML
    private JFXButton previewButton;
    @FXML
    private ImageView sourceImage;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private StackPane root;
    @FXML
    private JFXDialog dialog;
    @FXML
    private JFXDialogLayout layout;
    @FXML
    private JFXComboBox filtersBox;
    @FXML
    private ListView filtersList;

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filtersBox.setItems(FXCollections.observableList(FILTERS));
        filtersBox.getSelectionModel().select(0);
    }

    @FXML
    private void addButtonClickHandler() {
        String filterName = filtersBox.getSelectionModel().getSelectedItem().toString();
        HBox hBox = new HBox();

        Label label = new Label(filterName);
        label.setFont(FONT);
        label.setPrefHeight(30);
        label.setPrefWidth(90);

        JFXTextField textField = new JFXTextField();
        textField.setPrefWidth(60d);

        JFXButton button = new JFXButton("X");
        button.setPrefHeight(10);
        button.setPrefWidth(10);
        button.setRipplerFill(Paint.valueOf(RED_COLOR));

        button.setOnAction(event -> {
            HBox box = (HBox) button.getParent();
            filtersList.getItems().remove(box);
        });

        hBox.getChildren().add(label);
        hBox.getChildren().add(textField);
        hBox.getChildren().add(button);
        filtersList.getItems().add(hBox);
    }

    @FXML
    private void previewButtonHandler() {
        if (sourceImage.getImage() == null) {
            showDialogMessage("Choose the transforming image first!", RED_COLOR);
            return;
        }

        List<Filter> filterList = getFilterList();

        BufferedImage image = SwingFXUtils.fromFXImage(sourceImage.getImage(), null);
        TransformService service = new TransformService(filterList, image);
        disableButtons();

        service.setOnSucceeded(workerStateEvent -> {
            activateButtons();
            BufferedImage result = service.getValue();
            openPreviewWindow(SwingFXUtils.toFXImage(result, null));

            try {
                ImageHelper.saveImage(result, imagePath);
            } catch (IOException e) {
                showDialogMessage("Unable to save transformed image!", RED_COLOR);
                return;
            }
        });

        service.setOnFailed(workerStateEvent -> {
            activateButtons();
        });
        service.restart();
    }

    private void openPreviewWindow(Image image) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PREVIEW_FXML));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            PreviewController controller = loader.getController();
            controller.setPreviewImageImage(image);
            Stage stage = new Stage();
            stage.setTitle(TITLE);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.out.println("12312312");
        }
    }

    private List<Filter> getFilterList() {
        List<Filter> filters = new ArrayList<>();
        filtersList.getItems().forEach(o -> {
            HBox box = (HBox) o;
            String filterName = ((Label) box.getChildren().get(0)).getText();
            String value = ((JFXTextField) box.getChildren().get(1)).getText();
            filters.add(getFilter(filterName, value));
        });
        return filters;
    }

    private Filter getFilter(String name, String value) {
        switch (name) {
            case "Monochrome":
                //return new MonochromeFilter(Integer.valueOf(value));
            case "Median":
                return new MedianFilter(Integer.valueOf(value));
            case "Reduce Noise":
                return new ReduceNoiseFilter(Integer.valueOf(value));
            case "Canny":
                return new CannyDetectorFilter();
            case "Otsu":
                return new OtsuFilter();
            default:
                return null;
        }
    }

    @FXML
    private void uploadImageHandler() {
        File file = getFile();
        if (file != null) {
            imagePath = file.getAbsolutePath();
            sourceImage.setImage(new Image(file.toURI().toString()));
        }
    }

    private File getFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("*.jpg, *.jpeg, *.png", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(filter);
        return fileChooser.showOpenDialog(main.getPrimaryStage());
    }

    private void disableButtons() {
        transformButton.setDisable(true);
        uploadButton.setDisable(true);
        previewButton.setDisable(true);
        spinner.setVisible(true);
    }

    private void activateButtons() {
        transformButton.setDisable(false);
        uploadButton.setDisable(false);
        previewButton.setDisable(false);
        spinner.setVisible(false);
    }

    private void showDialogMessage(String message, String color) {
        Label body  = (Label) layout.getBody().get(0);
        body.setTextFill(Color.web(color));
        body.setText(message);
        dialog.show(root);
    }
}
