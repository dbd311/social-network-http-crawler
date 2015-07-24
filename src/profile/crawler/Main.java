/**
 * Profile crawler from the Web application (main)
 *
 * @author: Duy Dinh
 * @date: 10 july 2015
 * @org: Luxembourg Institute of Science and Technology
 */
package profile.crawler;

import org.apache.log4j.Logger;

/**
 *
 * @author Duy Dinh <duy.dinh@tudor.lu>
 */
public class Main {

    
    public static void main(String[] args) {

        String url = "";
        ProfileCrawler crawler = new ProfileCrawler();

        String userID = "55b08ff4c20c490147084b48";
        // userID = "55a7a8e0c20c49364413e831";
        String urlPrefix = "http://european-data-forum.list.lu/api/v1.0/profile.json";
        /**
         * * Research gate **
         */
        // url = "http://www.researchgate.net/profile/Andre_Kretzschmar2/info";
        //crawler = new ResearchGateCrawler();
        /**
         * * My Science work **
         */
        // url = "https://www.mysciencework.com/profile/dkd.list";
        // crawler = new MyScienceWorkCrawler();
        /**
         * * LinkedIn **
         */
        //url = "https://www.linkedin.com/in/williamhgates";
        //crawler = new LinkedInCrawler();
        
        if (args.length > 0) {
            //url = args[0];
            //crawler.get(url);
            userID = args[0];
            url = urlPrefix + "/" + userID;
            // crawler.downloadUserProfile(url, "recommender", "recommenderPassword");

            crawler.extractDataFromProfile(url);

            crawler.printKeywords(OUTPUT_FORMAT.JSON);

        } else {
            // crawl social network data from demo
            crawler.demo();
        }

        // url = "https://scholar.google.com/citations?user=wYeP57YAAAAJ&hl=en";
        // crawler = new GoogleScholarCrawler();
        //url = "https://fr.linkedin.com/pub/duy-dinh/74/478/8a1";
        //crawler = new LinkedInCrawler();
//        url = "http://dblp.uni-trier.de/pers/hd/d/Dinh:Duy";
//        crawler = new DBLPCrawler();
//        crawler.get(url);
        // crawler.printKeywords();
        // System.out.println("Program terminated");
        Logger.getLogger("main").info("*** Program terminated! ***");
    }
}
