/*******************************************************************************
 * Copyright 2019 Sateesh Gampala
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors:
 * 	Sateesh Gampala - Initial contribution and API
 ******************************************************************************/
package com.sa.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sa.web.interceptor.SessionTimerInterceptor;

/**
 * The <code>WebMvcConfig</code>
 *
 * @author Sateesh G
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private SessionTimerInterceptor sessionTimerInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionTimerInterceptor);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// Map "/"
		registry.addViewController("/").setViewName("forward:/index.html");

		// Map "/word", "/word/word", and "/word/word/word" - except for anything starting with "/api/..." or ending with
		// a file extension like ".js" - to index.html. By doing this, the client receives and routes the url. It also
		// allows client-side URLs to be bookmarked.

		// Single directory level - no need to exclude "api"
		registry.addViewController("/{x:[\\w\\-]+}").setViewName("forward:/index.html");
		// Multi-level directory path, need to exclude "api" on the first part of the path
		registry.addViewController("/{x:^(?!api$).*$}/**/{y:[\\w\\-]+}").setViewName("forward:/index.html");
	}

}
