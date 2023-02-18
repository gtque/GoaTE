package com.thegoate.expect.test;

import com.thegoate.logging.volume.amp.BasicAmplifier;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
//@GoateAmplifier(type = SimpleObject.class)
public class SimpleObjectAmplifier extends BasicAmplifier{

	public SimpleObjectAmplifier(Object message) {
		super(message);
	}

	@Override
	public String amplify(Object message) {
		return message!=null?(get((SimpleObject)message)):null;
	}

	private String get(SimpleObject message){
		StringBuilder sb = new StringBuilder();
		sb.append("Tarun");
//		sb.append("[ a=").append(message.getA())
//			.append(", b=").append(message.isB())
//			.append(", c=").append(message.getC())
//			.append(" ]");
		return sb.toString();
	}

	@Override
	public boolean isType(Object check) {
		return check instanceof SimpleObject;
	}

}
