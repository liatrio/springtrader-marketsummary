package org.springframework.nanotrader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.opentracing.Tracer;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan
public class SpringtraderMarketSummaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringtraderMarketSummaryApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurerConfigurer(HandlerInterceptor interceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(interceptor);
			}
		};
	}

	@Bean
	public HandlerInterceptor tracingInterceptor(Tracer tracer) {
		return new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
				if(tracer.activeSpan() != null) {
					tracer.activeSpan().setTag("http.locale", String.valueOf(request.getLocale()));
				}
				return true;
			}
		};
	}
}
