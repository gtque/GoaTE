package com.thegoate.utils.fill.serialize.pojos;

import java.util.List;

import com.thegoate.utils.fill.serialize.DeSerializerTests;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.TypeT;
import com.thegoate.utils.fill.serialize.collections.ListType;

public class ContentJA<T> extends Kid {

	@ListType(type = TypeT.class)
	@GoateSource(source = Inside.class, key = "")
	List<T> content;

	public void setContent(List<T> root) {
		this.content = root;
	}
}
