package controllers;

import java.util.List;

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

@RestController
@RequestMapping("/secure/controller")
public class ProtectedController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/userDetails", method = RequestMethod.GET)
	public Response getAllUsers() {
		Response response = new Response();
		List<User> userObj = null;
		try {
			userObj = userService.getAllUsers();
			if (userObj != null) {
				response.setStatusCode(ServiceConstants.DATA_RETRIVAL_SUCCESS_CODE);
				response.setDescription(ServiceConstants.DATA_RETRIVAL_SUCCESS_MESSAGE);
				response.setData(userObj);
			} else {
				response.setStatusCode(ServiceConstants.DATA_RETRIVAL_FAILED_CODE);
				response.setDescription(ServiceConstants.DATA_RETRIVAL_FAILED_MESSAGE);
				response.setData(null);
			}

			return response;
		} catch (Exception e) {
			response.setStatusCode(ServiceConstants.DATA_RETRIVAL_FAILED_CODE);
			response.setDescription(e.getMessage());
			return response;
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public Response logout(@RequestBody UserTokens userTokens) {
		Response response = new Response();
		User userObj = null;
		try {
			userService.dodeleteByUserToken(userTokens.getToken());
			response.setStatusCode(ServiceConstants.LOGOUT_SUCCESS_CODE);
			response.setDescription(ServiceConstants.LOGOUT_SUCCESS_MESSAGE);
			response.setData(userObj);

			return response;
		} catch (Exception e) {
			response.setStatusCode(ServiceConstants.LOGOUT_FAILED_CODE);
			response.setDescription(e.getMessage());
			return response;
		}
	}
}
