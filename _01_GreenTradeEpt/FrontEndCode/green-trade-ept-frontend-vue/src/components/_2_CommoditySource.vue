<template>
    <div id="commoditySource">
        <div class="up">
            <!-- 搜索框 -->
            <div class="elSearch">
                <el-input placeholder="请输入内容" v-model="keyword" class="input-with-select">
                    <el-button slot="append" icon="el-icon-search" @click="search"></el-button>
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
        <div class="down">
            <el-table :data="tableData" stripe style="width: 100%" @row-click="showCommodityInfo">
                <el-table-column prop="content" label="供需内容" width="650" :formatter="contentFormatter">
                </el-table-column>
                <el-table-column prop="title" label="标题" width="250">
                </el-table-column>
                <el-table-column prop="createTime" label="发布时间" :formatter="createTimeFormatter">
                </el-table-column>
            </el-table>
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
import { getPageByType } from "../apis/OrderService.js";
import DateUtils from "../util/DateUtils.js"

export default {
    data() {
        return {
            pageNum: 1,
            pageSize: 0,
            type: '',// needs或goods
            pageBean: null,
            pageList: [],
            keyword: '',
            tableData: [],// element表单的数据
        }
    },
    methods: {
        // 根据关键字分页查询
        async search(isHandleCurrentChange) {
            if (isHandleCurrentChange != true) {// 若参数不为true，表示并非页数变化
                this.pageNum = 1;// 则置当前页数为1
            }
            const result = await getPageByType(this.pageNum, this.pageSize, this.keyword, this.type);
            this.pageBean = result.data;
            this.pageList = result.data.items;
            this.tableData = this.pageList;
        },

        // 格式化供需内容
        contentFormatter(row, column, cellValue, index) {
            let str = row.type == "needs" ? "求购" : "供应";
            return "[" + str + "]" + cellValue;
        },

        // 格式化日期
        createTimeFormatter(row, column, cellValue, index) {
            return DateUtils.formatDate(cellValue);
        },

        // 当页数改变
        handleCurrentChange(val) {// val为当前页
            this.pageNum = val;
            this.search(true);// 仅当页数改变，传入参数true
        },

        // 展示商品详细信息
        async showCommodityInfo(row, column, event) {
            this.$router.push({
                name: "commodityInfo",// 使用name切换页面，目标页面才能接收params参数！
                params: { good: JSON.stringify(row) } // 将对象转为字符串
            });
        },
    },

    async mounted() {
        // 初始进行无条件搜索
        this.pageSize = 10;
        this.type = "needs";
        const result = await getPageByType(1, this.pageSize, '', this.type);
        this.pageBean = result.data;
        this.pageList = result.data.items;
        this.tableData = this.pageList;
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
#commoditySource {
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
    }

    .block {
        margin-top: 20px;
    }
}
</style>