package dao;

import java.util.List;

import models.User;
import models.UserTokens;
import dao.exception.DataServiceException;

public interface UserDao {

	public User getUserByFbId(User user) throws DataServiceException;

	public List<User> getAllUsers() throws DataServiceException;

	void saveUserToken(UserTokens userTokens) throws DataServiceException;
	
	void saveUser(User user) throws DataServiceException;

	void dodeleteByUserToken(String token) throws DataServiceException;
	
	void updateUserToken(UserTokens userTokens) throws DataServiceException;
	
	UserTokens getUserTokenByToken(String token) throws DataServiceException;

}
