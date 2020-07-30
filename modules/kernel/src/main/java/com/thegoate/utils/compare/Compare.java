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
package com.thegoate.utils.compare;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.ParseDetector;
import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.UtilCache;
import com.thegoate.utils.compare.tools.CompareObject;
import com.thegoate.utils.compare.tools.CompareObjectEqualTo;
import com.thegoate.utils.type.FindType;

/**
 * Generic compare service.
 * Created by Eric Angeli on 5/9/2017.
 */
@UtilCache(name = "compare", useCache = true)
public class Compare extends UnknownUtilType implements CompareUtility {
    final BleatBox LOG = BleatFactory.getLogger(getClass());
    CompareUtility tool = null;
    Object actual = null;
    Object operator = null;
    Object expected = null;
    boolean compareNumeric = false;
    boolean triedOnce = false;

    public Compare(Object actual) {
        super();
        this.actual = actual;
    }

    public Compare triedOnce(boolean triedOnce){
    	this.triedOnce = triedOnce;
    	return this;
	}

    @Override
    public boolean isType(Object check) {
        return false;
    }

    @Override
    public Goate healthCheck() {
        return new Goate().merge(health, false).merge(tool != null ? tool.healthCheck() : new Goate(), false);
    }

    @Override
    public boolean checkType(Class tool, Class type) {
        CompareUtil tu = (CompareUtil) tool.getAnnotation(CompareUtil.class);
        return tu.type()!=null?(tu.type() == type):(type == null);
    }

    @Override
    public boolean evaluate() {
        boolean result = false;
        try {
            if (lookupTool()) {
                result = tool.evaluate();//step into evaluate here to debug the comparator implementation
            }
        } catch (Exception e) {
            LOG.debug("Compare", "Failed to compare: " + e.getMessage(), e);
        }
        return result;
    }

    protected boolean lookupTool() {
        Object act = actual;
        Object exp = expected;
        Class type = new FindType().type(act);
        if((type == null || triedOnce || type == String.class ) && (!(""+operator).equalsIgnoreCase("isNull"))){
            //because the type check may not be doing a parse check, so a string could still be something different,
            //check the expected to see if it has a specific type.
            Class etype = new FindType().type(exp);
            if(etype!=null){
                type = etype;
            }
        }

        try {
            tool = (CompareUtility)buildUtil(actual, CompareUtil.class, actual, ""+operator, CompareUtil.class.getMethod("operator"), type);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //if tool is still null, this indicates a problem trying to find the
        //right comparator. Either nothing was found or there was no default.
        // if tool is null, re-run debug and step into buildtool above.
        boolean result = true;
        if (tool == null) {
            result = false;
            health.put("Tool Not Found", "Could not find \"" + operator + "\" for: " + actual);
        } else {
            if(compareNumeric&&tool instanceof CompareObject){
                result = false;
                health.put("Tool Not Found", "Expecting to compare a numeric, but did not find an implementation for the numeric type: \"" + operator + "\" for: " + actual.getClass());
            } else {
                LOG.debug("Compare", "Found comparator: " + tool.getClass());
                if(tool instanceof CompareTool){
					((CompareTool)tool).triedOnce(triedOnce);
				}
                tool.actual(actual).to(expected).using(operator);
            }
        }
        return result;
    }

    protected CompareUtility buildTool(Object checkFor) {
        return buildTool(checkFor, "isType");
    }

    protected CompareUtility buildTool(Object checkFor, String isType){
        CompareUtility foundTool = null;
        try {
            foundTool = (CompareUtility) buildUtil(checkFor, CompareUtil.class, "" + operator, CompareUtil.class.getMethod("operator"), isType);
        } catch (NoSuchMethodException e) {
            LOG.error("Problem finding the compare utility: " + e.getMessage(), e);
        }
        return foundTool;
    }

    public CompareUtility getTool() {
        if (tool == null) {
            tool = buildTool(actual);
        }
        return tool;
    }

    @Override
    public CompareUtility actual(Object actual) {
        this.actual = actual;
        return this;
    }

    @Override
    public CompareUtility to(Object expected) {
        this.expected = expected;
        return this;
    }

    @Override
    public CompareUtility using(Object operator) {
        this.operator = operator;
        return this;
    }

    public Compare compareNumeric(boolean compareNumeric){
        this.compareNumeric = compareNumeric;
        return this;
    }
}
