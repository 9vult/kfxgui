package moe.ninevolt.kfxgui.gui;

import java.util.Collections;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moe.ninevolt.kfxgui.KfxGui;
import moe.ninevolt.kfxgui.plugins.Plugin;

/**
 * MainWindow.java
 * Author: 9volt
 * Created: 2022/01/23
 */
public class MainWindow extends Application {

    // Layout definitions
    
    BorderPane bp;
    MenuBar menuBar;
    Menu fileMenu;
    MenuItem newMI;
    MenuItem saveMI;
    MenuItem openMI;
    
    Menu editMenu;
    
    VBox toolbox;
    Label toolboxLabel;
    ListView<Plugin> pluginList;

    @Override
    public void start(Stage window) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);
        // Initialize layout components
        bp = new BorderPane();
        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        newMI = new MenuItem("New");
        openMI = new MenuItem("Open");
        saveMI = new MenuItem("Save");
        editMenu = new Menu("Edit");
        toolbox = new VBox();
        toolboxLabel = new Label("Toolbox");
        pluginList = new ListView<>();
        
        // Set up layout
        window.setTitle("9volt GUI Karaoke Templator");
        
        fileMenu.getItems().addAll(newMI, openMI, saveMI);
        menuBar.getMenus().addAll(fileMenu, editMenu);
        bp.setTop(menuBar);

        pluginList.getItems().addAll(KfxGui.getPluginLoader().getSortedPlugins());
        toolboxLabel.setStyle("-fx-font-size: 16;");
        pluginList.setCellFactory(lc -> new ListCell<Plugin>() {
            final Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Plugin item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.getName());
                    tooltip.setText(item.getDescription());
                    setTooltip(tooltip);
                }
            }
        });
        pluginList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Plugin selected = pluginList.getSelectionModel().getSelectedItem();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Event add");
                    alert.setHeaderText(String.format("Adding event \"%s\"", selected.getName()));
                    alert.setContentText(String.format("Plugin Author: %s\nDescription: %s", selected.getAuthor(), selected.getDescription()));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {}});
                }
            }            
        });
        toolbox.getChildren().addAll(toolboxLabel, pluginList);
        VBox.setVgrow(pluginList, Priority.ALWAYS);
        bp.setLeft(toolbox);


        Scene rootScene = new Scene(bp, 1280, 720);
        window.setScene(rootScene);
        window.show();
    }
}
