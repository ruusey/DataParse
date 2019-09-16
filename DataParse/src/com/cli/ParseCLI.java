package cli;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import csv.CSVParse;
import json.JsonParse;
import schema.SchemaParse;
import xml.XMLParse;

public class ParseCLI {
	static final Logger LOGGER = LogManager.getLogger(ParseCLI.class);

	public static void main(String[] args) {
		LOGGER.info("[ParseCLI] Initialized...");
		if (args.length == 2) {
			if (args[0].contains(".") && !args[0].contains("/")) {
				String schema = args[0];
				LOGGER.info("[ParseCLI] Parsing schema '" + schema + "'");
				String[] parts = schema.split("\\.");
				Hashtable<String, List<String>> schemaOut = SchemaParse.mapTable(parts[0], parts[1]);
				File fileOut = null;
				try {
					fileOut = new File(args[1]);
				} catch (Exception e) {
					LOGGER.error("[ParseCLI] " + e.getMessage());
				}
				writeToFile(serializePretty(schemaOut), fileOut);
			} else {
				File fileIn = null;
				File fileOut = null;
				try {
					fileIn = new File(args[0]);
					fileOut = new File(args[1]);
				} catch (Exception e) {
					LOGGER.error("[ParseCLI] " + e.getMessage());
				}
				LOGGER.info("[ParseCLI] Parsing file '" + args[0] + "'");
				switch (getFileExtension(fileIn)) {
				case "xml":
					List<String> xmlOut = XMLParse.mapXML(fileIn.getAbsolutePath());
					writeToFile(serializePretty(xmlOut), fileOut);
					break;
				case "json":
					List<String> jsonOut = JsonParse.mapJson(fileIn.getAbsolutePath());
					writeToFile(serializePretty(jsonOut), fileOut);
					break;
				case "csv":
					Hashtable<String, List<String>> csvOut = CSVParse.mapCSVHeaders(fileIn.getAbsolutePath());
					writeToFile(serializePretty(csvOut), fileOut);
					break;
				}
			}
		} else {
			LOGGER.info("[ParseCLI] Too few arguments expected <path|schema.table> <output_directory>.");
		}
	}

	public static String serializePretty(Object o) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(o);
		return json;
	}

	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

	private static void writeToFile(String outTxt, File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(outTxt);
			writer.close();
			LOGGER.info("[ParseCLI] saved output to '" + file.getAbsolutePath() + "'");
		} catch (Exception e) {
			LOGGER.error("[ParseCLI] " + e.getMessage());
		}
	}
}
