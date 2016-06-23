package service.impl;

import java.util.List;

import models.User;
import models.UserTokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.UserService;
import service.exception.BusinessServiceException;
import dao.UserDao;
import dao.exception.DataServiceException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	public User getUserByFbId(User user) throws BusinessServiceException {
		User userObj = null;
		try {
			userObj = userDao.getUserByFbId(user);
			if (userObj == null) {
				userDao.saveUser(user);
				userObj = userDao.getUserByFbId(user);
			}
		} catch (DataServiceException exp) {
			throw new BusinessServiceException("Oops ! Something went wrong !");
		}
		return userObj;
	}

	public List<User> getAllUsers() throws BusinessServiceException {
		List<User> usersList = null;
		try {
			usersList = userDao.getAllUsers();

		} catch (DataServiceException exp) {
			throw new BusinessServiceException("Authentication Failed");
		}
		return usersList;
	}

	@Override
	public void saveUserToken(UserTokens userTokens)
			throws BusinessServiceException {
		try {
			userDao.saveUserToken(userTokens);
		} catch (DataServiceException e) {
			throw new BusinessServiceException(e.getMessage(), e);
		} finally {
			userTokens = null;
		}
	}

	@Override
	public void dodeleteByUserToken(String token)
			throws BusinessServiceException {
		try {
			userDao.dodeleteByUserToken(token);
		} catch (DataServiceException e) {
			throw new BusinessServiceException(e.getMessage(), e);
		}

	}

	@Override
	public void updateUserToken(UserTokens userTokens)
			throws BusinessServiceException {
		try {
			userDao.updateUserToken(userTokens);
		} catch (DataServiceException e) {
			throw new BusinessServiceException(e.getMessage(), e);
		}
		
	}

	@Override
	public UserTokens getUserTokenByToken(String token)
			throws BusinessServiceException {
		UserTokens userTokens = null;
		try {
			userTokens = userDao.getUserTokenByToken(token);
		} catch (DataServiceException e) {
			throw new BusinessServiceException(e.getMessage(), e);
		}
		return userTokens;
	}

}
