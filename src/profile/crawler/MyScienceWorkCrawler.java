/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package profile.crawler;

/**
 *
 * @author Duy Dinh <duy.dinh@tudor.lu>
 */
public class MyScienceWorkCrawler extends ProfileCrawler {

    public MyScienceWorkCrawler() {
        cssSelectors.put("div.user-keywords", "keywords");
    }
}
