package com.chowin21.android_odi_go;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;

import android.util.Log;
 
 
public class ExampleHandler extends DefaultHandler{
 
        // ===========================================================
        // Fields
        // ===========================================================
       
        private boolean bListPriceModelStoreService = false;
        private boolean blist_total_count = false;
        private boolean brow = false;
        private boolean bSH_ID = false;
        private boolean bSH_NAME = false;
        private boolean bINDUTY_CODE_SE = false;
        private boolean bINDUTY_CODE_SE_NAME = false;
        private boolean bSH_ADDR = false;
        private boolean bSH_PHONE = false;
        private boolean bSH_WAY = false;
        private boolean bSH_INFO = false;
        private boolean bSH_PRIDE = false;
        private boolean bSH_RCMN = false;
        private boolean bSH_PHOTO = false;
        private boolean bBASE_YM = false;
        private boolean bLon = false;
        private boolean bLat = false;
        private boolean bIM_NAME = false;
        private boolean bIM_PRICE = false;
        private boolean bDist = false;
        private Vector<ParsedExampleDataSet> v = new Vector<ParsedExampleDataSet>();
        private ParsedExampleDataSet myParsedExampleDataSet;
        String str;
 
        // ===========================================================
        // Getter & Setter
        // ===========================================================
 
        public Vector<ParsedExampleDataSet> getParsedData() {
                return this.v;
        }
        
        public void clearParsedData(){
        	this.v.clear();
        }
 
        // ===========================================================
        // Methods
        // ===========================================================
        @Override
        public void startDocument() throws SAXException {
                
        }
 
        @Override
        public void endDocument() throws SAXException {
                // Nothing to do
        }
 
        /** Gets be called on opening tags like:
         * <tag>
         * Can provide attribute(s), when xml was like:
         * <tag attribute="attributeValue">*/
        @Override
        public void startElement(String namespaceURI, String localName,
                        String qName, Attributes atts) throws SAXException {
        	if (localName.equals("ListPriceModelStoreService")) {
                this.bListPriceModelStoreService = true;
            }else if (localName.equals("list_total_count")) {
                this.blist_total_count = true;
            }else if (localName.equals("row")) {
                this.brow = true;
                this.myParsedExampleDataSet = new ParsedExampleDataSet();
            }else if (localName.equals("SH_ID")) {
                this.bSH_ID = true;
            }else if (localName.equals("SH_NAME")) {
            	this.bSH_NAME = true;
            }else if (localName.equals("INDUTY_CODE_SE")) {
            	this.bINDUTY_CODE_SE = true;
            }else if (localName.equals("INDUTY_CODE_SE_NAME")) {
            	this.bINDUTY_CODE_SE_NAME = true;
	        }else if (localName.equals("SH_ADDR")) {
	        	this.bSH_ADDR = true;
		    }else if (localName.equals("SH_PHONE")) {
		    	this.bSH_PHONE = true;
			}else if (localName.equals("SH_WAY")) {
				this.bSH_WAY = true;
			}else if (localName.equals("SH_INFO")) {
				this.bSH_INFO = true;
			}else if (localName.equals("SH_PRIDE")) {
				this.bSH_PRIDE = true;
			}else if (localName.equals("SH_RCMN")) {
				this.bSH_RCMN = true;
			}else if (localName.equals("SH_PHOTO")) {
				this.bSH_PHOTO = true;
			}else if (localName.equals("BASE_YM")) {
				this.bBASE_YM = true;
			}else if (localName.equals("lon")) {
				this.bLon = true;
			}else if (localName.equals("lat")) {
				this.bLat = true;
			}else if (localName.equals("IM_NAME")) {
				this.bIM_NAME = true;
			}else if (localName.equals("IM_PRICE")) {
				this.bIM_PRICE = true;
			}else if (localName.equals("dist")) {
				this.bDist = true;
			}
        }
       
        /** Gets be called on closing tags like:
         * </tag> */
        @Override
        public void endElement(String namespaceURI, String localName, String qName)
                        throws SAXException {
        	if (localName.equals("ListPriceModelStoreService")) {
                this.bListPriceModelStoreService = false;
            }else if (localName.equals("list_total_count")) {
                this.blist_total_count = false;
            }else if (localName.equals("row")) {
                this.brow = false;
                v.add(this.myParsedExampleDataSet);
            }else if (localName.equals("SH_ID")) {
                this.bSH_ID = false;
            }else if (localName.equals("SH_NAME")) {
            	this.bSH_NAME = false;
            }else if (localName.equals("INDUTY_CODE_SE")) {
            	this.bINDUTY_CODE_SE = false;
            }else if (localName.equals("INDUTY_CODE_SE_NAME")) {
            	this.bINDUTY_CODE_SE_NAME = false;
	        }else if (localName.equals("SH_ADDR")) {
	        	this.bSH_ADDR = false;
		    }else if (localName.equals("SH_PHONE")) {
		    	this.bSH_PHONE = false;
			}else if (localName.equals("SH_WAY")) {
				this.bSH_WAY = false;
			}else if (localName.equals("SH_INFO")) {
				this.bSH_INFO = false;
			}else if (localName.equals("SH_PRIDE")) {
				this.bSH_PRIDE = false;
			}else if (localName.equals("SH_RCMN")) {
				this.bSH_RCMN = false;
			}else if (localName.equals("SH_PHOTO")) {
				this.bSH_PHOTO = false;
			}else if (localName.equals("BASE_YM")) {
				this.bBASE_YM = false;
			}else if (localName.equals("lon")) {
				this.bLon = false;
			}else if (localName.equals("lat")) {
				this.bLat = false;
			}else if (localName.equals("IM_NAME")) {
				this.bIM_NAME = false;
			}else if (localName.equals("IM_PRICE")) {
				this.bIM_PRICE = false;
			}else if (localName.equals("dist")) {
				this.bDist = false;
			}
        }
       
        /** Gets be called on the following structure:
         * <tag>characters</tag> */
        @Override
    public void characters(char ch[], int start, int length) {
        	try{
        if(this.blist_total_count){
        	str = new String(ch, start, length);
            myParsedExampleDataSet.setExtractedInt(Integer.parseInt(str), 0);
        }else if(this.bSH_ID){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 1);
        }else if(this.bSH_NAME){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 2);
        }else if(this.bINDUTY_CODE_SE){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 3);
        }else if(this.bINDUTY_CODE_SE_NAME){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 4);
        }else if(this.bSH_ADDR){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 5);
        }else if(this.bSH_PHONE){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 6);
        }else if(this.bSH_WAY){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 7);
        }else if(this.bSH_INFO){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 8);
        }else if(this.bSH_PRIDE){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 9);
        }else if(this.bSH_RCMN){
        	str = new String(ch, start, length);
            myParsedExampleDataSet.setExtractedInt(Integer.parseInt(str), 10);
        }else if(this.bSH_PHOTO){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 11);
        }else if(this.bBASE_YM){
            myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 12);
        }else if(this.bLon){
        	str = new String(ch, start, length);
        	myParsedExampleDataSet.setLon(Double.parseDouble(str));
        }else if(this.bLat){
        	str = new String(ch, start, length);
        	myParsedExampleDataSet.setLat(Double.parseDouble(str));
        }else if(this.bIM_NAME){
        	myParsedExampleDataSet.setExtractedString(new String(ch, start, length), 13);
        }else if(this.bIM_PRICE){
        	str = new String(ch, start, length);
        	myParsedExampleDataSet.setExtractedInt(Integer.parseInt(str), 14);
        }else if(this.bDist){
        	str = new String(ch, start, length);
        	myParsedExampleDataSet.setDist(Double.parseDouble(str));
        }
        	}catch (NumberFormatException nfe) {
        		Log.e("NumberFormat", str + " " + bLon + " " + bLat,nfe);
        	}
        	
        	catch(Exception e){
        		//Log.e("Handler", e.toString(),e);
        		int aa = v.size();
        		
     
        		for (int i = 0; i < aa; i++) {
        			Log.e("Handler", v.elementAt(i).getExtractedString(2)+" "+
                			v.elementAt(i).getExtractedString(13)+" "+
                			v.elementAt(i).getExtractedInt(14),e);
        			
        		}
        	}
    }
}