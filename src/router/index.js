import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            component: () => import('../components/page/userManage/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '/forgetPsw',
            component: () => import('../components/page/userManage/ReSetPassword.vue'),
            meta: { title: '重置密码' }
        },
        {
            path: '/register',
            component: () => import( '../components/page/userManage/Register.vue'),
            meta: { title: '注册' }
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/home',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Home.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/user',
                    component: () => import('../components/page/userManage/User.vue'),
                    meta: { title: '用户管理' }
                },
                {
                    path: '/course',
                    component: () => import('../components/page/CourseManage/Course.vue'),
                    meta: { title: '课程管理' }
                },
                {
                    path: '/courseChart',
                    component: () => import('../components/page/CourseManage/CourseChart.vue'),
                    meta: { title: '课程分析' }
                },
                {
                    path: '/changePsw',
                    component: () => import('../components/page/userManage/ChangePassword.vue'),
                    meta: { title: '修改密码' }
                },
                {
                    path: '/questionBank',
                    component: () => import('../components/page/queManage/QuestionBank.vue'),
                    meta: { title: '题库管理' }
                },
                {
                    path: '/question',
                    component: () => import('../components/page/queManage/Question.vue'),
                    meta: { title: '题目管理' }
                },
                {
                    path: '/testPaper',
                    component: () => import('../components/page/TestPaper.vue'),
                    meta: { title: '试卷管理' }
                },
                {
                    path: '/info',
                    component: () => import('../components/page/userManage/Info.vue'),
                    meta: { title: '个人信息' }
                },

                /* 下面是例子路径 */
                {
                    path: '/dashboard',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/examplePage/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/icon',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/examplePage/Icon.vue'),
                    meta: { title: '自定义图标' }
                },
                {
                    path: '/table',
                    component: () => import(/* webpackChunkName: "table" */ '../components/examplePage/BaseTable.vue'),
                    meta: { title: '基础表格' }
                },
                {
                    path: '/tabs',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/examplePage/Tabs.vue'),
                    meta: { title: 'tab选项卡' }
                },
                {
                    path: '/form',
                    component: () => import(/* webpackChunkName: "form" */ '../components/examplePage/BaseForm.vue'),
                    meta: { title: '基本表单' }
                },
                {
                    // 富文本编辑器组件
                    path: '/editor',
                    component: () => import(/* webpackChunkName: "editor" */ '../components/examplePage/VueEditor.vue'),
                    meta: { title: '富文本编辑器' }
                },
                {
                    // markdown组件
                    path: '/markdown',
                    component: () => import(/* webpackChunkName: "markdown" */ '../components/examplePage/Markdown.vue'),
                    meta: { title: 'markdown编辑器' }
                },
                {
                    // 图片上传组件
                    path: '/upload',
                    component: () => import(/* webpackChunkName: "upload" */ '../components/examplePage/Upload.vue'),
                    meta: { title: '文件上传' }
                },
                {
                    // vue-schart组件
                    path: '/charts',
                    component: () => import(/* webpackChunkName: "chart" */ '../components/examplePage/BaseCharts.vue'),
                    meta: { title: 'schart图表' }
                },
                {
                    // 拖拽列表组件
                    path: '/drag',
                    component: () => import(/* webpackChunkName: "drag" */ '../components/examplePage/DragList.vue'),
                    meta: { title: '拖拽列表' }
                },
                {
                    // 拖拽Dialog组件
                    path: '/dialog',
                    component: () => import(/* webpackChunkName: "dragdialog" */ '../components/examplePage/DragDialog.vue'),
                    meta: { title: '拖拽弹框' }
                },
                {
                    // 国际化组件
                    path: '/i18n',
                    component: () => import(/* webpackChunkName: "i18n" */ '../components/examplePage/I18n.vue'),
                    meta: { title: '国际化' }
                },
                {
                    // 权限页面
                    path: '/permission',
                    component: () => import(/* webpackChunkName: "permission" */ '../components/examplePage/Permission.vue'),
                    meta: { title: '权限测试', permission: true }
                },
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/examplePage/404.vue'),
                    meta: { title: '404' }
                },
                {
                    path: '/403',
                    component: () => import(/* webpackChunkName: "403" */ '../components/examplePage/403.vue'),
                    meta: { title: '403' }
                },
                {
                    path: '/key',
                    component: () => import(/* webpackChunkName: "donate" */ '../components/page/userManage/Key_Manage.vue'),
                    meta: { title: '密钥管理' }
                },
                {
                    path: '/check',
                    component: () => import(/* webpackChunkName: "donate" */ '../components/page/check.vue'),
                    meta: { title: '试卷审核' }
                },
            ]
        },
        {
            path: '*',
            redirect: '/404'
        },
    ]
});
