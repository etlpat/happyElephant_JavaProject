# create database yumDashEpt;
USE yumDashEpt;


# 1.员工表 -- 管理端
DESC employee;
SELECT * FROM employee;
# update employee set status = 1;


# 2.分类表 -- 管理端
DESC category;
SELECT * FROM category;


# 3.菜品表 -- 管理端
DESC dish;
SELECT * FROM dish;


# 4.菜品口味表 -- 管理端
DESC dish_flavor;
SELECT * FROM dish_flavor;


# 5.套餐表 -- 管理端
DESC setmeal;
SELECT * FROM setmeal;


# 6.套餐菜品关系表 -- 管理端
DESC setmeal_dish;
SELECT * FROM setmeal_dish;
