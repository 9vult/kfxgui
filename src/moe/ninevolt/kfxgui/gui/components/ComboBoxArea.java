package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import moe.ninevolt.kfxgui.KfxGui;
import moe.ninevolt.kfxgui.template.Line;
import moe.ninevolt.kfxgui.template.LineType;

public class ComboBoxArea extends HBox {

    private VBox left;
    private VBox right;
    private Label typeLabel;
    private ComboBox<String> typeBox;
    private Label additionalLabel;
    private TextField additionalField;

    private Region paddingLeft = new Region();
    private Region paddingMiddle = new Region();
    private Region paddingRight = new Region();

    public ComboBoxArea() {
        this.left = new VBox();
        this.right = new VBox();
        this.typeLabel = new Label("Line Type");
        this.typeBox = new ComboBox<>();
        this.additionalLabel = new Label(Line.ADDITIONAL);
        this.additionalField = new TextField();

        paddingLeft.setMinWidth(12);
        paddingMiddle.setMinWidth(12);
        paddingRight.setMinWidth(12);

        left.getChildren().addAll(typeLabel, typeBox);
        right.getChildren().addAll(additionalLabel, additionalField);
        HBox.setHgrow(typeBox, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        this.getChildren().addAll(paddingLeft, left, paddingMiddle, right, paddingRight);
    }

    public void updateTypeList() {
        String prevText = typeBox.getValue();
        typeBox.getItems().clear();
        for (String type : LineType.getTypes(KfxGui.getCurrentProject().getTargetTemplater())) {
            typeBox.getItems().add(type);
        }
        typeBox.setValue(prevText);
    }

    public ComboBox<String> getTypeBox() {
        return this.typeBox;
    }

    public TextField getAdditionalField() {
        return this.additionalField;
    }
    
}
