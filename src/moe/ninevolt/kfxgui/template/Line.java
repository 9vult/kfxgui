package moe.ninevolt.kfxgui.template;

import java.util.List;

import javafx.beans.property.SimpleObjectProperty;

/**
 * A Line is the basic container for actions/plugins.
 * Lines can be either TEMPLATE or CODE lines, and containerize
 * actions into groups.
 * 
 * @author 9volt
 * @since 2022/01/25
 */
public class Line extends TemplateItem {

    private static final char COMMA = ',';
    private static final char LBRACE = '{';
    private static final char RBRACE = '}';

    private static final String NAME = "Name";
    private static final String STYLE = "Style";
    private static final String LAYER = "Layer";
    private static final String ACTOR = "Actor";
    
    public static final String ADDITIONAL = "Additional Template Declarations";
    public static final String EFFECT = "Effect";
    public static final String CODE = "Code";

    private SimpleObjectProperty<LineType> type;
    private static List<String> paramList = List.of(NAME, STYLE, ADDITIONAL, LAYER, ACTOR, EFFECT, CODE);

    public Line(LineType type, String name) {
        super(null, Line.paramList, name, "", false);
        this.type = new SimpleObjectProperty<LineType>(type);
        setParam(NAME, name);
        setParam(STYLE, "Default");
        setParam(LAYER, "0");
        setParam(ADDITIONAL, "");
        setParam(ACTOR, "");
        setParam(CODE, "");
        setParam(EFFECT, "template syl");
    }
    
    public SimpleObjectProperty<LineType> getType() {
        return this.type;
    }

    public void setType(LineType type) {
        try {
            this.type.set(type);
            setParam(EFFECT, type.getName());
        } catch (NullPointerException e) {}
    }

    @Override
    public String toString() {
        return this.nameProperty().get();
    }

    @Override
    public String getFormattedResult() {
        // Format: Layer, Start, End, Style, Actor, MarginL, MarginR, MarginV, Effect, Text
        StringBuilder result = new StringBuilder();
        result.append("Comment: ");
        result.append(paramMap.get(LAYER));
        result.append(COMMA);
        result.append("0:00:00.00");
        result.append(COMMA);
        result.append("0:00:00.00");
        result.append(COMMA);
        result.append(paramMap.get(STYLE));
        result.append(COMMA);
        result.append(paramMap.get(ACTOR));
        result.append(COMMA);
        result.append("0,0,0");
        result.append(COMMA);
        result.append((type.get().getName() + " " + paramMap.get(ADDITIONAL)).trim());
        result.append(COMMA);

        if (type.get().getName().toLowerCase().contains("template")){
            result.append(LBRACE);
            result.append(this.nameProperty().get());
            result.append(RBRACE);
            result.append(LBRACE);
            for (TemplateItem item : getChildren()) {
                try {
                    result.append(item.getFormattedResult());
                } catch (Exception e) {
                    System.out.println("Error on " + item.toString());
                }
            }
            result.append(RBRACE);
        } else if (type.get().getName().toLowerCase().contains("code")) {
            result.append(sanitize(paramMap.get(Line.CODE)));
        }
    
        return result.toString();
    }

    /**
     * Replace newlines with spaces for exportation
     * @param input Text to sanitize
     * @return Text without newlines
     */
    private static String sanitize(String input) {
        return input.replaceAll("\n", " ").replaceAll("§", " ");
    }

}
