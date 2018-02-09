package com.vito.uic.client.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.vito.common.util.date.DateUtil;
import com.vito.common.util.validate.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/12/6 10:22
 * 描述: token工具
 */
public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    private static final String USER_ID_KEY = "uid";
    private static final String GROUP_ID_KEY = "gid";
    private static final String USER_NAME_KEY = "uname";
    private static final String MANAGER_KEY = "manager";

    private static final String secret = "vtsj52";

    /**
     * 生成token
     *
     * @param tokenData
     * @return
     */
    //todo 此方法要移到服务端 secret要想办法只能在uic服务端存储
    public static String genToken(TokenData tokenData) {
        try {
            long begin = System.currentTimeMillis();
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                              .withIssuer(tokenData.getUicDomain())
                              .withIssuedAt(tokenData.getLoginTime())
                              .withAudience(tokenData.getAppDomains()
                                                     .toArray(new String[tokenData.getAppDomains().size()]))
                              .withClaim(USER_ID_KEY, tokenData.getUserId())
                              .withClaim(GROUP_ID_KEY, tokenData.getGroupId())
//                              .withClaim(USER_NAME_KEY, tokenData.getUserName())
                              .withClaim(MANAGER_KEY, tokenData.isManager())
                              .withExpiresAt(DateUtil.addSeconds(new Date(), 60 * 60 * 3))
                              .sign(algorithm);
            long end = System.currentTimeMillis();
            logger.debug("生成token花费时间：{}", (end - begin));
            return token;
        } catch (UnsupportedEncodingException e) {
            logger.error("token生成出错", e);
            throw new RuntimeException("token生成出错", e);
        }
    }

    /**
     * 从token中解析出tokenData
     *
     * @param jwtToken
     * @return TokenData
     */
    public static TokenData parseToken(String jwtToken, String uicDomain, String appDomain) {
        long begin = System.currentTimeMillis();
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Verification verification = JWT.require(algorithm);
            if (Validator.isNotNull(uicDomain)) {
                verification.withIssuer(uicDomain);
            }
            JWTVerifier verifier = verification.acceptLeeway(30) // 30 sec for nbf, iat and exp
                                               .build();

            DecodedJWT jwt = verifier.verify(jwtToken);
            Claim uidClaim = jwt.getClaim(USER_ID_KEY);
            Long userId = uidClaim.asLong();
            Claim gidClaim = jwt.getClaim(GROUP_ID_KEY);
            Long groupId = gidClaim.asLong();
//            Claim unameClaim = jwt.getClaim(USER_NAME_KEY);
//            String userName = unameClaim.asString();
            Claim managerClaim = jwt.getClaim(MANAGER_KEY);
            boolean manager = managerClaim.asBoolean();
            TokenData tokenData = new TokenData(userId, groupId, manager);
            List<String> appDomains = jwt.getAudience();
            if (Validator.isNotNull(appDomains)) {
                tokenData.setAppDomains(new HashSet<>(appDomains));
            }
            Date loginTime = jwt.getIssuedAt();
            tokenData.setLoginTime(loginTime);
            long end = System.currentTimeMillis();
            logger.debug("解析token花费时间：{}", (end - begin));
            return tokenData;
        } catch (JWTVerificationException e) {
            logger.error("appToken验证失败", e);
            throw new RuntimeException("appToken验证失败", e);
        } catch (Exception e) {
            logger.error("appToken解析失败", e);
            throw new RuntimeException("appToken解析失败", e);
        }
    }

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjEsIm1hbmFnZXIiOmZhbHNlLCJpc3MiOiJsb2NhbGhvc3QiLCJleHAiOjE1MTI2MTg0ODcsImlhdCI6MTUxMjYxNDg4N30.5xX4XqG_8Pn--7Jpr_8882Liy_0D9QejvnlNe3h_Rl0", "localhost", "");
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }

}
