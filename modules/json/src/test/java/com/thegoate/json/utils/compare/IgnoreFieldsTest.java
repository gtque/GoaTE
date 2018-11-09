package com.thegoate.json.utils.compare;

import com.thegoate.Goate;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.json.utils.compare.tools.CompareJsonEqualTo;
import com.thegoate.json.utils.compare.tools.IsEqualIgnoreFields;
import com.thegoate.utils.compare.CompareUtility;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Eric Angeli on 8/13/2018.
 */
public class IgnoreFieldsTest {

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
    public void regexPatternTest(){
        Goate data = new Goate();
        data.put("actual", actual)
                .put("expected2", expected2);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect(new Expectation(data).define("o::actual,isEqualIgnoreFields,o::expected2"));
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean resultact = ev.evaluate();
        CompareUtility comp = new IsEqualIgnoreFields(actual).to(expected);
        CompareUtility compa = new IsEqualIgnoreFields(actual).to(expected2);
        //CompareUtility compact = new IsEqualIgnoreFields(actual).to(actual);
        CompareUtility comp2 = new CompareJsonEqualTo(actual).to(expected);
        boolean result = comp.evaluate();
        boolean resulta = compa.evaluate();
        //boolean resultact = compact.evaluate();
        System.out.println(comp.healthCheck());
        assertTrue(result);
        assertTrue(resulta);
        assertTrue(resultact);
        assertFalse(comp2.evaluate());
    }
}
