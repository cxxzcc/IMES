package com.itl.iap.auth.controller.oauth;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.auth.entity.AuthAccessToken;
import com.itl.iap.auth.entity.AuthClient;
import com.itl.iap.auth.entity.IapUserLoginLogT;
import com.itl.iap.auth.entity.SysUser;
import com.itl.iap.auth.mapper.IapUserLoginLogTMapper;
import com.itl.iap.auth.service.IAuthAccessTokenService;
import com.itl.iap.auth.service.IAuthClientService;
import com.itl.iap.auth.service.IAuthCodeService;
import com.itl.iap.auth.service.ISysUserService;
import com.itl.iap.auth.shiro.jwt.JWTGenerator;
import com.itl.iap.auth.shiro.jwt.JWTToken;
import com.itl.iap.auth.util.PassWordUtil;
import com.itl.iap.auth.util.RedisUtil;
import com.itl.iap.common.base.aop.IpUtil;
import com.itl.iap.common.base.config.Constants;
import com.itl.iap.common.util.UUID;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * ????????????Controller
 *
 * @author ??????
 * @date 2020/10/22
 * @since jdk1.8
 */

@Api("Auth-?????????????????????")
@Slf4j
@RestController
public class AccessTokenController {

    @Autowired
    private IAuthCodeService authCodeService;

    @Autowired
    private IAuthClientService authClientService;

    @Autowired
    private IAuthAccessTokenService authAccessTokenService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired(required = false)
    private IapUserLoginLogTMapper iapUserLoginLogMapper;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    @ApiOperation(value = "??????IAP?????????accessToken", notes = "??????IAP?????????accessToken")
    public ResponseEntity<Object> token(HttpServletRequest request) throws OAuthSystemException {
        // ???????????????????????????
        String captcha = request.getParameter("captcha");
        int value = HttpStatus.OK.value();

        // Object captcha1 = request.getSession().getAttribute("captcha");

        int i = Integer.parseInt(request.getParameter("errorTimes"));
        if (i > 0) {
            if (!CaptchaUtil.ver(captcha, request)) {
                CaptchaUtil.clear(request);
                HashMap<String, Object> map = new HashMap<>();
                map.put("msg", "??????????????????");
                map.put("code", value);
                map.put("data", false);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }

        IapUserLoginLogT iapUserLoginLogT = new IapUserLoginLogT();
        AuthAccessToken authAccessToken = new AuthAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        try {
            // ??????OAuth??????
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            //????????????????????????ID????????????
            AuthClient client = authClientService.getById(oauthRequest.getClientId());
            if (null == client) {
                log.error("??????accessToken???????????????ID?????? client=" + oauthRequest.getClientId());
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            // ?????????????????????KEY????????????
            if (!oauthRequest.getClientSecret().equalsIgnoreCase(client.getClientSecret())) {
                log.error("ClientSecret????????? client_secret=" + oauthRequest.getClientSecret());
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            /*
             * ??????????????? AUTHORIZATION_CODE ????????????????????????PASSWORD ???REFRESH_TOKEN ??? CLIENT_CREDENTIALS
             * ???????????? {@link GrantType}
             * */
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equalsIgnoreCase(GrantType.PASSWORD.toString())) {
                //TODO ?????????????????????
                if (StringUtils.isEmpty(request.getParameter("username")) || StringUtils.isEmpty(request.getParameter("password"))) {
                    OAuthResponse response = OAuthResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                            .setErrorDescription(Constants.INVALID_USER_DESCRIPTION)
                            .buildJSONMessage();
                    iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE3); // ?????????????????????
                    iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);  // ????????????
                    loinLog(iapUserLoginLogT, request);
                    return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
                } else {
                    SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().lambda()
                            .eq(SysUser::getUserName, request.getParameter("username"))
                            .eq(SysUser::getUserPsw, PassWordUtil.encrypt(request.getParameter("password"), request.getParameter("username"))));
                    if (user == null) {
                        iapUserLoginLogT.setUserId(request.getParameter("username"));//??????
                        iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE3);  // ????????????
                        iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);  // ????????????
                        iapUserLoginLogT.setMessage("?????????????????????");
                        loinLog(iapUserLoginLogT, request);
                        throw new OAuthSystemException("?????????????????????");
                    }
                    if (user.getState() != null && user.getState().equals(SysUser.STATE_1) || user.getValidity() != null && user.getValidity().before(new Date())) {
                        iapUserLoginLogT.setUserId(request.getParameter("username"));//??????
                        iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE3);  // ????????????
                        iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);  // ????????????
                        iapUserLoginLogT.setMessage("???????????????????????????????????????????????????");
                        loinLog(iapUserLoginLogT, request);
                        throw new OAuthSystemException("???????????????????????????????????????????????????");
                    }
                    authAccessToken.setUserId(user.getUserName());
                    iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE3); // ??????????????????
                    if (null == user) {
                        OAuthResponse response = OAuthResponse
                                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                                .setErrorDescription(Constants.INVALID_USER_DESCRIPTION)
                                .buildJSONMessage();
                        iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                        loinLog(iapUserLoginLogT, request);
                        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
                    }
                    iapUserLoginLogT.setUserId(user.getUserName());
                }
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
                //TODO ?????? refresh_token ????????????
                //TODO ?????? refresh_token ????????????
                //???????????????TOKEN??????
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                        .setErrorDescription(Constants.INVALID_USER_DESCRIPTION)
                        .buildJSONMessage();

                iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                loinLog(iapUserLoginLogT, request);
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.CLIENT_CREDENTIALS.toString())) {
                OAuthResponse response = null;
                //1.?????????????????????
                boolean isClientCredential = false;
                String[] grantTypes = client.getGrantTypes().split(",");
                for (String grantType : grantTypes) {
                    if (grantType.equals(GrantType.CLIENT_CREDENTIALS.toString())) {
                        isClientCredential = true;
                        break;
                    }
                }
                //2.??????????????????????????????????????????????????????
                if (!isClientCredential) {
                    //??????????????????????????????
                    response = OAuthResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                            .setErrorDescription(Constants.INVALID_USER_DESCRIPTION)
                            .buildJSONMessage();
                    iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                    loinLog(iapUserLoginLogT, request);
                    return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
                }
            } else {
                //??????????????????????????????
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                        .setErrorDescription(Constants.INVALID_GRAND_TYPE_NOTFOUND)
                        .buildJSONMessage();
                iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                loinLog(iapUserLoginLogT, request);
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            // ??????Access Token
//            String token = JwtUtil.sign(request.getParameter("username"), request.getParameter("username"));
            String userName = "";
            // ???????????????
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.CLIENT_CREDENTIALS.toString())) {
                userName = client.getClientId();
                // auth2 code??????
            } else {
                // ????????????
                userName = request.getParameter("username");
            }
            JWTGenerator jwtGenerator = new JWTGenerator();
            jwtGenerator.setSalt(userName);
            jwtGenerator.setUsername(userName);
            jwtGenerator.setClientId(client.getClientId());
            OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(jwtGenerator);
            String accessToken = oAuthIssuer.accessToken();
//            String token = JwtUtil.sign(username, username);
            log.info("??????????????????accessToken=" + accessToken);
            //??????token
            authAccessToken.setId(UUID.uuid32());
            authAccessToken.setClientId(client.getClientId());
            authAccessToken.setTokenId(accessToken);
            authAccessToken.setCreateDate(new Date());
            authAccessToken.setAuthenticationId(null);
            authAccessToken.setTokenExpired(Constants.EXPIRES_IN);
//            authAccessToken.setUserId()
            System.out.println("1.---->>>SecurityUtils.getSubject().isAuthenticated() =" + SecurityUtils.getSubject().isAuthenticated());
            JWTToken jwtToken = JWTToken.build(accessToken, userName, userName, Constants.EXPIRES_IN, oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE), client.getClientId());
            SecurityUtils.getSubject().login(jwtToken);
            authAccessTokenService.save(authAccessToken);
            // ???????????????token??????Redis
            redisUtil.set(userName, authAccessToken.getTokenId(), Constants.EXPIRES_IN);
            // ??????OAuth??????
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(Constants.EXPIRES_IN))
                    .buildJSONMessage();
            System.out.println("2.---->>>SecurityUtils.getSubject().isAuthenticated() =" + SecurityUtils.getSubject().isAuthenticated());
            // ??????OAuthResponse ??????ResponseEntity
            iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND1);
            loinLog(iapUserLoginLogT, request);
            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));

        } catch (OAuthProblemException e) {
            log.error("??????accessToken????????????e=", e);
            // ??????????????????
            OAuthResponse response = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
            loinLog(iapUserLoginLogT, request);
            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param iapUserLoginLogT ???????????????????????????
     * @param request          HttpServletRequest
     */
    private void loinLog(IapUserLoginLogT iapUserLoginLogT, HttpServletRequest request) {
        Date date = new Date();
        iapUserLoginLogT.setCreateDate(date);
        iapUserLoginLogT.setLastUpdateDate(date);
        iapUserLoginLogT.setId(UUID.uuid32());
        // ??????ip??????
        String ipAddr = IpUtil.getIpAddr(request);
        iapUserLoginLogT.setLastIp(ipAddr);
        // ?????????????????????
        String browser = IpUtil.getBrowser(request.getHeader("user-agent"));
        // ??????????????????
        iapUserLoginLogT.setLoginOs(System.getProperty("os.name"));
        iapUserLoginLogT.setOsver(System.getProperty("os.version"));
        iapUserLoginLogT.setVersion("1.0");
        iapUserLoginLogT.setClient(browser);
        iapUserLoginLogMapper.insert(iapUserLoginLogT);
    }


}
