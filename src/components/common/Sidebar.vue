<template>
    <div class="sidebar">
        <!-- <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="collapse"
            background-color="rgb(7,196,168)"
            text-color="rbg(30,30,30)"
            active-text-color="rgb(45,140,240)"
            unique-opened
            router
        > -->
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="collapse"
            background-color="#324157"
            text-color="#bfcbd9"
            active-text-color="#20a0ff"
            unique-opened
            router
        >
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index" :key="item.index" :hidden="item.hidden">
                        <template slot="title">
                            <i :class="item.icon"></i>
                            <span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index" :hidden="subItem.hidden">
                                <template :hidden="subItem.hidden" slot="title">{{ subItem.title }}</template>
                                <el-menu-item
                                    v-for="(threeItem, i) in subItem.subs"
                                    :key="i"
                                    :index="threeItem.index"
                                    :hidden="threeItem.hidden"
                                    >{{ threeItem.title }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item
                                v-else
                                :hidden="subItem.hidden"
                                :index="subItem.index"
                                :key="subItem.index"
                                @click="subItem.title === '退出' ? loginOut : ''"
                                >{{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index" :hidden="item.hidden">
                        <i :class="item.icon"></i>
                        <span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
import bus from '../common/bus';
import { logOut } from '../../api/login';
export default {
    data() {
        return {
            collapse: false, //是否向左收起菜单栏
            items: [
                {
                    icon: 'el-icon-lx-home', //图标
                    index: 'home', //遇到/home是=时候就会去router/index.js下面去找路径
                    title: '系统首页'
                },
                {
                    icon: 'el-icon-lx-friend', //图标
                    index: 'user',
                    title: '用户管理',
                    hidden: this.getIsAdmin()
                },
                {
                    icon: 'el-icon-lx-read', //图标
                    index: 'course',
                    title: '课程管理'
                    // subs: [
                    //     {
                    //         icon: 'el-icon-lx-read', //图标
                    //         index: 'course',
                    //         title: '数据管理'
                    //     },

                    // ]
                },
                {
                    icon: 'el-icon-lx-file', //图标
                    index: 'st',
                    title: '试题管理',
                    subs: [
                        {
                            icon: 'el-icon-lx-text', //图标
                            index: 'questionBank',
                            title: '题库管理'
                        },
                        {
                            icon: 'el-icon-lx-read', //图标
                            index: 'question',
                            title: '题目管理'
                        }
                    ]
                },
                {
                    icon: 'el-icon-office-building', //图标
                    index: 'testPaper',
                    title: '试卷管理'
                },
                {
                    icon: 'el-icon-pie-chart', //图标
                    index: 'courseChart',
                    title: '课程分析'
                },
                {
                    icon: 'el-icon-lx-settings', //图标
                    index: 'setting',
                    title: '个人设置',
                    subs: [
                        {
                            icon: 'el-icon-lx-home', //图标
                            index: 'changePsw',
                            title: '修改密码'
                        },
                        {
                            icon: 'el-icon-lx-home', //图标
                            index: 'info',
                            title: '个人信息'
                        },
                        {
                            icon: 'el-icon-lx-roundclose',
                            // index: '',   //写上就会找不到loginOut页面直接404
                            title: '退出'
                        }
                    ]
                },
                {
                    icon: 'el-icon-lx-home', //图标
                    index: 'zl',
                    title: '例子',
                    hidden: true, //this.getIsAdmin(),
                    subs: [
                        {
                            icon: 'el-icon-lx-home', //图标
                            index: 'dashboard',
                            title: '系统首页'
                        },
                        {
                            icon: 'el-icon-lx-cascades',
                            index: 'table',
                            title: '基础表格'
                        },
                        {
                            icon: 'el-icon-lx-copy',
                            index: 'tabs',
                            title: 'tab选项卡'
                        },
                        {
                            icon: 'el-icon-lx-calendar',
                            index: '3',
                            title: '表单相关',
                            subs: [
                                {
                                    index: 'form',
                                    title: '基本表单'
                                },
                                // {
                                //     index: '3-2',
                                //     title: '三级菜单'
                                // },
                                {
                                    index: 'upload',
                                    title: '文件上传'
                                },
                                {
                                    index: 'editor',
                                    title: '富文本编辑器'
                                },
                                {
                                    index: 'markdown',
                                    title: 'markdown编辑器'
                                }
                            ]
                        },
                        {
                            icon: 'el-icon-lx-emoji',
                            index: 'icon',
                            title: '自定义图标'
                        },
                        {
                            icon: 'el-icon-pie-chart',
                            index: 'charts',
                            title: 'schart图表'
                        },
                        {
                            icon: 'el-icon-rank',
                            index: '6',
                            title: '拖拽组件',
                            subs: [
                                {
                                    index: 'drag',
                                    title: '拖拽列表'
                                },
                                {
                                    index: 'dialog',
                                    title: '拖拽弹框'
                                }
                            ]
                        },
                        {
                            icon: 'el-icon-lx-global',
                            index: 'i18n',
                            title: '国际化功能'
                        },
                        {
                            icon: 'el-icon-lx-warn',
                            index: '7',
                            title: '错误处理',
                            subs: [
                                {
                                    index: 'permission',
                                    title: '权限测试'
                                },
                                {
                                    index: '404',
                                    title: '404页面'
                                }
                            ]
                        },
                        {
                            icon: 'el-icon-lx-redpacket_fill',
                            index: '/donate',
                            title: '支持作者'
                        }
                    ]
                }
            ],
            param: {
                userId: localStorage.getItem('userId')
            }
        };
    },
    computed: {
        onRoutes() {
            return this.$route.path.replace('/', '');
        },
        loginOut() {
            this.$confirm('是否退出登录?', '确认信息', {
                distinguishCancelAndClose: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
                .then(() => {
                    logOut(this.param).then((res) => {});
                    localStorage.removeItem('ms_username');
                    this.$router.push('/login');
                    this.$message.warning('退出登录');
                })
                .catch((action) => {});
        }
    },
    methods: {
        getIsAdmin() {
            return localStorage.getItem('isAdmin') !== '管理员';
        }
    },
    created() {
        // 通过 Event Bus 进行组件间通信，来折叠侧边栏
        bus.$on('collapse', (msg) => {
            this.collapse = msg;
            bus.$emit('collapse-content', msg);
        });
    }
};
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
    width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
    /* 菜单栏大小 */
    width: 240px;
}
.sidebar > ul {
    height: 100%;
}
</style>
