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
    protected String amplify(){
        StringBuilder full = new StringBuilder();
        StringBuilder ss = new StringBuilder();
        boolean skipped = false;
        StringBuilder zs = new StringBuilder();
        boolean zero = false;
        for (ExpectThreadExecuter expectation : ev.expectations()) {
            Expectation ex = expectation.getExpectation();
            Goate eval = ex.getExpectations();
            for (String key : eval.keys()) {
                Goate exp = eval.get(key, null, Goate.class);
                if (!checkInExpectationList(exp.get("actual"), exp.get("operator", null, String.class), ev.passes())) {
                    if (!checkInExpectationList(exp.get("actual"), exp.get("operator", null, String.class), ev.fails())) {
                        if (!("" + exp.get("actual")).contains("*")&&!("" + exp.get("actual")).contains("+")) {
                            logVolume(exp);
                            skipped = true;
                            ss.append(expectSeparator);
                            ss.append(exp.toString("\t", ""));
                            ev.fails().add(exp);
                        } else {
                            if(!("" + exp.get("actual")).contains("+")) {
                                exp.drop("from");
                                exp.drop("fromExpected");
                                zs.append(expectSeparator);
                                zs.append(exp.toString("\t", ""));
                                zero = true;
                            }
                        }
                    }
                }
            }
        }
        if (skipped) {
            ss.append(expectSeparator);
            //LOG.error("\nfailed (skipped):" + ss.toString());
            full.append("\nfailed (skipped)").append(ss);
        }
        if (zero) {
            zs.append(expectSeparator);
            //LOG.info("\nzero or more:" + zs.toString());
            full.append("\nzero or more").append(zs);
        }
        return full.toString();
    }

    protected boolean checkInExpectationList(Object actual, String operator, List<Goate> list) {
        String act = "" + actual;
        boolean result = false;
        for (Goate expectation : list) {
            if (act.equals("" + expectation.get("actual", null))) {
                if (operator.equals("" + expectation.get("operator", null, String.class))) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }
}
