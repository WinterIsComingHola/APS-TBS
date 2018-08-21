<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>首页 - APSMOCK</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/admin.css" media="all">

    <script>
        /^http(s*):\/\//.test(location.href) || alert('请先部署到 localhost 下再访问');
    </script>
</head>
<body class="layui-layout-body">

<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-header">
            <!-- 头部区域111 -->
            <ul class="layui-nav layui-layout-left">
                <li class="layui-nav-item layadmin-flexible" lay-unselect>
                    <a href="javascript:;" layadmin-event="flexible" title="侧边伸缩">
                        <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                    </a>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;" layadmin-event="refresh" title="刷新">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </a>
                </li>
            </ul>
            <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">

                <li class="layui-nav-item" lay-unselect>
                    <a lay-href="index.html" layadmin-event="message" lay-text="必看前情">
                        <i class="layui-icon layui-icon-notice"></i>

                        <span class="layui-badge-dot"></span>
                    </a>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;">
                        <cite>${userAccount}</cite>
                    </a>
                    <dl class="layui-nav-child">
                        <dd><a lay-href="set/user/info.html">基本资料</a></dd>
                        <dd><a lay-href="${miscDomain}/aps/user/pagereset?userAccount=${userAccount}">修改密码</a></dd>
                        <hr>
                        <dd><a href="${miscDomain}/aps/user/logout">退出</a></dd>
                    </dl>
                </li>

                <li class="layui-nav-item layui-show-xs-inline-block layui-hide-sm" lay-unselect>
                    <a href="javascript:;" layadmin-event="more"><i class="layui-icon layui-icon-more-vertical"></i></a>
                </li>
            </ul>
        </div>

        <!-- 侧边菜单 -->
        <div class="layui-side layui-side-menu">
            <div class="layui-side-scroll">
                <div class="layui-logo" lay-href="${miscDomain}">
                    <span>欢迎你，${userName}</span>
                </div>

                <ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu" lay-filter="layadmin-system-side-menu">
                    <li data-name="component" class="layui-nav-item">
                        <a href="javascript:;" lay-tips="接口操作" lay-direction="2">
                            <i class="layui-icon layui-icon-component"></i>
                            <cite>接口操作</cite>
                        </a>
                        <dl class="layui-nav-child">
                            <dd data-name="list"><a lay-href="${miscDomain}/aps/project/mypublic?userAccount=${userAccount}&userRole=1">我创建的项目</a></dd>
                            <dd data-name="list"><a lay-href="${miscDomain}/aps/project/mypublic?userAccount=${userAccount}&userRole=0">我加入的项目</a></dd>
                        </dl>
                    </li>
                    <li data-name="template" class="layui-nav-item">
                        <a href="javascript:;" lay-tips="数据库操作" lay-direction="2">
                            <i class="layui-icon layui-icon-template"></i>
                            <cite>还在开发</cite>
                        </a>
                        <dl class="layui-nav-child">
                            <dd><a lay-href="${miscDomain}/aps/user/tonewpage">卡壳中</a></dd>
                        </dl>
                    </li>
                    <li data-name="set" class="layui-nav-item">
                        <a href="javascript:;" lay-tips="设置" lay-direction="2">
                            <i class="layui-icon layui-icon-set"></i>
                            <cite>设置</cite>
                        </a>
                        <dl class="layui-nav-child">
                            <dd>
                                <a href="javascript:;">系统设置</a>
                                <dl class="layui-nav-child">
                                    <dd><a lay-href="">网站设置</a></dd>
                                    <dd><a lay-href="">通知设置</a></dd>
                                </dl>
                            </dd>
                            <dd>
                                <a href="javascript:;">我的设置</a>
                                <dl class="layui-nav-child">
                                    <dd><a lay-href="set/user/info.html">基本资料</a></dd>
                                    <dd><a lay-href="${miscDomain}/aps/user/pagereset?userAccount=${userAccount}">修改密码</a></dd>
                                </dl>
                            </dd>
                        </dl>
                    </li>
                </ul>
            </div>
        </div>

        <!-- 页面标签 -->
        <div class="layadmin-pagetabs" id="LAY_app_tabs">
            <div class="layui-icon layadmin-tabs-control layui-icon-prev" layadmin-event="leftPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-next" layadmin-event="rightPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-down">
                <ul class="layui-nav layadmin-tabs-select" lay-filter="layadmin-pagetabs-nav">
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:;"></a>
                        <dl class="layui-nav-child layui-anim-fadein">
                            <dd layadmin-event="closeThisTabs"><a href="javascript:;">关闭当前标签页</a></dd>
                            <dd layadmin-event="closeOtherTabs"><a href="javascript:;">关闭其它标签页</a></dd>
                            <dd layadmin-event="closeAllTabs"><a href="javascript:;">关闭全部标签页</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="layui-tab" lay-unauto lay-allowClose="true" lay-filter="layadmin-layout-tabs">
                <ul class="layui-tab-title" id="LAY_app_tabsheader">
                    <li lay-id="${miscDomain}" class="layui-this"><i class="layui-icon layui-icon-home"></i></li>
                </ul>
            </div>
        </div>

        <!--iframe设置区域-->
        <div class="layui-body" id="LAY_app_body">
            <div class="layadmin-tabsbody-item layui-show">
                <iframe src="${miscDomain}/aps/project/public?userAccount=${userAccount}" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
        </div>

        <!-- 辅助元素，一般用于移动设备下遮罩 -->
        <div class="layadmin-body-shade" layadmin-event="shade"></div>
    </div>
</div>

<script src="${miscDomain}/statics/layuiadmin/layui/layui.js"></script>
<script>
    layui.config({
        base: '${miscDomain}/statics/layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use('index');
</script>
</body>
</html>
