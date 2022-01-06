package com.alibaba.jvm.sandbox.module.manager.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/27 - 18:02
 */
public class MyClassLoader extends URLClassLoader {


    public MyClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public MyClassLoader(URL[] urls) {
        super(urls);
    }

    public MyClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

}
