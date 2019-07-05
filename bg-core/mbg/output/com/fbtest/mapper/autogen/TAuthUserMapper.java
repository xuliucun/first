package com.fbtest.mapper.autogen;

import com.fbtest.model.TAuthUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TAuthUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TAuthUser record);

    TAuthUser selectByPrimaryKey(Integer id);

    List<TAuthUser> selectAll();

    int updateByPrimaryKey(TAuthUser record);
}