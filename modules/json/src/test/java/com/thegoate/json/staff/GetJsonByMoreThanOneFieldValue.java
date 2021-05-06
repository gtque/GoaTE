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
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.NotFound;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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

	@Test(groups = {"unit"})
	public void nestedArrayFind() {
		String result = "{\"content\":[{\"id\":\"8aaec0f065437141016543bc37270028\",\"createdBy\":\"wi01\",\"createDate\":\"2018-08-16T17:15:33Z\",\"lastModifiedBy\":\"wi01\",\"lastModificationDate\":\"2018-08-30T14:09:22Z\",\"udfResourceList\":[{\"id\":\"8aaec0f0658b205901658b2755140000\",\"createdBy\":\"wi01\",\"createDate\":\"2018-08-30T14:05:35Z\",\"lastModifiedBy\":null,\"lastModificationDate\":null,\"udfCode\":\"LOAN_DATE_UDF\",\"udfDisplay\":\"date udf\",\"udfFieldTypeId\":\"2AF9861173FA278EE0530100007FEBF7\",\"udfFieldTypeCode\":\"TXT_ENTRY\",\"udfFieldTypeDesc\":\"Text Entry\",\"udfDataTypeId\":\"2AF9861173F7278EE0530100007FEBF7\",\"udfDataTypeCd\":\"DT\",\"udfDataTypeDesc\":\"Date\",\"udfLength\":10,\"udfDecimalLength\":null,\"predefinedInd\":false,\"displayFormattedInd\":false,\"udfGroupId\":null,\"udfGroupCd\":null,\"defaultValue\":null,\"defaultUdfValueCode\":null,\"defaultUdfValueDesc\":null,\"categoryCode\":\"USER_DEFINED_DATA\",\"activeDataInd\":true,\"hideInd\":false,\"requiredInd\":false,\"udfValueId\":\"8aaec0f0658b205901658b27d1ce0009\",\"udfValue\":\"2018-08-06\",\"udfValueCode\":null,\"udfValueDesc\":null,\"objectId\":\"8aaec0f065437141016543bc37270028\",\"objectType\":\"LOAN\",\"orgUnitId\":null},{\"id\":\"2c9eeb145d56999b015d571c57b90001\",\"createdBy\":\"wi09\",\"createDate\":\"2017-07-18T23:11:01Z\",\"lastModifiedBy\":null,\"lastModificationDate\":null,\"udfCode\":\"LOAN_A\",\"udfDisplay\":\"a\",\"udfFieldTypeId\":\"2AF9861173F9278EE0530100007FEBF7\",\"udfFieldTypeCode\":\"CHECKBOX\",\"udfFieldTypeDesc\":\"Checkbox\",\"udfDataTypeId\":\"2AF9861173F6278EE0530100007FEBF7\",\"udfDataTypeCd\":\"BOOLEAN\",\"udfDataTypeDesc\":\"Boolean\",\"udfLength\":1,\"udfDecimalLength\":null,\"predefinedInd\":false,\"displayFormattedInd\":false,\"udfGroupId\":null,\"udfGroupCd\":null,\"defaultValue\":null,\"defaultUdfValueCode\":null,\"defaultUdfValueDesc\":null,\"categoryCode\":\"USER_DEFINED_DATA\",\"activeDataInd\":true,\"hideInd\":false,\"requiredInd\":false,\"udfValueId\":\"8aaec0f06586ce61016586f2a4e00001\",\"udfValue\":\"false\",\"udfValueCode\":null,\"udfValueDesc\":null,\"objectId\":\"8aaec0f065437141016543bc37270028\",\"objectType\":\"LOAN\",\"orgUnitId\":null},{\"id\":\"8aaec0f0653ef7ef01653efd427e0012\",\"createdBy\":\"wi00\",\"createDate\":\"2018-08-15T19:08:29Z\",\"lastModifiedBy\":null,\"lastModificationDate\":null,\"udfCode\":\"LOAN_LAME_UDF\",\"udfDisplay\":\"lame udf\",\"udfFieldTypeId\":\"2AF9861173FA278EE0530100007FEBF7\",\"udfFieldTypeCode\":\"TXT_ENTRY\",\"udfFieldTypeDesc\":\"Text Entry\",\"udfDataTypeId\":\"2AF9861173F5278EE0530100007FEBF7\",\"udfDataTypeCd\":\"ALPHANUM\",\"udfDataTypeDesc\":\"Alphanumeric\",\"udfLength\":10,\"udfDecimalLength\":null,\"predefinedInd\":false,\"displayFormattedInd\":false,\"udfGroupId\":null,\"udfGroupCd\":null,\"defaultValue\":null,\"defaultUdfValueCode\":null,\"defaultUdfValueDesc\":null,\"categoryCode\":\"USER_DEFINED_DATA\",\"activeDataInd\":true,\"hideInd\":false,\"requiredInd\":false,\"udfValueId\":\"8aaec0f06586ce61016586f2a4e00002\",\"udfValue\":\"22\",\"udfValueCode\":null,\"udfValueDesc\":null,\"objectId\":\"8aaec0f065437141016543bc37270028\",\"objectType\":\"LOAN\",\"orgUnitId\":null}],\"assetType\":\"SERIAL\",\"loanNumber\":3164,\"approvalNumber\":null,\"loanDescription\":\"2012 HOND CIVIC EX SD\",\"invoiceNumber\":null,\"adviceNumber\":null,\"invoiceAmount\":100,\"originalPrincipalAmount\":100,\"currentPrincipalAmount\":100,\"fundedAmount\":100,\"fundedIndicator\":true,\"commitmentAllocationAmount\":null,\"estimatedValueAmount\":null,\"postDate\":\"2018-08-16\",\"shipDate\":\"2016-08-01\",\"originalEffectiveDate\":\"2016-08-01\",\"effectiveDate\":\"2017-07-06\",\"invoiceDate\":\"2016-08-01\",\"invoiceReceiptDate\":\"2016-07-06\",\"fundingDate\":\"2017-07-06\",\"maturityDate\":\"2018-07-06\",\"loanStatusDate\":null,\"deleteReason\":null,\"legalDescription\":null,\"presoldIndicator\":false,\"retailCustomerTypeId\":null,\"retailCustomerTypeCode\":null,\"retailCustomerTypeDesc\":null,\"retailCustomerName\":null,\"guarantorName\":null,\"creditCheckedIndicator\":false,\"amortIndicator\":false,\"precomputedIndicator\":false,\"customPaymentScheduleIndicator\":false,\"paymentScheduleOverrideIndicator\":false,\"drawCount\":0,\"poNumber\":null,\"leaseNumber\":null,\"leaseTerm\":\"0\",\"leaseTypeId\":null,\"leaseTypeCode\":null,\"leaseTypeDesc\":null,\"leasePaymentAmount\":0,\"leaseStartDate\":null,\"insuranceValueAmount\":0,\"residualAmount\":null,\"serialNumber\":\"19XFB2F84CE336579\",\"originalSerialNumber\":null,\"stockNumber\":null,\"modelName\":\"CIVIC EX\",\"yearNumber\":2012,\"bodyName\":\"SD\",\"brandId\":\"2AF986119812278EE0530100007FEBF7\",\"brandCode\":\"HOND\",\"brandDesc\":\"HONDA\",\"mileageQuantity\":0,\"batchNumber\":725,\"forceMaturedIndicator\":false,\"sauIndicator\":false,\"soldDate\":null,\"assetQuantity\":1,\"perUnitAmount\":null,\"activeIndicator\":true,\"duplicateSerialNumberOverrideIndicator\":false,\"supplierCustomerNumber\":\"YUU\",\"decodeServiceUsedIndicator\":true,\"cctTransferCount\":0,\"epdPaidIndicator\":false,\"epdPaidAmount\":0,\"transferOriginalLoanNumber\":null,\"residualBillingDate\":null,\"externalFundingDate\":null,\"restrictCollateralApprovedIndicator\":false,\"amortizedLoanTypeId\":null,\"amortizedLoanTypeCode\":null,\"amortizedLoanTypeDesc\":null,\"loanOriginationSourceTypeId\":\"2AF986117169278EE0530100007FEBF7\",\"loanOriginationSourceTypeCode\":\"DAS_TRF\",\"loanOriginationSourceTypeDesc\":\"DAS transfer\",\"approvalStatusTypeId\":\"2AF986116549278EE0530100007FEBF7\",\"approvalStatusTypeCode\":\"APPROVED\",\"approvalStatusTypeDesc\":\"Approved\",\"loanStatusTypeId\":null,\"loanStatusTypeCode\":null,\"loanStatusTypeDesc\":null,\"securitizationTypeId\":null,\"securitizationTypeCode\":null,\"securitizationTypeDesc\":null,\"securitizationClassTypeId\":null,\"securitizationClassTypeCode\":null,\"securitizationClassTypeDesc\":null,\"productTypeId\":\"8ae0a11752b2e4bc0152b2ff9b5f0707\",\"productTypeCode\":\"NEW_CARS\",\"productTypeDesc\":\"New Cars\",\"loanTypeId\":\"2AF98611713B278EE0530100007FEBF7\",\"loanTypeCode\":\"PAY_AS_SOLD\",\"loanTypeDesc\":\"Pay as Sold\",\"retailFinanceTypeId\":null,\"retailFinanceTypeCode\":null,\"retailFinanceTypeDesc\":null,\"decodeServiceTypeId\":null,\"decodeServiceTypeCode\":null,\"decodeServiceTypeDesc\":null,\"customerId\":\"8ac2a3d05c650233015c6541f95e0003\",\"customerNumber\":\"PB0002\",\"customerDBAName\":\"DBA PB New Cust1\",\"customerCollateralTypeId\":\"8aaec0f065437141016543b3078e0006\",\"customerCollateralTypeDesc\":\"BOATS COLLATERAL\",\"collateralTypeId\":\"8ac2a3a854a14fdc0154eea727e40912\",\"collateralTypeCode\":\"BOATS_COLLATERAL\",\"collateralTypeDesc\":\"BOATS COLLATERAL\",\"financialTermScheduleId\":\"8aaec0f065437141016543b3233a0008\",\"financialPlanId\":\"8ac2a3665907146e015908451f1f0dfd\",\"financialPlanCode\":\"NK_PLAN_CD_001\",\"financialPlanDesc\":\"NK Plan Code 001\",\"principalPaymentScheduleId\":\"8ac2a3665907146e01590840a1990de0\",\"principalPaymentScheduleDesc\":\"NK_PPS_001\",\"financialBatchId\":\"2c9e88375da8b018015da8c8b2e80000\",\"customerLocationAddressId\":null,\"commitmentId\":null,\"sourceLoanId\":\"8ac2a3a854a154700155c0d40b4436d2\",\"loanRequestId\":null,\"supplierId\":\"8ac2a3a854a1547001559d2e8cd4351c\",\"invoiceId\":null,\"loanAddressId\":null,\"firstPaymentDate\":null,\"streetLine1\":null,\"streetLine2\":null,\"cityName\":null,\"postalCode\":null,\"countryTypeId\":null,\"countryTypeCode\":null,\"countryTypeDesc\":null,\"stateTypeId\":null,\"stateTypeCode\":null,\"stateTypeDesc\":null,\"lastPrincipalPaymentAmount\":null,\"lastPrincipalPaymentPostDate\":null,\"lastPrincipalPaymentEffectiveDate\":null,\"lastAdjustmentAmount\":null,\"lastAdjustmentPostDate\":null,\"lastAdjustmentEffectiveDate\":null,\"freePeriodEndDate\":\"2018-07-06\",\"retainedOriginalCurtailmentInd\":false,\"hasEarlyPaymentDiscount\":false,\"messages\":[],\"exceptions\":[]}],\"page\":{\"size\":20,\"totalElements\":1,\"totalPages\":1,\"number\":0}}";
		Object val = new GetJsonByFieldValue()
			.json(result)
			.root("content\\.[" + 0 + "]\\.udfResourceList\\.[0-9]")
			.pathPattern("udfDataTypeCd")
			.valueToFind("DT")
			.work();
		expect(Expectation.build()
			.actual("udfValue")
			.from(val)
			.isNull(false));
	}

	@Test(groups = {"unit"})
	public void compareJsonArrays() {
		String j1 = "[{'a':true,'b':3.14159'}]";
		String j2 = "[{'b':3.14159','a':true}]";

		expect(Expectation.build()
			.actual(j1)
			.isEqualTo(j2));
	}

	@Test(groups = {"unit"})
	public void compareJsonArrayContains() {
		String j1 = "[{'a':true,'b':3.14159'}]";
		String j2 = "[{'b':1.159','a':false, 'c':42}, {'b':3.14159','a':true, 'c':42}]";

		expect(Expectation.build()
			.actual(j1)
			.isIn(j2));
	}

	@Test(groups = {"unit"})
	public void compareJsonStringArrayContains() {
		String j1 = "\"halloworld\",\"hello world\"";

		//        new GetJsonByFieldValue()
		//            .json(j1)
		//            .pathPattern("[0-9]+")
		//            .
		//        expect(Expectation.build()
		//            .actual("[0-9]+")
		//            .from(j1)
		//            .contains("o w"));
//		expect(Expectation.build()
//			.actual("o w")
//			.isIn(j1));
		String from = "{\"content\":[{\"a\":\"hello world\",\"b\":\"howdy\"},{\"b\":\"hello world\",\"a\":\"howdy\"}]}";
		List<String> searchFields = new ArrayList<>();
		searchFields.add("a");
		searchFields.add("b");
		Object element = new Get("content"+".0").from(from);
		String seperator = "_$searchfield:_";
		for(int index = 0; !(element instanceof NotFound) && element != null; index++) {
			StringBuilder searchTextFields = new StringBuilder();
			for(String searchField:searchFields){
				searchTextFields.append(seperator).append(new Get(searchField).from(element));
			}
			expect(Expectation.build()
				.actual("o w")
				.isIn(searchTextFields.toString())
				.failureMessage("this should not be here: "+element));
			element = new Get("content"+"."+index).from(from);
		}
	}

	@Test(groups = {"unit"})
	public void compareJsonArrayDoesNotContains() {
		String j1 = "[{'a':false,'b':3.14159'}]";
		String j2 = "[{'b':1.159','a':false, 'c':42}, {'b':3.14159','a':true, 'c':42}]";

		expect(Expectation.build()
			.actual(j1)
			.isIn(j2));
		boolean passed = true;
		try {
			evaluate();
			passed = false;
		} catch (Throwable t) {
			LOG.info("Failed as expected.");
		}
		assertTrue(passed);
	}

}
