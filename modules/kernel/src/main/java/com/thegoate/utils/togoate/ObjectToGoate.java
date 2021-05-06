package com.thegoate.utils.togoate;

import java.util.Arrays;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.Utility;
import com.thegoate.utils.fill.serialize.Serializer;

/**
 * Converts the given object to a goate collection.
 * If a string, assumes it is in a semicolon separated list of key=value pairs.
 * Otherwise it will attempt to serialize it to a Goate collection using the default source.
 */
@ToGoateUtil()
@IsDefault
public class ObjectToGoate implements ToGoateUtility {
	Object takeActionOn;

	public ObjectToGoate(Object val) {
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
		return true;
	}

	@Override
	public ToGoateUtility autoIncrement(boolean increment) {
		return this;
	}

	@Override
	public Goate convert() {
		Goate goate = new Goate();

		if(takeActionOn != null){
			if(takeActionOn instanceof String) {
				goate = processString(goate, ""+takeActionOn);
			} else {
				goate = new Serializer(takeActionOn, takeActionOn.getClass()).toGoate();
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
