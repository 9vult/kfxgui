package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * A Parameter provides a label and a text entry field
 * for supplying values to a TemplateItem
 * 
 * @author 9volt
 * @since 2022/01/29
 */
public class Parameter extends HBox {

    private Label title;
    private TextField inputArea;
    private Region paddingLeft = new Region();
    private Region paddingRight = new Region();

    /**
     * Initialize a Parameter
     */
    public Parameter() {
        this.title = new Label();
        this.inputArea = new TextField();
        this.paddingLeft = new Region();
        this.paddingRight = new Region();

        this.title.setMinWidth(100);
        this.paddingLeft.setMinWidth(12);
        this.paddingRight.setMinWidth(12);

        HBox.setHgrow(inputArea, Priority.ALWAYS);
        this.getChildren().addAll(paddingLeft, title, inputArea, paddingRight);
    }

    /**
     * @return Reference to the Title
     */
    public Label getTitle() {
        return this.title;
    }

    /**
     * @return Reference to the InputArea
     */
    public TextField getInputArea() {
        return this.inputArea;
    }

}
