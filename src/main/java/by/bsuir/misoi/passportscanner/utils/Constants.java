package by.bsuir.misoi.passportscanner.utils;

import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.filters.MedianFilter;
import by.bsuir.misoi.passportscanner.filters.MonochromeFilter;
import by.bsuir.misoi.passportscanner.filters.ReduceNoiseFilter;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class Constants {

    public static final int WINDOW_WIDTH = 804;
    public static final int WINDOW_HEIGHT = 604;
    public static final String WINDOW_TITLE = "Passport Scanner";

    public static final List<Filter> FILTERS = ImmutableList.of(new ReduceNoiseFilter(), new MedianFilter(), new MonochromeFilter());
    public static final String RED_COLOR = "#f87965";
    public static final String GREEN_COLOR = "#18C31F";
}
