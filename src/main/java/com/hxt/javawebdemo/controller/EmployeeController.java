package com.hxt.javawebdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxt.javawebdemo.common.R;
import com.hxt.javawebdemo.entity.Employee;
import com.hxt.javawebdemo.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

//    @Autowired
    @Resource(name = "EmployeeServiceImpl")
    private EmployeeService employeeService;
    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper); // 数据库索引设置了"UNIQUE"保证唯一姓名

        //3、如果没有查询到则返回登录失败结果
        if(emp == null)
            return R.error("登陆失败");

        //4、密码比对，如果不一致则返回登录失败结果
        if(!password.equals(emp.getPassword()))
            return R.error("登录失败");

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId()); // 缓存里的long不会失去精度
        return R.success(emp);
    }
    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee"); // 清空session。需要和前端localStorage相关浏览器代码联动
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){ // 拿到的前端表单提交的数据是json，需要反序列化为entity对象
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页显示数据
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){ // get没有requestbody了?
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);
        //构造分页构造器(mbp提供
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name); // boolean condition, R column, Object val
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 修改用户数据（前端获得数据）
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        // 修改者的id！被修改者的id实际上是前端从请求url里拆出来的，然后初始化进页眉的表格employee数据
        Long empId = (Long)request.getSession().getAttribute("employee"); // localstorage里存的是long
        employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());
        // status也是前端传进来的，所以后端只剩update了
        // mybatis-plus
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){ // 不是按照?xx=xx的格式传来的，要自己拆一下
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id); // 序列化后的object就可以
        if(employee != null)
            return R.success(employee);
        return R.error("没有查询到对应员工信息");
    }
}
