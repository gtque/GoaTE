/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package com.thegoate.utils.fill.serialize.collections;

import com.thegoate.Goate;
import com.thegoate.utils.fill.serialize.Cast;
import com.thegoate.utils.fill.serialize.CastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric Angeli on 6/27/2018.
 */
@CastUtil(type = Map.class)
public class CastMap extends CastCollection {

	public CastMap(Object value) {
		super(value);
		typeAnnotation = MapType.class;
	}

	@Override
	public <T> T cast(Class<T> type) {
		try {
			if (value != null) {
				return type.cast(value);
			}
		} catch (Throwable t) {
			LOGGER.debug("Cast Map", "Couldn't just cast to an list, now trying to build it from the data.");
		}
		LOGGER.debug("Cast Map", "building from data.");

		Map<Object, Object> o = new HashMap<>();
		//this needs to be configurable from serialization some how
		int i = 0;
		if (data.filter("[0-9]+\\.key.*").size() > 0) {
			while (data.filter("" + i).size() > 0) {
				try {
					o.put(new Cast(data.filter("" + i).scrubKeys("" + i + "\\.key\\."), dataSource).cast(data.get("" + i + ".key"), getType("" + i, data.get("" + i), MapKeyType.class)),
						new Cast(data.filter("" + i).scrubKeys("" + i + "\\.value\\."), dataSource).cast(data.get("" + i + ".value"), getType("" + i, data.get("" + i), MapType.class)));
				} catch (IllegalAccessException | InstantiationException e) {
					LOGGER.error("Cast Map", "Failed to build map: " + e.getMessage(), e);
					throw new RuntimeException("Failed to construct map: " + e.getMessage());
				}
				i++;
			}
		} else {
			String key = fieldName();
			Goate mapData = data.filterExclude(key).filterExclude("(.*\\..*)+");
			for (String mapKey : mapData.keys()) {
				try {
					o.put(mapKey,
						new Cast(data.filter(mapKey).scrubKeys(mapKey+"\\."), dataSource).cast(data.get(mapKey), getType(mapKey, data.get(mapKey), MapType.class)));
				} catch (IllegalAccessException | InstantiationException e) {
					LOGGER.error("Cast Map", "Failed to build map: " + e.getMessage(), e);
					throw new RuntimeException("Failed to construct map: " + e.getMessage());
				}
//				if (!flatten) {
//					d = data.filter(fieldKey.replace("##", "[0-9]*")).scrubKeys(fieldKey + "\\.");
//				} else {
//					d = data.filterStrict(fieldKey.replace("##", "[0-9]*"));
//				}
			}
		}
		return type.cast(o);
	}

	private Class getType(String id, Object value, Class ant) {
		Class temp = typeAnnotation;
		typeAnnotation = ant;
		Class result = getType(id, value);
		typeAnnotation = temp;
		return result;
	}

	@Override
	public boolean isType(Object check) {
		Map<Object, Object> test = new HashMap<>();
		boolean result = false;
		try {
			((Class) check).cast(test);
			result = true;
		} catch (Throwable t) {
		}
		return result;
	}
}
