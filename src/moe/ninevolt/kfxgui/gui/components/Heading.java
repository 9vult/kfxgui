package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * The heading is the part of the parameter
 * area with the title display
 * 
 * @author 9volt
 * @since 2022/01/29
 */
public class Heading extends HBox {
    
    private Region headingLeft;
    private Region headingRight;
    private Label titleLabel;

    /**
     * Initialize the heading
     */
    public Heading() {
        this.headingLeft = new Region();
        this.headingRight = new Region();
        this.titleLabel = new Label();
        setup();
    }

    /**
     * Set up the layout
     */
    private void setup() {
        titleLabel.setStyle("-fx-font-size: 22; -fx-font-weight: Bold;");
        this.getChildren().addAll(headingLeft, titleLabel, headingRight);
        HBox.setHgrow(headingLeft, Priority.ALWAYS);
        HBox.setHgrow(headingRight, Priority.ALWAYS);
    }

    /**
     * Set the title of the heading
     * @param newText Title to set to
     */
    public void setTitle(String newText) {
        this.titleLabel.setText(newText);
    }

}
