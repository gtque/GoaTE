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
package com.thegoate.json.utils.compare;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.thegoate.Goate;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.json.utils.compare.tools.CompareJsonEqualTo;
import com.thegoate.json.utils.compare.tools.EqualIgnoreFields;
import com.thegoate.testng.TestNGEngineAnnotatedDL;
import com.thegoate.utils.compare.CompareUtility;

/**
 * Created by Eric Angeli on 8/13/2018.
 */
public class IgnoreFieldsTest extends TestNGEngineAnnotatedDL {

    String actual = "{\n" +
            "    \"_embedded\": {\n" +
            "        \"codeTypes\": [\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B113E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"APPROVED\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Approved\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B113E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B313E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"PENDING\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Pending\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B313E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B413E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"REJECTED\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Rejected\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B413E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B713E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"OPEN\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": true,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Open\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://locsfgdadfgsafdghafgdalhost:7005/api/code-type/240AA32B91B713E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"_links\": {\n" +
            "        \"self\": {\n" +
            "            \"href\": \"http://localhost:7005/api/code-type/?parentCodeTypeCode=DAS_LOAN&associationUsage=APPR_STATUS_CATEGORY_MAPPING\"\n" +
            "        }\n" +
            "    },\n" +
            "    \"page\": {\n" +
            "        \"size\": 4,\n" +
            "        \"totalElements\": 4,\n" +
            "        \"totalPages\": 1,\n" +
            "        \"number\": 0\n" +
            "    }\n" +
            "}";
    String expected = "{" +
            "   \"expected json\":{\n" +
            "    \"_embedded\": {\n" +
            "        \"codeTypes\": [\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B113E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"APPROVED\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Approved\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B113E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B313E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"PENDING\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Pending\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B313E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B413E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"REJECTED\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Rejected\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B413E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B713E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"OPEN\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": true,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Open\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B713E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"_links\": {\n" +
            "        \"self\": {\n" +
            "            \"href\": \"http://localhost:7005/api/code-type/?parentCodeTypeCode=DAS_LOAN&associationUsage=APPR_STATUS_CATEGORY_MAPPING\"\n" +
            "        }\n" +
            "    },\n" +
            "    \"page\": {\n" +
            "        \"size\": 4,\n" +
            "        \"totalElements\": 4,\n" +
            "        \"totalPages\": 1,\n" +
            "        \"number\": 0\n" +
            "    }\n" +
            "}," +
            "   \"_goate_ignore\": [" +
            "   \"_embedded\\\\.codeTypes\\\\.[0-9]{1,}\\\\._links\"" +
            "]" +
            "}";
    String expected2 = "{\n" +
            "    \"_embedded\": {\n" +
            "        \"codeTypes\": [\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B113E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"APPROVED\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Approved\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B113E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B313E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"PENDING\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Pending\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B313E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B413E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"REJECTED\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": false,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Rejected\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B413E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"240AA32B91B713E0E040010AEE01354F\",\n" +
            "                \"createdBy\": \"CONVERSION\",\n" +
            "                \"createDate\": \"2006-12-07T21:46:53.000+0000\",\n" +
            "                \"lastModifiedBy\": \"CONVERSION\",\n" +
            "                \"lastModificationDate\": \"2016-06-13T22:23:27.000+0000\",\n" +
            "                \"codeTypeCode\": \"OPEN\",\n" +
            "                \"codeGroupCode\": \"APPR_STATUS_TYP\",\n" +
            "                \"hideIndicator\": true,\n" +
            "                \"editIndicator\": false,\n" +
            "                \"deleteIndicator\": false,\n" +
            "                \"codeTypeDisplay\": \"Open\",\n" +
            "                \"exceptions\": null,\n" +
            "                \"_links\": {\n" +
            "                    \"self\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-type/240AA32B91B713E0E040010AEE01354F\"\n" +
            "                    },\n" +
            "                    \"codeGroup\": {\n" +
            "                        \"href\": \"http://localhost:7005/api/code-group/APPR_STATUS_TYP\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"_links\": {\n" +
            "        \"self\": {\n" +
            "            \"href\": \"http://localhost:7005/api/code-type/?parentCodeTypeCode=DAS_LOAN&associationUsage=APPR_STATUS_CATEGORY_MAPPING\"\n" +
            "        }\n" +
            "    },\n" +
            "    \"page\": {\n" +
            "        \"size\": 4,\n" +
            "        \"totalElements\": 4,\n" +
            "        \"totalPages\": 1,\n" +
            "        \"number\": 0\n" +
            "    },\n" +
            "   \"_goate_ignore\": [" +
            "   \"_embedded\\\\.codeTypes\\\\.[0-9]{1,}\\\\._links\"" +
            "]" +
            "}";

    @Test(groups = {"unit"})
    public void regexPatternTest() {
        Goate data = new Goate();
        data.put("actual", actual)
                .put("expected2", expected2);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect(new Expectation(data).define("o::actual,isEqualIgnoreFields,o::expected2"));
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean resultact = ev.evaluate();
        CompareUtility comp = new EqualIgnoreFields(actual).to(expected);
        CompareUtility compa = new EqualIgnoreFields(actual).to(expected2);
        CompareUtility comp2 = new CompareJsonEqualTo(actual).to(expected);
        boolean result = comp.evaluate();
        boolean resulta = compa.evaluate();
        System.out.println(comp.healthCheck());
        assertTrue(result);
        assertTrue(resulta);
        assertTrue(resultact);
        assertFalse(comp2.evaluate());
    }

    String j1 = "{\n" +
            "\t\"a\": 42,\n" +
            "\t\"b\": true,\n" +
            "\t\"c\": true,\n" +
            "\t\"d\": true\n" +
            "}";
    String j2 = "{\n" +
            "\t\"a\": 42,\n" +
            "\t\"b\": false,\n" +
            "\t\"c\": false,\n" +
            "\t\"d\": false\n" +
            "}";
    @Test(groups = {"unit"})
    public void buildIgnoreEqualsFieldExpected() {
        List<String> ignore = new ArrayList<String>() {{
            add("c");
            add("d");
        }};
        expect(Expectation.build()
                .actual(j1)
                .is(EqualIgnoreFields.expected(j2)
                        .ignoreField("b")
                        .ignoreFields(ignore)));
    }
}
