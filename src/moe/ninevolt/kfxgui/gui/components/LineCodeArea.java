package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Code editing area for code lines
 * 
 * @author 9volt
 * @since 2022/01/30
 */
public class LineCodeArea extends HBox {

    private Label heading;
    private TextArea textArea;

    private Region paddingLeft = new Region();
    private Region paddingRight = new Region();

    /**
     * Initialize the ComboBoxArea
     */
    public LineCodeArea() {
        this.heading = new Label("Code");
        this.textArea = new TextArea();
        this.heading.setMinWidth(100);
        this.textArea.setMinHeight(100);

        this.textArea.setStyle("-fx-font-family: 'Consolas', serif; -fx-font-size: 14");

        paddingLeft.setMinWidth(12);
        paddingRight.setMinWidth(12);

        HBox.setHgrow(textArea, Priority.ALWAYS);

        this.getChildren().addAll(paddingLeft, heading, textArea, paddingRight);
    }

    /**
     * @return The text area
     */
    public TextArea getTextArea() {
        return this.textArea;
    }

}
