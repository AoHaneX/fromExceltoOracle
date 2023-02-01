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
			final String connectionUrl = "jdbc:oracle:thin:@db-ora-trc3dv.cg76.fr:1521/oraserv_visite76_dv";
			final String userName = "[REDACTED]";
			final String userPass = "[REDACTED]";
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
