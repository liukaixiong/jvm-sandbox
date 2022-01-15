package com.alibaba.jvm.sandbox.module.manager.util.model;

import java.util.List;
import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/15 - 11:20
 */
public class ComplexModel {

    private List<UserModel> userModel;

    private Map<String, UserModel> maoUserModel;

    private Map<String, List<UserModel>> stringListMap;

    public List<UserModel> getUserModel() {
        return userModel;
    }

    public void setUserModel(List<UserModel> userModel) {
        this.userModel = userModel;
    }

    public Map<String, UserModel> getMaoUserModel() {
        return maoUserModel;
    }

    public void setMaoUserModel(Map<String, UserModel> maoUserModel) {
        this.maoUserModel = maoUserModel;
    }

    public Map<String, List<UserModel>> getStringListMap() {
        return stringListMap;
    }

    public void setStringListMap(Map<String, List<UserModel>> stringListMap) {
        this.stringListMap = stringListMap;
    }
}
