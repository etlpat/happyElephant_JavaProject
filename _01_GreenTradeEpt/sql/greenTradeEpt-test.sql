# 1.用户表
DESC tb_user;
DELETE FROM `tb_user`;
SELECT * FROM tb_user;


# 2.订单表
DESC tb_order;
SELECT * FROM tb_order;
SELECT * FROM tb_order ORDER BY order_id ASC;
# UPDATE tb_order SET picture = CONCAT(order_id, title, '.png');