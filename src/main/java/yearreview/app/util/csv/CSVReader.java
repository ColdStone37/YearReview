package yearreview.app.util.csv;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A CSVReader that can create a stream of rows of a CSV directly from the file.
 * Might output another Row after the CSV-file ends if it has an empty row at the bottom.
 *
 * @author ColdStone37
 */
public abstract class CSVReader {
	/**
	 * Creates A Stream of the rows of a CSV-file.
	 * @param f file to parse
	 * @return Stream of the rows
	 * @throws FileNotFoundException if the file was not found
	 */
	public static Stream<List<String>> getRowStream(File f) throws FileNotFoundException {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new CSVIterator(f), Spliterator.ORDERED), false);
	}

	/**
	 * An {@link Iterator} for the Rows of a CSV loading the Data right from a {@link BufferedReader}.
	 */
	static class CSVIterator implements Iterator<List<String>> {
		/**
		 * Reader used to read in the CSV-file.
		 */
		private final BufferedReader br;
		/**
		 * Whether the Iterator has come to the end of the file.
		 */
		private boolean finished = false;

		/**
		 * Constructs a CSV-Iterator from a given File.
		 * @param f file to iterate
		 * @throws FileNotFoundException if the file was not found
		 */
		CSVIterator(File f) throws FileNotFoundException {
			br = new BufferedReader(new FileReader(f));
		}

		/**
		 * Tests whether the Iterator still has rows left.
		 * @return true if there are still rows to be read
		 */
		@Override
		public boolean hasNext() {
			return !finished;
		}

		/**
		 * Gets a row from the CSV-file.
		 * @return Comma-Seperated values read into a list
		 */
		@Override
		public List<String> next() {
			// Create empty list
			List<String> line = new ArrayList<>();

			int c_int;
			char c;
			boolean inString = false;
			boolean lastBackslash = false;
			StringBuilder buffer = new StringBuilder();
			try {
				// Iterator over Reader until we reach the end or we return at a line break
				while((c_int = br.read()) != -1) {
					c = (char)c_int;

					// Different behaviour for Strings
					if(inString){
						if(lastBackslash){ // Escape Characters
							switch(c){
								case '\\':
								case '\"':
								case '\'':
									buffer.append(c);
									break;
								default:
									break;
							}
							lastBackslash = false;
						} else {
							switch(c){
								case '\\':
									lastBackslash = true;
									break;
								case '"':
									inString = false;
									break;
								default:
									buffer.append(c);
									break;
							}
						}
					} else {
						// Normal behaviour outside Strings
						switch(c){
							case '\"':
								inString = true;
								break;
							case ',':
								line.add(buffer.toString());
								buffer = new StringBuilder();
								break;
							case '\n':
								return line;
							default:
								buffer.append(c);
								break;
						}
					}
				}

				// Reached end of file -> finished
				br.close();
				finished = true;
				return line;
			} catch (IOException e){
				System.out.println("IOException while reading csv-file: " + e);
				finished = true;
			}
			return null;
		}
	}
}
