<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>项目信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
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

        .layui-text {
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
          <a href="${mainDomain}aps/interface/myinterface?projectid=${projectid}&userAccount=${userAccount}&userRole=${userRole}&page=1&limit=10">接口操作</a>
          <a><cite>MOCK</cite></a>
        </span>
        </div>

        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header layui-text" style="font-size: 20px;">MOCK测试</div>
                <div class="layui-card-body">

                    <div class="layui-tab layui-tab-brief" lay-filter="docTabBrief">
                        <ul class="layui-tab-title">
                            <li class="layui-this">预览信息</li>
                            <li>MOCK配置</li>
                            <li>MOCK测试</li>
                        </ul>
                        <div class="layui-tab-content">
                            <div class="layui-tab-item layui-show">
                                <div class="layui-card">
                                    <div class="layui-card-header layui-text"
                                         style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2">
                                        基本信息
                                    </div>
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
                                                <td>接口路径：&nbsp;&nbsp;&nbsp;<span class="layui-badge"
                                                                                 id="apiMethod-now">${apiMethod}</span>&nbsp;&nbsp;&nbsp;${apiPath}
                                                </td>
                                                <td>MOCK地址：&nbsp;&nbsp;&nbsp;${miscDomain}${apiPath}</td>
                                            </tr>
                                            <tr>
                                                <td>接口状态：&nbsp;&nbsp;&nbsp;<span
                                                        class="layui-badge-dot"></span>&nbsp;${isActive}</td>
                                                <td>创建时间：&nbsp;&nbsp;&nbsp;${createTime}</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div class="layui-tab-item">
                                <form class="layui-form" action="">

                                    <div class="layui-card">
                                        <div class="layui-card-header layui-text"
                                             style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2">
                                            基本设置
                                        </div>
                                        <div class="layui-card-body" id="apiInfo">
                                            <!-- <form class="layui-form" action=""> -->
                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i
                                                        style="color:red">*</i>&nbsp;接口名称：</label>
                                                <div class="layui-input-block">
                                                    <input type="text" name="intername" lay-verify="required"
                                                           autocomplete="off" placeholder="请输入接口名称" class="layui-input focusoutin"
                                                           value="${apiName}">
                                                </div>
                                            </div>

                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i
                                                        style="color:red">*</i>&nbsp;接口方法：</label>
                                                <div class="layui-input-block">
                                                    <select id="interfacemethod" name="intermetdod" lay-filter="method"
                                                            style="width: 300px">
                                                        <option value="">请选择接口方法</option>
                                                        <option value="GET">GET</option>
                                                        <option value="POST">POST</option>

                                                    </select>
                                                </div>
                                            </div>

                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i
                                                        style="color:red">*</i>&nbsp;接口路径：</label>
                                                <div class="layui-input-block">
                                                    <input type="text" name="interpath" lay-verify="required"
                                                           autocomplete="off" placeholder="路径必须以/开头" class="layui-input focusoutin"
                                                           value="${apiPath}">
                                                </div>
                                            </div>

                                            <div class="layui-form-item">
                                                <label class="layui-form-label"><i
                                                        style="color:red">*</i>&nbsp;接口状态：</label>
                                                <div class="layui-input-block">
                                                    <input type="checkbox" name="interstate" lay-skin="switch"
                                                           lay-text="开启|关闭" checked lay-filter="encrypt">
                                                </div>
                                            </div>

                                            <!-- <div class="layui-form-item">
                                              <div class="layui-input-block">
                                                <button class="layui-btn" lay-submit="" lay-filter="basicinf">提交保存</button>
                                              </div>
                                            </div> -->
                                            <!-- </form> -->


                                        </div>
                                    </div>

                                    <div class="layui-card">
                                        <div class="layui-card-header layui-text"
                                             style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2">
                                            MOCK参数配置
                                        </div>
                                        <div class="layui-card-body">
                                            <div class="layui-form " lay-filter="addrspparam" id="root-item"
                                                 data-apiid="${apiId}">
                                                <div class="layui-form-item" data-nodeid="${rootId}">
                                                    <div class="layui-input-inline">
                                                        <input type="text" name="rspparamname" id="id${rootId}"
                                                               autocomplete="off" placeholder="root"
                                                               class="layui-input layui-disabled"
                                                               style="width: 100%;float:right;" disabled>
                                                    </div>
                                                    <div class="layui-input-inline">
                                                        <input type="text" name="rspparamvalue" autocomplete="off"
                                                               placeholder="root"
                                                               class="layui-input layui-disabled input-value" disabled>
                                                    </div>
                                                    <div class="layui-input-inline">
                                                        <select id="${rootId}" name="rspparamneccroot" lay-filter="rspobj">
                                                            <option value="object">object</option>
                                                            <option value="array">array</option>
                                                        </select>
                                                    </div>
                                                    <div class="layui-input-inline">
                                                        <input type="text" name="rspparamdesc" autocomplete="off"
                                                               placeholder="描述" class="layui-input layui-disabled"
                                                               disabled>
                                                    </div>
                                                    <button class="layui-btn layui-btn-radius layui-btn-xs newmockparamc"
                                                            type="button" data-id="${rootId}" data-type="rspparamreload"
                                                            data-fatherid="-1">子节点
                                                    </button>
                                                </div>

                                                <!--根据服务器返回的数据进行展示-->
                                                <#if ((fieldNewParamInfos![])?size gt 0)>
                                                    <#list fieldNewParamInfos as fieldNewParamInfo>
                                                        <div class='layui-form-item'
                                                             data-nodeid='${fieldNewParamInfo.rowId}'
                                                             data-fatherid='${fieldNewParamInfo.fatherId}'>
                                                            <div class='layui-input-inline'>
                                                                <input type='text' name='1${fieldNewParamInfo.rowId}'
                                                                       value='${fieldNewParamInfo.fieldName}'
                                                                       lay-verify='required'
                                                                       id='id${fieldNewParamInfo.rowId}'
                                                                       autocomplete='off' placeholder='参数名称'
                                                                       data-type='updatereqexc'
                                                                       class='layui-input inputfocus'
                                                                       style='width: ${fieldNewParamInfo.rowWidth}%;float:right;'>
                                                            </div>
                                                            <div class='layui-input-inline'>
                                                                <input type='text' name='2${fieldNewParamInfo.rowId}'
                                                                       value='${fieldNewParamInfo.fieldValue}'
                                                                       autocomplete='off' placeholder='参数值'
                                                                       data-type='updatereqexc'
                                                                       class='layui-input input-value inputfocus'>
                                                            </div>
                                                            <div class='layui-input-inline'>
                                                                <select id='${fieldNewParamInfo.rowId}'
                                                                        data-optype='${fieldNewParamInfo.dataType}'
                                                                        name='rspparamnecc' lay-filter='rspobj'>
                                                                    <option value='object'>object</option>
                                                                    <option value='array'>array</option>
                                                                    <option value='string'>string</option>
                                                                    <option value='integer'>integer</option>
                                                                    <option value='float'>float</option>
                                                                    <option value='boolean'>boolean</option>
                                                                </select>
                                                            </div>
                                                            <div class='layui-input-inline'>
                                                                <input type='text' name='3${fieldNewParamInfo.rowId}'
                                                                       value='${fieldNewParamInfo.fieldDesc}'
                                                                       autocomplete='off' placeholder='描述'
                                                                       data-type='updatereqexc'
                                                                       class='layui-input inputfocus'>
                                                            </div>
                                                            <button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc'
                                                                    type='button' data-id='${fieldNewParamInfo.rowId}'
                                                                    data-type='rspparamreload'
                                                                    data-nodeid='child${fieldNewParamInfo.rowId}'>子节点
                                                            </button>
                                                            <button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc'
                                                                    type='button' data-id='${fieldNewParamInfo.rowId}'
                                                                    data-type='rspbroparamreload'
                                                                    data-nodeid='brother${fieldNewParamInfo.rowId}'>兄节点
                                                            </button>
                                                            <button class='layui-btn layui-btn-xs layui-btn-radius layui-btn-danger deletemockparam'
                                                                    type='button' data-type='deletemock'
                                                                    data-id='${fieldNewParamInfo.rowId}'>
                                                                <i class='layui-icon'>&#xe640;</i>
                                                            </button>
                                                        </div>
                                                    </#list>
                                                </#if>


                                            </div>

                                            <div>
                                                <hr class="layui-bg-green">
                                                <div class="layui-form-item">
                                                    <button class="layui-btn layui-disabled" lay-submit="" lay-filter="intersub" disabled>提交保存
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </form>
                            </div>
                            <div class="layui-tab-item">


                                <div class="layui-card">
                                    <div class="layui-card-header layui-text"
                                         style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2">
                                        运行MOCK
                                    </div>
                                    <div class="layui-card-body">

                                        <div class="layui-form layui-form-pane" id="response-man">
                                            <div class="layui-form-item">
                                                <div class="layui-input-inline">
                                                    <select id="responsetype" name="responsetype"
                                                            lay-filter="responsetype">
                                                        <option value="">请选择消息体类型</option>
                                                        <option value="JSON">JSON</option>
                                                        <option value="XML">XML</option>
                                                        <option value="JSONP">JSONP</option>
                                                    </select>
                                                </div>
                                                <div class="layui-input-inline" style="width: 500px;">
                                                    <input type="text" id="mockurl" name="mockurl"
                                                           value="${miscDomain}/mockserver${apiPath}" autocomplete="off"
                                                           disabled="true" class="layui-input">
                                                </div>
                                                <div class="layui-input-inline" style="width: 90px;display:none;">
                                                    <input type="text" id="jpfuntion" name="jpfuntion" value=""
                                                           placeholder="jsonp函数名" autocomplete="off"
                                                           class="layui-input">
                                                </div>
                                                <div class="layui-input-inline" style="width: 80px;">
                                                    <button class="layui-btn layui-btn-radius" type="button"
                                                            id="mocksubmit" data-type="mockexcu" style="float:right;">运行
                                                    </button>
                                                </div>
                                            </div>
                                            <hr class="layui-bg-green">

                                            <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
                                                <ul class="layui-tab-title">
                                                    <li class="layui-this">运行结果</li>
                                                </ul>
                                                <div class="layui-tab-content">
                                                    <div class="layui-tab-item layui-show">
                                                        <textarea name="mockresult" class="layui-textarea"
                                                                  style="width: 800px;height: 400px"></textarea>
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
    }).use(['index', 'table', 'layer', 'jquery', 'form', 'element'], function () {
        var layer = layui.layer
                , $ = layui.jquery
                , table = layui.table
                , form = layui.form
                , element = layui.element;

        //var userList = [];//定义空数组存储用户的选择数据
        var mockResponseType = null;
        var inputOldVal = null;

        //渲染接口表格
        table.init('basicinfo', {
            skin: 'nob'
        });


        //监听Tab页面的点击
        element.on('tab(docTabBrief)',function (data) {
            //console.log(this);
            console.log(data.index);
            if(data.index == 0){

            }else if(data.index == 2){
                var newValPath = $("input[name='interpath']").val();
                $("#mockurl").val("${miscDomain}/mockserver"+newValPath);
            }
        });

        //监听mockRun下拉菜单
        form.on('select(responsetype)', function (data) {
            mockResponseType = data.value;
            if (mockResponseType == "JSONP") {
                $("#jpfuntion").parent().prop("style", "width: 90px;");
            } else {
                $("#jpfuntion").parent().prop("style", "width: 90px;display:none;");
            }
        });

        //监听数据体下拉菜单
        form.on('select', function (data) {
            //获取name输入框对象
            var targetname = $(data.elem).parent().prev().prev().children();
            //获取value输入框对象
            var targetinput = $(data.elem).parent().prev().children();
            //获取按钮对象
            var targetbutton = $(data.elem).parent().next().next();
            var rowId = targetbutton.data("id");
            //获取整体layui-form-item对象
            var layuiItem = $(data.elem).parent().parent();

            var excuteUpdate = function (rowId, dataType) {
                switch (dataType) {
                    case "object":
                        if (active.updateRowDataType.call(this, rowId, 5) != 0) {
                            return -1;
                        }
                        ;
                        break;
                    case "array":
                        if (active.updateRowDataType.call(this, rowId, 6) != 0) {
                            return -1;
                        }
                        ;
                        break;
                    case "string":
                        if (active.updateRowDataType.call(this, rowId, 1) != 0) {
                            return -1;
                        }
                        ;
                        break;
                    case "float":
                        if (active.updateRowDataType.call(this, rowId, 3) != 0) {
                            return -1;
                        }
                        ;
                        break;
                    case "integer":
                        if (active.updateRowDataType.call(this, rowId, 2) != 0) {
                            return -1;
                        }
                        ;
                        break;
                    case "boolean":
                        if (active.updateRowDataType.call(this, rowId, 4) != 0) {
                            return -1;
                        }
                        ;
                        break;
                    default:
                        return -1;

                }
            };

            switch (data.value) {
                case "object":


                    if (excuteUpdate(rowId, "object") == -1) {
                        return;
                    }

                    if ($("#" + layuiItem.data("fatherid")).val() === "array") {
                        //如果父节点是array，name输入框也需要禁用
                        targetname.val("");
                        /*targetinput.attr("disabled",true);
                        targetinput.attr("class","layui-input layui-disabled");*/
                        targetname.attr("disabled", true);
                        targetname.attr("class", "layui-input layui-disabled");
                    }
                    //如果为object则value输入框禁用，且需要清空具体的值（需要后端同步处理）
                    targetinput.val("");
                    targetinput.attr("disabled", true);
                    targetinput.attr("class", "layui-input layui-disabled");

                    //新增子对象按钮可用
                    targetbutton.attr("class", "layui-btn layui-btn-radius layui-btn-xs newmockparamc");
                    //清空原来的值，如果是root节点，则不需要做这个操作
                    if (targetname.prop("name") != "rspparamname") {
                        active.updatereqexc.call(this, targetname.prop("name"), "");
                    }
                    if (targetinput.prop("name") != "rspparamvalue") {
                        active.updatereqexc.call(this, targetinput.prop("name"), "");
                    }

                    break;
                case "array":

                    if (excuteUpdate(rowId, "array") == -1) {
                        return;
                    }

                    if ($("#" + layuiItem.data("fatherid")).val() === "array") {
                        //如果父节点是array，name输入框也需要禁用
                        targetname.val("");
                        /*targetinput.attr("disabled",true);
                        targetinput.attr("class","layui-input layui-disabled");*/
                        targetname.attr("disabled", true);
                        targetname.attr("class", "layui-input layui-disabled");
                    }

                    //如果为array则value输入框禁用，且需要清空具体的值（需要后端同步处理）
                    targetinput.val("");
                    targetinput.attr("disabled", true);
                    targetinput.attr("class", "layui-input layui-disabled");

                    //新增子对象按钮可用
                    targetbutton.attr("class", "layui-btn layui-btn-radius layui-btn-xs newmockparamc");
                    //清空原来的值，如果是root节点，则不需要做这个操作
                    if (targetname.prop("name") != "rspparamname") {
                        active.updatereqexc.call(this, targetname.prop("name"), "");
                    }
                    if (targetinput.prop("name") != "rspparamvalue") {
                        active.updatereqexc.call(this, targetinput.prop("name"), "");
                    }

                    break;
                case "string":

                    if (active.getAllChildNodeId.call(this, $(data.elem).attr("id")).length > 0) {
                        //如果原数据类型下有子节点，则需要将下拉效果复原
                        if ($(data.elem).data("optype") === 5) {
                            $(data.elem).find("option[value='object']").prop("selected", true);
                        } else if ($(data.elem).data("optype") === 6) {
                            $(data.elem).find("option[value='array']").prop("selected", true);
                        }
                        form.render('select');
                        return layer.msg("请先删除节点下的子节点");
                    }

                    if (excuteUpdate(rowId, "string") == -1) {
                        return;
                    }

                    //如果父节点为array，那么name输入框需要禁用
                    if ($("#" + layuiItem.data("fatherid")).val() === "array") {
                        targetinput.attr("class", "layui-input inputfocus");
                        targetinput.attr("disabled", false);
                    } else {
                        //输入框可用
                        targetinput.attr("class", "layui-input inputfocus");
                        targetinput.attr("disabled", false);
                        targetname.attr("class", "layui-input inputfocus");
                        targetname.attr("disabled", false);
                    }

                    //新增子对象按钮禁用
                    targetbutton.attr("class", "layui-btn layui-btn-radius layui-btn-xs layui-btn-disabled");

                    break;
                case "float":

                    if (active.getAllChildNodeId.call(this, $(data.elem).attr("id")).length > 0) {
                        if ($(data.elem).data("optype") === 5) {
                            $(data.elem).find("option[value='object']").prop("selected", true);
                        } else if ($(data.elem).data("optype") === 6) {
                            $(data.elem).find("option[value='array']").prop("selected", true);
                        }
                        form.render('select');
                        return layer.msg("请先删除节点下的子节点");
                    }

                    if (excuteUpdate(rowId, "float") == -1) {
                        return;
                    }

                    //如果父节点为array，那么name输入框需要禁用
                    if ($("#" + layuiItem.data("fatherid")).val() === "array") {
                        targetinput.attr("class", "layui-input inputfocus");
                        targetinput.attr("disabled", false);
                    } else {
                        //输入框可用
                        targetinput.attr("class", "layui-input inputfocus");
                        targetinput.attr("disabled", false);
                        targetname.attr("class", "layui-input inputfocus");
                        targetname.attr("disabled", false);
                    }

                    //新增子对象按钮禁用
                    targetbutton.attr("class", "layui-btn layui-btn-radius layui-btn-xs layui-btn-disabled");
                    break;
                case "integer":

                    if (active.getAllChildNodeId.call(this, $(data.elem).attr("id")).length > 0) {
                        if ($(data.elem).data("optype") === 5) {
                            $(data.elem).find("option[value='object']").prop("selected", true);
                        } else if ($(data.elem).data("optype") === 6) {
                            $(data.elem).find("option[value='array']").prop("selected", true);
                        }
                        form.render('select');
                        return layer.msg("请先删除节点下的子节点");
                    }

                    if (excuteUpdate(rowId, "integer") == -1) {
                        return;
                    }

                    //如果父节点为array，那么name输入框需要禁用
                    if ($("#" + layuiItem.data("fatherid")).val() === "array") {
                        targetinput.attr("class", "layui-input inputfocus");
                        targetinput.attr("disabled", false);
                    } else {
                        //输入框可用
                        targetinput.attr("class", "layui-input inputfocus");
                        targetinput.attr("disabled", false);
                        targetname.attr("class", "layui-input inputfocus");
                        targetname.attr("disabled", false);
                    }

                    //新增子对象按钮禁用
                    targetbutton.attr("class", "layui-btn layui-btn-radius layui-btn-xs layui-btn-disabled");
                    break;
                case "boolean":

                    if (active.getAllChildNodeId.call(this, $(data.elem).attr("id")).length > 0) {
                        if ($(data.elem).data("optype") === 5) {
                            $(data.elem).find("option[value='object']").prop("selected", true);
                        } else if ($(data.elem).data("optype") === 6) {
                            $(data.elem).find("option[value='array']").prop("selected", true);
                        }
                        form.render('select');
                        return layer.msg("请先删除节点下的子节点");
                    }

                    if (excuteUpdate(rowId, "boolean") == -1) {
                        return;
                    }

                    //如果父节点为array，那么name输入框需要禁用
                    if ($("#" + layuiItem.data("fatherid")).val() === "array") {
                        targetinput.attr("class", "layui-input inputfocus");
                        targetinput.attr("disabled", false);
                    } else {
                        //输入框可用
                        targetinput.attr("class", "layui-input inputfocus");
                        targetinput.attr("disabled", false);
                        targetname.attr("class", "layui-input inputfocus");
                        targetname.attr("disabled", false);
                    }

                    //新增子对象按钮禁用
                    targetbutton.attr("class", "layui-btn layui-btn-radius layui-btn-xs layui-btn-disabled");
                    break;
                default:
                    break;
            }

            form.render();
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

            getRandom: function (num) {
                var random = Math.floor((Math.random() + Math.floor(Math.random() * 9 + 1)) * Math.pow(10, num - 1));
                return random;
            }

            //获取数据行的最大id值
            , getMaxRowId: function () {
                var maxRowId = null;
                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/getfieldrowmaxid.action" //提交后台处理
                            , type: "post"
                            , dataType: "json"
                            , async: false//禁止异步，这样可以操作maxRowId变量
                            //,data:paramData
                            , success: function (resp) {
                                maxRowId = resp.data.maxId;

                            }
                            , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if (XMLHttpRequest.status != 200) {
                                    return layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );
                return maxRowId;
            }

            //获取当前节点id下面的所有子节点id
            , getAllChildNodeId: function (currentNodeId) {
                var childNodeList = [];
                var paramData = {currentNodeId: currentNodeId};
                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/getallchildnodeid.action" //提交后台处理
                            , type: "post"
                            , dataType: "json"
                            , async: false//禁止异步，这样可以操作maxRowId变量
                            , data: paramData
                            , success: function (resp) {
                                var temp = resp.data.childIds;
                                for (let index in temp) {
                                    childNodeList.push(temp[index])
                                }

                            }
                            , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if (XMLHttpRequest.status != 200) {
                                    return layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );

                return childNodeList;
            }

            //获取当前节点的fatherid
            , getFatherNodeId: function (currentNodeId) {

                var fatherId = null;
                var paramData = {currentNodeId: currentNodeId};
                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/getfatherid.action" //提交后台处理
                            , type: "post"
                            , dataType: "json"
                            , async: false//禁止异步，这样可以操作maxRowId变量
                            , data: paramData
                            , success: function (resp) {

                                fatherId = resp.data.fatherId;
                            }
                            , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if (XMLHttpRequest.status != 200) {
                                    return layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );

                return fatherId;
            }

            //获取root节点的rootType
            ,getRootNodeType:function (apiId) {

                var rootType = 0;
                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/getroottype.action" //提交后台处理
                            , type: "post"
                            , dataType: "json"
                            , async: false
                            , data: {apiId:apiId}
                            , success: function (resp) {

                                rootType = resp.data.rootType;
                            }
                            , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if (XMLHttpRequest.status != 200) {
                                    return layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );

                return rootType;

            }

            //新增行数据
            , addNewNode: function (rowData) {
                var result = 0;

                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/insertnodedata.action" //提交后台处理
                            , type: "post"
                            , dataType: "json"
                            , async: false//禁止异步
                            , data: rowData
                            , success: function (resp) {
                                if (resp.state === -12003) {
                                    layer.msg('服务内部处理异常，请稍后再试');
                                    result = -1;
                                } else if (resp.state === -11000) {
                                    layer.msg('服务内部HTTP调用异常，请稍后再试');
                                    result = -2;
                                }

                            }
                            , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if (XMLHttpRequest.status != 200) {
                                    return layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );
                return result;
            }


            //更新域值中的数据
            , updatereqexc: function (rowid, newValue) {

                //获取字段类型，第一个字符
                var fieldty = rowid.slice(0, 1);
                //获取rowid，第二个字符开始到结束
                var newRowid = rowid.slice(1);

                //构造请求参数
                var fieldNewParamInfo = {};
                fieldNewParamInfo.fieldType = fieldty;
                fieldNewParamInfo.rowId = newRowid;
                if (fieldty == 1) {
                    fieldNewParamInfo.fieldName = newValue;
                } else if (fieldty == 2) {
                    fieldNewParamInfo.fieldValue = newValue;
                } else if (fieldty == 3) {
                    fieldNewParamInfo.fieldDesc = newValue;
                }

                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/updatefieldvalue.action" //提交后台处理
                            , type: "post"
                            , dataType: "json"
                            //,async:false//禁止异步，这样可以操作re变量
                            , data: fieldNewParamInfo
                            , success: function (resp) {
                                if (resp.state === -12003) {
                                    return layer.msg('服务内部处理异常，请稍后再试');

                                } else if (resp.state === -11000) {
                                    return layer.msg('服务内部HTTP调用异常，请稍后再试');

                                }
                            }
                            , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if (XMLHttpRequest.status != 200) {
                                    return layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );

            }

            //
            , updateRowDataType: function (rowid, newValue) {

                var result = 0;

                var fieldNewParamInfo = {};
                fieldNewParamInfo.dataType = newValue;
                fieldNewParamInfo.rowId = rowid;

                $.ajax(
                        {
                            url: "${mainDomain}aps/interface/updatefieldRowDataType.action" //提交后台处理
                            , type: "post"
                            , dataType: "json"
                            //,async:false//禁止异步，这样可以操作re变量
                            , data: fieldNewParamInfo
                            , success: function (resp) {
                                if (resp.state === -12003) {
                                    layer.msg('服务内部处理异常，请稍后再试');
                                    result = -1;
                                } else if (resp.state === -11000) {
                                    layer.msg('服务内部HTTP调用异常，请稍后再试');
                                    result = -2;
                                }
                            }
                            , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if (XMLHttpRequest.status != 200) {
                                    layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                            }
                        }
                );
                return result;
            }

            //mock子节点新增，需要两个参数：（当前节点id，子节点的排头输入框的宽度值）
            , rspparamreload: function (currentid, childWidth, selectVal) {

                var childNodeId = active.getMaxRowId.call(this) + 1;

                //定义需要提交后台的数据
                var rowData = {
                    id: childNodeId,
                    fatherId: currentid,
                    apiId: $('#root-item').data('apiid'),
                    rowWidth: childWidth
                };


                //生成子节点的HTML
                //如果当前节点为object，需要禁用子节点的val输入框，如果为array需要禁用子节点的name和val输入框
                if (selectVal === "object") {
                    var childFormHtml = "<div class='layui-form-item' data-nodeid='" + childNodeId + "' data-fatherid='" + currentid + "'><div class='layui-input-inline'><input type='text' name='1" + childNodeId + "' value='' lay-verify='required' id='id" + childNodeId + "' autocomplete='off' placeholder='参数名称' data-type='updatereqexc' class='layui-input inputfocus' style='width: " + childWidth + "%;float:right;'></div><div class='layui-input-inline'><input type='text' name='2" + childNodeId + "' value='' autocomplete='off' placeholder='参数值' data-type='updatereqexc' class='layui-input input-value inputfocus layui-disabled' disabled'></div><div class='layui-input-inline'><select id='" + childNodeId + "' name='rspparamnecc' data-optype='' lay-filter='rspobj'><option value='object'>object</option><option value='array'>array</option><option value='string'>string</option><option value='integer'>integer</option><option value='float'>float</option><option value='boolean'>boolean</option></select></div><div class='layui-input-inline'><input type='text' name='3" + childNodeId + "' value='' autocomplete='off' placeholder='描述' data-type='updatereqexc' class='layui-input inputfocus'></div><button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc' type='button' data-id='" + childNodeId + "' data-type='rspparamreload' data-nodeid=''>子节点</button><button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc' type='button' data-id='" + childNodeId + "' data-type='rspbroparamreload' data-nodeid=''>兄节点</button><button class='layui-btn layui-btn-xs layui-btn-radius layui-btn-danger deletemockparam' type='button' data-type='deletemock' data-id='" + childNodeId + "'><i class='layui-icon'>&#xe640;</i></button></div>";

                } else if (selectVal === "array") {

                    var childFormHtml = "<div class='layui-form-item' data-nodeid='" + childNodeId + "' data-fatherid='" + currentid + "'><div class='layui-input-inline'><input type='text' name='1" + childNodeId + "' value='' lay-verify='required' id='id" + childNodeId + "' autocomplete='off' placeholder='参数名称' data-type='updatereqexc' class='layui-input inputfocus layui-disabled' style='width: " + childWidth + "%;float:right;' disabled></div><div class='layui-input-inline'><input type='text' name='2" + childNodeId + "' value='' autocomplete='off' placeholder='参数值' data-type='updatereqexc' class='layui-input input-value inputfocus layui-disabled' disabled></div><div class='layui-input-inline'><select id='" + childNodeId + "' name='rspparamnecc' data-optype='' lay-filter='rspobj'><option value='object'>object</option><option value='array'>array</option><option value='string'>string</option><option value='integer'>integer</option><option value='float'>float</option><option value='boolean'>boolean</option></select></div><div class='layui-input-inline'><input type='text' name='3" + childNodeId + "' value='' autocomplete='off' placeholder='描述' data-type='updatereqexc' class='layui-input inputfocus'></div><button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc' type='button' data-id='" + childNodeId + "' data-type='rspparamreload' data-nodeid=''>子节点</button><button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc' type='button' data-id='" + childNodeId + "' data-type='rspbroparamreload' data-nodeid=''>兄节点</button><button class='layui-btn layui-btn-xs layui-btn-radius layui-btn-danger deletemockparam' type='button' data-type='deletemock' data-id='" + childNodeId + "'><i class='layui-icon'>&#xe640;</i></button></div>";
                }

                var $formchild = $(childFormHtml);

                /*
                * 对子节点的位置进行设置，如果当前节点下面没有子节点，则将子节点新增在当前节点后面
                * 如果有子节点，则找出子节点中的最大值，将新增节点设置于最大值后面
                * 节点关系逻辑由后台提供
                * */
                var childIds = active.getAllChildNodeId.call(this, currentid);

                /*
                *执行数据提交到后台的操作，注意执行顺序：
                * 1、先保证原始数据的查询
                * 2、在执行数据提交，如果提交异常，则直接返回
                * 3、在进行DOM操作
                * */
                var result = active.addNewNode.call(this, rowData);
                if (result === -1 || result === -2) {
                    return;
                }

                if (childIds.length === 0) {
                    $("div[data-nodeid='" + currentid + "']").after($formchild);
                } else {
                    //如果子节点有多个，则对子节点数组进行排序，取最大值
                    var newchildIds = childIds.sort(function (x, y) {
                        if (x < y) {
                            return 1;
                        } else if (x > y) {
                            return -1;
                        }
                        return 0;
                    });
                    //排序后的第一个元素最大
                    var maxChildId = newchildIds[0];
                    $("div[data-nodeid='" + maxChildId + "']").after($formchild);

                }

                form.render(null, 'addrspparam');


            }

            //mock同级节点新增1111111111
            , rspbroparamreload: function (currentid, brotherWith, selectVal) {

                var brotherNodeId = active.getMaxRowId.call(this) + 1;
                var fatherId = active.getFatherNodeId.call(this, currentid);

                var rowData = {
                    id: brotherNodeId,
                    fatherId: fatherId,
                    apiId: $('#root-item').data('apiid'),
                    rowWidth: brotherWith
                };

                var result = active.addNewNode.call(this, rowData);
                if (result === -1) {
                    return;
                }

                if (selectVal === "object") {
                    var brotherHtml = "<div class='layui-form-item' data-nodeid='" + brotherNodeId + "' data-fatherid='" + fatherId + "'><div class='layui-input-inline'><input type='text' name='1" + brotherNodeId + "' value='' lay-verify='required' id='id" + brotherNodeId + "' autocomplete='off' placeholder='参数名称' data-type='updatereqexc' class='layui-input inputfocus' style='width: " + brotherWith + "%;float:right;'></div><div class='layui-input-inline'><input type='text' name='2" + brotherNodeId + "' value='' autocomplete='off' placeholder='参数值' data-type='updatereqexc' class='layui-input input-value inputfocus layui-disabled' disabled'></div><div class='layui-input-inline'><select id='" + brotherNodeId + "' name='rspparamnecc' data-optype='' lay-filter='rspobj'><option value='object'>object</option><option value='array'>array</option><option value='string'>string</option><option value='integer'>integer</option><option value='float'>float</option><option value='boolean'>boolean</option></select></div><div class='layui-input-inline'><input type='text' name='3" + brotherNodeId + "' value='' autocomplete='off' placeholder='描述' data-type='updatereqexc' class='layui-input inputfocus'></div><button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc' type='button' data-id='" + brotherNodeId + "' data-type='rspparamreload' data-nodeid=''>子节点</button><button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc' type='button' data-id='" + brotherNodeId + "' data-type='rspbroparamreload' data-nodeid=''>兄节点</button><button class='layui-btn layui-btn-xs layui-btn-radius layui-btn-danger deletemockparam' type='button' data-type='deletemock' data-id='" + brotherNodeId + "'><i class='layui-icon'>&#xe640;</i></button></div>";
                } else if (selectVal === "array") {
                    var brotherHtml = "<div class='layui-form-item' data-nodeid='" + brotherNodeId + "' data-fatherid='" + fatherId + "'><div class='layui-input-inline'><input type='text' name='1" + brotherNodeId + "' value='' lay-verify='required' id='id" + brotherNodeId + "' autocomplete='off' placeholder='参数名称' data-type='updatereqexc' class='layui-input inputfocus layui-disabled' style='width: " + brotherWith + "%;float:right;' disabled></div><div class='layui-input-inline'><input type='text' name='2" + brotherNodeId + "' value='' autocomplete='off' placeholder='参数值' data-type='updatereqexc' class='layui-input inputfocus input-value layui-disabled' disabled></div><div class='layui-input-inline'><select id='" + brotherNodeId + "' name='rspparamnecc' data-optype='' lay-filter='rspobj'><option value='object'>object</option><option value='array'>array</option><option value='string'>string</option><option value='integer'>integer</option><option value='float'>float</option><option value='boolean'>boolean</option></select></div><div class='layui-input-inline'><input type='text' name='3" + brotherNodeId + "' value='' autocomplete='off' placeholder='描述' data-type='updatereqexc' class='layui-input inputfocus'></div><button class='layui-btn  layui-btn-radius layui-btn-xs newmockparamc' type='button' data-id='" + brotherNodeId + "' data-type='rspparamreload' data-nodeid=''>子节点</button><button class='layui-btn layui-btn-radius layui-btn-xs newmockparamc' type='button' data-id='" + brotherNodeId + "' data-type='rspbroparamreload' data-nodeid=''>兄节点</button><button class='layui-btn layui-btn-xs layui-btn-radius layui-btn-danger deletemockparam' type='button' data-type='deletemock' data-id='" + brotherNodeId + "'><i class='layui-icon'>&#xe640;</i></button></div>";
                }


                var $formchild = $(brotherHtml);
                $("div[data-nodeid='" + currentid + "']").after($formchild);
                form.render(null, 'addrspparam');

            }

            , deletemock: function (currentnodeid) {

                //计算当前节点和所有逻辑子节点
                var allnodeids = [];
                allnodeids.push(currentnodeid);

                var childNodes = active.getAllChildNodeId.call(this, currentnodeid);
                var rowIdList = allnodeids.concat(childNodes);
                var rowIdsParam = {rowIds: rowIdList};

                var result = 0;

                layer.confirm('将会同时删除子节点？', function (index) {
                    //调用删除接口
                    $.ajax(
                            {
                                url: "${mainDomain}aps/interface/deletemockdata.action" //用户删除接口
                                , type: "post"
                                , dataType: "json"
                                , async: false
                                , traditional: true
                                , data: rowIdsParam
                                , success: function (resp) {
                                    if (resp.state === -12003) {
                                        layer.msg('服务内部处理异常，请稍后再试');
                                        result = -1;

                                    } else if (resp.state === -11000) {
                                        layer.msg('服务内部HTTP调用异常，请稍后再试');
                                        result = -2;

                                    }
                                    //加入项目的提示与跳转
                                    layer.msg('删除项目成功', {
                                        offset: '15px'
                                        , icon: 1
                                        , time: 1000
                                    });
                                }
                                , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                    // 状态码:XMLHttpRequest.status
                                    // 状态:XMLHttpRequest.readyState
                                    // 错误信息 :textStatus
                                    if (XMLHttpRequest.status != 200) {
                                        layer.msg('后台处理异常，请重试');
                                        result = -3;
                                        //re = false;
                                    }
                                }
                            });

                    if (result != 0) {
                        return;
                    }
                    //删除dom结构
                    for (var cnodeindex in rowIdList) {
                        $("div[data-nodeid='" + rowIdList[cnodeindex] + "']").remove();
                    }
                    form.render(null, 'addrspparam');
                    layer.close(index);
                });
            }

            , mockexcu: function (url, respType, jsonpName) {

                var urlResult = null;
                var AcceptContext = "text/html";
                var datatype = null;
                if (respType == 3) {
                    urlResult = "${mainDomain}mockserver" + url + ".action?callback=" + jsonpName;
                    datatype = "jsonp";
                } else {
                    urlResult = "${mainDomain}mockserver" + url + ".action"
                }

                if (respType == 1) {
                    AcceptContext = "application/json";
                    datatype = "json";
                } else if (respType == 2) {
                    AcceptContext = "application/xml";
                    datatype = "xml";
                }

                $.ajax(
                        {
                            url: urlResult //提交后台处理
                            , type: "post"
                            , dataType: datatype
                            , beforeSend: function (req) {
                                req.setRequestHeader("Accept", AcceptContext);
                            },
                            jsonpClaaback: jsonpName
                            , success: function (resp) {
                                if (resp.state === -12003) {
                                    layer.msg('服务内部处理异常，请稍后再试');

                                } else if (resp.state === -11000) {
                                    layer.msg('服务内部HTTP调用异常，请稍后再试');
                                }
                                if (respType == 1) {
                                    $("textarea[name='mockresult']").text(formatJson(JSON.stringify(resp)));
                                } else if (respType == 2) {
                                    //console.log(resp.documentElement.childNodes);
                                    var resultVar = new XMLSerializer().serializeToString(resp.documentElement);
                                    $("textarea[name='mockresult']").text(resultVar.substring(9, resultVar.length - 10));
                                } else {
                                    console.log(resp);
                                    $("textarea[name='mockresult']").text(JSON.stringify(resp));
                                }


                            }
                            , error: function (XMLHttpRequest, textStatus, errorThrown) {
                                // 状态码:XMLHttpRequest.status
                                // 状态:XMLHttpRequest.readyState
                                // 错误信息 :textStatus
                                if (XMLHttpRequest.status != 200) {
                                    layer.msg('后台处理异常，请重试');
                                    //re = false;
                                }
                                $("textarea[name='mockresult']").text(XMLHttpRequest.responseText);
                                /*
                                                                console.log(XMLHttpRequest.status);
                                                                console.log(XMLHttpRequest.readyState);
                                                                console.log(XMLHttpRequest.responseText);
                                                                console.log(textStatus);
                                                                console.log(errorThrown);*/
                            }
                        }
                );
            }

        };


        //执行新增mock字段
        $('#root-item').on('click', '.newmockparamc', function () {

            //需要利用冒泡进行事件捕获，同时，需要捕获目标的选择器，所以需要使用on('event','selector',fn)这种方式，selector用于获取实际的点击对象

            //var target = e.target;
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //获取当前button的id属性
            var currentid = $(this).data('id');
            //获取当前节点的父子关系id
            //var fatherid =  $(this).data('fatherid');
            //获取当前节点的排头输入框宽度（参数名）
            var currentWidth = $("#id" + currentid).attr('style');

            var childWidth = null;
            if (type === "rspparamreload") {
                //如果点击子节点，获取当前row节点的下拉菜单值
                var selectVal = $("#" + currentid).val();
                //排头宽度减少10
                childWidth = parseInt(currentWidth.replace(/[^0-9]/ig, "")) - 10;
            } else if (type === "rspbroparamreload") {
                //如果点击兄弟节点，获取父级row节点的下拉菜单值
                var fatherId = active.getFatherNodeId.call(this, currentid);
                var selectVal = $("#" + fatherId).val();
                //排头宽度不变
                childWidth = parseInt(currentWidth.replace(/[^0-9]/ig, ""));
            }

            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type] ? active[type].call(this, currentid, childWidth, selectVal) : '';
        });

        //监听删除mock节点按钮
        $('#root-item').on('click', '.deletemockparam', function () {
            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //获取当前节点的父子关系id
            var currentnodeid = $(this).data('id');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type] ? active[type].call(this, currentnodeid) : '';
        });

        //监听select进行更新
        form.on('select(method)', function (data) {
            var tag = "3";
            var field = null;
            var apiid = $('#root-item').data('apiid');
            if(data.value == "GET"){
                field = "1";
            }else if(data.value == "POST"){
                field = "2";
            }
            <#--location.href = '${mainDomain}aps/interface/updateapiinfo?tag='+tag+'&field='+field+'&apiid='+apiid;-->
            $.ajax({
                type: "post",
                url: "${mainDomain}aps/interface/updateapiinfo",
                data: {
                    "field": field,
                    "tag": tag,
                    "apiid": apiid
                },
                error:function () {
                    layer.msg('后台处理异常，请重试');
                }
            });
        });
        //监听switch进行更新
        form.on('switch(encrypt)', function(data){
            console.log(data.elem.checked); //开关是否开启，true或者false
            var tag = "4";
            var field = null;
            var apiid = $('#root-item').data('apiid');
            if(data.elem.checked==true){
                field = "1";
            }else if(data.elem.checked==false){
                field = "0";
            }
        <#--location.href = '${mainDomain}aps/interface/updateapiinfo?tag='+tag+'&field='+field+'&apiid='+apiid;-->
            $.ajax({
                type: "post",
                url: "${mainDomain}aps/interface/updateapiinfo",
                data: {
                    "field": field,
                    "tag": tag,
                    "apiid": apiid
                },
                error:function () {
                    layer.msg('后台处理异常，请重试');
                }
            });
        });

        // 获得焦点事件
        $("#apiInfo").on("focus", ".focusoutin",function () {
            if($(this).prop("name") == "intername"){
                inputOldVal = $("input[name='intername']").val();
            }else if($(this).prop("name") == "interpath"){
                inputOldVal = $("input[name='interpath']").val();
            }
        });
        //MOCK配置 接口名称更新

        $("#apiInfo").on("focusout", ".focusoutin",function () {
            var field = $(this).val();
            var tag = null;
            var reg = /^\//;
            if($(this).prop("name") == "intername"){
                tag = "1";
            }else if($(this).prop("name") == "interpath"){
                tag = "2";
                if((!reg.test(field))) {
                    layer.msg('路径需要以/开头');
                    setTimeout(function () {
                        $("input[name='interpath']").val(inputOldVal);
                    }, 1000);
                    return;
                }
            }
            var apiid = $('#root-item').data('apiid');

            if(field.length == 0){
                if($(this).prop("name") == "intername"){
                    layer.msg("接口名称不能为空");
                    setTimeout(function () {
                        $("input[name='intername']").val(inputOldVal);
                    },1000);
                    return;
                }
            }

            $.ajax({
                type: "post",
                url: "${mainDomain}aps/interface/updateapiinfo",
                data: {
                    "field": field,
                    "tag": tag,
                    "apiid": apiid
                },
                success: function (result) {
                    var obj = JSON.parse(result);
                    if(obj.state==404){
                        layer.msg(obj.message);
                        setTimeout(function () {
                            $("input[name='intername']").val(inputOldVal);
                        },1000);

                    }else if(obj.state==405){
                        layer.msg(obj.message);
                        setTimeout(function () {
                            $("input[name='interpath']").val(inputOldVal);
                        }, 1000);
                    }
                },
                error:function () {
                    layer.msg('后台处理异常，请重试');
                }

            });
        });


        //监听mock运行按钮
        $('#mocksubmit').on('click', function () {

            var respType = null;
            var url = $("input[name='interpath']").val();
            if (mockResponseType === "JSON") {
                respType = 1;
            } else if (mockResponseType === "XML") {
                respType = 2;
            } else if (mockResponseType === "JSONP") {
                respType = 3;
            } else if (mockResponseType === null) {
                return layer.msg("请选择返回体数据类型");
            }

            var jsonpName = $("#jpfuntion").val();


            //获取查询按钮元素中的data-type属性的值
            var type = $(this).data('type');
            //判断active对象是否存在type类型的属性，如果存在，则执行对应函数，否则不做任何事情
            active[type] ? active[type].call(this, url, respType, jsonpName) : '';
        });

        //监听输入框失去焦点事件，用于实时更新输入框的编辑
        $('#root-item').on('focusout', '.inputfocus', function () {
            var type = $(this).data('type');
            var rowId = $(this).attr('name');
            var newValue = $(this).val();

            active[type] ? active[type].call(this, rowId, newValue) : '';

        });

        //页面load完成加载事件
        $(document).ready(function () {

            /*显示接口方法的下拉值*/
            var selectNow = $("#apiMethod-now").text();
            if (selectNow === "GET") {
                $("#interfacemethod > option[value='GET']").prop("selected", true);
            } else if (selectNow === "POST") {
                $("#interfacemethod > option[value='POST']").prop("selected", true);
            }
            form.render('select');

            //显示root节点的下拉菜单
            var apiid = $('#root-item').data('apiid');
            if(active.getRootNodeType.call(this,apiid) == 5){
                $("select[name='rspparamneccroot'] > option[value='object']").prop("selected", true);
            }else if(active.getRootNodeType.call(this,apiid) == 6){
                $("select[name='rspparamneccroot'] > option[value='array']").prop("selected", true);
            }


            /*操作mock字段中的下拉菜单*/
            var selectList = $("select[name='rspparamnecc']");
            selectList.each(function (index) {
                var currentId = $(this).attr("id");
                var currentId2 = "2" + currentId;

                switch ($(this).data("optype")) {
                    case 1:
                        $(this).find("option[value='string']").prop("selected", true);

                        //新增子对象按钮禁用
                        $("button[data-nodeid='child" + currentId + "']").prop("class", "layui-btn layui-btn-radius layui-btn-xs layui-btn-disabled");
                        break;
                    case 2:
                        $(this).find("option[value='integer']").prop("selected", true);

                        $("button[data-nodeid='child" + currentId + "']").prop("class", "layui-btn layui-btn-radius layui-btn-xs layui-btn-disabled");
                        break;
                    case 3:
                        $(this).find("option[value='float']").prop("selected", true);

                        $("button[data-nodeid='child" + currentId + "']").prop("class", "layui-btn layui-btn-radius layui-btn-xs layui-btn-disabled");
                        break;
                    case 4:
                        $(this).find("option[value='boolean']").prop("selected", true);

                        $("button[data-nodeid='child" + currentId + "']").prop("class", "layui-btn layui-btn-radius layui-btn-xs layui-btn-disabled");
                        break;
                    case 5:
                        //修改select选项
                        $(this).find("option[value='object']").prop("selected", true);
                        //当选项为object时，禁用value输入框
                        $("input[name='" + currentId2 + "']").prop("class", "layui-input input-value layui-disabled");
                        $("input[name='" + currentId2 + "']").prop("disabled", true);
                        break;
                    case 6:
                        $(this).find("option[value='array']").prop("selected", true);

                        //当选项为List时，禁用value输入框
                        $("input[name='" + currentId2 + "']").prop("class", "layui-input input-value layui-disabled");
                        $("input[name='" + currentId2 + "']").prop("disabled", true);
                        break;
                    default:
                        break;
                }
                form.render('select');
            });

            //遍历所有的节点。禁用array节点下的输入框
            var allNode = $("#root-item").children();
            allNode.each(function (index) {
                var fid = $(this).data("fatherid");
                var cid = $(this).data("nodeid");
                if ($("#" + fid).val() === "array") {
                    $(this).find("#id" + cid).prop("class", "layui-input input-value layui-disabled");
                    $(this).find("#id" + cid).prop("disabled", true);
                }
            });

        });
    });
</script>
</body>
</html>
