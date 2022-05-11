package com.yr.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yr.entity.Employee;
import com.yr.mapper.EmployeeMapper;
import com.yr.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
