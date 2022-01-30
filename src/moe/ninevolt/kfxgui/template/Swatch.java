package moe.ninevolt.kfxgui.template;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

/**
 * Color swatch
 * 
 * @author 9volt
 * @since 2022/01/30
 */
public class Swatch {
    
    private String name;
    private String value;
    public SimpleStringProperty htmlColor;

    public static List<Swatch> swatches = new ArrayList<>();

    /**
     * Create a Swatch
     * @param name Name of the swatch
     * @param value ASS string value
     */
    public Swatch(String name, String value, String hex) {
        this.htmlColor = new SimpleStringProperty(hex);
        this.setName(name);
        this.setValue(value);
    }

    /**
     * @return Name of the swatch
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the swatch
     * @param name Swatch's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return ASS color string
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the ASS &HBBGGRR& color string
     * @param value ASS &BBGGRR& color string
     */
    public void setValue(String value) {
        this.value = value;
        this.htmlColor.set(Swatch.convertColor(value));
    }

    /**
     * Get the luminosity of the color, 0 <= x <= 255
     * @return luminosity of the color
     */
    public double getLuminosity() {
        String sub = htmlColor.get().substring(1);
        int rgb = Integer.parseInt(sub, 16);
        double r = (rgb >> 16) & 0xff;
        double g = (rgb >> 8) & 0xff;
        double b = (rgb >> 0) & 0xff;
        return 0.2126 * r + 0.7152 * g + 0.0722 * b; // per ITU-R BT.709
    }

    /**
     * Convert an ASS color <code>&HBBGGRR&</code> to HTML <code>#RRGGBB</code>
     * @param assColor ASS color code
     * @return HTML color code
     */
    public static String convertColor(String assColor) {
        String trimmed = assColor.replaceAll("&", "").replaceAll("H", "");
        String[] splits = trimmed.split("(?<=\\G..)");
        
        if (splits.length != 3) return "";
        return '#' + splits[2] + splits[1] + splits[0];
    }

}
