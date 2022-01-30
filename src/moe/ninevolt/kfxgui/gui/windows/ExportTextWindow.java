package moe.ninevolt.kfxgui.gui.windows;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Window that displays the exported text generated
 * 
 * @author 9volt
 * @since 2022/01/29
 */
public class ExportTextWindow extends Stage {
    
    private TextArea textArea;
    private Scene scene;

    /**
     * Initialize the window
     * @param displayText Text to display
     */
    public ExportTextWindow(String displayText) {
        this.textArea = new TextArea();
        this.scene = new Scene(textArea, 700, 200);
        this.textArea.setStyle("-fx-font-family: 'Consolas', serif; -fx-font-size: 14");
        this.textArea.setText(displayText);
        this.textArea.setEditable(false);
        this.setScene(scene);
        this.setTitle("Export Text");
    }

}
