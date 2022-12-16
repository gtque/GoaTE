package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

/**
 * Created by Eric Angeli on 6/18/2020.
 */
public class FlatNest extends Kid {

	@GoateSource(source = Cheese.class, key = "##")
	@GoateSource(source = RootSource.class, key = "##")
	@GoateSource(key = "##")
	private FlatInner inner;

	public FlatInner getInner() {
		return inner;
	}

	public void setInner(FlatInner inner) {
		this.inner = inner;
	}
}
