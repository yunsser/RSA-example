function validateRSA() {
		
			var id = document.getElementById("id").value;
			var pwd = document.getElementById("pwd").value;
			
			try {

			    var rsa = new RSAKey();
				rsa.setPublic($('#rsaPublicKeyModulus').val(), $('#rsaPublicKeyExponent').val());
			
			    // 사용자ID, 비밀번호를 RSA로 암호화
			    var securedUser_Id = rsa.encrypt(id);
			    var securedUser_Pwd = rsa.encrypt(pwd);
			
			    var frm = document.getElementById("frm");
			    frm.securedUser_Id.value = securedUser_Id;
			    frm.securedUser_Pwd.value = securedUser_Pwd;
			    frm.submit();           
			    
			} catch(e) {
			    alert(e);
			}
		}