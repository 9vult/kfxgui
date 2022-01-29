package moe.ninevolt.kfxgui.gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import moe.ninevolt.kfxgui.KfxGui;
import moe.ninevolt.kfxgui.gui.TemplateTreeItem;
import moe.ninevolt.kfxgui.plugins.Transform;
import moe.ninevolt.kfxgui.template.Line;
import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * Left-hand panel containing the list of
 * loaded events that can be used in the project
 * 
 * @author 9volt
 * @since 2022/01/29
 */
public class Toolbox extends VBox {
    
    Label heading;
    ListView<TemplateItem> pluginList;
    TreeView<TemplateItem> tree;

    /**
     * Initialize the Toolbox
     * @param tree Tree being used
     */
    public Toolbox(TreeView<TemplateItem> tree) {
        this.tree = tree;
        heading = new Label("Toolbox");
        pluginList = new ListView<>();
        setup();
        VBox.setVgrow(pluginList, Priority.ALWAYS);

        this.getChildren().addAll(heading, pluginList);
    }

    /**
     * GUI setup helper method
     */
    private void setup() {
        pluginList.getItems().addAll(KfxGui.getPluginLoader().getSortedPlugins());
        heading.setStyle("-fx-font-size: 16;");
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

        pluginList.setOnMouseClicked((e) -> {
            if (e.getClickCount() != 2) return;
            TemplateItem selectedToolboxItem = pluginList.getSelectionModel().getSelectedItem();                    
            TreeItem<TemplateItem> selectedTreeItem = tree.getSelectionModel().getSelectedItem();
            TemplateItem newTemplateItem = KfxGui.getPluginLoader().create(null, selectedToolboxItem.nameProperty().get());

            TreeItem<TemplateItem> newTreeItem = TemplateTreeItem.baseItem(newTemplateItem);
            TemplateItem selectedTreeTemplateItem = selectedTreeItem.getValue();

            if (selectedToolboxItem instanceof Transform && selectedTreeTemplateItem instanceof Transform) {
                selectedTreeTemplateItem.getParent().getChildren().add(selectedToolboxItem);
                selectedTreeItem.getParent().getChildren().add(newTreeItem);
                newTemplateItem.setParent(selectedTreeTemplateItem.getParent());
            } else 
            if (selectedToolboxItem instanceof Transform && selectedTreeTemplateItem instanceof Line) {
                selectedTreeTemplateItem.getChildren().add(selectedToolboxItem);
                selectedTreeItem.getChildren().add(newTreeItem);
                newTemplateItem.setParent(selectedTreeTemplateItem);
            } else
            if (selectedToolboxItem instanceof Transform) {
                if (selectedTreeTemplateItem.getParent() instanceof Line) {
                    selectedTreeTemplateItem.getParent().getChildren().add(selectedToolboxItem);
                    selectedTreeItem.getParent().getChildren().add(newTreeItem);
                    newTemplateItem.setParent(selectedTreeTemplateItem.getParent());
                } else
                if (selectedTreeTemplateItem.getParent() instanceof Transform) {
                    selectedTreeTemplateItem.getParent().getParent().getChildren().add(selectedToolboxItem);
                    selectedTreeItem.getParent().getParent().getChildren().add(newTreeItem);
                    newTemplateItem.setParent(selectedTreeTemplateItem.getParent().getParent());
                } else {
                    selectedTreeTemplateItem.getChildren().add(selectedToolboxItem);
                    selectedTreeItem.getChildren().add(newTreeItem);
                    newTemplateItem.setParent(selectedTreeTemplateItem);
                }
            } else {
                if (selectedTreeTemplateItem instanceof Line) {
                    selectedTreeTemplateItem.getChildren().add(selectedToolboxItem);
                    selectedTreeItem.getChildren().add(newTreeItem);
                    newTemplateItem.setParent(selectedTreeTemplateItem);
                } else
                if (selectedTreeTemplateItem.getParent() instanceof Transform) {
                    if (selectedToolboxItem.transformProperty().get()) {
                        selectedTreeTemplateItem.getParent().getChildren().add(selectedToolboxItem);
                        selectedTreeItem.getParent().getChildren().add(newTreeItem);
                        newTemplateItem.setParent(selectedTreeTemplateItem.getParent());
                    } else {
                        selectedTreeTemplateItem.getParent().getParent().getChildren().add(selectedToolboxItem);
                        selectedTreeItem.getParent().getParent().getChildren().add(newTreeItem);
                        newTemplateItem.setParent(selectedTreeTemplateItem.getParent().getParent());
                    }
                } else
                if (selectedTreeTemplateItem instanceof Transform) {
                    if (selectedToolboxItem.transformProperty().get()) {
                        selectedTreeTemplateItem.getChildren().add(selectedToolboxItem);
                        selectedTreeItem.getChildren().add(newTreeItem);
                        newTemplateItem.setParent(selectedTreeTemplateItem);
                    } else {
                        selectedTreeTemplateItem.getParent().getChildren().add(selectedToolboxItem);
                        selectedTreeItem.getParent().getChildren().add(newTreeItem);
                        newTemplateItem.setParent(selectedTreeTemplateItem.getParent());
                    }
                } else {
                    selectedTreeTemplateItem.getParent().getChildren().add(selectedToolboxItem);
                    selectedTreeItem.getParent().getChildren().add(newTreeItem);
                    newTemplateItem.setParent(selectedTreeTemplateItem.getParent());
                }
            }
                    
            // Select it (and expand parents)
            for (TreeItem<TemplateItem> ti = newTreeItem; ti.getParent() != null; ti = ti.getParent()) {
                ti.getParent().setExpanded(true);
            }
            tree.getSelectionModel().select(tree.getRow(newTreeItem));
        });
    }

}
