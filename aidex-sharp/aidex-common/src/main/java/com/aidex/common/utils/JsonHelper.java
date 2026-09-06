package com.aidex.common.utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
public class JsonHelper extends ObjectMapper implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private static  class _class{
		private static JsonHelper instance  = new JsonHelper();
		private static JsonHelper baseInstance  = new JsonHelper();
	}
	private JsonHelper() {
		super();
	}

	public  static  JsonHelper getInstance(){
		_class.instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		_class.instance.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);//允许出现特殊字符和转义符
		return _class.instance;
	}

	public  static  JsonHelper getBaseInstance(){
		_class.baseInstance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return _class.baseInstance ;
	}

	@Override
	public String writeValueAsString(Object value){
		try {
			return super.writeValueAsString(value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}



}
