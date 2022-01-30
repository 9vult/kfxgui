package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import moe.ninevolt.kfxgui.template.Swatch;

/**
 * Preview pane for displaying colors
 * 
 * @author 9volt
 * @since 2022/01/30
 */
public class ColorPreviewArea extends HBox {

    private Label heading;
    private Pane previewPane;

    private Region paddingLeft = new Region();
    private Region paddingRight = new Region();

    /**
     * Initialize the ComboBoxArea
     */
    public ColorPreviewArea() {
        this.heading = new Label("Preview");
        this.previewPane = new Pane();
        this.heading.setMinWidth(100);

        paddingLeft.setMinWidth(12);
        paddingRight.setMinWidth(12);

        previewPane.setMinWidth(50);
        previewPane.setMinHeight(5);
        previewPane.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.BLACK,
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        HBox.setHgrow(previewPane, Priority.ALWAYS);

        this.getChildren().addAll(paddingLeft, heading, previewPane, paddingRight);
    }

    public void setColor(String color) {
        previewPane.setStyle("-fx-background-color: #FFFFFF");
        if (color.startsWith("@")) {
            String sName = color.substring(1);
            for (Swatch s : Swatch.swatches) {
                if (s.getName().equals(sName)) {
                    if (s.htmlColor.get().length() == 7) {
                        previewPane.setStyle("-fx-background-color: " + s.htmlColor.get());
                    }
                    break;
                }
            }
        } else if (color.length() == 9 && color.startsWith("&") && color.endsWith("&")) {
            String htmlColor = Swatch.convertColor(color);
            if (!htmlColor.equals("")) previewPane.setStyle("-fx-background-color: " + htmlColor);
        }
    }
    
}
