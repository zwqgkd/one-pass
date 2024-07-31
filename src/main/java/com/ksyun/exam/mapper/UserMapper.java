package com.ksyun.exam.mapper;

import java.util.List;
import java.util.Optional;

import com.ksyun.exam.mapper.module.UserRecord;
//import org.apache.ibatis.annotations.*;
//import org.apache.ibatis.type.JdbcType;
//import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
//import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
//import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
//import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
//import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
//import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;

//@Mapper
//public interface UserMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<UserRecord>, CommonUpdateMapper {

//    @SelectProvider(type= SqlWithRecordLockProviderAdapter.class, method="select")
//    @Results(id="UserResult",value={
//            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
//            @Result(column = "balance_amount", property = "balanceAmount", jdbcType = JdbcType.DECIMAL)
//    })
//    List<UserRecord> selectMany(SelectStatementProvider selectStatement);
//
//    @SelectProvider(type= SqlWithRecordLockProviderAdapter.class, method="select")
//    @ResultMap("UserResult")
//    Optional<UserRecord> selectOne(SelectStatementProvider selectStatement);
//}
