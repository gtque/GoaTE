package com.thegoate.utils.compare.tools.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
public class DateType extends FindType {
	protected String format = "yyyy-MM-dd";

	@Override
	public boolean isType(Object check){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		boolean result = true;
		try {
			String d = parse("" + check);
			sdf.parse(d);
		}catch (Throwable t){
			result = false;
		}
		return result;
	}

	public String parse(String d){
		int t = d.indexOf("T");
		if(t<0){
			t = d.length();
		}
		return d.substring(0,t);
	}

	@Override
	public Class type(Object check){
		return Date.class;
	}
}
