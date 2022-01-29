package moe.ninevolt.kfxgui.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * A TemplateItem is the basis for all
 * useful tools in KFX-GUI, including
 * Plugins, Lines, and the Transform
 * "plugin"
 * 
 * @author 9volt
 * @since 2022/01/25
 */
public abstract class TemplateItem {
    
    protected TemplateItem parent;
    protected SimpleStringProperty name;
    protected SimpleStringProperty description;
    protected SimpleBooleanProperty transform;
    protected List<TemplateItem> children;
    protected Map<String, String> paramMap;
    protected final List<String> params;

    /**
     * Create a new TemplateItem
     * @param parent Parent item
     * @param params Parameters for this item
     * @param name Name of this item
     */
    public TemplateItem(TemplateItem parent, List<String> params, String name, String description, boolean transform) {
        this.parent = parent;
        this.params = params;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.transform = new SimpleBooleanProperty(transform);
        this.children = new ArrayList<>();
        this.paramMap = new HashMap<>();
    }

    /**
     * Get the parent item of this item.
     * For Lines, this should be null.
     * @return The parent
     */
    public TemplateItem getParent() {
        return this.parent;
    }

    public void setParent(TemplateItem parent) {
        this.parent = parent;
    }

    /**
     * Get the children this item contains
     * @return The children
     */
    public List<TemplateItem> getChildren() {
        return this.children;
    }

    /**
     * Get the name of this item
     * @return Name Property
     */
    public SimpleStringProperty nameProperty() {
        return this.name;
    }

    /**
     * Get the description of this item
     * @return Description Property
     */
    public SimpleStringProperty descriptionProperty() {
        return this.description;
    }

    /**
     * Get if this item can be placed in a transform
     * @return Transform Property
     */
    public SimpleBooleanProperty transformProperty() {
        return this.transform;
    }

    /**
     * Get the list of parameters used by this item
     * @return List of parameters
     */
    public List<String> getParams() {
        return this.params;
    }

    /**
     * Set a parameter
     * @param key Name of the parameter to set
     * @param value Value to set to
     */
    public void setParam(String key, String value) {
        this.paramMap.put(key, value);
    } 

    public Map<String, String> getParamMap() {
        return this.paramMap;
    }

    @Override
    public String toString() {
        return this.name.get();
    }

    /**
     * Get the resulting template code produced by this item 
     * @return The template code, using <code>?</code> in place of <code>\</code>
     */
    public abstract String getFormattedResult();

}
