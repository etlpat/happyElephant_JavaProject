package com.etlpat.pojo.dto;

import com.etlpat.pojo.Setmeal;
import com.etlpat.pojo.SetmealDish;
import lombok.Data;

import java.util.List;


@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}