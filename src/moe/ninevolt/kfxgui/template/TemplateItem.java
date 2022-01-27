package moe.ninevolt.kfxgui.template;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

/**
 * TemplateItem.java
 * Author: 9volt
 * Created: 2022/01/25
 */
public class TemplateItem {
    
    private TemplateItem parent;
    private List<TemplateItem> children;
    protected SimpleStringProperty nameProperty;

    public TemplateItem(TemplateItem parent) {
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public TemplateItem getParent() {
        return parent;
    }

    public List<TemplateItem> getChildren() {
        return children;
    }

    public SimpleStringProperty nameProperty() {
        return nameProperty;
    }

}
