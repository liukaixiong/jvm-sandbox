package com.sandbox.application.plugin.cat.spring;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.AbstractMessage;
import com.dianping.cat.message.internal.NullMessage;
import com.sandbox.application.plugin.cat.consts.CatMsgConstants;
import com.sandbox.application.plugin.cat.utils.CatMsgContext;
import com.sandbox.application.plugin.cat.utils.Cats;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * cat的URL过滤器拦截
 *
 * @author : liukx
 * @create : 2018/7/2 11:31
 * @email : liukx@elab-plus.com
 */
public class HttpCatCrossFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(HttpCatCrossFilter.class);

    private static final String DEFAULT_APPLICATION_NAME = "default";

    private List<String> includeContentTypeList = new ArrayList<>();

    public void addIncludeContentType(String resource) {
        includeContentTypeList.add(resource);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        String uri = request.getRequestURI();
        String contentType = request.getContentType();

        String rootId = request.getHeader(Cat.Context.ROOT);
        String parentId = request.getHeader(Cat.Context.PARENT);
        String childId = request.getHeader(Cat.Context.CHILD);
        Transaction t = null;
        try {
            // 如果包含调用关系
            if (StringUtils.isNoneEmpty(rootId, parentId, childId)) {
                logger.debug(" 开启CAT消息树串联模式 ... " + uri + " \t " + contentType + " 相关消息编号 : rootId : " + rootId + " parentId :" + parentId + " childId : " + childId);
                t = Cat.newTransaction(CatMsgConstants.CROSS_SERVER, uri);
                Cat.Context context = new CatMsgContext();
                context.addProperty(Cat.Context.ROOT, rootId);
                context.addProperty(Cat.Context.PARENT, parentId);
                context.addProperty(Cat.Context.CHILD, childId);
                Cat.logRemoteCallServer(context);
                Cats.createProviderCross(request, t);
            } else {
                logger.debug(" 开启普通URL请求调用 ... ");
                t = Cat.getProducer().newTransaction(CatConstants.TYPE_URL, uri);
            }
            filterChain.doFilter(req, resp);
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            logger.error("------ Get cat msgtree error : ", e);
            Event event = Cat.newEvent("HTTP_REST_CAT_ERROR", uri);
            event.setStatus(e);
            completeEvent(event);
            t.addChild(event);
            t.setStatus(e.getClass().getSimpleName());
        } finally {
            t.complete();
            logger.debug("CAT 调用完毕 ... ");
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // 初始化默认的拦截请求类型
        includeContentTypeList.add("application/json");
        includeContentTypeList.add("application/x-www-form-urlencoded");
    }

    /**
     * 排除静态资源
     *
     * @param url
     * @return
     */
    public boolean includeContentType(String url) {
        if (url == null) {
            return false;
        }
        for (int i = 0; i < includeContentTypeList.size(); i++) {
            String contentType = includeContentTypeList.get(i);
            if (url.startsWith(contentType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }

    private void completeEvent(Event event) {
        if (event != NullMessage.EVENT) {
            AbstractMessage message = (AbstractMessage) event;
            message.setCompleted(true);
        }
    }

}