<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="fyunli">

    <base id="base" href="${base}">
    <title>登录页</title>

    <!-- Bootstrap core CSS -->
    <link href="//cdn.jsdelivr.net/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="${base}/css/layout.css" rel="stylesheet">
    <link href="${base}/css/layout-login.css" rel="stylesheet">
</head>
<body>
<div class="login-page page-container">
    <div class="mask"></div>
    <form class="form-horizontal" method="post" action="/login?target=${target!''}">
        <div class="login-wrapper">
            <div class="login-hd">
                <h2 class="text-center">登录</h2>
            </div>
            <div class="login-bd">
                <div class="form-group">
                    <label for="username" class="col-xs-3 control-label">账 号：</label>
                    <div class="col-xs-9">
                        <input type="text" class="form-control" name="loginName" placeholder="用户名"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-xs-3 control-label">密 码：</label>
                    <div class="col-xs-9">
                        <input type="password" class="form-control" name="password" placeholder="密码"/>
                    </div>
                </div>
            </div>
            <div class="login-ft">
                <div class="form-group">
                    <div class="col-xs-12">
                        <button type="submit" class="btn btn-info form-control">登录</button>
                        <div>${errMsg!''}</div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>