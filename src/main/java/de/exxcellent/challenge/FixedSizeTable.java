package de.exxcellent.challenge;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Data class representing a fixed size 2D table of data, optionally with column names.<br> 
 * The column count of the table is fixed either by providing column header names at
 * creation time,<br>
 * or in the absence of column headers taken from the first row which is added
 * to the table.<br><br>
 * 
 * This table implementation uses an underlying row-wise list of String arrays,<br>
 * making row lookup and field lookup O(1) and column lookup O(n)
 * 
 * @author Yannick Kaiser <yannick-kaiser@gmx.de>
 */
public class FixedSizeTable {

	private Optional<String[]> columnHeaders = Optional.empty();
	private ArrayList<String[]> rows = new ArrayList<>();

	/**Create a new empty table ready to be populated.
	 * 
	 * While the table is empty, the first added row can be of arbitrary dimensions<br>
	 * and will fix the width of the Table from then on.<br><br>
	 * 
	 * If however column headers are set before any rows are added, the number of headers will<br>
	 * fix the amount of columns in this fixed size table.
	 */
	public FixedSizeTable() {}

	/**
	 * Creates a new empty table with preset column headers.<br>
	 * This also fixes the column count in place; any rows entered must have the same amount of columns.<br>
	 * Access the width of this table at any point with {@link #getColumnCount()}.
	 * 
	 * @param columnHeaders <code>String[]</code> containing the column names for this Table
	 */
	public FixedSizeTable(String[] columnHeaders) {
		this.setColumnHeaders(columnHeaders);
	}
	
	/**
	 * @return the number of rows living in this Table, not counting column headers
	 */
	public int getRowCount() {
		return this.rows.size();
	}

	/*
	 * Returns the number of columns in this table. A return value of 0 indicates
	 * that this table is empty.
	 * 
	 * @return the number of columns in this Table instance
	 */
	public int getColumnCount() {
		if (this.hasColumnHeaders()) {
			return this.columnHeaders.get().length;
		}

		// no header present, maybe rows are present
		if (this.rows.size() == 0)
			return 0;

		// else the first row indicates the shape of the table
		return this.rows.get(0).length;
	}

	/**Changes a single column header to the specified value.<br>
	 * To change a header, the Table must already have headers set via {@link #setColumnHeaders(String[])},<br>
	 * otherwise a <code>NoSuchElementException</code> will be thrown.
	 * 
	 * @param columnIndex
	 * @param columnHeader
	 * 
	 * @throws {@link NoSuchElementException}, {@link IndexOutOfBoundsException}
	 */
	public void setHeader(int columnIndex, String columnHeader) {
		if (!this.hasColumnHeaders()) {
			throw new NoSuchElementException("Can't set a specific column header when no column headers are set. Set in bulk."); // TODO this is very unfriendly design and should gracefully set a single header
		}
		
		this.columnHeaders.get()[columnIndex] = columnHeader;
	}
	
	/**Arbitrary column headers can only be set when the Table holds no rows.<br>
	 * In any other case, the fixed column count can't be altered.<br>
	 * Use {@link #setHeader(int, String)} to alter a single column's header.
	 * 
	 * @param columnHeaders
	 * @throws IndexOutOfBoundsException if the column count of the new array mismatches the size of the table
	 */
	public void setColumnHeaders(String[] columnHeaders) {
		
		// always allowed when the table is empty, otherwise needs to adhere to the dimensionality of this Table
		if (this.getRowCount() == 0 || columnHeaders.length == this.getColumnCount()) {
			this.columnHeaders = Optional.of(columnHeaders);
			return;
		}
		
		// otherwise illegal	
		throw new IndexOutOfBoundsException("New column header count mismatches existing fixed column count");
	}

	/**
	 * @return true if this Table has column headers set
	 */
	public boolean hasColumnHeaders() {
		return !this.columnHeaders.isEmpty();
	}

	/**
	 * Returns the header/title of the specified column.<br>
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

	/**
	 * Adds the row to the table if possible.<br>
	 * <br>
	 * 
	 * If the table has column headers set, the amount of elements<br>
	 * in the row must match the amount of columns set by the column headers.<br>
	 * <br>
	 * 
	 * If the table has no column headers, the row at index 0 determines the width
	 * of the table.
	 * 
	 * @param row <code>String[]</code> array containing row entries
	 */
	public void addRow(String[] row) throws IllegalArgumentException {
		
		if (this.getRowCount() == 0 && !this.hasColumnHeaders()) {
			// totally empty table -> allow anyway
			this.rows.add(row);
			return;
		}
		
		// either has rows, headers, or both -> fixed size state
		if (this.getColumnCount() != row.length)
			throw new IllegalArgumentException(
					"Tried to enter a row of diverging column count into a fixed size table.");
		
		this.rows.add(row);
	}

	/**
	 * Retrieve a single row from this table (Random Access). <br>
	 * This operation returns in constant time O(1).
	 * 
	 * @param rowIndex
	 * @return a single row of this {@link FixedSizeTable}
	 * 
	 * @throws {@link IndexOutOfBoundsException} if the index is out of range of 0
	 *                <= index < row count
	 */
	public String[] getRow(int rowIndex) {
		return this.rows.get(rowIndex);
	}

	/**
	 * Does not include the header of this column, such that the height of the
	 * returned <code>String[]</code> is equal to {@link #getRowCount()} <br>
	 * The columns are indexed starting from 0.<br>
	 * <br>
	 * 
	 * This method builds the column on request in O(n)
	 * 
	 * @param columnIndex of the requested column
	 * @return a single column of this {@link FixedSizeTable}
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

	/**
	 * Retrieve a single row from this table (Random Access). <br>
	 * This operation returns in constant time O(1).
	 * 
	 * @param rowIndex    starting at 0
	 * @param columnIndex starting at 0
	 * @return a single row of this {@link FixedSizeTable}
	 * 
	 * @throws {@link IndexOutOfBoundsException} if the index is out of range of
	 *                either the number of rows or columns
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
