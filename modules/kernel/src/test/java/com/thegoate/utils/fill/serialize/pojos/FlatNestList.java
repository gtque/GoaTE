package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.util.List;

/**
 * Created by Eric Angeli on 6/18/2020.
 */
public class FlatNestList extends Kid {

	@ListType(type = FlatNest.class)
	@GoateSource(source = Cheese.class, key = "row")
	@GoateSource(source = RootSource.class, key = "content")
	@GoateSource(key = "")
	private List<FlatNest> nest;

	public List<FlatNest> getNest() {
		return nest;
	}

	public void setNest(List<FlatNest> nest) {
		this.nest = nest;
	}
}
