package com.korea.todo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.korea.todo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {

	// 비밀키
	private static final String SECRET_KEY = "d0fc2ae7445c87dfec6ee7e8cf09b46807311b6fdd99cf000d595e1721dbc23b08c146a474885eac48c1713e4c6455562a8ea61c133af324c2725a9d67d08642268a3d1fc6c7f9b39f30fb3bee9d1b461bbd94b4d32f275152bfdf0f45392a93cb27e5e8b9f9931695adaafab25980075e8760a309ea3dc8791fd9d04da6517a6d5d391af79062c6da1f1725c6757f4b713cef336a12d4496de95a547ada8241488818c95b5897dab1b4da36dbd829d4cf1bce4535747f4bf97ac3b6ce25f6aec7a7a4078f6234e61230296359e98c68e4a03c31007f93f79aec92ab0666d48d9cd53fc9ad33e4e8a63fa0ceca30a84e4974130721fe3789c4c443574d3ebb5e2bb85fdc06579961ac060caaa2d2d1b3757e7d0e3e3c243792adbc1d6dbd0e813f569c1478e0545a42de1f9a341075a0c945601aea7a559a0b2bd11b71980676ab38a68a2d4514cf00c195082c93bc0cb7a27e050e04d1ad6a6311adcd6072dbf92631cba33f8f6bf9aa82de79706f75fe194be35d164b1f5b60d2cd074be5bed7571974776e7374f099536ae46cbef6d0a7586eca1e35b27af244486e49c84f81b1f3fc289fcd23b76a540077130bf946f2f530f867447523437be0d0031f74b75888356ab93d5c7775135a9cf11ecc30302cd2397299bb3fffb756ccf63c7a719fc2474ed8f38e3719cdefa09f2064f51342c73f810dd4768a5377cc0cb5cc";
	
	// 토큰을 만드는 메서드
	public String create(UserEntity entity) {
		// 토큰 만료시간을 설정
		// 현재 시각 +1일
		// Instant클래스 : 타임스탬프로 찍는다.
		// plus() : 첫번째 인자는 더할 양, 두번째 인자는 시간단위
		// ChronoUnit열거형의 DAYS 일 단위를 의미한다.
		Date expiryDate = Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		
		/*
		 * header
		 * {
		 * "alg":"HS512"
		 * }.
		 * {
		 * "sub":"402883e596b367d80",
		 * "iss":"todo app",
		 * "iat":1595733657,
		 * "exp":1596597657,
		 * }.
		 * 서명
		 * */
		
		// JWT 토큰을 생성
		return Jwts.builder()
				// header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
					.signWith(SignatureAlgorithm.HS512,SECRET_KEY)
					.setSubject(entity.getId()) // sub 클레임 : 사용자 고유 ID
					.setIssuer("todo app") // iss 클레임 : 토큰 발급자
					.setIssuedAt(new Date()) // iat 클레임 : 발급 시각
					.setExpiration(expiryDate) // exp 클레임 : 만료 시각
					.compact(); // 최종 직렬화된 토큰 문자열 반환
	}
	
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(SECRET_KEY) // 서명 검증용 키 설정
							.parseClaimsJws(token) // 토큰 파싱 및 서명 검증
							.getBody(); // 내부 페이로드(Claims)획득
		
		return claims.getSubject(); // sub 클레임(사용자 ID) 반환
	}
}
