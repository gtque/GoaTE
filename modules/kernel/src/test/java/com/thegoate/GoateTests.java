/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package com.thegoate;

import com.thegoate.data.DataLoader;
import com.thegoate.data.PropertyFileDL;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.Expectation;
import com.thegoate.map.EnumMap;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.togoate.ToGoate;

import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thegoate.locate.Locate.path;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by gtque on 4/21/2017.
 */
public class GoateTests extends TestNGEngineMethodDL {

	@Test(groups = {"unit"})
	public void testUrlEncodedPath() {
		String path = "nonexistant%402/sample.json";
		path = GoateUtils.getFilePath(path);
		assertEquals(path, "\\\\nonexistant@2\\sample.json");
	}

	@Test(groups = {"unit"})
	public void testEnumMapping() {
		String key = EnumMap.map("frickle").to(STACKLES.class).toString();
		assertEquals(key, "frackle");
	}

	public enum STACKLES {
		crackle("crickle"), frackle("frickle");
		String column = "";

		STACKLES(String column) {
			this.column = column;
		}

		public boolean map(String key) {
			return column.equals(key);
		}
	}

	@Test(groups = {"unit"})
	public void addAutoIncrement() {
		Goate data = new Goate()
			.put("test##", "a")
			.put("atest", "nanotime::")
			.put("test2", "c")
			.put("test##", "b");
		assertEquals(data.size(), 3);
		assertEquals(data.get("test2"), "b");
	}

	@Test(groups = {"unit"})
	public void addAutoIncrementNested() {
		Goate data = new Goate()
			.put("test##.##", "a")
			.put("test##", "b")
			.put("test id0", "pinky malinky")
			.put("test##.##", "c")
			.put("test0.##", "a1");
		assertEquals(data.size(), 5);
		assertEquals(data.get("test1"), "b");
		assertEquals(data.get("test0.1"), "a1");
		assertEquals(data.get("test2.0"), "c");
	}

	@Test(groups = {"unit"})
	public void staticDataLoader() {
		DataLoader dl = new StaticDL().add("test##", "a").add("atest", "nanotime::").add("test2", "c").add("test##", "b");
		Goate data = dl.load().get(0);
		assertEquals(data.size(), 3);
		assertEquals(data.get("test2"), "b");
	}

	@Test(groups = {"unit"})
	public void propertyFileDataLoaderStringPath() {
		DataLoader dl = new PropertyFileDL().file("sample.prop");
		Goate data = dl.load().get(0);

		assertEquals(data.size(), 5);
		assertEquals(data.get("test3"), "d");
	}

	@Test(groups = {"unit"})
	public void propertyFileDataLoaderFile() {
		System.out.println(System.currentTimeMillis());
		DataLoader dl = new PropertyFileDL().file(new File(GoateUtils.getFilePath("sample.prop")));
		Goate data = dl.load().get(0);
		System.out.println("goate data: \n" + data.toString());
		assertEquals(data.size(), 5);
		assertEquals(data.get("test3"), "d");
	}

	@Test(groups = {"unit"})
	public void filterGoate() {
		String d = "{\n" +
			"        \"a\": [\n" +
			"                {\"b\":true,\"f\":false},\n" +
			"                {\"b\":true,\"f\":false},\n" +
			"                {\"b\":true,\"f\":false},\n" +
			"                {\"b\":true}\n" +
			"            ],\n" +
			"        \"c\": 42\n" +
			"    }";
		Goate data = new ToGoate(d).convert();
		Goate f1 = data.filterStrict(path().match("a").dot().anyNumberOneOrMore().toPath());
		Goate f2 = data.filterStrict(path().match("a").dot().anyNumberOneOrMore().dot().matchOneOrMore("f").toPath());
		Goate f3 = data.filterStrict(path().match("a").dot().anyNumberOneOrMore().dot().match("f").nTimes(3).toPath());
		assertEquals(f1.size(), 4);
	}

	@Test(groups = {"unit"})
	public void mapToGoate() {
		Map<String, Object> the_map = new HashMap<>();
		the_map.put("a", 42.41);
		the_map.put("b", false);
		Goate g = new ToGoate(the_map).convert();
		System.out.println(g.toString());
	}

	@Test(groups = {"unit"})
	public void regeX() {
		String d = "-!a!--!a.0!--!a.0.b!--!a.0.f!--!a.1!--!a.1.b!--!a.1.f!--!a.2!--!a.2.b!--!a.2.f!--!a.3!--!a.3.b!--!c!-";
		String pattern = "(-!a\\.[0-9]+\\.f!-){3}";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(d);
		while (m.find()) {
			String key = m.group().replace("-!", "").replace("!-", "");
			System.out.println(key);
		}
	}

	@Test(groups = {"unit"})
	public void testBoolean() {
		Goate g = new Goate().put("actual", true);
		assertTrue(g.get("actual", false, Boolean.class));
	}

	@Test(groups = {"unit"})
	public void testShort() {
		Goate g = new Goate().put("actual", "42");
		short actual = g.get("actual", 84, Short.class);
		assertEquals(actual, 42);
	}

	@Test(groups = {"unit"})
	public void testInteger() {
		Goate g = new Goate().put("actual", "42");
		int actual = g.get("actual", 84, Integer.class);
		assertEquals(actual, 42);
	}

	@Test(groups = {"unit"})
	public void testDouble() {
		Goate g = new Goate().put("actual", "42");
		double actual = g.get("actual", 84, Double.class);
		assertEquals(actual, 42D);
	}

	@Test(groups = {"unit"})
	public void testFloat() {
		Goate g = new Goate().put("actual", "42.4");
		float actual = g.get("actual", 84.8, Float.class);
		assertEquals(actual, 42.4F);
	}

	@Test(groups = {"unit"})
	public void testDecimalCompare() {
		Goate g = new Goate().put("actual", 42.42D);
		Goate g2 = new Goate().put("actual", "42.42");
		//float actual = g.get("actual", 84.8, Float.class);
		float f = Float.parseFloat("$4,200.42".replaceAll("[,$]", ""));
		expect(Expectation.build()
			.actual(g)
			.isEqualTo(g2));
	}

	@Test(groups = {"unit"})
	public void testLong() {
		Goate g = new Goate().put("actual", "42");
		long actual = g.get("actual", 84L, Long.class);
		assertEquals(actual, 42L);
	}

	@Test(groups = {"unit"})
	public void testByte() {
		Goate g = new Goate().put("actual", 1);
		byte actual = g.get("actual", 2, Byte.class);
		byte expected = 1;
		assertEquals(actual, 1);
	}

	@Test(groups = {"unit"})
	public void testCharacter() {
		Goate g = new Goate().put("actual", "a");
		char actual = g.get("actual", 'b', Character.class);
		assertEquals(actual, 'a');
	}
}
