package io.maerlyn.newsreader;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to differentiate between different categories of articles
 *
 * @author Maerlyn Broadbent
 */
public enum Category {
    cat1(1),
    cat2(2),
    cat3(3),
    cat4(4);

    private static Map map = new HashMap<>();

    // static initialiser to create a lookup map
    static {
        for (Category attrType : Category.values()) {
            map.put(attrType.value, attrType);
        }
    }

    private int value;

    /**
     * assigns values to the types
     *
     * @param value to assign
     */
    Category(int value) {
        this.value = value;
    }

    /**
     * Returns the AttrType for a given value
     *
     * @param value of the type to lookup
     * @return AttrType
     */
    public static Category valueOf(int value) {
        return (Category) map.get(value);
    }

    /**
     * returns the value of an AttrType
     *
     * @return value of an AttrType
     */
    public int getValue() {
        return value;
    }

}