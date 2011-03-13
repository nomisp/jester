package ch.jester.common.tests;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.Assert;

import org.hibernate.Session;
import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.hibernate.helper.ConfigurationHelper;
import ch.jester.hibernate.helper.HibernatehelperPlugin;

public class DBTest extends ActivatorProviderForTestCase {

	/**
	 * Ist der Activator vorhanden?
	 */
	@Test
	public void testDB() {
	
		String catalog="???";
		Session ssn = HibernatehelperPlugin.getSession();
		Connection con = ssn.connection();
		try {
			catalog = con.getCatalog();			
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


}
