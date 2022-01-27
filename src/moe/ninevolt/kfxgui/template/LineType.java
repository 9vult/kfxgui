package moe.ninevolt.kfxgui.template;

/**
 * LineType.java
 * Author: 9volt
 * Created: 2022/01/25
 */
public enum LineType {
    TEMPLATE_LINE   { public String toString() { return "template line";    } },
    TEMPLATE_SYL    { public String toString() { return "template syl";     } },
    TEMPLATE_CHAR   { public String toString() { return "template char";    } },
    CODE_ONCE       { public String toString() { return "code once";        } },
    CODE_LINE       { public String toString() { return "code line";        } },
    CODE_SYL        { public String toString() { return "code syl";         } },
    CODE_CHAR       { public String toString() { return "code char";        } },
    OTHER           { public String toString() { return "";                 } }
}
