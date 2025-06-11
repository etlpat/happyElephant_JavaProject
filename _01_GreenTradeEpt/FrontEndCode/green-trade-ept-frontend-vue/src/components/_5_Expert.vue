<template>
    <div id="expert-container">
        <div class="left-section">
            <div class="search-area">
                <!-- 搜索框 -->
                <div class="search-box">
                    <el-input placeholder="请输入搜索内容" v-model="keyword" class="search-input" @keyup.enter.native="search">
                        <el-button slot="append" icon="el-icon-search" @click="search">
                        </el-button>
                    </el-input>
                </div>
                <!-- 热门搜索选项 -->
                <div class="hot-search">
                    热门搜索：
                    <el-link type="primary" @click="keyword = '玉米苗灌溉'">玉米苗灌溉</el-link>
                    <el-link type="primary" @click="keyword = '苹果树'">苹果树</el-link>
                    <el-link type="primary" @click="keyword = 'gaoge'">gaoge</el-link>
                </div>
            </div>
            <!-- 左侧问题表 -->
            <div class="question-table">
                <el-table :data="tableData" stripe style="width: 100%" @row-click="showQuestionDetail"
                    :row-style="{ height: '75px' }">
                    <el-table-column prop="title" label="问题" :formatter="titleFormatter">
                    </el-table-column>
                    <el-table-column prop="questioner" label="提问者" width="180">
                    </el-table-column>
                    <el-table-column prop="expertName" label="专家" width="180">
                    </el-table-column>
                </el-table>
            </div>
            <!-- 分页框 -->
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :page-count="totalPages" :current-page="pageNum"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </div>
        </div>

        <div class="right-section">
            <div class="action-buttons">
                <el-button type="primary" icon="el-icon-chat-dot-round" class="action-btn">在线问答</el-button>
                <el-button type="primary" icon="el-icon-date" class="action-btn">专家预约</el-button>
            </div>
            <!-- 超链接，跳转到专家详情表 -->
            <div class="expert-header">
                <h3 class="section-title">专家列表</h3>
                <el-link type="primary" class="more-link" @click="moreExpert">查看更多专家 >></el-link>
            </div>
            <!-- 右侧专家列表 -->
            <div class="expert-list">
                <div class="expert-card" v-for="item in expertList" :key="item.userName">
                    <div class="expert-main">
                        <div class="expert-avatar">
                            <img src="else/expert.png" alt="专家头像">
                        </div>
                        <div class="expert-info-container">
                            <div class="expert-details">
                                <div class="detail-item">
                                    <span class="detail-label">专家姓名：</span>
                                    <span class="detail-value">{{ item.realName }}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">职称：</span>
                                    <span class="detail-value">{{ item.position }}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">专业：</span>
                                    <span class="detail-value">{{ item.profession }}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">电话：</span>
                                    <span class="detail-value">{{ item.phone }}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">单位：</span>
                                    <span class="detail-value">{{ item.belong }}</span>
                                </div>
                            </div>
                            <div class="expert-actions">
                                <el-button type="success" size="small" plain class="action-button"
                                    @click="askAQuestion(item)">提问</el-button>
                                <el-button type="success" size="small" plain class="action-button"
                                    @click="reserve(item)">预约</el-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { getPage } from "../apis/QuestionService.js";
import { getPageAll } from "../apis/ExpertService.js";

export default {
    data() {
        return {
            // 问题页的数据
            pageNum: 1,
            pageSize: 0,
            pageBean: null,
            pageList: [],
            keyword: '',
            tableData: [],// element表单的数据

            // 专家表
            expertList: [],
        }
    },

    methods: {
        // 根据关键字分页查询
        async search(isHandleCurrentChange) {
            if (isHandleCurrentChange != true) {// 若参数不为true，表示并非页数变化
                this.pageNum = 1;// 则置当前页数为1
            }
            const result = await getPage(this.pageNum, this.pageSize, this.keyword);
            this.pageBean = result.data;
            this.pageList = result.data.items;
            this.tableData = this.pageList;
        },


        // 当页数改变
        handleCurrentChange(val) {// val为当前页
            this.pageNum = val;
            this.search(true);// 仅当页数改变，传入参数true
        },

        // 格式化问题
        titleFormatter(row, column, cellValue, index) {
            return "[问]" + cellValue;
        },

        // 跳转到专家详情页
        moreExpert() {
            this.$router.push({
                name: "moreExpert",
            });
        },

        // 跳转到问题详情页
        showQuestionDetail() {
        },

        // 提问
        async askAQuestion(expert) {
            this.$router.push({
                name: "askAQuestion",// 使用name切换页面，目标页面才能接收params参数！
                params: { expert: JSON.stringify(expert) } // 将对象转为字符串
            });
        },

        // 预约
        async reserve(expert) {
            this.$router.push({
                name: "reserve",// 使用name切换页面，目标页面才能接收params参数！
                params: { expert: JSON.stringify(expert) } // 将对象转为字符串
            });
        },
    },

    async mounted() {
        // 获取问题列表
        this.pageSize = 5;
        const result = await getPage(1, this.pageSize, '');
        this.pageBean = result.data;
        this.pageList = result.data.items;
        this.tableData = this.pageList;

        // 获取专家列表
        this.expertList = (await getPageAll(1, 2)).data.items;
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
#expert-container {
    width: 90%;
    margin: 20px auto;
    display: flex;
    gap: 20px;
    height: 600px;

    .left-section {
        flex: 0 0 60%;
        background: #fff;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

        .search-area {
            display: flex;
            align-items: center;
            margin-bottom: 20px;

            .search-box {
                flex: 0 0 60%;

                .search-input {
                    border-radius: 20px;

                    .el-input-group__append {
                        background-color: #409EFF;
                        color: white;
                        border-top-right-radius: 20px;
                        border-bottom-right-radius: 20px;
                    }
                }
            }

            .hot-search {
                flex: 1;
                margin-left: 20px;
                font-size: 14px;
                color: #666;

                .el-link {
                    margin: 0 8px;
                }
            }
        }

        .question-table {
            margin-bottom: 20px;

            .el-table {
                border-radius: 8px;
                overflow: hidden;

                &::before {
                    display: none;
                }

                th {
                    background-color: #f5f7fa;
                    color: #333;
                    font-weight: 600;
                }
            }
        }

        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;

            .el-pagination {
                padding: 10px 0;
            }
        }
    }

    .right-section {
        flex: 1;
        background: #fff;
        border-radius: 8px;
        padding: 18px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        min-width: 350px;
        /* 确保有足够宽度 */

        .expert-header {
            .section-title {
                margin-bottom: 10px;
            }

            .more-link {
                margin-bottom: 15px;
            }
        }

        .expert-list {
            display: flex;
            flex-direction: column;
            gap: 20px;

            .expert-card {
                background: #f9f9f9;
                border-radius: 8px;
                padding: 15px;
                transition: all 0.3s ease;

                &:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                }

                .expert-main {
                    display: flex;
                    gap: 15px;
                }

                .expert-avatar {
                    flex-shrink: 0;

                    img {
                        width: 80px;
                        height: 80px;
                        border-radius: 50%;
                        object-fit: cover;
                        border: 2px solid #e1f5fe;
                    }
                }

                .expert-info-container {
                    flex: 1;
                    display: flex;
                    flex-direction: column;
                    min-width: 0;
                    /* 防止flex项目溢出 */
                }

                .expert-details {
                    display: grid;
                    grid-template-columns: repeat(2, 1fr);
                    gap: 12px 15px;
                    margin-bottom: 10px;

                    .detail-item {
                        display: flex;
                        min-width: 0;
                        /* 防止文本溢出 */
                        white-space: nowrap;
                        /* 防止换行 */

                        .detail-label {
                            color: #666;
                            font-size: 14px;
                            margin-right: 5px;
                            flex-shrink: 0;
                            /* 防止标签被压缩 */
                        }

                        .detail-value {
                            color: #333;
                            font-weight: 500;
                            font-size: 14px;
                            overflow: hidden;
                            text-overflow: ellipsis;
                        }
                    }
                }

                .expert-actions {
                    display: flex;
                    gap: 10px;
                    justify-content: flex-end;

                    .action-button {
                        width: 80px;
                    }
                }
            }
        }
    }
}

@media (max-width: 1200px) {
    .right-section {
        .expert-list {
            .expert-card {
                .expert-details {
                    grid-template-columns: 1fr;
                    /* 小屏幕时改为单列 */
                }
            }
        }
    }
}
</style>