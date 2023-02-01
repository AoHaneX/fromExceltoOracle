package org.cd76.visite76.import_;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class fonctionXLSX {
	// Rajouter un constructeur/gettet/setter ?
	final static Logger loggerFonctionXLSX = LoggerFactory.getLogger(fonctionXLSX.class);

	public static void useXLSX(FileInputStream fichier, int siteId, OracleDriver connect, int idDeLaCarte)
			throws IOException, SQLException {
		// Les variables locales qui vont être extraite de chaque POI présent dans le fichier excel :
		String nomPoi = "";
		String texteCourt = "";
		String texteLong = "";
		final List<String> listParcoursTextUnPoiLocal = new ArrayList<String>();
		final List<String> listTagTextUnPoiLocal = new ArrayList<String>();
		double coorX = 0;
		double coorY = 0;
		boolean choixCoor = false; // Variable servant à différencier les coordonées X et Y

		final XSSFWorkbook wb = new XSSFWorkbook(fichier);
		final XSSFSheet sheet = wb.getSheetAt(0);
		final FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
		final int idCarte = idDeLaCarte;

		new ArrayList<POI>();

		final List<Parcours> listParcoursUnSite = new ArrayList<Parcours>();

		final List<String> listNomParcoursUnSite = new ArrayList<String>();
		final List<Tag> listTagUnSite = new ArrayList<Tag>();

		final List<String> listNomTagUnSite = new ArrayList<String>();

		boolean premierTour = true; // Variable permettant de repérer le premier tour lors du parcours du tableau pour
									// éviter que la ligne correspondante soit cosdiérer comme un Poi à extraire
		boolean fin = false; // Variable servant d'éviter que les lignes vides ne causent de problèmes à la fin du
								// fichier
		for (final Row ligne : sheet) {// parcourir les lignes
			if (fin == true) {
				break;
			}
			int numColoneActuel = 1;
			for (final Cell cell : ligne) {// parcourir les colonnes
				switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
				case Cell.CELL_TYPE_BLANK:

					if (numColoneActuel == 1) {
						fin = true;
						break;
					}

				case Cell.CELL_TYPE_NUMERIC:
					cell.getNumericCellValue();
					if (numColoneActuel == 1) { // If l'élement va dans la table POI
						numColoneActuel++;
						break;
					}

				case Cell.CELL_TYPE_STRING:
					final String celluletexte = cell.getStringCellValue();

					if (numColoneActuel == 1) { // Si la colone est est le numéro
						numColoneActuel++;
					} else if (numColoneActuel == 2) { // Si la colone est Titre
						nomPoi = celluletexte;
						numColoneActuel++;
					} else if (numColoneActuel == 3) { // Si la colone est texte court
						texteCourt = celluletexte;
						numColoneActuel++;
					} else if (numColoneActuel == 4) { // Si la colone est texte long
						texteLong = celluletexte;
						numColoneActuel++;
					} else if (numColoneActuel == 5) { // Si la colone est parcours
						final StringTokenizer st = new StringTokenizer(celluletexte, ";");
						numColoneActuel++;
						while (st.hasMoreTokens()) {
							listParcoursTextUnPoiLocal.add(st.nextToken());

						}
						System.out.print("\n");
					} else if (numColoneActuel == 6 && premierTour == false) { // Si la colone est tag
						final StringTokenizer st = new StringTokenizer(celluletexte, ";");
						while (st.hasMoreTokens()) {
							listTagTextUnPoiLocal.add(st.nextToken());
						}
						numColoneActuel++;
					} else if (numColoneActuel == 7 && premierTour == false) { // Si la colone est coordonnée
						numColoneActuel++;
						final StringTokenizer st = new StringTokenizer(celluletexte, ",");
						choixCoor = false; // On évite que le résultat du dernier parcours de la boucle entre en compte
						while (st.hasMoreTokens()) {

							if (choixCoor == false && premierTour == false) {
								coorX = Double.parseDouble(st.nextToken());
								choixCoor = true;
							} else if (choixCoor == true && premierTour == false) {
								coorY = Double.parseDouble(st.nextToken());
							} else {
								numColoneActuel++;
								break;
							}

						}

					} else {
						numColoneActuel++;
					}

				}

			}

			if (premierTour == false && fin == false) {
				boolean result;
				listNomTagUnSite.addAll(extractionTag(fichier, siteId, connect));
				listNomParcoursUnSite.addAll(extractionParcours(fichier, siteId, connect));
				try { // Permet de vérifier si les tags d'un Poi existent et sinon, de les créer

					for (final String str : listTagTextUnPoiLocal) {

						final Tag unTag = new Tag(str, siteId, connect);
						result = unTag.checkExist(listNomTagUnSite);
						if (result == true) {

							listTagUnSite.add(unTag);

						} else {
							unTag.ajouterTag();

							listTagUnSite.add(unTag);
							loggerFonctionXLSX.info("Le tag \" {}  \" vient d'être ajoutée à la base de donnée",
									unTag.getNomTag());
							listNomTagUnSite.add(unTag.getNomTag());

						}
					}

				} catch (final SQLException e) {

					loggerFonctionXLSX.error(e.getMessage());
				}
				try { // Permet de vérifier si les parcours d'un Poi existent et sinon, de les créer

					for (final String str : listParcoursTextUnPoiLocal) {

						final Parcours unParcours = new Parcours(str, siteId, connect);
						result = unParcours.checkExist(listNomParcoursUnSite);
						if (result == true) {

							listParcoursUnSite.add(unParcours);

						} else {
							unParcours.ajouterParcours();

							listParcoursUnSite.add(unParcours);
							loggerFonctionXLSX.info("Le parcours \" {}  \" vient d'être ajoutée à la base de donnée",
									unParcours.getNomParcours());
							listNomParcoursUnSite.add(unParcours.getNomParcours());

						}
					}

				} catch (final SQLException e) {

					loggerFonctionXLSX.error(e.getMessage());
				}

				final POI poiExtrait = new POI(nomPoi, texteCourt, texteLong, coorX, coorY, listTagUnSite,
						listParcoursUnSite, connect);

				if (poiExtrait.checkExist(idCarte) == true) {
					loggerFonctionXLSX.info("Le poi \" {}  \" vient d'être mis à jour si c'était nécessaire", nomPoi);
					poiExtrait.updatePoi();
				} else {
					poiExtrait.ajoutPOI(idCarte);
					loggerFonctionXLSX.info("Le poi \" {}  \" vient d'être ajouté à la base de donnée", nomPoi);
				}

				// poiExtrait.afficherPoi();

				poiExtrait.liaisonParcours();
				poiExtrait.liaisonTag();

				listParcoursTextUnPoiLocal.clear();
				listTagTextUnPoiLocal.clear();
				listParcoursUnSite.clear();
				listTagUnSite.clear();
				nomPoi = "";
				texteCourt = "";
				texteLong = "";
				// System.out.print(ligne1);
				// Penser à clean les listes
				// public void ajouterPOI(POI ....)

			} else {
				listParcoursTextUnPoiLocal.clear();
			}
			premierTour = false;
		}
		wb.close();

	}

	public static List<String> extractionTag(FileInputStream fichier, int idSite, OracleDriver connect)
			throws SQLException {
		final List<String> listeTag = new ArrayList<String>();

		final String query = "SELECT LABEL FROM TAG WHERE SITE_ID=?";
		final PreparedStatement ps = connect.con.prepareStatement(query);
		ps.setInt(1, idSite);
		final ResultSet rs = ps.executeQuery();
		try {
			while (rs.next()) {
				listeTag.add(rs.getString(1));

				// System.out.print(rs.getString(1) + "\n");
			}
		} catch (final SQLException e) {

			loggerFonctionXLSX.error(e.getMessage());
		} finally {
			rs.close();
		}

		return listeTag;

	}

	public static List<String> extractionParcours(FileInputStream fichier, int idSite, OracleDriver connect)
			throws SQLException {
		final List<String> listeParcours = new ArrayList<String>();

		final String query = "SELECT TITRE FROM PARCOURS WHERE SITE_ID=?";
		final PreparedStatement ps = connect.con.prepareStatement(query);
		ps.setInt(1, idSite);
		final ResultSet rs = ps.executeQuery();
		try {
			while (rs.next()) {
				listeParcours.add(rs.getString(1));

				// System.out.print(rs.getString(1) + "\n");
			}
		} catch (final SQLException e) {

			loggerFonctionXLSX.error(e.getMessage());
		} finally {
			rs.close();
		}

		return listeParcours;

	}

}
