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
package com.thegoate.dsl;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;

import java.util.Map;
import java.util.TreeMap;

/**
 * Simple app for reading the dictionary.
 * Useful when IDE support is not available.
 * Created by Eric Angeli on 2/19/2018.
 */
public class GoateDictionary {

    public static void main(String[] args){
        Map<String, Class> dictionary = new TreeMap<>(new Interpreter(new Goate()).getDictionary());
        StringBuilder book = new StringBuilder("");
        for(Map.Entry<String,Class> entry:dictionary.entrySet()){
            GoateDescription description = (GoateDescription)entry.getValue().getAnnotation(GoateDescription.class);
            book.append(entry.getKey()).append(":: ");
            if(description!=null){
                book.append(description.description());
                if(description.parameters()!=null&&description.parameters().length>0){
                    for(int index = 0; index<description.parameters().length; index++){
                        book.append("\n\tparameter "+(index+1)+" : "+description.parameters()[index]);
                    }
                } else {
                    book.append("\n\tno parameters");
                }
            } else {
                book.append("no definition found.");
            }
            book.append("\n location: ").append(entry.getValue().getCanonicalName()).append("\n");
        }
        System.out.print(book.toString());
    }
}
