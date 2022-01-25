package moe.ninevolt.kfxgui.gui;

import javafx.scene.control.TreeItem;
import moe.ninevolt.kfxgui.template.TemplateItem;

public class TemplateTreeItem<T> extends TreeItem<TemplateItem> {
    
    private T propertyValue;

    public TemplateTreeItem(TemplateItem item, T value) {
        super(item);
        this.propertyValue = value;
    }

    public static TemplateTreeItem<TemplateItem> baseItem(TemplateItem item) {
        return new TemplateTreeItem<TemplateItem>(item, item);
    }

    public T getPropertyValue() {
        return propertyValue;
    }
    
}
