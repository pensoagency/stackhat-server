package penso.stackhat.builtwith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSon {
	private String json;
	private ArrayList<String> names;
	private ArrayList<String> interestedCurrentBWName;
	private int techCount;
	private String[] interestedTags = { "analytics", "mobile", "cms", "cdn", "hosting", "mx", "Web Server", "feeds",
			"ssl", "ads", "media", "payment", "mapping" };
	private ArrayList<String> wrongURLs;
//	private Map<String, Long> all_tech;
	private static ArrayList<Tech> all_tech = new ArrayList<Tech>();

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public ArrayList<String> getNames() {
		return names;
	}

	public void setNames(ArrayList<String> names) {
		this.names = names;
	}

	public ArrayList<String> getInterestedNames() {
		return interestedCurrentBWName;
	}

	public void setInterestedNames(ArrayList<String> interestedNames) {
		this.interestedCurrentBWName = interestedNames;
	}

	public int getTechCount() {
		return techCount;
	}

	public void setTechCount(int techCount) {
		this.techCount = techCount;
	}

	public ArrayList<String> getWrongURLs() {
		return wrongURLs;
	}

	public void setWrongURLs(ArrayList<String> wrongURLs) {
		this.wrongURLs = wrongURLs;
	}

	/**
	 * return the JSon of a website
	 * 
	 * @author Yuxuan
	 * @param domain the domain need to be analyzed in String format
	 * @return JSon in String format
	 */
	public String getJson(String domain, String APIKey) throws IOException {
		URL url = new URL("https://api.builtwith.com/v13/api.json?" + "KEY=" + APIKey + "&LOOKUP=" + domain);
		URLConnection uc = url.openConnection();

		BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));

		json = br.readLine();
		return json;
	}

	/**
	 * get all the tech used in the website
	 * 
	 * @author Yuxuan
	 * @param JSon generated by Builtwith API, in String format
	 * @return ArrayList contains the names of all techniques used
	 */
	public ArrayList<String> getAllTechNames(String JSon) {
		this.names = new ArrayList<String>();
		// create Json object using json received from "getJson" method
		JSONObject dataJson = new JSONObject(JSon);

		// Json array "Results"
		JSONArray data = dataJson.getJSONArray("Results");
		JSONObject Results = data.getJSONObject(0);
		JSONObject Result = Results.getJSONObject("Result");
		JSONArray Paths = Result.getJSONArray("Paths");

		// get the web domain
		JSONObject Paths2 = Paths.getJSONObject(0);
//		String domain = Paths2.getString("Domain");
//		System.out.println(domain);

//		this.names.add(domain);

		JSONArray Technologies = Paths2.getJSONArray("Technologies");
		// append all technologies' name to list
		for (int i = 0; i < Technologies.length(); i++) {
			JSONObject obj = Technologies.getJSONObject(i);
			String Name = obj.getString("Name");
			this.names.add(Name);
		}
		return this.names;
	}

	public String checkWrongURL(String JSon) {
		this.wrongURLs = new ArrayList<String>();

		JSONObject dataJson = new JSONObject(JSon);
		// Json Array "Errors"
		// if "Errors" is not empty, the url is wrong
		JSONArray errors = dataJson.getJSONArray("Errors");
		if (errors.length() != 0) {
			JSONObject Error = errors.getJSONObject(0);
			String wrongURL = Error.getString("Lookup");
//			System.out.println(wrongURL);
			return wrongURL;
		}
		return null;
	}

	/**
	 * get all the Tech used in the website with specific tags = { "analytics",
	 * "mobile", "cms", "cdn", "hosting", "mx", "Web Server", "feeds", "ssl", "ads",
	 * "media", "payment", "mapping" }
	 * 
	 * @author Yuxuan
	 * @param JSon generated by Builtwith API, in String format
	 * @return ArrayList<Tech> contains the names of all techniques used for each domain
	 */
	public ArrayList<Tech> getInterestedTechNames(String JSon) {

//		this.interestedCurrentBWName = new ArrayList<String>();
		ArrayList<Tech> interestedTech = new ArrayList<Tech>();
		// create Json object using json received from "getJson" method
		JSONObject dataJson = new JSONObject(JSon);

		// Json array "Results"
		JSONArray data = dataJson.getJSONArray("Results");
		JSONObject Results = data.getJSONObject(0);
		JSONObject Result = Results.getJSONObject("Result");
		JSONArray Paths = Result.getJSONArray("Paths");

		// get the web domain
		JSONObject Paths2 = Paths.getJSONObject(0);
//		String domain = Paths2.getString("Domain");
//		System.out.println(domain);

//		interestedNames.add(domain);
//		boolean flag = true;
		JSONArray Technologies = Paths2.getJSONArray("Technologies");
		// append all technologies' name to list
		for (String tag : interestedTags) {
			for (int i = 0; i < Technologies.length(); i++) {
				JSONObject obj = Technologies.getJSONObject(i);
				String Tag = obj.getString("Tag");
				if (tag.equals(Tag)) {
					
					Tech tech = new Tech();
					//check if is active
					//active:last detected time within 1 month range
					tech.setName(obj.getString("Name"));
					tech.setFirstDetectedTime(obj.getLong("FirstDetected"));
					tech.setLastDetectedTime(obj.getLong("LastDetected"));
					
					// format duration from milliseconds to 'years-days' format
					int totaldays = (int) Math.ceil((obj.getLong("LastDetected")-obj.getLong("FirstDetected"))/86400000);
					int years = totaldays/365;
					int days = totaldays - years*365;
					
					String duration="";
					if(years ==0) {
						duration+=days;
						duration+=" days";
					}else {
						duration=duration+years+" years "+days+" days";
					}
					tech.setDuration(duration);
					
//					System.out.println(tech.toString());
					getAll_tech().add(tech);
					if(((System.currentTimeMillis()-tech.getLastDetectedTime())/86400000)<=30) 
						tech.setStatus(true);
					else
						tech.setStatus(false);
					interestedTech.add(tech);
				}
			}
//			flag=true;
		}
		return interestedTech;
	}

	public HashMap<String, Integer> countTech(ArrayList<String> list, HashMap<String, Integer> map) {
		for (String s : list) {
			// skip if it is domain
			if (s.equals(list.get(0))) {
				continue;
			}
			if (map.containsKey(s)) {
				map.put(s, map.get(s) + 1);
			} else {
				map.put(s, 1);
			}
		}
		return map;
	}

	public static ArrayList<Tech> getAll_tech() {
		return all_tech;
	}

	public static void setAll_tech(ArrayList<Tech> all_tech) {
		JSon.all_tech = all_tech;
	}

}