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

public class Toolbox extends VBox {
    
    Label heading;
    ListView<TemplateItem> pluginList;
    TreeView<TemplateItem> tree;

    public Toolbox(TreeView<TemplateItem> tree) {
        this.tree = tree;
        heading = new Label("Toolbox");
        pluginList = new ListView<>();
        setup();
        VBox.setVgrow(pluginList, Priority.ALWAYS);

        this.getChildren().addAll(heading, pluginList);
    }

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
        });
    }

}
