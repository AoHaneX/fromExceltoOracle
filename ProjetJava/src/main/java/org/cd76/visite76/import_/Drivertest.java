package org.cd76.visite76.import_;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Drivertest {
	String driverName = "oracle.jdbc.driver.OracleDriver";
	Connection con = null;
	final static Logger loggerDriver = LoggerFactory.getLogger(Drivertest.class);

	public Drivertest() {
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
			final String userName = "VISITE76";
			final String userPass = "A4f82d4991fae7c2e5_201031a";
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
		final Drivertest con = new Drivertest();
		con.createConnection();
		con.closeConnection();
	}

}
