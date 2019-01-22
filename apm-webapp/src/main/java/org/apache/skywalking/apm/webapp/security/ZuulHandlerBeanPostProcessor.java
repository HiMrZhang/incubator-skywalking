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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableConfigurationProperties({PortalProperties.class})
public class ZuulHandlerBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ZuulHandlerBeanPostProcessor.class);

    @Autowired
    private PortalProperties portalProperties;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private PortalCommonService portalCommonService;

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {

        if (bean instanceof ZuulHandlerMapping) {
            logger.info("add interceptors!");
            ZuulHandlerMapping zuulHandlerMapping = (ZuulHandlerMapping) bean;
            Set<String> set = new HashSet();
            set.add("/error");
            set.add("/js/**");
            set.add("/css/**");
            set.add("/images/**");
            set.add("/static/**");
            set.add("/favicon.ico");
            set.add("/**/favicon.ico");
            set.add("/webjars/**");
            set.add("/swagger-ui.html");
            set.add("/v2/systemapi-docs");
            set.add("/swagger-resources/**");
            String[] excludePaths;
            if (null != this.portalProperties.getExcludePathPatterns()) {
                excludePaths = this.portalProperties.getExcludePathPatterns();
                int var4 = excludePaths.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    String s = excludePaths[var5];
                    set.add(s);
                }
            }

            excludePaths = new String[set.size()];
            excludePaths = (String[]) set.toArray(excludePaths);
            zuulHandlerMapping.setInterceptors(new MappedInterceptor(this.portalProperties.getPathPatterns(), excludePaths, springMVCInterceptor()));

        }

        return super.postProcessAfterInstantiation(bean, beanName);
    }

    @Bean
    public PortalOAuthInterceptor springMVCInterceptor() {

        PortalOAuthInterceptor portalOAuthInterceptor = new PortalOAuthInterceptor(portalProperties, systemUserService, portalCommonService);

        return portalOAuthInterceptor;
    }


}
