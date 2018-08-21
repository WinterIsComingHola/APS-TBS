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

<div class="layui-fluid">

    <div class="layui-row layui-col-space15">

        <div class="layui-col-md12">
        <span class="layui-breadcrumb" lay-separator="-">
          <a href="${miscDomain}/aps/project/public?userAccount=${userAccount}">首页</a>
          <a href="${miscDomain}/aps/project/public?userAccount=${userAccount}">公共项目</a>
          <#if userRole == 1>
              <a href="${miscDomain}/aps/project/mypublic?userAccount=${userAccount}&userRole=1">我创建的项目</a>
          <#elseif userRole == 0>
              <a href="${miscDomain}/aps/project/mypublic?userAccount=${userAccount}&userRole=0">我加入的项目</a>
          <#else>
                <a href="${miscDomain}/aps/project/public?userAccount=${userAccount}">首页</a>
          </#if>
          <a href="${mainDomain}aps/interface/myinterface?projectid=${projectid}&userAccount=${userAccount}&userRole=${userRole}">接口操作</a>
          <a><cite>POSTMAN</cite></a>
        </span>
        </div>

        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header layui-text" style="font-size: 20px;">POSTMAN</div>
                <div class="layui-card-body">

                    <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
                        <ul class="layui-tab-title">
                            <li class="layui-this">预览信息</li>
                            <li>POSTMAN配置</li>
                            <li>运行测试</li>
                        </ul>
                        <div class="layui-tab-content">
                            <div class="layui-tab-item layui-show">
                                <div class="layui-card">
                                    <div class="layui-card-header layui-text" style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2">基本信息</div>
                                    <div class="layui-card-body">
                                        <table lay-filter="basicinfo">
                                            <thead>
                                            <tr>
                                                <th lay-data="{field:'heada',style:'height:80px'}"></th>

                                                <th lay-data="{field:'headb'}"></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>接口名称：&nbsp;&nbsp;&nbsp;${apiName}</td>
                                                <td>创建人：&nbsp;&nbsp;&nbsp;${createrName}</td>
                                            </tr>
                                            <tr>
                                                <td>接口路径：&nbsp;&nbsp;&nbsp;<span class="layui-badge">${apiMethod}</span>&nbsp;&nbsp;&nbsp;${apiPath}</td>
                                                <td>MOCK地址：&nbsp;&nbsp;&nbsp;${miscDomain}${apiPath}</td>
                                            </tr>
                                            <tr>
                                                <td>接口状态：&nbsp;&nbsp;&nbsp;<span class="layui-badge-dot"></span>&nbsp;${isActive}</td>
                                                <td>创建时间：&nbsp;&nbsp;&nbsp;${createTime}</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div class="layui-tab-item">
                                <form class="layui-form" action="">

                                    <#--<div class="layui-card">
                                        <div class="layui-card-header layui-text" style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2">基本设置</div>
                                        <div class="layui-card-body">
                                            <!-- <form class="layui-form" action=""> &ndash;&gt;
                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i style="color:red">*</i>&nbsp;接口名称：</label>
                                                <div class="layui-input-block">
                                                    <input type="text" name="intername" lay-verify="required" autocomplete="off" placeholder="请输入接口名称" class="layui-input" value="${apiName}">
                                                </div>
                                            </div>

                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i style="color:red">*</i>&nbsp;接口方法：</label>
                                                <div class="layui-input-block">
                                                    <select id="interfacemethod" name="intermetdod" lay-filter="method" style="width: 300px">
                                                        <option value="">请选择接口方法</option>
                                                        <option value="GET">GET</option>
                                                        <option value="POST">POST</option>
                                                        <option value="PUT">PUT</option>
                                                        <option value="DELETE">DELETE</option>
                                                        <option value="HEAD">HEAD</option>
                                                        <option value="OPTION">OPTION</option>
                                                    &lt;#&ndash;<option value="${apiMethod}" selected>${apiMethod}</option>&ndash;&gt;
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i style="color:red">*</i>&nbsp;接口路径：</label>
                                                <div class="layui-input-block">
                                                    <input type="text" name="interpath" lay-verify="required" autocomplete="off" placeholder="路径必须以/开头" class="layui-input" value="${apiPath}">
                                                </div>
                                            </div>

                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i style="color:red">*</i>&nbsp;接口状态：</label>
                                                <div class="layui-input-block">
                                                    <input type="checkbox" name="interstate" lay-skin="switch" lay-text="开启|关闭" checked>
                                                </div>
                                            </div>


                                        </div>
                                    </div>-->

                                    <div class="layui-card">
                                        <div class="layui-card-header layui-text" style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2">请求参数配置</div>
                                        <div class="layui-card-body">
                                            <div class="layui-tab layui-tab-brief">
                                                <ul class="layui-tab-title">
                                                    <li class="layui-this">请求BODY</li>
                                                    <li>请求Header</li>
                                                    <li>Cookie</li>
                                                </ul>

                                                <div class="layui-tab-content" id="request-root-param" data-apiid="${apiId}">
                                                    <div class="layui-tab-item layui-show" id="req-body">
                                                        <input type="radio" name="bodyty" value="www-form" title="www-form" lay-filter="bodyty" checked>
                                                        <input type="radio" name="bodyty" value="raw" title="raw" lay-filter="bodyty">
                                                        <hr>
                                                        <!--后台需要根据数据库数据进行循环显示-->
                                                        <div id="newparamdiv">
                                                            <button class="layui-btn layui-btn-radius" type="button" id="newparam" data-type="reqparamreload">新增</button>
                                                        </div>
                                                        <div hidden>
                                                            <hr>
                                                        </div>

                                                        <div class="layui-form" lay-filter="addreqparam" id="submitreqparam">
                                                            <!--将reqParamovInfos进行迭代显示-->

                                                            <#if ((reqParams![])?size gt 0)>
                                                                <#list reqParams as reqParam>
                                                                    <#if reqParam.botyType == 1 && reqParam.paramType == 1>
                                                                        <div class='layui-form-item' id='${reqParam.rowId}'>
                                                                            <div class='layui-input-inline'>
                                                                                <input type='text' name='1${reqParam.rowId}' value='${reqParam.fieldName}' lay-verify='required' autocomplete='off' placeholder='参数名称' data-type='updatereqexc' class='layui-input inputfocus'/>
                                                                            </div>
                                                                            <div class='layui-input-inline'>
                                                                                <input type='text' name='2${reqParam.rowId}' value='${reqParam.fieldValue}' lay-verify='required' autocomplete='off' placeholder='参数值' data-type='updatereqexc' class='layui-input inputfocus'/>
                                                                            </div>
                                                                            <div class='layui-input-inline'>
                                                                                <input type='text' name='3${reqParam.rowId}' value='${reqParam.fieldDesc}' autocomplete='off' placeholder='描述' data-type='updatereqexc' class='layui-input inputfocus'/>
                                                                            </div>
                                                                            <div class='layui-input-inline' style='width: 40px;'>
                                                                                <button class='layui-btn layui-btn-sm layui-btn-radius layui-btn-danger deletereqparam' id='${reqParam.rowId}' data-type='deletereq' data-reqty='param' type='button'>
                                                                                    <i class='layui-icon'>&#xe640;</i>
                                                                                </button>
                                                                            </div>
                                                                        </div>
                                                                    </#if>
                                                                </#list>
                                                            </#if>

                                                        </div>
                                                        <div id="newrawdiv">

                                                            <#if ((reqParams![])?size gt 0)>
                                                                <#list reqParams as reqParam>
                                                                    <#if reqParam.botyType == 2 && reqParam.paramType == 1>
                                                                        <textarea name="rawty" rowid="4${reqParam.rowId}" class="layui-textarea inputfocus" placeholder="请输入json或者xml格式" data-type='updatereqexc' style="width: 800px;height: 400px">${reqParam.rawBody}</textarea>
                                                                    </#if>
                                                                </#list>
                                                            <#else>
                                                                <textarea name="rawty" rowid="" class="layui-textarea inputfocus" placeholder="请输入json或者xml格式" data-type='updatereqexc' style="width: 800px;height: 400px"></textarea>
                                                            </#if>
                                                            <#--<#if reqrawbody!=null>
                                                                <textarea name="rawty" class="layui-textarea" placeholder="请输入json或者xml格式" style="width: 800px;height: 400px">${reqrawbody}</textarea>
                                                            </#if>-->
                                                        </div>
                                                    </div>
                                                    <div class="layui-tab-item">
                                                        <!--后台需要根据数据库数据进行循环显示-->
                                                        <button class="layui-btn layui-btn-radius" type="button" id="newhead" data-type="reqheadreload">新增</button>
                                                        <hr>
                                                        <div class="layui-form" lay-filter="addreqhead" id="submitreqhead">
                                                            <#if ((reqParams![])?size gt 0)>
                                                                <#list reqParams as reqParam>
                                                                    <#if reqParam.paramType == 2>
                                                                        <div class='layui-form-item' id='${reqParam.rowId}'>
                                                                            <div class='layui-input-inline'>
                                                                                <input type='text' name='1${reqParam.rowId}' value='${reqParam.fieldName}' lay-verify='required' autocomplete='off' placeholder='参数名称' data-type='updatereqexc' class='layui-input inputfocus'/>
                                                                            </div>
                                                                            <div class='layui-input-inline'>
                                                                                <input type='text' name='2${reqParam.rowId}' value='${reqParam.fieldValue}' lay-verify='required' autocomplete='off' placeholder='参数值' data-type='updatereqexc' class='layui-input inputfocus'/>
                                                                            </div>
                                                                            <div class='layui-input-inline'>
                                                                                <input type='text' name='3${reqParam.rowId}' value='${reqParam.fieldDesc}' autocomplete='off' placeholder='描述' data-type='updatereqexc' class='layui-input inputfocus'/>
                                                                            </div>
                                                                            <div class='layui-input-inline' style='width: 40px;'>
                                                                                <button class='layui-btn layui-btn-sm layui-btn-radius layui-btn-danger deletereqparam' id='${reqParam.rowId}' data-type='deletereq' data-reqty='header' type='button'><i class='layui-icon'>&#xe640;</i>
                                                                                </button>
                                                                            </div>
                                                                        </div>
                                                                    </#if>
                                                                </#list>
                                                            </#if>
                                                        </div>

                                                    </div>
                                                    <div class="layui-tab-item">
                                                        <!--后台需要根据数据库数据进行循环显示-->
                                                        <button class="layui-btn layui-btn-radius" type="button" id="newcookie" data-type="cookiedreload">新增</button>
                                                        <hr>
                                                        <div class="layui-form" lay-filter="addcookie" id="submitcookie">
                                                            <#if ((reqParams![])?size gt 0)>
                                                                <#list reqParams as reqParam>
                                                                    <#if reqParam.paramType == 3>
                                                                        <div class='layui-form-item' id='${reqParam.rowId}'>
                                                                            <div class='layui-input-inline'>
                                                                                <input type='text' name='1${reqParam.rowId}'  value='${reqParam.fieldName}' lay-verify='required' autocomplete='off' placeholder='name' data-type='updatereqexc' class='layui-input inputfocus'/>
                                                                            </div>
                                                                            <div class='layui-input-inline'>
                                                                                <input type='text' name='2${reqParam.rowId}' value='${reqParam.fieldValue}'  lay-verify='required' autocomplete='off' placeholder='value' data-type='updatereqexc' class='layui-input inputfocus'/>
                                                                            </div>
                                                                            <div class='layui-input-inline' style='width: 40px;'>
                                                                                <button class='layui-btn layui-btn-sm layui-btn-radius layui-btn-danger deletereqparam' id='${reqParam.rowId}' data-type='deletereq' data-reqty='cookie' type='button'><i class='layui-icon'>&#xe640;</i>
                                                                                </button>
                                                                            </div>
                                                                        </div>
                                                                    </#if>
                                                                </#list>
                                                            </#if>
                                                        </div>

                                                    </div>
                                                </div>
                                            </div>

                                        </div>

                                        <div>
                                            <hr class="layui-bg-green">
                                            <div class="layui-form-item">
                                                <button class="layui-btn" lay-submit="" lay-filter="intersub">提交保存</button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>

                            <div class="layui-tab-item">

                                <div class="layui-card">
                                    <div class="layui-card-header layui-text" style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2">请求man</div>
                                    <div class="layui-card-body">

                                        <div class="layui-form layui-form-pane" id="request-man">
                                            <div class="layui-form-item">
                                                <div class="layui-input-inline">
                                                    <select id="reqmethod" name="reqmethod" lay-filter="reqmethod">
                                                        <option value="">请选择接口方法</option>
                                                        <option value="GET">GET</option>
                                                        <option value="POST">POST</option>
                                                    </select>
                                                </div>
                                                <div class="layui-input-inline">
                                                    <input type="text" id="targeturl" name="url" lay-verify="required" autocomplete="off" placeholder="http://example.com" class="layui-input" style="width: 500px">
                                                </div>
                                                <div class="layui-input-inline" style="width: 400px;">
                                                    <button class="layui-btn layui-btn-radius" type="button" id="reqRunSend" data-type="reqexc" style="float:right;">发送</button>
                                                </div>
                                            </div>
                                            <hr class="layui-bg-green">

                                            <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
                                                <ul class="layui-tab-title">
                                                    <li class="layui-this">响应HEAD</li>
                                                    <li>响应BODY</li>
                                                </ul>
                                                <div class="layui-tab-content">
                                                    <div class="layui-tab-item layui-show">
                                                        <textarea name="respstrhead" class="layui-textarea" style="width: 800px;height: 400px"></textarea>
                                                    </div>
                                                    <div class="layui-tab-item">
                                                        <textarea name="respstrbody" class="layui-textarea" style="width: 800px;height: 400px"></textarea>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="${miscDomain}/statics/layuiadmin/layui/layui.js"></script>
<script src="${miscDomain}/statics/layuiadmin/aps/jsonhander.js"></script>
<script>
    layui.config({
        base: '${miscDomain}/statics/layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index', 'table','layer','jquery','form'], function(){

        var layer = layui.layer
                ,$ = layui.jquery
                ,table = layui.table
                ,form = layui.form;
        var reqMethod = null;

        //渲染接口表格
        table.init('basicinfo', {
            skin: 'nob'
        });

        //监听请求类型单选框
        form.on('radio(bodyty)',function(data){
            if(data.value==='www-form'){

                //清楚页面上的消息格式
                if($("#submitreqhead > *").length>0){
                    $("input[title='raw']").prop("checked",true);
                    $("input[title='www-form']").removeAttr("checked");
                    form.render('radio');

                    return layer.msg('当前页面存在form格式请求头，请删除后再试');
                }else if($("#submitcookie > *").length>0){
                    $("input[title='raw']").prop("checked",true);
                    $("input[title='www-form']").removeAttr("checked");
                    form.render('radio');

                    return layer.msg('当前页面存在form格式cookie，请先删除后再试');
                }
                if($('#newrawdiv > textarea').text().length>0){
                    $('#newrawdiv > textarea').text("");
                }

                //清理后台数据
                var rawrowid = $('#newrawdiv > textarea').attr("rowid");
                var type1 = 'deleteRaw';
                active[type1]?active[type1].call(this,rawrowid):'';

                $('#newrawdiv').css("display", "none");
                $('#newparamdiv').css("display", "block");
                form.render('radio');
            }else if(data.value==='raw'){

                var maxRowId = active.getMaxRowId.call(this)+1;

                if($("#submitreqparam > *").length>0){
                    /*$("input[title='raw']").attr("disabled",true);*/
                    $("input[title='www-form']").prop("checked",true);
                    $("input[title='raw']").removeAttr("checked");
                    form.render('radio');

                    return layer.msg('当前页面存在form格式请求参数，请删除后再试');
                }else if($("#submitreqhead > *").length>0){
                    $("input[title='www-form']").prop("checked",true);
                    $("input[title='raw']").removeAttr("checked");
                    form.render('radio');

                    return layer.msg('当前页面存在form格式请求头，请删除后再试');
                }else if($("#submitcookie > *").length>0){
                    $("input[title='www-form']").prop("checked",true);
                    $("input[title='raw']").removeAttr("checked");
                    form.render('radio');

                    return layer.msg('当前页面存在form格式cookie，请先删除后再试');
                }

                $('#newrawdiv').css("display", "block");
                $('#newrawdiv > textarea').attr("rowid","4"+maxRowId);

                $('#newparamdiv').css("display", "none");
                form.render('radio');


                //新增raw数据
                var type2 = 'rawreload';
                //调用active中的rawreload方法，请求后台接口
                active[type2]?active[type2].call(this,maxRowId):'';

                /*
                $("#submitreqparam > *").each(function(){
                    $(this).remove();
                });

                form.render(null, 'addreqparam');*/
            }
        });

        //监听HTTPMethod下拉菜单
        form.on('select(reqmethod)',function (data) {
            reqMethod = data.value;
            if(reqMethod === 'GET'){
                layer.msg('GET方法的请求参数请直接在URL中进行设定');
            }
            /*else if(httpMethod === 'POST'){
                $('#req-body > *').css("display", "block");
            }*/
        });

        //监听提交
        form.on('submit(intersub)', function(data){
            layer.msg('提交数据成功');

            /*        console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
                    console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
                    console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}*/


            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。

        });


        //定义active对象，用于响应按钮点击
        var active = {

            //获取随机数
            getRandom:function(num){
                var random = Math.floor((Math.random()+Math.floor(Math.random()*9+1))*Math.pow(10,num-1));
                return random;
            },

            //获取rowid最大值接口
            getMaxRowId:function(){

                var maxRowId = null;

                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/queryparammaxid.action" //提交后台处理
                            ,type:"post"
                            ,dataType:"json"
                            ,async:false//禁止异步，这样可以操作maxRowId变量
                            //,data:paramData
                            ,success: function(resp){
                            maxRowId = resp.data.maxId;

                        }
                            ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                            // 状态码:XMLHttpRequest.status
                            // 状态:XMLHttpRequest.readyState
                            // 错误信息 :textStatus
                            if(XMLHttpRequest.status != 200){
                                layer.msg('后台处理异常，请重试');
                                //re = false;
                            }
                        }
                        }
                );
                return maxRowId;
            }

            //根据apiid获取当前row的bodytype类型，用于当前页面的初始化展示
            ,getBodyByApi:function (apiId) {
                var bodyType = null;
                var reqParam = {apiId:apiId};
                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/querybody.action" //提交后台处理
                            ,type:"post"
                            ,dataType:"json"
                            ,async:false//禁止异步，这样可以操作maxRowId变量
                            ,data:reqParam
                            ,success: function(resp){
                                bodyType = resp.data.bodyType;
                            }
                            ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if(XMLHttpRequest.status != 200){
                                    layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );
                return bodyType;
            }


            //新增行数据
            ,addParamRow:function(data){
                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/insertparamrow.action" //提交后台处理
                            ,type:"post"
                            ,dataType:"json"
                            //,async:false//禁止异步，这样可以操作maxRowId变量
                            ,data:data
                            ,success: function(resp){
                            console.log("新增参数成功");
                        }
                            ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                            // 状态码:XMLHttpRequest.status
                            // 状态:XMLHttpRequest.readyState
                            // 错误信息 :textStatus
                            if(XMLHttpRequest.status != 200){
                                layer.msg('后台处理异常，请重试');
                                //re = false;
                            }
                        }
                        }
                );
            }

            //请求参数新增按钮
            ,reqparamreload:function(){

                var maxRowId = active.getMaxRowId.call(this)+1;
                var radnum = active.getRandom.call(this,2);
                var newFormHtml =
                        "<div class='layui-form-item' id='"+radnum+maxRowId+"'><div class='layui-input-inline'><input type='text' name='1"+maxRowId+"' value='' lay-verify='required' autocomplete='off' placeholder='参数名称' data-type='updatereqexc' class='layui-input inputfocus'></div><div class='layui-input-inline'><input type='text' name='2"+maxRowId+"' value='' lay-verify='required' autocomplete='off' placeholder='参数值' data-type='updatereqexc' class='layui-input inputfocus'></div><div class='layui-input-inline'><input type='text' name='3"+maxRowId+"' value='' autocomplete='off' placeholder='描述' data-type='updatereqexc' class='layui-input inputfocus'></div><div class='layui-input-inline' style='width: 40px;'><button class='layui-btn layui-btn-sm layui-btn-radius layui-btn-danger deletereqparam' id='"+radnum+maxRowId+"' data-type='deletereq' data-reqty='param' type='button'><i class='layui-icon'>&#xe640;</i></button></div></div>";
                var $form = $(newFormHtml);

                var paramData = {
                    id:maxRowId
                    ,bodyType:1
                    ,paramType:1
                    ,apiId:$('#request-root-param').data('apiid')
                };

                //新增row数据接口
                active.addParamRow.call(this,paramData);

                $("#submitreqparam").append($form);
                form.render(null, 'addreqparam');

            }
            //header参数新增
            ,reqheadreload:function(){

                var maxRowId = active.getMaxRowId.call(this)+1;
                var radnum = active.getRandom.call(this,2);

                var newFormHtml =
                        "<div class='layui-form-item' id='"+radnum+maxRowId+"'><div class='layui-input-inline'><input type='text' name='1"+maxRowId+"' value='' lay-verify='required' autocomplete='off' placeholder='参数名称' data-type='updatereqexc' class='layui-input inputfocus'></div><div class='layui-input-inline'><input type='text' name='2"+maxRowId+"' value='' lay-verify='required' autocomplete='off' placeholder='参数值' data-type='updatereqexc' class='layui-input inputfocus'></div><div class='layui-input-inline'><input type='text' name='3"+maxRowId+"' value='' autocomplete='off' placeholder='描述' data-type='updatereqexc' class='layui-input inputfocus'></div><div class='layui-input-inline' style='width: 40px;'><button class='layui-btn layui-btn-sm layui-btn-radius layui-btn-danger deletereqparam' id='"+radnum+maxRowId+"' data-type='deletereq' data-reqty='header' type='button'><i class='layui-icon'>&#xe640;</i></button></div></div>";
                var $form = $(newFormHtml);

                var paramData = {
                    id:maxRowId
                    ,bodyType:(function(){
                        if($('input[name="bodyty"]:checked').val()==='www-form'){
                            return 1;
                        }else{
                            return 2;
                        }
                    }())
                    ,paramType:2
                    ,apiId:$('#request-root-param').data('apiid')
                };

                //新增row数据接口
                active.addParamRow.call(this,paramData);

                $("#submitreqhead").append($form);
                form.render(null, 'addreqhead');
            }
            //cookie新增
            ,cookiedreload:function(){

                var maxRowId = active.getMaxRowId.call(this)+1;
                var radnum = active.getRandom.call(this,2);
                var newFormHtml =
                        "<div class='layui-form-item' id='"+radnum+maxRowId+"'><div class='layui-input-inline'><input type='text' name='1"+maxRowId+"' lay-verify='required' autocomplete='off' placeholder='name' data-type='updatereqexc' class='layui-input inputfocus'></div><div class='layui-input-inline'><input type='text' name='2"+maxRowId+"' lay-verify='required' autocomplete='off' placeholder='value' data-type='updatereqexc' class='layui-input inputfocus'></div><div class='layui-input-inline' style='width: 40px;'><button class='layui-btn layui-btn-sm layui-btn-radius layui-btn-danger deletereqparam' id='"+radnum+maxRowId+"' data-type='deletereq' data-reqty='cookie' type='button'><i class='layui-icon'>&#xe640;</i></button></div></div>";
                var $form = $(newFormHtml);


                var paramData = {
                    id:maxRowId
                    ,bodyType:(function(){
                        if($('input[name="bodyty"]:checked').val()==='www-form'){
                            return 1;
                        }else{
                            return 2;
                        }
                    }())
                    ,paramType:3
                    ,apiId:$('#request-root-param').data('apiid')
                };


                //新增row数据接口
                active.addParamRow.call(this,paramData);

                $("#submitcookie").append($form);
                form.render(null, 'addcookie');
            }

            //新增rawbody体行数据
            ,rawreload:function(maxRowId){

                var paramData = {
                    id:maxRowId
                    ,bodyType:2
                    ,paramType:1
                    ,apiId:$('#request-root-param').data('apiid')
                };

                //新增row数据接口
                active.addParamRow.call(this,paramData);
            }

            //删除row行数据
            ,deleteRaw:function (rowId) {

                var reqParam = {rowId:rowId.slice(1)};
                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/deleteparamrow.action" //提交后台处理
                            ,type:"post"
                            ,dataType:"json"
                           // ,async:false//禁止异步，这样可以操作maxRowId变量
                            ,data:reqParam
                            ,success: function(resp){
                                //console.log("新增参数成功");
                            }
                            ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if(XMLHttpRequest.status != 200){
                                    layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );
            }

            //相应请求字段的删除按钮
            ,deletereq:function(currentid,reqty){

                layer.confirm('确定删除吗？', function(index){

                    //调用删除接口
                    var rowIdNew = 'N'+currentid;//这个N没有任何业务逻辑意义，为了后面的slice函数取值用
                    active.deleteRaw.call(this,rowIdNew);

                    //删除dom结构
                    $("#"+currentid+"").remove();

                    if(reqty==="param"){
                        form.render(null, 'addreqparam');
                    }else if(reqty==="header"){
                        form.render(null, 'addreqhead');
                    }else if(reqty==="cookie"){
                        form.render(null, 'addcookie');
                    }

                    layer.close(index);

                });
            }

            //向目标服务器更新请求请求数据
            ,updatereqexc:function(rowid,newValue){
                //var re = true;
                //获取字段类型，第一个字符
                var fieldty = rowid.slice(0,1);
                //获取rowid，第二个字符开始到结束
                var newRowid = rowid.slice(1);

                //构造请求参数
                var paramData = {};
                paramData.fieldType = fieldty;
                paramData.rowId = newRowid;
                if(fieldty==1){
                    paramData.fieldName = newValue;
                }else if(fieldty==2){
                    paramData.fieldValue = newValue;
                }else if(fieldty==3){
                    paramData.fieldDesc = newValue;
                }else if(fieldty==4){
                    paramData.rawBody = newValue;
                }

                $.ajax(

                        {
                            url: "${mainDomain}aps/interface/updatereqfield.action" //提交后台处理
                            ,type:"post"
                            ,dataType:"json"
                            //,async:false//禁止异步，这样可以操作re变量
                            ,data:paramData
                            ,success: function(resp){

                            }
                            ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if(XMLHttpRequest.status != 200){
                                    layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );

            }

            ,reqexc:function(url,reqMethod,apiId,formOrRaw){

                var sendParam = {
                    url:url,
                    reqMethod:reqMethod,
                    apiId:apiId,
                    formOrRaw:formOrRaw
                };

                $.ajax(

                        {
                            url: "${mainDomain}aps/interface/runpostman.action" //提交后台处理
                            ,type:"post"
                            ,dataType:"json"
                            //,async:false//禁止异步，这样可以操作re变量
                            ,data:sendParam
                            ,success: function(resp){
                                //将数据结果返回到页面元素
                                //console.log(resp.data);
                                $("textarea[name='respstrhead']").text(formatJson(resp.data.header));
                                $("textarea[name='respstrbody']").text(formatJson(resp.data.respBody));

                            }
                            ,error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if(XMLHttpRequest.status != 200){
                                    layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );

            }

        };

        //执行新建param按钮点击事件
        $('#newparam').on('click',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this):'';
        });
        //执行新增head
        $('#newhead').on('click',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this):'';
        });
        //执行新增cookie
        $('#newcookie').on('click',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this):'';
        });


        //监听删除request参数节点按钮
        $('#request-root-param').on('click','.deletereqparam',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            var reqty = $(this).data('reqty');
            var currentid = $(this).attr('id');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this,currentid,reqty):'';
        });


        //监听输入框失去焦点事件，用于实时更新输入框的编辑
        $('#request-root-param').on('focusout','.inputfocus',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            var rowid = null;
            if($(this).attr('name')==='rawty'){
                rowid = $(this).attr('rowid');
            }else{
                rowid= $(this).attr('name');
            }

            //获取输入框value值的最新值
            var newValue = $(this).val();
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this,rowid,newValue):'';
            //console.log("执行了焦点失去事件");

        });



        //监听request发送runSend按钮
        $('#reqRunSend').on('click',function(){


            var url = $('#targeturl').val();
            var apiId = $('#request-root-param').data('apiid');
            var formOrRaw = null;
            if(reqMethod == null){
                return layer.msg('请先选择HTTP方法');
            }
            if(reqMethod==='POST'){
                if($('input:radio[name="bodyty"]:checked').val()==='www-form'){
                    if($("#submitreqparam > *").length===0){
                        return layer.msg('请设置消息体参数');
                    }
                    formOrRaw = 1;

                }else if($('input:radio[name="bodyty"]:checked').val()==='raw'){
                    if($("textarea[name='rawty']").text().length===0){
                        return layer.msg('请设置raw消息体参数');
                    }
                    formOrRaw = 2;
                }
            }else if(reqMethod==='GET'){
                formOrRaw = 0;
            }else{
                return layer.msg('请选择接口方法');
            }


            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this,url,reqMethod,apiId,formOrRaw):'';
        });



        //监听mock运行按钮
        $('#mocksubmit').on('click',function(){
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type]?active[type].call(this):'';
        });

        //页面load完成加载事件
        $(document).ready(function(){
            /*if($("#submitreqparam > *").length>=0 && $("textarea[name='rawty']").text().length==0){
                $('#newrawdiv').css("display", "none");
            }else if($("#submitreqparam > *").length==0 && $("textarea[name='rawty']").text().length>0){
                $('#newparamdiv').css("display", "none");
                //修改单选框的选择
                $("input[title='raw']").prop("checked",true);
                $("input[title='www-form']").removeAttr("checked");
                form.render('radio');

            }*/
            var apiId = $('#request-root-param').data('apiid');
            var type = 'getBodyByApi';
            var bodyType = active[type]?active[type].call(this,apiId):'';

            if(bodyType == 2){
                $('#newparamdiv').css("display", "none");
                //修改单选框的选择
                $("input[title='raw']").prop("checked",true);
                $("input[title='www-form']").removeAttr("checked");
                form.render('radio');
            }else{
                $('#newrawdiv').css("display", "none");
                form.render('radio');
            }

        });

    });
</script>

</body>