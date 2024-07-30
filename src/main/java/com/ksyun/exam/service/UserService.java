package com.ksyun.exam.service;

import com.ksyun.exam.mapper.UserMapper;
import com.ksyun.exam.mapper.module.UserDynamicSqlSupport;
import com.ksyun.exam.mapper.module.UserRecord;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int insertOne(Long uid, BigDecimal balanceAmount) {
        UserRecord row=new UserRecord(uid,balanceAmount);
        InsertStatementProvider<UserRecord> insertStatement=insert(row)
                .into(UserDynamicSqlSupport.user)
                .map(UserDynamicSqlSupport.id).toProperty("id")
                .map(UserDynamicSqlSupport.balanceAmount).toProperty("balanceAmount")
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return userMapper.insert(insertStatement);
    }

    public int insertMany(List<UserRecord> rows){
        MultiRowInsertStatementProvider<UserRecord> insertStatement=insertMultiple(rows)
                .into(UserDynamicSqlSupport.user)
                .map(UserDynamicSqlSupport.id).toProperty("id")
                .map(UserDynamicSqlSupport.balanceAmount).toProperty("balanceAmount")
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return userMapper.insertMultiple(insertStatement);
    }

    public Optional<UserRecord> selectOneById(Long uid){
        SelectStatementProvider selectStatement = select(UserDynamicSqlSupport.id,UserDynamicSqlSupport.balanceAmount)
                .from(UserDynamicSqlSupport.user)
                .where(UserDynamicSqlSupport.id, isEqualTo(uid))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return userMapper.selectOne(selectStatement);
    }

    public int updateBalanceAmountById(Long uid, BigDecimal balanceAmount){
        UpdateStatementProvider updateStatement = update(UserDynamicSqlSupport.user)
                .set(UserDynamicSqlSupport.balanceAmount).equalTo(balanceAmount)
                .where(UserDynamicSqlSupport.id, isEqualTo(uid))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return userMapper.update(updateStatement);
    }

}
