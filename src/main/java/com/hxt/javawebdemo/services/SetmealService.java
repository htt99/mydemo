package com.hxt.javawebdemo.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxt.javawebdemo.dto.SetmealDto;
import com.hxt.javawebdemo.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);
}
