<template>
    <div id="purchaseDemand">
        <div class="up">
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
                <el-link type="primary" @click="keyword = '蜜桃'">蜜桃</el-link>&nbsp;&nbsp;&nbsp;
                <el-link type="primary" @click="keyword = '大米'">大米</el-link>&nbsp;&nbsp;&nbsp;
                <el-link type="primary" @click="keyword = '土豆'">土豆</el-link>
            </div>
        </div>
        <!-- 卡片 -->
        <div class="down">
            <el-row>
                <el-col :span="4" v-for="item in pageList" :key="item.orderId">
                    <div @click="showGoodsInfo(item)">
                        <el-card :body-style="{ padding: '10px' }" shadow="always">
                            <img :src="'orderImgs/' + item.picture" class="image">
                            <div class="font font1">[{{ item.ownName }}]</div>
                            <div class="font font2">{{ truncatedContent(item.content) }}</div>
                            <div class="font font3">￥{{ item.price.toFixed(2) }}</div>
                        </el-card>
                    </div>
                </el-col>
            </el-row>
        </div>
        <!-- 页码框 -->
        <div class="block">
            <el-pagination layout="prev, pager, next" :page-count="totalPages" :current-page="pageNum"
                @current-change="handleCurrentChange">
            </el-pagination>
        </div>
    </div>
</template>

<script>
import { getPage } from "../apis/OrderService.js";

export default {
    data() {
        return {
            pageNum: 1,
            pageSize: 0,
            pageBean: null,
            pageList: [],
            keyword: '',
        }
    },

    methods: {
        // 根据关键字分页查询
        async searchPage(isHandleCurrentChange) {
            if (isHandleCurrentChange != true) {// 若参数不为true，表示并非页数变化
                this.pageNum = 1;// 则置当前页数为1
            }
            const result = await getPage(this.pageNum, this.pageSize, this.keyword);
            this.pageBean = result.data;
            this.pageList = result.data.items;
        },

        // 将content限制在30个汉字
        truncatedContent(content) {
            const text = content || '';
            return text.length > 30 ? text.slice(0, 30) + '...' : text;
        },

        // 当页数改变
        handleCurrentChange(val) {// val为当前页
            this.pageNum = val;
            this.searchPage(true);// 仅当页数改变，传入参数true
        },

        // 展示商品详细信息
        async showGoodsInfo(good) {// 接收商品对象
            this.$router.push({
                name: "goodInfo",// 使用name切换页面，目标页面才能接收params参数！
                params: { good: JSON.stringify(good) } // 将对象转为字符串
            });
        },
    },

    async mounted() {
        // 初始进行无条件搜索
        this.pageSize = 18;
        const result = await getPage(1, this.pageSize, '');
        this.pageBean = result.data;
        this.pageList = result.data.items;
    },

    computed: {
        // 计算属性，用于获取总页数
        totalPages() {
            if (!this.pageBean || !this.pageBean.total || !this.pageSize) return 0;
            // 总页数 = (总项数 / 每页项数) 向上取整
            return Math.ceil(Number(this.pageBean.total) / Number(this.pageSize));
        }
    }
}
</script>

<style lang="scss" scoped>
#purchaseDemand {
    width: 80%;
    height: 100%;
    margin: 0 auto;
    display: flex;
    flex-direction: column;


    .up {
        width: 100%;
        height: 40px;
        display: flex;
        justify-content: flex-start;
        align-items: center;

        .elSearch {
            width: 35%;
        }

        .searchWords {
            display: flex;
            justify-content: flex-start;
            font-size: 15px;
            margin-left: 3%;
        }

    }

    .down {
        margin-top: 10px;

        .el-card {
            width: 180px;
            height: 280px;
            margin: 5px;
            border-radius: 5px;
            border: 0.5px solid gainsboro;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .image {
            width: 100%;
            aspect-ratio: 1/1;
            display: block;
            border-radius: 5px;
            margin-bottom: 5px;
        }

        .font {
            font-size: 13px;
            display: flex;
            justify-content: flex-start;
            text-align: left;
            margin-bottom: 2px;
        }

        .font1 {
            color: gray;
            font-weight: 550;
        }

        .font3 {
            color: red;
            font-weight: 580;
        }

        .clearfix:before,
        .clearfix:after {
            display: table;
            content: "";
        }

        .clearfix:after {
            clear: both
        }
    }

    .block {
        margin-top: 20px;
    }
}
</style>