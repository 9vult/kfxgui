package moe.ninevolt.kfxgui.exporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moe.ninevolt.kfxgui.template.TemplateItem;

public class ExportItem {

    private String name;
    private List<ExportItem> children;
    private Map<String, String> paramMap;
    
    public ExportItem(TemplateItem ti) {
        this.name = ti.nameProperty().get();
        this.children = new ArrayList<>();
        this.paramMap = ti.getParamMap();

        for (TemplateItem child : ti.getChildren()) {
            this.children.add(new ExportItem(child));
        }
    }

    public String getName() {
        return this.name;
    }

    public List<ExportItem> getChildren() {
        return this.children;
    }

    public Map<String, String> getParamMap() {
        return this.paramMap;
    }

}
