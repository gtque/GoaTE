package com.thegoate.utils.fill.serialize;

import java.lang.reflect.Field;
import java.util.Map;

import com.thegoate.Goate;
import com.thegoate.HealthMonitor;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareUtility;

/**
 * Created by Eric Angeli on 7/3/2020.
 */
public class Nanny implements HealthMonitor,TypeT {

	@GoateIgnore
	private Class goateType = Object.class;

	@GoateIgnore
	private Goate health = new Goate();

	public void setGoateType(Class type){
		this.goateType = type;
	}

	@Override
	public Class goateType(){
		return this.goateType;
	}

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

	@Override
	public boolean equals(Object compare){
		boolean result = true;
		if(compare == null) {
			result = false;
			reportHealth(getClass().getSimpleName(), "{ initialized instance != null }");
		}
		if(getClass().isInstance(compare)){
			GoateReflection gr = new GoateReflection();
			for(Map.Entry<String, Field> field:gr.findFields(getClass()).entrySet()){
				boolean accessible = field.getValue().isAccessible();
				try {
					field.getValue().setAccessible(true);
					Object actual = field.getValue().get(this);
					Object expected = field.getValue().get(compare);
					CompareUtility check = new Compare(actual).to(expected).using("==");
					if(!check.evaluate()){
						result = false;
						String checkHealth = check.healthCheck().toString("\t\t", "", true);
						String label = actual.getClass().getSimpleName() + " " + field.getKey();
						if(checkHealth.isEmpty()) {
							//reportHealth("field mismatch ##",  label + " {" + actual + " != " + expected + "}");
							reportHealth(label, " {" + actual + " != " + expected + "}");
						} else {
							//checkHealth.replace("%_goate_t_%", "%_goate_t_%%_goate_t_%").replace("\t","%_goate_t_%");
							checkHealth = check.healthCheck().pad(checkHealth + "\n\t}", "\t");
							//reportHealth("field mismatch ##", label + " {\n" + checkHealth);
							reportHealth(label, " {\n" + checkHealth);
						}
					}
				} catch (Exception e){
					reportHealth("field access ##", "problem accessing field: " + e.getMessage());
				} finally {
					field.getValue().setAccessible(accessible);
				}
			}
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(":{");
		GoateReflection gr = new GoateReflection();
		boolean notFirst = false;
		for(Map.Entry<String, Field> field:gr.findFields(getClass()).entrySet()){
			if(notFirst){
				sb.append(", ");
			}
			sb.append(field.getKey()).append(":");
			boolean accessible = field.getValue().isAccessible();
			Object fieldValue = "Exception: could not get toString";
			try {
				field.getValue().setAccessible(true);
				fieldValue = field.getValue().get(this);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			sb.append(fieldValue);
			notFirst = true;
		}
		sb.append("}");
		return sb.toString();
	}
}
