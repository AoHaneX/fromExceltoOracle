package org.cd76.visite76.import_;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Visite76 {

	public static void main(String[] args) throws IOException, SQLException {
		OracleDriver connect = null;
		final Logger logger = LoggerFactory.getLogger(POI.class);
		logger.info("Example log from {}", POI.class.getSimpleName());
		try {

			connect = new OracleDriver();
			// Création de l'objet de connexion à la base de donnée
			connect.createConnection(); // Ouvre la connexion
			final Site siteUsed = new Site("Parc de Clères" + "", connect);
			;
			final int idCarte = 23;
			// siteUsed.ajoutSite(); //permet d'ajouter le site en cas de besoin

			if (siteUsed.checkExist() == true) { // Vérifie l'existence du site entré dans la base de donnée peut
													// importe les majuscules.
				final FileInputStream fichierExcel = new FileInputStream(new File(
						"C:\\dev\\workspaces\\Backup\\visite76-import\\target\\classes\\Data\\Tableau_poi.xlsx")); // Définit
																													// le
				// fichier
				// XLSX
				// à
				// utiliser
				// final FileInputStream fichierExcel = new FileInputStream(new File(
				// "C:\\dev\\workspaces\\Backup\\visite76-import\\target\\classes\\Data\\Tableau du contenu des POI -
				// application numérique MTAN.xlsx"));
				// final FileInputStream fichierExcel = new FileInputStream(new
				// File("C:\\dev\\workspaces\\Backup\\visite76-import\\target\\classes\\Data\\tableau du contenu des POI
				// _ MVH.xlsx"));
				// final FileInputStream fichierExcel = new FileInputStream(new File(
				// "C:\\dev\\workspaces\\Backup\\visite76-import\\target\\classes\\Data\\tableau du contenu des POI _
				// MVH.xlsx"));
				// final FileInputStream fichierExcel = new FileInputStream(new File(
				// "C:\\dev\\workspaces\\Backup\\visite76-import\\target\\classes\\Data\\contenu appli ABSG.xlsx"));

				final fonctionXLSX excel = new fonctionXLSX();
				excel.useXLSX(fichierExcel, siteUsed.getSiteId(), connect, idCarte);
			} else {
				logger.info("Le site n'existe pas ou est mal orthographié.");
				logger.info(siteUsed.afficheSite());
				siteUsed.ajoutSite();

			}
		} catch (final SQLException e) {
			logger.error(e.getMessage());
		} finally {
			if (connect != null) {
				connect.closeConnection(); // Ferme la connection
			}

		}

	}
}
