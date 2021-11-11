package de.exxcellent.challenge;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 Tests for {@link CSVDataLoader}
 * 
 * @author Yannick Kaiser <yannick-kaiser@gmx.de>
 */
class FixedSizeTableTest {

	@BeforeAll
	static void setup() {
		System.out.println(" -- FixedSizeTable Tests --");
	}

	@Test
	void tableCreationTest() {

		System.out.println("  - Table Creation Test -");

		FixedSizeTable emptyTable = new FixedSizeTable();
		assertEquals(emptyTable.getRowCount(),0);
		assertEquals(emptyTable.getColumnCount(),0);
		
		emptyTable.addRow(new String[] {"elem1", "elem2", "elem3"});
		emptyTable.addRow(new String[] {"elem4", "elem5", "elem6"});
		assertEquals(emptyTable.getRowCount(), 2);
		assertEquals(emptyTable.getColumnCount(), 3);
		
		
		FixedSizeTable headerTable = new FixedSizeTable(new String[] {"header1", "header2"});
		assertEquals(headerTable.getRowCount(),0);
		assertEquals(headerTable.getColumnCount(),2);
		
		headerTable.addRow(new String[] {"elem1", "elem2"});
		headerTable.addRow(new String[] {"elem3", "elem4"});
		headerTable.addRow(new String[] {"elem5", "elem6"});
		assertEquals(headerTable.getRowCount(),3);
		assertEquals(headerTable.getColumnCount(),2);
	}

	@Test
	void editHeaderTest() {

		System.out.println("  - Edit Header Test -");
		
		FixedSizeTable headerTable = new FixedSizeTable(new String[] {"header1", "header2"});
		String new_header_name = "new_header1";
		String new_header_name_2 = "new_header2";
		assertDoesNotThrow(() -> headerTable.setHeader(0, new_header_name));
		assertEquals(headerTable.getColumnHeader(0), new_header_name);
		
		assertDoesNotThrow(() -> headerTable.setColumnHeaders(new String[] {new_header_name, new_header_name_2}));
		assertEquals(headerTable.getColumnHeader(0), new_header_name);
		assertEquals(headerTable.getColumnHeader(1), new_header_name_2);
	}

	@Test
	void tableAccessTest() {
		
		System.out.println("  - Table Access Test -");
		
		FixedSizeTable table = new FixedSizeTable(new String[] {"header_1", "header_2", "header_3"});
		table.addRow(new String[] {"elem1", "elem2", "elem3"});
		table.addRow(new String[] {"elem4", "elem5", "elem6"});
		
		assertDoesNotThrow(() -> table.getRow(0));
		String[] row = table.getRow(0);
		assertEquals(row[0], "elem1");
		assertEquals(row[1], "elem2");
		assertEquals(row[2], "elem3");
		
		assertDoesNotThrow(() -> table.getColumn(0));
		String[] column = table.getColumn(2);
		assertEquals(column[0], "elem3");
		assertEquals(column[1], "elem6");
		
		assertDoesNotThrow(() -> table.getColumn("header_2"));
		String[] namedColumn = table.getColumn("header_2");
		assertEquals(namedColumn[0], "elem2");
		assertEquals(namedColumn[1], "elem5");
	}
}