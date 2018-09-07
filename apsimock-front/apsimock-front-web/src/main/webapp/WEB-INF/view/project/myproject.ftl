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
          <a><cite>我创建的项目</cite></a>
        </span>
        </div>

        <div class="layui-col-md12">
            <#if projects==0>
            <div class="layui-card">
                <div class="layui-card-header layui-text" style="font-size: 20px;">我创建的项目</div>
                <div class="layadmin-tips">
                    <i class="layui-icon" face>&#xe664;</i>
                    <div class="layui-text" style="font-size: 20px;">
                        你还没有创建项目
                    </div>
                    <button id="newp" class="layui-btn" data-type="newprojectnorecord">新建项目</button>
                </div>
            </div>
            <#elseif projects==1>
                <div class="layui-card">
                    <div class="layui-card-header layui-text" style="font-size: 20px;">我创建的项目</div>
                    <div class="layui-card-body">

                        <div style="margin-bottom: 10px;">
                            <!--data-type属性为H5新增，可以在$中使用data(type)进行值的获取-->
                            <button id="newp" class="layui-btn" data-type="newproject">新建项目</button>
                        </div>
                        <table class="layui-hide" id="my-project-table" lay-filter="project-table-event"></table>

                        <!--编写表格中的按钮模板-->
                        <script type="text/html" id="edit-project">
                            <a class="layui-btn layui-btn-xs" lay-event="oper">操作接口</a>
                            <a class="layui-btn layui-btn-xs" lay-event="Invite">邀请成员</a>
                            <br/>
                            <a class="layui-btn layui-btn-xs" lay-event="query">查看成员</a>
                            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除项目</a>
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


<!--用于新增项目弹层-->
<div id="pro" hidden>
    <br/>
    <div class="layui-form">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">项目名称</label>
                <div class="layui-input-inline">
                    <input type="text" id="projectname" required lay-verify="required" placeholder="请输入项目名称" autocomplete="on" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <i style="color:red">*</i>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">项目标签</label>
                <div class="layui-input-inline">
                    <input type="text" id="projecttag" required lay-verify="required" placeholder="请输入项目标签" autocomplete="on" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <i style="color:red">*</i>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">项目描述</label>
                <div class="layui-input-inline">
                    <input type="text" id="projectdesc" placeholder="请输入项目描述" autocomplete="on" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">公开程度</label>
                <div class="layui-input-inline">
                    <input type="radio" id="projectpri1" name="aboutpub" value="1" title="公开" checked>
                    <input type="radio" id="projectpri2" name="aboutpub" value="0" title="私有">
                </div>
            </div>
        </div>
    </div>
</div>

<!--用于用户选择弹层-->
<div hidden id="users">
    <div class="layui-col-md12">
        <div class="layui-card">
            <div class="layui-card-body">

                <div style="margin-bottom: 10px;">
                    <label class="layui-form-label">用户姓名：</label>
                    <input class="layui-input" name="user-name" id="query-table-username" autocomplete="off" style="width: 200px">
                    <label class="layui-form-label">用户工号：</label>
                    <input class="layui-input" name="user-account" id="query-table-account" autocomplete="off" style="width: 200px">
                    <!--data-type属性为H5新增，可以在$中使用data(type)进行值的获取-->
                    <button id="queryus" class="layui-btn" data-type="userreload">查询</button>
                </div>

                <table class="layui-hide" id="user-info-table" lay-filter="user-table-event"></table>
            </div>
        </div>
    </div>
</div>

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
        var currentProjectId = null;

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
                userAccount:"${userAccount}"
                ,userRole:1
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

                location.href = '${mainDomain}aps/interface/myinterface?projectid='+trdata.projectid+'&userAccount=${userAccount}&userRole=1&page=1&limit=10'; //跳转接口操作页面

            }else if(layEvent==='Invite'){//邀请人员

                currentProjectId = trdata.projectid;

                layer.open(
                        {
                            type:"1"
                            ,area : '500px'
                            ,title:"未邀请用户信息"
                            ,content: $("#users")
                            ,btn: ['确定', '取消']
                            ,btnAlign: 'c'
                            ,success:function(childobj,index){
                            //每次成功打开碳层，便需要渲染用户信息表格
                                table.render({
                                    elem: '#user-info-table'
                                    ,url: '${mainDomain}aps/user/getallusers.action'//用户数据接口
                                    ,method:'post'
                                    ,cols:[[
                                        {type:'checkbox'}
                                        ,{field:'userid', title: '用户id'}
                                        ,{field:'username', title: '用户姓名'}
                                        ,{field:'userAccount', title: '工号'}
                                    ]]
                                    ,width:400
                                    ,page: true
                                    ,where:{
                                        projectid:currentProjectId
                                        ,isInv:0
                                        ,userAccount:null
                                        ,userName:null
                                        ,page:laypage.curr
                                        ,limit:laypage.limit
                                    }
                                });
                            }
                            ,yes:function(index,childobj){

                            //判断全局数组，如果数组为空，则说明用户未勾选任何人
                            if(userUnique.length==0){
                                return layer.msg('请选择一个用户');
                            }

                            var yaoqingdata = {
                                projectName:trdata.projectname,
                                userIdList:userUnique
                            };
                            //向后台发起邀请用户请求
                            var re = true;
                            $.ajax(
                                    {
                                        url: "${mainDomain}aps/project/makeprohaveper.action" //邀请用户接口
                                        ,type:"post"
                                        ,dataType:"json"
                                        ,async:false//禁止异步，这样可以操作re变量
                                        ,data:JSON.stringify(yaoqingdata)
                                        ,contentType: 'application/json;charset=utf-8'
                                        ,success: function(resp){

                                        //加入项目的提示与跳转
                                        layer.msg('邀请用户成功', {
                                            offset: '15px'
                                            ,icon: 1
                                            ,time: 1000
                                        },function(){
                                            //后台处理完成后，关闭弹层
                                            layer.close(index);
                                            //刷新页面
                                            location.reload();
                                        });
                                        }
                                        ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                                        // 状态码:XMLHttpRequest.status
                                        // 状态:XMLHttpRequest.readyState
                                        // 错误信息 :textStatus
                                        if(XMLHttpRequest.status != 200){
                                            layer.msg('后台处理异常，请重试');
                                            re = false;
                                            return;
                                        }
                                    }
                                    });
                            if(!re){
                                return false;
                            }
                        }
                        }
                );
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
                                }
                            });
                        }
                        });
            }else if(layEvent==='del'){
                layer.confirm('删除项目同时将会删除项目下的所有数据', function(index){
                    //调用删除接口
                    var rre = true;
                    $.ajax(
                            {
                                url: "${mainDomain}aps/project/deleteproject.action" //用户删除接口
                                ,type:"post"
                                ,dataType:"json"
                                ,async:false//禁止异步，这样可以操作re变量
                                ,data:{
                                        projectid:trdata.projectid
                                      }
                                ,success: function(resp){

                                    if(resp.state==103){
                                        layer.msg('参数异常，请稍后再试');
                                        rre = false;
                                        return;
                                    }else if(resp.state==-12003){
                                        layer.msg('服务器内部错误，请稍后再试');
                                        rre = false;
                                        return;
                                    }else if(resp.state==-11000){
                                        layer.msg('内部异常，请联系17040386');
                                        rre = false;
                                        return;
                                    }

                                    //加入项目的提示与跳转
                                    layer.msg('删除项目成功', {
                                        offset: '15px'
                                        ,icon: 1
                                        ,time: 1000
                                    });
                                }
                                ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                                    // 状态码:XMLHttpRequest.status
                                    // 状态:XMLHttpRequest.readyState
                                    // 错误信息 :textStatus
                                    if(XMLHttpRequest.status != 200){
                                        layer.msg('后台处理异常，请重试');
                                        rre = false;
                                        return;
                                    }
                                }
                            });
                    if(!rre){
                        return false;
                    }

                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    location.reload();
                });
            }
        });

        //监听表格复选框
        table.on('checkbox(user-table-event)',function(obj){

            var checkdata = obj.data;//获取选中行的相关数据
            var user = checkdata.userid;//获取选择行的用户id
            if(obj.checked){
                userList.push(user);//放入list中待用
                var useSet = new Set(userList);//新建Set对象
                userUnique = Array.from(useSet);//获得去重数组
            }else{//如果不是选中状态则删除数组中的元素
                var user = checkdata.userid;//获取选择行的用户id
                Array.prototype.indexOf = function(val) {//重写Array的原型的indexOf方法，所以的数组实例都能引用
                    for (var i = 0; i < this.length; i++) {
                        if (this[i] == val)
                            return i;
                    }
                    return -1;
                };
                Array.prototype.remove = function(val) {//重写Array的原型的remove方法，所以的数组实例都能引用
                    var index = this.indexOf(val);
                    if (index > -1) {
                        this.splice(index, 1);
                    }
                };
                userUnique.remove(user)
            }

        });


        //定义active对象，用于弹层获取数据并进行表格重载
        var active = {

            newproject:function(){

                layer.open(
                        {
                            type:"1"
                            ,area : '500px'
                            ,title:"新增项目信息"
                            ,content: $("#pro")
                            ,btn: ['新增', '取消']
                            ,btnAlign: 'c'
                            ,yes:function(index,childobj){
                            var name = $('#projectname').val();
                            var prtag = $('#projecttag').val();
                            var prdesc = $('#projectdesc').val();
                            var ispub = $('input[name="aboutpub"]:checked').val();

                            if(name.length==0){
                                return layer.msg('名称不能为空');
                            }else if(prtag.length==0){
                                return layer.msg('标签不能为空随便写点呗');
                            }

                            var podata = {
                                projectName:name
                                ,privacy:ispub
                                ,tag:prtag
                                ,desc:prdesc
                                ,userAccount:"${userAccount}"
                            };

                            //校验通过后，向后台新增项目数据
                            var rre = true;
                            $.ajax(
                                    {
                                        url: "${mainDomain}aps/project/addproject.action" //新增项目接口
                                        ,type:"post"
                                        ,async:false//禁止异步，这样可以操作re变量
                                        ,dataType:"json"
                                        ,data:podata
                                        ,success: function(resp){

                                        if(resp.state==303){
                                            layer.msg('项目名称重复');
                                            rre = false;
                                            return;
                                        }

                                        //加入项目的提示与跳转
                                        layer.msg('新增项目成功', {
                                            offset: '15px'
                                            ,icon: 1
                                            ,time: 1000
                                        }, function(){
                                            //后台处理通过后，执行关闭
                                            layer.close(index);

                                            //关闭完成后，进行表格重载
                                            table.reload('my-project-table',{
                                                url:'${mainDomain}aps/project/myprojects.action'//去后台重新获取新的表格数据
                                                ,method:'post'
                                                ,page: {
                                                    curr: 1 //重新从第 1 页开始
                                                }
                                                ,where: {
                                                    userAccount:"${userAccount}"
                                                    ,userRole:"1"
                                                }
                                            });
                                        });
                                    }
                                        ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                                        // 状态码:XMLHttpRequest.status
                                        // 状态:XMLHttpRequest.readyState
                                        // 错误信息 :textStatus
                                        if(XMLHttpRequest.status != 200){
                                            layer.msg('后台处理异常，请重试');
                                            rre = false;
                                            return;
                                        }
                                    }
                                    });

                            if(!rre){
                                return false;
                            }
                        }
                        }
                );

            }
            ,newprojectnorecord:function(){

                layer.open(
                        {
                            type:"1"
                            ,area : '500px'
                            ,title:"新增项目信息"
                            ,content: $("#pro")
                            ,btn: ['新增', '取消']
                            ,btnAlign: 'c'
                            ,yes:function(index,childobj){
                            var name = $('#projectname').val();
                            var prtag = $('#projecttag').val();
                            var prdesc = $('#projectdesc').val();
                            var ispub = $('input[name="aboutpub"]:checked').val();

                            if(name.length==0){
                                return layer.msg('名称不能为空');
                            }else if(prtag.length==0){
                                return layer.msg('标签不能为空随便写点呗');
                            }

                            var podata = {
                                projectName:name
                                ,privacy:ispub
                                ,tag:prtag
                                ,desc:prdesc
                                ,userAccount:"${userAccount}"
                            };

                            //校验通过后，向后台新增项目数据
                            var rre = true;
                            $.ajax(
                                    {
                                        url: "${mainDomain}aps/project/addproject.action" //用户加入项目接口
                                        ,type:"post"
                                        ,async:false//禁止异步，这样可以操作re变量
                                        ,dataType:"json"
                                        ,data:podata
                                        ,success: function(resp){

                                        if(resp.state==303){
                                            layer.msg('项目名称重复');
                                            rre = false;
                                            return;
                                        }

                                        //加入项目的提示与跳转
                                        layer.msg('新增项目成功', {
                                            offset: '15px'
                                            ,icon: 1
                                            ,time: 1000
                                        }, function(){
                                            //后台处理通过后，执行关闭
                                            layer.close(index);

                                            //关闭完成后，刷新当前页面
                                            location.reload();

                                        });
                                    }
                                        ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                                        // 状态码:XMLHttpRequest.status
                                        // 状态:XMLHttpRequest.readyState
                                        // 错误信息 :textStatus
                                        if(XMLHttpRequest.status != 200){
                                            layer.msg('后台处理异常，请重试');
                                            rre = false;
                                            return;
                                        }
                                    }
                                    });

                            if(!rre){
                                return false;
                            }

                        }
                        }
                );

            }
            ,userreload:function(){
                var usern = $('#query-table-username');
                var usera = $('#query-table-account');
                //执行表格重载
                table.reload('user-info-table',{
                    url:'${mainDomain}aps/user/getallusers.action'//用户查询接口
                    ,method:'post'
                    ,page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: {
                        projectid:currentProjectId
                        ,isInv:0
                        ,userName: usern.val()//获取到projectName元素的value属性的值
                        ,userAccount:usera.val()
                    }
                });

            }

        };

        //执行新建项目按钮点击事件
        $('#newp').on('click',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this):'';
        });

        //执行用户查询按钮点击事件
        $('#queryus').on('click',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this):'';
        });



    });
</script>
</body>
</html>