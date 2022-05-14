package de.deadlocker8.budgetmaster.databasemigrator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;


public class CustomIdGenerator extends IdentityGenerator implements IdentifierGenerator
{
	public static final String GENERATOR = "de.deadlocker8.budgetmaster.databasemigrator.CustomIdGenerator";

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException
	{
		final Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);

		if(id == null)
		{
			return super.generate(session, object);
		}
		else
		{
			return id;
		}
	}
}
