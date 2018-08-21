
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>忘记密码 - APSMOCK</title>
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
            <p>重置密码</p>
        </div>
        <div class="layadmin-user-login-box layadmin-user-login-body layui-form">
            <div>
                <input type="hidden" name="userAccount" value="${userAccount}">
            </div>
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="LAY-user-login-password"></label>
                <input type="password" name="password" id="LAY-user-login-password" lay-verify="pass" placeholder="新密码" class="layui-input">
            </div>
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="LAY-user-login-repass"></label>
                <input type="password" name="repass" id="LAY-user-login-repass" lay-verify="required" placeholder="确认密码" class="layui-input">
            </div>
            <div class="layui-form-item">
                <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="LAY-user-forget-resetpass">重置新密码</button>
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

        //找回密码下一步
        form.on('submit(LAY-user-forget-resetpass)', function(obj){
            var field = obj.field;

            if(field.password !== field.repass){
                return layer.msg('两次密码输入不一致');
            }

            //调用重置密码接口
            $.ajax({
                url: "${mainDomain}aps/user/resetpass.action"
                ,type:"post"
                ,dataType:"json"
                ,data: field
                ,success: function(res){

                    if(res.state==207){
                        return layer.msg('账号或者用户名不正确，请核实');
                    }

                    layer.msg('密码修改成功，请使用新密码登陆', {
                        offset: '50px'
                        ,icon: 1
                        ,time: 1000
                    }, function(){
                        location.href = '${mainDomain}aps/user/login'; //跳转到登入页
                    });
                }
            });

            return false;
        });

    });
</script>
</body>
</html>