/**
 * Profile crawler from the Web
 *
 * @author: Duy Dinh
 * @date: 10 july 2015
 * @org: Luxembourg Institute of Science and Technology
 */
package profile.crawler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.FileManager;

/**
 *
 * @author Duy Dinh <duy.dinh@tudor.lu>
 */
public class ProfileCrawler extends Settings {

    private static final String USER_AGENT = "Mozilla/5.0";
    // local css selector map
    protected HashMap<String, String> cssSelectors = new HashMap<String, String>();
    // global CSS selector map
    protected HashMap<String, String> cssSelectorsMap = new HashMap<String, String>();
    // map of css selector maps, key = social network and value is a set of local css selectors
    protected HashMap<String, HashMap<String, String>> selectorMap = new HashMap<String, HashMap<String, String>>();

    Logger logger;
    /**
     *
     */
    protected Map<String, String> profileEntries;
    protected ArrayList<String> keywordList;

    public Map<String, String> getKeywords() {
        return profileEntries;
    }

    public void setKeywords(Map<String, String> keywords) {
        this.profileEntries = keywords;
    }

    public ProfileCrawler() {
        logger = Logger.getLogger(ProfileCrawler.class.getName());

        profileEntries = new HashMap<String, String>();
        keywordList = new ArrayList<String>();
        loadCSSSelectors();

    }

    /**
     * Extract data from social network url
     *
     * @url social network url
     */
    public String get(String url) {

        StringBuilder sb = new StringBuilder();
        try {

            Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
            //            System.out.println(doc.toString());

            // for each css selector
            Set<String> cssSelectorEntries = cssSelectors.keySet();

            for (String cssSelector : cssSelectorEntries) {

                logger.info(String.format("*** Extracting data from '%s' using selector '%s'\n", url, cssSelector));

                Elements elements = doc
                        .select(cssSelector);

                // System.out.format("There are %d elements found using selector %s.\n", elements.size(), cssSelector);
                for (Element element : elements) {
                    //System.out.format("%s\t[%s]\t[%s]==>\t%s\n", element.text(), element.className(), element.absUrl("href"), element.toString());

                    profileEntries.put(element.text(), cssSelector);
                    keywordList.add(String.format("%s\t%s", element.text(), cssSelector));
                }

                // printKeywords();
            }

        } catch (IOException ex) {
            Logger.getLogger(ProfileCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    /**
     *
     * @param mode
     */
    public void printKeywords(int mode) {
        switch (mode) {
            case OUTPUT_FORMAT.JSON:
                printKeywordsJSON();
                break;
            case OUTPUT_FORMAT.XML:
                printKeywordsXML();
                break;
            default:
                printKeywords();
                break;
        }
    }

    public void printKeywords() {

        logger.info(String.format("\n\n*** SUMMARY: \nThere are %d entries found from the user profile.\n", profileEntries.size()));
        Set<Entry<String, String>> entries = profileEntries.entrySet();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (Entry keyword : entries) {
            logger.info(String.format("[%d] %s\t%s\n", i++, keyword.getKey(), cssSelectorsMap.get((String) keyword.getValue()).toLowerCase()));
            sb.append(String.format("%s\t%s\n", keyword.getKey(), cssSelectorsMap.get((String) keyword.getValue()).toLowerCase()));
        }

        //FileManager.WriteToFile(sb.toString(), properties.getProperty("output.profile", "output/profile.txt"));
//        i = 0;
//        for (String keyword: keywordList){
//            System.out.format("[%d] %s\n", i++, keyword);
//        }
    }

    /**
     * Print extracted keywords in JSON format
     */
    private String printKeywordsJSON() {

        JSONObject obj = new JSONObject();
        Set<Entry<String, String>> entries = profileEntries.entrySet();

        // combine keywords into the same category
        Set<Entry<String, String>> cssEntries = cssSelectorsMap.entrySet();

        for (Entry cssEntry : cssEntries) {

            String category = (String) cssEntry.getValue();
            //System.out.println(category);
            obj.put(category, combineKeywords(category, entries));
        }

        System.out.println(obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * Print extracted keywords in XML format
     */
    private void printKeywordsXML() {
    }

    public JSONArray combineKeywords(String category, Set<Entry<String, String>> entries) {
        JSONArray array = new JSONArray();
        for (Entry keyword : entries) {

            if (cssSelectorsMap.get((String) keyword.getValue()).toLowerCase().equalsIgnoreCase(category)) {
                array.add(keyword.getKey());
            }
        }
        return array;
    }

    /**
     * Run the extraction from input/url_list.txt
     */
    void demo() {
        logger.info("Running demo ...");
        ArrayList<String> lines = FileManager.ReadLines(properties.getProperty("profile.file.urls"));

        for (String line : lines) {
            try {
                if (line.startsWith("#") || line.startsWith("--") || line.startsWith("//") || line.trim().length() == 0) {
                    continue;
                }
                String[] fields = line.split("[\t]");
                if (fields.length < 2) {
                    continue;
                }

                String url = fields[0];

                for (int i = 1; i < fields.length; i++) {
                    String[] selectors = fields[i].split("[|]+");
                    cssSelectors.put(selectors[0], selectors[1]);
                    cssSelectorsMap.put(selectors[0].toLowerCase(), selectors[1]);
                }

                get(url);

                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProfileCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }

            printKeywords(OUTPUT_FORMAT.JSON);
        }
    }

    /**
     * Run the extraction from user profile
     */
    void extractDataFromProfile(String profileURL) {
        // Dowload user profile
        logger.info(profileURL);

        StringBuilder jsonContent = downloadUserProfile(profileURL, properties.getProperty("user.name", "admin"), properties.getProperty("user.password", "admin"));

        logger.info(jsonContent.toString());

        try {

            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(jsonContent.toString());

            if (array.size() == 0) {
                return;
            }
            JSONObject obj2 = (JSONObject) array.get(0);
            //System.out.println("Field \"id\" \"social_profiles\"");
            //System.out.format("%s\t%s\n", obj2.get("id"), obj2.get("social_profiles"));
            Object socialProfileObj = obj2.get("social_profiles");
            JSONArray list = (JSONArray) parser.parse(socialProfileObj.toString());
            for (int i = 0; i < list.size(); i++) {
                //System.out.format("[%s] %s\n", i, list.get(i));
                JSONObject obj = (JSONObject) list.get(i);
                String socialNetwork = obj.get("service_name").toString();
                String url = obj.get("url").toString();
                //System.out.println(socialNetwork + " " + url);
                cssSelectors = selectorMap.get(socialNetwork.toLowerCase());
                if (cssSelectors != null) {
                    get(url);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        printKeywords();

    }

    /**
     *
     * @param urlStr user profile EDF URL
     * @param user userName
     * @param pass password
     * @param outFilePath output file
     */
    public StringBuilder downloadUserProfile(String urlStr, String user, String pass) {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(urlStr);

        httpGet.addHeader(BasicScheme.authenticate(
                new UsernamePasswordCredentials(user, pass),
                charsetEncoding, false));

        HttpResponse httpResponse;
        StringBuilder responseStrBuilder = new StringBuilder();
        try {
            httpResponse = httpClient.execute(httpGet);

            HttpEntity responseEntity = httpResponse.getEntity();
            InputStream inputStream = responseEntity.getContent();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, charsetEncoding));

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            logger.info(responseStrBuilder.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseStrBuilder;
    }

    private void loadCSSSelectors() {
        ArrayList<String> lines = FileManager.ReadLines(properties.getProperty("css.selectors.config"));
        for (String line : lines) {
            try {
                if (line.startsWith("#") || line.startsWith("--") || line.startsWith("//") || line.trim().length() == 0) {
                    continue;
                }
                String[] fields = line.split("[::]");
                if (fields.length < 2) {
                    continue;
                }

                // parse CSS selectors for each social network
                cssSelectors = new HashMap<String, String>();
                String[] selectorCategoryPairs = fields[2].split("[ \t]+");
                for (int i = 0; i < selectorCategoryPairs.length; i++) {
                    String[] pairs = selectorCategoryPairs[i].split("[|]+");
                    if (pairs.length > 1) {
                        cssSelectors.put(pairs[0], pairs[1]);
                        cssSelectorsMap.put(pairs[0].toLowerCase(), pairs[1]);
                    }
                }
                selectorMap.put(fields[0], cssSelectors);
                //get(socialNetworkName);
                //Thread.sleep(500);
            } catch (Exception ex) {
                Logger.getLogger(ProfileCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class SocialProfileJSONHolder {

        @JsonProperty("service_name")
        public String serviceName;
        @JsonProperty("url")
        public String url;
    }
}
