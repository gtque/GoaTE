package com.thegoate.eut;

import com.thegoate.Goate;
import com.thegoate.data.PropertyFileDL;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.fill.FillString;
import com.thegoate.utils.fill.serialize.GoateIgnore;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Goate approach to loading and handling properties. <br/>
 * This provides a loading method using reflection, which allows you to use polymorphism with your properties. <br/>
 * Extend `Eut` and add a static final singleton, name of your choice but we recommend eut or config, and initialize it using the static load method.<br/>
 * The parameter to `load` should be the Class itself. You should also set the Class as the generic type in the extends Eut&lt?&gt
 * example:
 * <pre><p>Simple config, not intended to be inherited</p>
 * {@code
 * public class SimpleConfig extends Eut<SimpleConfig> {
 *     public static final SimpleConfig eut = load(SimpleConfig.class);
 *
 *     public SimpleConfig eut() {
 *         return eut;
 *     }
 * }
 * }
 * </pre>
 * <pre><p>More complex config, that should be extended itself.</p>
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
 * </pre>
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

    protected void loadEut() {
        EutConfig propertiesDefinition = getClass().getAnnotation(EutConfig.class);
        EutSettings definition = new EutSettings();
        if (propertiesDefinition == null || propertiesDefinition.useEutConfigFile()) {
            //Loading the eut/eut.config file
            Goate config = new Goate();
            if (loadedEutFiles.containsKey("eut/eut.config")) {
                if(loadedEutFiles.get("eut/eut.config") instanceof EutSettings) {
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

        properties = loadFromCustomPropertiesFile(definition);

        GoateReflection gr = new GoateReflection();
        Map<String, Field> fields = gr.findFields(getClass());
        if (fields.size() > 0) {
            for (Map.Entry<String, Field> field : fields.entrySet()) {
                String fieldName = field.getKey();
                Field fieldself = field.getValue();
                boolean _accessible = fieldself.isAccessible();
                boolean _finalAfterLoad = fieldself.getAnnotation(FinalAfterLoad.class) != null;
                fieldself.setAccessible(true);
                Class fieldType = checkType(fieldself.getType());
                Object fieldValue = fieldself.get(this);
                //Object setValue1 = getProperty(source.toUpperCase().replace("-", "_").replace(".", "_") + fieldName.toUpperCase().replace("-", "_"), fieldValue, fieldType, properties);
                List<String> altNames = getAlternateName(fieldself, fieldName, sources);//.toLowerCase().replace("_", "."), source.toLowerCase());
                Object setValue1 = null;

                for (String altName : altNames) {
                    setValue1 = getProperty(altName, null, fieldType, properties);
                    if (setValue1 != null) {
                        LOGGER.debug("Properties", "Found a property with the alternate name: " + fieldName + ">" + altName);
                        properties.put(altName, setValue1);
                        break;
                    }
                }

                if (setValue1 != null) {
                    fieldself.set(this, setValue1);
                }
                if (_finalAfterLoad) {
                    fieldself.setAccessible(false);
                } else {
                    fieldself.setAccessible(_accessible);
                }
            }
        }
        LOGGER.debug("finished initializing properties...");
    }

    private List<String> buildListOfPropertyNames(Field field, String name) {
        List<String> names = new ArrayList<>();

        return names;
    }


    public void reset() {
//        try {
//            loadPropertiesFile();
//        } catch (IllegalAccessException e) {
//            LOG.warn("Properties", "Problem re-initializing properties: " + e.getMessage(), e);
//        }
    }

    public String get(String key) {
        return get(key, null, String.class);
    }

    public String get(String key, Object defaultValue) {
        return get(key, defaultValue, String.class);
    }

    public <T> T get(String key, Object defaultValue, Class<T> type) {
        return null;//getProperty(key, defaultValue, type, properties);
    }

    class SourceNode {
        SourceNode next = null;
        GoateSource theSource = null;

        public void addSource(GoateSource source) {
            if (source != null) {
                if (theSource == null) {
                    theSource = source;
                } else {
                    if (getPriority() <= source.priority()) {
                        if (next == null) {
                            next = new SourceNode();
                        }
                        next.addSource(source);
                    } else {
                        SourceNode temp = new SourceNode();
                        temp.addSource(theSource);
                        temp.next = next;
                        next = temp;
                        theSource = source;
                    }
                }
            }
        }

        public int getPriority() {
            return theSource == null ? 0 : theSource.priority();
        }

        public GoateSource getTheSource() {
            return theSource;
        }
    }

    public List<String> sourcePrefixList() {
        List<String> sources = new ArrayList<>();
        GoateSource[] sourceLabels = getClass().getAnnotationsByType(GoateSource.class);
        SourceNode head = new SourceNode();
        SourceNode finalHead = head;
        Arrays.stream(sourceLabels).forEach(source -> finalHead.addSource(source));
        head.addSource(defineDefaultSource(getClass().getSimpleName()));
        while (head != null) {
            String prefix = head.getTheSource().flatten() ? "" : head.getTheSource().key();
            if (!sources.contains(prefix)) {
                sources.add(prefix);
            }
            head = head.next;
        }
        return sources;
    }

    private GoateSource defineDefaultSource(String fieldName) {
        return new GoateSource() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return GoateSource.class;
            }

            @Override
            public Class source() {
                return null;
            }

            @Override
            public String key() {
                return fieldName.toLowerCase().replace("_", ".");
            }

            @Override
            public boolean flatten() {
                return false;
            }

            @Override
            public Class serializeTo() {
                return null;
            }

            @Override
            public boolean skipInModel() {
                return false;
            }

            @Override
            public int priority() {
                return Integer.MAX_VALUE;
            }
        };
    }

    public List<String> getAlternateNames(Field field, String name, List<String> sources) {
        List<String> alternate = new ArrayList<>();
        GoateSource[] altProperties = field.getAnnotationsByType(GoateSource.class);
        SourceNode head = new SourceNode();
        SourceNode finalHead = head;
        Arrays.stream(altProperties).forEach(source -> finalHead.addSource(source));
        head.addSource(defineDefaultSource(field.getName()));
        while (head != null) {
            String base = head.getTheSource().key();
            //base2 is recommended to be used for setting as environment properties and is all uppercase with - and . replaced with _
            //example: if original is hello-world.greeting then use HELLO_WORLD_GREETING as the property name for the environment variable.
            //although technically it HELLO_WORLD_GREETING would also work if used in the properties file, it is recommended to only use
            //that pattern when setting it through environment variables to help preserve your sanity.
            String base2 = base.toUpperCase().replace("-", "_").replace(".", "_");
            if (head.getTheSource().flatten()) {
                if (!alternate.contains(base2)) {
                    alternate.add(base2);
                }
                if (!alternate.contains(base)) {
                    alternate.add(base);
                }
            } else {
                sources.stream().forEach(source -> addAlternates(alternate, source, base, base2));
            }
            head = head.next;
        }
        return alternate;
    }

    private void addAlternates(List<String> alternates, String prefix, String base, String base2) {
        String full1 = prefix + (prefix.isEmpty() ? "" : ".") + base;
        String prefix2 = prefix.toUpperCase().toUpperCase().replace("-", "_").replace(".", "_");
        String full2 = prefix2 + (prefix2.isEmpty() ? "" : "_") + base2;
        if (!alternates.contains(full2)) {
            alternates.add(full2);
        }
        if (!alternates.contains(full1)) {
            alternates.add(full1);
        }
    }
//    protected void loadPropertiesFile() throws IllegalAccessException {
//    }
//
//    private <Type> Type getProperty(String key, Object def, Class<Type> type, Goate properties) {
//        Object property = properties.get(key, def, type);
//        return (Type) property;
//    }
//
//    private Class checkType(Class type) {
//        GoateReflection gr = new GoateReflection();
//        if (gr.isPrimitive(type)) {
//            if (gr.isBooleanType(type)) {
//                type = Boolean.class;
//            } else if (gr.isByteType(type)) {
//                type = Byte.class;
//            } else if (gr.isIntegerType(type)) {
//                type = Integer.class;
//            } else if (gr.isDoubleType(type)) {
//                type = Double.class;
//            } else if (gr.isFloatType(type)) {
//                type = Float.class;
//            } else if (gr.isLongType(type)) {
//                type = Long.class;
//            } else if (gr.isCharacterType(type)) {
//                type = Character.class;
//            } else if (gr.isShortType(type)) {
//                type = Short.class;
//            }
//        }
//        return type;
//    }
//
//
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
        String eutProfile = "" + (new FillString(fileName)).with(propDef);
        String file = propDef.get("location") + "/" + eutProfile + "." + extension;
        if (!(new File(GoateUtils.getFilePath(file))).exists()) {
            file = defaultFile;
        }
        if (!loadedPropertyFiles.containsKey(file)) {
            loadedPropertyFiles.put(file, (new PropertyFileDL()).file(file).load().get(0));

        }
        return (Goate) loadedPropertyFiles.get(file);
    }
}
