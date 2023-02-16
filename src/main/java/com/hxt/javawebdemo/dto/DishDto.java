package com.hxt.javawebdemo.dto;

import com.hxt.javawebdemo.entity.Dish;
import com.hxt.javawebdemo.entity.DishFlavor;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish { // data transfor object 联系了dish和dishflavor两个表，方便数据返回页面展示

    private List<DishFlavor> flavors = new ArrayList<>(); // 可以有多个flavor，少冰少糖之类的

    private String categoryName;

    private Integer copies;
}
