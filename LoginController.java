package com.demo.sample.controller;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class LoginController {
	// 아이디 암호화
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String Login(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception, NoSuchAlgorithmException {

		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048); // 키 사이즈 - 1024, 2048

		KeyPair keyPair = generator.genKeyPair();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		// 개인 키 생성 후 세션에 저장
		session.setAttribute("__rsaPrivateKey__", privateKey);

		// 공개키를 문자열로 변환
		RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

		String publicKeyModulus = publicSpec.getModulus().toString(16);
		String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

		// 로그인 폼 Input hidden 값 설정
		request.setAttribute("publicKeyModulus", publicKeyModulus);
		request.setAttribute("publicKeyExponent", publicKeyExponent);
		
		System.out.println("publicKeyModulus: "+publicKeyModulus);
		System.out.println("publicKeyExponent: "+publicKeyExponent);

		return "login/login";
	}

	// 복호화 함수 정의
	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
		System.out.println("will decrypt : " + securedValue);
		Cipher cipher = Cipher.getInstance("RSA");
		byte[] encryptedBytes = hexToByteArray(securedValue);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩
		return decryptedValue;
	}

	// 16진수 문자열을 바이트 배열로 변환
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		
		String securedUser_Id = (String) request.getParameter("securedUser_Id");
        String securedUser_Pwd = (String) request.getParameter("securedUser_Pwd");
        
        System.out.println("securedUser_Id: "+securedUser_Id.toString());
        System.out.println("securedUser_Pwd: "+securedUser_Pwd.toString());
 
		//세션에 저장된 개인키를 불러온다.
		PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
		session.removeAttribute("__rsaPrivateKey__"); // 키 재사용 방지

		if (privateKey == null) {
			throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
		}

		try {
			// 개인키로 데이터를 복호화한다.
			String id = decryptRsa(privateKey, securedUser_Id);
			String pwd = decryptRsa(privateKey, securedUser_Pwd);
			
			request.setAttribute("id", id);
			request.setAttribute("pwd", pwd);
			
			System.out.println("id: "+id);
			System.out.println("pwd: "+pwd);
			
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage(), ex);

		}

		return "redirect:/"; //
	}
	
	
}