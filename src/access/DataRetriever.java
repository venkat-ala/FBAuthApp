package access;

import java.util.List;

import dao.exception.DataAccessException;

public interface DataRetriever {

	<T> T retrieveById(T type, String keyValue) throws DataAccessException;

	<E> List<E> retrieveByQuery(String queryString) throws DataAccessException;

	<T> T retrieveObjectByQuery(String queryString,
			List<QueryParameter<?>> queryParameters) throws DataAccessException;
}
