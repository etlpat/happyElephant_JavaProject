<!-- 显示商品的需求信息 -->
<template>
    <div id="commodityInfo" v-if="good">
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
                    <label class="introduce"><i class="el-icon-sell"></i>需求：</label>
                    <label>{{ good.content }}</label>
                </div>
            </div>
            <div class="down">
                <!-- tooltip文字提示框（鼠标悬浮在上方，则提示文字） -->
                <el-tooltip class="item" effect="dark" :content="buyerInfo" placement="top-start">
                    <label class="buyerInfo">
                        <i class="el-icon-warning"></i>&nbsp;买家信息
                    </label>
                </el-tooltip>
                <label class="own">
                    <i class="el-icon-user"></i>&nbsp;{{ good.ownName }}
                </label>
                <label class="timer">
                    <i class="el-icon-timer"></i>&nbsp;发布日期:{{ $dateUtils.formatDate(good.createTime)
                    }}&nbsp;&nbsp;&nbsp;
                    <i class="el-icon-time"></i>&nbsp;更新日期:{{ $dateUtils.formatDate(good.updateTime) }}
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
import DateUtils from "../util/DateUtils.js"
export default {
    data() {
        return {
            good: null,// 该页面的商品对象
            buyerInfo: "",// 买家信息
        }
    },
    methods: {
        exit() {
            this.$router.back();// 返回上一页
        }
    },
    mounted() {
        if (this.$route.params.good) {
            this.good = JSON.parse(this.$route.params.good);// 接收传来的商品数据

            this.buyerInfo = "买家姓名：" + this.good.ownName
                + "； 买家地址：" + this.good.address
                + "； 发布时间：" + DateUtils.formatDateTime(this.good.createTime);
        }
    }
}
</script>

<style lang="scss" scoped>
#commodityInfo {
    width: 75%;
    height: 100%;
    box-sizing: border-box;
    display: flex;
    margin: 50px auto;
    border-radius: 10px;
    border: 0.5px solid gainsboro;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    background-color: rgba(255, 255, 0, 0.07);


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

            .buyerInfo {
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
                    margin-left: 25px;

                    .el-button {
                        margin-right: 5px;
                    }
                }
            }
        }
    }
}
</style>