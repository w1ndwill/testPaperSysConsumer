<template>
    <div ref="surroundGraph"></div>
</template>

<script>
// import resize from '@/mixins/resize';
import resize from '@/utils/resize';
import echarts from 'echarts';
const colorList = [
    '#28CAD8',
    '#E5A214',
    '#F69F73',
    '#ff7f50',
    '#87cefa',
    '#da70d6',
    '#32cd32',
    '#6495ed',
    '#ff69b4',
    '#cd5c5c',
    '#1e90ff',
    '#ff6347',
    '#7b68ee',
    '#6b8e23',
    '#4ea397',
    '#b8860b',
    '#7bd9a5'
];
export default {
    name: 'surroundGraph',    //关系图
    mixins: [resize],
    props:{
      data:Array
    },
    data() {
        return {
            // mydata: [
            //     {
            //         name: 'Vue',
            //         value: 85
            //     },
            //     {
            //         name: 'css',
            //         value: 55
            //     },
            //     {
            //         name: 'html',
            //         value: 35
            //     },
            //     {
            //         name: 'Java',
            //         value: 110
            //     },
            //     {
            //         name: 'element-ui',
            //         value: 85
            //     },
            //     {
            //         name: 'echarts',
            //         value: 75
            //     },
            //     {
            //         name: 'SpringBoot',
            //         value: 100
            //     },
            //     {
            //         name: 'MybatisPlus',
            //         value: 100
            //     },
            //     {
            //         name: 'MySQL',
            //         value: 90
            //     },
            //     {
            //         name: 'Redis',
            //         value: 60
            //     }
            // ]
        };
    },
    mounted() {
        this.$nextTick(() => {
            this.initEchart();
        });
    },
    methods: {
        initEchart() {
            this.myChart = echarts.init(this.$refs.surroundGraph);
            this.myChart.setOption(
                {
                    tooltip: {},
                    series: [
                        {
                            type: 'graph',
                            layout: 'force',
                            force: {
                                repulsion: 120,
                                edgeLength: [20, 70]
                            },
                            roam: true,
                            draggable: true,
                            symbolSize: (params) => {
                                return params;
                            },
                            itemStyle: {
                                normal: {
                                    shadowColor: 'rgba(133,203,247,0.75)',
                                    shadowBlur: 15,
                                    color: (params) => {
                                        return colorList[Math.floor(Math.random() * colorList.length)];
                                    }
                                }
                            },
                            label: {
                                normal: {
                                    show: true
                                }
                            },
                            data: this._props.data || this.mydata
                        }
                    ],

                    animationDurationUpdate: (index) => {
                        return index * 100;
                    },
                    animationEasingUpdate: 'bounceIn'
                },
                true
            );
        }
    }
};
</script>
