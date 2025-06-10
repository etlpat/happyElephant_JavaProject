<!-- 显示农业知识的信息 -->
<template>
    <div class="knowledge-container">
        <div id="knowledgeInfo" v-if="knowledge">
            <!-- 商品图片 -->
            <img :src="'knowledgeImgs/' + knowledge.picPath" alt="" class="knowledgeImg">
            <!-- 商品信息 -->
            <div class="right">
                <div class="up">
                    <label class="title">
                        {{ knowledge.title }}
                    </label>
                    <div class="content">
                        <label class="introduce"><i class="el-icon-collection"></i>知识：</label>
                        <label>{{ knowledge.content }}</label>
                    </div>
                </div>
                <div class="down">
                    <label class="own">
                        <i class="el-icon-user"></i>&nbsp;{{ knowledge.ownName }}
                    </label>
                    <label class="timer">
                        <i class="el-icon-timer"></i>&nbsp;发布日期:{{ $dateUtils.formatDate(knowledge.createTime)
                        }}&nbsp;&nbsp;&nbsp;
                        <i class="el-icon-time"></i>&nbsp;更新日期:{{ $dateUtils.formatDate(knowledge.updateTime) }}
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

        <!-- 新增评论区 -->
        <div class="comment-section" v-if="knowledge">
            <!-- 评论表单 -->
            <div class="comment-form">
                <el-input type="textarea" :rows="3" placeholder="请输入您的评论..." v-model="newComment">
                </el-input>
                <el-button type="primary" @click="submitComment" style="margin-top: 10px;">
                    发表评论
                </el-button>
            </div>
            <!-- 评论列表 -->
            <div class="comment-list">
                <div v-if="discussList.length === 0" class="no-comments">
                    暂无评论，快来发表第一条评论吧~
                </div>
                <div v-for="discuss in discussList" :key="discuss.discussId" class="comment-item">
                    <div class="comment-header">
                        <span class="comment-author"><i class="el-icon-user">&nbsp;</i>{{ discuss.ownName }}</span>
                        <span class="comment-time"><i class="el-icon-date"></i>&nbsp;{{
                            $dateUtils.formatDate(discuss.createTime) }}</span>
                    </div>
                    <div class="comment-content">{{ discuss.content }}</div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
import { getAllByKnowledgeId, save } from "../apis/DiscussService.js";

export default {
    data() {
        return {
            knowledge: null,// 该页面的商品对象
            discussList: [], // 存储评论列表
            newComment: '', // 存储新评论内容
        }
    },
    methods: {
        exit() {
            this.$router.back();// 返回上一页
        },

        // 加载评论
        async loadComments() {
            if (this.knowledge) {
                const response = await getAllByKnowledgeId(this.knowledge.knowledgeId);
                this.discussList = response.data || [];
            }
        },

        // 提交评论
        async submitComment() {
            if (!this.newComment.trim()) {
                this.$message.warning('评论内容不能为空');
                return;
            }
            const discuss = {// 创建discuss对象
                knowledgeId: this.knowledge.knowledgeId,
                ownName: this.$store.state.loginUsername,// 当前登录用户
                content: this.newComment,
            };
            await save(discuss);// 将disscuss对象插入数据库
            this.newComment = '';
            this.loadComments(); // 刷新评论列表
        }
    },

    mounted() {
        if (this.$route.params.knowledge) {
            this.knowledge = JSON.parse(this.$route.params.knowledge);// 接收传来的商品数据

            this.loadComments(); // 加载评论
        }
    }
}
</script>

<style lang="scss" scoped>
.knowledge-container {
    width: 75%;
    margin: 20px auto;
}

#knowledgeInfo {
    height: 100%;
    box-sizing: border-box;
    display: flex;
    border-radius: 10px;
    border: 0.5px solid gainsboro;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    background-color: rgba(0, 217, 255, 0.07);
    align-items: stretch;
    margin-bottom: 40px;

    .knowledgeImg {
        width: 280px;
        // height: 280px;
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
            margin-top: 10px;

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
                    margin-left: 58px;

                    .el-button {
                        margin-right: 5px;
                    }
                }
            }
        }
    }
}

/* 新增评论区样式 */
.comment-section {
    // margin-top: 30px;
    padding: 20px;
    border-radius: 10px;
    border: 0.5px solid gainsboro;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    background-color: white;

    max-height: 450px;
    /* 最大高度 */
    overflow-y: auto;
    /* 超出时显示垂直滚动条 */
    margin-top: 15px;
    /* 可选：与评论表单的间距 */
    padding-right: 5px;
    /* 可选：避免滚动条遮挡内容 */

    .comment-form {
        margin-bottom: 30px;
        width: 90%;
        margin: 0 auto;
    }

    .comment-list {
        width: 90%;
        margin: 0 auto;

        .no-comments {
            color: #999;
            text-align: center;
            padding: 20px;
        }

        .comment-item {
            padding: 15px 0;
            border-bottom: 1px solid #f0f0f0;

            &:last-child {
                border-bottom: none;
            }

            .comment-header {
                display: flex;
                justify-content: space-between;
                margin-bottom: 3px;
                margin-left: 10px;
                margin-right: 10px;

                .comment-author {
                    font-size: 13px;
                    color: #333;
                }

                .comment-time {
                    color: #999;
                    font-size: 13px;
                }
            }

            .comment-content {
                color: #333;
                font-size: 16px;
            }
        }
    }
}
</style>