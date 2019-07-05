package com.fbtest.mapper.autogen;

import com.fbtest.model.TAuthRole;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TAuthRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TAuthRole record);

    TAuthRole selectByPrimaryKey(Integer id);

    List<TAuthRole> selectAll();

    int updateByPrimaryKey(TAuthRole record);
}