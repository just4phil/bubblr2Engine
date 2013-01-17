
package de.philweb.bubblr.tools;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
 
public class LanguageManager {
        private static final String DEFAULT_LANGUAGE = "en_UK";
       
        private String m_languagesFile = null;
        private HashMap<String, String> m_language = null;
        private String m_languageName = null;
       
//        private static final Logger m_logger = Game.getLogger();
//        private static final PlatformResolver m_platformResolver = Game.getPlarformResolver();
       
        public LanguageManager(String languagesName) {
                this("data/languages.xml", languagesName);
        }
       
        public LanguageManager(String languagesFile, String languagesName) {           
                // Languages file
                m_languagesFile = languagesFile;
               
                // Create language map
                m_language = new HashMap<String, String>();
               
                // Default language (system language)
                m_languageName = languagesName;
               
//                if (m_languageName.equals("") && m_platformResolver != null) {
//                        m_languageName = m_platformResolver.getDefaultLanguage();
//                }
               
              if (m_languageName.equals("")) m_languageName = java.util.Locale.getDefault().toString();
                
                //  Try to load selected language, if it fails, load default one
                if (!loadLanguage(m_languageName)) {
                        loadLanguage(DEFAULT_LANGUAGE);
                }
        }
       
        public String getLanguagesFile() {
                return m_languagesFile;
        }
       
        public void setLanguagesFile(String languagesFile) {
//        	System.out.println("LanguageManager: setting languages file to " + languagesFile);
                m_languagesFile = languagesFile;
        }
       
        public String getLanguage() {
                return m_languageName;
        }
       
        public boolean loadLanguage() {
                return loadLanguage(m_languageName);
        }
       
        public boolean loadLanguage(String languageName) {
//        	System.out.println("LanguageManager: try to load: " + languageName);
                try {
                        // Parse xml document
                        XmlReader reader = new XmlReader();
//                        System.out.println("LanguageManager: new XmlReader() done.");
                        	
                        Element root = reader.parse(Gdx.files.internal(m_languagesFile).read());
//                        System.out.println("LanguageManager: reader.parse(...) done.");
                        
                        Array<Element> languages = root.getChildrenByName("language");
//                        System.out.println("LanguageManager: Array<Element> languages =.... done.");
                        
                        // Iterate over languages, trying to find the selected one
                        for (int i = 0; i < languages.size; ++i) {
                                Element language = languages.get(i);
                               
                                if (language.getAttribute("name").equals(languageName)) {
                                        // Clear the previous language
                                        m_language.clear();
                                        Element languageElement = (Element)language;
                                        Array<Element> strings = languageElement.getChildrenByName("string");
                                       
                                        // Load all the strings for that language
                                        for (int j = 0; j < strings.size; ++j) {
                                                Element stringNode = strings.get(j);
                                                String key = stringNode.getAttribute("key");
                                                String value = stringNode.getAttribute("value");
                                                value = value.replace("<br />", "\n");
                                                m_language.put(key, value);
//                                                System.out.println("LanguageManager: loading key " + key);
                                        }
                                       
                                        m_languageName = languageName;
                                       
//                                        System.out.println("LanguageManager: " + languageName + " language sucessfully loaded");
                                       
                                        return true;
                                }
                        }
                }
                catch (Exception e) {
//                	System.out.println("LanguageManager: error loading File: " + m_languagesFile + " language: " + languageName);
//                	System.out.println("error: " + e);
                        return false;
                }
               
//                System.out.println("LanguageManager: couldn´t load language " + languageName);
               
                return false;
        }
 
        public String getString(String key) {
                if (m_language != null) {
                        // Look for string in selected language
                        String string = m_language.get(key);
                       
                        if (string != null) {
                                return string;
                        }
                }
       
                // Key not found, return the key itself
//                System.out.println("LanguageManager: string " + key + " not found");		// TODO: activate for testing
                return key;
        }
 
        // Not compatible with HTML5
        public String getString(String key, Object... args) {
                return String.format(getString(key), args);
        }
}



//package de.philweb.bubblr.tools;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import com.badlogic.gdx.Gdx;
// 
//public class LanguageManager {
//        private static final String DEFAULT_LANGUAGE = "en_UK";
//       
//        //---- alt 
//        private String m_languagesFile = null;
//        private HashMap<String, String> m_language = null;
//        private String m_languageName = null;
//        
//        
//        
//        //---- neu 
//    	private HashMap<String, String> _language = null;
//    	private String _languageName = null;
//        
//        
//       
////        private static final Logger m_logger = Game.getLogger();
////        private static final PlatformResolver m_platformResolver = Game.getPlarformResolver();
//       
//    	
//        public LanguageManager(String languagesName) {
//                this("data/languages.xml", languagesName);
//        }
//       
//        
//        public LanguageManager(String languagesFile, String languagesName) {           
//                // Languages file
//                m_languagesFile = languagesFile;
//               
//                // Create language map
//                m_language = new HashMap<String, String>();
//               
//                // Default language (system language)
//                m_languageName = languagesName;
//               
////                if (m_languageName.equals("") && m_platformResolver != null) {
////                        m_languageName = m_platformResolver.getDefaultLanguage();
////                }
//               
//              if (m_languageName.equals("")) m_languageName = java.util.Locale.getDefault().toString();
//                
//                //  Try to load selected language, if it fails, load default one
//                if (!loadLanguage(m_languageName)) {
//                        loadLanguage(DEFAULT_LANGUAGE);
//                }
//        }
//       
//        public String getLanguagesFile() {
//                return m_languagesFile;
//        }
//       
//        public void setLanguagesFile(String languagesFile) {
////        	System.out.println("LanguageManager: setting languages file to " + languagesFile);
//                m_languagesFile = languagesFile;
//        }
//       
//        public String getLanguage() {
//                return m_languageName;
//        }
//       
//        public boolean loadLanguage() {
//                return loadLanguage(m_languageName);
//        }
//       
//        
//        
//        
//        public boolean loadLanguage(String languageName) {
////        	System.out.println("LanguageManager: try to load: " + languageName);
//                
//        	
//        	
//      	  try {
//      		 
//      		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//      		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//      		Document doc = dBuilder.parse(Gdx.files.internal(m_languagesFile).read());
//      		doc.getDocumentElement().normalize();
//       
////      		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
//      		
//      		//---------- Anzahl LANGUAGES ermitteln ---------------
//      		NodeList nList = doc.getElementsByTagName("language");
//      		
//      		
//      		for (int temp = 0; temp < nList.getLength(); temp++) {
//       
//      			//---------- pro LANGUAGE ---------------
//      		   Node nNode = nList.item(temp);
//      		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//       
//      		      Element eElement = (Element) nNode;
//       
//      		      Node langNameNode = eElement.getAttributes().getNamedItem("name");
//      		      String langName = langNameNode.getNodeValue();
//      		      
//      		      //---------- Namen der LANGUAGE ausgeben ---------------
//      		      System.out.println("language: " + langName);
//      		    
//      		      
//      		      //---------- wenn richtige LANGUAGE ---------------
////      		      if (eElement.getAttributes().getNamedItem("name").equals(languageName)) {
//      		      if (langName.equals("en_UK")) {
//  						
//      		    	  
//      		    	  
//      		    	  
//      		    	  
//      		    	  
////      				  NodeList nodeLst = doc.getDocumentElement().getChildNodes();
////      				  Map elemen = getElementKeyValue(nodeLst);
////      				  Iterator it = elemen.entrySet().iterator();
////      				  while (it.hasNext()) {
////      					  Map.Entry pairs = (Map.Entry)it.next();
////      					  	System.out.println(pairs.getKey() + " = " + pairs.getValue());
////      				  }
//      		    	  
//      		    	  
//      		    	  
//      		    	  
//      		    	  
//      		    	  
//      		    	  
////   					_language.clear();
////					Array<Element> strings = eElement.getChildNodes();
//					
//  					//---------- Liste der Key/value-pairs ermitteln ---------------
//  					NodeList nListKeys = langNameNode.getFirstChild().getChildNodes();
////  					NodeList nListKeys = langNameNode.getAttributes().getLength();
////  							doc.getElementsByTagName("key");
//  					
//  					System.out.println("langNameNode.getAttributes().getLength(): " + langNameNode.getAttributes().getLength() );
//
//					for (int j = 0; j < nListKeys.getLength(); ++j) {
//						
//						Node nNodeKey = nListKeys.item(j);
////						Element eElementKey = (Element) nNodeKey;
//						
////						String key = nNodeKey.getAttributes().getNamedItem("key").getNodeValue();
////						String key = eElementKey.getAttributes().getNamedItem("key").getNodeValue();
////						String value = nNodeKey.getAttributes().getNamedItem("value").getNodeValue();
////						value = value.replace("&lt;br /&gt;&lt;br /&gt;", "\n");
////						_language.put(key, value);
//						
////						System.out.println("key: " + key + " // value: " + value );
//					}
// 
//					return true;
//				}
//  				
//  				
//  				
//////      		      System.out.println("First Name : " + getTagValue("firstname", eElement));
//
////       
//      		   }
//      		}
//      	  } catch (Exception e) {
//      		e.printStackTrace();
//      	  }
//      	  
//      	 
//               
////                System.out.println("LanguageManager: couldn´t load language " + languageName);
//               
//                return false;
//        }
// 
//        
//        
////        private static String getTagValue(String sTag, Element eElement) {
////        	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
////         
////                Node nValue = (Node) nlList.item(0);
////         
////        	return nValue.getNodeValue();
////          }
//        
//        
//        
//        public String getString(String key) {
//                if (m_language != null) {
//                        // Look for string in selected language
//                        String string = m_language.get(key);
//                       
//                        if (string != null) {
//                                return string;
//                        }
//                }
//       
//                // Key not found, return the key itself
////                System.out.println("LanguageManager: string " + key + " not found");		// TODO: activate for testing
//                return key;
//        }
// 
////        // Not compatible with HTML5
////        public String getString(String key, Object... args) {
////                return String.format(getString(key), args);
////        }
//
////    	public Map getElementKeyValue(NodeList nodeList){
////    		Map elements = new HashMap();
////    		if (nodeList!=null && nodeList.getLength()>0 ){
////    			for(int i=0 ; i < nodeList.getLength() ; i++){
////    				Node node = nodeList.item(i); //Take the node from the list
////    				NodeList valueNode = node.getChildNodes(); // get the children of the node
////    		        String value = (valueNode.item(0)!=null) ? valueNode.item(0).getNodeValue() : "";
////    		        elements.put(node.getNodeName(), value);
////    			}
////    		}
////    		return elements;
////    	}
//
//}





