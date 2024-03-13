package com.goalkeepers.server.service;

import java.util.UUID;

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
import com.goalkeepers.server.exception.ErrorCode;
import com.goalkeepers.server.repository.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class KakaoService {
	
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

	private final MemberRepository memberRepository;
	private final AuthService authService;
	private final PasswordEncoder passwordEncoder;
	private ObjectMapper objectMapper;

	public KakaoService(AuthService authService, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
		this.authService = authService;
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
		this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}


	@Transactional
	public TokenDto createKakaoUser(String kakaoAccessToken) {

		/*
		 * 카카오 서버에서 로그인한 유저 정보 가져오기 -> kakaoAccountDto
		 */	
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
    	headers.add("Authorization", "Bearer " + kakaoAccessToken);
    	headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);
		KakaoAccountDto kakaoAccountDto = null;

		try {
			ResponseEntity<String> accountInfoResponse = rt.exchange(
				USER_INFO_URI,
				HttpMethod.POST,
				accountInfoRequest,
				String.class
			);

			kakaoAccountDto = kakaoJsonParsing(accountInfoResponse.getBody(), KakaoAccountDto.class);
		} catch (HttpClientErrorException e) {
			throw new CustomException(ErrorCode.PRECONDITION_FAILED);
			
		} catch (Exception e) {
			throw new CustomException(ErrorCode.SERVER_ERROR, e.getMessage());
		}
		

		/*
		 * 가입한 회원인지 확인해서 회원가입 or 로그인하기
		 */
		Long kakaoId = kakaoAccountDto.getId();
		String kakaoPassword = "kakaologinpassword";

		// 로컬 회원가입한 이메일인 경우 -> 카카오로 로그인 할 수 있게 DB 수정 -> 로그인
		Member existEmail = memberRepository.findByEmail(kakaoAccountDto.getKakao_account().getEmail()).orElse(null);
		if (existEmail != null) {
			existEmail.setSns(SNS.KAKAO);
			existEmail.setSnsId(kakaoId);
			existEmail.setPassword(passwordEncoder.encode(kakaoPassword));
			// 로그인
			LoginRequestDto requestDto = new LoginRequestDto(existEmail.getEmail(), kakaoPassword);
			return authService.login(requestDto);
		}

		// 카카오로 가입했는지
		Member existKakaoMember = memberRepository.findBySnsId(kakaoId).orElse(null);

		if(existKakaoMember == null) {
			// 닉네임 중복 확인 
			String nickname = kakaoAccountDto.getKakao_account().getProfile().getNickname();
			int maxAttempts = 10; // Limit the number of attempts to generate a unique nickname
			int attempts = 0;
			String tempNickname = nickname;
			int size = nickname.length();
			while (memberRepository.existsByNickname(tempNickname) && attempts < maxAttempts) {
				tempNickname = nickname + randomString(size);
				attempts++;
			} 

			// 만약 10번을 랜덤을 돌려도 존재하는 닉네임이면 Exception 보내기
			if (attempts == maxAttempts) {
				throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "닉네임 오류입니다. 다시 소셜 로그인을 진행해주세요.");
			} else {
				nickname = tempNickname;
			}

			// db 저장
			Member member = Member.builder()
							.email(kakaoAccountDto.getKakao_account().getEmail())
							.nickname(nickname)
							.password(passwordEncoder.encode(kakaoPassword))
							.sns(SNS.KAKAO)
							.role(Role.ROLE_USER)
							.snsId(kakaoId)
							.build();
			Member savedMember = memberRepository.save(member);

			// 로그인
			LoginRequestDto requestDto = new LoginRequestDto(savedMember.getEmail(), kakaoPassword);
			return authService.login(requestDto);
		} else {
			// 이미 가입되어 있으면 바로 로그인
			LoginRequestDto requestDto = new LoginRequestDto(existKakaoMember.getEmail(), kakaoPassword);
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

		try {
			// 카카오로부터 Access token 받아오기
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> accessTokenResponse = rt.exchange(
				TOKEN_URI,
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
			);

			KakaoTokenDto kakaoTokenDto = kakaoJsonParsing(accessTokenResponse.getBody(), KakaoTokenDto.class);
			return kakaoTokenDto;
		
		} catch (HttpClientErrorException e) {
			throw new CustomException(ErrorCode.PRECONDITION_FAILED);
			
		} catch (Exception e) {
			throw new CustomException(ErrorCode.SERVER_ERROR, e.getMessage());
		}
	}

	private <T> T kakaoJsonParsing(String json, Class<T> valueType){
		try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PARSE_ERROR);
        }
	}

	private String randomString(int size) {
		return UUID.randomUUID().toString().substring(0,15-size);
	}
}
