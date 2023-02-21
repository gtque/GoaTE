/*
 * Copyright (c) 2023. Eric Angeli
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

package com.thegoate.eut;

import com.thegoate.Goate;
import com.thegoate.data.PropertyFileDL;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.fill.FillString;
import com.thegoate.utils.fill.serialize.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Goate approach to loading and handling properties. <br/>
 * This provides a loading method using reflection, which allows you to use polymorphism with your properties. <br/>
 * Extend `Eut` and add a static final singleton, name of your choice but we recommend eut or config, and initialize it using the static load method.<br/>
 * The parameter to `load` should be the Class itself. You should also set the Class as the generic type in the extends Eut&lt;?&gt;
 * example:
 * <div><pre>Simple config, not intended to be inherited
 * {@code
 * public class SimpleConfig extends Eut<SimpleConfig> {
 *     public static final SimpleConfig eut = load(SimpleConfig.class);
 *
 *     public SimpleConfig eut() {
 *         return eut;
 *     }
 * }
 * }
 * </pre></div>
 * <div><pre>More complex config, that should be extended itself.
 * {@code
 * public class ParentConfig<CONFIG extends ParentConfig> extends Eut<CONFIG> {
 *     public String FIELD_A = "tiny fluffy kittens";
 * }
 *
 * public class ChildConfig extends ParentConfig<ChildConfig> {
 *     public static final ChildConfig eut = load(ChildConfig.class);
 *
 *     public String FIELD_B = "have lost their mittens";
 *
 *     public ChildConfig eut() {
 *         return eut;
 *     }
 * }
 *
 * public class Usage {
 *
 *     public String getMessage() {
 *         return ChildConfig.eut.FIELD_A + " " + ChildConfig.eut.FIELD_B;
 *     }
 * }
 * }
 * </pre>
 * </div>
 *
 * @param <EUT> Should be the class itself, unless it too is abstract and intended to be extended, in which it should take a generic type and pass it Eut's generic type.
 */
public abstract class Eut<EUT extends Eut> extends Kid {

    @GoateIgnore
    private static BleatBox SLOG = BleatFactory.getLogger(Eut.class);
    @GoateIgnore
    private BleatBox LOG = BleatFactory.getLogger(getClass());
    @GoateIgnore
    protected Goate properties;
    @GoateIgnore
    private static Goate loadedEutFiles = new Goate();

    /**
     * When extending Eut, implement this method and return the static singleton you defined and initialized using the static load method.
     *
     * @return The static singleton
     */
    public abstract EUT eut();

    public static <EUT extends Eut> EUT load(Class<EUT> eutType) {
        EUT eut = null;
        try {
            eut = eutType.getDeclaredConstructor().newInstance();
            if (eut.eut() == null) {
                eut.loadEut();
            } else {
                eut = (EUT) eut.eut();
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            SLOG.error("EUT", "Problem initializing eut: " + e.getMessage(), e);
        }
        return eut;
    }

    public static <EUT extends Eut> EUT getEut(Class<EUT> eutType) {
        return (EUT) load(eutType).eut();
    }

    private EutSettings defineEutSettings() {
        EutConfig propertiesDefinition = getClass().getAnnotation(EutConfig.class);
        EutSettings definition = new EutSettings();
        if (propertiesDefinition == null || propertiesDefinition.useEutConfigFile()) {
            //Loading the eut/eut.config file
            Goate config = new Goate();
            if (loadedEutFiles.containsKey("eut/eut.config")) {
                if (loadedEutFiles.get("eut/eut.config") instanceof EutSettings) {
                    definition = (EutSettings) loadedEutFiles.get("eut/eut.config");
                } else {
                    config = (Goate) loadedEutFiles.get("eut/eut.config");
                }
            } else {
                //try loading the file.
                List<Goate> configs = (new PropertyFileDL()).file("eut/eut.config").load();
                if (configs.size() > 0) {
                    config = configs.get(0);
                }
            }
            if (!definition.isConfigured()) {
                definition.setLocation(config.get("location", "settings", String.class))
                        .setExtension(config.get("extension", "properties", String.class))
                        .setPattern(config.get("pattern", "config-${profile}", String.class))
                        .setDefaultFile(config.get("default", "settings/config.properties", String.class))
                        .setDefaultProfile(config.get("default.profile", "localdev", String.class))
                        .setUseEutSettings(config.get("useEutSettings", true, Boolean.class))
                        .configured();
                loadedEutFiles.put("eut/eut.config", definition);
            }
        } else {
            definition.setLocation(propertiesDefinition.location())
                    .setExtension(propertiesDefinition.extension())
                    .setPattern(propertiesDefinition.pattern())
                    .setDefaultFile(propertiesDefinition.defaultFile())
                    .setDefaultProfile(propertiesDefinition.defaultProfile())
                    .setUseEutSettings(false)
                    .configured();
        }
        return definition;
    }

    protected void loadEut() {
        EutSettings definition = defineEutSettings();
        properties = loadFromCustomPropertiesFile(definition);
        DeSerializer deSerializer = new DeSerializer().pojo(this).from(AllOrderedSource.class).data(properties);
        deSerializer.build(getClass());
        properties = deSerializer.getDupeData();
        LOG.debug("finished initializing properties...");
    }

    public void reset() {
        loadEut();
    }

    public String get(String key) {
        return get(key, null, String.class);
    }

    public String get(String key, Object defaultValue) {
        return get(key, defaultValue, String.class);
    }

    public <T> T get(String key, Object defaultValue, Class<T> type) {
        return properties.get(key, defaultValue, type);//null;//getProperty(key, defaultValue, type, properties);
    }

    private <Type> Type getProperty(String key, Object def, Class<Type> type, Goate properties) {
        Object property = properties.get(key, def, type);
        return (Type) property;
    }

    private Goate loadFromCustomPropertiesFile(EutSettings propertiesDefinition) {
        String location = propertiesDefinition.getLocation();
        String extension = propertiesDefinition.getExtension();
        String defaultFile = propertiesDefinition.getDefaultFile();
        String pattern = propertiesDefinition.getPattern();
        String defaultProfile = propertiesDefinition.getDefaultProfile();
        String profile = getProperty("profile", defaultProfile, String.class, new Goate());
        Goate propDef = new Goate()
                .put("location", location)
                .put("extension", extension)
                .put("pattern", pattern)
                .put("default", defaultFile)
                .put("default.profile", defaultFile)
                .put("profile", profile);
        String fileName = propDef.get("pattern", defaultFile, String.class);
        propDef.merge(getPatternProperties(fileName), false);
        String eutProfile = "" + (new FillString(fileName)).with(propDef);
        String file = propDef.get("location") + "/" + eutProfile + "." + extension;
        if (!(new File(GoateUtils.getFilePath(file))).exists()) {
            file = defaultFile;
        }
        Goate loadedEut = null;
        if (!loadedEutFiles.containsKey(file)) {
            loadedEut = (new PropertyFileDL()).file(file).load().get(0);

        }
        loadedEut = loadedEutFiles.get(file, loadedEut, Goate.class);
        return loadedEut;
    }

    public Goate getPatternProperties(String input) {
        Goate patternProperties = new Goate();
        String keyPattern = "\\$\\{[^}]*\\}";
        Pattern pattern = Pattern.compile(keyPattern);
        ArrayList list = new ArrayList();
        //Matching the compiled pattern in the String
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String key = "" + it.next();
            key = key.replace("${", "").replace("}", "");
            patternProperties.put(key, getProperty(key, null, String.class, patternProperties));
        }
        return patternProperties;
    }
}
