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
package com.thegoate.info;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.annotations.GoateDescription;

/**
 * Simple app for reading the dictionary.
 * Useful when IDE support is not available.
 * Created by Eric Angeli on 2/19/2018.
 */
public class GoateEncyclopedia {
    private Goate encyclopedia = null;

    /**
     * Builds the index.
     * Looks for anything annotated with Info and adds it to the index.
     * The last instance of a word wins, ie there will only be one version of a word in the dictionary
     * which may produce unexpected results if more than one uses the same word.
     * @return the compiled encyclopedia describing all of Goate.
     */
    public GoateEncyclopedia buildIndex() {
        try {
            if (encyclopedia == null) {
                new AnnotationFactory().doDefault().annotatedWith(Info.class).buildDirectory();
                encyclopedia = new Goate();
            }
            AnnotationFactory.directory.get(Info.class.getCanonicalName()).forEach((key, value) -> {
                Goate index = new Goate();
                index.put("_class", value);
                Info info = (Info)value.getAnnotation(Info.class);
                GoateDescription description = (GoateDescription)value.getAnnotation(GoateDescription.class);
                String desc = "NOT SET, PLEASE ADD @GoateDescription";
                if(description!=null){
                    desc = description.description();
                }
                index.put("_description", desc);
                index.put("_classifier", info.classifier());
                index.put("_tags", info.tags());
                index.put("_tags_string", tags(info.tags()));
                index.put("_sub_topics", new Goate());
                encyclopedia.put(key, index);
            });
        } catch (Throwable e) {
            System.out.println("Problem initializing encyclopedia: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    private String tags(String[] tags){
        StringBuilder tag_string = new StringBuilder("{");
        boolean first = true;
        for(String tag:tags){
            if(!first){
                tag_string.append(", ");
            }
            tag_string.append(tag);
            first = false;
        }
        return tag_string.append("}").toString();
    }

    public Goate getEncyclopedia(){
        return this.encyclopedia;
    }

    public static void main(String[] args){
        //Map<String, Class> dictionary = new TreeMap<>(new Interpreter(new Goate()).getDictionary());
        GoateEncyclopedia ged = new GoateEncyclopedia();
        ged.buildIndex();
        StringBuilder book = new StringBuilder("");
        Goate encyclopedia = ged.getEncyclopedia();
        for(String key:encyclopedia.keys()){
            Goate volume = encyclopedia.get(key, null, Goate.class);
            if(volume!=null){
                book.append(volume.get("_class",String.class, Class.class).getSimpleName())
                        .append(": \n")
                        .append("\tdescription: ").append(volume.get("_description","UNDEFINED", String.class)).append("\n")
                        .append("\trelated topics: ").append(volume.get("_tags_string","{}", String.class)).append("\n")
                        .append("\tmethod of look up: ").append(volume.get("_classifier","", String.class)).append("\n")
                        .append("\t").append(key).append("\n");
                //need to process sub_topics here, they should be looked up and built, probably similar to dictionary.
            }
        }
        System.out.print(book.toString());
    }
}
