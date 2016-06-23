package controllers;

import java.util.Date;

import models.User;
import models.UserTokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import response.Response;
import service.UserService;

import common.ServiceConstants;
import common.StringEncryption;

@RestController
@RequestMapping("/controller")
public class LoginController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Response getMethodSample(@RequestBody User user) {
		Response response = new Response();
		try {
			User userObj = userService.getUserByFbId(user);
			UserTokens ut = new UserTokens();
			if (userObj != null) {
				String sec = StringEncryption.getEngryptedString(userObj
						.getEmail()
						+ userObj.getFbId()
						+ new Date()
						+ System.getProperty("os.name").toLowerCase());
				ut.setUserId(userObj);
				ut.setIssuedTime(new Date());
				ut.setToken(sec);
				ut.setLastAccessTime(new Date());
				userService.saveUserToken(ut);
				response.setStatusCode(ServiceConstants.AUTHENTICATION_SUCCESS_CODE);
				response.setDescription(ServiceConstants.AUTHENTICATION_SUCCESS_MESSAGE);
				response.setData(ut);
			} else {
				response.setStatusCode(ServiceConstants.AUTHENTICATION_FAILED_CODE);
				response.setDescription(ServiceConstants.AUTHENTICATION_FAILED_MESSAGE);
				response.setData(null);
			}

			return response;
		} catch (Exception e) {
			response.setStatusCode(ServiceConstants.AUTHENTICATION_FAILED_CODE);
			response.setDescription(e.getMessage());
			return response;
		}
	}
	
}
