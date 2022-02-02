package moe.ninevolt.kfxgui;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import javafx.application.Application;
import moe.ninevolt.kfxgui.gui.windows.MainWindow;
import moe.ninevolt.kfxgui.plugins.PluginLoader;
import moe.ninevolt.kfxgui.template.Project;

/**
 * Main class
 * 
 * @author 9volt
 * @since 2022/01/22
 */
public class KfxGui {

    public static String APPLICATION_NAME = "KFX-GUI";
    public static String APPLICATION_DESC = "9volt GUI Karaoke Template Builder";
    public static String APPLICATION_VERS = "0.0.5 Alpha";

    private static PluginLoader pl;
    private static Project project;

    private KfxGui(String[] args) {
        // Check if the plugins directory exists and is populated before starting
        try {
            Path pluginsDirectory = Paths.get("plugins");
            if (Files.exists(pluginsDirectory)) {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(pluginsDirectory)) {
                    if (!directoryStream.iterator().hasNext()) {
                        JOptionPane.showMessageDialog(null, "Failed to load: Plugins directory is empty.");
                        System.exit(-1);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to load: Plugins directory does not exist.");
                System.exit(-1);
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Failed to load: IO Error: " + ioe.getMessage());
            System.exit(-1);
        }
        pl = new PluginLoader("plugins");
        project = new Project("New Project", "Stock");

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
