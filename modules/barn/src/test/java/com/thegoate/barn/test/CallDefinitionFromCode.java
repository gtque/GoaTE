package com.thegoate.barn.test;

import com.thegoate.Goate;
import com.thegoate.barn.staff.ApiTester;
import com.thegoate.barn.staff.BarnPreviewEmployee;
import com.thegoate.staff.Employee;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.GetFileAsString;
import com.thegoate.utils.togoate.ToGoate;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 10/3/2018.
 */
public class CallDefinitionFromCode {

    @Test(groups = {"api"})
    public void callDefinition(){
        Employee e = new ApiTester();
        Goate data = new Goate();
        data.put("barn source",new Get("testcases/apis/simple.json").from("file::")).put("barn root","testcases/apis");
        BarnPreviewEmployee bpe = new BarnPreviewEmployee();
        e.init(new ToGoate("" + bpe.init(data).work()).convert());
        Response r = (Response)e.work();
        assertEquals(r.statusCode(), 200);
    }
}
