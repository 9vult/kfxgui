package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Heading extends HBox {
    
    private Region headingLeft;
    private Region headingRight;
    private Label titleLabel;

    public Heading() {
        this.headingLeft = new Region();
        this.headingRight = new Region();
        this.titleLabel = new Label();
        setup();
    }

    private void setup() {
        titleLabel.setStyle("-fx-font-size: 22; -fx-font-weight: Bold;");
        this.getChildren().addAll(headingLeft, titleLabel, headingRight);
        HBox.setHgrow(headingLeft, Priority.ALWAYS);
        HBox.setHgrow(headingRight, Priority.ALWAYS);
    }

    public void setTitle(String newText) {
        this.titleLabel.setText(newText);
    }

}
