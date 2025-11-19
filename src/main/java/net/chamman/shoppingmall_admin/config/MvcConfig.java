package net.chamman.shoppingmall_admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
	
//	private final ClientSpecificArgumentResolver clientSpecificArgumentResolver;
//	private final ClientInfoInterceptor clientInfoInterceptor;
//	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(clientInfoInterceptor).addPathPatterns("/**"); 
//	}
//	
//	@Override
//	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//		resolvers.add(clientSpecificArgumentResolver);
//	}
}
