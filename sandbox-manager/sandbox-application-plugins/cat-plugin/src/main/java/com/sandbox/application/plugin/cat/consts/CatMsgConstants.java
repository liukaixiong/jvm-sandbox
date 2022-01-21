package com.sandbox.application.plugin.cat.consts;

/**
 * Cat的常量枚举定义
 *
 * @author : liukx
 * @create : 2018/7/2 11:28
 * @email : liukx@elab-plus.com
 */
public class CatMsgConstants {

    public static final String CROSS_CONSUMER = "PigeonCall";

    public static final String LOCAL_THREAD_SERVER = "LocalThreadServer";

    /**
     * Cross报表中的数据标识
     */
    public static final String CROSS_SERVER = "PigeonService";

    public static final String CROSS_MQ = "PigeonMQ";


    public static final String PROVIDER_APPLICATION_NAME = "serverApplicationName";

    public static final String CONSUMER_CALL_SERVER = "PigeonCall.server";

    public static final String CONSUMER_CALL_APP = "PigeonCall.app";

    public static final String CONSUMER_CALL_PORT = "PigeonCall.port";

    public static final String PROVIDER_CALL_SERVER = "PigeonService.client";

    /**
     * 客户端调用标识
     */
    public static final String PROVIDER_CALL_APP = "PigeonService.app";

    public static final String FORK_MESSAGE_ID = "m_forkedMessageId";

    public static final String FORK_ROOT_MESSAGE_ID = "m_rootMessageId";

    public static final String FORK_PARENT_MESSAGE_ID = "m_parentMessageId";

    public static final String INTERFACE_NAME = "interfaceName";

    /**
     * 客户端调用的服务名称 -> 最好是Cat.getManager().getDomain()获取
     */
    public static final String APPLICATION_KEY = "application.name";
    /**
     * 代理中的关于iphone使用标识
     */
    public static final String AGENT_IPHONE = "iPhone";

    /**
     * 代理中关于Android的标识
     */
    public static final String AGENT_ANDROID = "Android";

    /**
     * 第三方URL调用
     */
    public static final String THIRD_PARTY = "third_party_url";


}