package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import moe.ninevolt.kfxgui.gui.TemplateTreeItem;
import moe.ninevolt.kfxgui.plugins.Transform;
import moe.ninevolt.kfxgui.template.Line;
import moe.ninevolt.kfxgui.template.LineType;
import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * Right-hand panel containing the project's
 * tree structure
 * 
 * @author 9volt
 * @since 2022/01/29
 */
public class ProjectTree extends VBox {
    
    HBox top;
    Label treeLabel;
    Label addLabel;
    TreeView<TemplateItem> tree;

    ContextMenu contextMenu;
    MenuItem deleteMI;
    MenuItem cutMI;
    MenuItem pasteMI;

    TreeItem<TemplateItem> clipboard;

    /**
     * Initialize the ProjectTree panel
     */
    public ProjectTree() {
        this.top = new HBox();
        this.treeLabel = new Label("Project Tree");
        this.addLabel = new Label("+ ");
        this.tree = new TreeView<>();

        this.contextMenu = new ContextMenu();
        this.deleteMI = new MenuItem("Delete");
        this.cutMI = new MenuItem("Cut");
        this.pasteMI = new MenuItem("Paste");
        contextMenu.getItems().addAll(deleteMI, cutMI);

        setup();
        VBox.setVgrow(tree, Priority.ALWAYS);

        this.getChildren().addAll(top, tree);
    }

    /**
     * GUI setup helper method
     */
    private void setup() {
        treeLabel.setStyle("-fx-font-size: 16;");
        addLabel.setStyle("-fx-font-size: 16; -fx-text-fill: blue;");
        addLabel.setTooltip(new Tooltip("Add Line..."));

        Region spacerRegion = new Region();
        HBox.setHgrow(spacerRegion, Priority.ALWAYS);
        top.getChildren().addAll(treeLabel, spacerRegion, addLabel);

        tree.setShowRoot(false);
        TreeItem<TemplateItem> treeRoot = new TreeItem<>();
        tree.setRoot(treeRoot);

        TemplateItem newLine = new Line(LineType.make("template syl"), "New Line");
        TreeItem<TemplateItem> defaultRootLineItem = TemplateTreeItem.baseItem(newLine);
        treeRoot.getChildren().add(defaultRootLineItem);

        addLabel.setOnMouseClicked((e) -> {
            if (e.getClickCount() != 1) return;
            TemplateItem line = new Line(LineType.make("template syl"), "New Line");
            TreeItem<TemplateItem> lineItem = TemplateTreeItem.baseItem(line);
            treeRoot.getChildren().add(lineItem);
        });

        // Context Menu actions
        deleteMI.setOnAction((e) -> {
            TreeItem<TemplateItem> selectedTreeItem = tree.getSelectionModel().getSelectedItem();
            TemplateItem selectedTemplateItem = selectedTreeItem.getValue();
            if (treeRoot.getChildren().size() > 1) {
                if (!(selectedTemplateItem instanceof Line))
                    selectedTemplateItem.getParent().getChildren().remove(selectedTemplateItem);
                selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
                try { 
                    tree.getSelectionModel().select(treeRoot.getChildren().get(0)); 
                } catch (Exception ex) {}
            }
        });

        cutMI.setOnAction((e) -> {
            TreeItem<TemplateItem> selectedTreeItem = tree.getSelectionModel().getSelectedItem();
            TemplateItem selectedTemplateItem = selectedTreeItem.getValue();
            this.clipboard = selectedTreeItem;
            if (!(selectedTemplateItem instanceof Line))
                selectedTemplateItem.getParent().getChildren().remove(selectedTemplateItem);
            if (!(selectedTemplateItem instanceof Line) || treeRoot.getChildren().size() > 1) {
                selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
                this.contextMenu.getItems().add(pasteMI);
            }
        });

        pasteMI.setOnAction((e) -> {
            TreeItem<TemplateItem> selectedTreeItem = tree.getSelectionModel().getSelectedItem();
            TemplateItem selectedTemplateItem = selectedTreeItem.getValue();
            if (clipboard.getValue() instanceof Line) {
                treeRoot.getChildren().add(clipboard);
            } else
            if (clipboard.getValue() instanceof Transform && selectedTemplateItem instanceof Transform) {
                selectedTemplateItem.getParent().getChildren().add(clipboard.getValue());
                selectedTreeItem.getParent().getChildren().add(clipboard);
                clipboard.getValue().setParent(selectedTemplateItem.getParent());
            } else 
            if (clipboard.getValue() instanceof Transform && selectedTemplateItem instanceof Line) {
                selectedTemplateItem.getChildren().add(clipboard.getValue());
                selectedTreeItem.getChildren().add(clipboard);
                clipboard.getValue().setParent(selectedTemplateItem);
            } else
            if (clipboard.getValue() instanceof Transform) {
                if (selectedTemplateItem.getParent() instanceof Line) {
                    selectedTemplateItem.getParent().getChildren().add(clipboard.getValue());
                    selectedTreeItem.getParent().getChildren().add(clipboard);
                    clipboard.getValue().setParent(selectedTemplateItem.getParent());
                } else
                if (selectedTemplateItem.getParent() instanceof Transform) {
                    selectedTemplateItem.getParent().getParent().getChildren().add(clipboard.getValue());
                    selectedTreeItem.getParent().getParent().getChildren().add(clipboard);
                    clipboard.getValue().setParent(selectedTemplateItem.getParent().getParent());
                } else {
                    selectedTemplateItem.getChildren().add(clipboard.getValue());
                    selectedTreeItem.getChildren().add(clipboard);
                    clipboard.getValue().setParent(selectedTemplateItem);
                }
            } else {
                if (selectedTemplateItem instanceof Line) {
                    selectedTemplateItem.getChildren().add(clipboard.getValue());
                    selectedTreeItem.getChildren().add(clipboard);
                    clipboard.getValue().setParent(selectedTemplateItem);
                } else
                if (selectedTemplateItem.getParent() instanceof Transform) {
                    if (clipboard.getValue().transformProperty().get()) {
                        selectedTemplateItem.getParent().getChildren().add(clipboard.getValue());
                        selectedTreeItem.getParent().getChildren().add(clipboard);
                        clipboard.getValue().setParent(selectedTemplateItem.getParent());
                    } else {
                        selectedTemplateItem.getParent().getParent().getChildren().add(clipboard.getValue());
                        selectedTreeItem.getParent().getParent().getChildren().add(clipboard);
                        clipboard.getValue().setParent(selectedTemplateItem.getParent().getParent());
                    }
                } else
                if (selectedTemplateItem instanceof Transform) {
                    if (clipboard.getValue().transformProperty().get()) {
                        selectedTemplateItem.getChildren().add(clipboard.getValue());
                        selectedTreeItem.getChildren().add(clipboard);
                        clipboard.getValue().setParent(selectedTemplateItem);
                    } else {
                        selectedTemplateItem.getParent().getChildren().add(clipboard.getValue());
                        selectedTreeItem.getParent().getChildren().add(clipboard);
                        clipboard.getValue().setParent(selectedTemplateItem.getParent());
                    }
                } else {
                    selectedTemplateItem.getParent().getChildren().add(clipboard.getValue());
                    selectedTreeItem.getParent().getChildren().add(clipboard);
                    clipboard.getValue().setParent(selectedTemplateItem.getParent());
                }
            }
            this.clipboard = null;
            this.contextMenu.getItems().remove(pasteMI);
        });

        tree.setContextMenu(contextMenu);
    }

    /**
     * @return Reference to the project tree
     */
    public TreeView<TemplateItem> getTree() {
        return this.tree;
    }

}
