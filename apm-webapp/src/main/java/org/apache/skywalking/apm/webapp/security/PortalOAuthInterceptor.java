/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.webapp.security;

import com.suixingpay.portal.authorization.config.PortalProperties;
import com.suixingpay.portal.authorization.service.PortalCommonService;
import com.suixingpay.portal.authorization.service.SystemUserService;
import com.suixingpay.portal.common.constant.Constant;
import com.suixingpay.portal.common.domain.SystemUser;
import com.suixingpay.takin.exception.IHttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLEncoder;

public final class PortalOAuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ZuulHandlerBeanPostProcessor.class);


    private static final String CHAR_SET = "UTF-8";


    private static final String QUESTION = "?";

    private static final String AND = "&";

    private static final String EQUAL = "=";


    private final PortalProperties portalConfig;

    private SystemUserService systemUserService;
    private PortalCommonService portalCommonService;

    public PortalOAuthInterceptor(PortalProperties portalConfig, SystemUserService systemUserService, PortalCommonService portalCommonService) {
        this.portalConfig = portalConfig;
        this.systemUserService = systemUserService;
        this.portalCommonService = portalCommonService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = getToken(request);
        SystemUser systemUser = null;
        HttpStatus status = null;
        if (null != portalConfig.getDebugUser() && null != portalConfig.getDebugUser().getId()) {
            systemUser = portalConfig.getDebugUser();
        } else {
            if (!isCorrectToken(token)) {
                writeData(request, response, HttpStatus.UNAUTHORIZED);
                return false;
            }

            try {
                logger.info("portalConfig.getSystemCode() : {};authCode : {}", portalConfig.getSystemCode(), null);
                systemUser = systemUserService.checkAuthAndGetUserInfo(token, portalConfig.getSystemCode(), null);
                logger.info("systemUser:{}", systemUser);
            } catch (Throwable e) {
                if (e instanceof IHttpStatusException) {
                    IHttpStatusException tmp = (IHttpStatusException) e;
                    for (HttpStatus httpStatus : HttpStatus.values()) {
                        if (httpStatus.value() == tmp.getStatus()) {
                            status = httpStatus;
                            break;
                        }
                    }
                } else {
                    status = HttpStatus.UNAUTHORIZED;
                }
                logger.error(e.getMessage(), e);
            }
        }
        if (systemUser == null) {
            writeData(request, response, status);
            return false;
        } else {
            return true;
        }

    }


    private final String getToken(HttpServletRequest request) {
        return request.getHeader(Constant.TOKEN_HEADER_NAME);
    }


    private boolean isCorrectToken(String token) {
        if (null == token || token.trim().isEmpty()) {
            logger.info("error 401 token is empty");
            return false;
        }
        return true;
    }


    private String buildRedirectUrl(HttpStatus status) throws Exception {
        if (null == portalConfig.getFrontDomain()) {
            String frontDomain = portalCommonService.getFrontDomain();
            portalConfig.setFrontDomain(frontDomain);
        }
        PortalProperties.Error error = portalConfig.getError();
        String errorPageUrl = null;
        switch (status) {
            case UNAUTHORIZED:
                errorPageUrl = error.getPortal401Page();
                break;
            case FORBIDDEN:
                errorPageUrl = error.getPortal403Page();
                break;
            default:
                errorPageUrl = error.getPortal500Page();
                break;
        }
        if (errorPageUrl.indexOf(QUESTION) == -1) {
            errorPageUrl = errorPageUrl + QUESTION;
        }
        return errorPageUrl + AND + error.getErrorMessageName() + EQUAL + URLEncoder.encode(status.name(), CHAR_SET) + AND
                + error.getCodeName() + EQUAL + status.value();
    }

    private void writeData(HttpServletRequest request, HttpServletResponse response,
                           HttpStatus status) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        String resStr = "{\"redirectUrl\":\"" + buildRedirectUrl(status) + "\"}";
        try (PrintWriter out = response.getWriter()) {
            out.print(resStr);
            out.flush();
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
