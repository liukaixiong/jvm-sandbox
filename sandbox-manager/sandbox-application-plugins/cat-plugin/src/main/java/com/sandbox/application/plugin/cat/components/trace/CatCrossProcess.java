package com.sandbox.application.plugin.cat.components.trace;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author : liukx
 * @create : 2018/6/25 16:27
 * @email : liukx@elab-plus.com
 */
public class CatCrossProcess {

    public static void createConsumerCross(HttpServletRequest request, Transaction transaction) {
        Event crossAppEvent = Cat.newEvent(CatMsgConstants.CONSUMER_CALL_APP, getProviderAppName(request));
        Event crossServerEvent = Cat.newEvent(CatMsgConstants.CONSUMER_CALL_SERVER, request.getRemoteHost());
        Event crossPortEvent = Cat.newEvent(CatMsgConstants.CONSUMER_CALL_PORT, request.getRemotePort() + "");
        crossAppEvent.setStatus(Event.SUCCESS);
        crossServerEvent.setStatus(Event.SUCCESS);
        crossPortEvent.setStatus(Event.SUCCESS);
        transaction.addChild(crossAppEvent);
        transaction.addChild(crossPortEvent);
        transaction.addChild(crossServerEvent);
    }

    /**
     * 创建远程事务消息
     *
     * @param type     消费类型
     * @param name     消费名称
     * @param consumer 消费者
     * @return
     */
    public static Object createRemoteMsg(String type, String name, Function<Map<String, String>, Object> consumer) {
        // 构建一个事务日志
        Transaction transaction = Cat.newTransaction(type, name);
        try {
            // 构建一个上下文事务消息
            Map<String, String> msgContextMap = getMsgContextMap();
            Object apply = consumer.apply(msgContextMap);
            transaction.setSuccessStatus();
            return apply;
        } catch (Exception e) {
            transaction.setStatus(e);
        } finally {
            transaction.complete();
        }
        return null;
    }

    /**
     * 创建远程MQ消息
     *
     * @param type     消息类型
     * @param name     消息名称
     * @param consumer 消费者
     * @return
     */
    public static Object createRemoteMQMsg(String type, String name, Function<Map<String, String>, Object> consumer) {
        // 构建一个事务日志
        Transaction t = Cat.newTransaction(type, name);
        // 该类型消息是不会作为事务消息去统计，并且不会在Transaction报表中展现
        // TaggedTransaction t = Cat.newTaggedTransaction(type, name, "MQ");
        try {
            Map<String, String> msgContextMap = getDomainMsgContextMap("MQ-CONSUMER");
            //t.bind("MQ",msgContextMap.get(Cat.Context.CHILD),"MQ消息请走特殊查询");
            // 构建一个上下文事务消息

            Object apply = consumer.apply(msgContextMap);
            t.setSuccessStatus();
            return apply;
        } catch (Exception e) {
            t.setStatus(e);
        } finally {
            t.complete();
        }
        return null;
    }

    /**
     * 构建消息树串联
     *
     * @param type
     * @param name
     * @param msgIdMap
     * @param process
     * @return
     */
    public static <T> T buildRemoteMsg(String type, String name, Map<String, String> msgIdMap, Supplier<T> process) {
        Transaction transaction = getCrossTransactionMsg(type, name, msgIdMap);
        return getTransaction(transaction, process);
    }

    /**
     * 构建远程传递来的MQ类型的消息
     *
     * @param type     消息类型
     * @param name     消息名称
     * @param msgIdMap 客户端携带过来的上下文编号
     * @param process  具体的执行业务
     * @return
     */
    public static <T> T buildRemoteMQMsg(String type, String name, Map<String, String> msgIdMap, Supplier<T> process) {
        Transaction transaction = getCrossMQTransactionMsg(type, name, msgIdMap);
        return getTransaction(transaction, process);
    }

    private static <T> T getTransaction(Transaction t, Supplier<T> process) {
        try {
            Object result = process.get();
            t.setSuccessStatus();
            return (T) result;
        } catch (Exception e) {
            t.setStatus(e);
            throw e;
        } finally {
            t.complete();
        }
    }

    public static String getProviderAppName(HttpServletRequest request) {
        String appName = request.getParameter(CatMsgConstants.PROVIDER_APPLICATION_NAME);
        if (StringUtils.isEmpty(appName)) {
            String interfaceName = request.getParameter(CatMsgConstants.INTERFACE_NAME);
            appName = interfaceName.substring(0, interfaceName.lastIndexOf('.'));
        }
        return appName;
    }

    /**
     * 串联provider端消息树
     *
     * @param request
     * @param t
     */
    public static void createProviderCross(HttpServletRequest request, Transaction t) {
        Event crossAppEvent = Cat.newEvent(CatMsgConstants.PROVIDER_CALL_APP, request.getHeader(CatMsgConstants.APPLICATION_KEY));
        //clientIp
        Event crossServerEvent = Cat.newEvent(CatMsgConstants.PROVIDER_CALL_SERVER, request.getRemoteAddr());
        crossAppEvent.setStatus(Event.SUCCESS);
        crossServerEvent.setStatus(Event.SUCCESS);
        t.addChild(crossAppEvent);
        t.addChild(crossServerEvent);
    }


    /**
     * 获取消息上下文对象
     *
     * @return
     */
    public static Map<String, String> getMsgContextMap() {
        return getDomainMsgContextMap(Cat.getManager().getDomain());
    }

    /**
     * 构建远程服务类型
     *
     * @param remoteDomain
     * @return
     */
    public static Map<String, String> getDomainMsgContextMap(String remoteDomain) {
        CatMsgContext context = new CatMsgContext();
        Cat.logRemoteCallClient(context, remoteDomain);
        Map<String, String> contextMap = new LinkedHashMap<>();
        contextMap.put(Cat.Context.PARENT, context.getProperty(Cat.Context.PARENT));
        contextMap.put(Cat.Context.ROOT, context.getProperty(Cat.Context.ROOT));
        contextMap.put(Cat.Context.CHILD, context.getProperty(Cat.Context.CHILD));
        contextMap.put(CatMsgConstants.APPLICATION_KEY, Cat.getManager().getDomain());
        return contextMap;
    }

    ;

    /**
     * 构建远程客户端
     *
     * @param crossName  埋点名称
     * @param crossValue 埋点值
     * @param msgIdMap   远程客户端携带过来的消息编号
     * @return
     */
    public static Transaction getCrossTransactionMsg(String crossName, String crossValue, Map<String, String> msgIdMap) {
        Transaction transaction = Cat.newTransaction(crossName, crossValue);

        if (msgIdMap != null && msgIdMap.get(Cat.Context.ROOT) != null) {
            Cat.Context context = new CatMsgContext();
            context.addProperty(Cat.Context.ROOT, msgIdMap.get(Cat.Context.ROOT));
            context.addProperty(Cat.Context.PARENT, msgIdMap.get(Cat.Context.PARENT));
            context.addProperty(Cat.Context.CHILD, msgIdMap.get(Cat.Context.CHILD));
            Cat.logRemoteCallServer(context);

            Event crossAppEvent = Cat.newEvent(CatMsgConstants.PROVIDER_CALL_APP, msgIdMap.get(CatMsgConstants.APPLICATION_KEY));
            crossAppEvent.setStatus(Event.SUCCESS);
            transaction.addChild(crossAppEvent);
        }
        return transaction;
    }

    /**
     * 构建消费者消息
     *
     * @param crossName  消息name
     * @param crossValue 消息值
     * @param msgIdMap   消息串联编号,由远程客户端携带过来的客户编号
     * @return
     */
    public static Transaction getCrossMQTransactionMsg(String crossName, String crossValue, Map<String, String> msgIdMap) {
        Transaction transaction = Cat.newTransaction(crossName, crossValue);

        if (msgIdMap != null && msgIdMap.get(Cat.Context.ROOT) != null) {
            Cat.Context context = new CatMsgContext();
            context.addProperty(Cat.Context.ROOT, msgIdMap.get(Cat.Context.ROOT));
            context.addProperty(Cat.Context.PARENT, msgIdMap.get(Cat.Context.PARENT));
            // 这里不需要构建客户端带过来的编号，因为消费者是多个，避免LOG被覆盖的情况。
            context.addProperty(Cat.Context.CHILD, Cat.getCurrentMessageId());
            Cat.logRemoteCallServer(context);

            Event crossAppEvent = Cat.newEvent(CatMsgConstants.PROVIDER_CALL_APP, msgIdMap.get(CatMsgConstants.APPLICATION_KEY));
            crossAppEvent.setStatus(Event.SUCCESS);
            transaction.addChild(crossAppEvent);
        }
        return transaction;
    }

    /**
     * 执行事务消息
     *
     * @param name     名称
     * @param value    值
     * @param consumer -> 具体的消费逻辑
     * @return
     */
    public static <T> T processTransactionMsg(String name, String value,  FunctionProcess<Transaction, T> consumer) throws Exception {
        Transaction transaction = getCrossTransactionMsg(name, value, null);
        try {
            T process = consumer.process(transaction);
            transaction.setSuccessStatus();
            return process;
        } catch (Throwable e) {
            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
    }

}
