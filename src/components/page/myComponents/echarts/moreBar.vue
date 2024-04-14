<template>
    <div ref="moreBar"></div>
</template>

<script>
// import resize from '@/mixins/resize';
import resize from '@/utils/resize';
import echarts from 'echarts';
export default {
    name: 'moreBar',   // 柱状图
    mixins: [resize],
    props: {
        xData: Array,
        yData: Array,
        colWidth: {
            type: Number,
            default: 10
        },
        radius: {
            type: Number,
            default: 5
        }
    },
    data() {
        return {};
    },
    watch: {
        xData() {
            this.initEchart();
        }
    },
    methods: {
        getSeries() {
            let seriesArr = [];
            this._props.yData.forEach((item) => {
                var obj = new Object();
                obj.name = item.name;
                obj.type = 'bar';
                obj.barGap = '20%';
                obj.data = item.data;
                obj.barWidth = item.barWidth || this._props.colWidth || '10';
                obj.itemStyle = {
                    normal: {
                        barBorderRadius: item.radius || this._props.radius || '10',
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            {
                                offset: 0,
                                color: item.color1 || '#5171fd'
                            },
                            {
                                offset: 1,
                                color: item.color2 || '#c97afd'
                            }
                        ]),
                        label: {
                            show: true, //开启显示
                            position: 'top', //在上方显示
                            textStyle: {
                                //数值样式
                                color: 'black',
                                fontSize: 16
                            }
                        }
                    }
                };
                seriesArr.push(obj);
            });
            return seriesArr;
        },
        initEchart() {
            this.myChart = echarts.init(this.$refs.moreBar);
            this.myChart.clear();
            this.myChart.setOption(
                {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        },
                        padding: [5, 10]
                    },
                    grid: {
                        left: 50,
                        right: 20,
                        bottom: 40,
                        top: 30
                    },

                    //给x轴设置滚动条
                    dataZoom: [
                        {
                            start: 0, //默认为0
                            end: 100 - this._props.xData.length, //默认为100
                            type: 'slider',
                            show: this._props.xData.length > 10, //当x轴数据大于10自动打开滑动条
                            xAxisIndex: [0],
                            handleSize: 2, //滑动条的 左右2个滑动条的大小
                            height: 20, //组件高度
                            left: '50%', //左边的距离
                            right: '10%', //右边的距离
                            bottom: 0, //右边的距离
                            borderColor: '#000',
                            fillerColor: '#269cdb',
                            borderRadius: 10,
                            backgroundColor: '#33384b', //两边未选中的滑动条区域的颜色
                            showDataShadow: false, //是否显示数据阴影 默认auto
                            showDetail: false, //即拖拽时候是否显示详细数值信息 默认true
                            realtime: true, //是否实时更新
                            filterMode: 'filter'
                        },
                        //下面这个属性是里面拖到
                        {
                            type: 'inside',
                            show: true,
                            xAxisIndex: [0],
                            start: 0, //默认为1
                            end: 100 - 1500 / 31 //默认为100
                        }
                    ],
                    legend: {
                        show: true,
                        itemWidth: 16,
                        itemHeight: 12
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: this._props.xData || [],
                            axisLine: {
                                lineStyle: {
                                    color: '#cecece'
                                }
                            },
                            axisLabel: {
                                color: '#666'
                            },
                            axisTick: {
                                show: false
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisTick: {
                                show: false
                            },
                            axisLine: {
                                lineStyle: {
                                    color: '#cecece'
                                }
                            },
                            axisLabel: {
                                color: '#666'
                            },
                            splitLine: {
                                show: true
                            }
                        }
                    ],
                    series: this.getSeries() || []
                },
                true
            );
        }
    }
};
</script>
