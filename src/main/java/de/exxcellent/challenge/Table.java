package de.exxcellent.challenge;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/** Data class representing a 2D table of data, optionally with column names.
 * 
 * TODO
 * 
 * @author Yannick Kaiser <yannick-kaiser@gmx.de>
 */
public class Table {
	
	private Optional<String[]> columnHeaders = Optional.empty();
	private ArrayList<String[]> rows = new ArrayList<>();
	
	/**Creates a new empty table ready to be populated.
	 * TODO
	 *  
	 */
	public Table() {
		
	}
	
	/**TODO
	 * 
	 * @param columnHeaders
	 */
	public Table(String[] columnHeaders) {
		this.setColumnHeaders(columnHeaders);
	}
	
	/**
	 * @return the number of rows living in this Table, not counting column headers
	 */
	public int getRowCount() {
		return this.rows.size();
	}
	
	/* Returns the number of columns in this table. A return value of 0 indicates that this table is empty.
	 * 
	 * @return the number of columns in this Table instance
	 */
	public int getColumnCount() {
		if (this.hasColumnHeaders()) {
			return this.columnHeaders.get().length;
		}
		
		// no header present, maybe rows are present
		if (this.rows.size() == 0) return 0;
		
		// else the first row indicates the shape of the table
		return this.rows.get(0).length;
	}
	
	public void setColumnHeaders(String[] columnHeaders) {
		this.columnHeaders = Optional.of(columnHeaders);
	}
	
	public boolean hasColumnHeaders() {
		return !this.columnHeaders.isEmpty();
	}
	
	/**Returns the header/title of the specified column.<br>
	 * The columns are indexed starting from 0.
	 * 
	 * @param columnIndex of the requested column
	 * @return the header of the column
	 * 
	 * @throws {@link NoSuchElementException}, {@link IndexOutOfBoundsException}
	 */
	public String getColumnHeader(int columnIndex) {
		return this.columnHeaders.get()[columnIndex];
	}
	
	/**Adds the row as-is, without validation checks.
	 * 
	 * @param row <code>String[]</code> array containing row entries
	 */
	public void addRow(String[] row) {
		this.rows.add(row);
	}
	
	/**Retrieve a single row from this table (Random Access). <br>
	 * This operation returns in constant time O(1).
	 * 
	 * @param rowIndex
	 * @return a single row of this {@link Table}
	 * 
	 * @throws {@link IndexOutOfBoundsException} if the index is out of range of 0 <= index < row count
	 */
	public String[] getRow(int rowIndex) {
		return this.rows.get(rowIndex);
	}
	
	/**Does not include the header of this column, such that the height 
	 * of the returned <code>String[]</code> is equal to {@link #getRowCount()} <br>
	 * The columns are indexed starting from 0.<br><br>
	 * 
	 * This method builds the column on request in O(n)
	 * 
	 * @param columnIndex of the requested column
	 * @return a single column of this {@link Table}
	 * 
	 * @throws {@link IndexOutOfBoundsException}
	 */
	public String[] getColumn(int columnIndex) {
		String[] column = new String[this.getRowCount()];
		
		for (int i = 0; i < this.getRowCount(); ++i) {
			column[i] = this.rows.get(i)[columnIndex];
		}
		
		return column;
	}
	
	/**Retrieve a single row from this table (Random Access). <br>
	 * This operation returns in constant time O(1).
	 * 
	 * @param rowIndex starting at 0
	 * @param columnIndex starting at 0
	 * @return a single row of this {@link Table}
	 * 
	 * @throws {@link IndexOutOfBoundsException} if the index is out of range of either the number of rows or columns
	 */
	public String getField(int rowIndex, int columnIndex) {
		return this.rows.get(rowIndex)[columnIndex];
	}
	
	@Override
	public String toString() {
		StringBuilder repr = new StringBuilder();
		if (this.hasColumnHeaders()) {
			Stream.of(this.columnHeaders.get()).forEach(header -> repr.append(header));
		}
		for (String[] row : this.rows) {
			repr.append("\n");
			Stream.of(row).forEach(header -> repr.append(header));
		}
		return repr.toString();
	}
}
