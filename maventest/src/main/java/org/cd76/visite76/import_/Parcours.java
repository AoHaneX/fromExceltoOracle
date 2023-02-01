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
public class Parcours {
	private final String nomParcours;
	OracleDriver connect = new OracleDriver();
	private final int idSite;
	private final String constanteParcoursIcone = "Icone.png";
	final static Logger loggerParcours = LoggerFactory.getLogger(Parcours.class);

	public Parcours(String nom, int idSiteUsed, OracleDriver connection) throws SQLException {
		connect = connection;
		nomParcours = nom.trim();
		idSite = idSiteUsed;

	}

	public String getNomParcours() {
		return nomParcours;
	}

	/**
	 * public void ajouterParcour() throws SQLException {
	 *
	 * try { final String query = "INSERT INTO PARCOURS(TITRE,SITE_ID) VALUES(?,?)"; final PreparedStatement ps =
	 * connect.con.prepareStatement(query); // System.out.print("\n" + query + "\n"); ps.setString(1, nomParcours);
	 * ps.setInt(2, idSite); ps.executeUpdate();
	 *
	 * } catch (final SQLException e) { e.printStackTrace(); }
	 *
	 * }
	 */

	public boolean checkExist(List<String> listeParcours) throws SQLException { // Permet de vérifié si le tag saisit
																				// existe
		// dans la base de
		// donnée en renvoyant un true si c'est le cas. Fonctionne avec
		// majuscule ou non mais pas avec les accents /!\
		boolean result = false;
		for (final String str : listeParcours) {
			final int compar = str.compareToIgnoreCase(nomParcours);
			if (compar == 0) {
				result = true;
			}
		}

		return result; // If result==true, le site existe, sinon non

	}

	public void afficheNomParcours() {
		System.out.print("\n" + nomParcours + "\n");
	}

	public int getParcoursId() throws SQLException {
		int parcoursId = -1;
		try {
			final String query = "SELECT ID_ FROM PARCOURS WHERE TITRE=?";

			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setString(1, nomParcours);
			final ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				parcoursId = rs.getInt(1);
			}

			rs.close();
			ps.close();
		} catch (final SQLException e) {

			loggerParcours.error(e.getMessage());
		}
		return parcoursId;
	}

	public void ajouterParcours() throws SQLException {

		try {
			final String statutParcours = "ACTIF";
			final String query = "INSERT INTO PARCOURS(TITRE,SITE_ID,ICONE,STATUT,ACCESSIBLE_PMR,DUREE_MINUTES,MISE_EN_AVANT) VALUES(?,?,?,?,0,0,0)";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setString(1, nomParcours);
			ps.setInt(2, idSite);
			ps.setString(3, constanteParcoursIcone);
			ps.setString(4, statutParcours);
			ps.executeUpdate();

			ps.close();
		} catch (final SQLException e) {
			loggerParcours.error(e.getMessage());
		}

	}
}
