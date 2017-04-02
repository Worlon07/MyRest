package com.company.my.rest;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Class for working with conditions in a path
 */
public class PathCondition {
    private final String name;
    private final boolean isCondition;

    private PathCondition(final String name, final boolean isCondition) {
        this.name = isCondition ? name.substring(1, name.length() - 1) : name;
        this.isCondition = isCondition;
    }

    public String getName() {
        return name;
    }

    public boolean isCondition() {
        return isCondition;
    }

    /**
     * Convert path into PathCondition array
     *
     * @param path web path
     * @return array of conditions
     */
    public static PathCondition[] getPathConditions(final String path) {
        final String[] splitPath = path.split("/");
        return Arrays.stream(splitPath)
                .map(s -> new PathCondition(s, s.startsWith("{") && s.endsWith("}")))
                .toArray(size -> new PathCondition[size]);
    }

    public static boolean pathEquals(String[] splitPath, PathCondition[] conditions) {
        if (splitPath.length == conditions.length) {
            return IntStream.range(0, conditions.length)
                    .allMatch(i -> conditions[i].isCondition() || conditions[i].getName().equals(splitPath[i]));
        }
        return false;
    }
}
