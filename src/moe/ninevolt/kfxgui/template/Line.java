package moe.ninevolt.kfxgui.template;

/**
 * Line.java
 * Author: 9volt
 * Created: 2022/01/25
 */
public class Line extends TemplateItem {

    private LineType type;
    private String name;

    public Line(TemplateItem parent, LineType type, String name) {
        super(parent);
        this.type = type;
        this.name = name;
    }

    public LineType getType() {
        return this.type;
    }

    public void setType(LineType type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
