package com.thegoate.expect.amp;

import com.thegoate.Goate;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.logging.BleatLevel;
import com.thegoate.logging.volume.amp.BasicAmplifier;

import java.util.List;
import java.util.logging.Level;

import static com.thegoate.Goate.GOATE_VARIABLE_PREFIX;
import static com.thegoate.dsl.words.EutConfigDSL.eut;

public abstract class StatusAmplifier extends BasicAmplifier {

    protected BleatBox LOG = BleatFactory.getLogger(getClass());
    protected BleatLevel level = null;
    protected boolean mute = false;
    protected String test = null;
    protected ExpectEvaluator ev;
    protected List<Goate> status;

    protected final String expectSeparator = "\n--------------------\n";

    public StatusAmplifier(Object message) {
        super(message);
        level = LOG.level();
    }

    public StatusAmplifier muteFrom(boolean mute) {
        this.mute = mute;
        return this;
    }

    public StatusAmplifier testName(String test) {
        this.test = test;
        return this;
    }

    protected void logVolume(Goate p) {
        if (eut("expect.scenario", false, Boolean.class)) {
            p.put("_scenario", test);
        }
        if (mute || level.isLoudEnough(Level.WARNING)) {
            p.drop("from");
            p.drop("fromExpected");
        }
        if (level.isLoudEnough(Level.SEVERE)) {
            p.drop("actual");
            p.drop("expected");
        }
        //StackTrace amplifier has to be controlled by the log level of the stack trace amplifier, not the status amplifier.
        if (BleatFactory.getLogger(StackTraceAmplifier.class).level().isLoudEnough(Level.INFO)) {
            p.drop("stack");
        }
        if (p.get("actual") != null && p.get("actual") instanceof String) {
            if (p.get("actual", "", String.class).equals(p.get("from"))) {
                p.drop("from");
            }
            p.put("actual", p.get("actual", "", String.class).replace(GOATE_VARIABLE_PREFIX, ""));
        }
    }

    @Override
    public String amplify(Object message) {
        ev = (ExpectEvaluator) message;
        setStatus();
        return amplify();
    }

    protected abstract void setStatus();

    protected String amplify() {
        StringBuilder ps = new StringBuilder();
        for (Goate p : status) {
            logVolume(p);
            ps.append(expectSeparator);
            ps.append(p.toString("\t", ""));
        }
        if (ps.length() > 0) {
            ps.append(expectSeparator);
        }
        return ps.toString();
    }
}
