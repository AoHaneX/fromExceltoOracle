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
			// Cr�ation de l'objet de connexion � la base de donn�e
			connect.createConnection(); // Ouvre la connexion
			final Site siteUsed = new Site("Parc de Cl�res" + "", connect);
			;
			final int idCarte = 23;
			// siteUsed.ajoutSite(); //permet d'ajouter le site en cas de besoin

			if (siteUsed.checkExist() == true) { // V�rifie l'existence du site entr� dans la base de donn�e peut
													// importe les majuscules.
				final FileInputStream fichierExcel = new FileInputStream(new File(
						"C:\\dev\\workspaces\\Backup\\visite76-import\\target\\classes\\Data\\Tableau_poi.xlsx")); // D�finit
																													// le
				// fichier
				// XLSX
				// �
				// utiliser
				// final FileInputStream fichierExcel = new FileInputStream(new File(
				// "C:\\dev\\workspaces\\Backup\\visite76-import\\target\\classes\\Data\\Tableau du contenu des POI -
				// application num�rique MTAN.xlsx"));
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
				logger.info("Le site n'existe pas ou est mal orthographi�.");
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
