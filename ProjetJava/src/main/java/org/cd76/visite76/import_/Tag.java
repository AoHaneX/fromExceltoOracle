/**
 *
 */
package org.cd76.visite76.import_;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sstad0
 *
 */
public class Tag {
	private final String nomTag;
	private final int idSite;

	OracleDriver connect = new OracleDriver();
	final static Logger loggerTag = LoggerFactory.getLogger(Tag.class);

	public Tag(String nom, int idSiteUsed, OracleDriver connection) throws SQLException {
		connect = connection;
		idSite = idSiteUsed;

		nomTag = nom.trim();

	}

	public String getNomTag() {
		return nomTag;
	}

	public void ajouterTag() throws SQLException {

		try {
			final String query = "INSERT INTO TAG(LABEL,SITE_ID) VALUES(?,?)";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setString(1, nomTag);
			ps.setInt(2, idSite);
			ps.executeUpdate();

		} catch (final SQLException e) {
			loggerTag.error(e.getMessage());
		}

	}

	public boolean checkExist(List<String> listeTag) throws SQLException {
		boolean result = false;
		for (final String str : listeTag) {
			final int compar = str.compareToIgnoreCase(nomTag);
			if (compar == 0) {
				result = true;
			}
		}

		return result; // If result==true, le site existe, sinon non

	}

	public void afficheNomTag() {
		System.out.print("\n" + nomTag + "\n");
	}

	public int getTagId() throws SQLException {
		int tagId = -1;
		try {
			final String query = "SELECT ID_ FROM TAG WHERE LABEL=?";

			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setString(1, nomTag);
			final ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				tagId = rs.getInt(1);
			}
		} catch (final SQLException e) {

			loggerTag.error(e.getMessage());
		}
		return tagId;
	}

}
