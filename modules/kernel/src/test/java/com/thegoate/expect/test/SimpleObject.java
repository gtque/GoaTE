package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
public class SimpleObject extends Kid {
	private String a;
	private boolean b;
	private List<String> c = new ArrayList<>();
	private List<SimpleObject2> so2;
	private SimpleObject2[] so2A;
	private NestedObject nested = null;

	public String getA() {
		return a;
	}

	public SimpleObject setA(String a) {
		this.a = a;
		return this;
	}

	public boolean isB() {
		return b;
	}

	public SimpleObject setB(boolean b) {
		this.b = b;
		return this;
	}

	public List<String> getC() {
		return c;
	}

	public SimpleObject addC(String v){
		this.c.add(v);
		return this;
	}

	public SimpleObject setC(List<String> c) {
		this.c = c;
		return this;
	}

	public NestedObject getNested() {
		return nested;
	}

	public SimpleObject setNested(NestedObject nested) {
		this.nested = nested;
		return this;
	}

	public List<SimpleObject2> getSo2() {
		return so2;
	}

	public SimpleObject setSo2(List<SimpleObject2> so2) {
		this.so2 = so2;
		return this;
	}

	public SimpleObject2[] getSo2A() {
		return so2A;
	}

	public SimpleObject setSo2A(SimpleObject2[] so2A) {
		this.so2A = so2A;
		return this;
	}

//	@Override
//	public boolean equals(Object compare){
//		boolean result = true;
//		if(compare instanceof SimpleObject){
//			SimpleObject so = (SimpleObject)compare;
//			if(!this.a.equals(so.getA())){
//				result = false;
//				reportHealth("field a", this.a + " != " + so.getA());
//			}
//			if(this.b != so.isB()){
//				result = false;
//				reportHealth("field b", this.b + " != " + so.isB());
//			}
//			CompareUtility c = new Compare(this.c).using("==").to(so.getC());
//			if(!c.evaluate()){
//				result = false;
//				reportHealth("field c", c.healthCheck());
//			}
//		} else {
//			result = false;
//			reportHealth("object check", "not the same type");
//		}
//		return result;
//	}
}
