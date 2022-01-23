package moe.ninevolt.kfxgui.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: 9volt
 * Created: 2022/01/22
 */
public class Plugin {

    private final String name;
    private final String author;
    private final String description;
    private final boolean transform;
    private final String[] params;

    private String format;
    private Map<String, String> paramMap;

    /**
     * A plugin represents any generic feature supported by KFX-GUI.
     * Plugins are created using a JSON file describing basic details
     * about the action the plugin will perform, like the parameters
     * required and the resulting template code format.
     * @param name Name of the plugin
     * @param author Author of the plugin
     * @param description Brief description of the plugin
     * @param transform Is the plugin valid in a Transform block?
     * @param params Parameters required by the plugin
     * @param format Output declaration using <code>${paramname}</code>
     */
    public Plugin(String name, String author, String description, boolean transform, String[] params, String format) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.transform = transform;
        this.params = params;
        this.format = format;
    }

    /**
     * Set a parameter for this plugin.
     * The value must use <code>?</code> in place of <code>\</code?
     * @param param Name of the parameter being set
     * @param value Value of the parameter
     */
    public void setParam(String param, String value) {
        if (this.paramMap == null) setUpMap();
        paramMap.put(param, value);
    }

    /**
     * Get the resulting template code produced by this plugin. 
     * @return The template code, using <code>?</code> in place of <code>\</code>
     */
    public String getFormattedResult() {
        String result = this.format;
        Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");
        Matcher matcher = pattern.matcher(format);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) matches.add(matcher.group());

        for (String match : matches) {
            String token = match.substring(2, match.length() - 1);
            result = result.replace(match, this.paramMap.get(token));
        }

        return result;
    }

    private void setUpMap() {
        this.paramMap = new HashMap<>();
        Arrays.stream(params).forEach(param -> paramMap.put(param, ""));
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public boolean isTransform() {
        return transform;
    }

    public String[] getParams() {
        return params;
    }

    /**
     * Replaces <code>?</code> with <code>\</code>
     * and wraps in braces if desired.
     * This is the final step, and should be called once,
     * after all actions have been parsed and the template
     * is ready to be exported.
     * @param input Template code
     * @param wrap Should the line be wrapped in <code>{ }</code>?
     * @return Finalized template code
     */
    public static String normalizeOutput(String input, boolean wrap) {
        String replace = input.replaceAll("\\?", "\\\\");
        return wrap ? '{' + replace + '}'  : replace;
    }

}
