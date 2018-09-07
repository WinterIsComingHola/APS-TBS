<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>公共项目信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/admin.css" media="all">
</head>
<body>

<div class="layui-card layadmin-header">
    <div class="layui-breadcrumb" lay-filter="breadcrumb">
        <a lay-href="">主页</a>
        <a><cite>组件</cite></a>
        <a><cite>主页</cite></a>
        <a><cite>项目信息</cite></a>
    </div>
</div>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <span class="layui-breadcrumb" lay-separator="-">
                <a href="${miscDomain}/aps/project/public?userAccount=${userAccount}">首页</a>
                <a><cite>公共项目</cite></a>
            </span>
        </div>

        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">公共项目</div>
                <a href="${miscDomain}/aps/project/mypublic?userAccount=${userAccount}&userRole=1&page=1&limit=10" class="layui-btn">我创建的项目</a>
                <div class="layui-card-body">

                    <div style="margin-bottom: 10px;">
                        项目名称：
                        <div class="layui-inline">
                            <input class="layui-input" name="projectname" id="query-table-projectname"
                                   autocomplete="off">
                        </div>
                        创建人：
                        <div class="layui-inline">
                            <input class="layui-input" name="createuser" id="query-table-createuser" autocomplete="off">
                        </div>

                        <!--data-type属性为H5新增，可以在$中使用data(type)进行值的获取-->
                        <button class="layui-btn" data-type="reload">查询</button>
                        <div id="hidden-account" hidden>${userAccount}</div>
                    </div>

                    <table class="layui-hide" id="public-project-table" lay-filter="project-table-event"></table>

                    <!--编写表格中的按钮模板-->
                    <script type="text/html" id="join-project">
                        <a class="layui-btn layui-btn-xs" lay-event="join">加入项目</a>
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${miscDomain}/statics/layuiadmin/layui/layui.js"></script>
<script>
        layui.config({
            base: '${miscDomain}/statics/layuiadmin/' //静态资源所在路径
        }).extend({
            index: 'lib/index' //主入口模块
        }).use(['index', 'table', 'form', 'jquery','laypage'], function () {
            var admin = layui.admin
                    , $ = layui.jquery
                    , table = layui.table
                    , laypage = layui.laypage;

            var projectnameinput = $('#query-table-projectname');
            var createuserinput = $('#query-table-createuser');


            //渲染表格
            table.render({
                elem: '#public-project-table'
                , url: "${mainDomain}aps/project/getpub.action"
                , method: 'post'
                , cols: [[
                    {field: 'projectid', title: '项目id'}
                    , {field: 'projectname', title: '项目名称'}
                    , {field: 'createuser', title: '所属人'}
                    , {field: 'tag', title: '标签'}
                    , {field: 'desc', title: '描述'}
                    , {field: 'oper', title: '操作', toolbar: '#join-project'}
                    /*,{field:'experience', width:80, title: '积分', sort: true}
                    ,{field:'score', width:80, title: '评分', sort: true}
                    ,{field:'classify', width:80, title: '职业'}
                    ,{field:'wealth', width:135, title: '财富', sort: true}*/
                ]]
                , where: {
                    projectName: projectnameinput.val().trim()//获取到projectName元素的value属性的值
                    , createUser: createuserinput.val().trim()
                    , pageNum: laypage.curr
                    , pageSize: laypage.limit
                }
                , page: true

            });

            //监听表格的工具bar
            table.on('tool(project-table-event)', function (obj) {
                var trdata = obj.data;//获得当前行数据
                var hiddendiv = $('#hidden-account');

                trdata.userAccount = hiddendiv.text();
                trdata.isMaster = 0;//加入的项目，所以这个地方设置为0
                var layEvent = obj.event;//获得 模板中lay-event 对应的值
                if (layEvent === 'join') {

                    $.ajax(
                            {
                                url: "${mainDomain}aps/project/joinProject.action" //用户加入项目接口
                                , type: "post"
                                , dataType: "json"
                                , data: trdata
                                , success: function (resp) {

                                    if (resp.state == 208) {
                                        return layer.msg('您已经加入过该项目');
                                    }

                                    //加入项目的提示与跳转
                                    layer.msg('加入项目成功', {
                                        offset: '15px'
                                        , icon: 1
                                        , time: 1000
                                    }, function () {
                                        //location.href = '${mainDomain}aps/user/home'; //后台主页
                                    });
                                }
                            });

                }
            });

//定义active对象，用于表格重载
            var active = {

                reload: function () {
                    var projectnameinput = $('#query-table-projectname');
                    var createuserinput = $('#query-table-createuser');
                    /*if((projectName.val()=="")&&(createUser.val()=="")){
                        return layer.msg('查询条件不能都为空');
                    }*/

                    //执行表格重载
                    table.reload('public-project-table', {
                        url: '${mainDomain}aps/project/getpub.action'
                        , method: 'post'
                        , page: {
                            curr: 1 //重新从第 1 页开始
                        }
                        , where: {
                            projectName: projectnameinput.val().trim()//获取到projectName元素的value属性的值
                            , createUser: createuserinput.val().trim()
                            , pageNum: laypage.curr
                            , pageSize: laypage.limit
                        }
                    });

                }
        };

            //执行查询按钮点击事件
            $('.layui-btn').on('click', function () {
                //获取查询按钮元素中的data-type属性的值
                var type = $(this).data('type');
                //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
                active[type] ? active[type].call(this) : '';
            });

        });

</script>
</body>
</html>
