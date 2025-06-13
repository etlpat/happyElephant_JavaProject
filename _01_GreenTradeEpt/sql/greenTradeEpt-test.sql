# 1.用户表
DESC tb_user;
# DELETE FROM `tb_user`;
SELECT * FROM tb_user;
# DELETE FROM tb_user WHERE user_name = '555';
# UPDATE tb_user SET nick_name='张三丰',phone='2333333',address='张山峰'  WHERE user_name = '张三';
# UPDATE tb_user SET avatar = CONCAT(user_name, '.png');

# 2.订单表
DESC tb_order;
SELECT * FROM tb_order;
SELECT * FROM tb_order ORDER BY order_id ASC;
# UPDATE tb_order SET picture = CONCAT(order_id, title, '.png');
SELECT * FROM tb_order 
	WHERE title LIKE '%西瓜%' 
	OR content LIKE '%西瓜%' 
	OR own_name LIKE '%西瓜%';


# 3.农业知识表
DESC tb_knowledge;
SELECT * FROM tb_knowledge;
# UPDATE tb_knowledge SET pic_path = CONCAT(knowledge_id, title, '.png');

SELECT * FROM tb_knowledge;


# 4.农业知识评论表
DESC tb_discuss;
SELECT * FROM tb_discuss;

# 5.专家表
DESC tb_expert;
SELECT * FROM tb_expert;

# 6.问题表
DESC tb_question;
SELECT * FROM tb_question;

# 7.预约表
DESC tb_reserve;
SELECT * FROM tb_reserve;

# 8.购物车表
DESC tb_shoppingcart;
SELECT * FROM tb_shoppingcart;
# DELETE FROM `tb_shoppingcart`;

# 9.购物清单表
DESC tb_purchase;
SELECT * FROM tb_purchase;

# 10.银行表
DESC tb_bank;
SELECT * FROM tb_bank;