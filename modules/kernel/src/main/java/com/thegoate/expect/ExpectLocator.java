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
package com.thegoate.expect;

import com.thegoate.locate.Locate;

/**
 * Created by Eric Angeli on 7/12/2019.
 */
public class ExpectLocator extends Locate {

    public static Locate start(String start) {
        return path().match(start);
    }

    public static Locate path() {
        return new ExpectLocator();
    }

    public Locate wildcardIndex(){
        path.append(ExpectWildcardIndexPath.wildcardIndex());
        return this;
    }

    public Locate wildcardIndex(int expectedSize){
        path.append(ExpectWildcardIndexPath.wildcardIndex(expectedSize));
        return this;
    }

    public Locate wildcardIndex(int expectedSize, int startingIndex){
        path.append(ExpectWildcardIndexPath.wildcardIndex());
        return this;
    }

    public Locate wildcardIndexOneOrMore(){
        path.append(ExpectWildcardIndexOneOrMorePath.wildcardIndexOneOrMore());
        return this;
    }

    public Locate matchIndex(){
        path.append(ExpectMatchWildcardIndexPath.matchWildcardIndex());
        return this;
    }

    public Locate matchIndex(int matchIndex){
        path.append(ExpectMatchWildcardIndexPath.matchWildcardIndex(matchIndex));
        return this;
    }

    /**
     * This overrides the inherited dot method and does not include the forward slash
     * as expect locators do not use actual regex.
     * @return self
     */
    @Override
    public Locate dot(){
        path.append(".");
        return this;
    }
}
