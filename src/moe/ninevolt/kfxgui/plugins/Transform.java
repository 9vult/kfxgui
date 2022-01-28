package moe.ninevolt.kfxgui.plugins;

import java.util.List;

import moe.ninevolt.kfxgui.template.TemplateItem;

/**
 * Transform is a special TemplateItem
 * that supports the \t transform tag
 * in the template.
 * <p>It, along with Line, are the only
 * TemplateItem implementations
 * that actually use children.
 * 
 * @author 9volt
 * @since 2022/01/23
 */
public class Transform extends TemplateItem {

    private static final String START_TIME = "StartTime";
    private static final String END_TIME = "EndTime";
    private static final String VELOCITY = "Velocity";
    public static final String NAME = "(Transform)";

    public Transform(TemplateItem parent) {
        super(parent, 
            List.of(START_TIME, END_TIME, VELOCITY), 
            Transform.NAME,
            "Non-linear Transformation",
            false);
        setParam(VELOCITY, "1");
    }

    @Override
    public String getFormattedResult() {
        String sTime = getParamMap().get(START_TIME);
        String eTime = getParamMap().get(END_TIME);
        String veloc = getParamMap().get(VELOCITY);
        StringBuilder result = new StringBuilder();
        result.append(String.format("?t(%s, %s, %s,",
                                    sTime,
                                    eTime,
                                    veloc));
        for (TemplateItem child : children) {
            result.append(child.getFormattedResult());
        }
        result.append(")");
        return result.toString();
    }
    
}
