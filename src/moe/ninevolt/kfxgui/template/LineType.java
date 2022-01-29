package moe.ninevolt.kfxgui.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Types of lines
 * 
 * @author 9volt
 * @since 2022/01/25
 */
public class LineType {

    private String name;
    private static Map<String, List<String>> types;

    private LineType(String type) {
        this.name = type;
    }

    public static void load(Map<String, List<String>> types) {
        LineType.types = types;
    }

    public static LineType make(String type) {
        return new LineType(type);
    }

    public static List<String> getTypes(String target) {
        return LineType.types.get(target);
    }

    public static List<String> getTargets() {
        List<String> result = new ArrayList<>();
        result.addAll(LineType.types.keySet());
        Collections.sort(result);
        return result;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
