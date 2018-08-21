<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>登入 - TBSMOCK</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" href="${miscDomain}/statics/layuiadmin/style/login.css" media="all">
</head>
<body>

<div class="layadmin-user-login layadmin-user-display-show" id="LAY-user-login" style="display: none;">

    <div class="layadmin-user-login-main">
        <div class="layadmin-user-login-box layadmin-user-login-header">
            <h2>TBS</h2>
            <p>接口测试系统</p>
        </div>
        <div class="layadmin-user-login-box layadmin-user-login-body layui-form">
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-username" for="LAY-user-login-username"></label>
                <input type="text" name="userAccount" id="LAY-user-login-username" lay-verify="required" placeholder="用户名" class="layui-input">
            </div>
            <div class="layui-form-item">
                <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="LAY-user-login-password"></label>
                <input type="password" name="passWord" id="LAY-user-login-password" lay-verify="required" placeholder="密码" class="layui-input">
            </div>
            <div class="layui-form-item" style="margin-bottom: 20px;">
                <input type="checkbox" name="remember" lay-skin="primary" title="记住账号">
                <a href="${mainDomain}aps/user/forget" class="layadmin-user-jump-change layadmin-link" style="margin-top: 7px;">忘记密码？</a>
            </div>
            <div class="layui-form-item">
                <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="LAY-user-login-submit">登 入</button>
            </div>
            <div class="layui-trans layui-form-item layadmin-user-login-other">
                <a href="javascript:;"><i class="layadmin-user-jump-change layadmin-link">必看前情</i></a>

                <a href="${mainDomain}aps/user/register" class="layadmin-user-jump-change layadmin-link">注册帐号</a>
            </div>
        </div>
    </div>


    <!--<div class="ladmin-user-login-theme">
      <script type="text/html" template>
        <ul>
          <li data-theme=""><img src="{{ layui.setter.base }}style/res/bg-none.jpg"></li>
          <li data-theme="#03152A" style="background-color: #03152A;"></li>
          <li data-theme="#2E241B" style="background-color: #2E241B;"></li>
          <li data-theme="#50314F" style="background-color: #50314F;"></li>
          <li data-theme="#344058" style="background-color: #344058;"></li>
          <li data-theme="#20222A" style="background-color: #20222A;"></li>
        </ul>
      </script>
    </div>-->

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

        //提交
        form.on('submit(LAY-user-login-submit)', function(obj){
            var formField = obj.field;

            var isremeber = formField.remember;
            if(typeof(isremeber)=="undefined"){
                isremeber = "noton";
            }

            //请求登入接口
            $.ajax(
                    {
                        url: "${mainDomain}aps/user/checkUserInfo.action" //实际使用请改成服务端真实接口
                        ,type:"post"
                        ,dataType:"json"
                        ,data:formField
                        ,success: function(resp){

                        if(resp.state==205){
                            return layer.msg('账号不存在，请核实');
                        }else if(resp.state==200){
                            return layer.msg('密码不正确，请核实');
                        }

                        //登入成功的提示与跳转
                        layer.msg('登入成功', {
                            offset: '15px'
                            ,icon: 1
                            ,time: 1000
                        }, function(){
                            location.href = '${mainDomain}aps/user/homehandle?remeberMe='+isremeber+'&userAccount='+formField.userAccount; //后台主页
                            });
                        }
                    });
            return false;
        });

    });
</script>
</body>
</html>