package com.ksyun.exam;

import com.ksyun.exam.mapper.UserMapper;
import com.ksyun.exam.mapper.module.UserDynamicSqlSupport;
import com.ksyun.exam.mapper.module.UserRecord;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.insert.render.InsertSelectStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(UUID.randomUUID().toString());
    }

    @Test
    void testGeneralSelect(){
        SelectStatementProvider selectStatement = select(UserDynamicSqlSupport.id, UserDynamicSqlSupport.balanceAmount)
                .from(UserDynamicSqlSupport.user)
                .where(UserDynamicSqlSupport.id, isEqualTo(1L))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<UserRecord> rows=userMapper.selectMany(selectStatement);
        System.out.println(rows);
    }

    @Test
    void testGeneralInsert(){
        UserRecord row=new UserRecord(2L, new BigDecimal("100"));
        InsertStatementProvider<UserRecord> insertStatement=insert(row)
                .into(UserDynamicSqlSupport.user)
                .map(UserDynamicSqlSupport.id).toProperty("id")
                .map(UserDynamicSqlSupport.balanceAmount).toProperty("balanceAmount")
                .build()
                .render(RenderingStrategies.MYBATIS3);
        userMapper.insert(insertStatement);
    }

}
