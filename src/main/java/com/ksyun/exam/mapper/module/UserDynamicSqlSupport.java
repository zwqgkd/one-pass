package com.ksyun.exam.mapper.module;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.AliasableSqlTable;

import java.math.BigDecimal;
import java.sql.JDBCType;

public final class UserDynamicSqlSupport {

    public static final User user = new User();

    public static final SqlColumn<Long> id=user.id;

    public static final SqlColumn<BigDecimal> balanceAmount=user.balanceAmount;

    public static final class User extends AliasableSqlTable<User> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<BigDecimal> balanceAmount = column("balance_amount", JDBCType.DECIMAL);

        public User() {
            super("user_balance", User::new);
        }
    }
}
