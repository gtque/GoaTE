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
import com.thegoate.expect.Expectation;
import com.thegoate.expect.builder.ExpectationBuilder;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.logging.BleatLevel;
import com.thegoate.logging.volume.amp.Amplifier;
import com.thegoate.logging.volume.amp.GoateAmplifier;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.Utility;

import java.util.Arrays;
import java.util.logging.Level;

/**
 * Created by Eric Angeli on 7/2/2020.
 */
@GoateAmplifier(type = StackTraceElement[].class)
@IsDefault(forType = true)
public class StackTraceAmplifier implements Amplifier {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    Object message;

    public StackTraceAmplifier(Object message) {
        this.message = message;
    }

    @Override
    public String amplify(Object message) {
        StringBuilder amplified = new StringBuilder("");
        BleatLevel level = LOG.level();
        StackTraceElement[] stack = (StackTraceElement[]) message;
        if (stack != null) {
            if (level.isLoudEnough(Level.INFO)) {
                LOG.debug("stack isn't logged because the level is set to INFO or higher.");
            } else if (level.isLoudEnough(Level.CONFIG)) {
                GoateReflection gr = new GoateReflection();
                boolean foundNonExpectClass = false;
                for (int i = 0; !foundNonExpectClass && i < stack.length; i++) {
                    Class klass = gr.findClass(stack[i].getClassName());
                    if(!(Expectation.class.isAssignableFrom(klass) || ExpectationBuilder.class.isAssignableFrom(klass))) {
                        amplified.append(stack[i]).append("\n");
                        foundNonExpectClass = true;
                    }
                }
            } else {
                amplified.append(Arrays.toString(stack).replace(',', '\n'));
            }
        }
        return amplified.toString();
    }

    @Override
    public Goate healthCheck() {
        return null;
    }

    @Override
    public Utility setData(Goate data) {
        return null;
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof StackTraceElement;
    }
}
