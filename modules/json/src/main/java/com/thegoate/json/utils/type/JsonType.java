package com.thegoate.json.utils.type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
@IsDefault(forType = true)
public class JsonType extends FindType {

	@Override
	public boolean isType(Object check){
		return isJSONObject(check) || isJSONArray(check);
	}

	@Override
	public Class type(Object check){
		return JSONObject.class;
	}

	public boolean isJSONObject(Object check){
		boolean istype = true;
		try{
			if(check != JSONObject.NULL) {
				if (!(check instanceof JSONObject)) {
					if (!((check instanceof String && new JSONObject("" + check) != null) || (check instanceof JSONArray))) {
						istype = false;
					} else {
						if(!(""+check).matches("\\{[\\S\\s]*\\}\\s*") ){
							istype = false;
						}
//						if((""+check).split("[}\\]]\\s[^,]*\\s[\\{\\[]").length>1) {
//							istype = false;
//						}
					}
				}
			}
		}catch(JSONException je){
//			LOG.debug("Check if JSON", "this was not json.");
			istype = false;
		}
		return istype;
	}

	public boolean isJSONArray(Object check){
		boolean istype = true;
		try{
			if(!(check instanceof JSONArray)) {
				if (!(check instanceof String && new JSONArray(""+check) != null)) {
					istype = false;
				} else {
					if(!(""+check).matches("\\[[\\S\\s]*\\]\\s*") ){
						istype = false;
					}
				}
			}
		}catch (Exception e){
			istype = false;
		}
		return istype;
	}
}
