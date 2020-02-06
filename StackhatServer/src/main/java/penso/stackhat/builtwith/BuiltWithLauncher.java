package penso.stackhat.builtwith;

import java.io.IOException;
import java.util.ArrayList;

public class BuiltWithLauncher {

	/**
	 * input:   a list of URLs path: absolute path of template.xlsx pathDatabase:
	 * absolute path of database.xlsx
	 */
	public static void main(String[] args) throws IOException, Exception {

//		String [] args= {
//				"https://www.cpaaustralia.com.au/",
//				"https://www.charteredaccountantsanz.com/",
//				"https://fia.org.au/",
//				"https://www.surgeons.org/",
//				"https://reiv.com.au/",
//				"https://www.vit.vic.edu.au/",
//				"https://aicd.companydirectors.com.au/",
//				"https://www.publicaccountants.org.au/",
//				"https://www.acecqa.gov.au/",
//				"https://www.engineersaustralia.org.au/",
//				"https://www.anzsnm.org.au/",
//				"https://www.aasw.asn.au/",
//				"https://www.icaew.com/",
//				"https://www.facs.org/",
//				"https://www.unimelb.edu.au/",
//				"https://www.deakin.edu.au/",
//				"https://www.thegymnasium.com/",
//				"https://www.facebook.com/business/learn",
//				"https://grasshopper.codes/",
//				"https://central.xero.com/s/learning",
//				"https://ed.ted.com/",
//				"https://www.udemy.com/",
//				"https://academy.hubspot.com/",
//				"https://www.12wbt.com/",
//				"https://en.wikipedia.org/",
//				"https://www.csiro.au/",
//				"https://www.wheelercentre.com/",
//				"https://www.washingtonpost.com/",
//				"https://www.bbc.com/news",
//				"http://www.usatoday.com/",
//				"https://www.nytimes.com/",
//				"http://www.newyorker.com/",
//				"https://www.bostonglobe.com",
//				"https://www.polygon.com/",
//				"https://www.theverge.com/",
//				"https://techcrunch.com/",
//				"https://www.wired.com/",
//				"https://www.quora.com/",
//				"https://feedly.com/",
//				"https://www.reddit.com/",
//				"https://www.domain.com.au/",
//				"https://www.realestate.com.au/"
//		};
		
		
		//in category
//		String[] args = { "https://njca.com.au/", 
//				"https://www.liv.asn.au/", 
//				"https://victorialawfoundation.org.au/",
//				"https://www.judcom.nsw.gov.au/", 
//				"https://www.sentencingcouncil.qld.gov.au/",
//				"https://www.fedcourt.gov.au/",
//				"https://www.courts.vic.gov.au/",
//				"http://www.courts.justice.nsw.gov.au/",
//				"https://aija.org.au/",
//				"https://www.jca.asn.au/",
//				"https://www.vicbar.com.au/", 
//				"http://www.austlii.edu.au/", 
//				"https://www.lawhandbook.org.au/",
//				"https://lawlex.com.au/", 
//				"https://www.lexisnexis.com.au/en/products-and-services/lexis-red",
//				"http://lexisweb.lexisnexis.com.au/Home.aspx", 
//				"https://legal.thomsonreuters.com.au/",
//				"https://jade.io/",
//				"https://www.lawlibrary.vic.gov.au/", 
//				"http://www.legislation.vic.gov.au/",
//				"https://www.sentencingcouncil.vic.gov.au/",
//				"www.lawreform.vic.gov.au",
//				"www.alrc.gov.au/",
//				"www.lawreform.justice.nsw.gov.au/",
//				"https://www.qls.com.au/About_QLS/Queensland_Law_Society/Resources_publications/Newsletters_magazines/Proctor",
//				"https://lsj.com.au/",
//				"https://www.parliament.vic.gov.au/assembly/tabled-documents",
//				"https://www.parliament.vic.gov.au/publications",
//				"https://www.crimestatistics.vic.gov.au/",
//				"https://apo.org.au/",
//				"https://www.premier.vic.gov.au/media-centre/#results",
//				"http://www.queenslandreports.com.au/qlr/",
//				"https://www.judges.org/",
//				"https://www.supremecourt.gov.sg/sjc/home", 
//				"https://mjc.olemiss.edu/",
//				"https://cjc-ccm.ca/en",
//				"https://www.nji-inm.ca/",
//				"https://www.judiciary.uk/about-the-judiciary/training-support/judicial-college/",
//				"http://www.iojt.org/", 
//				"http://nasje.org/", 
//				"https://ncpj.org/", 
//				"https://www.nawj.org/",
//				"http://www.ejtn.eu/",
//				"https://www.ncsc.org/"};
		String path = "C:\\Users\\myxll\\Desktop\\test\\tmp.xlsx";
		String pathDatabase = "C:\\Users\\myxll\\Desktop\\test\\database.xlsx";

		String APIKey = "c8fb6490-26a8-46bd-88a9-ac26d7994656"; // API

		Program program = new Program();
		ArrayList<String> log = new ArrayList<String>();
		program.start(args, log, path, pathDatabase, APIKey);
	}

}