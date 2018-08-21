<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>忘记密码 - TBSMOCK</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/login.css" media="all">
</head>

<div class="layadmin-user-login layadmin-user-display-show" id="LAY-user-login" style="display: none;">
    <div class="layadmin-user-login-main">
        <div class="layadmin-user-login-box layadmin-user-login-header">
            <h2>TBS</h2>
            <p>找回密码</p>
        </div>
        <div class="layadmin-user-login-box layadmin-user-login-body layui-form">

            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-cellphone" for="LAY-user-login-cellphone"></label>
                <input type="text" name="userAccount" id="LAY-user-login-cellphone" lay-verify="required" placeholder="请输入注册时的工号" class="layui-input">
            </div>
            <div class="layui-form-item">
                <div class="layui-row">
                    <div>
                        <label class="layadmin-user-login-icon layui-icon layui-icon-vercode" for="LAY-user-login-vercode"></label>
                        <input type="text" name="userName" id="LAY-user-login-vercode" lay-verify="required" placeholder="请输入注册时的姓名" class="layui-input">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="LAY-user-forget-submit">找回密码</button>
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
    }).use(['index', 'form','jquery'], function(){
        var $ = layui.jquery
                ,form = layui.form;

        form.render();

        form.on('submit(LAY-user-forget-submit)', function(obj){
            var field = obj.field;

            //请求接口
            $.ajax({
                url: "${mainDomain}aps/user/checkforget.action" //实际使用请改成服务端真实接口
                ,type:"post"
                ,dataType:"json"
                ,data: field
                ,success: function(res){

                    if(res.state==207){
                        return layer.msg('账号或者用户名不正确，请核实');
                    }

                    layer.msg('校验成功，请重置密码', {
                        offset: '50px'
                        ,icon: 1
                        ,time: 1000
                    }, function(){
                        location.href = '${mainDomain}aps/user/reset?userAccount='+field.userAccount; //跳转到密码重置页面
                    });
                }
            });

            return false;
        });

    });
</script>
</body>
</html>