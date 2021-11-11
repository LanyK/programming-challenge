package de.exxcellent.challenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

/** {@link TableDataLoader} implementation which loads from a <code>.csv</code> file.<br>
 *  It assumes a default delimiter of <code>","</code>, which can also be set in the full constructor.<br><br>
 *  
 *  The constructor is lazy: No <code>I/O</code> actions happen until either {@link #getHeaderLine()} or {@link #streamRows()} 
 *  has been called for the first time.<br><br>
 *  
 *  It is suggested to use <code>getHeaderLine</code> after <code>streamRows</code> to cut one file access from the process.
 * 
 * @author Yannick Kaiser <yannick-kaiser@gmx.de>
 */
public class CSVDataLoader implements TableDataLoader {

	public final static String defaultDelimiter = ",";
	
	private Optional<String[]> headerCache = Optional.empty();
	private String delimiter = defaultDelimiter;
	private Path path;
	private boolean expectsHeaderLine = false;
	
	/**Create a new <code>CSVDataLoader</code> with a specified delimiter and headerLine setting.<br><br>
	 * Access the rows of data with {@link #streamRows()} and retrieve the header line content with {@link #getHeaderLine()}.<br>
	 * It is recommended to call {@link #getHeaderLine()} last to minimize <code>I/O</code> calls.
	 * 
	 * @param csvFilePath {@link Path} object pointing to the CSV file.
	 * @param delimiter defaults to ","
	 * @param headerLine specifies whether this loader will expect a header line of column names in the CSV file
	 */
	public CSVDataLoader(Path csvFilePath, String delimiter, boolean headerLine) {
		
		if (delimiter != null && !delimiter.strip().isEmpty()) {
			this.delimiter = delimiter; // keep default otherwise
		}
		
		this.path = csvFilePath;
		this.expectsHeaderLine = headerLine;
	}
	
	/**Create a new <code>CSVDataLoader</code> with a specified delimiter and headerLine setting.<br><br>
	 * Access the rows of data with {@link #streamRows()} and retrieve the header line content with {@link #getHeaderLine()}.<br>
	 * It is recommended to call {@link #getHeaderLine()} last to minimize <code>I/O</code> calls.<br><br>
	 *
	 * Equivalent to a call of <code>CSVDataLoader(csvFilePath, delimiter, false)</code>
	 * 
	 * @param csvFilePath {@link Path} object pointing to the CSV file.
	 * @param delimiter defaults to ","
	 */
	public CSVDataLoader(Path csvFilePath, String delimiter) {
		this(csvFilePath, delimiter, false);
	}
	
	/**Retrieves the rows of data in the CSV file as a <code>Stream<[String]></code>.
	 * If the file has a header line, it is skipped and can be accessed with {@link #getHeaderLine()} instead.
	 * 
	 * @return a stream containing the rows of the underlying CSV file
	 * @throws IOException if the underlying file can't be read, or if the file is empty when a header is expected
	 */
	@Override
	public Stream<String[]> streamRows() throws IOException {
					
		// retrieve the first line separately on-the-fly when this method is called if applicable
		if (this.expectsHeaderLine && this.headerCache.isEmpty()) {
			
			this.getHeaderLine(); // retrieves the header line into cache. It would be more elegant to access the first
			// element of the filestream non-destructively, but this is not currently supported by Stream. Converting to
			// an Iterator is not an option either, since it removes Stream's ability to auto-close the underlying
			// resource.
		}
		
		return Files.lines(this.path).skip(this.expectsHeaderLine ? 1 : 0).map(line -> line.strip().split(this.delimiter));
	}

	/** 
	 * Retrieve the header line of column names from the underlying data source.<br>
	 * This method only performs an <code>I/O</code> operation if this instance hasn't either<br>
	 * - handed out a Stream via {@link #streamRows()}<br>
	 * - or invoked this method before.<br>
	 * Otherwise, the header line is already cached.<br><br>
	 * 
	 * If this <code>CSVDataLoader</code> has been set to not expect a header line, this method will always
	 * return an empty <code>Optional</code>
	 * 
	 * @return an Optional containing a <code>String[]</code> with the contents of the header line
	 * @throws IOException if the underlying file can't be read or the file is empty 
	 *  
	 */
	@Override
	public Optional<String[]> getHeaderLine() throws IOException {
		
		// If this TableDataLoader does not expect a header file, return an empty Optional
		if (!this.expectsHeaderLine) {
			return Optional.empty();
		}
		
		// The header has already been loaded successfully before
		if (this.headerCache.isPresent()) {
			return this.headerCache;
		}
		
		// This loader expects a header line, but has not loaded one before. 
		// This is most likely because there has not been any I/O access before.
		// Peek at the file.
		try (Stream<String> fileStream = Files.lines(this.path)) {
			Optional<String> line = fileStream.findFirst();
			
			if (line.isEmpty()) throw new IOException("CSVDataLoader tried to read the header line, but the file is empty");
			else {
				this.headerCache = Optional.of(line.get().strip().split(this.delimiter));
				return this.headerCache;
			}
		}
	}
}
