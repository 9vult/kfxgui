package moe.ninevolt.kfxgui.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import moe.ninevolt.kfxgui.KfxGui;
import moe.ninevolt.kfxgui.template.Line;
import moe.ninevolt.kfxgui.template.LineType;
import moe.ninevolt.kfxgui.template.TemplateItem;

public class ProjectExporter {

    public static void writeJson(List<TemplateItem> lines, File saveFile) {
        try {
            Gson gson = new Gson();
            ExportWrap wrapper = new ExportWrap(lines);
            try (BufferedWriter bw = Files.newBufferedWriter(saveFile.toPath(), StandardCharsets.UTF_8)) {
                bw.write(gson.toJson(wrapper));
            }
        } catch (JsonIOException | IOException e) {
            e.printStackTrace();
        }
    }

    public static ExportWrap loadJson(File openFile) {
        ExportWrap wrapper;
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(openFile.toPath());
            wrapper = gson.fromJson(reader, ExportWrap.class);
            reader.close();
            return wrapper;
        } catch (JsonIOException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<TemplateItem> generateProjectTree(ExportWrap wrapper) {
        List<TemplateItem> result = new ArrayList<>();
        for (ExportItem export : wrapper.getLines()) {
            TemplateItem line = new Line(LineType.make(export.getParamMap().get(Line.EFFECT)), export.getName());
            line.setParamMap(export.getParamMap());
            for (ExportItem cExport : export.getChildren()) {
                line.getChildren().add(KfxGui.getPluginLoader().create(line, cExport));
            }
            result.add(line);
        }
        return result;
    }
    
}
