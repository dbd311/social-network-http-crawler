/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package profile.crawler;

/**
 *
 * @author Duy Dinh <duy.dinh@tudor.lu>
 */
public class ResearchGateCrawler extends ProfileCrawler {

    public ResearchGateCrawler() {
        // div.profile-skills >  div.skill-toplist > ul > li.skill-toplist-item > a.      
        cssSelectors.put("div>div>ul>li>div.keyword-list-token-plain>a.keyword-list-token-text", "skills");
        
        cssSelectors.put("div>div>ul>li>div.keyword-list-token>a.keyword-list-token-text", "topics");

    }
}
