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
package com.thegoate.eut.properties;

import com.thegoate.eut.Eut;
import com.thegoate.eut.EutConfig;
import com.thegoate.utils.fill.serialize.IsFinal;

@EutConfig(useEutConfigFile = true)
public class Simple2Eut extends Eut<Simple2Eut> {

    public static final Simple2Eut eut = load(Simple2Eut.class);

    public String FIELD_A = "Fuzzy Wuzzy had no hair";

	@IsFinal
	public String FIELD_B = "Fuzzy Wuzzy wasn't fuzzy was he";

    @Override
    public Simple2Eut eut() {
        return eut;
    }
}
