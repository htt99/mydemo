package com.hxt.javawebdemo.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxt.javawebdemo.entity.DishFlavor;
import com.hxt.javawebdemo.mapper.DishFlavorMapper;
import com.hxt.javawebdemo.services.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
