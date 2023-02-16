package com.hxt.javawebdemo.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxt.javawebdemo.dto.DishDto;
import com.hxt.javawebdemo.entity.Dish;
import com.hxt.javawebdemo.mapper.DishMapper;

public interface DishService extends IService<Dish> {
    //新增菜品及菜品对应的口味
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
