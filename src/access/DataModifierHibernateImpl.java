package access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.exception.DataAccessException;
import dao.exception.DataSourceOperationFailedException;
import dao.exception.DuplicateRecordException;

/**
 * This implementation is based on hibernate and this is used for the following
 * DML operation Insert , Update , Delete This uses hibernate session which is
 * injected through spring container and also the transaction managed by spring
 * container.
 * 
 * @author Venkateswarlu Ala
 * 
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = DataAccessException.class)
public class DataModifierHibernateImpl implements DataModifier, Serializable {
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
	public <T> void insert(T type) throws DataAccessException {
		try {
			Session session = getSessionFactory().getCurrentSession();
			session.save(type);
			session.flush();
		} catch (ConstraintViolationException cvException) {
			throw new DuplicateRecordException("Dulicate records exception !");
		} catch (HibernateException hibernateException) {
			throw new DataSourceOperationFailedException(
					hibernateException.getMessage());
		} catch (Exception exception) {
			throw new DataSourceOperationFailedException(exception.getMessage());
		}
	}

	@Override
	public <T> void update(T type) throws DataAccessException {
		try {
			Session session = getSessionFactory().getCurrentSession();
			session.clear();
			session.saveOrUpdate(type);
			session.flush();
		} catch (ConstraintViolationException cvException) {
			throw new DuplicateRecordException("Dulicate records exception !");
		} catch (HibernateException hibernateException) {
			throw new DataSourceOperationFailedException(
					hibernateException.getMessage());
		} catch (Exception exception) {
			throw new DataSourceOperationFailedException(exception.getMessage());
		}
	}

	@Override
	public Integer executeQuery(String queryString,
			List<QueryParameter<?>> queryParameters) throws DataAccessException {
		Integer noOfRowsUpdated = 0;
		try {
			Session session = getSessionFactory().getCurrentSession();
			if (queryString != null) {
				Query query = session.createQuery(queryString);
				for (QueryParameter<?> queryParameter : queryParameters) {
					if (queryParameter.getValue() != null
							&& (queryParameter.getValue().getClass()
									.equals(List.class) || queryParameter
									.getValue().getClass()
									.equals(ArrayList.class))) {
						List<?> parameter = (List<?>) queryParameter.getValue();
						query.setParameterList(queryParameter.getName(),
								parameter);
					} else {
						query.setParameter(queryParameter.getName(),
								queryParameter.getValue());
					}
				}
				noOfRowsUpdated = query.executeUpdate();
			}
			return noOfRowsUpdated;
		} catch (HibernateException hibernateException) {
			throw new DataSourceOperationFailedException(
					hibernateException.getMessage());
		} catch (Exception exception) {
			throw new DataSourceOperationFailedException(exception.getMessage());
		}
	}
}
