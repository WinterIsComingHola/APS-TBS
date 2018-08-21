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
          <a><cite>当当当</cite></a>
        </span>
        </div>

        <div class="layui-col-md12">

                <div class="layui-card">
                    <div class="layui-card-header layui-text" style="font-size: 20px;">当当当</div>
                    <div class="layadmin-tips">
                        <i class="layui-icon" face>&#xe69c;</i>
                        <div class="layui-text" style="font-size: 20px;">
                            CODING................
                        </div>
                    </div>
                </div>


        </div>
    </div>
</div>
<#--<div id="hidden-account" hidden>${userAccount}</div>-->

</body>
</html>