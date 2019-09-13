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
import java.util.Set;

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
	//Map all data in the the CSV file at 'path' to its respsective header columm
	//Result = hashtable of headers and a list of their data.
	@SuppressWarnings("unused")
	public static void mapCSVHeaders(String path) {
		CSVParser csvParser = null;
		LOGGER.info("[CSVParser] Initializing CSV mapper ");
		long startTime = System.currentTimeMillis();
		try {
	            Reader reader = Files.newBufferedReader(Paths.get(path));
	            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord(false));
	            List<String> headers = csvParser.getHeaderNames();
	            Hashtable<String, List<String>> data = new Hashtable<String, List<String>>(0);
	            
	            LOGGER.info("[CSVParser] Found "+headers.size()+" headers in CSV file '"+path+"'");
	            LOGGER.info("[CSVParser] Mapping data in '"+path+"'");
	            
	            for(CSVRecord record: csvParser) {
	            	Map<String,String> mapped = record.toMap();
	            	for(Entry<String,String> entry: mapped.entrySet()) {
	            		if(!data.containsKey(entry.getKey())) {
	            			List<String> newArray = new ArrayList<String>();
	            			newArray.add(entry.getValue());
	            			data.put(entry.getKey(), newArray);
	            		}else {
	            			data.get(entry.getKey()).add(entry.getValue());
	            		}
	            	}
//	            	if(data.get(mapped.))
//	            	data.put(key, value)
	            }
	            LOGGER.info("[CSVParser] Mapped CSV file in ["+(System.currentTimeMillis()-startTime)+"ms]");
	            LOGGER.info("[CSVParser] Result "+gen.serialize(data));
	            
	            
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				csvParser.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
