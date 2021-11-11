package de.exxcellent.challenge;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/** Interface representing the behavior of an one-use data loader object that will load and retrieve 
 * database-like data as a stream of <code>String[]</code> row instances. <br><br>
 * 
 * This loader must implement behaviors dealing with the existance of header lines in the accessed data,
 * and allow access to parsed header lines with {@link #getHeaderLine()}.
 * 
 * The default behavior in regards to header lines is up to the implementation.
 * 
 * @author Yannick Kaiser <yannick-kaiser@gmx.de>
 */
public interface TableDataLoader {
	/**Loads the data from the underlying source and provides it as a Stream of rows. This is expected to be a one-use method, 
	 * unless the implementation explicitly states otherwise.
	 * 
	 * @return a Stream providing <code>String[]</code> instances representing rows of data in a table
	 * 
	 * @exception IOException
	 */
	public Stream<String[]> streamOfRows() throws IOException; // TODO rethink naming
	
	/**<code>getHeaderLine</code> returns the header line if <br>
	 * - The concrete <code>TableDataLoader</code> implementation supports it
	 * - The concrete <code>TableDataLoader</code> implementation is setup to expect a header line
	 * - The underlying data source was not empty
	 * 
	 * @return the <code>String[]</code> header line of column names, if it exists. 
	 */
	public Optional<String[]> getHeaderLine();
}
