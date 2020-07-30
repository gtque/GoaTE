package com.thegoate.utils.fill.serialize;

import com.thegoate.Goate;
import com.thegoate.HealthMonitor;

/**
 * Created by Eric Angeli on 7/3/2020.
 */
@GoatePojo
public class Kid implements HealthMonitor {

	@GoateIgnore
	private Goate health = new Goate();

	/**
	 * Resets the health collection to a new empty one.
	 */
	protected void resetHealth(){
		health = new Goate();
	}

	public void reportHealth(String id, Object message){
		health.put(id, message);
	}

	/**
	 * This returns and resets the current health report.
	 * @return Goate collection containing any reported health issues.
	 */
	public Goate healthCheck(){
		Goate report = health;
		resetHealth();
		return report;
	}
}
