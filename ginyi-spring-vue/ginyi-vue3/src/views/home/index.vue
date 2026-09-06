<template>
    <div class="grid-container">
        <n-card class="card item1" :style="{backgroundColor: useHexToRgba(themeColor, 0.3)}">
            <div class="item1-main">
                <div class="item1-main-content">
                    <n-avatar round :size="105" :src="logo"/>
                    <div style="margin-left: 20px">
                        <h1 style="font-weight: 400; margin: 5px 0">下午好，欢迎登录！</h1>
                        <span>做点喜欢的事情，学点不一样的知识，挺好！</span>
                        <br>
                        <span>努力坚持吧！希望未来咱都能笑着直面生活的全部！</span>
                    </div>
                </div>
                <div class="item1-main-bg"/>
            </div>
        </n-card>
        <n-card class="card item2" title="一个小目标">
            <n-statistic tabular-nums>
                <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="100000000"/>
                <template #suffix>
                    元
                </template>
            </n-statistic>
        </n-card>
        <n-card class="card item3" title="您一共处理了">
            <n-statistic tabular-nums>
                <n-number-animation ref="numberAnimationInstRef" :from="0" :to="12039"/>
                <template #suffix>
                    条群消息
                </template>
            </n-statistic>
        </n-card>
        <n-card class="card item4" title="活动剩余时间">
            <n-statistic tabular-nums>
                <n-countdown ref="countdown" :duration="86400000" :active="true"/>
            </n-statistic>
        </n-card>
        <n-card class="card item5" title="微信步数">
            <n-statistic tabular-nums>
                <n-number-animation ref="numberAnimationInstRef" :from="0" :to="12039"/>
                <template #suffix>
                    步
                </template>
            </n-statistic>
        </n-card>
        <n-card class="card item6" title="好友点赞总数">
            <n-statistic tabular-nums>
                <n-number-animation ref="numberAnimationInstRef" :from="0" :to="154861"/>
                <template #suffix>
                    个
                </template>
            </n-statistic>
        </n-card>
        <n-card class="card item7" title="活跃用户">
            <n-statistic tabular-nums>
                <n-number-animation ref="numberAnimationInstRef" :from="0" :to="186411"/>
                <template #suffix>
                    位
                </template>
            </n-statistic>
        </n-card>
        <n-card class="card item8">
            <div class="item8-main">
                <div class="item8-main-content">
                    <n-statistic tabular-nums label="总销售额">
                        <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="1546721546"/>
                        <template #suffix>
                            元
                        </template>
                    </n-statistic>
                </div>
                <div class="item8-main-content">
                    <n-progress style="margin-right: 15px" type="dashboard" gap-position="bottom" :percentage="80"/>
                    <n-statistic tabular-nums label="当月销售额占比">
                        <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="14884614"/>
                        <template #suffix>
                            元
                        </template>
                    </n-statistic>
                </div>
                <div class="item8-main-content">
                    <n-progress
                        style="margin: 0 8px 12px 0"
                        type="circle"
                        status="success"
                        :percentage="95"
                    />
                    <n-statistic tabular-nums label="客户下单率">
                        <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="15984"/>
                        <template #suffix>
                            （单）
                        </template>
                    </n-statistic>
                </div>
                <div class="item8-main-content">
                    <n-progress
                        style="width: 120px; margin: 0 8px 12px 0"
                        type="multiple-circle"
                        :percentage="[70, 35]"
                    />
                    <n-statistic tabular-nums label="新用户数量和占比">
                        <n-number-animation ref="numberAnimationInstRef" show-separator :from="0" :to="22564"/>
                        <template #suffix>
                            （位）
                        </template>
                    </n-statistic>
                </div>
            </div>
        </n-card>
        <n-card class="card item9" title="不知道写点什么">
            <n-result status="404" title="这个位置留给你头脑风暴吧！" description="生活总归带点荒谬" />
        </n-card>
        <n-card class="card item10" title="留言板">
            <div class="item11-main">
                <div class="item10-main-content">
                    <n-avatar round size="medium" :src="logo" style="margin-right: 15px"/>
                    <div style="width: 100%; display: flex; justify-content: space-between">
                        <span>做得挺好！但是客户喜欢第一版~</span>
                        <span>2023-03-02</span>
                    </div>
                </div>
                <div class="item10-main-content">
                    <n-avatar round size="medium" :src="logo" style="margin-right: 15px"/>
                    <div style="width: 100%; display: flex; justify-content: space-between">
                        <span>可以可以！如果这个位置再完善下是不是更好些？</span>
                        <span>2023-03-01</span>
                    </div>
                </div>
                <div class="item10-main-content">
                    <n-avatar round size="medium" :src="logo" style="margin-right: 15px"/>
                    <div style="width: 100%; display: flex; justify-content: space-between">
                        <span>@全体成员 2023-02-29至2023-02-31，放假3天！3月1日正常上班。</span>
                        <span>2023-02-28</span>
                    </div>
                </div>
                <div class="item10-main-content">
                    <n-avatar round size="medium" :src="logo" style="margin-right: 15px"/>
                    <div style="width: 100%; display: flex; justify-content: space-between">
                        <span>有个新需求！根据用户手机壳颜色自动改变APP主题色。</span>
                        <span>2023-02-26</span>
                    </div>
                </div>
            </div>
        </n-card>
        <n-card class="card item11" title="好评率">
            <div class="item11-main">
                <div class="item11-main-content">
                    <n-avatar round size="medium" :src="logo" style="margin-right: 15px"/>
                    <n-progress type="line" status="error" :percentage="60" :indicator-placement="'inside'" processing/>
                </div>
                <div class="item11-main-content">
                    <n-avatar round size="medium" :src="logo" style="margin-right: 15px"/>
                    <n-progress type="line" :percentage="78" :indicator-placement="'inside'"
                                processing/>
                </div>
                <div class="item11-main-content">
                    <n-avatar round size="medium" :src="logo" style="margin-right: 15px"/>
                    <n-progress type="line" status="success" :percentage="98" :indicator-placement="'inside'"
                                processing/>
                </div>
                <div class="item11-main-content">
                    <n-avatar round size="medium" :src="logo" style="margin-right: 15px"/>
                    <n-progress type="line" status="warning" :percentage="88" :indicator-placement="'inside'"
                                processing/>
                </div>
            </div>
        </n-card>
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {storeToRefs} from "pinia";
import {useHexToRgba, useLighten} from "@/hooks/useColor";
import {setting} from "@/config/setting";

export default defineComponent({
    setup() {
        const {clientHeight, layoutFooterHeight, layoutHeaderHeight, themeColor} = storeToRefs(useSystemStore())
        const {logo} = setting;

        return {
            clientHeight, layoutFooterHeight, layoutHeaderHeight, themeColor,
            useLighten, useHexToRgba,
            logo
        }
    }
})
</script>

<style lang="less">

.grid-container {
    display: grid;
    height: 100%;
    width: 100%;

    .item1 {
        grid-area: aaa;
        min-height: 180px;
        padding: 0 !important;

        .n-card__content {
            padding: 0 !important;
        }

        &-main {
            display: flex;
            align-items: flex-end;
            height: 100%;

            &-content {
                width: 600px;
                height: 100%;
                min-height: 150px;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            &-bg {
                flex: 1;
                height: 90%;
                text-align: end;
                background-size: 100% 100%;
                background-repeat: no-repeat;
                background-image: url("../../../src/assets/home-bg.svg");
            }
        }
    }

    .item2 {
        grid-area: bbb;
        max-height: 400px;
    }

    .item3 {
        grid-area: ccc;
        max-height: 400px;
    }

    .item4 {
        grid-area: ddd;
        max-height: 400px;
    }

    .item5 {
        grid-area: eee;
        max-height: 400px;
    }

    .item6 {
        grid-area: fff;
        max-height: 400px;
    }

    .item7 {
        grid-area: ggg;
        max-height: 400px;
    }

    .item8 {
        grid-area: hhh;
        min-height: 150px;
        &-main {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: row;
            align-items: center;

            &-content {
                flex: 1;
                display: flex;
                align-items: center;
            }
        }
    }

    .item9 {
        grid-area: iii;
        width: 100%;
    }

    .item10 {
        grid-area: jjj;

        &-main {
            &-content {
                display: flex;
                align-items: center;
                margin-bottom: 15px;
            }
        }
    }

    .item11 {
        grid-area: kkk;

        &-main {
            &-content {
                display: flex;
                align-items: center;
                margin-bottom: 15px;
            }
        }
    }

    grid-template-areas:
                'aaa aaa aaa aaa aaa aaa'
                'bbb ccc ddd eee fff ggg'
                'hhh hhh hhh hhh hhh hhh'
                'iii iii jjj jjj kkk kkk';
    grid-gap: 10px;

}

</style>