package com.thegoate.staff;

import com.thegoate.Goate;

/**
 * Created by Eric Angeli on 6/26/2020.
 */
public interface Worker<T, S> {
	S work();
	HealthRecord getHrReport();
	T setName(String name);
	String parameterName(String parameter);
	String getName();
	String getNameDef();
	T initData();
	T init(Goate data);
	T setData(Goate data);
	T mergeData(Goate data);
	T defaultPeriod(long period);
	T period(long period);
	T clockIn();
	Goate scrub(Goate data);
	Goate clean(Goate data, String[] scrub);
	String[] detailedScrub();

}
