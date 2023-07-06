package jp.co.sss.shop.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ページング設定
 *
 * @author Tan
 */
@Configuration
public class PagingConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> agResolvers) {
		// TODO Auto-generated method stub
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		//一ページ10個
		resolver.setFallbackPageable(PageRequest. of(0, 10));
		agResolvers.add(resolver);
	}

}
