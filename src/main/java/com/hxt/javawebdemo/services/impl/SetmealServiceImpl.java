package com.hxt.javawebdemo.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxt.javawebdemo.common.CustomException;
import com.hxt.javawebdemo.dto.SetmealDto;
import com.hxt.javawebdemo.entity.Setmeal;
import com.hxt.javawebdemo.entity.SetmealDish;
import com.hxt.javawebdemo.mapper.SetmealMapper;
import com.hxt.javawebdemo.services.SetmealDishService;
import com.hxt.javawebdemo.services.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        // 缺少套餐id
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        list.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1); // 在售

        int count = this.count(queryWrapper);
        if(count > 0)
            throw new CustomException("套餐正在售卖中，不能删除");

        // 删除选中套餐
        this.removeByIds(ids); // removeById只能主键使用

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper1);

    }
}
