package moe.ninevolt.kfxgui.gui;

import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moe.ninevolt.kfxgui.KfxGui;
import moe.ninevolt.kfxgui.plugins.Plugin;
import moe.ninevolt.kfxgui.plugins.Transform;
import moe.ninevolt.kfxgui.template.Line;
import moe.ninevolt.kfxgui.template.LineType;
import moe.ninevolt.kfxgui.template.TemplateItem;

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
    MenuItem nMI;
    MenuItem saveMI;
    MenuItem openMI;
    
    Menu editMenu;
    
    VBox toolbox;
    Label toolboxLabel;
    ListView<Plugin> pluginList;

    VBox treeBox;
    HBox treeHBox;
    Label treeLabel;
    Label addLineLabel;
    TreeView<TemplateItem> tree;

    @Override
    public void start(Stage window) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);
        // Initialize layout components
        bp = new BorderPane();
        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        nMI = new MenuItem("New");
        openMI = new MenuItem("Open");
        saveMI = new MenuItem("Save");
        editMenu = new Menu("Edit");
        toolbox = new VBox();
        toolboxLabel = new Label("Toolbox");
        pluginList = new ListView<>();
        treeBox = new VBox();
        treeHBox = new HBox();
        addLineLabel = new Label("+ ");
        treeLabel = new Label("Template Tree");
        tree = new TreeView<>();

        // Set up layout
        window.setTitle("9volt GUI Karaoke Template Builder");
        
        fileMenu.getItems().addAll(nMI, openMI, saveMI);
        menuBar.getMenus().addAll(fileMenu, editMenu);
        bp.setTop(menuBar);

        // Toolbox

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
                    Plugin genericPlugin = pluginList.getSelectionModel().getSelectedItem();
                    
                    TreeItem<TemplateItem> cItem = tree.getSelectionModel().getSelectedItem();
                    if (cItem == null) return;
                    if ((cItem.getValue().toString().equals(Transform.NAME) && genericPlugin.isTransform()) 
                            || cItem.getValue() instanceof Line) {
                        // Add to the current line or transform
                        Plugin selectedPlugin = new Plugin(cItem.getValue(), genericPlugin);
                        cItem.getChildren().add(new TemplateTreeItem<String>(selectedPlugin, selectedPlugin.getName()));
                        cItem.getValue().getChildren().add(selectedPlugin);
                    } else { // Add to the parent line or transform
                        Plugin selectedPlugin = new Plugin(cItem.getParent().getValue(), genericPlugin);
                        cItem.getParent().getChildren().add(new TemplateTreeItem<String>(selectedPlugin, selectedPlugin.getName()));
                        cItem.getParent().getValue().getChildren().add(selectedPlugin);
                    }
                    
                }
            }            
        });
        toolbox.getChildren().addAll(toolboxLabel, pluginList);
        VBox.setVgrow(pluginList, Priority.ALWAYS);
        bp.setLeft(toolbox);

        // Effect Tree

        tree.setShowRoot(false);
        TreeItem<TemplateItem> treeRoot = new TreeItem<>();
        tree.setRoot(treeRoot);
        TemplateItem newLine = new Line(null, LineType.TEMPLATE, "New Line");
        TreeItem<TemplateItem> lineItem = TemplateTreeItem.baseItem(newLine);
        treeRoot.getChildren().add(lineItem);
        tree.getSelectionModel().select(lineItem);
        
        treeLabel.setStyle("-fx-font-size: 16;");
        addLineLabel.setStyle("-fx-font-size: 16; -fx-text-fill: blue;");
        addLineLabel.setTooltip(new Tooltip("Add Line..."));
        Region spacerRegion = new Region();
        treeHBox.getChildren().addAll(treeLabel, spacerRegion, addLineLabel);
        HBox.setHgrow(spacerRegion, Priority.ALWAYS);

        addLineLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    TemplateItem newLine = new Line(null, LineType.TEMPLATE, "New Line");
                    TreeItem<TemplateItem> lineItem = TemplateTreeItem.baseItem(newLine);
                    treeRoot.getChildren().add(lineItem);
                }
            }
        });

        treeBox.getChildren().addAll(treeHBox, tree);
        VBox.setVgrow(tree, Priority.ALWAYS);
        bp.setRight(treeBox);

        Scene rootScene = new Scene(bp, 1280, 720);
        window.setScene(rootScene);
        window.show();
    }
}
