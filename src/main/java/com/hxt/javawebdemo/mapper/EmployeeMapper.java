package com.hxt.javawebdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxt.javawebdemo.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
