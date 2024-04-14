<template>
    <div>
        <div class="container">
            <div>
                <el-radio-group v-model="chart" size="medium" @change="chartChange">
                        <el-radio-button label="hollowPie1" >试卷分析</el-radio-button>
                        <el-radio-button label="hollowPie2" >题库分析</el-radio-button>
                        <el-radio-button label="moreBar">题目分析</el-radio-button>
                        <!-- <el-radio-button label="liquidChart">球</el-radio-button> -->
                </el-radio-group>
            </div>
             <div class="schart-box"  v-if="chart === 'hollowPie1'">
                <div class="content-title">试卷分析图</div>
                <hollowPie ref="hollowPie1" 
                    style="width: 800px; height: 400px" 
                    :xData="tpXdata" 
                    :yData="tpYdata" 
                    :classification="'张试卷'"
                />
            </div>
            <div class="schart-box" v-if="chart === 'hollowPie2'">
                <div class="content-title">题库分析图</div>
                <hollowPie ref="hollowPie2" 
                    style="width: 800px; height: 400px" 
                    :xData="queBankXdata" 
                    :yData="queBankYdata" 
                    :type="'/'" 
                    :classification="'个题库'"
                />
            </div>
            <!-- <div class="schart-box"  v-if="chart === 'moreLine'">
                <div class="content-title">折线图测试</div>
                <moreLine ref="moreLine" style="width: 1150px; height: 400px" :xData="xddd" :yData="yddd"  />
            </div> -->
            <div class="schart-box"  v-if="chart === 'moreBar'">
                <div class="content-title">课程分析图</div>
                <moreBar  ref="moreBar" style="width: 1000px; height: 400px" :xData="xData" :yData="yData" :colWidth="20" />
            </div>
            <!-- <div class="schart-box"  v-if="chart === 'liquidChart'">
                <div class="content-title">球</div>
                <liquid-chart ref="liquidChart" class="schart" />
            </div> -->
            <!-- <div class="schart-box">
                <div class="content-title">测试</div>
                <gradients-bar class="schart" />
            </div>
            <div class="schart-box">
                <div class="content-title">球</div>
                <liquid-chart class="schart" />
            </div> -->
        </div>
    </div>
</template>

<script>
// import gradientsBar from './char/gradientsBar';
// import liquidChart from './char/liquidChart';
import moreBar from '../myComponents/echarts/moreBar';
import moreLine from '../myComponents/echarts/moreLine';
import hollowPie from '../myComponents/echarts/hollowPie';
import { query as getAnalysis,getQueBankList } from '@/api/CourseAnalysis';
export default {
    name: 'basecharts',
    components: {
        // gradientsBar,
        // liquidChart,
        moreBar,
        moreLine,
        hollowPie
    },
    data() {
        return {
            chart: 'hollowPie1',
            xData: [],
            yData: [],
            queBankXdata:[],
            queBankYdata:[],
            tpXdata:[],
            tpYdata:[]
        }
    },
    created() {
        this.getData();
    },
    methods: {
        chartChange(val){
            this.$refs[val].initEchart();
        },
        getData() {
            getAnalysis().then((res) => {
                this.xData = res.xdata;
                this.yData = res.ydataList;
            });
            getQueBankList().then(res=>{
                this.queBankXdata = res.xdata;
                this.queBankYdata = res.ydataList[0];
                this.tpXdata = res.xdata;
                this.tpYdata = res.ydataList[1];
             })
        }
    }
};
</script>

<style scoped>
.schart-box {
    display: inline-block;
    margin: 20px;
}
.schart {
    width: 400px;
    height: 400px;
}
.content-title {
    clear: both;
    font-weight: 400;
    line-height: 50px;
    font-size: 22px;
    color: #1f2f3d;
    position: relative;
    float: left;
    left: 300px;
    top:-65px;

}
</style>