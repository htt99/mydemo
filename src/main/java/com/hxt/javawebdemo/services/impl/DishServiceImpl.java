package com.hxt.javawebdemo.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxt.javawebdemo.dto.DishDto;
import com.hxt.javawebdemo.entity.Dish;
import com.hxt.javawebdemo.entity.DishFlavor;
import com.hxt.javawebdemo.mapper.DishMapper;
import com.hxt.javawebdemo.services.DishFlavorService;
import com.hxt.javawebdemo.services.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishFlavorService dishFlavorService;

    @Override
    @Transactional // 操作多张表，需要事务管理
    public void saveWithFlavor(DishDto dishDto) {
        // 继承了dish，保存其中的dish字段
        this.save(dishDto);

        Long dishId = dishDto.getId();// 继承下来的

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList()); // 给每个flavor补充上菜品id

        dishFlavorService.saveBatch(flavors);
    }

    @Override
//    @Transactional
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto); // 不用ignore

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);

        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表基本信息
        this.updateById(dishDto); // 子类

        // 清理口味数据
        dishFlavorService.removeById(dishDto.getId());

        // 重新添加新的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList()); // 给每个flavor补充上菜品id

        dishFlavorService.saveBatch(flavors);
    }
}
