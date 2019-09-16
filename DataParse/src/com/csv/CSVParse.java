package csv;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.owlike.genson.Genson;

public class CSVParse {
	static final Logger LOGGER = LogManager.getLogger(CSVParse.class);
	static Genson gen = new Genson();

	public static void main(String[] args) {
		mapCSVHeaders("resources/user_sample.csv");
	}

	public static Hashtable<String, List<String>> mapCSVHeaders(String path) {
		CSVParser csvParser = null;
		LOGGER.info("[CSVParser] Initializing CSV mapper ");
		long startTime = System.currentTimeMillis();
		try {
			Reader reader = Files.newBufferedReader(Paths.get(path));
			csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord(false));
			List<String> headers = csvParser.getHeaderNames();
			Hashtable<String, List<String>> data = new Hashtable<String, List<String>>(0);

			LOGGER.info("[CSVParser] Found " + headers.size() + " headers in CSV file '" + path + "'");
			LOGGER.info("[CSVParser] Mapping data in '" + path + "'");

			for (CSVRecord record : csvParser) {
				Map<String, String> mapped = record.toMap();
				for (Entry<String, String> entry : mapped.entrySet()) {
					if (!data.containsKey(entry.getKey())) {
						List<String> newArray = new ArrayList<String>();
						newArray.add(entry.getValue());
						data.put(entry.getKey(), newArray);
					} else {
						data.get(entry.getKey()).add(entry.getValue());
					}
				}
			}
			LOGGER.info("[CSVParser] Mapped CSV file in [" + (System.currentTimeMillis() - startTime) + "ms]");
			//LOGGER.info("[CSVParser] Result " + gen.serialize(data));
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				csvParser.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
