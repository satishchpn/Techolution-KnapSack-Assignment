package com.knapsack.daoImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.knapsack.dao.KnapsackDao;
import com.knapsack.structure.InputFileData;

/**
 * This class will help to intract with file.
 * 
 * @author Satish
 * 
 *
 */
@Component
@Qualifier("KnapsackDaoImpl")
@Scope("prototype")
public class KnapsackDaoImpl implements KnapsackDao {
	private static Logger logger = Logger.getLogger(KnapsackDaoImpl.class);

	private File file;

	/**
	 * Reads File data and returns FileData Object.
	 */
	public InputFileData getDataFromFile(File file) {
		this.file = file;
		return getDataFromFile();
	}

	/**
	 * 
	 * (This can be read through application.properties
	 * using @Value("${item.file}") and ResourceLoader) Reads File data and
	 * returns FileData Object.
	 */
	private InputFileData getDataFromFile() {
		if (file == null) {
			logger.error("Please pass File.");
			return null;
		}

		logger.info("Reading File.");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line = null;
			InputFileData fileData = new InputFileData();
			while ((line = reader.readLine()) != null) {
				String items[] = line.split(" ");
				fileData.pushToList(Integer.parseInt(items[0]), Integer.parseInt(items[1]));
			}
			return fileData;
		} catch (NumberFormatException e) {
			logger.error("Pass Number", e);
		} catch (FileNotFoundException e) {
			logger.error("File not found.", e);
		} catch (IOException e) {
			logger.error("Can't Read File", e);
		} catch (Exception e) {
			logger.error("Exception.", e);
		}
		return null;
	}

	public void setDataFile(File file) {
		this.file = file;
	}

	public File getDataFile() {
		return file;
	}
}
