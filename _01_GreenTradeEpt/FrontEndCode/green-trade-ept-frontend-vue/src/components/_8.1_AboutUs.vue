<template>
    <div class="about-container">
        <!-- 顶部品牌展示区 -->
        <div class="brand-header">
            <div class="logo-glow">
                <img src="../assets/logo.png" alt="融销通Logo" class="rotating-logo">
            </div>
            <h1 class="brand-title">融销通 <span class="brand-subtitle">农产品融销一体平台</span></h1>
        </div>

        <!-- 主体内容区 -->
        <div class="content-wrapper">
            <!-- 关于介绍卡片 -->
            <div class="about-card">
                <div class="card-decoration">
                    <div class="leaf left-leaf"></div>
                    <div class="leaf right-leaf"></div>
                </div>
                <h2 class="section-title">关于融销通</h2>
                <p class="lead-text">融销通是一款基于数字经济的农产品融销一体平台</p>

                <div class="feature-grid">
                    <!-- 特色亮点1 -->
                    <div class="feature-item">
                        <div class="feature-icon">🌱</div>
                        <p class="feature-text">从用户角度出发，支持农户进行产销购入等活动</p>
                    </div>

                    <!-- 特色亮点2 -->
                    <div class="feature-item">
                        <div class="feature-icon">🤝</div>
                        <p class="feature-text">农产品求购与货源同时展出，提高交易成功率</p>
                    </div>

                    <!-- 特色亮点3 -->
                    <div class="feature-item">
                        <div class="feature-icon">👨‍🌾</div>
                        <p class="feature-text">农业专家知识分享，线上答疑+线下预约服务</p>
                    </div>

                    <!-- 特色亮点4 -->
                    <div class="feature-item">
                        <div class="feature-icon">💰</div>
                        <p class="feature-text">智能匹配银行资源，简化农户融资流程</p>
                    </div>

                    <!-- 特色亮点5 -->
                    <div class="feature-item">
                        <div class="feature-icon">📊</div>
                        <p class="feature-text">实时市场行情分析，为农户提供精准价格参考</p>
                    </div>

                    <!-- 特色亮点6 -->
                    <div class="feature-item">
                        <div class="feature-icon">🚚</div>
                        <p class="feature-text">整合物流资源，提供农产品运输一站式解决方案</p>
                    </div>
                </div>
            </div>

            <!-- 详细描述区 -->
            <div class="description-section">
                <p class="description-text">
                    从用户的角度出发，能够很好的支持农户进行产销购入等活动：针对性性强，使用简便，
                    农户进行买卖商品，联系客户时，操作简单易于理解。将农产品求购与货源同时展出，
                    提高了成功率。长此以往将形成一个良性循环。支持农业专家进行知识分享，解惑答疑等工作，
                    农户不仅在网站平台上进行商品的买卖，如果遇到有关问题，可以线上与专家讨论，
                    搜索相关知识，也可以与专家线下预约，解决农业问题。
                </p>
                <p class="description-text">
                    同时，平台也提供了一个良好的银行与用户进行融资的环境，农户用户作为贷款很大的群体，
                    实际中却不尽人意，银行方面，难以获取实际需求用户，难以了解用户需求，农民方面，
                    文化水平有限或单方面排斥，不能很好的相关获取信息，利用银行资源。平台瞄准需求，
                    给银行推智能匹配客户，给农户开通融资申请接口，方便双方沟通，达到双赢。
                </p>
            </div>

            <!-- 联系信息区 -->
            <div class="contact-section">
                <h3 class="contact-title">联系我们</h3>
                <div class="company-card">
                    <div class="company-logo">ETLPAT</div>
                    <p class="company-info">etlpat科技有限公司</p>
                    <div class="social-links">
                        <a href="#" class="social-icon">📱</a>
                        <a href="#" class="social-icon">📧</a>
                        <a href="#" class="social-icon">🌐</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- 背景装饰元素 -->
        <div class="bg-elements">
            <div class="corners corner-tl"></div>
            <div class="corners corner-tr"></div>
            <div class="corners corner-bl"></div>
            <div class="corners corner-br"></div>
            <div class="floating-grains grain1">🌾</div>
            <div class="floating-grains grain2">🌾</div>
            <div class="floating-grains grain3">🌾</div>
        </div>
    </div>
</template>

<script>
export default {
    mounted() {
        // 添加滚动动画效果
        const animateOnScroll = () => {
            const elements = document.querySelectorAll('.feature-item, .description-text');
            elements.forEach(el => {
                const elementPosition = el.getBoundingClientRect().top;
                const screenPosition = window.innerHeight / 1.3;

                if (elementPosition < screenPosition) {
                    el.classList.add('appear');
                }
            });
        };

        window.addEventListener('scroll', animateOnScroll);
        animateOnScroll(); // 初始化执行一次
    }
}
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Serif+SC:wght@400;500;700&display=swap');

$primary-color: #4a8f29;
$secondary-color: #f8a726;
$text-color: #333;
$light-bg: #f9f9f7;
$card-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);

.about-container {
    font-family: 'Noto Serif SC', serif;
    color: $text-color;
    max-width: 970px;
    margin: 0 auto;
    padding: 2rem;
    position: relative;
    overflow: hidden;
    background: linear-gradient(135deg, #f5f7fa 0%, #f0f4f8 100%);
}

/* 品牌头部样式 */
.brand-header {
    text-align: center;
    margin-bottom: 3rem;
    position: relative;
    z-index: 2;

    .logo-glow {
        display: inline-block;
        padding: 1rem;
        background: white;
        border-radius: 50%;
        box-shadow: 0 0 30px rgba($primary-color, 0.3);
        margin-bottom: 1.5rem;
        transition: all 0.5s ease;

        &:hover {
            transform: scale(1.05);
            box-shadow: 0 0 40px rgba($primary-color, 0.4);
        }
    }

    .rotating-logo {
        border-radius: 50%;
        width: 120px;
        height: 120px;
        object-fit: contain;
        animation: float 6s ease-in-out infinite;
    }

    .brand-title {
        font-size: 2.5rem;
        font-weight: 700;
        color: $primary-color;
        margin: 0.5rem 0;
        text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
    }

    .brand-subtitle {
        display: block;
        font-size: 1.2rem;
        font-weight: 400;
        color: lighten($text-color, 20%);
        margin-top: 0.5rem;
    }
}

/* 内容容器 */
.content-wrapper {
    position: relative;
    z-index: 2;
}

/* 关于卡片样式 */
.about-card {
    background: white;
    border-radius: 20px;
    padding: 3rem;
    margin-bottom: 3rem;
    box-shadow: $card-shadow;
    position: relative;
    overflow: hidden;

    .card-decoration {
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;
        pointer-events: none;
        z-index: 0;

        .leaf {
            position: absolute;
            width: 150px;
            height: 150px;
            background-size: contain;
            background-repeat: no-repeat;
            opacity: 0.1;

            &.left-leaf {
                left: -50px;
                top: -50px;
                background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="%234a8f29" d="M416 127.1c0-35.38-28.62-64-64-64s-64 28.62-64 64c0 23.1 12.88 44.25 32 55.5v84.5c0 19.1 12.88 36.12 32 41.62v84.75c0 19.1 12.88 36.12 32 41.62v53.75c0 17.62 14.38 32 32 32s32-14.38 32-32v-53.75c19.12-5.5 32-22.52 32-41.62v-84.75c19.12-5.5 32-22.52 32-41.62v-84.5c19.12-11.25 32-32.4 32-55.5z"/></svg>');
            }

            &.right-leaf {
                right: -50px;
                bottom: -50px;
                background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="%23f8a726" d="M416 127.1c0-35.38-28.62-64-64-64s-64 28.62-64 64c0 23.1 12.88 44.25 32 55.5v84.5c0 19.1 12.88 36.12 32 41.62v84.75c0 19.1 12.88 36.12 32 41.62v53.75c0 17.62 14.38 32 32 32s32-14.38 32-32v-53.75c19.12-5.5 32-22.52 32-41.62v-84.75c19.12-5.5 32-22.52 32-41.62v-84.5c19.12-11.25 32-32.4 32-55.5z"/></svg>');
                transform: rotate(180deg);
            }
        }
    }

    .section-title {
        font-size: 2rem;
        color: $primary-color;
        margin-bottom: 1.5rem;
        position: relative;
        display: inline-block;

        &::after {
            content: '';
            position: absolute;
            bottom: -10px;
            left: 0;
            width: 50px;
            height: 3px;
            background: $secondary-color;
            border-radius: 3px;
        }
    }

    .lead-text {
        font-size: 1.2rem;
        color: lighten($text-color, 10%);
        margin-bottom: 2rem;
        font-weight: 500;
    }
}

/* 特色网格布局 */
.feature-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 2rem;
    margin-bottom: 3rem;

    .feature-item {
        background: $light-bg;
        border-radius: 15px;
        padding: 1.5rem;
        transition: all 0.3s ease;
        opacity: 0;
        transform: translateY(20px);
        transition: all 0.6s ease;

        &.appear {
            opacity: 1;
            transform: translateY(0);
        }

        &:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .feature-icon {
            font-size: 2.5rem;
            margin-bottom: 1rem;
        }

        .feature-text {
            font-size: 1rem;
            line-height: 1.6;
        }
    }
}

/* 描述文本区域 */
.description-section {
    background: white;
    border-radius: 20px;
    padding: 3rem;
    margin-bottom: 3rem;
    box-shadow: $card-shadow;

    .description-text {
        font-size: 1.1rem;
        line-height: 1.8;
        margin-bottom: 1.5rem;
        opacity: 0;
        transform: translateY(20px);
        transition: all 0.6s ease 0.2s;

        &.appear {
            opacity: 1;
            transform: translateY(0);
        }

        &:last-child {
            margin-bottom: 0;
        }
    }
}

/* 联系信息区域 */
.contact-section {
    text-align: center;

    .contact-title {
        font-size: 1.8rem;
        color: $primary-color;
        margin-bottom: 2rem;
        position: relative;

        &::after {
            content: '';
            position: absolute;
            bottom: -10px;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 3px;
            background: $secondary-color;
            border-radius: 3px;
        }
    }

    .company-card {
        display: inline-block;
        background: white;
        border-radius: 15px;
        padding: 2rem 3rem;
        box-shadow: $card-shadow;
        transition: all 0.3s ease;

        &:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
        }

        .company-logo {
            font-size: 2rem;
            font-weight: 700;
            color: $primary-color;
            margin-bottom: 0.5rem;
            letter-spacing: 2px;
        }

        .company-info {
            font-size: 1.1rem;
            color: lighten($text-color, 20%);
            margin-bottom: 1.5rem;
        }

        .social-links {
            display: flex;
            justify-content: center;
            gap: 1.5rem;

            .social-icon {
                font-size: 1.5rem;
                color: $primary-color;
                transition: all 0.3s ease;

                &:hover {
                    color: $secondary-color;
                    transform: scale(1.2);
                }
            }
        }
    }
}

/* 背景装饰元素 */
.bg-elements {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    pointer-events: none;
    z-index: 1;

    .corners {
        position: absolute;
        width: 200px;
        height: 200px;
        border: 15px solid rgba($primary-color, 0.05);

        &.corner-tl {
            top: 0;
            left: 0;
            border-right: none;
            border-bottom: none;
            border-radius: 20px 0 0 0;
        }

        &.corner-tr {
            top: 0;
            right: 0;
            border-left: none;
            border-bottom: none;
            border-radius: 0 20px 0 0;
        }

        &.corner-bl {
            bottom: 0;
            left: 0;
            border-right: none;
            border-top: none;
            border-radius: 0 0 0 20px;
        }

        &.corner-br {
            bottom: 0;
            right: 0;
            border-left: none;
            border-top: none;
            border-radius: 0 0 20px 0;
        }
    }

    .floating-grains {
        position: absolute;
        font-size: 3rem;
        opacity: 0.1;
        animation: float 6s ease-in-out infinite;

        &.grain1 {
            top: 20%;
            left: 10%;
            animation-delay: 0s;
        }

        &.grain2 {
            top: 60%;
            right: 15%;
            animation-delay: 2s;
        }

        &.grain3 {
            bottom: 10%;
            left: 20%;
            animation-delay: 4s;
        }
    }
}

/* 动画效果 */
@keyframes float {
    0% {
        transform: translateY(0) rotate(0deg);
    }

    50% {
        transform: translateY(-20px) rotate(5deg);
    }

    100% {
        transform: translateY(0) rotate(0deg);
    }
}

/* 响应式设计 */
@media (max-width: 768px) {
    .about-container {
        padding: 1rem;
    }

    .about-card,
    .description-section {
        padding: 1.5rem;
    }

    .feature-grid {
        grid-template-columns: 1fr;
    }

    .brand-title {
        font-size: 2rem !important;
    }

    .bg-elements .corners {
        width: 100px;
        height: 100px;
        border-width: 8px;
    }
}
</style>