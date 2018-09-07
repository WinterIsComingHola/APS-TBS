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

<!--   <div class="layui-card layadmin-header">
    <div class="layui-breadcrumb" lay-filter="breadcrumb">
      <a lay-href="">主页</a>
      <a><cite>组件</cite></a>
      <a><cite>主页</cite></a>
      <a><cite>项目信息</cite></a>
    </div>
  </div> -->

<div class="layui-fluid">



    <div class="layui-row layui-col-space15">

        <div class="layui-col-md12">
        <span class="layui-breadcrumb" lay-separator="-">
          <a href="${miscDomain}/aps/project/public?userAccount=${userAccount}">首页</a>
          <a href="${miscDomain}/aps/project/public?userAccount=${userAccount}">公共项目</a>
          <#if userRole == 1>
              <a href="${miscDomain}/aps/project/mypublic?userAccount=${userAccount}&userRole=1&page=1&limit=10">我创建的项目</a>
          <#elseif userRole == 0>
              <a href="${miscDomain}/aps/project/mypublic?userAccount=${userAccount}&userRole=0&page=1&limit=10">我加入的项目</a>
          <#else>
                <a href="${miscDomain}/aps/project/public?userAccount=${userAccount}">首页</a>
          </#if>

          <a><cite>接口操作</cite></a>
        </span>
        </div>

        <div class="layui-col-md12">

            <#if apiInfos==0>
                <div class="layui-card">
                    <div class="layui-card-header layui-text" style="font-size: 20px;">接口操作</div>
                    <div class="layadmin-tips">
                        <i class="layui-icon" face>&#xe664;</i>
                        <div class="layui-text" style="font-size: 20px;">
                            项目下还没有任何接口
                        </div>
                        <button id="newinnotable" class="layui-btn newin" data-type="newinterface" data-state="newinnotable">添加接口</button>
                    </div>
                </div>
            <#elseif apiInfos==1>
                <div class="layui-card">
                    <div class="layui-card-header layui-text" style="font-size: 20px;">接口操作</div>
                    <div class="layui-card-body">

                        <div style="margin-bottom: 10px;">

                            <!--data-type属性为H5新增，可以在$中使用data(type)进行值的获取-->
                            <button id="newinwithtable" class="layui-btn newin" data-type="newinterface" data-state="newinwithtable">添加接口</button>
                            <div id="hidden-account" hidden>17040386</div>
                        </div>

                        <table class="layui-hide" id="interface-table" lay-filter="interface-table-event"></table>

                        <!--编写表格中的按钮模板-->
                        <script type="text/html" id="edit-interface">
                            <a class="layui-btn layui-btn-xs" lay-event="mock">MOCK</a>
                            <a class="layui-btn layui-btn-xs" lay-event="postman">POSTMAN</a>
                        </script>
                    </div>
                </div>
            <#else>
            </#if>

        </div>
    </div>
</div>


<!--用于新增接口弹层-->
<div id="interface" hidden>
    <br/>
    <div class="layui-form">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">接口名称</label>
                <div class="layui-input-inline">
                    <input type="text" id="interfacename" required lay-verify="required" placeholder="请输入接口名称" autocomplete="on" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <i style="color:red">*</i>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">接口路径</label>
                <div class="layui-input-inline">
                    <input type="text" id="interfacepath" required lay-verify="required" placeholder="接口路径请以/开始" autocomplete="on" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <i style="color:red">*</i>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">接口方法</label>
                <div class="layui-input-inline">
                    <select id="interfacemethod" lay-filter="method" style="width: 300px">
                        <option value="">请选择接口方法</option>
                        <option value="1">GET</option>
                        <option value="2">POST</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline">
                <i style="color:red">*</i>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">接口描述</label>
                <div class="layui-input-inline">
                    <input type="text" id="interfacedesc" placeholder="请输入接口描述" autocomplete="on" class="layui-input">
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
    }).use(['index', 'table','layer','jquery','laypage'], function(){
        var layer = layui.layer
                ,$ = layui.jquery
                ,table = layui.table
                ,laypage = layui.laypage;

        //var userList = [];//定义空数组存储用户的选择数据

        //渲染接口表格
        table.render({
            elem: '#interface-table'
            ,url: '${mainDomain}aps/interface/getApiByProject.action'//获得所有有效API的接口
            ,method:'post'
            ,cols: [[
                {field:'interid', title: '接口id'}
                ,{field:'intername', title: '接口名称'}
                ,{field:'intermethod', title: '接口方法'}
                ,{field:'interpath', title: '接口路径'}
                ,{field:'interdesc', title: '描述'}
                ,{field:'oper', title: '操作',toolbar:'#edit-interface'}
                /*,{field:'experience', width:80, title: '积分', sort: true}
                ,{field:'score', width:80, title: '评分', sort: true}
                ,{field:'classify', width:80, title: '职业'}
                ,{field:'wealth', width:135, title: '财富', sort: true}*/
            ]]
            ,where: {
                projectid:${projectid}
                ,page:laypage.curr
                ,limit:laypage.limit
            }
            ,page: true
        });

        //监听表格的工具bar
        table.on('tool(interface-table-event)',function(obj){
            var trdata = obj.data;//获得当前行数据
            //var hiddendiv = $('#hidden-account');

            //trdata.userAccount = hiddendiv.text();
            var layEvent = obj.event;//获得 模板中lay-event 对应的值
            if(layEvent==='mock'){//操作接口

                location.href = '${mainDomain}aps/interface/getinterconfig?projectid=${projectid}&userAccount=${userAccount}&apiId='+trdata.interid+'&userRole=${userRole}'; //跳转接口操作页面

            }else if(layEvent==='postman'){
                location.href = '${mainDomain}aps/interface/getpostmanconfig?projectid=${projectid}&userAccount=${userAccount}&apiId='+trdata.interid+'&userRole=${userRole}'; //跳转POSTMAN操作页面
            }
        });


        //定义active对象，用于弹层获取数据并进行表格重载
        var active = {

            newinterface:function(pagestat){

                layer.open(
                        {
                            type:"1"
                            ,area : ['500px', '430px']
                            ,title:"新增项目信息"
                            ,content: $("#interface")
                            ,btn: ['新增', '取消']
                            ,btnAlign: 'c'
                            ,yes:function(index,childobj){
                            var name = $('#interfacename').val();
                            var path = $('#interfacepath').val();
                            var method = $("#interfacemethod option:selected").val();
                            var desc = $('#interfacedesc').val();

                            var reg = /^\//;

                            if(name.length==0){
                                return layer.msg('名称不能为空');
                            }else if(path.length==0){
                                return layer.msg('路径不能为空');
                            }else if(!reg.test(path)){
                                return layer.msg('路径需要以/开头');
                            }else if(method.length==0){
                                return layer.msg('请选择一个接口方法');
                            }

                            var apiInfo = {
                                apiName:name
                                ,urlPath:path
                                ,httpMethod:method
                                ,desc:desc
                                ,projectId:${projectid}
                                ,userAccount:"${userAccount}"
                            };

                            //校验通过后，向后台新增项目数据
                            var rre = true;
                            $.ajax(
                                    {
                                        url: "${mainDomain}aps/interface/addApi.action" //添加接口
                                        ,type:"post"
                                        ,async:false//禁止异步，这样可以操作re变量
                                        ,dataType:"json"
                                        ,data:apiInfo
                                        ,success: function(resp){

                                        if(resp.state==404){
                                            layer.msg('接口名称已经存在，请核实');
                                            rre = false;
                                            return;
                                        }else if(resp.state==405){
                                            layer.msg('路径已经存在，请核实');
                                            rre = false;
                                            return;
                                        }else if(resp.state==102){
                                            layer.msg('数据处理异常，请刷新浏览器再试');
                                            rre = false;
                                            return;
                                        }

                                        //新增接口的提示与跳转
                                        layer.msg('新增接口成功', {
                                            offset: '15px'
                                            ,icon: 1
                                            ,time: 1000
                                        }, function(){

                                            if(pagestat==='newinnotable'){
                                                //后台处理通过后，执行关闭
                                                layer.close(index);
                                                //关闭完成后，刷新当前页面
                                                location.reload();
                                            }else if(pagestat==='newinwithtable'){
                                                //后台处理通过后，执行关闭
                                                layer.close(index);

                                                //关闭完成后，进行表格重载
                                                table.reload('interface-table',{
                                                    url:'${mainDomain}aps/interface/getApiByProject.action'//去后台重新获取新的表格数据
                                                    ,method:'post'
                                                    ,page: {
                                                        curr: 1 //重新从第 1 页开始
                                                    }
                                                    ,where: {
                                                        projectid:${projectid}
                                                        ,page: laypage.curr
                                                        ,limit:laypage.limit
                                                    }
                                                });
                                            }

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

        };

        //执行新建项目按钮点击事件
        $('.newin').on('click',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //获取新增接口按钮的所属页面
            var pagestat = $(this).data('state');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this,pagestat):'';
        });
    });
</script>
</body>
</html>
