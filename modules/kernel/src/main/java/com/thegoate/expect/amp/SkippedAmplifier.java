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
package com.thegoate.expect.amp;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.expect.ExpectThreadExecuter;
import com.thegoate.expect.Expectation;
import com.thegoate.logging.volume.amp.GoateAmplifier;

import java.util.List;

@GoateAmplifier(type = SkippedChannel.class)
@IsDefault(forType = true)
public class SkippedAmplifier extends StatusAmplifier {

    public SkippedAmplifier(Object message) {
        super(message);
    }

    @Override
    protected void setStatus() {
    }

    @Override
    protected void logVolume(Goate p) {
        super.logVolume(p);
        if (p.get("failure message", null) == null) {
            p.drop("failure message");
        }
//        p.drop("stack");
    }

    @Override
    protected String amplify() {
        StringBuilder full = new StringBuilder();
        StringBuilder ss = new StringBuilder();
        boolean skipped = false;
        for (Goate exp : ev.skipped()) {
            logVolume(exp);
            skipped = true;
            ss.append(expectSeparator);
            ss.append(exp.toString("\t", ""));
        }
        if (skipped) {
            ss.append(expectSeparator);
            //LOG.error("\nfailed (skipped):" + ss.toString());
            full.append("\nfailed (skipped)").append(ss);
        }

        return full.toString();
    }


    @Override
    public boolean isType(Object check) {
        return false;
    }
}
