package com.thegoate.utils.togoate;

import com.thegoate.Goate;
import com.thegoate.utils.Utility;

import java.util.Arrays;
import java.util.Map;

/**
 * Converts the given map to a goate collection.
 * If a string, assumes it is in a semicolon separated list of key=value pairs.
 * Otherwise it will attempt to serialize it to a Goate collection using the default source.
 */
@ToGoateUtil()
public class MapToGoate implements ToGoateUtility {
	Object takeActionOn;

	public MapToGoate(Object val) {
		takeActionOn = val;
	}

	@Override
	public Goate healthCheck() {
		return null;
	}

	@Override
	public Utility setData(Goate data) {
		return null;
	}

	@Override
	public boolean isType(Object check) {
		return check instanceof Map;
	}

	@Override
	public ToGoateUtility autoIncrement(boolean increment) {
		return this;
	}

	@Override
	public Goate convert() {
		Goate goate = new Goate();

		if(takeActionOn != null){
			Map map = (Map) takeActionOn;
			for(Object key:map.keySet()){
				Object val = map.get(key);
				if(val!=null) {
					goate.put("" + key, val);
				} else {
					goate.put("" + key, "null::");
				}
			}
		}
		return goate;
	}

	private Goate processString(Goate goate, String value){
		String[] elements = value.split(";");
		Arrays.stream(elements).forEach(element -> addToGoate(element, goate));
		return goate;
	}

	private void addToGoate(String element, Goate goate){
		String[] keyValue = element.split("=");
		if(keyValue.length == 2){
			goate.put(keyValue[0], keyValue[1]);
		}
	}

}
