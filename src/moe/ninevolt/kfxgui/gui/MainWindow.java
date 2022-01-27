package moe.ninevolt.kfxgui.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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
    MenuItem atarashiiMI;
    MenuItem saveMI;
    MenuItem saveAsMI;
    MenuItem openMI;
    MenuItem exportMI;
    
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
        atarashiiMI = new MenuItem("New");
        openMI = new MenuItem("Open");
        saveMI = new MenuItem("Save");
        saveAsMI = new MenuItem("Save As...");
        exportMI = new MenuItem("Export...");
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
        
        fileMenu.getItems().addAll(atarashiiMI, openMI, saveMI, saveAsMI, exportMI);
        menuBar.getMenus().addAll(editMenu);
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
                    } else { // Cannot add to a "regular" plugin, go up a level
                        Plugin selectedPlugin = new Plugin(cItem.getParent().getValue(), genericPlugin);
                        if (cItem.getParent().getValue().toString().equals(Transform.NAME) && selectedPlugin.getName().equals(Transform.NAME)) {
                            // Need to go up to grandparent level, can't add transform to transform!
                            cItem.getParent().getParent().getChildren().add(new TemplateTreeItem<String>(selectedPlugin, selectedPlugin.getName()));
                            cItem.getParent().getParent().getValue().getChildren().add(selectedPlugin);
                        } else {
                            // Add to the parent level
                            cItem.getParent().getChildren().add(new TemplateTreeItem<String>(selectedPlugin, selectedPlugin.getName()));
                            cItem.getParent().getValue().getChildren().add(selectedPlugin);
                        }
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
        TreeItem<TemplateItem> defaultRootLineItem = TemplateTreeItem.baseItem(newLine);
        treeRoot.getChildren().add(defaultRootLineItem);
        
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

        VBox paramGrandparentVBox = new VBox();
        tree.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
            // Clear the box
            paramGrandparentVBox.getChildren().clear();
            Region vPaddingBeeg = new Region();
            vPaddingBeeg.setMinHeight(20);
            
            // Heading
            HBox heading = new HBox();
            Region headLeft = new Region();
            Region headRight = new Region();
            Label titleLabel = new Label();
            heading.getChildren().addAll(headLeft, titleLabel, headRight);
            HBox.setHgrow(headLeft, Priority.ALWAYS);
            HBox.setHgrow(headRight, Priority.ALWAYS);
            titleLabel.setStyle("-fx-font-size: 22; -fx-font-weight: Bold;");
            paramGrandparentVBox.getChildren().addAll(vPaddingBeeg, heading);

            TemplateItem selectedItem = newValue.getValue();
            if (selectedItem instanceof Plugin) {
                Plugin selectedPlugin = (Plugin)selectedItem;
                titleLabel.setText(selectedPlugin.getName());
                for (String parameter : selectedPlugin.getParams()) {
                    HBox paramParentHBox = new HBox();
                    Label paramTitle = new Label(parameter);
                    TextField paramInput = new TextField();
                    paramInput.setText(selectedPlugin.getParamMap().get(parameter));
                    paramInput.textProperty().addListener((observableText, oldValueText, newValueText) -> {
                        selectedPlugin.setParam(parameter, newValueText);
                    });
                    paramTitle.setMinWidth(100);
                    HBox.setHgrow(paramInput, Priority.ALWAYS);
                    // Padding, temporary
                    Region vPaddingSmol = new Region();
                    Region hPaddingLeft = new Region();
                    Region hPaddingRight = new Region();
                    vPaddingSmol.setMinHeight(10);
                    hPaddingLeft.setMinWidth(12);
                    hPaddingRight.setMinWidth(12);
                    //
                    paramParentHBox.getChildren().addAll(hPaddingLeft, paramTitle, paramInput, hPaddingRight);
                    paramGrandparentVBox.getChildren().addAll(vPaddingSmol, paramParentHBox);
                }
            } else if (selectedItem instanceof Line) {
                Line selectedLine = (Line)selectedItem;
                titleLabel.setText(selectedLine.getName());
            }
        });

        bp.setCenter(paramGrandparentVBox);

        treeBox.getChildren().addAll(treeHBox, tree);
        VBox.setVgrow(tree, Priority.ALWAYS);
        bp.setRight(treeBox);

        // Window Finalization
        
        tree.getSelectionModel().select(defaultRootLineItem);
        Scene rootScene = new Scene(bp, 1100, 605);
        window.setScene(rootScene);
        window.show();
    }
}
