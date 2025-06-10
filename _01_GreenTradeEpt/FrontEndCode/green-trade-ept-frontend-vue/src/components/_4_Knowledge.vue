<template>
    <div id="knowledge">
        <div class="up">
            <div class="elSearch">
                <el-input placeholder="请输入内容" v-model="keyword" class="input-with-select">
                    <el-button slot="append" icon="el-icon-search" @click="searchPage"></el-button>
                </el-input>
            </div>
            <div class="searchWords">
                热门搜索：&nbsp;
                <el-link type="primary" @click="keyword = '种植'">种植</el-link>&nbsp;&nbsp;&nbsp;
                <el-link type="primary" @click="keyword = '园艺'">园艺</el-link>&nbsp;&nbsp;&nbsp;
                <el-link type="primary" @click="keyword = 'zhangxukun'">zhangxukun</el-link>&nbsp;&nbsp;&nbsp;
                <el-link type="primary" @click="keyword = 'gaoge'">gaoge</el-link>
            </div>
        </div>

        <!-- 卡片 -->
        <div class="down">
            <el-row>
                <el-col :span="8" v-for="item in pageList" :key="item.knowledgeId">
                    <div @click="showKnowledgeInfo(item)">
                        <el-card :body-style="{ padding: '0px' }" shadow="always">
                            <img :src="'knowledgeImgs/' + item.picPath" class="image">
                            <div class="font font1">{{ item.title }}</div>
                            <div class="font font2">{{ truncatedContent(item.content) }}</div>
                            <div class="font font3">
                                <i class="el-icon-s-custom"></i>&nbsp;{{ item.ownName }}
                            </div>
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
import { getPage } from "../apis/KnowledgeService.js";

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

        // 将content限制在115个汉字
        truncatedContent(content) {
            const text = content || '';
            return text.length > 115 ? text.slice(0, 115) + '...' : text;
        },

        // 当页数改变
        handleCurrentChange(val) {// val为当前页
            this.pageNum = val;
            this.searchPage(true);// 仅当页数改变，传入参数true
        },

        // 展示知识详细信息
        async showKnowledgeInfo(knowledge) {
            this.$router.push({
                name: "knowledgeInfo",// 使用name切换页面，目标页面才能接收params参数！
                params: { knowledge: JSON.stringify(knowledge) } // 将对象转为字符串
            });
        },
    },

    async mounted() {
        // 初始进行无条件搜索
        this.pageSize = 3;
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
#knowledge {
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
            height: 540px;
            margin: 10px;
            border-radius: 5px;
            border: 0.5px solid gainsboro;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .image {
            width: 100%;
            aspect-ratio: 1/0.8;
            display: block;
            border-radius: 5px;
            margin-bottom: 5px;
        }

        .font {
            font-size: 16px;
            display: flex;
            justify-content: flex-start;
            text-align: left;
            margin-left: 15px;
            margin-right: 15px;
            align-items: center;
        }

        .font1 {
            margin-top: 10px;
            font-size: 20px;
            font-weight: bold;
        }

        .font2 {
            margin-top: 8px;
        }

        .font3 {
            margin-top: 8px;
            color: rgb(167, 164, 164);
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
        margin-top: 15px;
    }
}
</style>