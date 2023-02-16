package com.hxt.javawebdemo.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxt.javawebdemo.entity.Employee;
import com.hxt.javawebdemo.mapper.EmployeeMapper;
import com.hxt.javawebdemo.services.EmployeeService;
import org.springframework.stereotype.Service;

@Service("EmployeeServiceImpl")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
