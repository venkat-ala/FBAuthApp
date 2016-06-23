package access;

import java.util.List;

import dao.exception.DataAccessException;

public interface DataModifier {

	<T> void insert(T type) throws DataAccessException;

	<T> void update(T type) throws DataAccessException;
	
	Integer executeQuery(String queryString,
			List<QueryParameter<?>> queryParameters) throws DataAccessException;

}
