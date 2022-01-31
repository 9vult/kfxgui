package moe.ninevolt.kfxgui.template;

/**
 * Holds information and settings about the current project
 * 
 * @author 9volt
 * @since 2022/01/28
 */
public class Project {

    private String targetTemplater;
    private String name;

    public Project(String projectName, String targetTemplater) {
        this.name = projectName;
        this.targetTemplater = targetTemplater;
    }

    public String getTargetTemplater() {
        return targetTemplater;
    }

    public String getName() {
        return name;
    }

    public void setTargetTemplater(String templaterName) {
        this.targetTemplater = templaterName;
    }

    public void setName(String name) {
        this.name = name;
    }

}
