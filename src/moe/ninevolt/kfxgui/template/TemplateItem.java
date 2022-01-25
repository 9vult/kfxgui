package moe.ninevolt.kfxgui.template;

import java.util.ArrayList;
import java.util.List;

/**
 * TemplateItem.java
 * Author: 9volt
 * Created: 2022/01/25
 */
public class TemplateItem {
    
    private TemplateItem parent;
    private List<TemplateItem> children;

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

}
