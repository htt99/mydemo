package com.hxt.javawebdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxt.javawebdemo.common.R;
import com.hxt.javawebdemo.entity.Category;
import com.hxt.javawebdemo.services.CategoryService;
import com.hxt.javawebdemo.services.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){ // 需要和前端传进来的变量名保持一致：可以直接接收url带着的参数
        // mbp分页构造器
        Page<Category> pageInfo = new Page<>();
        // 构造过滤器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> deleteById(Long id){ // 直接接收到url中参数
        log.info("删除分类信息：{}", id);
        // 有关联的东西则不能删
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){ // 没有的字段就是空？
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }

    /**
     * 根据条件获得分类类表
     * @param category
     * @return
     */
    @GetMapping("/list")
     public R<List<Category>> list(Category category){ // 虽然目前传过来只有type，但可以封装成通用的整个entity
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> categories = categoryService.list(queryWrapper);

        return R.success(categories);
     }


}
