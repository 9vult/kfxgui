package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import moe.ninevolt.kfxgui.template.Line;
import moe.ninevolt.kfxgui.template.LineType;
import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * The parameter area is where the user
 * inputs values for each parameter
 * 
 * @author 9volt
 * @since 2022/01/29
 */
public class ParamArea extends VBox {
    
    private Heading heading;
    private Region vertPaddingTop;
    private Region vertPaddingMiddle;
    private TemplateItem hostItem;
    private boolean hostLine;
    private ComboBoxArea cba;

    /**
     * Initialize the ParamArea
     * @param hostItem Item whose params are being hosted
     */
    public ParamArea(TemplateItem hostItem) {
        this.hostItem = hostItem;
        this.heading = new Heading();
        this.vertPaddingTop = new Region();
        this.vertPaddingMiddle = new Region();
        this.cba = new ComboBoxArea();

        this.vertPaddingTop.setMinHeight(20);
        this.vertPaddingMiddle.setMinHeight(20);
        
        this.getChildren().addAll(vertPaddingTop, heading, vertPaddingMiddle);

        if (this.hostItem instanceof Line) {
            setUpLine();
            this.hostLine = true;
        } else {
            setUpEvent();
            this.hostLine = false;
        } 
    }

    /**
     * @return True if the host item is a Line
     */
    public boolean isHostLine() {
        return this.hostLine;
    }

    /**
     * @return The heading
     */
    public Heading getHeading() {
        return this.heading;
    }

    /**
     * @return Reference to the ComboBoxArea
     */
    public ComboBoxArea getComboBoxArea() {
        return this.cba;
    }

    /**
     * Setup procedure for Events
     */
    private void setUpEvent() {
        this.heading.setTitle(this.hostItem.nameProperty().get());
        for (String pString : hostItem.getParams()) {
            Region r = new Region();
            r.setMinHeight(12);

            Parameter p = new Parameter();
            p.getTitle().setText(pString);
            p.getInputArea().setText(hostItem.getParamMap().get(pString));
            p.getInputArea().textProperty().addListener((obs, oldText, newText) -> {
                this.hostItem.setParam(pString, newText);
            });
            this.getChildren().addAll(r, p);
        }
    }

    /**
     * Setup procedure for Lines
     */
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

            Region r = new Region();
            r.setMinHeight(12);

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
            this.getChildren().addAll(r, p);
        }
    }

}
