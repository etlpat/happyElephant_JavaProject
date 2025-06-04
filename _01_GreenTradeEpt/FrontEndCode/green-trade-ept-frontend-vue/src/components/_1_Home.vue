<template>
    <div id="home">
        <div class="left">
            <!-- 搜索框 -->
            <div class="elSearch">
                <el-input placeholder="请输入内容" v-model="keyword" class="input-with-select">
                    <el-button slot="append" icon="el-icon-search" @click="searchPage"></el-button>
                </el-input>
            </div>
            <!-- 热门搜索 -->
            <div class="searchWords">
                热门搜索：&nbsp;
                <el-link type="primary" @click="keyword = '苹果'">苹果</el-link>&nbsp;&nbsp;&nbsp;
                <el-link type="primary" @click="keyword = '西瓜'">西瓜</el-link>&nbsp;&nbsp;&nbsp;
                <el-link type="primary" @click="keyword = '蜜桃'">蜜桃</el-link>
            </div>
            <!-- 分页订单列表 -->
            <li v-for="item in pageList" :key="item.orderId">
                <!-- 商品图片 -->
                <img :src="'orderImgs/' + item.picture" alt="">
                <!-- 商品信息 -->
                <div class="good">
                    <label class="title">{{ item.title }}&nbsp;
                        <span class="type">[{{ item.type == 'goods' ? '供' : '需' }}]</span></label>
                    <label class="content"><i class="el-icon-shopping-bag-1"></i>&nbsp;商品介绍：{{ item.content }}</label>
                    <label class="price"><i class="el-icon-shopping-cart-1"></i>&nbsp;￥{{ item.price.toFixed(2)
                    }}</label>
                    <label class="nameAndTime">
                        <i class="el-icon-user"></i>&nbsp;{{ item.ownName }}&nbsp;&nbsp;&nbsp;
                        <i class="el-icon-timer"></i>&nbsp;{{ item.createTime }}
                    </label>
                </div>
            </li>
            <!-- 假如查询内容不存在 -->
            <div class="alertInfo">
                <el-alert title="列表为空" type="warning" description="暂无数据，请添加或检查查询条件" show-icon
                    v-if="pageList.length === 0">
                </el-alert>
            </div>
        </div>
        <div class="right">
            <div>
                <el-button type="primary" class="btn">免费发布信息</el-button>
            </div>
            <div>
                <label>近期热门信息</label>
                <li v-for="(item, index) in contents" :key="index">
                    <el-divider></el-divider>
                    {{ index + 1 }}.&nbsp;{{ item }}
                </li>
            </div>
        </div>
    </div>
</template>

<script>
import { getPage } from "../apis/OrderService.js";
import { getContent } from "../apis/OrderService.js";
export default {
    data() {
        return {
            pageNum: 1,
            pageSize: 0,
            pageBean: null,
            pageList: [],
            keyword: '',
            contents: [],
        }
    },
    methods: {
        // 根据关键字分页查询
        async searchPage() {
            const result = await getPage(this.pageNum, this.pageSize, this.keyword);
            this.pageBean = result.data;
            this.pageList = result.data.items;
        }
    },
    async mounted() {
        // 初始进行无条件搜索
        this.pageSize = 7;
        const result = await getPage(1, this.pageSize, '');
        this.pageBean = result.data;
        this.pageList = result.data.items;
        // 获取全部评论
        this.contents = (await getContent()).data;
    }
}
</script>

<style lang="scss" scoped>
#home {
    width: 100%;
    height: 100%;
    display: flex;

    .left {
        width: 60%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        margin-left: 3%;

        .elSearch {
            margin-left: 7%;
            width: 62%;
        }

        .searchWords {
            display: flex;
            justify-content: flex-start;
            margin-top: 12px;
            margin-left: 8%;
            margin-bottom: 23px;
            font-size: 15px;
        }

        li {
            display: flex;
            height: 110px;
            justify-content: flex-start;
            margin-bottom: 15px;
            margin-left: 5%;
            list-style: none;
            border-radius: 10px;
            border: 0.5px solid gainsboro;

            img {
                width: 120px;
                height: 110px;
                border-radius: 10px;
            }

            .good {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                padding-top: 3px;
                padding-bottom: 3px;
                padding-left: 10px;

                label {
                    display: flex;
                    justify-content: flex-start;
                    align-items: center;
                }

                .title {
                    font-size: 18px;
                    font-weight: 595;
                }

                .type {
                    color: gray;
                    font-size: 14px;
                }

                .content {
                    font-size: 15px;
                }

                .price {
                    color: red;
                    font-size: 15px;
                }

                .nameAndTime {
                    color: gray;
                    font-size: 13px;
                }
            }
        }

        .alertInfo {
            // margin-top: 20px;
            margin-left: 5%;
        }

    }

    .right {
        width: 22%;
        height: 100vh;
        /* 设置固定高度为视口高度 */
        max-height: 78vh;
        /* 防止溢出 */
        border-radius: 10px;
        border: 0.5px solid gainsboro;
        margin-left: 7%;
        overflow-y: auto;
        /* 启用垂直滚动条（内容溢出时） */
        position: sticky;
        /* 可选：使右侧栏悬浮 */
        top: 0;
        /* 配合 sticky 使用 */

        .btn {
            margin: 20px;
        }

        div {
            label {
                font-weight: bold;
                font-size: 20px;
                display: block;
                /* 确保 label 独占一行 */
                padding: 0 20px;
                /* 添加内边距 */
            }

            li {
                text-align: left;
                list-style-type: none;
                margin: 10px 20px;
                /* 调整间距 */
                padding: 8px 0;
                /* 增加可点击区域 */
            }
        }
    }

    /* 自定义滚动条样式（仅支持 Chrome/Edge/Safari） */
    .right::-webkit-scrollbar {
        width: 6px;
    }

    .right::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 3px;
    }
}

.input-with-select .el-input-group__prepend {
    background-color: #fff;
}
</style>