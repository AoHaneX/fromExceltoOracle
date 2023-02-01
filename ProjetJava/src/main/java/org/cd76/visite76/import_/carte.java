package org.cd76.visite76.import_;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class carte {
	int siteId;
	int poiId;
	OracleDriver connect = new OracleDriver();
	final static Logger loggercarte = LoggerFactory.getLogger(carte.class);

	public carte(Site siteUtil, POI POIUtil, OracleDriver connection) throws SQLException {
		siteId = siteUtil.getSiteId();
		poiId = POIUtil.getPoiID();
		// poiID=POIUTIL.getID()
		connect = connection;
	}

	public void creerLiaison() { // Insère la liason entre un tag et un Poi dans la base de
		try {
			final String query = "INSERT INTO CARTE() VALUES(?,?)";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setInt(1, siteId);
			ps.setInt(2, poiId);
			ps.executeUpdate();
		} catch (final SQLException e) {

			loggercarte.error(e.getMessage());
		}

	}

}
