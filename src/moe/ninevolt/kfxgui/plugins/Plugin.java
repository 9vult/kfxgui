package moe.ninevolt.kfxgui.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * Plugins represent most generic features supported by
 * KFX-GUI.
 * <p>Plugins are created using a TOML file describing
 * basic details about the plugin, like the parameters
 * required and the resulting template code format.</p>
 * 
 * @author 9volt
 * @since 2022/01/22
 */
public class Plugin extends TemplateItem implements Comparable<Plugin> {

    private final String author;
    
    private String format;

    public Plugin(TemplateItem parent, List<String> params, String name, String author, 
        String description, boolean transform, String format) {
        super(parent, params, name, description, transform);
        this.name.set(name);
        this.author = author;
        this.format = format;
    }

    public String getAuthor() {
        return author;
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

    @Override
    public int compareTo(Plugin o) {
        return this.name.get().compareTo(o.nameProperty().get());
    }

}
