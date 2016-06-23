package service;

import java.util.List;

import models.User;
import models.UserTokens;
import service.exception.BusinessServiceException;

public interface UserService {

	public User getUserByFbId(User user) throws BusinessServiceException;

	public List<User> getAllUsers() throws BusinessServiceException;

	void saveUserToken(UserTokens userTokens) throws BusinessServiceException;

	void dodeleteByUserToken(String token) throws BusinessServiceException;

	void updateUserToken(UserTokens userTokens) throws BusinessServiceException;

	UserTokens getUserTokenByToken(String token) throws BusinessServiceException;
}
