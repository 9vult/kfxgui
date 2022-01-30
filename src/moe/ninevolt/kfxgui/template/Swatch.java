package moe.ninevolt.kfxgui.template;

import java.util.ArrayList;
import java.util.List;

public class Swatch {
    
    private String name;
    private String value;

    public static List<Swatch> swatches = new ArrayList<>();

    public Swatch(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
