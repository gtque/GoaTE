package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.DefaultSource;
import com.thegoate.utils.fill.serialize.GoateSource;

/**
 * Created by Eric Angeli on 6/18/2020.
 */
public class FlatNest {

	private FlatInner inner;

	public FlatInner getInner() {
		return inner;
	}

	public void setInner(FlatInner inner) {
		this.inner = inner;
	}
}
