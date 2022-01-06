package com.sandbox.application.plugin.cat.event;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.lkx.jvm.sandbox.core.compoents.PrintFormat;
import com.sandbox.application.plugin.cat.utils.SqlParseUtils;

import java.util.Map;

/**
 * 处理SQL事件的情况
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/26 - 18:27
 */
public class SqlEvent {

    public static void process(ModuleEventWatcher watcher) {
        // UPDATE
        new EventWatchBuilder(watcher)
                .onClass("org.apache.ibatis.executor.statement.PreparedStatementHandler")
                .includeSubClasses()
                .onBehavior("update")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        if (!advice.isProcessTop()) {
                            return;
                        }
                        // 记录耗时
                        advice.getProcessTop().attach(System.currentTimeMillis());
                    }


                    @Override
                    protected void after(Advice advice) throws Throwable {
                        if (!advice.isProcessTop()) {
                            return;
                        }
                        String params = advice.getParameterArray()[0].toString();
                        String sql = params.split(":")[1];
                        // 记录成功或者失败
                        int success = 1;
                        if (advice.isThrows()) {
                            success = -1;
                        }
                        MySqlSchemaStatVisitor mySqlSchemaStatVisitor = SqlParseUtils.parseMysqlSql(sql);
                        Map<TableStat.Name, TableStat> tables = mySqlSchemaStatVisitor.getTables();
                        String name = tables.keySet().stream().findFirst().get().getName();
                        String command = tables.values().stream().findFirst().get().toString();
                        Object returnObj = advice.getReturnObj();
                        Long start = advice.getProcessTop().attachment();
                        Long time = System.currentTimeMillis() - start;

                        PrintFormat format = new PrintFormat();
                        format.put("名称", name);
                        format.put("执行命令", command);
                        format.put("执行结果", success);
                        format.put("SQL", sql);
                        format.put("返回参数", returnObj);
                        format.put("耗时", time);
                        format.printLog();
                    }
                });
    }

}
