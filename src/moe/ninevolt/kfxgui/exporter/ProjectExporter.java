package moe.ninevolt.kfxgui.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import moe.ninevolt.kfxgui.template.TemplateItem;

public class ProjectExporter {

    public static void writeJson(List<TemplateItem> lines, File saveFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ExportWrap wrapper = new ExportWrap(lines);
            try (BufferedWriter bw = Files.newBufferedWriter(saveFile.toPath(), StandardCharsets.UTF_8)) {
                bw.write(gson.toJson(wrapper));
            }
        } catch (JsonIOException | IOException e) {
            e.printStackTrace();
        }
    }
    
}
