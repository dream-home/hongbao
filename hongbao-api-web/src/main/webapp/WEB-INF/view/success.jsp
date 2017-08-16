<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport"content="width=device-width,initial-scale=1,user-scalable=0">
	<title>提示-斗拍商城</title>
	<link rel="stylesheet" href="/api/resources/css/weui.css" />
</head>
<body>
	<div class="container" id="container">
		<div class="page msg_success js_show">
			<div class="weui-msg">
				<div class="weui-msg__icon-area">
					<i class="weui-icon-success weui-icon_msg"></i>
				</div>
				<div class="weui-msg__text-area">
					<h2 class="weui-msg__title">分享成功</h2>
					<p class="weui-msg__desc">${msg}</p>
				</div>
				<div class="weui-msg__opr-area">
					<p class="weui-btn-area">
						<a href="javascript:history.back();" class="weui-btn weui-btn_primary">确 定</a>
					</p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>