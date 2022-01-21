package com.sandbox.application.plugin.cat.listener;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.sandbox.application.plugin.cat.utils.RangeUtils;
import com.sandbox.application.plugin.cat.utils.SqlParseUtils;
import com.sandbox.manager.api.AdviceNameDefinition;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Mybatis 埋点
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/19 - 14:44
 */
@Component
public class MybatisPointListener extends AbstractTransactionPointListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.CAT_MYBATIS_TRANSACTION_POINT;
    }

    private int getMaxAffectRow() {
        return 100;
    }

    private int getMaxListRow() {
        return 1000;
    }

    @Override
    public void before(Advice advice) throws Throwable {
        Object mappedStatement = advice.getParameterArray()[0];
        Object parameter = advice.getParameterArray()[1];
        String sql = getBoundSql(mappedStatement, parameter);
        String mapperId = getMapperId(mappedStatement);
        Transaction transaction = Cat.newTransaction("SQL", mapperId);

        parseSql(sql);

        getTransactionThreadLocal().set(transaction);
    }

    private void parseSql(String sql) {

        MySqlSchemaStatVisitor mySqlSchemaStatVisitor = SqlParseUtils.parseMysqlSql(sql);
        Map<TableStat.Name, TableStat> tables = mySqlSchemaStatVisitor.getTables();
        // 分析SQL中的数据,如果多表关联的话，可能会出问题。
        if (tables != null && tables.size() > 0) {
            String name = tables.keySet().stream().findFirst().get().getName();
            String command = tables.values().stream().findFirst().get().toString();
            Cat.newEvent("SQL.Table", name);
            Cat.newEvent("SQL.Command", command);
        }

        // 植入日志
//        Cats.logDebug(sql);
    }

    @Override
    public void afterReturning(Advice advice) throws Throwable {
        Optional.ofNullable(advice.getReturnObj()).ifPresent((rowNumber) -> {
            // 计算影响行数
            if (rowNumber instanceof Number) {
                Number row = (Number) rowNumber;
                invokeWarnEvent("SQL.Warn.AffectRow", row.intValue(), getMaxAffectRow());
            } else if (rowNumber instanceof Collection) {
                Collection<?> row = (Collection<?>) rowNumber;
                invokeWarnEvent("SQL.Warn.ListRow", row.size(), getMaxListRow());
            }
        });
    }

    /**
     * 为一些危险埋点触发埋点事件
     *
     * @param eventName 事件名称
     * @param row       返回行数
     * @param maxRow    最大行数
     */
    private void invokeWarnEvent(String eventName, Integer row, Integer maxRow) {
        if (row > maxRow) {
            Integer rangeNumber = RangeUtils.getRangeNumber(row, maxRow);
            Cat.newEvent(eventName, rangeNumber.toString());
        }
    }

    /**
     * 获取SQL
     *
     * @param obj
     * @param params
     * @return
     */
    public String getBoundSql(Object obj, Object params) {
        try {
            Object boundSql = MethodUtils.invokeMethod(obj, "getBoundSql", params);
            return (String) MethodUtils.invokeMethod(boundSql, "getSql");
        } catch (Exception e) {
            logger.error("解析Mybatis异常", e);
        }
        return null;
    }

    public String getMapperId(Object obj) {
        try {
            return (String) MethodUtils.invokeMethod(obj, "getId");
        } catch (Exception e) {
            logger.error("解析Mybatis异常", e);
        }
        return null;
    }

}
