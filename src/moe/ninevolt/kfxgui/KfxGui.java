package moe.ninevolt.kfxgui;

import moe.ninevolt.kfxgui.gui.MainWindow;

/**
 * KfxGui.java
 * Author: 9volt
 * Created: 2022/01/22
 */
public class KfxGui {

    private MainWindow mainWindow;
    
    private KfxGui(String[] args) {
        this.mainWindow = new MainWindow(args);
    }

    public static void main(String[] args) {
        new KfxGui(args);
    }

}
