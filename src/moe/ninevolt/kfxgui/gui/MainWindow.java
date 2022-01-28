package moe.ninevolt.kfxgui.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
    
    Menu editMenu;
    
    VBox toolbox;
    Label toolboxLabel;
    ListView<TemplateItem> pluginList;

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
        exportsMI = new MenuItem("Export Text");
        exportfMI = new MenuItem("Export File...");
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
        
        fileMenu.getItems().addAll(atarashiiMI, openMI, saveMI, saveAsMI, new SeparatorMenuItem(), exportsMI, exportfMI);
        menuBar.getMenus().addAll(fileMenu, editMenu);
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
                    
                    TreeItem<TemplateItem> cItem = tree.getSelectionModel().getSelectedItem();
                    if (cItem == null) return;
                    if ((cItem.getValue() instanceof Transform && selectedItem.transformProperty().get()) 
                            || cItem.getValue() instanceof Line) {
                        // Add to the current line or transform
                        TemplateItem newItem = KfxGui.getPluginLoader().create(cItem.getValue(), selectedItem.nameProperty().get());
                        cItem.getChildren().add(new TemplateTreeItem<String>(newItem, newItem.nameProperty().get()));
                        cItem.getValue().getChildren().add(newItem);
                    } else { // Cannot add to a "regular" plugin, go up a level
                    TemplateItem newItem = KfxGui.getPluginLoader().create(cItem.getParent().getValue(), selectedItem.nameProperty().get());
                        if (cItem.getParent().getValue() instanceof Transform && newItem instanceof Transform) {
                            // Need to go up to grandparent level, can't add transform to transform!
                            cItem.getParent().getParent().getChildren().add(new TemplateTreeItem<String>(newItem, newItem.nameProperty().get()));
                            cItem.getParent().getParent().getValue().getChildren().add(newItem);
                        } else {
                            // Add to the parent level
                            cItem.getParent().getChildren().add(new TemplateTreeItem<String>(newItem, newItem.nameProperty().get()));
                            cItem.getParent().getValue().getChildren().add(newItem);
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
        TemplateItem newLine = new Line(LineType.TEMPLATE_SYL, "New Line");
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
                    TemplateItem newLine = new Line(LineType.TEMPLATE_SYL, "New Line");
                    TreeItem<TemplateItem> lineItem = TemplateTreeItem.baseItem(newLine);
                    treeRoot.getChildren().add(lineItem);
                }
            }
        });

        // Parameter Input Area

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
            if (!(selectedItem instanceof Line)) {
                titleLabel.setText(selectedItem.nameProperty().get());
                for (String parameter : selectedItem.getParams()) {
                    HBox paramParentHBox = new HBox();
                    Label paramTitle = new Label(parameter);
                    TextField paramInput = new TextField();
                    paramInput.setText(selectedItem.getParamMap().get(parameter));
                    paramInput.textProperty().addListener((observableText, oldValueText, newValueText) -> {
                        selectedItem.setParam(parameter, newValueText);
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
            } else {
                Line selectedLine = (Line)selectedItem;
                titleLabel.setText(selectedLine.nameProperty().get());
                // Line Type ComboBox
                HBox typeHBox = new HBox();
                Label typeLabel = new Label("Line Type");
                ComboBox<LineType> typeBox = new ComboBox<>();
                for (LineType type : LineType.values()) {
                    typeBox.getItems().add(type);
                }
                Region typePaddingV = new Region();
                Region typePaddingHleft = new Region();
                Region typePaddingHright = new Region();
                typePaddingV.setMinHeight(10);
                typePaddingHleft.setMinWidth(12);
                typePaddingHright.setMinWidth(12);
                typeLabel.setMinWidth(100);
                typeHBox.getChildren().addAll(typePaddingHleft, typeLabel, typeBox, typePaddingHright);
                HBox.setHgrow(typeBox, Priority.ALWAYS);
                paramGrandparentVBox.getChildren().addAll(typePaddingV, typeHBox);
                typeBox.getSelectionModel().select(selectedLine.getType());
                typeBox.setOnAction(e -> {
                    selectedLine.setType(typeBox.getSelectionModel().getSelectedItem());
                });

                // Everything Else for Lines
                for (String parameter : selectedLine.getParams()) {
                    HBox paramParentHBox = new HBox();
                    Label paramTitle = new Label(parameter);
                    TextField paramInput = new TextField();
                    paramInput.setText(selectedLine.getParamMap().get(parameter));
                    paramInput.textProperty().addListener((observableText, oldValueText, newValueText) -> {
                        selectedLine.setParam(parameter, newValueText);
                        if (parameter.equals("Name")) {
                            titleLabel.setText(newValueText);
                            selectedItem.nameProperty().set(newValueText);
                        }
                    });
                    paramTitle.setMinWidth(200);
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
