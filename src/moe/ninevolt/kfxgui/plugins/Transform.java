package moe.ninevolt.kfxgui.plugins;

import java.util.ArrayList;

/**
 * Transform.java
 * Author: 9volt
 * Created: 2022/01/23
 */
public class Transform extends Plugin {

    private ArrayList<Plugin> children;
    private static final String START_TIME = "StartTime";
    private static final String END_TIME = "EndTime";
    private static final String VELOCITY = "Velocity";
    public static final String NAME = "(Transform)";

    /**
     * A Transform super-event is a special event
     * that hosts child events (plugins) within it.
     * 
     */
    public Transform() {
        super(  NAME, 
                "9volt", 
                "Non-linear transformation super-event", 
                false, 
                new String[] {START_TIME, END_TIME, VELOCITY},
                ""
        );
        this.children = new ArrayList<>();
    }

    public Transform(Plugin p) {
        this();
    }

    /** Not used */
    private Transform(String name, String author, String description, boolean transform, String[] params, String format) {
        super(name, author, description, transform, params, format);
        this.children = new ArrayList<>();
    }

    /**
     * Get the child events contained within the Transform super-event
     * @return List of child events
     */
    public ArrayList<Plugin> getChildren() {
        return children;
    }

    @Override
    public String getFormattedResult() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("?(%s, %s, %s,",
                                    getParamMap().get(START_TIME),
                                    getParamMap().get(END_TIME),
                                    getParamMap().get(VELOCITY)));
        for (Plugin child : children) {
            result.append(child.getFormattedResult());
        }
        result.append(")");
        return result.toString();
    }
    
}
