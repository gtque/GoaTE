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

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Eric Angeli on 8/10/2018.
 */
public class CSVParser {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    org.apache.commons.csv.CSVParser csvParser;
    List<CSVRecord> records = null;
    Map<String, Integer> map = null;
    /**
     * Creates a parser for the given {@link File}.
     *
     * @param file    a CSV file. Must not be null.
     * @param charset A Charset
     * @param format  the CSVFormat used for CSV parsing. Must not be null.
     * @return a new parser
     * @throws IllegalArgumentException If the parameters of the format are inconsistent or if either file or format are null.
     * @throws IOException              If an I/O error occurs
     */
    @SuppressWarnings("resource")
    public static CSVParser parse(final File file, final Charset charset, final CSVFormat format) throws IOException {
        return new CSVParser(org.apache.commons.csv.CSVParser.parse(file, charset, format));
    }

    /**
     * Creates a CSV parser using the given {@link CSVFormat}.
     * <p>
     * If you do not read all records from the given {@code reader}, you should call {@link #close()} on the parser,
     * unless you close the {@code reader}.
     * </p>
     *
     * @param inputStream an InputStream containing CSV-formatted input. Must not be null.
     * @param charset     a Charset.
     * @param format      the CSVFormat used for CSV parsing. Must not be null.
     * @return a new CSVParser configured with the given reader and format.
     * @throws IllegalArgumentException If the parameters of the format are inconsistent or if either reader or format are null.
     * @throws IOException              If there is a problem reading the header or skipping the first record
     * @since 1.5
     */
    @SuppressWarnings("resource")
    public static CSVParser parse(final InputStream inputStream, final Charset charset, final CSVFormat format)
            throws IOException {
        return new CSVParser(org.apache.commons.csv.CSVParser.parse(new InputStreamReader(inputStream, charset), format));
    }

    /**
     * Creates a parser for the given {@link Path}.
     *
     * @param path    a CSV file. Must not be null.
     * @param charset A Charset
     * @param format  the CSVFormat used for CSV parsing. Must not be null.
     * @return a new parser
     * @throws IllegalArgumentException If the parameters of the format are inconsistent or if either file or format are null.
     * @throws IOException              If an I/O error occurs
     * @since 1.5
     */
    public static CSVParser parse(final Path path, final Charset charset, final CSVFormat format) throws IOException {
        return new CSVParser(org.apache.commons.csv.CSVParser.parse(Files.newBufferedReader(path, charset), format));
    }

    /**
     * Creates a CSV parser using the given {@link CSVFormat}
     * <p>
     * If you do not read all records from the given {@code reader}, you should call {@link #close()} on the parser,
     * unless you close the {@code reader}.
     * </p>
     *
     * @param reader a Reader containing CSV-formatted input. Must not be null.
     * @param format the CSVFormat used for CSV parsing. Must not be null.
     * @return a new CSVParser configured with the given reader and format.
     * @throws IllegalArgumentException If the parameters of the format are inconsistent or if either reader or format are null.
     * @throws IOException              If there is a problem reading the header or skipping the first record
     * @since 1.5
     */
    public static CSVParser parse(Reader reader, final CSVFormat format) throws IOException {
        return new CSVParser(new org.apache.commons.csv.CSVParser(reader, format));
    }

    /**
     * Creates a parser for the given {@link String}.
     *
     * @param string a CSV string. Must not be null.
     * @param format the CSVFormat used for CSV parsing. Must not be null.
     * @return a new parser
     * @throws IllegalArgumentException If the parameters of the format are inconsistent or if either string or format are null.
     * @throws IOException              If an I/O error occurs
     */
    public static CSVParser parse(final String string, final CSVFormat format) throws IOException {
        return new CSVParser(new org.apache.commons.csv.CSVParser(new StringReader(string), format));
    }

    /**
     * Creates a parser for the given URL.
     * <p>
     * If you do not read all records from the given {@code url}, you should call {@link #close()} on the parser, unless
     * you close the {@code url}.
     * </p>
     *
     * @param url     a URL. Must not be null.
     * @param charset the charset for the resource. Must not be null.
     * @param format  the CSVFormat used for CSV parsing. Must not be null.
     * @return a new parser
     * @throws IllegalArgumentException If the parameters of the format are inconsistent or if either url, charset or format are null.
     * @throws IOException              If an I/O error occurs
     */
    public static CSVParser parse(final URL url, final Charset charset, final CSVFormat format) throws IOException {
        return new CSVParser(new org.apache.commons.csv.CSVParser(new InputStreamReader(url.openStream(), charset), format));
    }

    public CSVParser(org.apache.commons.csv.CSVParser csvParser) {
        this.csvParser = csvParser;
    }

    /**
     * Customized CSV parser using the given {@link CSVFormat}
     * <p>
     * If you do not read all records from the given {@code reader}, you should call {@link #close()} on the parser,
     * unless you close the {@code reader}.
     * </p>
     *
     * @param reader a Reader containing CSV-formatted input. Must not be null.
     * @param format the CSVFormat used for CSV parsing. Must not be null.
     * @throws IllegalArgumentException If the parameters of the format are inconsistent or if either reader or format are null.
     * @throws IOException              If there is a problem reading the header or skipping the first record
     */
    public CSVParser(final Reader reader, final CSVFormat format) throws IOException {
        this(reader, format, 0, 1);
    }

    /**
     * Customized CSV parser using the given {@link CSVFormat}
     * <p>
     * If you do not read all records from the given {@code reader}, you should call {@link #close()} on the parser,
     * unless you close the {@code reader}.
     * </p>
     *
     * @param reader          a Reader containing CSV-formatted input. Must not be null.
     * @param format          the CSVFormat used for CSV parsing. Must not be null.
     * @param characterOffset Lexer offset when the parser does not start parsing at the beginning of the source.
     * @param recordNumber    The next record number to assign
     * @throws IllegalArgumentException If the parameters of the format are inconsistent or if either reader or format are null.
     * @throws IOException              If there is a problem reading the header or skipping the first record
     * @since 1.1
     */
    @SuppressWarnings("resource")
    public CSVParser(final Reader reader, final CSVFormat format, final long characterOffset, final long recordNumber)
            throws IOException {
        csvParser = new org.apache.commons.csv.CSVParser(reader, format, characterOffset, recordNumber);
    }

    /**
     * Closes resources.
     *
     * @throws IOException If an I/O error occurs
     */
    public void close() throws IOException {
        csvParser.close();
    }

    /**
     * Returns the current line number in the input stream.
     * <p>
     * <strong>ATTENTION:</strong> If your CSV input has multi-line values, the returned number does not correspond to
     * the record number.
     * </p>
     *
     * @return current line number
     */
    public long getCurrentLineNumber() {
        return csvParser.getCurrentLineNumber();
    }

    /**
     * Gets the first end-of-line string encountered.
     *
     * @return the first end-of-line string
     * @since 1.5
     */
    public String getFirstEndOfLine() {
        return csvParser.getFirstEndOfLine();
    }

    /**
     * Returns a copy of the header map that iterates in column order.
     * <p>
     * The map keys are column names. The map values are 0-based indices.
     * </p>
     *
     * @return a copy of the header map that iterates in column order.
     */
    public Map<String, Integer> getHeaderMap() {
        if (map == null) {
            map = csvParser.getHeaderMap();
            if (map == null) {
                try {
                    CSVRecord record = getRecords().get(0);
                    map = new ConcurrentHashMap<>();
                    for (int i = 0; i < record.size(); i++) {
                        map.put(record.get(i), i);
                    }
                } catch (IOException e) {
                    LOG.debug("CSV Parse", "Problem getting header map: " + e.getMessage(), e);
                }
            }
        }
        return map;
    }

    /**
     * Returns the current record number in the input stream.
     * <p>
     * <strong>ATTENTION:</strong> If your CSV input has multi-line values, the returned number does not correspond to
     * the line number.
     * </p>
     *
     * @return current record number
     */
    public long getRecordNumber() {
        return csvParser.getRecordNumber();
    }

    /**
     * Parses the CSV input according to the given format and returns the content as a list of
     * {@link CSVRecord CSVRecords}.
     * <p>
     * The returned content starts at the current parse-position in the stream.
     * </p>
     *
     * @return list of {@link CSVRecord CSVRecords}, may be empty
     * @throws IOException on parse error or input read-failure
     */
    public List<CSVRecord> getRecords() throws IOException {
        if (records == null) {
            records = csvParser.getRecords();
        }
        return records;
    }

    /**
     * Gets whether this parser is closed.
     *
     * @return whether this parser is closed.
     */
    public boolean isClosed() {
        return csvParser.isClosed();
    }

    /**
     * Returns an iterator on the records.
     * <p>
     * An {@link IOException} caught during the iteration are re-thrown as an
     * {@link IllegalStateException}.
     * </p>
     */
    public Iterator<CSVRecord> iterator() {
        return csvParser.iterator();
    }
}
