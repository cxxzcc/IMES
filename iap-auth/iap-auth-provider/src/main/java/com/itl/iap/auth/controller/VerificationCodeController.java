package com.itl.iap.auth.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 获取验证码图片
 * </p>
 *
 * @author kk
 * @date 2021/12/30 16:49
 */
@Api("验证码")
@RestController
public class VerificationCodeController {

    @PostMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 宽 高 位数
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 4);
        // 数字和大写字母混合
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        captcha.setFont(Captcha.FONT_5);
        captcha.toBase64();
        CaptchaUtil.out(captcha, request, response);
    }

}
