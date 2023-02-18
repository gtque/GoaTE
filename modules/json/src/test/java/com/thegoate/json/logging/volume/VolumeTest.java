package com.thegoate.json.logging.volume;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.thegoate.expect.Expectation;
import com.thegoate.json.utils.type.JsonType;
import com.thegoate.testng.ExpectToFail;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.GoateUtils;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.thegoate.logging.volume.VolumeKnob.volume;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Eric Angeli on 7/2/2020.
 */
public class VolumeTest extends TestNGEngineMethodDL {

	String sample = "{\n"
		+ "\t\"a\":42,\n"
		+ "\t\"b\":\"hello, world!\"\n"
		+ "}";
	String sample2 = "{\n"
		+ "\t\"a\":3.14159,\n"
		+ "\t\"b\":\"hello, jim!\"\n"
		+ "}";

	String sampleA = "[\n\t{\n"
		+ "\t\t\"a\":42,\n"
		+ "\t\t\"b\":\"hello, world!\"\n"
		+ "\t},\n"
	    + "\t{\n"
		+ "\t\t\"a\":42,\n"
		+ "\t\t\"b\":\"hello, world!\"\n"
		+ "\t}\n]";

	String sampleA2 = "[\n\t{\n"
		+ "\t\t\"a\":42,\n"
		+ "\t\t\"b\":\"hello, world!\"\n"
		+ "\t},\n"
		+ "\t{\n"
		+ "\t\t\"a\":3.14159,\n"
		+ "\t\t\"b\":\"hello, world!\"\n"
		+ "\t}\n]";
	@AfterMethod
	public void resetVolume() {
		GoateUtils.removeEnvironment("volume.json.level");
		GoateUtils.removeEnvironment("json.compare.strict");
	}

	@Test(groups = {"unit"})
	public void minVolume() {
		//GoateUtils.setEnvironment("volume.json.level", Volume.MIN.name());
		Logger root = (Logger) LoggerFactory.getLogger(JsonAmplifier.class);
		Level old = root.getLevel();
		root.setLevel(Level.ERROR);
		expect(Expectation.build()
			.actual("{...}")
			.isEqualTo(volume(sample)));
		root.setLevel(old);
	}

	@Test(groups = {"unit"})
	public void minVolumeArray() {
		//GoateUtils.setEnvironment("volume.json.level", Volume.MIN.name());
		Logger root = (Logger) LoggerFactory.getLogger(JsonAmplifier.class);
		Level old = root.getLevel();
		root.setLevel(Level.ERROR);
		expect(Expectation.build()
			.actual("[...]")
			.isEqualTo(volume(sampleA)));
		root.setLevel(old);
	}

	@Test(groups = {"unit"})
	public void defaultMaxVolume() {
		Logger root = (Logger) LoggerFactory.getLogger(JsonAmplifier.class);
		Level old = root.getLevel();
		root.setLevel(Level.INFO);
		expect(Expectation.build()
			.actual(sample)
			.isEqualTo(volume(sample)));
		root.setLevel(old);
	}

	@Test(groups = {"unit"})
	public void minVolumeJustInOutput() {
		//GoateUtils.setEnvironment("volume.json.level", Volume.MIN.name());
		Logger root = (Logger) LoggerFactory.getLogger(JsonAmplifier.class);
		Level old = root.getLevel();
		root.setLevel(Level.ERROR);
		expect(Expectation.build()
			.actual(sample)
			.isEqualTo(sample));
		root.setLevel(old);
	}

	@Test(groups = {"unit"})
	public void maxVolumeJustInOutput() {
//		GoateUtils.setEnvironment("volume.json.level", Volume.MIN.name());
		expect(Expectation.build()
			.actual(sample)
			.isEqualTo(sample));
	}
	@ExpectToFail
	@Test(groups = {"unit"})
	public void minVolumeJustInOutputFail() {
//		GoateUtils.setEnvironment("volume.json.level", Volume.MIN.name());
		boolean passed = true;
		try {
			expect(Expectation.build()
				.actual(sample)
				.isEqualTo(sample2));
			evaluate();
			passed = false;
		} catch (Throwable t) {
			LOG.debug("volume test", "I threw an exception because I should have failed.");
		}
		expect(Expectation.build()
			.actual(passed)
			.isEqualTo(true));
	}
	@ExpectToFail
	@Test(groups = {"unit"})
	public void maxVolumeJustInOutputFail() {
//		GoateUtils.setEnvironment("volume.json.level", Volume.MIN.name());
		boolean passed = true;
		try {
			expect(Expectation.build()
				.actual(sample)
				.isEqualTo(sample2));
			evaluate();
			passed = false;
		} catch (Throwable t) {
			LOG.debug("volume test", "I threw an exception because I should have failed.");
		}
		expect(Expectation.build()
			.actual(passed)
			.isEqualTo(true));
	}
	@ExpectToFail
	@Test(groups = {"unit"})
	public void minVolumeJustInOutputFailArray() {
		//GoateUtils.setEnvironment("volume.json.level", Volume.MIN.name());
		GoateUtils.setEnvironment("json.compare.strict", ""+true);
		boolean passed = true;
		try {
			expect(Expectation.build()
				.actual(sampleA)
				.isEqualTo(sampleA2));
			evaluate();
			passed = false;
		} catch (Throwable t) {
			LOG.debug("volume test", "I threw an exception because I should have failed.");
		}
		expect(Expectation.build()
			.actual(passed)
			.isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void parseJson(){
		String a = "{'a':true} != {'a':false}";
		String a2 = "list: {'a':true} , {'a':false}";
		String b = "{'a':true, 'b':[{'c':1},{'c':2}]} != {'a':false}";
		String c = "{'a':true, 'b':[{'c':1} , " +
				"{'c':2}]}";
		String d = "['a',true] != ['a',false]";
		String e = "[['a'],[true]] != {'a':false}";
		String f = "[['a'],[true]] != [['a'],[false]]";
		String g = "[['a'],[true]]";
		String h = "[{'c':1},{'c':2}]";

//		boolean ra = new JsonType().isType(a);
//		assertFalse(ra);
		boolean ra2 = new JsonType().isType(a2);
		assertFalse(ra2);
//		boolean rb = new JsonType().isType(b);
//		assertFalse(rb);
		boolean rc = new JsonType().isType(c);
		assertTrue(rc);
//		boolean ra = new JsonType().isType(a);
//		assertFalse(ra);
//		assertFalse(r);
	}
}
