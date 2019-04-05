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
package com.thegoate.json.staff;

import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineAnnotatedDL;
import org.testng.annotations.Test;

/**
 * Created by Eric Angeli on 4/3/2019.
 */
public class GetJsonByMoreThanOneFieldValue extends TestNGEngineAnnotatedDL {

    String json = "{\"data\":[{\n" +
            "        \"aggregations\": [\n" +
            "            {\n" +
            "                \"aggregateFunctionType\": \"SUM\",\n" +
            "                \"property\": \"currentAmount\",\n" +
            "                \"value\": \"35555.52\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"aggregateFunctionType\": \"COUNT\",\n" +
            "                \"property\": \"loan.id\",\n" +
            "                \"value\": \"4\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"groupKey\": {\n" +
            "            \"customer.id\": \"42\",\n" +
            "            \"accountGroupType.id\": \"2AF986116F61278EE0530100007FEBF7\"\n" +
            "        }\n" +
            "    },\n" +
            "    {\n" +
            "        \"aggregations\": [\n" +
            "            {\n" +
            "                \"aggregateFunctionType\": \"SUM\",\n" +
            "                \"property\": \"currentAmount\",\n" +
            "                \"value\": \"34146.06\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"aggregateFunctionType\": \"COUNT\",\n" +
            "                \"property\": \"loan.id\",\n" +
            "                \"value\": \"4\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"groupKey\": {\n" +
            "            \"customer.id\": \"43\",\n" +
            "            \"accountGroupType.id\": \"2AF986116F61278EE0530100007FEBF7\"\n" +
            "        }\n" +
            "    }\n" +
            "]}";
    String expected = "[\n" +
            "            {\n" +
            "                \"aggregateFunctionType\": \"SUM\",\n" +
            "                \"property\": \"currentAmount\",\n" +
            "                \"value\": \"34146.06\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"aggregateFunctionType\": \"COUNT\",\n" +
            "                \"property\": \"loan.id\",\n" +
            "                \"value\": \"4\"\n" +
            "            }\n" +
            "        ]";
    @Test(groups = {"unit"})
    public void twoFieldValues() {
        Object found = new GetJsonByFieldValue()
                .json(json)
                .root("data\\.[0-9]+")
                .pathPattern("groupKey\\.accountGroupType\\.id")
                .valueToFind("2AF986116F61278EE0530100007FEBF7")
                .secondaryCondition("groupKey", "customer.id", 43)
                .work();
        LOG.debug("found", "" + found);
        expect(Expectation.build()
                .actual("aggregations")
                .from(found)
                .isEqualTo(expected));

    }
}
