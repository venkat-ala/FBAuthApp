package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class SimpleCORSFilter implements Filter {

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;
    HttpServletRequest request = (HttpServletRequest) req;
    response.setHeader("Access-Control-Allow-Origin", "*");
    if (request.getHeader("Access-Control-Request-Method") != null
            && "OPTIONS".equals(request.getMethod())) {
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
	    response.setHeader("Access-Control-Allow-Credentials", "true");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "Accept, Authorization, Content-Type, token");
	    response.setHeader("token", "*");
    }
    chain.doFilter(req, res);
  }

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}

}