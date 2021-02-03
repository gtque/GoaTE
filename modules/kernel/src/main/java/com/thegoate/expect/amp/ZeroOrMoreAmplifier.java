package com.thegoate.expect.amp;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.logging.volume.amp.GoateAmplifier;

@GoateAmplifier(type = ZeroOrMoreChannel.class)
@IsDefault(forType = true)
public class ZeroOrMoreAmplifier extends StatusAmplifier {

    public ZeroOrMoreAmplifier(Object message) {
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
        StringBuilder zs = new StringBuilder();
        boolean zero = false;
        for (Goate exp : ev.zeroOrMore()) {
            exp.drop("from");
            exp.drop("fromExpected");
            zs.append(expectSeparator);
            zs.append(exp.toString("\t", ""));
            zero = true;
        }
        if (zero) {
            zs.append(expectSeparator);
            //LOG.info("\nzero or more:" + zs.toString());
            full.append("\nzero or more").append(zs);
        }
        return full.toString();
    }
    

    @Override
    public boolean isType(Object check) {
        return false;
    }
}
