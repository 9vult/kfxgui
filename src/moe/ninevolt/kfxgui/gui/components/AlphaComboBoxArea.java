package moe.ninevolt.kfxgui.gui.components;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import moe.ninevolt.kfxgui.plugins.Alpha;

/**
 * The Alpha Combo Box Area is
 * a special combobox for the alpha
 * effect, for listing alpha targets
 * 
 * @author 9volt
 * @since 2022/01/31
 */
public class AlphaComboBoxArea extends HBox {

    private Label heading;
    private ComboBox<String> targetBox;

    private Region paddingLeft = new Region();
    private Region paddingRight = new Region();

    /**
     * Initialize the ComboBoxArea
     */
    public AlphaComboBoxArea() {
        this.heading = new Label(Alpha.TARGET);
        this.targetBox = new ComboBox<>();
        this.heading.setMinWidth(100);

        ArrayList<String> targetList = new ArrayList<>();
        targetList.addAll(Alpha.targetMap.keySet());
        Collections.sort(targetList);
        targetBox.getItems().addAll(targetList);

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
