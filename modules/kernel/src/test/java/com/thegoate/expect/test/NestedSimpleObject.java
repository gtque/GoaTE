package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
public class NestedSimpleObject extends Kid {
	private double a;
	private char b;

	public double getA() {
		return a;
	}

	public NestedSimpleObject setA(double a) {
		this.a = a;
		return this;
	}

	public char getB() {
		return b;
	}

	public NestedSimpleObject setB(char b) {
		this.b = b;
		return this;
	}
}
