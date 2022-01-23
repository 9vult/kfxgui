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

import com.google.gson.Gson;

/**
 * PluginLoader.java
 * Author: 9volt
 * Created: 2022/01/22
 */
public class PluginLoader {

    private Gson gson;
    private Map<String, Plugin> plugins;

    /**
     * The PluginLoader is responsible for loading and managing plugins.
     * @param pluginDirectory Directory plugins are located in
     */
    public PluginLoader(String pluginDirectory) {
        gson = new Gson();
        plugins = new HashMap<>();
        try {
            List<Path> pluginPathList = Files.walk(Paths.get(pluginDirectory)).filter(Files::isRegularFile).collect(Collectors.toList());

            for (Path pluginPath : pluginPathList) {
                if (!pluginPath.toString().endsWith("json")) continue;
                Plugin p = gson.fromJson(Files.newBufferedReader(pluginPath), Plugin.class);
                plugins.put(p.getName(), p);
            }

        } catch (IOException ioe) {
            System.err.println(String.format("PluginLoader: %s", ioe.getMessage()));
        }
    }

    /**
     * Get the map of currently loaded plugins
     * @return Map of plugins
     */
    public Map<String, Plugin> getLoadedPlugins() {
        return plugins;
    }

    public ArrayList<String> getLoadedPluginNames() {
        ArrayList<String> names = new ArrayList<>(plugins.keySet());
        Collections.sort(names);
        return names;
    }
    
}
