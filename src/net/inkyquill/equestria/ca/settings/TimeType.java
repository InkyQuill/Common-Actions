package net.inkyquill.equestria.ca.settings;

public enum TimeType {mine("mine"), real("real"), day("day"), night("night"), chaos("chaos"), fixed("fixed");

    private String text;

    TimeType(String text) {
        this.text = text;
    }

    public static TimeType fromString(String text) {
        if (text != null) {
            for (TimeType b : TimeType.values()) {
                if (text.trim().equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    public String getText() {
        return this.text;
    }
}
