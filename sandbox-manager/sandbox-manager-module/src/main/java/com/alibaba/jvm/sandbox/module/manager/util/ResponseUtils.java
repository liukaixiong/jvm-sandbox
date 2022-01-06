package com.alibaba.jvm.sandbox.module.manager.util;

import com.lkx.jvm.sandbox.core.model.response.JsonResult;
import com.lkx.jvm.sandbox.core.util.JsonUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 14:33
 */
public class ResponseUtils {


    /**
     * 统一的正常返回
     *
     * @param response
     * @throws Exception
     */
    public static void writeTrue(HttpServletResponse response) throws Exception {
        writeJson(response, JsonResult.builder().success(true).build());
    }

    /**
     * 写入JSON数据返回给客户端
     *
     * @param response
     * @param responseObject
     * @throws Exception
     */
    public static void writeJson(HttpServletResponse response, Object responseObject) throws Exception {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json;charset=utf-8");
        write(writer, JsonUtils.toJsonString(responseObject));
    }

    private static void write(PrintWriter writer, Object responseObject) {
        writer.print(responseObject);
        writer.flush();
        writer.close();
    }

}
