package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import moe.ninevolt.kfxgui.plugins.Color;

/**
 * The Color Combo Box Area is
 * a special combobox for the color
 * effect, for listing color targets
 * 
 * @author 9volt
 * @since 2022/01/30
 */
public class ColorComboBoxArea extends HBox {

    private Label heading;
    private ComboBox<String> targetBox;

    private Region paddingLeft = new Region();
    private Region paddingRight = new Region();

    /**
     * Initialize the ComboBoxArea
     */
    public ColorComboBoxArea() {
        this.heading = new Label(Color.TARGET);
        this.targetBox = new ComboBox<>();
        this.heading.setMinWidth(100);

        targetBox.getItems().addAll(Color.targetMap.keySet());

        paddingLeft.setMinWidth(12);
        paddingRight.setMinWidth(12);

        this.getChildren().addAll(paddingLeft, heading, targetBox, paddingRight);
    }

    /**
     * @return Reference to the ComboBox
     */
    public ComboBox<String> getTargetBox() {
        return this.targetBox;
    }
    
}
