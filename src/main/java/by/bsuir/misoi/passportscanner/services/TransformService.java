package by.bsuir.misoi.passportscanner.services;

import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.awt.image.BufferedImage;
import java.util.List;

public class TransformService extends Service<BufferedImage> {

    private List<Filter> filters;
    private BufferedImage image;

    public TransformService(List<Filter> filters, BufferedImage image) {
        this.filters = filters;
        this.image = image;
    }

    @Override
    protected Task<BufferedImage> createTask() {

        return new Task<BufferedImage>() {

            @Override
            protected BufferedImage call() {
                int[] pixels = ImageHelper.getPixels(image);
                for (Filter filter: filters) {
                    pixels = filter.transform(image.getWidth(), image.getHeight(), pixels);
                }
                return ImageHelper.getImageFromPixels(pixels, image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            }
        };
    }
}
