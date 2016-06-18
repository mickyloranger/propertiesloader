package com.mickyloranger.propertiesloader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author MickyLoranger
 * 
 *
 */
public class App {
    private static final String IV_DATA_PROPERTY_NAME = "protocol";
    private static final String KEY_PROPERTY_NAME = "secure";
    private static final String VALUE_ATTRIBUTE_SEPARATOR = "=";
    private static final Object STAGE_END_LINE = ":";
    private static final String STAGE_SEPARATOR = "/";
    private static final char COMMENT_LINE = '#';

    public static void main(String[] args) throws IOException {
	Map<String, Map<String, String>> result = new App().loadProperties("application.properties");
	System.out.println(result);
    }

    public Map<String, Map<String, String>> loadProperties(String relativeFilePath) throws IOException {
	ClassLoader classLoader = this.getClass().getClassLoader();
	URL url = classLoader.getResource(relativeFilePath);
	if (url == null) {
	    throw new FileNotFoundException("the file was not found");
	}

	String filePath = url.getFile();

	@SuppressWarnings("resource")
	BufferedReader br = new BufferedReader(new FileReader(filePath));

	String sCurrentLine;

	Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();

	String[] keys = null;

	Map<String, String> values = new HashMap<String, String>();

	int counter = 0;

	while ((sCurrentLine = br.readLine()) != null) {

	    sCurrentLine = sCurrentLine.trim();

	    boolean iscurrentLineEmpty = sCurrentLine.isEmpty();

	    if (!iscurrentLineEmpty) {

		boolean isCurrentLineComment = sCurrentLine.charAt(0) == COMMENT_LINE;

		if (!isCurrentLineComment) {

		    counter++;

		    if (canPutResult(counter)) {

			if (keys == null) {
			    throw new RuntimeException(
				    "the property file contain an invalid key or doesn't contain a Fkey");
			}

			for (int i = 0; i < keys.length; i++) {
			    result.put(keys[i], values);
			}
		    }

		    if (isStageLine(sCurrentLine)) {
			keys = getStages(sCurrentLine);
			values = new HashMap<String, String>();
		    }

		    if (isIvDataLine(sCurrentLine)) {
			values.put(IV_DATA_PROPERTY_NAME, getIvData(sCurrentLine));
		    }

		    if (isKeyLine(sCurrentLine)) {
			values.put(KEY_PROPERTY_NAME, getKey(sCurrentLine));
		    }
		}
	    }
	}

	if (br != null) {
	    br.close();
	}

	return result;

    }

    private boolean isStageLine(String lineContent) {
	int length = lineContent.length() - 1;
	return lineContent.substring(length).equals(STAGE_END_LINE);
    }

    private String[] getStages(String lineContent) {
	int length = lineContent.length() - 1;
	String lineContentWithoutKomma = lineContent.substring(0, length);
	String[] stages = lineContentWithoutKomma.split(STAGE_SEPARATOR);
	for (int i = 0; i < stages.length; i++) {
	    stages[i] = stages[i].trim();
	}
	return stages;
    }

    private boolean isIvDataLine(String lineContent) {
	String[] content = lineContent.split(VALUE_ATTRIBUTE_SEPARATOR);
	return (content[0].equals(IV_DATA_PROPERTY_NAME)) && (content.length == 2);
    }

    private String getIvData(String lineContent) {
	String[] content = lineContent.split(VALUE_ATTRIBUTE_SEPARATOR);
	return content[1];
    }

    private boolean isKeyLine(String lineContent) {
	String[] content = lineContent.split(VALUE_ATTRIBUTE_SEPARATOR);
	return (content[0].equals(KEY_PROPERTY_NAME)) && (content.length == 2);
    }

    private String getKey(String lineContent) {
	String[] content = lineContent.split(VALUE_ATTRIBUTE_SEPARATOR);
	return content[1];
    }

    private boolean canPutResult(int counter) {
	return (counter % 3) == 0;
    }
}