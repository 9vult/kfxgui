package moe.ninevolt.kfxgui.template;

/**
 * Holds information and settings about the current project
 * 
 * @author 9volt
 * @since 2022/01/28
 */
public class Project {

    private String targetTemplater;

    public Project() {
        this.targetTemplater = "Stock";
    }

    public String getTargetTemplater() {
        return targetTemplater;
    }

    public void setTargetTemplater(String templaterName) {
        this.targetTemplater = templaterName;
    }

}
