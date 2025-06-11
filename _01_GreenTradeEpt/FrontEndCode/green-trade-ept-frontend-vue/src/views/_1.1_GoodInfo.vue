<!-- 显示商品的详细信息 -->
<template>
    <div id="goodInfo" v-if="good">
        <!-- 商品图片 -->
        <img :src="'orderImgs/' + good.picture" alt="" class="goodImg">
        <!-- 商品信息 -->
        <div class="right">
            <div class="up">
                <label class="title">
                    {{ good.title }}&nbsp;
                    <span class="type">[{{ good.type == 'goods' ? '供' : '需' }}]</span>
                </label>
                <div class="content">
                    <label class="introduce"><i class="el-icon-shopping-bag-1"></i>简介：</label>
                    <label>{{ good.content }}</label>
                </div>
            </div>
            <div class="down">
                <label class="price">
                    <i class="el-icon-shopping-cart-1"></i>&nbsp;￥{{ good.price.toFixed(2) }}
                </label>
                <label class="own">
                    <i class="el-icon-user"></i>&nbsp;{{ good.ownName }}
                </label>
                <label class="timer">
                    <i class="el-icon-timer"></i>&nbsp;发布时间:{{ $dateUtils.formatDate(good.createTime)
                    }}&nbsp;&nbsp;&nbsp;
                    <i class="el-icon-time"></i>&nbsp;更新时间:{{ $dateUtils.formatDate(good.updateTime) }}
                </label>
                <div class="bottom">
                    <div class="icon">
                        <div>
                            <i class="el-icon-thumb"></i>
                            <label>点赞</label>
                        </div>
                        <div>
                            <i class="el-icon-star-off"></i>
                            <label>收藏</label>
                        </div>
                        <div>
                            <i class="el-icon-edit-outline"></i>
                            <label>评论</label>
                        </div>
                    </div>
                    <div class="btn">
                        <el-button type="primary" @click="saveGood">加入购物车</el-button>
                        <el-button type="primary" @click="exit">返回上一页</el-button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div v-else>
        <el-empty description="商品信息加载中..."></el-empty>
    </div>
</template>

<script>
import { save } from "../apis/ShoppingcartService.js";

export default {
    data() {
        return {
            good: null,// 该页面的商品对象
        }
    },
    methods: {
        exit() {
            this.$router.back();// 返回上一页
        },

        // 将商品对象保存到购物车
        async saveGood() {
            let username = this.$store.state.loginUsername;
            if (!username) {
                alert("请先登录！");
                return;
            }
            // 创建shoppingcart对象
            const shoppingcart = {
                orderId: this.good.orderId,
                count: 1,
                ownName: username,
            };
            await save(shoppingcart);// 将信息交给数据库
            this.$message.success('商品成功放入购物车！');
            this.exit();
        }
    },
    mounted() {
        if (this.$route.params.good) {
            this.good = JSON.parse(this.$route.params.good);// 接收传来的商品数据
        }
    }
}
</script>

<style lang="scss" scoped>
#goodInfo {
    width: 75%;
    height: 100%;
    box-sizing: border-box;
    display: flex;
    margin: 50px auto;
    border-radius: 10px;
    border: 0.5px solid gainsboro;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    background-color: rgba(0, 255, 0, 0.07);

    .goodImg {
        width: 280px;
        height: 280px;
        border-radius: 10px;
        margin-right: 20px;
    }

    .right {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: flex-start;
        padding-top: 10px;
        padding-bottom: 10px;

        .up {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            align-items: flex-start;

            .title {
                font-size: 23px;
                font-weight: 595;
                margin-bottom: 10px;
            }

            .type {
                color: gray;
                font-size: 17px;
            }

            .content {
                font-size: 18px;
                text-align: left;
                display: flex;
                justify-content: space-between;
                margin-left: 5px;

                .introduce {
                    white-space: nowrap;
                    /* 禁止换行 */
                }
            }
        }

        .down {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            align-items: flex-start;
            margin-left: 5px;

            .price {
                font-size: 18px;
                color: red;
            }

            .own {
                color: gray;
                font-size: 15px;
            }

            .timer {
                color: gray;
                font-size: 15px;
            }

            .bottom {
                display: flex;
                justify-content: space-between;
                margin-top: 10px;
                margin-left: 15px;

                .icon {
                    display: flex;

                    div {
                        display: flex;
                        flex-direction: column;
                        justify-content: space-between;
                        align-items: center;
                        margin-right: 5px;
                        border-radius: 5px;
                        border: 0.1px solid gainsboro;
                    }

                    i {
                        font-size: 25px;
                    }

                    label {
                        font-size: 13px;
                    }
                }

                .btn {
                    display: flex;
                    align-items: center;
                    margin-left: 15px;

                    .el-button {
                        margin-right: 5px;
                    }
                }
            }
        }
    }
}
</style>