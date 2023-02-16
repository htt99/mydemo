package com.hxt.javawebdemo.dto;

import com.hxt.javawebdemo.entity.Setmeal;
import com.hxt.javawebdemo.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
