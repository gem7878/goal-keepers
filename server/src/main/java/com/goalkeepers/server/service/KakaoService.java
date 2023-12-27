package com.goalkeepers.server.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.goalkeepers.server.dto.KakaoAccountDto;
import com.goalkeepers.server.dto.KakaoTokenDto;
import com.goalkeepers.server.dto.LoginRequestDto;
import com.goalkeepers.server.dto.TokenDto;
import com.goalkeepers.server.entity.Member;
import com.goalkeepers.server.entity.Role;
import com.goalkeepers.server.entity.SNS;
import com.goalkeepers.server.exception.CustomException;
import com.goalkeepers.server.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class KakaoService {

	private final MemberRepository memberRepository;
	private final AuthService authService;
	private final PasswordEncoder passwordEncoder;
	
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String CLIENT_ID;

	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String CLIENT_SECRET;	
	
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String REDIRECT_URI;			
	
	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	public String TOKEN_URI;	
	
	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	public String USER_INFO_URI;


	@Transactional
	public TokenDto createKakaoUser(String kakaoAccessToken) throws IOException {

		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
    	headers.add("Authorization", "Bearer " + kakaoAccessToken);
    	headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);
		
		// POST 방식으로 API 서버에 요청 후 response 받아옴
		ResponseEntity<String> accountInfoResponse = rt.exchange(
            USER_INFO_URI,
            HttpMethod.POST,
            accountInfoRequest,
            String.class
    	);

		// JSON Parsing (-> kakaoAccountDto)
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		KakaoAccountDto kakaoAccountDto = null;
		try {
			kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
		} catch (JsonProcessingException e) {
			throw new CustomException("------------Json Error----------" + e.getMessage());

		} catch (HttpClientErrorException e) {
			throw new CustomException("------------RestTemplate Error----------" + e.getMessage());
			
		} catch (Exception e) {
			throw new CustomException("------------Exception----------" + e.getMessage());
		}

		Long kakaoId = kakaoAccountDto.getId();
		Member existMember = memberRepository.findBySnsId(kakaoId).orElse(null);

		if(existMember == null) {
			Member member = Member.builder()
							.email(kakaoAccountDto.getKakao_account().getEmail())
							.nickname(kakaoAccountDto.getKakao_account().getProfile().getNickname())
							.password(passwordEncoder.encode("kakaologinpassword"))
							.sns(SNS.KAKAO)
							.role(Role.ROLE_USER)
							.snsId(kakaoAccountDto.getId())
							.build();
			Member savedMember = memberRepository.save(member);

			LoginRequestDto requestDto = new LoginRequestDto(savedMember.getEmail(), "kakaologinpassword");
			return authService.login(requestDto);
		} else {
			LoginRequestDto requestDto = new LoginRequestDto(existMember.getEmail(), "kakaologinpassword");
			return authService.login(requestDto);
		}
	}

	@Transactional
	public KakaoTokenDto getKakaoAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// Http Response Body 객체 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
    	params.add("client_id", CLIENT_ID); // 카카오 Dev 앱 REST API 키
    	params.add("redirect_uri", REDIRECT_URI); // 카카오 Dev redirect uri
    	params.add("code", code); // 프론트에서 인가 코드 요청시 받은 인가 코드값
    	params.add("client_secret", CLIENT_SECRET); // 카카오 Dev 카카오 로그인 Client Secret	

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		KakaoTokenDto kakaoTokenDto = null;

		try {
			// 카카오로부터 Access token 받아오기
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> accessTokenResponse = rt.exchange(
				TOKEN_URI,
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
			);

			// Json parsing
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
		
		} catch (JsonProcessingException e) {
			throw new CustomException("------------Json Error----------" + e.getMessage());

		} catch (HttpClientErrorException e) {
			throw new CustomException("------------RestTemplate Error----------" + e.getMessage());
			
		} catch (Exception e) {
			throw new CustomException("------------Exception----------" + e.getMessage());
		}
		
		return kakaoTokenDto;
	}
}
