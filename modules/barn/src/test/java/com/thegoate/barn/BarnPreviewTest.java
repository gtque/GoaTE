package com.thegoate.barn;

import com.thegoate.Goate;
import com.thegoate.barn.staff.BarnPreviewEmployee;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.get.Get;
import org.testng.annotations.Test;

/**
 * Created by Eric Angeli on 4/18/2018.
 */
public class BarnPreviewTest {
    @Test(groups = {"unit"})
    public void barnPreview(){
        Goate d = new Goate().put("barn source",new Get("testcases/apis/simple.json").from("file::")).put("barn root","testcases/apis");
        BarnPreviewEmployee bpe = new BarnPreviewEmployee();
        String preview = "" + bpe.init(d).work();
        System.out.println(preview);
    }
}
