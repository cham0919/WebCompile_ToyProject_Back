package com.wcp.auth;

import com.wcp.common.AESUtils;
import com.wcp.common.Base64Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String SECRET_KEY = Base64Utils.encode("wcp");

    private final String ROLE = "role";
    private final String UUID = "uuid";
    private final String KEY = "key";

    // 토큰 유효시간 30분
    private long tokenValidTime = 30 * 60 * 1000L;

    // JWT 토큰 생성
    public String createToken(TokenDto tokenDto) {
        Claims claims = Jwts.claims(); // JWT payload 에 저장되는 정보단위
        claims.put(ROLE, tokenDto.getRole()); // 정보는 key / value 쌍으로 저장된다.
        claims.put(KEY, tokenDto.getKey()); // 정보는 key / value 쌍으로 저장된다.
        claims.put(UUID, AESUtils.encrypt(tokenDto.getValidateToken())); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 사용할 암호화 알고리즘과
                .compact();
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return (String)Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get(KEY);
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    // 웹 토큰의 유효성 확인
    public boolean validateWebToken(TokenDto dto) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(dto.getAccessToken()).getBody();
            String uuidToken = AESUtils.decrypt(String.valueOf(claims.get(UUID)));
            String validateToken = dto.getValidateToken();
            if(uuidToken.equalsIgnoreCase(validateToken)){
                dto.setRole(String.valueOf(claims.get(ROLE)));
                dto.setKey(String.valueOf(claims.get(KEY)));
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
