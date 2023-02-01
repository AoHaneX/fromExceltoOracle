package org.cd76.visite76.import_;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class POI {

	private final int lienCarte = 0;
	private final String titrePoi;
	private final String textCourt;
	private final String textLong;
	private final double coordonneeX;
	private final double coordonneeY;
	final List<Tag> listTag = new ArrayList<Tag>();
	final List<Parcours> listParcours = new ArrayList<Parcours>();
	OracleDriver connect = new OracleDriver();
	final static Logger loggerPoi = LoggerFactory.getLogger(POI.class);
	final String statutPoi = "ACTIF";

	public POI(String titre, String texteCourt, String texteLong, double coorX, double coorY, List<Tag> lesTag,
			List<Parcours> lesParcours, OracleDriver connexion) {
		titrePoi = titre;
		textCourt = texteCourt;
		textLong = texteLong;
		coordonneeX = coorX;
		coordonneeY = coorY;
		connect = connexion;
		listTag.addAll(lesTag);
		listParcours.addAll(lesParcours);

	}

	public String getTitre() {
		return titrePoi;
	}

	public String getTextC() {
		return textCourt;
	}

	public String getTextL() {
		return textLong;
	}

	public double getCoorX() {
		return coordonneeX;
	}

	public double getCoorY() {
		return coordonneeY;
	}

	// public int getID() {} à faire

	public void ajoutPOI(int idCarte) throws SQLException {

		try {
			final String query = "INSERT INTO POI(TITRE,TEXTE_COURT,TEXTE_LONG,COORDONNEE_X,COORDONNEE_Y,LIEN_CARTE,CARTE_AFFICHAGE_ID,STATUT) VALUES(?,?,?,?,?,?,?,?)";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setString(1, titrePoi);
			ps.setString(2, textCourt);
			ps.setString(3, textLong);
			ps.setDouble(4, coordonneeX);
			ps.setDouble(5, coordonneeY);
			ps.setInt(6, lienCarte);
			ps.setInt(7, idCarte);
			ps.setString(8, statutPoi);
			ps.executeUpdate();
			ps.close();
		} catch (final SQLException e) {

			loggerPoi.error(e.getMessage());
		}

		// Faire une autre partiecréant les champs POI_TAG
	}

	public int getPoiID() throws SQLException { // Permet de récuperer l'id du POI
		int poiID = -1;
		try {
			final String query = "SELECT ID_ FROM POI WHERE TITRE=?";

			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setString(1, titrePoi);
			final ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				poiID = rs.getInt(1);
			}
			ps.close();
			rs.close();
		} catch (final SQLException e) {

			loggerPoi.error(e.getMessage());
		}

		return poiID;
	}

	public void afficherPoi() {
		System.out.print("Le nom du POI est " + titrePoi + "\n");
		System.out.print("Le texte court est " + textCourt + "\n");
		System.out.print("Le  texte long du POI est " + textLong + "\n");
		// System.out.print("Les coordonnées en X du POi sont " + coordonneeX + "\n");
		// System.out.print("Les coordonnées en Y du POi sont " + coordonneeY + "\n");

	}

	public boolean checkExist(int idCarte) throws SQLException {
		final List<String> listeNomPoi = new ArrayList<String>();
		boolean result = false;

		try {
			final String query = "SELECT TITRE FROM POI WHERE CARTE_AFFICHAGE_ID=?";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setInt(1, idCarte);
			final ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				listeNomPoi.add(rs.getString(1));

			}
			for (final String str : listeNomPoi) {
				final int compar = str.compareToIgnoreCase(titrePoi);
				if (compar == 0) {
					result = true;
				}
			}
			rs.close();
			ps.close();
		} catch (

		final SQLException e) {
			e.printStackTrace();
		}
		return result; // If result==true, le site existe, sinon non

	}

	public void updatePoi() {
		final String statutPoi = "ACTIF";
		try {
			final String query = "UPDATE POI SET TEXTE_COURT=?, TEXTE_LONG=?,COORDONNEE_X=?,COORDONNEE_Y=?,STATUT=? WHERE TITRE=?";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setString(1, textCourt);
			ps.setString(2, textLong);
			ps.setDouble(3, coordonneeX);
			ps.setDouble(4, coordonneeY);
			ps.setString(5, titrePoi);
			ps.setString(6, statutPoi);
			ps.executeUpdate();

			ps.close();
		} catch (final SQLException e) {

			loggerPoi.error(e.getMessage());
		}
	}

	public void afficheParcoursPoi() {
		for (final Parcours str : listParcours) {
			str.afficheNomParcours();
		}
	}

	public void afficheTagPoi() {
		for (final Tag str : listTag) {
			str.afficheNomTag();
		}
	}

	public void liaisonParcours() throws SQLException {
		final int poiId = getPoiID();
		final List<Integer> listeIdTag = getListeLiasonParcoursId();
		for (final Parcours unParcours : listParcours) {

			final int idParcoursDuPoi = unParcours.getParcoursId();
			if (listeIdTag.contains(idParcoursDuPoi) == true) {
				loggerPoi.info("La liason entre le parcour \" {}  \" et le poi \" {}  \" existe déjà",
						unParcours.getNomParcours(), titrePoi);

			}

			else {
				try {
					final String queryinsert = "INSERT INTO PARCOURS_POI(POI_ID,PARCOURS_ID) VALUES(?,?)";
					final PreparedStatement ps1 = connect.con.prepareStatement(queryinsert);
					ps1.setInt(1, poiId);
					ps1.setInt(2, idParcoursDuPoi);
					ps1.executeUpdate();

					loggerPoi.info("La liason entre le parcour \" {}  \" et le poi \" {}  \" vient d'être crée",
							unParcours.getNomParcours(), titrePoi);
					ps1.close();
				} catch (final SQLException e) {

					loggerPoi.error(e.getMessage());
				}
			}
		}
	}

	public List<Integer> getListeLiasonTagId() throws SQLException {
		final int poiId = getPoiID();
		// On récupère l'id du POI utilisé

		final List<Integer> listeIdTag = new ArrayList<Integer>();
		new ArrayList<Integer>();

		try {
			final String query = "SELECT TAG_ID FROM POI_TAG WHERE POI_ID=?";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setInt(1, poiId);
			final ResultSet rs1 = ps.executeQuery();

			while (rs1.next()) {
				listeIdTag.add(rs1.getInt(1));

			}
			rs1.close();
			ps.close();

		} catch (final SQLException e) {

			loggerPoi.error(e.getMessage());
		}

		return listeIdTag; // Renvoi la liste de tous les ids liée à un poi dans la table POI_TAG
	}

	public List<Integer> getListeLiasonParcoursId() throws SQLException {
		final int poiId = getPoiID();
		// On récupère l'id du POI utilisé

		final List<Integer> listeIdParcours = new ArrayList<Integer>();
		new ArrayList<Integer>();

		try {
			final String query = "SELECT PARCOURS_ID FROM PARCOURS_POI WHERE POI_ID=?";
			final PreparedStatement ps = connect.con.prepareStatement(query);
			ps.setInt(1, poiId);
			final ResultSet rs2 = ps.executeQuery();

			while (rs2.next()) {
				listeIdParcours.add(rs2.getInt(1));

			}
			rs2.close();
			ps.close();

		} catch (final SQLException e) {

			loggerPoi.error(e.getMessage());
		}
		return listeIdParcours; // Renvoi la liste de tous les ids liée à un poi dans la table POI_TAG
	}

	public void liaisonTag() throws SQLException {
		final int poiId = getPoiID();
		final List<Integer> listeIdTag = getListeLiasonTagId();
		for (final Tag unTag : listTag) {

			final int idTagDuPoi = unTag.getTagId();
			if (listeIdTag.contains(idTagDuPoi) == true) {
				loggerPoi.info("La liason entre le tag \" {}  \" et le poi" + " \" {}  \" existe déjà",
						unTag.getNomTag(), titrePoi);
			}

			else {
				try {
					final String queryinsert = "INSERT INTO POI_TAG(POI_ID,TAG_ID) VALUES(?,?)";
					final PreparedStatement ps1 = connect.con.prepareStatement(queryinsert);
					ps1.setInt(1, poiId);
					ps1.setInt(2, idTagDuPoi);
					ps1.executeUpdate();
					loggerPoi.info("La liason entre \" {} \" et le tag  \" {}  \" vient d'être crée", titrePoi,
							unTag.getNomTag());
					ps1.close();
				} catch (final SQLException e) {
					loggerPoi.error(e.getMessage());
				}

			}
		}
	}

}
