<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>基本资料</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/login.css" media="all">
</head>
<body>
<div class="layui-card">
    <div class="layui-card-header layui-text" style="font-size: 20px;border-left: 3px solid #009688;background-color:#f2f2f2;line-height:50px">
        基本资料（可修改用户账号和用户名）
    </div>
    <div class="layui-card-body">
        <!-- <form class="layui-form" action=""> -->
        <div class="layui-form-item">
            <label class="layui-form-label" >&nbsp;用户ID：</label>
            <div class="layui-input-block">
                <input type="text"  readonly="readonly" name="intername" lay-verify="required" autocomplete="off" placeholder="不支持输入" class="layui-input focusoutin" value="${userId}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">&nbsp;用户账号：</label>
            <div class="layui-input-block">
                <input type="text" id="modifyUserAccountin" style="width:93%;float: left"  name="intername" lay-verify="required" autocomplete="off" placeholder="请输入想修改的用户账号" class="layui-input focusoutin" value="${userAccount}">
                <button id="modifyUserAccount" class="layui-btn layui-btn-normal" style="float:right;text-align:center">修改</button>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label" >&nbsp;用户名：</label>
            <div class="layui-input-block">
                <input type="text" id="modifyUserNamein" style="width:93%;float: left" name="intername" lay-verify="required" autocomplete="off" placeholder="请输入想修改的用户名" class="layui-input focusoutin" value="${userName}">
                <button id="modifyUserName" class="layui-btn layui-btn-normal" style="float:right;text-align:center">修改</button>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">&nbsp;用户状态：</label>
            <div class="layui-input-block" id="modifyIsActive">
                <input type="text"  readonly="readonly" name="intername" lay-verify="required" autocomplete="off" placeholder="不支持输入" class="layui-input focusoutin" value="${isActive}">
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
    }).use(['index','form','jquery'],function () {//use方法用于加载layui模块
        var $ = layui.jquery
                , form = layui.form;
        var fieldold;
        var fieldoldname;

        //获得用户账号输入框中的值
        $("#modifyUserAccount").ready(function () {
            fieldold = $("#modifyUserAccountin").val();
        });

        //点击用户账号修改按钮后调用接口，修改新用户账号
        $("#modifyUserAccount").click(function () {
            var field = $("#modifyUserAccountin").val();
            field = field.trim();
            if(field==""){
                layer.msg('用户账号不能为空',{offset: '15px',time: 2000,icon: 2});
            }else if(fieldold==field){
                layer.msg('内容未变更，请变更后再进行修改',{offset: '15px',time: 2000,icon: 2});
            }else {
                var accountdata={"userId":${userId},"newUserAccount":field}
                $.ajax({
                    url: "${mainDomain}aps/user/queryAccountCount" //修改用户名接口
                    , type: "post"
                    , dataType: "json"
                    , async: false
                    , traditional: true
                    , data: accountdata
                    , success: function (resp) {
                        console.log(resp.data.count);
                        if(resp.data.count==0){
                            layer.confirm('确认后，将修该用户账号，原账号失效，需要使用新账号重新登录，是否确认修改?', {icon: 3, title: '提示'}, function (index) {
                                layer.msg('修改成功,将跳转到登录页面，请使用新账号重新登录！',{offset: '15px',icon: 1,time:3000},function(){
                                    location.href = '${mainDomain}aps/user/updateAccount?userAccount='+field+'&userId='+${userId};
                                    top.location.href = '${mainDomain}aps/user/logout';
                                });
                                layer.close(index);
                            });

                        }else{
                            layer.msg('用户账号已存在，请更换其他账号',{offset: '15px',time: 2000,icon: 2});
                        }

                    }
                });




            }
        });

        //获得用户名输入框中的值
        $("#modifyUserName").ready(function () {
            fieldoldname = $("#modifyUserNamein").val();
        });
        //点击用户名修改按钮调用，修改用户名
        $("#modifyUserName").click(function(){
            var fieldname = $("#modifyUserNamein").val();
            fieldname = fieldname.trim();
            if(fieldname==""){
                layer.msg('用户名不能为空',{offset: '15px',time: 2000,icon: 2});
            }else if(fieldoldname==fieldname){
                layer.msg('内容未变更，请变更后再进行修改',{offset: '15px',time: 2000,icon: 2});
            }else{
                var userinfodata ={"userId":${userId},"userName":fieldname};
                $.ajax({
                    url: "${mainDomain}aps/user/updateUserNameById" //修改用户名接口
                    , type: "post"
                    , dataType: "json"
                    , async: false
                    , traditional: true
                    , data: userinfodata
                    , success: function (resp) {
                        if(resp.state=="1000"){
                            layer.msg('用户名修改成功',{offset: '15px',time: 2000,icon: 1},function(){
                                location.reload();
                            });

                        }else{
                            layer.msg('用户名修改失败',{offset: '15px',time: 2000,icon: 2});
                        }

                    }
                });
            }
        });


    });

</script>
</body>
</html>