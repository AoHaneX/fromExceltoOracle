package org.cd76.visite76.import_;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Site {
	String nomSite;
	OracleDriver connect = new OracleDriver();
	final static Logger loggerSite = LoggerFactory.getLogger(Site.class);

	public Site(String nomduSite, OracleDriver connection) {
		nomSite = nomduSite;
		connect = connection;

	}

	public String getNomSite() {
		return nomSite;
	}

	public int getSiteId() throws SQLException { // Permet de récuperer l'id du POI
		int siteId = -1;
		final List<String> listeSite = new ArrayList<String>();
		final ArrayList<Integer> listeId = new ArrayList<Integer>();
		int compteur = -1;
		try {
			final String query = "SELECT NOM_SITE,ID_ FROM SITE WHERE NOM_SITE=?"; // Méthode à re vérifier
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setString(1, nomSite);
			final ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				listeSite.add(rs.getString(1));
				listeId.add(rs.getInt(2));
			}
		} catch (final SQLException e) {

			loggerSite.error(e.getMessage());
		}
		for (final String str : listeSite) {
			final int compar = str.compareToIgnoreCase(nomSite);
			compteur++;
			if (compar == 0) {
				siteId = listeId.get(compteur);

			}
		}

		return siteId;
	}

	public boolean checkExist() throws SQLException { // Permet de vérifié si le site saisit existe dans la base de
														// donnée en renvoyant un true si c'est le cas. Fonctionne avec
														// majuscule ou non mais pas avec les accents /!\
		final List<String> listeNom = new ArrayList<String>();
		boolean result = false;
		final String query = "SELECT NOM_SITE FROM SITE";
		final PreparedStatement ps = connect.con.prepareStatement(query);
		final ResultSet rs = ps.executeQuery();
		try {
			while (rs.next()) {
				listeNom.add(rs.getString(1));

				// System.out.print(rs.getString(1) + "\n");
			}
			for (final String str : listeNom) {
				final int compar = str.compareToIgnoreCase(nomSite);
				if (compar == 0) {
					result = true;
				}
			}
			rs.close();
		} catch (final SQLException e) {

			loggerSite.error(e.getMessage());
		}
		return result; // If result==true, le site existe, sinon non

	}

	public String afficheSite() throws SQLException { // Affiche la liste de tous les sites de la base de donnée
		final String query = "SELECT NOM_SITE FROM SITE";
		final StringBuilder sb = new StringBuilder();
		final PreparedStatement ps = connect.con.prepareStatement(query);
		final ResultSet rs = ps.executeQuery();
		try {
			while (rs.next()) {
				sb.append(rs.getString(1)).append("\n");
			}
			rs.close();
		} catch (final SQLException e) {

			loggerSite.error(e.getMessage());
		}
		return sb.toString();
	}

	public void ajoutSite() throws SQLException { // Permet d'ajouter un site à la base de donnée

		try {
			final String query = "INSERT INTO SITE(NOM_SITE) VALUES(?)";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			// System.out.print("\n" + query + "\n");
			ps.setString(1, nomSite);
			ps.executeUpdate();

		} catch (final SQLException e) {

			loggerSite.error(e.getMessage());
		}

	}

}
