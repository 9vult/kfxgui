package moe.ninevolt.kfxgui.gui;

import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.scene.control.TreeItem;
import moe.ninevolt.kfxgui.template.TemplateItem;

public class TemplateTreeItem<T> extends TreeItem<TemplateItem> {
    
    private T propertyValue;

    public TemplateTreeItem(TemplateItem item, T value) {
        super(item);
        this.propertyValue = value;
        item.nameProperty().addListener(nameListener);
        this.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.nameProperty().removeListener(nameListener);
            }
            if (newValue != null) {
                newValue.nameProperty().addListener(nameListener);
            }
        });

    }

    public static TemplateTreeItem<TemplateItem> baseItem(TemplateItem item) {
        return new TemplateTreeItem<TemplateItem>(item, item);
    }

    public T getPropertyValue() {
        return propertyValue;
    }

    private ChangeListener<String> nameListener = (obs, oldName, newName) -> {
        TreeModificationEvent<TemplateItem> event = new TreeModificationEvent<>(TreeItem.valueChangedEvent(), this);
        Event.fireEvent(this, event);
    };
    
}
