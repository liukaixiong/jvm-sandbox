package com.sandbox.application.plugin.cat.utils;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/26 - 18:12
 */
public class SqlParseUtils {


    public static MySqlSchemaStatVisitor parseMysqlSql(String sql) {
        SQLStatement sqlStatement = SQLUtils.parseSingleStatement(sql, DbType.mysql);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();

        sqlStatement.accept(visitor);

        return visitor;
    }

}
