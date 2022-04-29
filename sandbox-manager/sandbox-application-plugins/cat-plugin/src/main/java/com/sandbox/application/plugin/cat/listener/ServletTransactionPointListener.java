package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import com.lkx.jvm.sandbox.core.util.InterfaceProxyUtils;
import com.sandbox.application.plugin.cat.consts.CatMsgConstants;
import com.sandbox.application.plugin.cat.utils.CatMsgContext;
import com.sandbox.manager.api.AdviceNameDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/19 - 10:45
 */
@Component
public class ServletTransactionPointListener extends AbstractTransactionPointListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.CAT_URL_TRANSACTION_POINT;
    }

    @Override
    public void before(Advice advice) throws Throwable {

        // 俘虏HttpServletRequest参数为傀儡
        final IHttpServletRequest httpServletRequest = InterfaceProxyUtils.puppet(
                IHttpServletRequest.class,
                advice.getParameterArray()[0]
        );

        String uri = httpServletRequest.getRequestURI();

        String rootId = httpServletRequest.getHeader(Cat.Context.ROOT);
        String parentId = httpServletRequest.getHeader(Cat.Context.PARENT);
        String childId = httpServletRequest.getHeader(Cat.Context.CHILD);
        Transaction transaction = null;
        // 如果是远程调用的链路串联
        if (StringUtils.isNoneEmpty(rootId, parentId, childId)) {
            String contentType = httpServletRequest.getContentType();
            logger.debug(" 开启CAT消息树串联模式 : " + uri + " \t " + contentType + " 相关消息编号 : rootId : " + rootId + " parentId :" + parentId + " childId : " + childId);
            transaction = Cat.newTransaction(CatMsgConstants.CROSS_SERVER, uri);
            Cat.Context context = new CatMsgContext();
            context.addProperty(Cat.Context.ROOT, rootId);
            context.addProperty(Cat.Context.PARENT, parentId);
            context.addProperty(Cat.Context.CHILD, childId);

            Cat.logRemoteCallServer(context);
            // 构建串联情况
            builderCrossInfo(httpServletRequest, transaction);
        } else {
            transaction = Cat.newTransaction(CatConstants.TYPE_URL, uri);
        }

        getTransactionThreadLocal().set(transaction);
    }

//    @Override
//    public void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
//        System.out.println("--------->>>" + callJavaClassName + "#" + callJavaMethodName + "#" + callJavaMethodDesc + "\t" + callLineNum);
//    }


    @Override
    public void after(Advice advice) throws Throwable {
        final IHttpServletResponse httpServletResponse = InterfaceProxyUtils.puppet(
                IHttpServletResponse.class,
                advice.getParameterArray()[1]
        );

        httpServletResponse.setHeader("trace_id",Cat.getCurrentMessageId());

        super.after(advice);
    }

    private void builderCrossInfo(IHttpServletRequest httpServletRequest, Transaction transaction) {
        Event crossAppEvent = Cat.newEvent(CatMsgConstants.PROVIDER_CALL_APP, httpServletRequest.getHeader(CatMsgConstants.APPLICATION_KEY));
        Event crossServerEvent = Cat.newEvent(CatMsgConstants.PROVIDER_CALL_SERVER, httpServletRequest.getRemoteAddress());
        crossAppEvent.setStatus(Event.SUCCESS);
        crossServerEvent.setStatus(Event.SUCCESS);
        transaction.addChild(crossAppEvent);
        transaction.addChild(crossServerEvent);
    }

    /**
     * HTTP接入信息
     */
    static class HttpAccess {
        final long beginTimestamp = System.currentTimeMillis();
        final String from;
        final String method;
        final String uri;
        final Map<String, String[]> parameterMap;
        final String userAgent;
        int status = 200;

        HttpAccess(final String from,
                   final String method,
                   final String uri,
                   final Map<String, String[]> parameterMap,
                   final String userAgent) {
            this.from = from;
            this.method = method;
            this.uri = uri;
            this.parameterMap = parameterMap;
            this.userAgent = userAgent;
        }

        void setStatus(int status) {
            this.status = status;
        }

    }

    interface IHttpServletRequest {

        @InterfaceProxyUtils.ProxyMethod(name = "getRemoteAddr")
        String getRemoteAddress();

        String getMethod();

        String getRequestURI();

        Map<String, String[]> getParameterMap();

        String getHeader(String name);

        String getContentType();

    }

    interface IHttpServletResponse {

        public void setHeader(String name, String value);

    }

}
