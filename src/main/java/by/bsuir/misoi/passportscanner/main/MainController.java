package by.bsuir.misoi.passportscanner.main;

import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.filters.MedianFilter;
import by.bsuir.misoi.passportscanner.filters.MonochromeFilter;
import by.bsuir.misoi.passportscanner.filters.ReduceNoiseFilter;
import by.bsuir.misoi.passportscanner.services.TransformService;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static by.bsuir.misoi.passportscanner.utils.Constants.GREEN_COLOR;
import static by.bsuir.misoi.passportscanner.utils.Constants.RED_COLOR;

public class MainController implements Initializable {

    private String imagePath;
    private Main main;

    @FXML
    private JFXButton transformButton;
    @FXML
    private JFXButton uploadButton;
    @FXML
    private ImageView sourceImage;
    @FXML
    private ImageView transformedImage;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private StackPane root;
    @FXML
    private JFXDialog dialog;
    @FXML
    private JFXDialogLayout layout;
    @FXML
    private JFXToggleButton medianFilter;
    @FXML
    private JFXToggleButton monochromeFilter;
    @FXML
    private JFXToggleButton noiseFilter;

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void uploadImageHandler() {
        File file = getFile();
        if (file != null) {
            imagePath = file.getAbsolutePath();
            sourceImage.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void transformButtonClickHandler() {
        if (sourceImage.getImage() == null) {
            showDialogMessage("Choose the transforming image first!", RED_COLOR);
            return;
        }

        BufferedImage image = SwingFXUtils.fromFXImage(sourceImage.getImage(), null);
        TransformService service = new TransformService(getFilters(), image);
        disableButtons();

        service.setOnSucceeded(workerStateEvent -> {
            activateButtons();
            BufferedImage result = service.getValue();
            transformedImage.setImage(SwingFXUtils.toFXImage(result, null));

            try {
                ImageHelper.saveImage(result, imagePath);
            } catch (IOException e) {
                showDialogMessage("Unable to save transformed image!", RED_COLOR);
                return;
            }

            showDialogMessage("Transformation was successfully completed!", GREEN_COLOR);
        });

        service.setOnFailed(workerStateEvent -> {
            activateButtons();
            showDialogMessage("Transformation failed!", RED_COLOR);
        });
        service.restart();
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
        spinner.setVisible(true);
    }

    private void activateButtons() {
        transformButton.setDisable(false);
        uploadButton.setDisable(false);
        spinner.setVisible(false);
    }

    private void showDialogMessage(String message, String color) {
        Label body  = (Label) layout.getBody().get(0);
        body.setTextFill(Color.web(color));
        body.setText(message);
        dialog.show(root);
    }

    private List<Filter> getFilters() {
        List<Filter> filters = new ArrayList<>();

        if (medianFilter.selectedProperty().getValue()) {
            filters.add(new MedianFilter());
        }
        if (monochromeFilter.selectedProperty().getValue()) {
            filters.add(new MonochromeFilter());
        }
        if (noiseFilter.selectedProperty().getValue()) {
            filters.add(new ReduceNoiseFilter());
        }
        return filters;

    }
}
