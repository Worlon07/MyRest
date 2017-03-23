package com.company.my.Rest;

public class MyRequestMappingInfo {
    private String[] paths;

    private MyRequestMethod[] methods;

    private final String[] params;

    private final String mappingName;

    public MyRequestMappingInfo(MyRequestMapping mapping) {
        mappingName = mapping.name();
        paths = mapping.path();
        methods = mapping.method();
        params = mapping.params();
    }

    public String[] getPaths() {
        return paths;
    }

    private MyRequestMethod[] getMethods() {
        return methods;
    }

    public String[] getParams() {
        return params;
    }

    public String getMappingName() {
        return mappingName;
    }

    public MyRequestMappingInfo combine(final MyRequestMappingInfo info) {
        if (paths.length == 0) {
            paths = info.getPaths();
        } else {
            if (info.getPaths().length > 0) {
                final String[] otherPaths = info.getPaths();
                final String[] newPaths = new String[otherPaths.length * paths.length];
                for (int i = 0; i < otherPaths.length; i++) {
                    for (int j = 0; j < paths.length; j++) {
                        newPaths[i * newPaths.length + j] = paths[j] + otherPaths[i];
                    }
                }
                paths = newPaths;
            }
        }
        if (methods.length == 0) {
            methods = info.getMethods();
        }

        return this;
    }
}
