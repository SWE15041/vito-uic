package com.jay.vito.uic.client.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.jay.vito.common.util.date.DateUtil;
import com.jay.vito.common.util.json.JsonParser;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.vo.ApiErrorResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 日期: 2017/12/6 10:22
 * 描述: token工具
 *
 * @author zhaixm
 */
public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    public static final String TOKEN_HEADER = "Authorization";
    private static final String USER_ID_KEY = "uid";
    private static final String GROUP_ID_KEY = "gid";
    private static final String USER_NAME_KEY = "uname";
    private static final String MANAGER_KEY = "manager";

    private static final String secret = "vtsj52";

	/**
	 * 生成jwt token，默认有效期3小时
	 * @param tokenData
	 * @return
	 */
	public static String genToken(TokenData tokenData) {
		return genToken(tokenData, 60 * 60 * 3);
	}

    /**
     * 生成token
     *
     * @param tokenData
     * @return
     */
    //todo 此方法要移到服务端 secret要想办法只能在uic服务端存储
    public static String genToken(TokenData tokenData, Integer expireSeconds) {
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
                    .withExpiresAt(DateUtil.addSeconds(new Date(), expireSeconds))
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
            // 30 sec for nbf, iat and exp
            JWTVerifier verifier = verification.acceptLeeway(30).build();

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
            tokenData.setToken(jwtToken);
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

    /**
     * 获取认证token
     *
     * @param httpReq
     * @return
     */
    public static String getToken(HttpServletRequest httpReq) {
        String authorization = httpReq.getHeader(TOKEN_HEADER);
        if (Validator.isNotNull(authorization) && authorization.startsWith("bearer")) {
            String token = authorization.split(" ")[1];
            return token;
        }
        return null;
    }

    public static void authFail(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        httpResp.setContentType("application/json");
        httpResp.setCharacterEncoding("UTF-8");
        httpResp.setStatus(HttpStatus.SC_UNAUTHORIZED);
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setMsg("token验证失败，无权访问该数据");
        errorResponse.setErrCode("INVALID_AUTH_TOKEN");
        httpResp.getWriter().write(JsonParser.convertObjectToJson(errorResponse));
    }

}
