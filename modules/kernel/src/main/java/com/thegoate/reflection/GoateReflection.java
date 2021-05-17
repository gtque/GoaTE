/*
 * Copyright (c) 2017. Eric Angeli
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

package com.thegoate.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.fill.serialize.GoateIgnore;

/**
 * Some simple custom methods for reflection.
 * This class cannot use BleatBox, it causes a circular dependency.
 * Created by gtque on 4/24/2017.
 */
public class GoateReflection {

	public Constructor findConstructor(Class theClass, Object[] args) {
		return findConstructor(theClass.getConstructors(), args);
	}

	public Constructor findConstructor(Constructor<?>[] constructors, Object[] args) {
		Constructor p = null;
		for (Constructor constructor : constructors) {
			Class<?>[] ptypes = constructor.getParameterTypes();
			boolean found = false;
			if (ptypes.length == args.length) {
				found = true;
				for (int i = 0; i < args.length; i++) {
					if (args[i] != null && isPrimitive(args[i].getClass())) {
						if (ptypes[i].equals(Integer.TYPE)) {
							if (!(args[i] instanceof Integer)) {
								found = false;
							}
						} else if (ptypes[i].equals(Byte.TYPE)) {
							if (!(args[i] instanceof Byte)) {
								found = false;
							}
						} else if (ptypes[i].equals(Double.TYPE)) {
							if (!(args[i] instanceof Double)) {
								found = false;
							}
						} else if (ptypes[i].equals(Float.TYPE)) {
							if (!(args[i] instanceof Float)) {
								found = false;
							}
						} else if (ptypes[i].equals(Long.TYPE)) {
							if (!(args[i] instanceof Long)) {
								found = false;
							}
						} else if (ptypes[i].equals(Boolean.TYPE)) {
							if (!(args[i] instanceof Boolean)) {
								found = false;
							}
						} else if (ptypes[i].equals(Character.TYPE)) {
							if (!(args[i] instanceof Character)) {
								found = false;
							}
						} else if (!ptypes[i].equals(Object.class)) {
							found = false;
						}
					} else {
						if (args[i] != null && !ptypes[i].isAssignableFrom(args[i].getClass())) {
							found = false;
						}
					}
				}
			}
			if (found) {
				p = constructor;
				break;
			}
		}
		return p;
	}

	public boolean isPrimitiveOrNumerical(Object o) {
		return o != null && (isPrimitive(o.getClass()) || o instanceof Number || primitiveType(o) != null);
	}

	public boolean isPrimitive(Class c) {
		return c.equals(Boolean.class) || c.equals(Boolean.TYPE)
			|| c.equals(Byte.class) || c.equals(Byte.TYPE)
			|| c.equals(Integer.class) || c.equals(Integer.TYPE)
			|| c.equals(Double.class) || c.equals(Double.TYPE)
			|| c.equals(Float.class) || c.equals(Float.TYPE)
			|| c.equals(Long.class) || c.equals(Long.TYPE)
			|| c.equals(Character.class) || c.equals(Character.TYPE)
			|| c.equals(Short.class) || c.equals(Short.TYPE);
	}

	public boolean isBooleanType(Class c) {
		return c.equals(Boolean.class) || c.equals(Boolean.TYPE);
	}

	public boolean isByteType(Class c) {
		return c.equals(Byte.class) || c.equals(Byte.TYPE);
	}

	public boolean isIntegerType(Class c) {
		return c.equals(Integer.class) || c.equals(Integer.TYPE);
	}

	public boolean isDoubleType(Class c) {
		return c.equals(Double.class) || c.equals(Double.TYPE);
	}

	public boolean isFloatType(Class c) {
		return c.equals(Float.class) || c.equals(Float.TYPE);
	}

	public boolean isLongType(Class c) {
		return c.equals(Long.class) || c.equals(Long.TYPE);
	}

	public boolean isCharacterType(Class c) {
		return c.equals(Character.class) || c.equals(Character.TYPE);
	}

	public boolean isShortType(Class c) {
		return c.equals(Short.class) || c.equals(Short.TYPE);
	}

	public boolean isBoolean(Object c) {
		String cs = "" + c;
		return (c instanceof Boolean) || cs.equalsIgnoreCase("true") || cs.equalsIgnoreCase("false");
	}

	public boolean isByte(Object c) {
		String cs = "" + c;
		boolean result = false;
		try {
			Byte.parseByte(cs);
			result = true;
		} catch (Throwable e) {
			//            LOG.debug(""+actual + " is not a float.");
		}
		return result;
	}

	public boolean isInteger(Object c) {
		String cs = "" + c;
		boolean result = false;
		try {
			Integer.parseInt(cs);
			result = true;
		} catch (Throwable e) {
			//            LOG.debug(""+actual + " is not a float.");
		}
		return result;
	}

	public boolean isDouble(Object c) {
		String cs = "" + c;
		boolean result = false;
		try {
			Double.parseDouble(cs);
			result = true;
		} catch (Throwable e) {
			//            LOG.debug(""+actual + " is not a float.");
		}
		return result;
	}

	public boolean isFloat(Object c) {
		String cs = "" + c;
		boolean result = false;
		try {
			Float.parseFloat(cs);
			result = true;
		} catch (Throwable e) {
			//            LOG.debug(""+actual + " is not a float.");
		}
		return result;
	}

	public boolean isLong(Object c) {
		String cs = "" + c;
		boolean result = false;
		try {
			Long.parseLong(cs);
			result = true;
		} catch (Throwable e) {
			//            LOG.debug(""+actual + " is not a float.");
		}
		return result;
	}

	public boolean isCharacter(Object c) {
		String cs = "" + c;
		boolean result = false;
		if (cs.length() == 1) {
			result = true;
		}
		return result;
	}

	public boolean isShort(Object c) {
		String cs = "" + c;
		boolean result = false;
		try {
			Short.parseShort(cs);
			result = true;
		} catch (Throwable e) {
			//            LOG.debug(""+actual + " is not a float.");
		}
		return result;
	}

	public List<Method> getDeclaredMethods(Class klass) {
		List<Method> methods = new ArrayList<>();
		for (Method m : klass.getDeclaredMethods()) {
			if (!m.getName().startsWith("$jacoco")) {
				methods.add(m);
			}
		}
		return methods;
	}

	public List<Method> getAllMethods(Class klass) {
		return getAllMethods(klass, new ArrayList<>());
	}

	public List<Method> getAllMethods(Class klass, List<Method> methods) {
		if (methods == null) {
			methods = new ArrayList<>();
		}
		for (Method m : klass.getDeclaredMethods()) {
			if (!m.getName().startsWith("$jacoco")) {
				methods.add(m);
			}
		}
		if (klass.getSuperclass() != null) {
			methods = getAllMethods(klass.getSuperclass(), methods);
		}
		return methods;
	}

	public Method findMut(Class actualClass, String theMethod, Class[] pc) throws NoSuchMethodException {
		String theClass = actualClass.getName();
		int pcl = pc == null ? 0 : pc.length;
		List<Method> methods = new ArrayList<>();
		getAllMethods(actualClass, methods);
		Method result = null;
		for (Method m : methods) {
			if (m.getName().equals(theMethod)) {
				if (m.getParameterTypes().length == pcl) {
					boolean matched = true;
					for (Class type : m.getParameterTypes()) {
						boolean found = false;
						for (Class c : pc) {
							if (type.isAssignableFrom(c)) {
								found = true;
								break;
							}
						}
						if (!found) {
							matched = false;
						}
					}
					if (matched || pcl == 0) {
						result = m;
						break;
					}
				}
			}
		}
		if (result == null) {
			throw new NoSuchMethodException("" + theMethod + " could not be found in " + theClass);
		}
		return result;
	}

	public Map<String, Field> findFields(Class theClass) {
		Map<String, Field> fields = new HashMap<>();
		findFields(theClass, fields);
		return fields;
	}

	public void findFields(Class theClass, Map<String, Field> fieldMap) {
		if (fieldMap != null) {
			for (Field f : theClass.getDeclaredFields()) {
				if (!f.getName().contains("$jacocoData") && f.getType() != theClass) {
					//need to specify a list of things to ignore, in a property file perhaps?
					//need to ignore nested objects of the same type to avoid recursion issues,
					//it may still be possible if the object is not the same type, but some object in the nested one does.
					GoateIgnore ignore = f.getAnnotation(GoateIgnore.class);
					if (ignore == null) {
						fieldMap.put(f.getName(), f);
					}
				}
			}
			if (theClass.getSuperclass() != null) {
				findFields(theClass.getSuperclass(), fieldMap);
			}
		}
	}

	public Method findMethod(Object theClass, String methodName) {
		Optional<Method> first = getAllMethods(theClass.getClass()).stream().filter(m -> m.getName().equals(methodName)).findFirst();
		return first.isPresent() ? first.get() : null;
	}

	public Class findClass(String theClass) {
		Class<?> cls = null;
		try {
			cls = Class.forName(theClass);
		} catch (ClassNotFoundException ignored) {
		}
		return cls;
	}

	public Class primitiveType(Object o) {
		Class klass = null;
		if(o instanceof String) {
			if((""+o).matches("[truefalsTRUEFALS$.,\\-+0-9]+")||(""+o).length()<2) {
				try {
					parseLong(o);
					klass = Long.class;
				} catch (Exception peLong) {
					try {
						parseInt(o);
						klass = Integer.class;
					} catch (Exception peInteger) {
						try {
							parseShort(o);
							klass = Short.class;
						} catch (Exception peShort) {
							try {
								parseFloat(o);
								klass = Float.class;
							} catch (Exception peFloat) {
								try {
									parseDouble(o);
									klass = Double.class;
								} catch (Exception peDouble) {
									try {
										parseChar(o);
										klass = Character.class;
									} catch (Exception pe) {
										try {
											parseByte(o);
											klass = Byte.class;
										} catch (Exception peByte) {
											try {
												parseBoolean(o);
												klass = Boolean.class;
											} catch (Exception peBoolean) {
												BleatBox LOG = BleatFactory.getLogger(getClass());
												LOG.debug("Goate Reflection", "I had to give up looking up your primitive type: " + o);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} else {
			klass = o.getClass();
		}
		return klass;
	}

	public BigDecimal parseBigDecimal(Object value) {
		return new BigDecimal(("" + value).replaceAll("[,$]", ""));
	}

	public byte parseByte(Object value) {
		return Byte.parseByte(("" + value).replaceAll("[,$]", ""));
	}

	public char parseChar(Object value) {
		String v = "" + value;
		if (v.length() > 1) {
			throw new ClassCastException("Problem: " + value + " is not a valid char.");
		}
		return v.charAt(0);
	}

	public double parseDouble(Object value) {
		return Double.parseDouble(("" + value).replaceAll("[,$]", ""));
	}

	public float parseFloat(Object value) {
		return Float.parseFloat(("" + value).replaceAll("[,$]", ""));
	}

	public int parseInt(Object value) {
		return Integer.parseInt(("" + value).replaceAll("[,$]", ""));
	}

	public long parseLong(Object value) {
		return Long.parseLong(("" + value).replaceAll("[,$]", ""));
	}

	public short parseShort(Object value) {
		return Short.parseShort(("" + value).replaceAll("[,$]", ""));
	}

	public boolean parseBoolean(Object value) {
		String v = "" + value;
		if(!v.equalsIgnoreCase("true")&&!v.equalsIgnoreCase("false")){
			throw new RuntimeException("Not a parseable boolean: ie, not true or false.");
		}
		return Boolean.parseBoolean(v);
	}
}
