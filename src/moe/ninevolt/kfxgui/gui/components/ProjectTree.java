package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import moe.ninevolt.kfxgui.gui.TemplateTreeItem;
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

    /**
     * Initialize the ProjectTree panel
     */
    public ProjectTree() {
        this.top = new HBox();
        this.treeLabel = new Label("Project Tree");
        this.addLabel = new Label("+ ");
        this.tree = new TreeView<>();

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
    }

    /**
     * @return Reference to the project tree
     */
    public TreeView<TemplateItem> getTree() {
        return this.tree;
    }

}
