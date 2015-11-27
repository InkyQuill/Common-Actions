/*
 * Decompiled with CFR 0_102.
 */
package org.equestria.minecraft.common.properties;

public enum FoodProperties {
    FOOD_LVL_0("0"),
    FOOD_LVL_1("1"),
    FOOD_LVL_2("2"),
    FOOD_LVL_3("3"),
    FOOD_LVL_4("4"),
    FOOD_LVL_5("5"),
    FOOD_LVL_6("6"),
    FOOD_LVL_7("7"),
    FOOD_LVL_8("8"),
    FOOD_LVL_9("9"),
    FOOD_LVL_10("10");
    
    private String level;

    private FoodProperties(String level) {
        this.level = level;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

