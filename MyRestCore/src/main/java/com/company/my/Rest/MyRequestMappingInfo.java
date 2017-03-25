package com.company.my.rest;

public class MyRequestMappingInfo {
    private String[] paths;

    private MyRequestMethod[] methods;

    private final String[] params;

    private final String mappingName;

    public MyRequestMappingInfo(final MyRequestMapping mapping) {
        mappingName = mapping.name();
        paths = mapping.path();
        methods = mapping.method();
        params = mapping.params();
    }

    private MyRequestMappingInfo(final MyRequestMappingInfo mapping) {
        mappingName = mapping.mappingName;
        paths = mapping.paths;
        methods = mapping.methods;
        params = mapping.params;
    }

    public String[] getPaths() {
        return paths;
    }

    public MyRequestMethod[] getMethods() {
        return methods;
    }

    public String[] getParams() {
        return params;
    }

    public String getMappingName() {
        return mappingName;
    }

    public MyRequestMappingInfo combine(final MyRequestMappingInfo info) {
        final MyRequestMappingInfo result = new MyRequestMappingInfo(this);
        if (result.paths.length == 0) {
            result.paths = info.getPaths();
        } else {
            if (info.getPaths().length > 0) {
                final String[] otherPaths = info.getPaths();
                final String[] newPaths = new String[otherPaths.length * result.paths.length];
                for (int i = 0; i < otherPaths.length; i++) {
                    for (int j = 0; j < result.paths.length; j++) {
                        newPaths[i * otherPaths.length + j] = result.paths[j] + otherPaths[i];
                    }
                }
                result.paths = newPaths;
            }
        }
        if (result.methods.length == 0) {
            result.methods = info.getMethods();
        }

        return result;
    }
}
