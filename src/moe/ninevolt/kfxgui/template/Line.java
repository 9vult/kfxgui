package moe.ninevolt.kfxgui.template;

import java.util.List;

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
    private static final String ADDITIONAL = "Additional Type Declarations";
    private static final String LAYER = "Layer";
    private static final String ACTOR = "Actor";

    private LineType type;
    private static List<String> paramList = List.of(NAME, STYLE, ADDITIONAL, LAYER, ACTOR);

    public Line(LineType type, String name) {
        super(null, Line.paramList, name, "", false);
        this.type = type;
        setParam(NAME, name);
        setParam(STYLE, "Default");
        setParam(LAYER, "0");
        setParam(ADDITIONAL, "");
    }
    
    public LineType getType() {
        return this.type;
    }

    public void setType(LineType type) {
        this.type = type;
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
        result.append((type.toString() + " " + paramMap.get(ADDITIONAL)).trim());
        result.append(COMMA);

        result.append(LBRACE);
        result.append(this.nameProperty().get());
        result.append(RBRACE);

        if (type.toString().contains("template")) result.append(LBRACE);
        for (TemplateItem item : getChildren()) {
            result.append(item.getFormattedResult());
        }
        if (type.toString().contains("template")) result.append(RBRACE);
        return result.toString();
    }

}
