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
import com.thegoate.utils.fill.serialize.GoateIgnore;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.GoateSourceNode;
import com.thegoate.utils.fill.serialize.IsFinal;
import com.thegoate.utils.fill.serialize.Kid;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private EutSettings defineEutSettings() {
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
		return definition;
	}
    protected void loadEut() {

        EutSettings definition = defineEutSettings();

        properties = loadFromCustomPropertiesFile(definition);

		List<String> sources = sourcePrefixList();

        GoateReflection gr = new GoateReflection();
        Map<String, Field> fields = gr.findFields(getClass());
        if (fields.size() > 0) {
			Field modifiersField = gr.findField(Field.class,"modifiers");
			boolean modifiersFieldAccessible = false;
			if(modifiersField != null) {
				modifiersFieldAccessible = modifiersField.canAccess(this);
				modifiersField.setAccessible(true);
			}

			for (Map.Entry<String, Field> field : fields.entrySet()) {
                String fieldName = field.getKey();
                Field fieldself = field.getValue();
				int fieldModifiers = fieldself.getModifiers();
                boolean _accessible = fieldself.canAccess(this);
                boolean _finalAfterLoad = fieldself.getAnnotation(IsFinal.class) != null;
                fieldself.setAccessible(true);

				if(modifiersField != null) {
					try {
						modifiersField.setInt(this, fieldModifiers & ~Modifier.FINAL);
					} catch (IllegalAccessException e) {
						LOG.warn("EUT", "Couldn't remove \"final\" modifier, may encounter an issue trying to set the field.");
					}
				}
				Class fieldType = gr.toType(fieldself.getType());
                List<String> altNames = getAlternateNames(fieldself, fieldName, sources);//.toLowerCase().replace("_", "."), source.toLowerCase());
                Object setValue1 = null;
                for (String altName : altNames) {
                    setValue1 = getProperty(altName, null, fieldType, properties);
                    if (setValue1 != null) {
                        LOG.debug("Properties", "Found a property with the alternate name: " + fieldName + ">" + altName);
                        properties.put(altName, setValue1);
                        break;
                    }
                }

                if (setValue1 != null) {
					try {
						fieldself.set(this, setValue1);
					} catch (IllegalAccessException e) {
						LOG.error("Eut", "Unable to set property \""+ fieldself.getName() + "\" in the eut/config pojo: " + e.getMessage());
					}
				}
                if (_finalAfterLoad) {
                    fieldself.setAccessible(false);
					if(modifiersField != null) {
						try {
							modifiersField.setInt(this, fieldModifiers ^ Modifier.FINAL);
						} catch (IllegalAccessException e) {
							LOG.warn("Eut", "Unable to make field final.");
						}
					}
                } else {
                    fieldself.setAccessible(_accessible);
					if(modifiersField != null) {
						try {
							modifiersField.setInt(this, fieldModifiers);
						} catch (IllegalAccessException e) {
							LOG.warn("EUT", "Couldn't reset field modifiers: " + e.getMessage());
						}
					}
				}
            }
			if(modifiersField != null) {
				modifiersField.setAccessible(modifiersFieldAccessible);
			}
        }
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
        return null;//getProperty(key, defaultValue, type, properties);
    }

    public List<String> sourcePrefixList() {
        List<String> sources = new ArrayList<>();
        GoateSource[] sourceLabels = getClass().getAnnotationsByType(GoateSource.class);
		GoateSourceNode head = new GoateSourceNode();
		GoateSourceNode finalHead = head;
        Arrays.stream(sourceLabels).forEach(source -> finalHead.addSource(source));
        head.addSource(defineDefaultSource(getClass().getSimpleName()));
        while (head != null) {
            String prefix = head.getTheSource().flatten() ? "" : head.getTheSource().key();
            if (!sources.contains(prefix)) {
                sources.add(prefix);
            }
            head = head.getNext();
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
		GoateSourceNode head = new GoateSourceNode();
		GoateSourceNode finalHead = head;
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
            head = head.getNext();
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

	public Goate getPatternProperties(String input){
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
		while(it.hasNext()){
			String key = ""+it.next();
			key = key.replace("${","").replace("}","");
			patternProperties.put(key, getProperty(key, null, String.class, patternProperties));
		}
		return patternProperties;
	}
}
