package com.lkx.jvm.sandbox.core.compoents;

import org.slf4j.Logger;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/29 - 10:17
 */
public class PrintFormat {

    private String separator = "/n";
    private String lineBreak = ":";
    private final StringBuilder sb = new StringBuilder();

    public PrintFormat() {

    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setLineBreak(String lineBreak) {
        this.lineBreak = lineBreak;
    }

    public PrintFormat put(String title, Object value) {
        sb.append(title).append(lineBreak).append(value).append(separator);
        return this;
    }

    public void printLog() {
        System.out.println(sb.toString());
    }

    public void printLog(Logger logger) {
        logger.info(sb.toString());
    }

}
