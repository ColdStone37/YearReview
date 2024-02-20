package yearreview.app.util.csv;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class CSVReader {
	public static Stream<List<String>> getLineStream(File f) throws FileNotFoundException {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new CSVIterator(f), Spliterator.ORDERED), false);
	}

	static class CSVIterator implements Iterator<List<String>> {
		private final BufferedReader br;
		private boolean finished = false;
		CSVIterator(File f) throws FileNotFoundException {
			br = new BufferedReader(new FileReader(f));
		}

		@Override
		public boolean hasNext() {
			return !finished;
		}

		@Override
		public List<String> next() {
			List<String> line = new ArrayList<String>();

			int c_int;
			char c;
			boolean inString = false;
			boolean lastBackslash = false;
			StringBuilder buffer = new StringBuilder();
			try {
				while((c_int = br.read()) != -1) {
					c = (char)c_int;
					if(inString){
						if(lastBackslash){
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
