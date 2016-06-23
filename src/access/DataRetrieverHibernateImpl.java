package access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.exception.DataAccessException;
import dao.exception.DataSourceOperationFailedException;

/**
 * This implementation is based on hibernate and this is used for the Select DML
 * operation This uses hibernate session which is injected through spring
 * container.Transactions managed by spring container.
 * 
 * @author Venkateswarlu Ala
 * 
 */

@Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@SuppressWarnings("unchecked")
public class DataRetrieverHibernateImpl implements DataRetriever, Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public <T> T retrieveById(T type, String keyValue)
			throws DataAccessException {
		T object = null;
		try {
			Session session = getSessionFactory().getCurrentSession();
			ClassMetadata classMetadata = getSessionFactory().getClassMetadata(
					type.getClass());
			String keyName = classMetadata.getIdentifierPropertyName();
			Criteria criteria = session.createCriteria(type.getClass());
			criteria.add(Restrictions.eq(keyName, keyValue));
			object = (T) criteria.uniqueResult();
		} catch (HibernateException hibernateException) {
			throw new DataSourceOperationFailedException(
					"Data retrivel failed !");
		} catch (Exception exception) {
			throw new DataSourceOperationFailedException(
					"Data retrivel failed !");
		}
		return object;
	}

	@Override
	public <E> List<E> retrieveByQuery(String queryString)
			throws DataAccessException {
		List<E> objects = null;
		try {
			Session session = getSessionFactory().getCurrentSession();
			if (queryString != null) {
				Query query = session.createQuery(queryString);
				objects = query.list();
			}
		} catch (HibernateException hibernateException) {
			throw new DataSourceOperationFailedException(
					"Data retrivel failed !");
		} catch (Exception exception) {
			throw new DataSourceOperationFailedException(
					"Data retrivel failed !");
		}
		return objects;
	}

	@Override
	public <T> T retrieveObjectByQuery(String queryString,
			List<QueryParameter<?>> queryParameters) throws DataAccessException {
		T object = null;
		try {
			Session session = getSessionFactory().getCurrentSession();
			if (queryString != null) {
				Query query = session.createQuery(queryString);
				for (QueryParameter<?> queryParameter : queryParameters) {
					if (queryParameter.getValue().getClass().equals(List.class)
							|| queryParameter.getValue().getClass()
									.equals(ArrayList.class)) {
						List<?> parameter = (List<?>) queryParameter.getValue();
						query.setParameterList(queryParameter.getName(),
								parameter);
					} else {
						query.setParameter(queryParameter.getName(),
								queryParameter.getValue());
					}
				}
				object = (T) query.uniqueResult();
			}
		} catch (HibernateException hibernateException) {
			throw new DataSourceOperationFailedException(
					"Data retriveled failed !");
		} catch (Exception exception) {
			throw new DataSourceOperationFailedException(
					"Data retriveled failed !");
		}
		return object;
	}

}
