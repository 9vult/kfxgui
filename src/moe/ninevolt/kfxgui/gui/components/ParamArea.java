package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import moe.ninevolt.kfxgui.template.Line;
import moe.ninevolt.kfxgui.template.LineType;
import moe.ninevolt.kfxgui.template.TemplateItem;

public class ParamArea extends VBox {
    
    private Heading heading;
    private Region vertPaddingTop;
    private TemplateItem hostItem;
    private boolean hostLine;
    private ComboBoxArea cba;

    public ParamArea(TemplateItem hostItem) {
        this.hostItem = hostItem;
        this.heading = new Heading();
        this.vertPaddingTop = new Region();
        this.cba = new ComboBoxArea();

        this.vertPaddingTop.setMinHeight(20);
        
        this.getChildren().addAll(vertPaddingTop, heading);

        if (this.hostItem instanceof Line) {
            setUpLine();
            this.hostLine = true;
        } else {
            setUpEvent();
            this.hostLine = false;
        } 
    }

    public boolean isHostLine() {
        return this.hostLine;
    }

    public Heading getHeading() {
        return this.heading;
    }

    private void setUpEvent() {
        this.heading.setTitle(this.hostItem.nameProperty().get());
        for (String pString : hostItem.getParams()) {
            Parameter p = new Parameter();
            p.getTitle().setText(pString);
            p.getInputArea().setText(hostItem.getParamMap().get(pString));
            p.getInputArea().textProperty().addListener((obs, oldText, newText) -> {
                this.hostItem.setParam(pString, newText);
            });
            this.getChildren().add(p);
        }
    }

    private void setUpLine() {
        Line selectedLine = (Line)this.hostItem;
        this.heading.setTitle(selectedLine.nameProperty().get());
        cba.updateTypeList();
        getChildren().add(cba);
        cba.getTypeBox().getSelectionModel().select(selectedLine.getType().getName());
        cba.getTypeBox().setOnAction(e -> {
            selectedLine.setType(LineType.make(cba.getTypeBox().getSelectionModel().getSelectedItem()));
        });
        
        for (String pString : hostItem.getParams()) {
            if (pString.equals(Line.ADDITIONAL)) continue;
            Parameter p = new Parameter();
            p.getTitle().setText(pString);
            p.getInputArea().setText(hostItem.getParamMap().get(pString));
            p.getInputArea().textProperty().addListener((obs, oldText, newText) -> {
                this.hostItem.setParam(pString, newText);
                if (pString.equals("Name")) {
                    heading.setTitle(newText);
                    hostItem.nameProperty().set(newText);
                }
            });
            this.getChildren().add(p);
        }
    }

    public ComboBoxArea getComboBoxArea() {
        return this.cba;
    }

}
