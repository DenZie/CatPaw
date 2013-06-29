package com.dd.test.catpaw.reports.reporter.runtimereport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;




/**
 * RuntimeReportResourceManager will take care of moving the resources files
 * needed for RuntimeReporter to test-output folder.
 * 
 */
public class RuntimeReportResourceManager {

//    final static SimpleLogger logger = CatPawLogger.getLogger();

    private void writeStreamToFile(InputStream isr, String fileName, String outputFolder) throws IOException {
//        // logger.entering(new Object[] { isr, fileName, outputFolder });

        File outFile = new File(outputFolder + File.separator + fileName);
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        FileOutputStream outStream = new FileOutputStream(outFile);
        byte[] bytes = new byte[1024];
        int readLength = 0;
        while ((readLength = isr.read(bytes)) != -1) {
            outStream.write(bytes, 0, readLength);
        }
        isr.close();
        outStream.flush();
        outStream.close();

//        // logger.exiting();
    }

    /**
     * This method copies all the resources specified in
     * RuntimeReporterResources.properties file and move it to
     * test-output/RuntimeReporter folder
     * 
     * @param outputFolder
     *            - the folder in which all the resource file will be moved.
     */
    public void copyResources(String outputFolder) {

//        // logger.entering(new Object[] { outputFolder });

        Properties resourceListToCopy = new Properties();
        try {
            ClassLoader localClassLoader = this.getClass().getClassLoader();

            resourceListToCopy.load(localClassLoader.getResourceAsStream("RuntimeReporterResources.properties"));
            Enumeration<Object> keys = resourceListToCopy.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String fileName = resourceListToCopy.getProperty(key);
                writeStreamToFile(localClassLoader.getResourceAsStream("templates/" + fileName), fileName, outputFolder);

            }

        } catch (IOException e) {
//    //        // logger.log(Level.SEVERE, e.getMessage(), e);
        }
//        // logger.exiting();
    }

}
