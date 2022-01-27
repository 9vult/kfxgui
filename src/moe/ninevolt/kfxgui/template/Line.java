package moe.ninevolt.kfxgui.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;

/**
 * Line.java
 * Author: 9volt
 * Created: 2022/01/25
 */
public class Line extends TemplateItem {

    private LineType type;
    private static List<String> params = List.of("Name", "Style", "Additional Template Info", "Actor");
    private Map<String, String> paramMap;

    /**
     * A Line is the basic container for actions/plugins.
     * Lines can be either TEMPLATE or CODE lines, and containerize
     * actions into groups.
     * @param parent Parent of the Line, usually Null
     * @param type Type of Line, either TEMPLATE or CODE
     * @param name Name of the Line. Meaningless outside this application
     */
    public Line(TemplateItem parent, LineType type, String name) {
        super(parent);
        this.type = type;
        this.nameProperty = new SimpleStringProperty(name);
        this.paramMap = new HashMap<>();
        params.forEach(param -> paramMap.put(param, ""));
        paramMap.put("Name", name);
    }

    /**
     * Set a parameter for this line.
     * @param param Name of the parameter being set
     * @param value Value of the parameter
     */
    public void setParam(String param, String value) {
        paramMap.put(param, value);
        if (param.equals("Name")) nameProperty().set(value);
    }

    public LineType getType() {
        return this.type;
    }

    public void setType(LineType type) {
        this.type = type;
    }

    public String getName() {
        return this.nameProperty().get();
    }

    public Map<String, String> getParamMap() {
        return this.paramMap;
    }

    public List<String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return this.nameProperty().get();
    }

}
