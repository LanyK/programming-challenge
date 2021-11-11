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
class CSVDataLoaderTest {

	private static Path emptyCSV = Path.of("src/test/resources/de/exxellent/challenge/empty.csv");
	private static Path validCSV = Path.of("src/test/resources/de/exxellent/challenge/valid.csv");
	private static Path headerlessCSV = Path.of("src/test/resources/de/exxellent/challenge/headerless.csv");

	@BeforeAll
	static void setup() {
		System.out.println(" -- CSVFileLoader Tests --");
	}

	@Test
	void emptyFileTest() {

		System.out.println("  - Empty CSV Tests -");

		// header expected, but empty on streamOfRows
		CSVDataLoader testSubject1 = new CSVDataLoader(emptyCSV, null, true);
		Exception e1 = assertThrows(IOException.class, () -> testSubject1.streamOfRows().collect(Collectors.toList()));
		assertEquals("CSVDataLoader tried to read the header line, but the file is empty", e1.getMessage());

		// empty on getHeaderLine
		CSVDataLoader testSubject2 = new CSVDataLoader(emptyCSV, null, true);
		Exception e2 = assertThrows(IOException.class, () -> testSubject2.getHeaderLine());
		assertEquals("CSVDataLoader tried to read the header line, but the file is empty", e2.getMessage());

	}

	@Test
	void badPathTest() {

		System.out.println("  - Bad Path Test -");

		CSVDataLoader testSubject = new CSVDataLoader(Path.of(""), null, true);
		Exception e = assertThrows(UncheckedIOException.class, () -> testSubject.streamOfRows().collect(Collectors.toList()));
	}

	/**
	 * Tests the CSVDataLoader on valid files with headers.
	 */
	@Test
	void validDataTest() {

		System.out.println("  - Valid File Test -");

		// valid file, 2 rows x 3 columns with header
		CSVDataLoader testSubject = new CSVDataLoader(validCSV, null, true);

		assertDoesNotThrow(() -> assertEquals(testSubject.streamOfRows().count(), 2));

		try {
			List<String[]> rows = testSubject.streamOfRows().collect(Collectors.toList());
			assertEquals(rows.get(0)[0], "Arsenal");
			assertEquals(rows.get(0).length, 3);
			assertEquals(rows.get(1)[2], "24");
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertDoesNotThrow(() -> testSubject.getHeaderLine());

		try {
			var header = testSubject.getHeaderLine();
			assertEquals(header.get()[0], "Team");
			assertEquals(header.get()[2], "Wins");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tests the CSVDataLoader on valid, but headerless files.
	 */
	@Test
	void headerlessDataTest() {

		System.out.println("  - Headerless File Test -");

		// valid file, 2 rows x 2 columns, no header
		CSVDataLoader testSubject = new CSVDataLoader(headerlessCSV, null);

		assertDoesNotThrow(() -> testSubject.getHeaderLine());

		try {
			assertEquals(Optional.empty(), testSubject.getHeaderLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}