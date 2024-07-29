package com.ksyun.exam;

import com.ksyun.exam.mapper.UserMapper;
import com.ksyun.exam.mapper.module.UserDynamicSqlSupport;
import com.ksyun.exam.mapper.module.UserRecord;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

@SpringBootTest
class Demo1ApplicationTests {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void contextLoads() {
        System.out.println(UUID.randomUUID().toString());
    }

    @Test
    void testGeneralSelect(){
        try(SqlSession session=sqlSessionFactory.openSession()){
            UserMapper mapper = session.getMapper(UserMapper.class);
            SelectStatementProvider selectStatement = select(UserDynamicSqlSupport.id, UserDynamicSqlSupport.balanceAmount)
                    .from(UserDynamicSqlSupport.user)
//                    .where(UserDynamicSqlSupport.id, isEqualTo(1L))
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
            List<UserRecord> rows=mapper.selectMany(selectStatement);
            System.out.println(rows);
        }
    }

    @Test
    void testGeneralInsert(){
        try(SqlSession session=sqlSessionFactory.openSession()){
            UserMapper mapper = session.getMapper(UserMapper.class);

        }
    }

}
