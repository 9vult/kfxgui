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

    private Map<String, Path> plugins;

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
                this.plugins.put(fc.get("name"), pluginPath);
                fc.close();
            }
            this.plugins.put(Transform.NAME, null);
            
        } catch (IOException ioe) {
            System.err.println(String.format("PluginLoader: %s", ioe.getMessage()));
        }
    }

    /**
     * Get the list of loaded plugins
     * @return Map of loaded plugins
     */
    public List<TemplateItem> getSortedPlugins() {
        List<String> names = new ArrayList<>();
        names.addAll(plugins.keySet());
        Collections.sort(names);

        List<TemplateItem> pluginList = new ArrayList<>();
        for (String name : names)
            pluginList.add(this.create(null, name));

        return pluginList;
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
    public TemplateItem create(TemplateItem parent, String name) {
        if (name.equals(Transform.NAME)) return new Transform(parent);

        FileConfig fc = FileConfig.of(plugins.get(name));
        fc.load();

        Plugin p = new Plugin(parent,
                            fc.get("params"),
                            fc.get("name"), 
                            fc.get("author"), 
                            fc.get("description"), 
                            fc.get("transform"), 
                            fc.get("format"));
        fc.close();
        return p;
    }
    
}
