package moe.ninevolt.kfxgui;

import javafx.application.Application;
import moe.ninevolt.kfxgui.gui.MainWindow;
import moe.ninevolt.kfxgui.plugins.PluginLoader;
import moe.ninevolt.kfxgui.template.Project;

/**
 * Main class
 * 
 * @author 9volt
 * @since 2022/01/22
 */
public class KfxGui {

    private static PluginLoader pl;
    private static Project project;

    private KfxGui(String[] args) {
        pl = new PluginLoader("./plugins");
        project = new Project();

        Application.launch(MainWindow.class, args);
    }

    public static void main(String[] args) {
        new KfxGui(args);
    }

    public static PluginLoader getPluginLoader() {
        return pl;
    }

    public static Project getCurrentProject() {
        return project;
    }

}
