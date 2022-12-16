package com.thegoate;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
public final class Veterinarian {
	Goate health;

	public Veterinarian(Goate health){
		this.health = health;
	}

	public Goate getHealth(){
		return health;
	}

	@Override
	public String toString(){
		if(health!=null){
			String chart = "" + health.toString();
			return chart;
		}
		return null;
	}
}
