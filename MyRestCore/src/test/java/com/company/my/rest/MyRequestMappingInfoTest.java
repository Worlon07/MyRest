package com.company.my.rest;

import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;

public class MyRequestMappingInfoTest {

    @Test
    public void fieldsTest() throws Exception {
        final String name = "TestName";
        final String[] value = new String[0];
        final String[] path = new String[1];
        final MyRequestMethod[] method = new MyRequestMethod[2];
        final String[] params = new String[3];
        final MyRequestMapping mapping = new MyRequestMappingImpl(name, value, path, method, params);
        final MyRequestMappingInfo mappingInfo = new MyRequestMappingInfo(mapping);
        assertEquals(name, mappingInfo.getMappingName());
        assertArrayEquals(path, mappingInfo.getPaths());
        assertArrayEquals(params, mappingInfo.getParams());
        assertArrayEquals(method, mappingInfo.getMethods());
    }

    @Test(expected = NullPointerException.class)
    public void nullParameterTest() throws Exception {
        new MyRequestMappingInfo(null);
    }

    @Test
    public void pathCombineTest() throws Exception {
        final String[] path = {"/path1", "/path2"};
        final String[] subPath = {"/subPath1", "/subPath2"};
        final MyRequestMappingInfo mappingPath = new MyRequestMappingInfo(new MyRequestMappingImpl("path", null, path, new MyRequestMethod[0], null));
        final MyRequestMappingInfo mappingSubPath = new MyRequestMappingInfo(new MyRequestMappingImpl("subPath", null, subPath, new MyRequestMethod[0], null));
        final MyRequestMappingInfo mappingEmptyPath = new MyRequestMappingInfo(new MyRequestMappingImpl("emptyPath", null, new String[0], new MyRequestMethod[0], null));

        MyRequestMappingInfo mappingCombined = mappingPath.combine(mappingSubPath);
        assertArrayEquals(new String[]{"/path1/subPath1", "/path2/subPath1", "/path1/subPath2", "/path2/subPath2"}, mappingCombined.getPaths());

        mappingCombined = mappingPath.combine(mappingEmptyPath);
        assertArrayEquals(path, mappingCombined.getPaths());

        mappingCombined = mappingEmptyPath.combine(mappingSubPath);
        assertArrayEquals(subPath, mappingCombined.getPaths());
    }

    @Test
    public void methodCombineTest() throws Exception {
        final MyRequestMethod[] getMethod = {MyRequestMethod.GET};
        final MyRequestMethod[] postMethod = {MyRequestMethod.POST};
        final MyRequestMethod[] allMethods = {MyRequestMethod.GET, MyRequestMethod.POST};
        final MyRequestMethod[] emptyMethod = new MyRequestMethod[0];

        final MyRequestMappingInfo mappingGet = new MyRequestMappingInfo(new MyRequestMappingImpl("get", null, new String[0], getMethod, null));
        final MyRequestMappingInfo mappingPost = new MyRequestMappingInfo(new MyRequestMappingImpl("post", null, new String[0], postMethod, null));
        final MyRequestMappingInfo mappingAll = new MyRequestMappingInfo(new MyRequestMappingImpl("post", null, new String[0], allMethods, null));
        final MyRequestMappingInfo mappingEmpty = new MyRequestMappingInfo(new MyRequestMappingImpl("empty", null, new String[0], emptyMethod, null));

        MyRequestMappingInfo mappingCombined = mappingGet.combine(mappingPost);
        assertArrayEquals(getMethod, mappingCombined.getMethods());

        mappingCombined = mappingGet.combine(mappingEmpty);
        assertArrayEquals(getMethod, mappingCombined.getMethods());

        mappingCombined = mappingEmpty.combine(mappingPost);
        assertArrayEquals(postMethod, mappingCombined.getMethods());

        mappingCombined = mappingAll.combine(mappingGet);
        assertArrayEquals(allMethods, mappingCombined.getMethods());
    }

    class MyRequestMappingImpl implements MyRequestMapping {

        private final String name;
        private final String[] value;
        private final String[] path;
        private final MyRequestMethod[] method;
        private final String[] params;

        public MyRequestMappingImpl(final String name, final String[] value, final String[] path,
                                    final MyRequestMethod[] method, final String[] params) {
            this.name = name;
            this.value = value;
            this.path = path;
            this.method = method;
            this.params = params;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String[] value() {
            return value;
        }

        @Override
        public String[] path() {
            return path;
        }

        @Override
        public MyRequestMethod[] method() {
            return method;
        }

        @Override
        public String[] params() {
            return params;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}