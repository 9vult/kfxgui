package moe.ninevolt.kfxgui.gui.windows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import moe.ninevolt.kfxgui.KfxGui;
import moe.ninevolt.kfxgui.exporter.ProjectExporter;
import moe.ninevolt.kfxgui.gui.components.ParamArea;
import moe.ninevolt.kfxgui.gui.components.ProjectTree;
import moe.ninevolt.kfxgui.gui.components.Toolbox;
import moe.ninevolt.kfxgui.plugins.Plugin;
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
    // MenuItem saveAsMI;
    MenuItem openMI;
    MenuItem exportsMI;
    MenuItem exportfMI;
    
    Menu projectMenu;
    Menu targetMI;
    ToggleGroup targetTG;
    List<RadioMenuItem> targetMenuItems;

    Menu viewMenu;
    MenuItem swatchMI;

    ParamArea currentDisplay;
    ProjectTree projectTree;
    Toolbox toolbox;

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
        // saveAsMI = new MenuItem("Save As...");
        exportsMI = new MenuItem("Export Text");
        exportfMI = new MenuItem("Export File...");
        projectMenu = new Menu("Project");
        targetMI = new Menu("Set Target Templater...");
        targetTG = new ToggleGroup();
        viewMenu = new Menu("View");
        swatchMI = new MenuItem("Swatches");
        targetMenuItems = new ArrayList<>();

        projectTree = new ProjectTree();
        toolbox = new Toolbox(projectTree.getTree());

        // Menu strip
        
        fileMenu.getItems().addAll(atarashiiMI, openMI, saveMI, new SeparatorMenuItem(), exportsMI, exportfMI);
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

        viewMenu.getItems().add(swatchMI);

        menuBar.getMenus().addAll(fileMenu, viewMenu, projectMenu);
        bp.setTop(menuBar);

        // MenuStrip
        exportsMI.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (TreeItem<TemplateItem> treeItem : projectTree.getTree().getRoot().getChildren()) {
                TemplateItem templateItem = treeItem.getValue();
                sb.append(Plugin.normalizeOutput(templateItem.getFormattedResult(), false));
                sb.append("\n");
            }
            ExportTextWindow etw = new ExportTextWindow(sb.toString());
            etw.initModality(Modality.APPLICATION_MODAL);
            etw.show();
        });

        swatchMI.setOnAction(e -> {
            SwatchWindow sw = new SwatchWindow();
            sw.show();
        });

        saveMI.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save");
            fc.setInitialFileName(KfxGui.getCurrentProject().getName());
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("KFX-GUI Project (*.kfxgui)", "*.kfxgui"));
            File saveFile = fc.showSaveDialog(window);
            if (saveFile != null) {
                // Get lines
                List<TemplateItem> lines = new ArrayList<>();
                for (TreeItem<TemplateItem> treeItem : projectTree.getTree().getRoot().getChildren()) {
                    lines.add(treeItem.getValue());
                }
                ProjectExporter.writeJson(lines, saveFile);
            }
        });

        // Toolbox
        
        bp.setLeft(toolbox);

        // Project Tree
        
        projectTree.getTree().getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
            TemplateItem selectedItem = newValue.getValue();
            currentDisplay = new ParamArea(selectedItem);
            bp.setCenter(currentDisplay);
        });
        
        bp.setRight(projectTree);

        // Window Finalization

        window.setOnCloseRequest(e -> {
            Platform.exit();
        });
        
        projectTree.getTree().getSelectionModel().select(0);
        window.setTitle(KfxGui.APPLICATION_DESC + " (" + KfxGui.APPLICATION_VERS + ")");
        Scene rootScene = new Scene(bp, 1100, 605);
        window.setScene(rootScene);
        window.show();
    }
}
