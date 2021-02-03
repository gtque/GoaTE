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
