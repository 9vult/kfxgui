package moe.ninevolt.kfxgui.gui;

import java.util.ArrayList;
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
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
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
import moe.ninevolt.kfxgui.gui.components.ParamArea;
import moe.ninevolt.kfxgui.plugins.Plugin;
import moe.ninevolt.kfxgui.plugins.Transform;
import moe.ninevolt.kfxgui.template.Line;
import moe.ninevolt.kfxgui.template.LineType;
import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * Main GUI window for
 * 
 * @author 9volt
 * @since 2022/01/23
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
    MenuItem exportsMI;
    MenuItem exportfMI;
    
    Menu projectMenu;
    Menu targetMI;
    ToggleGroup targetTG;
    List<RadioMenuItem> targetMenuItems;
    
    VBox toolbox;
    Label toolboxLabel;
    ListView<TemplateItem> pluginList;

    VBox treeBox;
    HBox treeHBox;
    Label treeLabel;
    Label addLineLabel;
    TreeView<TemplateItem> tree;

    ParamArea currentDisplay;

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
        exportsMI = new MenuItem("Export Text");
        exportfMI = new MenuItem("Export File...");
        projectMenu = new Menu("Project");
        targetMI = new Menu("Set Target Templater...");
        targetTG = new ToggleGroup();
        targetMenuItems = new ArrayList<>();
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
        
        fileMenu.getItems().addAll(atarashiiMI, openMI, saveMI, saveAsMI, new SeparatorMenuItem(), exportsMI, exportfMI);
        projectMenu.getItems().addAll(targetMI);

        for (String templater : LineType.getTargets()) {
            RadioMenuItem target = new RadioMenuItem(templater);
            target.setToggleGroup(targetTG);
            targetMI.getItems().add(target);
            if (templater.equals(KfxGui.getCurrentProject().getTargetTemplater())) target.setSelected(true);
            target.setOnAction((e) -> {
                KfxGui.getCurrentProject().setTargetTemplater(target.getText());
                if (currentDisplay != null)
                    currentDisplay.getComboBoxArea().updateTypeList();
            });
        }

        menuBar.getMenus().addAll(fileMenu, projectMenu);
        bp.setTop(menuBar);

        // MenuStrip
        exportsMI.setOnAction(e -> {
            for (TreeItem<TemplateItem> treeItem : tree.getRoot().getChildren()) {
                TemplateItem templateItem = treeItem.getValue();
                System.out.println(Plugin.normalizeOutput(templateItem.getFormattedResult(), false));
            }
        });

        // Toolbox

        pluginList.getItems().addAll(KfxGui.getPluginLoader().getSortedPlugins());
        toolboxLabel.setStyle("-fx-font-size: 16;");
        pluginList.setCellFactory(lc -> new ListCell<TemplateItem>() {
            final Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(TemplateItem item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.nameProperty().get());
                    tooltip.setText(item.descriptionProperty().get());
                    setTooltip(tooltip);
                }
            }
        });
        pluginList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TemplateItem selectedItem = pluginList.getSelectionModel().getSelectedItem();                    
                    TreeItem<TemplateItem> curItem = tree.getSelectionModel().getSelectedItem();
                    TemplateTreeItem<String> addingItem;

                    if (curItem == null) return;
                    if ((curItem.getValue() instanceof Transform && selectedItem.transformProperty().get()) 
                            || curItem.getValue() instanceof Line) {
                        // Add to the current line or transform
                        TemplateItem newItem = KfxGui.getPluginLoader().create(curItem.getValue(), selectedItem.nameProperty().get());
                        addingItem = new TemplateTreeItem<>(newItem, newItem.nameProperty().get());
                        curItem.getChildren().add(addingItem);
                        curItem.getValue().getChildren().add(newItem);

                    } else { // Cannot add to a "regular" plugin, go up a level
                    TemplateItem newItem = KfxGui.getPluginLoader().create(curItem.getParent().getValue(), selectedItem.nameProperty().get());
                        if (curItem.getParent().getValue() instanceof Transform && newItem instanceof Transform) {
                            // Need to go up to grandparent level, can't add transform to transform!
                            addingItem = new TemplateTreeItem<>(newItem, newItem.nameProperty().get());
                            curItem.getParent().getParent().getChildren().add(addingItem);
                            curItem.getParent().getParent().getValue().getChildren().add(newItem);
                        } else {
                            // Add to the parent level
                            addingItem = new TemplateTreeItem<>(newItem, newItem.nameProperty().get());
                            curItem.getParent().getChildren().add(addingItem);
                            curItem.getParent().getValue().getChildren().add(newItem);
                        }
                    }
                    // Select it (and expand parents)
                    for (TreeItem<TemplateItem> ti = addingItem; ti.getParent() != null; ti = ti.getParent()) {
                        ti.getParent().setExpanded(true);
                    }
                    tree.getSelectionModel().select(tree.getRow(addingItem));
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
        TemplateItem newLine = new Line(LineType.make("template syl"), "New Line");
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
                    TemplateItem newLine = new Line(LineType.make("template syl"), "New Line");
                    TreeItem<TemplateItem> lineItem = TemplateTreeItem.baseItem(newLine);
                    treeRoot.getChildren().add(lineItem);
                }
            }
        });

        // Parameter Input Area

        tree.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
            TemplateItem selectedItem = newValue.getValue();
            currentDisplay = new ParamArea(selectedItem);
            bp.setCenter(currentDisplay);
        });
        

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
