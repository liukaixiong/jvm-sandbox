package com.lkx.jvm.sandbox.core.model.command;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 15:53
 */
public class WatchCommandInfoModel extends CommandInfoModel {


    private String at;
    private String watch;

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }
}
