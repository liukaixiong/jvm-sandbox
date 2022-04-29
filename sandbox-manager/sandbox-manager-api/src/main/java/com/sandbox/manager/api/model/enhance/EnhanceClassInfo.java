package com.sandbox.manager.api.model.enhance;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.google.common.collect.Lists;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.List;

/**
 * 来自jvm-sandbox-repeater类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 14:25
 */
public class EnhanceClassInfo {

    /**
     * //     * 增强类表达式，支持通配符
     * //     *
     * //     * @see com.alibaba.jvm.sandbox.api.util.GaStringUtils#matching
     * //
     */
    private final String classPattern;

    private final String[] classAnnotation;

    private final String[] classInterfaceTypes;

    /**
     * 增强方法表达式，，支持通配符
     */
    private final MethodPattern[] methodPatterns;

    /**
     * 观察的事件
     * <p>
     * 一般情况需要关注 BEFORE/RETURN/THROW 事件，构成一个方法的around，完成方法入参/返回值/异常的录制
     * <p>
     * 如果基于回调的情况，例如onRequest/onResponse，则只需关注BEFORE事件，通过两个BEFORE去组装，但注意这种情况需要重写{@link
     */
    private final Event.Type[] watchTypes;

    /**
     * 是否包含子类
     */
    private final boolean includeSubClasses;

    private final boolean includeBootstrap;

    @ConstructorProperties({"classPattern", "classInterfaceTypes", "classAnnotation", "methodPatterns", "watchTypes", "includeSubClasses", "includeBootstrap"})
    EnhanceClassInfo(String classPattern, String[] classInterfaceTypes, String[] classAnnotation, MethodPattern[] methodPatterns, Event.Type[] watchTypes, boolean includeSubClasses, boolean includeBootstrap) {
        this.classPattern = classPattern;
        this.classInterfaceTypes = classInterfaceTypes;
        this.classAnnotation = classAnnotation;
        this.methodPatterns = methodPatterns;
        this.watchTypes = watchTypes;
        this.includeSubClasses = includeSubClasses;
        this.includeBootstrap = includeBootstrap;
    }

    public static EnhanceModelBuilder builder() {
        return new EnhanceModelBuilder();
    }

    /**
     * 行为转换
     *
     * @param behavior 行为模型
     * @return 增强类模型
     */
    public static EnhanceClassInfo convert(Behavior behavior) {
        return EnhanceClassInfo.builder()
                .classPattern(behavior.getClassPattern())
                .methodPatterns(MethodPattern.transform(behavior.getMethodPatterns()))
                .includeSubClasses(behavior.isIncludeSubClasses())
                .includeBootstrap(behavior.isIncludeBootstrap())
                .watchTypes(Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS)
                .build();
    }

    public String getClassPattern() {
        return this.classPattern;
    }

    public MethodPattern[] getMethodPatterns() {
        return this.methodPatterns;
    }

    public Event.Type[] getWatchTypes() {
        return this.watchTypes;
    }

    public String[] getClassAnnotation() {
        return classAnnotation;
    }

    public String[] getClassInterfaceTypes() {
        return classInterfaceTypes;
    }

    public boolean isIncludeSubClasses() {
        return this.includeSubClasses;
    }

    public boolean isIncludeBootstrap() {
        return includeBootstrap;
    }

    public static class EnhanceModelBuilder {
        private String classPattern;
        private String[] classAnnotation;
        private String[] classInterfaceTypes;
        private MethodPattern[] methodPatterns;
        private Event.Type[] watchTypes;
        private boolean includeSubClasses;
        private boolean includeBootstrap;

        EnhanceModelBuilder() {
        }

        public EnhanceModelBuilder classPattern(String classPattern) {
            this.classPattern = classPattern;
            return this;
        }

        public EnhanceModelBuilder hasClassAnnotation(String[] classAnnotation) {
            this.classAnnotation = classAnnotation;
            return this;
        }

        public EnhanceModelBuilder hasClassInterfaceTypes(String[] classInterfaceTypes) {
            this.classInterfaceTypes = classInterfaceTypes;
            return this;
        }

        public EnhanceModelBuilder methodPatterns(MethodPattern[] methodPatterns) {
            this.methodPatterns = methodPatterns;
            return this;
        }

        public EnhanceModelBuilder watchTypes(Event.Type... watchTypes) {
            this.watchTypes = watchTypes;
            return this;
        }

        public EnhanceModelBuilder includeSubClasses(boolean includeSubClasses) {
            this.includeSubClasses = includeSubClasses;
            return this;
        }

        public EnhanceModelBuilder includeBootstrap(boolean includeBootstrap) {
            this.includeBootstrap = includeBootstrap;
            return this;
        }

        public EnhanceClassInfo build() {
            return new EnhanceClassInfo(this.classPattern, this.classInterfaceTypes, this.classAnnotation, this.methodPatterns, this.watchTypes, this.includeSubClasses, this.includeBootstrap);
        }

        @Override
        public String toString() {
            return "EnhanceModel.EnhanceModelBuilder(classPattern=" + this.classPattern + ", methodPatterns=" + Arrays.deepToString(this.methodPatterns) + ", watchTypes=" + Arrays.deepToString(this.watchTypes) + ", includeSubClasses=" + this.includeSubClasses + ")";
        }
    }

    public static class MethodPattern {

        String methodName;
        String[] parameterType;
        String[] annotationTypes;

        @ConstructorProperties({"methodName", "parameterType", "annotationTypes"})
        MethodPattern(String methodName, String[] parameterType, String[] annotationTypes) {
            this.methodName = methodName;
            this.parameterType = parameterType;
            this.annotationTypes = annotationTypes;
        }

        public static MethodPattern[] transform(String... methodNames) {
            if (methodNames == null || methodNames.length == 0) {
                return null;
            }
            List<MethodPattern> methodPatterns = Lists.newArrayList();
            for (String methodName : methodNames) {
                methodPatterns.add(MethodPattern.builder().methodName(methodName).build());
            }
            return methodPatterns.toArray(new MethodPattern[0]);
        }

        public static MethodPatternBuilder builder() {
            return new MethodPatternBuilder();
        }

        public String getMethodName() {
            return this.methodName;
        }

        public String[] getParameterType() {
            return this.parameterType;
        }

        public String[] getAnnotationTypes() {
            return this.annotationTypes;
        }

        public static class MethodPatternBuilder {
            private String methodName;
            private String[] parameterType;
            private String[] annotationTypes;

            MethodPatternBuilder() {
            }

            public MethodPatternBuilder methodName(String methodName) {
                this.methodName = methodName;
                return this;
            }

            public MethodPatternBuilder parameterType(String[] parameterType) {
                this.parameterType = parameterType;
                return this;
            }

            public MethodPatternBuilder annotationTypes(String[] annotationTypes) {
                this.annotationTypes = annotationTypes;
                return this;
            }

            public MethodPattern build() {
                return new MethodPattern(this.methodName, this.parameterType, this.annotationTypes);
            }

            @Override
            public String toString() {
                return "EnhanceModel.MethodPattern.MethodPatternBuilder(methodName=" + this.methodName + ", parameterType=" + Arrays.deepToString(this.parameterType) + ", annotationTypes=" + Arrays.deepToString(this.annotationTypes) + ")";
            }
        }
    }

}
