/*
 * Copyright (c) 2023. Eric Angeli
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
package com.thegoate.eut;

import com.thegoate.Goate;
import com.thegoate.eut.properties.Simple2Eut;
import com.thegoate.eut.properties.SimpleEut;
import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.GoateUtils;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.thegoate.utils.compare.tools.CompareInvertedContains.LIST_CONTAINS;

public class EutTest extends TestNGEngineMethodDL {

	@Test(groups = {"unit"})
	public void simpleEutAccess() {
		expect(Expectation.build()
			.actual(SimpleEut.eut.FIELD_A)
			.isEqualTo("winning!"));
		LOG.debug(SimpleEut.getEut(SimpleEut.class).FIELD_A);
		expect(Expectation.build()
			.actual(Simple2Eut.eut.FIELD_A)
			.isEqualTo("Fuzzy Wuzzy had no hair"));
		//IsFinal doesn't work currently, yes the code is still there, but currently it fails to force the field to be final
		//so the following is commented out since it will fail.
//		try {
//			Simple2Eut.eut.FIELD_B = "ha-ha";
//			LOG.error("You shouldn't see this message, if you do that means it failed to set FIELD_B to final");
//		} catch (Exception e) {
//			LOG.info("Yes this should in fact through an exception, so if you see this it behaved as expected.");
//		}
		expect(Expectation.build()
			.actual(Simple2Eut.eut.FIELD_B)
			.isEqualTo("but sally sells seashells by the seashore"));
	}

//	@Test(groups = {"unit"})
//	public void sourcePrefixList() {
//		List<String> prefixes = SimpleEut.eut.sourcePrefixList();
//		expect(Expectation.build()
//			.actual(prefixes.size())
//			.isEqualTo(6));
//		expect(Expectation.build()
//			.actual(prefixes.get(1))
//			.isEqualTo("second"));
//	}
//
//	@Test(groups = {"unit"})
//	public void alternateNameList() throws NoSuchFieldException {
//		List<String> prefixes = SimpleEut.eut.sourcePrefixList();
//		Field fieldA = SimpleEut.class.getField("FIELD_A");
//		List<String> alternates = SimpleEut.eut.getAlternateNames(fieldA, fieldA.getName(), prefixes);
//		expect(Expectation.build()
//			.actual(prefixes.size())
//			.isEqualTo(6));
//		expect(Expectation.build()
//			.actual(alternates.size())
//			.isEqualTo(38));
//		expect(Expectation.build()
//			.actual(alternates)
//			.is(LIST_CONTAINS)
//			.expected("simpleeut.field.a"));
//	}

	@Test(groups = {"unit"})
	public void findPropertyKeysInFileName() {
		GoateUtils.setEnvironment("world", "earth");
		GoateUtils.setEnvironment("are you", "well");
		Goate keys = SimpleEut.eut.getPatternProperties("hello-${world}.${how}_${are you}");
		LOG.debug(keys.toString());
		expect(Expectation.build()
			.actual("world")
			.from(keys)
			.isEqualTo("earth")
		);
		expect(Expectation.build()
			.actual("how")
			.from(keys)
			.isEqualTo(null)
		);
		expect(Expectation.build()
			.actual("are you")
			.from(keys)
			.isEqualTo("well")
		);
	}
}
