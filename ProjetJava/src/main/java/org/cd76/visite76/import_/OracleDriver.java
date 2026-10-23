package org.cd76.visite76.import_;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleDriver {
	String driverName = "oracle.jdbc.driver.OracleDriver";
	Connection con = null;
	final static Logger loggerDriver = LoggerFactory.getLogger(OracleDriver.class);

	public OracleDriver() {
		try {
			// Loading Driver for MySql
			Class.forName(driverName);
		} catch (final Exception e) {

			loggerDriver.error(e.getMessage());
		}
	}

	public Connection createConnection() {
		try {
			// Les informations de connexion sont lues dans les variables d'environnement
			final String connectionUrl = System.getenv("VISITE76_DB_URL");
			final String userName = System.getenv("VISITE76_DB_USER");
			final String userPass = System.getenv("VISITE76_DB_PASSWORD");
			con = DriverManager.getConnection(connectionUrl, userName, userPass);
			System.out.println("******* Connection created successfully........");
		} catch (final Exception e) {

			loggerDriver.error(e.getMessage());
		}
		return con;
	}

	public void closeConnection() {
		try {
			this.con.close();
			System.out.println("******* Connection closed Successfully.........");
		} catch (final Exception e) {

			loggerDriver.error(e.getMessage());
		}
	}

	public static void main(String args[]) {
		final OracleDriver con = new OracleDriver();
		con.createConnection();
		con.closeConnection();
	}

}
