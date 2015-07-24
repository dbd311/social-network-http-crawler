/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package profile.crawler;

/**
 *
 * @author Duy Dinh <duy.dinh@tudor.lu>
 */
class GoogleScholarCrawler extends ProfileCrawler {

    public GoogleScholarCrawler() {
        cssSelectors.put("a.gsc_prf_ila", "discipline");
        
        // find div of class gsc_prf_il that does not contains tag 'a'
        cssSelectors.put("div.gsc_prf_il:not(:has(a))", "current-position"); 
    }
}
