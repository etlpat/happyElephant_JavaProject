# 1.用户表
DESC tb_user;
DELETE FROM `tb_user`;
SELECT * FROM tb_user;
DELETE FROM tb_user WHERE user_name = '555';


# 2.订单表
DESC tb_order;
SELECT * FROM tb_order;
SELECT * FROM tb_order ORDER BY order_id ASC;
# UPDATE tb_order SET picture = CONCAT(order_id, title, '.png');


# 3.农业知识表
DESC tb_knowledge;
SELECT * FROM tb_knowledge;
# UPDATE tb_knowledge SET pic_path = CONCAT(knowledge_id, title, '.png');


# 4.农业知识评论表
DESC tb_discuss;
SELECT * FROM tb_discuss;

# 5.专家表
DESC tb_expert;
SELECT * FROM tb_expert;

# 6.问题表
DESC tb_question;
SELECT * FROM tb_question;