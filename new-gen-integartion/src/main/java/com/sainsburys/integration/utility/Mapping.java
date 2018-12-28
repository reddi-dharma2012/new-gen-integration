package com.sainsburys.integration.utility;

import java.util.HashMap;
import java.util.Map;

public class Mapping {
	
	
	public static String getSku(String upc){
		Map<String,String> map = new HashMap<String,String>();
		map.put("123456", "8772254");
		map.put("124457", "8644983");
		map.put("123458", "8644745");
		return map.get(upc);
	}
	

}
