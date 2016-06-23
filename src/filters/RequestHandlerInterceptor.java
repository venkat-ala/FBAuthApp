package filters;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.User;
import models.UserTokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import response.Response;
import service.UserService;

import com.google.gson.Gson;

import common.ServiceConstants;
import common.ServiceResponseUtils;

@Component
public class RequestHandlerInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		User user;
		UserTokens userTokenByToken = null;
		String token = (String) request.getHeader("token");
		try {
			if (token == null || token.toString().trim().equals("")) {
				Response sr = ServiceResponseUtils.dataResponse(
						ServiceConstants.UNAUTHORIZED_CODE,
						ServiceConstants.UNAUTHORIZED_MESSAGE, null);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response = getJsonResponce(response, sr);
				return false;
			} else {
				userTokenByToken = userService.getUserTokenByToken(token);
				if (userTokenByToken == null) {
					Response sr = ServiceResponseUtils.dataResponse(
							ServiceConstants.UNAUTHORIZED_CODE,
							ServiceConstants.UNAUTHORIZED_MESSAGE, null);
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response = getJsonResponce(response, sr);
					return false;
				} else {
					user =  userTokenByToken.getUserId();
					long diffSeconds = 0;
					if (userTokenByToken.getLastAccessTime() != null) {
						long diff = new Date().getTime() - userTokenByToken.getLastAccessTime().getTime();
						diffSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);
					}
					if (diffSeconds <= 300) {
						userTokenByToken.setLastAccessTime(new Date());
						userService.updateUserToken(userTokenByToken);
						return true;
					} else {
						userService.dodeleteByUserToken(token);
						Response sr = ServiceResponseUtils.dataResponse(
								ServiceConstants.UNAUTHORIZED_CODE,
								ServiceConstants.UNAUTHORIZED_MESSAGE, null);
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response = getJsonResponce(response, sr);
						return false;
					}
				}
				
			}
		} catch (Exception e) {
			Response sr = ServiceResponseUtils.dataResponse(
					ServiceConstants.SYSTEM_ERROR_CODE, e.getMessage(), null);
			response = getJsonResponce(response, sr);
			return false;
		}
	}

	/**
	 * converting the service response object to json response.
	 */
	private HttpServletResponse getJsonResponce(HttpServletResponse response,
			Response sr) throws IOException {
		response.setContentType("application/json");
		String res = new Gson().toJson(sr);
		response.getWriter().println(res);
		return response;
	}

}
