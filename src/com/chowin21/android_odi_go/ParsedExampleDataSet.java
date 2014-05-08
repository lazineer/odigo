package com.chowin21.android_odi_go;

public class ParsedExampleDataSet {    
        private int list_total_count = 0;
        private String SH_ID = "";
        private String SH_NAME = "";
        private String INDUTY_CODE_SE = "";
        private String INDUTY_CODE_SE_NAME = "";
        private String SH_ADDR = "";
        private String SH_PHONE = "";
        private String SH_WAY = "";
        private String SH_INFO = "";
        private String SH_PRIDE = "";
        private int SH_RCMN = 0;
        private String SH_PHOTO = "";
        private String BASE_YM = "";
        private double lon = 0.0;
        private double lat = 0.0;
        private String IM_NAME = "";
        private int IM_PRICE = 0;
        private double dist = 0.0;
 
        public String getExtractedString(int i) {
        	String str = null;
        	
        	switch(i)
        	{
        	case 1:
        		str = SH_ID;
        		break;
        	case 2:
        		str = SH_NAME;
        		break;
        	case 3:
        		str = INDUTY_CODE_SE;
        		break;
        	case 4:
        		str = INDUTY_CODE_SE_NAME;
        		break;
        	case 5:
        		str = SH_ADDR;
        		break;
        	case 6:
        		str = SH_PHONE;
        		break;
        	case 7:
        		str = SH_WAY;
        		break;
        	case 8:
        		str = SH_INFO;
        		break;
        	case 9:
        		str = SH_PRIDE;
        		break;
        	case 11:
        		str = SH_PHOTO;
        		break;
        	case 12:
        		str = BASE_YM;
        		break;
        	case 13:
        		str = IM_NAME;
        		break;
        	}
                return str;
        }
        public void setExtractedString(String extractedString, int i) {
        	switch(i)
        	{
        	case 1:
        		SH_ID += extractedString;
        		break;
        	case 2:
        		SH_NAME += extractedString;
        		break;
        	case 3:
        		INDUTY_CODE_SE += extractedString;
        		break;
        	case 4:
        		INDUTY_CODE_SE_NAME += extractedString;
        		break;
        	case 5:
        		SH_ADDR += extractedString;
        		break;
        	case 6:
        		SH_PHONE += extractedString;
        		break;
        	case 7:
        		SH_WAY += extractedString;
        		break;
        	case 8:
        		SH_INFO += extractedString;
        		break;
        	case 9:
        		SH_PRIDE += extractedString.replace('\n', ' ');
        		break;
        	case 11:
        		SH_PHOTO += extractedString;
        		break;
        	case 12:
        		BASE_YM += extractedString;
        		break;
        	case 13:
        		IM_NAME += extractedString;
        		break;
        	}
        }
 
        public int getExtractedInt(int i) {
        	int n = 0;
        	
        	switch(i)
        	{
        	case 0:
        		n = list_total_count;
        		break;
        	case 10:
        		n = SH_RCMN;
        		break;
        	case 14:
        		n = IM_PRICE;
        		break;
        	}
                return n;
        }
        public void setExtractedInt(int extractedInt, int i) {
        	switch(i)
        	{
        	case 0:
        		list_total_count = extractedInt;
        		break;
        	case 10:
        		SH_RCMN = extractedInt;
        		break;
        	case 14:
        		IM_PRICE = extractedInt;
        		break;
        	}
        }
    
        public void setLat(double lat){
        	this.lat = lat;
        }
        
        public void setLon(double lon){
        	this.lon = lon;
        }
        
        public double getLat(){
        	return this.lat;
        }
        
        public double getLon(){
        	return this.lon;
        }
        
        public void setDist(double dist){
        	this.dist = dist;
        }
        
        public double getDist(){
        	return this.dist;
        }
        
        public void init()
        {
        	SH_ID = "";
    		SH_NAME = "";
    		INDUTY_CODE_SE = "";
    		INDUTY_CODE_SE_NAME = "";
    		SH_ADDR = "";
    		SH_PHONE = "";
    		SH_WAY = "";
    		SH_INFO = "";
    		SH_PRIDE = "";
    		SH_PHOTO = "";
    		BASE_YM = "";
        }
}