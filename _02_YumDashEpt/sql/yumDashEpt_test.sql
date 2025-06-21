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

UPDATE dish SET is_deleted = 0;
UPDATE dish SET STATUS = 1;


# 4.菜品口味表 -- 管理端
DESC dish_flavor;
SELECT * FROM dish_flavor;


# 5.套餐表 -- 管理端
DESC setmeal;
SELECT * FROM setmeal;

UPDATE setmeal SET is_deleted = 0;
UPDATE setmeal SET STATUS = 1;


# 6.套餐菜品关系表 -- 管理端
DESC setmeal_dish;
SELECT * FROM setmeal_dish;


# 7.用户表 -- 用户端
DESC USER;
SELECT * FROM USER;
# update user set status = 1;


# 8.地址表 -- 用户端
DESC address_book;
SELECT * FROM address_book;
SELECT * FROM address_book WHERE province_code IS NULL;


# 9.购物车表 -- 用户端
DESC shopping_cart;
SELECT * FROM shopping_cart;
# select sum(number * amount) from shopping_cart where user_id = 1935718294321422338;


# 10.订单表 -- 用户端
DESC orders;
SELECT * FROM orders;
# update orders set status = 2;


# 11.订单细节表 -- 用户端
DESC order_detail;
SELECT * FROM order_detail;




