USE flareTraceEpt;
SHOW TABLES;

# 1.用户表
DESC tb_user;
SELECT * FROM tb_user;
SELECT * FROM tb_user WHERE phone = 13000000003;


# 2.商铺类型表
DESC tb_shop_type;
SELECT * FROM tb_shop_type;


# 3.商铺表
DESC tb_shop;
SELECT * FROM tb_shop;
# update tb_shop set x=120.149992,Y=30.334229 where id = 1;


# 4.代金券表
DESC tb_voucher;
SELECT * FROM tb_voucher;


# 5.代金券订单表
DESC tb_voucher_order;
SELECT * FROM tb_voucher_order;
SELECT * FROM tb_voucher_order ORDER BY create_time DESC;


# 6.秒杀券表
DESC tb_seckill_voucher;
SELECT * FROM tb_seckill_voucher;


# 7.探店博客表
DESC tb_blog;
SELECT * FROM tb_blog;
# update tb_blog set liked = 50 where id = 10;


# 8.博客评论表
DESC tb_blog_comments;
SELECT * FROM tb_blog_comments;


# 9.用户关注表
DESC tb_follow;
SELECT * FROM tb_follow;
# delete from tb_follow where user_id = 1012;


