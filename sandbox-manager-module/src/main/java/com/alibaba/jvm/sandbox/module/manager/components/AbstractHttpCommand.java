package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.enums.CommandEnums;
import com.alibaba.jvm.sandbox.module.manager.util.PropertyUtil;
import com.alibaba.jvm.sandbox.module.manager.util.ResponseUtils;
import com.lkx.jvm.sandbox.core.model.command.CommandConfigRequest;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
import com.lkx.jvm.sandbox.core.model.response.JsonResult;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import com.lkx.jvm.sandbox.core.util.HttpUtil;
import com.lkx.jvm.sandbox.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 14:44
 */
public abstract class AbstractHttpCommand<T extends CommandInfoModel>  {

    private Logger logger = LoggerFactory.getLogger(getClass());




    public abstract CommandEnums commandName();



}
