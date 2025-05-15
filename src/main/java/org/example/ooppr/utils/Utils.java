package org.example.ooppr.utils;

import javafx.scene.paint.Color;

public class Utils {
    public static String colorToString( Color color ) {
        return color.toString();
    }

    public static Color stringtoColor( String colorString ) {
        return Color.web(colorString);
    }
}
