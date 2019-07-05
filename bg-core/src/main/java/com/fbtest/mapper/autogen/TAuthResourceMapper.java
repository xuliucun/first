package com.fbtest.mapper.autogen;

import com.fbtest.model.TAuthResource;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TAuthResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TAuthResource record);

    TAuthResource selectByPrimaryKey(Integer id);

    List<TAuthResource> selectAll();

    int updateByPrimaryKey(TAuthResource record);
}