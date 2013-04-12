package com.dd.test.catpaw.reports.runtime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;


import com.dd.test.catpaw.reports.model.Screenshot;
import com.dd.test.catpaw.reports.model.Source;


/**
 * saver that store the info on the filesystem.
 * 
 */
public class SaverFileSystem implements DataSaver {
	
	// private static SimpleLogger logger = CatPawLogger.getLogger();

	String outputFolder;

	public SaverFileSystem(String testNGOutputFolder) {
		this.outputFolder = testNGOutputFolder;
	}


	public String saveScreenshot(Screenshot s) {
		// logger.entering(s);
		String screenshotAbsolutePath = getScreenshotAbsolutePath(s.getUuid());
		try {
			OutputStream out = new FileOutputStream(screenshotAbsolutePath);
			out.write(s.getScreenshot());
			out.close();
		} catch (IOException e) {
			// logger.log(Level.SEVERE, "An error occurred while trying to save screenshot " + e.getMessage(), e);
		}
		String screenshotUrl = "screenshots/" + s.getUuid() + ".png";
		// logger.exiting(screenshotUrl);
		return screenshotUrl;

	}

	private String getScreenshotAbsolutePath(String name) {
		// logger.entering(name);
		String screenshotPath = "screenshots" + File.separator + name + ".png";
		String screenshotAbsolutePath = outputFolder + screenshotPath;
		// logger.exiting(screenshotAbsolutePath);
		return screenshotAbsolutePath;
	}

	public String saveSources(Source s) {
		/*
		 * Made output to txt file not html
		 */
		// logger.entering(s);
		String path = outputFolder + "sources" + File.separator + s.getUuid()
				+ ".source.txt";
		BufferedWriter out;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path), "UTF8"));
			out.write(s.getSource());
			out.close();
		} catch (Exception e) {
			// logger.log(Level.SEVERE, "An error occurred while trying to save page source " + e.getMessage(), e);
		}
		// logger.exiting(path);

		return path;
	}


	public Screenshot getScreenshotByName(String name) throws Exception {
		// logger.entering(name);
		String path = getScreenshotAbsolutePath(name);
		File f = new File(path);
		long timestamp = f.lastModified();
		byte[] bytes = getBytesFromFile(f);
		Screenshot returnValue = new Screenshot(bytes, name, timestamp);
		// logger.exiting();
		return returnValue;
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		// logger.entering(file);
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			IOException e = new IOException("Could not completely read file "
					+ file.getName()); 
			// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}

		// Close the input stream and return bytes
		is.close();
		// logger.exiting();
		return bytes;
	}

	/**
	 * @see com.dd.test.catpaw.reports.runtime.DataSaver#init()
	 * Creates directories sources, html, screenshots based off output folder
	 */

	public void init() {
		// logger.entering();
		(new File(outputFolder)).mkdirs();
		(new File(outputFolder, "sources")).mkdir();
		(new File(outputFolder, "html")).mkdir();
		(new File(outputFolder, "screenshots")).mkdir();
		// logger.exiting();
	}
}
