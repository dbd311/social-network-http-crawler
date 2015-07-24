/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package profile.crawler;

/**
 *
 * @author Duy Dinh <duy.dinh@tudor.lu>
 */
class DBLPCrawler extends ProfileCrawler {

    public DBLPCrawler() {
        cssSelectors.put("span.title", "PUBLICATION-TITLE");        
    }
}
