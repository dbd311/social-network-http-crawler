/*
 * IRToolkit - Information Retrieval Toolkit
 * Website: http://www.irit.fr/~Duy.Dinh/tools/irtoolkit
 * Contact: irtoolkit{at}irit{dot}fr
 * University of Toulouse - Institut for Research in Informatics of Toulouse
 * http://www.irit.fr/
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is PropertyManager.java.
 *
 * The Original Code is Copyright (C) 2011-2012 the University of Toulouse.
 * All Rights Reserved.
 *
 * Contributor(s):
 *   Duy Dinh <dinh{at}irit{dot}fr> (original author)
 */
package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager {

    /**
     * Create and load properties from file
     * @param propFile input property file
     * @return properties
     */
    public static Properties loadProperties(String propFile) {

        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream(propFile);
            properties.load(in);
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(PropertyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }

    /**
     *  save & close properties
     */
    public static void saveProperties(Properties properties, String propFile, String comment) {
        
        FileOutputStream out;
        try {
            out = new FileOutputStream(propFile);
            properties.store(out, comment);
            out.close();

        } catch (Exception ex) {
            Logger.getLogger(PropertyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *  save & close properties
     */
    public static void saveProperties(Properties properties, String propFile){
        saveProperties(properties, propFile, "");
    }
}