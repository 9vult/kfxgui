package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import moe.ninevolt.kfxgui.plugins.Color;
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
    private LineComboBoxArea cba;
    private LineCodeArea lca;

    /**
     * Initialize the ParamArea
     * @param hostItem Item whose params are being hosted
     */
    public ParamArea(TemplateItem hostItem) {
        this.hostItem = hostItem;
        this.heading = new Heading();
        this.vertPaddingTop = new Region();
        this.vertPaddingMiddle = new Region();
        this.cba = new LineComboBoxArea();
        this.lca = new LineCodeArea();

        this.vertPaddingTop.setMinHeight(20);
        this.vertPaddingMiddle.setMinHeight(20);
        
        this.getChildren().addAll(vertPaddingTop, heading, vertPaddingMiddle);

        if (this.hostItem instanceof Line) {
            setUpLine();
            this.hostLine = true;
        } else {
            if (hostItem instanceof Color) { setUpColorEvent(); }
            else { setUpEvent(); }
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
    public LineComboBoxArea getComboBoxArea() {
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
     * Special setup procedure for the Color event
     */
    private void setUpColorEvent() {
        this.heading.setTitle(this.hostItem.nameProperty().get());
        ColorComboBoxArea ccba = new ColorComboBoxArea();
        Parameter cParam = new Parameter();
        ColorPreviewArea cpa = new ColorPreviewArea();
        Region r = new Region();
        r.setMinHeight(12);
        Region r2 = new Region();
        r2.setMinHeight(12);
        Region r3 = new Region();
        r3.setMinHeight(12);

        cParam.getInputArea().textProperty().addListener((obs, oldText, newText) -> {
            this.hostItem.setParam(Color.COLOR, newText);
            cpa.setColor(newText);
        });
        ccba.getTargetBox().setOnAction(e -> {
            this.hostItem.setParam(Color.TARGET, Color.targetMap.get(ccba.getTargetBox().getSelectionModel().getSelectedItem()));
            this.hostItem.setParam(Color.VERBOSE, ccba.getTargetBox().getSelectionModel().getSelectedItem());
        });

        cParam.getTitle().setText(Color.COLOR);
        cParam.getInputArea().setText(hostItem.getParamMap().get(Color.COLOR));

        ccba.getTargetBox().getSelectionModel().select(this.hostItem.getParamMap().get(Color.VERBOSE));
        this.getChildren().addAll(r, ccba, r2, cParam, r3, cpa);
    }

    /**
     * Setup procedure for Lines
     */
    private void setUpLine() {
        Line selectedLine = (Line)this.hostItem;
        this.heading.setTitle(selectedLine.nameProperty().get());
        cba.updateTypeList();
        getChildren().add(cba);
        cba.getTypeBox().getSelectionModel().select(selectedLine.getType().get().getName());
        cba.getTypeBox().setOnAction(e -> {
            selectedLine.setType(LineType.make(cba.getTypeBox().getSelectionModel().getSelectedItem()));
        });
        cba.getAdditionalField().textProperty().set(selectedLine.getParamMap().get(Line.ADDITIONAL));
        cba.getAdditionalField().textProperty().addListener((obs, oldVal, newVal) -> {
            selectedLine.setParam(Line.ADDITIONAL, newVal);
        });

        for (String pString : hostItem.getParams()) {
            if (pString.equals(Line.ADDITIONAL)) continue;
            if (pString.equals(Line.EFFECT)) continue;
            if (pString.equals(Line.CODE)) continue;

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

        Region codePadding = new Region();
        codePadding.setMinHeight(12);
        getChildren().add(codePadding);

        lca.getTextArea().setText(selectedLine.getParamMap().get(Line.CODE).replaceAll("§", "\n"));
        lca.getTextArea().textProperty().addListener((obs, oldVal, newVal) -> {
            selectedLine.setParam(Line.CODE, newVal.replaceAll("\n", "§"));
        });

        selectedLine.getType().addListener((obs, oldVal, newVal) -> {
            if (newVal.getName().toLowerCase().contains("code")) {
                if (!(getChildren().contains(lca))) getChildren().add(lca);
            } else {
                if (getChildren().contains(lca)) getChildren().remove(lca);
            }
        });

        if (cba.getTypeBox().getSelectionModel().getSelectedItem().toLowerCase().contains("code")) {
            if (!(getChildren().contains(lca))) getChildren().add(lca);
        } else {
            if (getChildren().contains(lca)) getChildren().remove(lca);
        }
    }

}
