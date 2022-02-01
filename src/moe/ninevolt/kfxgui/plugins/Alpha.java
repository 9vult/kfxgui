package moe.ninevolt.kfxgui.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * Special builtin plugin for ASS alpha
 * support
 * 
 * @author 9volt
 * @since 2022/01/31
 */
public class Alpha extends TemplateItem {

    public static Map<String, String> targetMap;
    public static final String TARGET = "Target";
    public static final String ALPHA = "Alpha";
    public static final String NAME = "Alpha";
    public static final String VERBOSE = "Verbose";

    public Alpha(TemplateItem parent) {
        super(parent, 
            List.of(TARGET, ALPHA, VERBOSE),
            Alpha.NAME,
            "Set the alpha",
            true);
        setParam(TARGET, "alpha");
        setParam(ALPHA, "&HFF&");
        setParam(VERBOSE, "0 All");

        targetMap = new HashMap<>();
        targetMap.put("0 All", "alpha");
        targetMap.put("1 Primary",   "1a");
        targetMap.put("2 Secondary", "2a");
        targetMap.put("3 Outline",   "3a");
        targetMap.put("4 Shadow",    "4a");
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
        result.append(paramMap.get(ALPHA));
        return result.toString();
    }
    
}
