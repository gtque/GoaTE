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
package com.thegoate.spreadsheets.csv;

import org.apache.commons.csv.CSVFormat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Eric Angeli on 8/22/2018.
 */
public class CSVRecord {
    CSVParser parser;
    org.apache.commons.csv.CSVRecord record;

    public CSVRecord(CSVParser parser, org.apache.commons.csv.CSVRecord record) {
        this.parser = parser;
        this.record = record;
    }

    /**
     * Returns a value by {@link Enum}.
     *
     * @param e an enum
     * @return the String at the given enum String
     */
    public String get(final Enum<?> e) {
        return get(e.toString());
    }

    /**
     * Returns a value by index.
     *
     * @param i a column index (0-based)
     * @return the String at the given index
     */
    public String get(final int i) {
        return record.get(i);
    }

    /**
     * Returns a value by name.
     *
     * @param name the name of the column to be retrieved.
     * @return the column value, maybe null depending on {@link CSVFormat#getNullString()}.
     * @throws IllegalStateException    if no header mapping was provided
     * @throws IllegalArgumentException if {@code name} is not mapped or if the record is inconsistent
     * @see #isConsistent()
     * @see CSVFormat#withNullString(String)
     */
    public String get(final String name) {
        return get(findColumnIndex(name));
    }

    private int findColumnIndex(String name){
        return parser.getHeaderMap().get(name);
    }

    /**
     * Returns the start position of this record as a character position in the source stream. This may or may not
     * correspond to the byte position depending on the character set.
     *
     * @return the position of this record in the source stream.
     */
    public long getCharacterPosition() {
        return record.getCharacterPosition();
    }

    /**
     * Returns the comment for this record, if any.
     * Note that comments are attached to the following record.
     * If there is no following record (i.e. the comment is at EOF)
     * the comment will be ignored.
     *
     * @return the comment for this record, or null if no comment for this record is available.
     */
    public String getComment() {
        return record.getComment();
    }

    /**
     * Returns the number of this record in the parsed CSV file.
     * <p>
     * <p>
     * <strong>ATTENTION:</strong> If your CSV input has multi-line values, the returned number does not correspond to
     * the current line number of the parser that created this record.
     * </p>
     *
     * @return the number of this record.
     * @see org.apache.commons.csv.CSVParser#getCurrentLineNumber()
     */
    public long getRecordNumber() {
        return record.getRecordNumber();
    }

    /**
     * Tells whether the record size matches the header size.
     * <p>
     * <p>
     * Returns true if the sizes for this record match and false if not. Some programs can export files that fail this
     * test but still produce parsable files.
     * </p>
     *
     * @return true of this record is valid, false if not
     */
    public boolean isConsistent() {
        return parser.getHeaderMap() == null || parser.getHeaderMap().size() == record.size();
    }

    /**
     * Checks whether this record has a comment, false otherwise.
     * Note that comments are attached to the following record.
     * If there is no following record (i.e. the comment is at EOF)
     * the comment will be ignored.
     *
     * @return true if this record has a comment, false otherwise
     * @since 1.3
     */
    public boolean hasComment() {
        return record.hasComment();
    }

    /**
     * Checks whether a given column is mapped, i.e. its name has been defined to the parser.
     *
     * @param name the name of the column to be retrieved.
     * @return whether a given column is mapped.
     */
    public boolean isMapped(final String name) {
        return parser.getHeaderMap() != null && parser.getHeaderMap().containsKey(name);
    }

    /**
     * Checks whether a given columns is mapped and has a value.
     *
     * @param name the name of the column to be retrieved.
     * @return whether a given columns is mapped and has a value
     */
    public boolean isSet(final String name) {
        return isMapped(name) && parser.getHeaderMap().get(name).intValue() < record.size();
    }

    /**
     * Returns an iterator over the values of this record.
     *
     * @return an iterator over the values of this record.
     */
    public Iterator<String> iterator() {
        return record.iterator();
    }

    /**
     * Returns the number of values in this record.
     *
     * @return the number of values.
     */
    public int size() {
        return record.size();
    }

    /**
     * Copies this record into a new Map. The new map is not connect
     *
     * @return A new Map. The map is empty if the record has no headers.
     */
    public Map<String, String> toMap() {
        return record.toMap();
    }

    /**
     * Returns a string representation of the contents of this record. The result is constructed by comment, mapping,
     * recordNumber and by passing the internal values array to {@link Arrays#toString(Object[])}.
     *
     * @return a String representation of this record.
     */
    @Override
    public String toString() {
        return record.toString();
    }
}
