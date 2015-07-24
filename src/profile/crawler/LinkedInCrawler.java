/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package profile.crawler;

/**
 *
 * @author Duy Dinh <duy.dinh@tudor.lu>
 */
public class LinkedInCrawler extends ProfileCrawler {

    public LinkedInCrawler() {
        cssSelectors.put("li.interest-item", "interest");
        cssSelectors.put("dd > span.locality", "living-location");
        cssSelectors.put("tr#overview-summary-current> td", "organization");
    }
}
