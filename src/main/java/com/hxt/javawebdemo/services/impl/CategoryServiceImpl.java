package com.hxt.javawebdemo.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxt.javawebdemo.common.CustomException;
import com.hxt.javawebdemo.entity.Category;
import com.hxt.javawebdemo.entity.Dish;
import com.hxt.javawebdemo.entity.Setmeal;
import com.hxt.javawebdemo.mapper.CategoryMapper;
import com.hxt.javawebdemo.services.CategoryService;
import com.hxt.javawebdemo.services.DishService;
import com.hxt.javawebdemo.services.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        // 如果该类别关联了东西就不能删
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishQueryWrapper);
        if(count1 > 0){
            throw new CustomException("存在关联菜品，无法删除！");
        }

        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealQueryWrapper);
        if(count2 > 0){
            throw new CustomException("存在关联套餐，无法删除！");
        }


        super.removeById(id);
    }
}
