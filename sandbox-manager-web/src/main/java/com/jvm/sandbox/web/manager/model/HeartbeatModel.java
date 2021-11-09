package com.jvm.sandbox.web.manager.model;

import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8 - 17:02
 */
public class HeartbeatModel {

    /**
     * mode : ATTACH
     * environment : test
     * moduleList : [{"id":"online-manager-module","isLoaded":true,"isActivated":true,"version":"0.0.2","author":"luanjia@taobao.com"},{"id":"sandbox-info","isLoaded":true,"isActivated":true,"version":"0.0.4","author":"luanjia@taobao.com"},{"id":"sandbox-module-mgr","isLoaded":true,"isActivated":true,"version":"0.0.2","author":"luanjia@taobao.com"},{"id":"sandbox-control","isLoaded":true,"isActivated":true,"version":"0.0.3","author":"luanjia@taobao.com"}]
     * port : 39651
     * appName : z-demo
     * ip : 172.19.189.160
     * isEnableUnsafe : true
     * namespace : default
     * pid : 27697
     * version : 1.3.3
     * status : ACTIVE
     */
    private String appName;
    private String ip;
    private String port;
    private String pid;
    private String mode;
    private String environment;
    private List<ModuleListDTO> moduleList;
    private String isEnableUnsafe;
    private String namespace;
    private String version;
    private String status;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public List<ModuleListDTO> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<ModuleListDTO> moduleList) {
        this.moduleList = moduleList;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsEnableUnsafe() {
        return isEnableUnsafe;
    }

    public void setIsEnableUnsafe(String isEnableUnsafe) {
        this.isEnableUnsafe = isEnableUnsafe;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ModuleListDTO {
        /**
         * id : online-manager-module
         * isLoaded : true
         * isActivated : true
         * version : 0.0.2
         * author : luanjia@taobao.com
         */

        private String id;
        private Boolean isLoaded;
        private Boolean isActivated;
        private String version;
        private String author;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getIsLoaded() {
            return isLoaded;
        }

        public void setIsLoaded(Boolean isLoaded) {
            this.isLoaded = isLoaded;
        }

        public Boolean getIsActivated() {
            return isActivated;
        }

        public void setIsActivated(Boolean isActivated) {
            this.isActivated = isActivated;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
