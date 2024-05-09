<template>
    <div ref="moreLine"></div>
</template>

<script>
import resize from '@/utils/resize';
import * as echarts from 'echarts';
export default {
    name: 'moreLine',   // 折线图
    mixins: [resize],
    props: {
        xData: Array,
        yData: Array
    },
    data() {
        return {};
    },
    watch: {
        yData() {
            this.initEchart();
        }
    },
    methods: {
        getArrMax(arr) {
            let max = arr[0];
            let index = 0;
            arr.forEach((item, i) => {
                if (item > max) {
                    max = item;
                    index = i;
                }
            });
            let map = new Map();
            map.set('max', max);
            map.set('index', index);
            return map;
        },
        getSeries() {
            let seriesArr = [];
            this._props.yData.forEach((item) => {
                const obj = new Object();
                obj.name = item.name;
                obj.type = 'line';
                obj.data = item.data;
                obj.smooth = true;
                obj.symbol = 'circle';
                obj.showSymbol = true;
                let maxMap = this.getArrMax(item.data);
                obj.markPoint = {
                    data: [
                        {
                            maxName: item.maxName || '',
                            value: maxMap.get('max'),
                            xAxis: maxMap.get('index'),
                            yAxis: maxMap.get('max'),
                            symbolSize: 40
                        }
                    ]
                };
                obj.lineStyle = {
                    normal: {
                        width: 4,
                        color: item.color1 || '#F6D06F', // 线条颜色
                        shadowColor: 'rgba(246,208,111, 0.85)',
                        shadowBlur: 10,
                        shadowOffsetY: 6
                    }
                };
                seriesArr.push(obj);
            });
            return seriesArr;
        },
        initEchart() {
            this.myChart = echarts.init(this.$refs.moreLine);
            this.myChart.clear();
            this.myChart.setOption(
                {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        }
                    },
                    grid: {
                        left: 50,
                        right: 20,
                        bottom: 40,
                        top: 65
                    },
                    legend: {
                        show: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: this._props.xData || ['10-1', '10-2', '10-3', '10-4', '10-5', '10-6', '10-7'],
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
                            },
                            axisPointer: {
                                snap: true
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
                                show: false
                            },
                            axisPointer: {
                                snap: true
                            }
                        }
                    ],
                    series: this.getSeries()
                },
                true
            );
        }
    }
};
</script>
