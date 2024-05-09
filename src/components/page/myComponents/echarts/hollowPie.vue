<template>
    <div ref="hollowPie"></div>
</template>

<script>
import resize from '@/utils/resize';
import * as echarts from 'echarts';

export default {
    name: 'hollowPie',    // 饼状图
    mixins: [resize],
    props: {
        xData: Array,
        yData: [Object,Array],
        keys: Array,
        values: [Object,Array],
        type: {
            type: String,
            default: 'solid'
        },
        classification:{
            type:String,
            default:''
        }
    },
    data() {
        return {};
    },
    watch: {
        yData() {
            this.initEchart();
        },
        type(){
            this.initEchart();
        }
    },
    methods: {
        getColor() {
            let colorArr = [];
            try{
                (this._props.yData || this._props.values).color1.forEach((item) => {
                    colorArr.push(item);
                });
                return colorArr;
            }catch{
                return ['#4FD8bb', '#C15Faa',
                         '#FF5666', '#FFC977', 
                         '#767999', 'rgb(248,70,102)', 
                         '#FF5F55', '#FFC935', 
                         '#767BFB', 'rgb(248,70,102)'
                ];
            }
        },
        getYdata(){
            let i = 0;
            let objArr = [];
            let yData = this._props.yData.data || this._props.values.data;
             yData.forEach(item => {
                let obj =  new Object()
                    obj.value = item;
                    obj.name =  (this.keys|| this.xData)[i++];
                    objArr.push(obj);
                });
                return objArr;
        },
        initEchart() {
            this.myChart = echarts.init(this.$refs.hollowPie);
            this.myChart.clear();
            this.myChart.setOption(
                {
                    title:
                        this._props.type === 'solid'
                            ? []
                            : [
                                  {
                                      text: (this._props.yData || this._props.values).text || '',
                                      textStyle: {
                                          color: '#5171fd',
                                          fontSize: 25
                                      },
                                      top: 'center',
                                      left: 'center'
                                  }
                              ],
                    tooltip: {
                        trigger: 'item',
                        formatter: (params) => {
                            return params.marker + ' ' + params.name + '：' + params.value + this.classification+' (' + params.percent + '%)';
                        }
                    },
                    legend: {
                        show: true,
                        left: '15',
                        top: 0,
                        type: 'scroll',
                        itemWidth: 18,
                        itemHeight: 11,
                        data: this._props.xData || this._props.keys || []
                    },
                    color: this.getColor(),
                    series: [
                        {
                            data: this.getYdata() || [],
                            // name: '消费类-----型',
                            type: 'pie',
                            icon: 'circle',
                            center: ['50%', '50%'],
                            radius: this._props.type === 'solid' ? '55%' : ['30%', '55%'],
                            itemStyle: {
                                normal: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                }
                            },
                            label: {},
                            labelLine: {
                                normal: {
                                    show: true
                                }
                            }
                        }
                    ]
                },
                true
            );
        }
    }
};
</script>
