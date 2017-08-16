<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport"content="width=device-width,initial-scale=1,user-scalable=0">
	<title>斗拍商城</title>
	<link rel="stylesheet" href="/api/resources/css/weui.css" />
	<link rel="stylesheet" href="/api/resources/css/download.css" />
</head>
<body>
	<div class="container" id="container">
		<div class="page msg_success js_show">
			<div class="weui-msg tips">
				<div class="weui-msg__icon-area">
					<div class="arrow"></div>
				</div>
				<p>

					<div class="logo"></div>
				</p>
			</div>
		
			<div class="weui-msg download">
				<div class="weui-msg__icon-area">
					<div class="logo"></div>
				</div>

				<div class="weui-msg__opr-area">
					<p class="weui-btn-area">
						${unionid}
					</p>
					<p class="weui-btn-area">
						${openid}
					</p>
					<p class="weui-btn-area">
						${uid}
					</p>
					<p class="weui-btn-area">
						${info}
					</p>
				</div>
			</div>
		</div>
	</div>
	<script src="/api/resources/js/jquery-min.js"></script>
	<script>
		$(function() {
			var ua = window.navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i) == 'micromessenger') {
				$(".tips").show();
				$(".download").hide();
			} else {
				$(".tips").hide();
				$(".download").show();
			}
		});
	</script>
</body>
</html>