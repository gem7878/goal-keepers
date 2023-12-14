package com.goalkeepers.server.service;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
// import org.springframework.web.servlet.view.RedirectView;

import com.goalkeepers.server.transformer.Trans;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class KakaoService {
	
	private final HttpSession httpSession;	
	
	@Autowired
	public HttpCallService httpCallService;
	

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String CLIENT_ID;

	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String CLIENT_SECRET;	
	
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String REDIRECT_URI;	
	
	@Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
	private String AUTHORIZE_URI;		
	
	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	public String TOKEN_URI;	
	
	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	public String USER_INFO_URI;
	
	
	public String goKakaoOAuth() {
       return goKakaoOAuth("");
	}
	
	public String goKakaoOAuth(String scope) {
	   String uri = AUTHORIZE_URI+"?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&response_type=code";
	   if(!scope.isEmpty()) uri += "&scope="+scope;
			   
       return uri;
	}	
	
	public String loginCallback(String code) {	
		String param = "grant_type=authorization_code&client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&client_secret="+CLIENT_SECRET+"&code="+code;
		String rtn = httpCallService.Call("POST", TOKEN_URI, "", param);
        httpSession.setAttribute("token", Trans.token(rtn));     		
		return "";
	}
			
	public String getProfile() {	
		return httpCallService.CallwithToken("GET", USER_INFO_URI, httpSession.getAttribute("token").toString());
	}
}
