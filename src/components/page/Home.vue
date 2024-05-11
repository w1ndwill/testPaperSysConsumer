<template>
    <div>
        <el-row :gutter="20">
            <el-card shadow="hover" class="mgb20" style="margin: 10px">
                <el-row>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }" class="myCard1">
                            <div class="grid-content grid-con-1" @click="to('testPaper')">
                                <i class="el-icon-office-building grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">
                                        <count-to :startVal="0" :endVal="tpNo" :duration="2000" />
                                    </div>
                                    <div style="color:#fff">试卷数量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }" class="myCard2">
                            <div class="grid-content grid-con-2" @click="to('course')">
                                <i class="el-icon-lx-read grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">
                                        <div class="grid-num">
                                            <count-to :startVal="0" :endVal="courseNo" :duration="2000" />
                                        </div>
                                    </div>
                                    <div style="color:#fff">课程数量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }" class="myCard3">
                            <div class="grid-content grid-con-3" @click="to('question')">
                                <i class="el-icon-lx-text grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">
                                        <div class="grid-num">
                                            <count-to :startVal="0" :endVal="queNo" :duration="2000" />
                                        </div>
                                    </div>
                                    <div style="color:#fff">题目总数量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }" class="myCard4">
                            <div class="grid-content grid-con-4" @click="to('check')">
                                <i class="el-icon-lx-text grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">
                                        <div class="grid-num">
                                            <count-to :startVal="0" :endVal="paperNo" :duration="2000" />
                                        </div>
                                    </div>
                                    <div style="color:#fff">待审批数量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-card>
            <el-col :span="24">
                <el-card shadow="hover" style="height: 600px; text-align: center">
                    <div slot="header" class="clearfix">
                        <span>使用技术</span>
                    </div>
                    <surroundGraph style="width: 100%; height: 450px" :data="languageData" />
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import surroundGraph from './myComponents/echarts/surroundGraph';
import {getTpNo,getCourseNo,getQueNo,getPaperNo} from '@/api/index'
import countTo from 'vue-count-to';
export default {
    name: 'dashboard',
    components: {
        surroundGraph,
        countTo,
    },
    data() {
        return {
            name: localStorage.getItem('ms_username'),
            imgSrc: localStorage.getItem('photo'),
            tpNo:0,
            queNo:0,
            courseNo:0,
            paperNo:0,
            // todoList: [
            //     {
            //         title: '今天要出五张试卷',
            //         status: false
            //     },
            //
            //     {
            //         title: '完成试卷的审阅',
            //         status: false
            //     },
            //
            //     {
            //         title: '下班前完成试卷数据的处理~',
            //         status: true
            //     },
            //     {
            //         title: '今天要完成课程知识点的添加~',
            //         status: true
            //     }
            // ],
            languageData: [
                {
                    name: 'Vue',
                    value: 85
                },
                {
                    name: 'css',
                    value: 55
                },
                {
                    name: 'html',
                    value: 35
                },
                {
                    name: 'Java',
                    value: 110
                },
                {
                    name: 'element-ui',
                    value: 85
                },
                {
                    name: 'echarts',
                    value: 75
                },
                {
                    name: 'SpringBoot',
                    value: 100
                },
                {
                    name: 'MybatisPlus',
                    value: 100
                },
                {
                    name: 'MySQL',
                    value: 90
                },
                {
                    name: 'Redis',
                    value: 60
                }
            ]
        };
    },
    created(){
        this.init();
    },
    methods: {
       to(val){
           this.$router.push(val)
       },
       init(){
            getTpNo().then(res=>{
                if(res){
                    this.tpNo  = res
                }
           })
           getCourseNo().then(res=>{
                if(res){
                    this.courseNo  = res
                }
           })
           getQueNo().then(res=>{
                if(res){
                    this.queNo  = res
                }
           })
           getPaperNo().then(res=>{
                if(res){
                    this.paperNo  = res
                }
           })
       }
           
    },
    computed: {
        role() {
            return localStorage.getItem('isAdmin');
        },
        localSrc() {
            return localStorage.getItem('photo');
        }
    }
};
</script>


<style scoped>
.myCard1{
    width: 200px;
    border-radius: 20px;
    background-image:linear-gradient(#5771FD, #7D74FD, #A577FD, #C47AFE); 

}
.myCard2{
    width: 200px;
    border-radius: 20px;
    background-image:linear-gradient(#45D89A, #5EDA8B, #82DE74, #A1E162); 

}
.myCard3{
    width: 200px;
    border-radius: 20px;
    background-image:linear-gradient(#EB697B, #EC7A72, #EE8E67, #EF9961);

}

.myCard4{
    width: 200px;
    border-radius: 20px;
    background-image: linear-gradient(#6B5B95, #BC70A4, #BFD641, #F0B98D);
}

.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: #6672FD;
}

.grid-con-1 .grid-num {
    /* color: rgb(45, 140, 240); */
    color: white;
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: white;
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: white;
}

.grid-con-4 .grid-con-icon {
    background: rgba(119, 95, 142, 0.75);
}

.grid-con-3 .grid-num {
    color: white;
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}

.user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
}

.mgb20 {
    margin-bottom: 20px;
}

.todo-item {
    font-size: 14px;
}

.todo-item-del {
    text-decoration: line-through;
    color: #999;
}
</style>
