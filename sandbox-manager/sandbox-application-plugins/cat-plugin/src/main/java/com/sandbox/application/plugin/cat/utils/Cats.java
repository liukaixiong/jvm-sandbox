package com.sandbox.application.plugin.cat.utils;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import com.lkx.jvm.sandbox.core.util.Strings;
import com.sandbox.application.plugin.cat.consts.CatMsgConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/19 - 10:27
 */
public class Cats {

    public static void createProviderCross(HttpServletRequest request, Transaction t) {
        Event crossAppEvent = Cat.newEvent(CatMsgConstants.PROVIDER_CALL_APP, request.getHeader(CatMsgConstants.APPLICATION_KEY));
        //clientIp
        Event crossServerEvent = Cat.newEvent(CatMsgConstants.PROVIDER_CALL_SERVER, request.getRemoteAddr());
        crossAppEvent.setStatus(Event.SUCCESS);
        crossServerEvent.setStatus(Event.SUCCESS);
        t.addChild(crossAppEvent);
        t.addChild(crossServerEvent);
    }

    public static void logDebug(String content, Object... objects) {
        Cat.logEvent("LOG", "DEBUG", Event.SUCCESS, Strings.lenientFormat(content, "{}", objects));
    }

}
