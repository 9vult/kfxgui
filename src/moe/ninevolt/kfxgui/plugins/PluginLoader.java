package moe.ninevolt.kfxgui.plugins;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.electronwill.nightconfig.core.file.FileConfig;

import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * PluginLoader.java
 * Author: 9volt
 * Created: 2022/01/22
 */
public class PluginLoader {

    private Map<String, Plugin> plugins;

    /**
     * The PluginLoader is responsible for loading and managing plugins.
     * @param pluginDirectory Directory plugins are located in
     */
    public PluginLoader(String pluginDirectory) {
        plugins = new HashMap<>();
        try {
            List<Path> pluginPathList = Files.walk(Paths.get(pluginDirectory)).filter(Files::isRegularFile).collect(Collectors.toList());

            for (Path pluginPath : pluginPathList) {
                if (!pluginPath.toString().endsWith("toml")) continue;

                FileConfig fc = FileConfig.of(pluginPath);
                fc.load();

                Plugin p = new Plugin(fc.get("name"), 
                                        fc.get("author"), 
                                        fc.get("description"), 
                                        fc.get("transform"), 
                                        fc.get("params"), 
                                        fc.get("format"));
                plugins.put(p.getName(), p);
                fc.close();
            }
            // Load in the Transform "plugin"
            plugins.put(Transform.NAME, new Transform());
            
        } catch (IOException ioe) {
            System.err.println(String.format("PluginLoader: %s", ioe.getMessage()));
        }
    }

    /**
     * Get the list of loaded plugins
     * @return Map of loaded plugins
     */
    public ArrayList<Plugin> getSortedPlugins() {
        ArrayList<Plugin> values = new ArrayList<>(plugins.values());
        Collections.sort(values);
        return values;
    }

    /**
     * Get the list of loaded plugin names
     * @return List of plugin names
     */
    public ArrayList<String> getLoadedNames() {
        ArrayList<String> names = new ArrayList<>(plugins.keySet());
        Collections.sort(names);
        return names;
    }

    /**
     * Create a new instance of an action (plugin).
     * <p>Returns a copy of the "original" plugin instance
     * with the same base parameters.</p>
     * @param parent Parent item of the plugin
     * @param name Name of the plugin to create
     * @return New copy of the specified plugin
     */
    public Plugin create(TemplateItem parent, String name) {
        return new Plugin(parent, plugins.get(name));
    }
    
}
