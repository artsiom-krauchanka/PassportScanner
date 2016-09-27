package by.bsuir.misoi.passportscanner.main;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PreviewController {

    @FXML
    private ImageView previewImage;

    public void setPreviewImageImage(Image image) {
        if (image != null) {
            previewImage.setImage(image);
        }
    }
}
