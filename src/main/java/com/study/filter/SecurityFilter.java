package com.study.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.domain.User;

@WebFilter("/*")
public class SecurityFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	
	public void destroy() {	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String requestURI = req.getRequestURI();
		String antMatchers1 = "/mypage, /mypage/password";
		String antMatchers2 = "/login, /register";
		String logoutURI = "/logout";
		String adminPage = "/admin";
		
		System.out.println("시큐리티 동작");
		// 페이지에 접근했을때 && 로그인되지 않았을때
		if(antMatchers1.contains(requestURI) && !authorization(req.getSession())) {
			resp.sendRedirect("/login");
			return;
		}
		
		// 페이지에 접근했을때 && 로그인되었을때
		if(antMatchers2.contains(requestURI) && authorization(req.getSession())) {
			resp.sendRedirect("/mypage");
			return;
		}
		
		// 로그아웃
		// invalidate 세션을 강제종료
		if(logoutURI.equalsIgnoreCase(requestURI)) {
			req.getSession().invalidate();
			resp.sendRedirect("/login");
			return;
		}
		
		// 내가 요청한 URI에 admin이 들어간 요청주소가 있다면, 권한이 없다면
		if(requestURI.contains(adminPage) && !hasRole(req.getSession(), "ADMIN")) {
			resp.sendError(403, "Forbidden.");
			return;
		}
		
		chain.doFilter(request, response);
	}
	
	private boolean authorization(HttpSession session) {
		User principalUser = (User) session.getAttribute("principal");
		System.out.println(principalUser);
		return principalUser != null;
	}
	
	// 권한
	private boolean hasRole(HttpSession session, String role) {
		AtomicBoolean result = new AtomicBoolean(false);
		// 로그인 되어있을떄
		if(authorization(session)) {
			User principalUser = (User) session.getAttribute("principal");
			
			// role을 꺼내서 replaceAll로 띄워쓰기를 제거후 ,를 기준으로 끊는다. 그것을 배열에 넣음.
			String[] roleArray = principalUser.getRoles().replaceAll(" ", "").split(",");
			// 배열을 LIST로 변환한다.
			List<String> roleList = Arrays.asList(roleArray);
			
			roleList.forEach(r -> {
				if(r.startsWith("ROLE_") && r.contains(role)) {
					result.set(true);
				}
			});
		}
		return result.get();
	}
	
	
	public void init(FilterConfig fConfig) throws ServletException {}

}
