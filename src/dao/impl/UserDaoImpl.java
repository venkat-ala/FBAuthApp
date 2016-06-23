package dao.impl;

import java.util.ArrayList;
import java.util.List;

import models.User;
import models.UserTokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import access.DataModifier;
import access.DataRetriever;
import access.QueryParameter;
import dao.UserDao;
import dao.exception.DataAccessException;
import dao.exception.DataServiceException;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private DataModifier dataModifier;

	@Autowired
	private DataRetriever dataRetriever;

	public DataModifier getDataModifier() {
		return dataModifier;
	}

	public void setDataModifier(DataModifier dataModifier) {
		this.dataModifier = dataModifier;
	}

	public DataRetriever getDataRetriever() {
		return dataRetriever;
	}

	public void setDataRetriever(DataRetriever dataRetriever) {
		this.dataRetriever = dataRetriever;
	}

	public User getUserByFbId(User user) throws DataServiceException {
		User userObj = null;
		String fbId = null;
		try {
			if (user != null) {
				fbId = user.getFbId();
				StringBuffer queryString = new StringBuffer(
						"FROM User u where u.fbId=:fbId");
				List<QueryParameter<?>> queryParam = new ArrayList<QueryParameter<?>>();
				queryParam.add(new QueryParameter<String>("fbId", fbId));
				userObj = getDataRetriever().retrieveObjectByQuery(
						queryString.toString(), queryParam);

			}

		} catch (Exception exp) {
			throw new DataServiceException("User data retrival failed.");
		}
		return userObj;
	}

	public List<User> getAllUsers() throws DataServiceException {
		List<User> usersList = null;
		try {
			StringBuffer queryString = new StringBuffer("From User");
			usersList = getDataRetriever().retrieveByQuery(
					queryString.toString());
		} catch (Exception exp) {
			throw new DataServiceException("User data retrival failed.");
		}
		return usersList;
	}

	@Override
	public void saveUserToken(UserTokens userTokens)
			throws DataServiceException {
		try {
			getDataModifier().insert(userTokens);
		} catch (DataAccessException dataAccessException) {
			throw new DataServiceException("Data insertion failed !");
		}
	}
	
	@Override
	public void saveUser(User user)
			throws DataServiceException {
		try {
			getDataModifier().insert(user);
		} catch (DataAccessException dataAccessException) {
			throw new DataServiceException("Data insertion failed !");
		}
	}

	@Override
	public void dodeleteByUserToken(String token) throws DataServiceException {
		try {
			StringBuffer queryString = new StringBuffer(
					" delete From UserTokens where token=:token");
			List<QueryParameter<?>> queryParam = new ArrayList<QueryParameter<?>>();
			queryParam.add(new QueryParameter<String>("token", token));
			dataModifier.executeQuery(queryString.toString(), queryParam);
		} catch (DataAccessException dataAccessException) {
			throw new DataServiceException("Data deletion failed !");
		}
	}

	@Override
	public void updateUserToken(UserTokens userTokens)
			throws DataServiceException {
		try {
			getDataModifier().update(userTokens);
		} catch (DataAccessException dataAccessException) {
			throw new DataServiceException("Data updation failed !");
		}
		
	}

	@Override
	public UserTokens getUserTokenByToken(String token)
			throws DataServiceException {
		UserTokens userTokens = null;
		try {
			StringBuffer queryString = new StringBuffer(
					"FROM UserTokens ut where ut.token=:token");
			List<QueryParameter<?>> queryParameters = new ArrayList<QueryParameter<?>>();
			queryParameters.add(new QueryParameter<String>("token", token));
			userTokens = getDataRetriever().retrieveObjectByQuery(queryString.toString(), queryParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DataServiceException("Data retrivel failed !");
		}
		return userTokens;
	}
	
	

}
