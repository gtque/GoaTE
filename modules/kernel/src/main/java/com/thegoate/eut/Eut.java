package com.thegoate.eut;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.fill.serialize.GoateIgnore;
import com.thegoate.utils.fill.serialize.Kid;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Eut<EUT extends Eut> extends Kid {

    @GoateIgnore
    private static BleatBox SLOG = BleatFactory.getLogger(Eut.class);
    @GoateIgnore
    private BleatBox LOG = BleatFactory.getLogger(getClass());
    @GoateIgnore
    protected Goate properties;
    @GoateIgnore
    private static Goate loadedEutFiles = new Goate();

    public abstract EUT config();

    public static <EUT extends Eut> EUT load(Class<EUT> eutType) {
        EUT eut = null;
        try {
            eut = eutType.getDeclaredConstructor().newInstance();
            eut.loadEut();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            SLOG.error("EUT", "Problem initializing eut: " + e.getMessage(), e);
        }
        return eut;
    }

    public static <EUT extends Eut> EUT getConfig(Class<EUT> eutType) {
        return (EUT)load(eutType).config();
    }

    protected void loadEut(){

    }

    private List<String> buildListOfPropertyNames(Field field, String name){
        List<String> names = new ArrayList<>();

        return names;
    }


}
