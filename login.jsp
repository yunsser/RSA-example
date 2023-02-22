<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset=utf-8" />
<title>로그인</title>
<!-- jquery + bootstrap-->
<script type="text/javascript" src="plugins/jquery/jquery-3.3.1.min.js"></script>
<link rel="stylesheet" href="plugins/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="plugins/bootstrap/js/bootstrap.min.js">
<!-- css -->
<link rel="stylesheet" href="/css/login/login.css">
<!-- js -->
<script src="/js/login/login.js"></script>
<!-- 순서에 유의 -->
<script type="text/javascript" src="plugins/rsa/jsbn.js"></script>
<script type="text/javascript" src="plugins/rsa/rsa.js"></script>
<script type="text/javascript" src="plugins/rsa/prng4.js"></script>
<script type="text/javascript" src="plugins/rsa/rng.js"></script>
</head>
<body>
	<div class="login_all">
		<div class="login_menu">
				<a href="/"><img alt="logo" src="/images/logo.png" height="50" width="100"></a>
				
					<!-- 서버에서 전달받은 공개키를 hidden에 설정한다.  -->
					<input type="hidden" id="rsaPublicKeyModulus" value="${publicKeyModulus}" />
					<input type="hidden" id="rsaPublicKeyExponent" value="${publicKeyExponent}" />
					 
					<div>
						<label>ID</label>
						<input type="text" id="id" name="id" autocomplete="off" class="id_inp">
					</div>
					<div>
						<label>PW</label>
						<input type="password" id="pwd" name="pwd" autocomplete="off" class="pw_inp">
					</div>
					<div>
						<a class="btn btn-secondary" href="/login" onclick="validateRSA(); return false;">로그인</a>
					</div>
					
				<form id="frm" name="frm" method="post" action="/login">
					<input type="hidden" name="securedUser_Id" id="securedUser_Id"value="" />
					<input type="hidden" name="securedUser_Pwd" id="securedUser_Pwd" value="" />
				</form>
		</div>
	</div>
</body>
</html>
