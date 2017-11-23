package io.maerlyn.newsreader;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to differentiate between different categories of articles
 *
 * @author Maerlyn Broadbent
 */
public enum Category {
    ALL(1),
    ABOUT(2),
    DESIGN(3),
    AUSTRALIA(4),
    BUSINESS(5),
    BOOKS(6),
    OPINION(7),
    COMMUNITY(8),
    CULTURE(9),
    EDUCATION(10),
    ENVIRONMENT(11),
    EXTRA(12),
    FILM(13),
    FOOTBALL(14),
    GAMES(15)
    ;

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

    public String toString(){
        return this.name();
    }

}