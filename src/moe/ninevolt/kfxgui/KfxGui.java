package moe.ninevolt.kfxgui;

import javafx.application.Application;
import moe.ninevolt.kfxgui.gui.MainWindow;
import moe.ninevolt.kfxgui.plugins.PluginLoader;

/**
 * KfxGui.java
 * Author: 9volt
 * Created: 2022/01/22
 */
public class KfxGui {

    private static PluginLoader pl;

    private KfxGui(String[] args) {
        pl = new PluginLoader("./plugins");

        Application.launch(MainWindow.class, args);
    }

    public static void main(String[] args) {
        new KfxGui(args);
    }

    public static PluginLoader getPluginLoader() {
        return pl;
    }

}
