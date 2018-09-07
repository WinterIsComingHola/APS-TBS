<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>项目信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/admin.css" media="all">
    <style type="text/css">
        .layui-table-cell {
            height: auto;
            line-height: 28px;
            padding: 0 15px;
            position: relative;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: normal;
            box-sizing: border-box;
        }
        .layui-text{
            line-height: 50px;
            font-size: 14px;
            color: #666;
        }
    </style>
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
          <a href="${miscDomain}/aps/project/public?userAccount=${userAccount}">公共项目</a>
          <a><cite>我加入的项目</cite></a>
        </span>
        </div>

        <div class="layui-col-md12">
            <#if projects==0>
            <div class="layui-card">
                <div class="layui-card-header layui-text" style="font-size: 20px;">我加入的项目</div>
                <div class="layadmin-tips">
                    <i class="layui-icon" face>&#xe664;</i>
                    <div class="layui-text" style="font-size: 20px;">
                        你还没有加入任何项目
                    </div>
                </div>
            </div>
            <#elseif projects==1>
                <div class="layui-card">
                    <div class="layui-card-header layui-text" style="font-size: 20px;">我加入的项目</div>
                    <div class="layui-card-body">

                        <#--<div style="margin-bottom: 10px;">
                            <!--data-type属性为H5新增，可以在$中使用data(type)进行值的获取&ndash;&gt;
                            <button id="newp" class="layui-btn" data-type="newproject">新建项目</button>
                        </div>-->
                        <table class="layui-hide" id="my-project-table" lay-filter="project-table-event"></table>

                        <!--编写表格中的按钮模板-->
                        <script type="text/html" id="edit-project">
                            <a class="layui-btn layui-btn-xs" lay-event="oper">操作接口</a>
                            <#--<a class="layui-btn layui-btn-xs" lay-event="Invite">邀请成员</a>-->
                            <br/>
                            <a class="layui-btn layui-btn-xs" lay-event="query">查看成员</a>
                            <#--<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除项目</a>-->
                        </script>
                    </div>
                </div>
            <#else>

                <div class="layadmin-tips">
                    <i class="layui-icon" face>&#xe664;</i>
                    <div class="layui-text" style="font-size: 20px;">
                        好像出错了
                    </div>
                </div>


            </#if>
        </div>
    </div>
</div>
<#--<div id="hidden-account" hidden>${userAccount}</div>-->


<!--用于显示关联用户的弹层-->
<div hidden id="prouserrel">
    <table class="layui-hide" id="user-pro-table" lay-filter="user-pro-event"></table>
</div>

<script src="${miscDomain}/statics/layuiadmin/layui/layui.js"></script>
<script>
    layui.config({
        base: '${miscDomain}/statics/layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index', 'table','layer','jquery','laypage'], function(){
        var layer = layui.layer
                ,$ = layui.jquery
                ,table = layui.table
                ,laypage = layui.laypage;

        var userList = [];//定义空数组存储用户的选择数据
        var userUnique = [];//去重后的数组

        //渲染项目表格
        table.render({
            elem: '#my-project-table'
            ,url: '${mainDomain}aps/project/myprojects.action'//公开项目接口
            ,method:'post'
            ,cols: [[
                {field:'projectid', title: '项目id'}
                ,{field:'projectname', title: '项目名称'}
                ,{field:'tag', title: '标签'}
                ,{field:'desc', title: '描述'}
                ,{field:'oper', title: '操作',toolbar:'#edit-project'}
                /*,{field:'experience', width:80, title: '积分', sort: true}
                ,{field:'score', width:80, title: '评分', sort: true}
                ,{field:'classify', width:80, title: '职业'}
                ,{field:'wealth', width:135, title: '财富', sort: true}*/
            ]]
            ,where:{
                userAccount:"${userAccount}",
                userRole:0
                ,page:laypage.curr
                ,limit:laypage.limit
            }
            ,page: true
        });
        /*//渲染用户信息表格
        table.render({
            elem: '#user-info-table'
            ,url: 'aps/user/getallusers.action'//用户数据接口
            ,method:'post'
            ,cols:[[
                {type:'checkbox'}
                ,{field:'userid', title: '用户id'}
                ,{field:'username', title: '用户姓名'}
                ,{field:'userAccount', title: '工号'}
            ]]
            ,width:400
            ,page: true
        });*/
        //渲染项目下用户关系表格
        table.render({
            elem: '#user-pro-table'
            ,url: layui.setter.base + 'json/table/prouserinfo.js'//项目和用户关系接口
            ,method:'post'
            ,cols:[[
                {field:'userid', title: '用户id'}
                ,{field:'username', title: '用户姓名'}
                ,{field:'userAccount', title: '工号'}
            ]]
            ,width:400
            ,page: true
        });

        //监听表格的工具bar
        table.on('tool(project-table-event)',function(obj){
            var trdata = obj.data;//获得当前行数据
            var hiddendiv = $('#hidden-account');

            trdata.userAccount = hiddendiv.text();
            var layEvent = obj.event;//获得 模板中lay-event 对应的值
            if(layEvent==='oper'){//操作接口

                location.href = '${mainDomain}aps/interface/myinterface?projectid='+trdata.projectid+'&userAccount=${userAccount}&userRole=0&page=1&limit=10'; //跳转接口操作页面

            }else if(layEvent==='query'){

                layer.open(
                        {
                            type:"1"
                            ,area : '500px'
                            ,title:"已邀请用户信息"
                            ,content: $("#prouserrel")
                            ,btn: ['确定']
                            ,btnAlign: 'c'
                            ,success:function(childobj,index){
                            //每次成功打开碳层，便需要渲染用户信息表格
                            table.render({
                                elem: '#user-pro-table'
                                ,url: '${mainDomain}aps/user/getallusers.action'//用户数据接口
                                ,method:'post'
                                ,cols:[[
                                    {field:'userid', title: '用户id'}
                                    ,{field:'username', title: '用户姓名'}
                                    ,{field:'userAccount', title: '工号'}
                                ]]
                                ,width:400
                                ,page: true
                                ,where:{
                                    projectid:trdata.projectid,
                                    isInv:1
                                    ,page:laypage.curr
                                    ,limit:laypage.limit
                                }
                            });
                        }
                        });
            }
        });

    });
</script>
</body>
</html>