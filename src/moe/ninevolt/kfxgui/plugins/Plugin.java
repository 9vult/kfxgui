package moe.ninevolt.kfxgui.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * Author: 9volt
 * Created: 2022/01/22
 */
public class Plugin extends TemplateItem implements Comparable<Plugin> {

    private final String name;
    private final String author;
    private final String description;
    private final boolean transform;
    private final List<String> params;

    private String format;
    private Map<String, String> paramMap;

    /**
     * A plugin represents any generic feature supported by KFX-GUI.
     * Plugins are created using a TOML file describing basic details
     * about the action the plugin will perform, like the parameters
     * required and the resulting template code format.
     * 
     * <p>This constructor should not be used outside of initial
     * loading from the TOML files. Use the copy constructor for
     * making usable plugin instances.</p>
     * @param name Name of the plugin
     * @param author Author of the plugin
     * @param description Brief description of the plugin
     * @param transform Is the plugin valid in a Transform block?
     * @param params Parameters required by the plugin
     * @param format Output declaration using <code>${paramname}</code>
     */
    public Plugin(String name, String author, String description, boolean transform, List<String> params, String format) {
        super(null);
        this.name = name;
        this.author = author;
        this.description = description;
        this.transform = transform;
        this.params = params;
        this.format = format;
        this.paramMap = new HashMap<>();
        params.forEach(param -> paramMap.put(param, ""));
    }

    /**
     * Creates a duplicate.
     * This is used so a "original copy" can be kept, with
     * all in-use actions being a copy of that original copy,
     * allowing for multiple of the same type of plugin to
     * co-exist.
     * @param p Plugin to copy
     */
    public Plugin(TemplateItem parent, Plugin p) {
        super(parent);
        this.name = p.name;
        this.author = p.author;
        this.description = p.description;
        this.transform = p.transform;
        this.params = p.params;
        this.format = p.format;
        this.paramMap = new HashMap<>();
        params.forEach(param -> paramMap.put(param, ""));
    }

    /**
     * Set a parameter for this plugin.
     * The value must use <code>?</code> in place of <code>\</code?
     * @param param Name of the parameter being set
     * @param value Value of the parameter
     */
    public void setParam(String param, String value) {
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

    public Map<String, String> getParamMap() {
        return paramMap;
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

    public List<String> getParams() {
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

    @Override
    public int compareTo(Plugin o) {
        return this.name.compareTo(o.getName());
    }

    @Override
    public String toString() {
        return this.name;
    }

}
