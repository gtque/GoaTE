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
package com.thegoate.locate;

/**
 * Created by Eric Angeli on 3/20/2019.
 */
public class Locate {
    protected StringBuilder path = new StringBuilder();
    private String oneOrMoreNumber = "([0-9])+";
    private String zeroOrMoreNumber = "([0-9])*";

    public static Locate start(String start) {
        return path().match(start);
    }

    public static Locate path() {
        return new Locate();
    }

    public Locate wildcardIndex() {
        return anyNumberZeroOrMore();
    }

    public Locate wildcardIndex(int expectedSize) {
        return matchNTimes(zeroOrMoreNumber, expectedSize);
    }

    public Locate wildcardIndex(int expectedSize, int startingIndex) {
        return matchNTimesStartingAt(zeroOrMoreNumber, expectedSize, startingIndex);
    }

    public Locate wildcardIndexOneOrMore() {
        return anyNumberOneOrMore();
    }

    public Locate matchIndex() {
        return wildcardIndex();
    }

    /**
     * At the locate level this is mostly just syntactic sugar.
     * Extensions of Locate may implement more explicit use of matchIndex(int matchIndex)
     *
     * @param matchIndex the index to match, ignored in this implementation.
     * @return self
     */
    public Locate matchIndex(int matchIndex) {
        return wildcardIndex();
    }

    public Locate dot() {
        path.append("\\.");
        return this;
    }

    public Locate anyNumberOneOrMore() {
        path.append(oneOrMoreNumber);
        return this;
    }

    public Locate anyNumberZeroOrMore() {
        path.append(zeroOrMoreNumber);
        return this;
    }

    public Locate anyLetterOneOrMore() {
        path.append("([a-zA-Z])+");
        return this;
    }

    public Locate anyLetterZeroOrMore() {
        path.append("([a-zA-Z])*");
        return this;
    }

    public Locate match(String pattern) {
        path.append(pattern);
        return this;
    }

    public Locate matchZeroOrMore(String pattern) {
        path.append("(").append(pattern).append(")*");
        return this;
    }

    public Locate matchOneOrMore(String pattern) {
        path.append("(").append(pattern).append(")+");
        return this;
    }

    public Locate matchNTimes(String pattern, int numberOfTimesToMatch) {
        path.append("{").append(numberOfTimesToMatch).append("}");
        return this;
    }

    public Locate matchNTimesStartingAt(String pattern, int numberOfTimesToMatch, int start) {
        path.append(pattern).append("{").append(start).append(",").append(numberOfTimesToMatch).append("}");
        return this;
    }

    public Locate nTimes(int numberOfTimesToMatch) {
        path.insert(0, "((").append("){").append(numberOfTimesToMatch).append("})");
        return this;
    }

    public Locate nTimesStartingAt(int numberOfTimesToMatch, int start) {
        path.insert(0, "((").append("){").append(start).append(",").append(numberOfTimesToMatch).append("})");
        return this;
    }

    public String end() {
        return toPath();
    }

    public String end(String match) {
        path.append(match);
        return toPath();
    }

    public String toPath() {
        return path.toString();
    }

//    public Locate find(String find){
//        this.find = find;
//        return this;
//    }
//
//    public Locate in(Object source){
//        this.source = source;
//        return this;
//    }
//
//    @Override
//    public String[] detailedScrub() {
//        return new String[0];
//    }
//
//    @Override
//    protected Employee init() {
//        return this;
//    }
//
//    @Override
//    protected Object doWork() {
//        Object container = source;
//        if(container instanceof Employee){
//            container = ((Employee) container).work();
//        }
//        return new Get(find).from(container);
//    }
}
