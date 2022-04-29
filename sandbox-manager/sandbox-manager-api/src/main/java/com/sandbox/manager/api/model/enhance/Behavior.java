package com.sandbox.manager.api.model.enhance;


/**
 * <p>
 *
 * @author zhaoyb1990
 */
public class Behavior {
    private String classPattern;
    private String[] methodPatterns;
    private boolean includeSubClasses;
    private boolean includeBootstrap;

    public Behavior() {
    }

    public Behavior(String classPattern, String... methodPatterns) {
        this.classPattern = classPattern;
        this.methodPatterns = methodPatterns;
    }

    public Behavior(String classPattern, String[] methodPatterns, boolean includeSubClasses) {
        this.classPattern = classPattern;
        this.methodPatterns = methodPatterns;
        this.includeSubClasses = includeSubClasses;
    }

    public Behavior(String classPattern, String[] methodPatterns, boolean includeSubClasses, boolean includeBootstrap) {
        this.classPattern = classPattern;
        this.methodPatterns = methodPatterns;
        this.includeSubClasses = includeSubClasses;
        this.includeBootstrap = includeBootstrap;
    }

    public String getClassPattern() {
        return classPattern;
    }

    public void setClassPattern(String classPattern) {
        this.classPattern = classPattern;
    }

    public String[] getMethodPatterns() {
        return methodPatterns;
    }

    public void setMethodPatterns(String[] methodPatterns) {
        this.methodPatterns = methodPatterns;
    }

    public boolean isIncludeSubClasses() {
        return includeSubClasses;
    }

    public void setIncludeSubClasses(boolean includeSubClasses) {
        this.includeSubClasses = includeSubClasses;
    }

    public boolean isIncludeBootstrap() {
        return includeBootstrap;
    }

    public void setIncludeBootstrap(boolean includeBootstrap) {
        this.includeBootstrap = includeBootstrap;
    }
}
