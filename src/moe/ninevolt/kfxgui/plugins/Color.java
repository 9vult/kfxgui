package moe.ninevolt.kfxgui.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.ninevolt.kfxgui.template.Swatch;
import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * Special builtin plugin for ASS color
 * support
 * 
 * @author 9volt
 * @since 2022/01/30
 */
public class Color extends TemplateItem {

    public static Map<String, String> targetMap;
    public static final String TARGET = "Target";
    public static final String COLOR = "Color";
    public static final String NAME = "Color";
    public static final String VERBOSE = "Verbose";

    public Color(TemplateItem parent) {
        super(parent, 
            List.of(TARGET, COLOR, VERBOSE),
            Color.NAME,
            "Set the color",
            true);
        setParam(TARGET, "1");
        setParam(COLOR, "&HFFFFFF&");
        setParam(VERBOSE, "1 Primary");

        targetMap = new HashMap<>();
        targetMap.put("1 Primary",   "1");
        targetMap.put("2 Secondary", "2");
        targetMap.put("3 Outline",   "3");
        targetMap.put("4 Shadow",    "4");
    }


    /**
     * Get the mapping between human-readable color
     * targets and the ASS value for them
     * @return Target map
     */
    public Map<String, String> getTargetMap() {
        return Color.targetMap;
    }

    @Override
    public String getFormattedResult() {
        StringBuilder result = new StringBuilder();
        result.append('?');
        result.append(paramMap.get(TARGET));
        result.append('c');
        String c = paramMap.get(COLOR);
        if (c.startsWith("@")) {
            String swatchName = c.replaceFirst("@", "");
            for (Swatch s : Swatch.swatches) {
                if (s.getName().equals(swatchName)) {
                    result.append(s.getValue());
                    break;
                }
            }
        } else {
            result.append(paramMap.get(COLOR));
        }
        return result.toString();
    }
    
}
