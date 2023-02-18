package com.thegoate.expect.test;

import com.thegoate.Goate;
import com.thegoate.utils.fill.serialize.GoateIgnore;
import com.thegoate.utils.fill.serialize.Kid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
public class SimpleObjectHealth extends Kid {
	private String a;
	private boolean b;
	private List<String> c = new ArrayList<>();
	@GoateIgnore
	private Goate health = new Goate();

	public String getA() {
		return a;
	}

	public SimpleObjectHealth setA(String a) {
		this.a = a;
		return this;
	}

	public boolean isB() {
		return b;
	}

	public SimpleObjectHealth setB(boolean b) {
		this.b = b;
		return this;
	}

	public List<String> getC() {
		return c;
	}

	public SimpleObjectHealth addC(String v){
		this.c.add(v);
		return this;
	}

	public SimpleObjectHealth setC(List<String> c) {
		this.c = c;
		return this;
	}

//	@Override
//	public boolean equals(Object compare){
//		boolean result = true;
//		if(compare instanceof SimpleObjectHealth){
//			SimpleObjectHealth so = (SimpleObjectHealth)compare;
//			if(!this.a.equals(so.getA())){
//				result = false;
//				health.put("field a", this.a + " != " + so.getA());
//			}
//			if(this.b != so.isB()){
//				result = false;
//				health.put("field b", this.b + " != " + so.isB());
//			}
//			CompareUtility c = new Compare(this.c).using("==").to(so.getC());
//			if(!c.evaluate()){
//				result = false;
//				health.merge(c.healthCheck(),false);
//			}
//		} else {
//			result = false;
//			health.put("object check", "not the same type");
//		}
//		return result;
//	}

	@Override
	public Goate healthCheck(){
		return this.health;
	}
}
