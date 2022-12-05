package com.study.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;


@WebFilter("/*")
public class EncodingFilter extends HttpFilter implements Filter {

	private static final long serialVersionUID = 1L;

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
//		ServletRequset를 다운캐스팅
		HttpServletRequest hsr = (HttpServletRequest) request;
		
		// equalsIgnoreCase = 대소문자 구별없이 문자열비교
		if(hsr.getMethod().equalsIgnoreCase("POST")) {
			request.setCharacterEncoding("UTF-8");
		}
		// 전처리
		chain.doFilter(request, response);
		// 후처리
	}

	public void init(FilterConfig fConfig) throws ServletException {}

}
