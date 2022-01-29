package moe.ninevolt.kfxgui.template;

import java.util.List;

/**
 * Types of lines
 * 
 * @author 9volt
 * @since 2022/01/25
 */
public class LineType {

    private String type;
    private static List<String> types;

    private LineType(String type) {
        this.type = type;
    }

    public static void load(List<String> types) {
        LineType.types = types;
    }

    public static LineType make(String type) {
        return new LineType(type);
    }

    public static List<String> getTypes() {
        return LineType.types;
    }

    public String getName() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return this.type;
    }

}
