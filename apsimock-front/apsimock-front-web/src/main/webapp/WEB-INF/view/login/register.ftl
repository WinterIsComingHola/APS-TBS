<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>注册 - APSMOCK</title>
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
        <h2>TBSK</h2>
        <p>接口测试系统</p>
      </div>
      <div class="layadmin-user-login-box layadmin-user-login-body layui-form">
        <div class="layui-form-item">
          <label class="layadmin-user-login-icon layui-icon layui-icon-cellphone" for="LAY-user-login-cellphone"></label>
          <input type="text" name="userAccount" id="LAY-user-login-cellphone" lay-verify="required" placeholder="苏宁工号" class="layui-input">
        </div>
        <div class="layui-form-item">
          <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="LAY-user-login-password"></label>
          <input type="password" name="passWord" id="LAY-user-login-password" lay-verify="pass|required" placeholder="密码" class="layui-input">
        </div>
        <div class="layui-form-item">
          <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="LAY-user-login-repass"></label>
          <input type="password" name="passConfirm" id="LAY-user-login-repass" lay-verify="pass|required" placeholder="确认密码" class="layui-input">
        </div>
        <div class="layui-form-item">
          <label class="layadmin-user-login-icon layui-icon layui-icon-username" for="LAY-user-login-nickname"></label>
          <input type="text" name="userName" id="LAY-user-login-nickname" lay-verify="nickname|required" placeholder="姓名" class="layui-input">
        </div>
        <div class="layui-form-item">
          <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="LAY-user-reg-submit">注 册</button>
        </div>
        <div class="layui-trans layui-form-item layadmin-user-login-other">

          <a href="${mainDomain}aps/user/login" class="layadmin-user-jump-change layadmin-link layui-hide-xs">用已有帐号登入</a>
          <a href="login.html" class="layadmin-user-jump-change layadmin-link layui-hide-sm layui-show-xs-inline-block">登入</a>
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
                  ,form = layui.form;
          form.render();

          form.on('submit(LAY-user-reg-submit)',function (obj) {

              var formField = obj.field;
              if(formField.passWord !==formField.passConfirm){
                  return layer.msg('两次密码输入不一致');
              }
              $.ajax(
                      {
                          url:"${mainDomain}aps/front/user/addUserInfo.action"
                          ,type:"post"
                          ,dataType:"json"
                          ,data:formField
                          ,success:function (resp) {
                          if(resp.state==204){
                              return layer.msg('账号已经被注册，请换一个再试');
                          }else if(resp.state==109){
                              return layer.msg('内部异常，请稍后再试');
                          }
                          layer.msg('注册成功，请使用注册账号登录', {
                              offset: '50px'
                              ,icon: 1
                              ,time: 1000
                          }, function(){
                              location.href = '${mainDomain}aps/user/login'; //跳转到登入页
                          });
                      }
                      }
              );
              return false;
          });
      });

  </script>


<!--
  <script>
  layui.config({
    base: '/statics/layuiadmin/' //静态资源所在路径
  }).extend({
    index: 'lib/index' //主入口模块
  }).use(['index', 'user'], function(){
    var $ = layui.$
    ,setter = layui.setter
    ,admin = layui.admin
    ,form = layui.form
    ,router = layui.router();

    form.render();

    //提交
    form.on('submit(LAY-user-reg-submit)', function(obj){
      var field = obj.field;
      
      //确认密码
      if(field.passWord !== field.passConfirm){
        return layer.msg('两次密码输入不一致');
      }
      
      //请求接口
      admin.req({
        url: 'http://localhost:18081/aps-soa/test/aps/rest/user/addUserInfo.action' //实际使用请改成服务端真实接口
        ,data: field
        ,done: function(res){        
          layer.msg('注册成功', {
            offset: '15px'
            ,icon: 1
            ,time: 1000
          }, function(){
            location.hash = '/user/login'; //跳转到登入页
          });
        }
      });
      
      return false;
    });
  });
  </script>-->
</body>
</html>