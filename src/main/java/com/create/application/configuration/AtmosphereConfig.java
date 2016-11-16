/*
 * Copyright  2016 Sebastian Gil.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.create.application.configuration;

import com.create.atmosphere.LiveUpdate;
import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.spring.SpringWebObjectFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


@Configuration
@ComponentScan
@ComponentScan({
		"com.create.atmosphere"
})
public class AtmosphereConfig implements ServletContextInitializer {

	@Bean
	public AtmosphereServlet atmosphereServlet() {
		return new AtmosphereServlet();
	}

	@Bean
	public AtmosphereFramework atmosphereFramework() {
		return atmosphereServlet().framework();
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		configureAtmosphere(atmosphereServlet(), servletContext);
	}

	private void configureAtmosphere(AtmosphereServlet servlet, ServletContext servletContext) {
		final ServletRegistration.Dynamic atmosphereServlet = servletContext.addServlet("atmosphereServlet", servlet);

		atmosphereServlet.setInitParameter(ApplicationConfig.ANNOTATION_PACKAGE,
				LiveUpdate.class.getPackage().getName());
		atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_CACHE, UUIDBroadcasterCache.class.getName());
		atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_SHARABLE_THREAD_POOLS, "true");
		atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_MESSAGE_PROCESSING_THREADPOOL_MAXSIZE, "10");
		atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_ASYNC_WRITE_THREADPOOL_MAXSIZE, "10");
		atmosphereServlet.setInitParameter(ApplicationConfig.OBJECT_FACTORY, SpringWebObjectFactory.class.getName());
		servletContext.addListener(new org.atmosphere.cpr.SessionSupport());
		atmosphereServlet.addMapping("/live/*");
		atmosphereServlet.setLoadOnStartup(0);
		atmosphereServlet.setAsyncSupported(true);
	}

}
