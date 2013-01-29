
package de.philweb.bubblr.tools;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
 
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
			
			//------ method 1 ----------------------------------
				
//				DOMParser parser = new DOMParser();
				
//				parser.parse(Gdx.files.internal(m_languagesFile).readString("utf-8"));
					// returns: java.net.MalformedURLException: no protocol: %3C?xml%20version=%221.0%22%20encoding=%22utf-8%22%20?%3E%0A%3Clanguages%3E%0A%0A%0
					//			0is%20not%20implemented%20at%20the%20moment%20(coming%20soon)!%22%20value
				
//				parser.parse(m_languagesFile);
					// returns: java.io.FileNotFoundException: L:\eclipse-projects\bubblr2\bubblr-desktop\data\languages.xml (Das System kann den angegebenen Pfad nicht finden)
				
//				parser.parse(new InputSource(new ByteArrayInputStream(m_languagesFile.getBytes("utf-8"))));
					// returns: [Fatal Error] :1:1: Content ist nicht zulässig in Prolog.
					//			org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1; Content ist nicht zulässig in Prolog.
					//			but: xml file seems to be valid: <?xml version="1.0" encoding="utf-8"?>
				
//				parser.parse(Gdx.files.internal(m_languagesFile).readString("utf-8"));
					// returns: java.net.MalformedURLException: no protocol: %3C?xml%20version=%221.0%22%20encoding=%22utf-8%22?%3E%0A%3Clanguages%3E%0A%0A%09%3
				
				
//				Document doc = parser.getDocument();
				 
				
			//------ method 2 ----------------------------------
				
        		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        		DocumentBuilder builder = null;
        		try {
        		    builder = builderFactory.newDocumentBuilder();
        		} 
        		catch (ParserConfigurationException e) {
        		    e.printStackTrace();  
        		}
        		
        		Document doc = null;
        		
        		try {
        		    
//        			doc = builder.parse(Gdx.files.internal(m_languagesFile).read());
        		    	// returns: languages: items: 21
        		    	//			languages: language: #text
        		    	//			java.lang.NullPointerException
        		    	//			LanguageManager: couldn´t load language en_UK
        		    	//			at de.philweb.bubblr.tools.LanguageManager.getNodeAttr(LanguageManager.java:246)
        		    
//        		    doc = builder.parse(new InputSource(Gdx.files.internal(m_languagesFile).readString("UTF-8")));
        				// returns:
						//			java.net.MalformedURLException: no protocol: %3C?xml%20version=%221.0%22%20encoding=%22utf-8%22%20?%3E%0A%3Clanguages%3E%0A%0K
						//					0is%20not%20implemented%20at%20the%20moment%20(coming%20soon)!%22%20value=%22Questo%20non%20&#232;%20ancora%20disponi
						//						at java.net.URL.<init>(Unknown Source)
						//						at java.net.URL.<init>(Unknown Source)
						//						at java.net.URL.<init>(Unknown Source)
						//						at com.sun.org.apache.xerces.internal.impl.XMLEntityManager.setupCurrentEntity(Unknown Source)
						//						at com.sun.org.apache.xerces.internal.impl.XMLVersionDetector.determineDocVersion(Unknown Source)
						//						at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(Unknown Source)
						//						at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(Unknown Source)
						//						at com.sun.org.apache.xerces.internal.parsers.XMLParser.parse(Unknown Source)
						//						at com.sun.org.apache.xerces.internal.parsers.DOMParser.parse(Unknown Source)
						//						at com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderImpl.parse(Unknown Source)
						//						at de.philweb.bubblr.tools.LanguageManager.loadLanguage(LanguageManager.java:124)
        		}
        		catch (SAXException e) {
        		    e.printStackTrace();
        		}
        		catch (IOException e) {
        		    e.printStackTrace();
        		}
				
				//--------------------------------------
				
				// Get the document's root XML node
        		NodeList root = doc.getChildNodes();
        		Node languagesNode = getNode("languages", root);
        		
				NodeList languages = languagesNode.getChildNodes();
				
				Gdx.app.log("languages", "items: " + languages.getLength());
				
				for (int x = 0; x < languages.getLength(); x++ ) {			// Iterate over languages, trying to find the selected one
					
					Node language = languages.item(x);
					
					Gdx.app.log("languages", "language: " + language.getNodeName());
					Gdx.app.log("Language", "" + getNodeAttr("name", language));
					
					if (getNodeAttr("name", language).equals(languageName)) {
						
						m_language.clear();								// Clear the previous language
						NodeList strings = language.getChildNodes();
						  
						// Load all the strings for that language
						for (int j = 0; j < strings.getLength(); ++j) {
							
							Node keyValuePair = strings.item(j);   
							String key = getNodeAttr("key", keyValuePair);
							String value = getNodeAttr("value", keyValuePair);
							value = value.replace("<br />", "\n");
							m_language.put(key, value);
							System.out.println("LanguageManager: loading key " + key);
						}
						
						m_languageName = languageName;
						System.out.println("LanguageManager: " + languageName + " language sucessfully loaded");
						 
						return true;  
					}
				}
				 
   
			}
			catch ( Exception e ) {
			    e.printStackTrace();
			}
                        
			
			System.out.println("LanguageManager: couldn´t load language " + languageName);
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
        
        
        
        
        
//===== methods for parsing DOM Xml==========================================
//        
// http://www.drdobbs.com/jvm/easy-dom-parsing-in-java/231002580
        
        
        protected Node getNode(String tagName, NodeList nodes) {
            for ( int x = 0; x < nodes.getLength(); x++ ) {
                Node node = nodes.item(x);
                if (node.getNodeName().equalsIgnoreCase(tagName)) {
                    return node;
                }
            }
         
            return null;
        }
         
        protected String getNodeValue( Node node ) {
            NodeList childNodes = node.getChildNodes();
            for (int x = 0; x < childNodes.getLength(); x++ ) {
                Node data = childNodes.item(x);
                if ( data.getNodeType() == Node.TEXT_NODE )
                    return data.getNodeValue();
            }
            return "";
        }
         
        protected String getNodeValue(String tagName, NodeList nodes ) {
            for ( int x = 0; x < nodes.getLength(); x++ ) {
                Node node = nodes.item(x);
                if (node.getNodeName().equalsIgnoreCase(tagName)) {
                    NodeList childNodes = node.getChildNodes();
                    for (int y = 0; y < childNodes.getLength(); y++ ) {
                        Node data = childNodes.item(y);
                        if ( data.getNodeType() == Node.TEXT_NODE )
                            return data.getNodeValue();
                    }
                }
            }
            return "";
        }
         
        protected String getNodeAttr(String attrName, Node node ) {
            NamedNodeMap attrs = node.getAttributes();
            for (int y = 0; y < attrs.getLength(); y++ ) {
                Node attr = attrs.item(y);
                if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                    return attr.getNodeValue();
                }
            }
            return "";
        }
         
        protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
            for ( int x = 0; x < nodes.getLength(); x++ ) {
                Node node = nodes.item(x);
                if (node.getNodeName().equalsIgnoreCase(tagName)) {
                    NodeList childNodes = node.getChildNodes();
                    for (int y = 0; y < childNodes.getLength(); y++ ) {
                        Node data = childNodes.item(y);
                        if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
                            if ( data.getNodeName().equalsIgnoreCase(attrName) )
                                return data.getNodeValue();
                        }
                    }
                }
            }
         
            return "";
        }
}




//------------------------------------------------
//package de.philweb.bubblr.tools;
//
//import java.util.HashMap;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.XmlReader;
//import com.badlogic.gdx.utils.XmlReader.Element;
// 
//public class LanguageManager {
//        private static final String DEFAULT_LANGUAGE = "en_UK";
//       
//        private String m_languagesFile = null;
//        private HashMap<String, String> m_language = null;
//        private String m_languageName = null;
//       
////        private static final Logger m_logger = Game.getLogger();
////        private static final PlatformResolver m_platformResolver = Game.getPlarformResolver();
//       
//        public LanguageManager(String languagesName) {
//                this("data/languages.xml", languagesName);
//        }
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
//        public boolean loadLanguage(String languageName) {
////        	System.out.println("LanguageManager: try to load: " + languageName);
//                try {
//                        // Parse xml document
//                        XmlReader reader = new XmlReader();
////                        System.out.println("LanguageManager: new XmlReader() done.");
//                        	
//                        Element root = reader.parse(Gdx.files.internal(m_languagesFile).read());
////                        System.out.println("LanguageManager: reader.parse(...) done.");
//                        
//                        Array<Element> languages = root.getChildrenByName("language");
////                        System.out.println("LanguageManager: Array<Element> languages =.... done.");
//                        
//                        // Iterate over languages, trying to find the selected one
//                        for (int i = 0; i < languages.size; ++i) {
//                                Element language = languages.get(i);
//                               
//                                if (language.getAttribute("name").equals(languageName)) {
//                                        // Clear the previous language
//                                        m_language.clear();
//                                        Element languageElement = (Element)language;
//                                        Array<Element> strings = languageElement.getChildrenByName("string");
//                                       
//                                        // Load all the strings for that language
//                                        for (int j = 0; j < strings.size; ++j) {
//                                                Element stringNode = strings.get(j);
//                                                String key = stringNode.getAttribute("key");
//                                                String value = stringNode.getAttribute("value");
//                                                value = value.replace("<br />", "\n");
//                                                m_language.put(key, value);
////                                                System.out.println("LanguageManager: loading key " + key);
//                                        }
//                                       
//                                        m_languageName = languageName;
//                                       
////                                        System.out.println("LanguageManager: " + languageName + " language sucessfully loaded");
//                                       
//                                        return true;
//                                }
//                        }
//                }
//                catch (Exception e) {
////                	System.out.println("LanguageManager: error loading File: " + m_languagesFile + " language: " + languageName);
////                	System.out.println("error: " + e);
//                        return false;
//                }
//               
////                System.out.println("LanguageManager: couldn´t load language " + languageName);
//               
//                return false;
//        }
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
//        // Not compatible with HTML5
//        public String getString(String key, Object... args) {
//                return String.format(getString(key), args);
//        }
//}


