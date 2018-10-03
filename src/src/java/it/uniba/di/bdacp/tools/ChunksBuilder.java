package it.uniba.di.bdacp.tools;

import java.io.*;
import java.util.Properties;

public class ChunksBuilder {
    private String folderPath;
    private String fileName;
    private String suffix;
    private long maxFileSize;

    private File lastFile;
    private PrintWriter out;
    private int currIndex;
    private long bytesWritten;

    public ChunksBuilder(Properties conf) throws IOException {
        this.folderPath = conf.getProperty("folder_path");
        this.fileName = conf.getProperty("file_name");
        this.suffix = conf.getProperty("suffix");
        this.maxFileSize = Long.parseLong(conf.getProperty("max_file_size"));

        File folder = new File(folderPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        this.currIndex = findFirstAvailableChunk();

        lastFile = new File(folderPath + File.separator + fileName + currIndex + suffix);
        if(!lastFile.exists()) {
            this.bytesWritten = 0;
            lastFile.createNewFile();
        } else {
            this.bytesWritten = lastFile.length();
        }

        out = new PrintWriter(new BufferedWriter(new FileWriter(lastFile, true)));
    }

    public void addData(String data) throws IOException {
        if (bytesWritten + data.length() + "\r\n".length() >= maxFileSize) {
            out.close();
            bytesWritten = 0;
            currIndex++;
            lastFile = new File(folderPath + File.separator + fileName + currIndex + suffix);
            out = new PrintWriter(new BufferedWriter(new FileWriter(lastFile, true)));
        }

        out.print(data);
        out.print("\r\n");
        bytesWritten += data.length() + "\r\n".length();
    }

    public void close() {
        out.close();
    }

    private int findFirstAvailableChunk() {
        int lastIndex = 0;
        File f = new File(folderPath + File.separator + fileName + lastIndex + suffix);

        while(f.exists()) {
            f = new File(folderPath + File.separator + fileName + ++lastIndex + suffix);
        } // lastIndex is set to the first non-existing chunk

        if (lastIndex == 0) { // No chunk exists
            return lastIndex;
        }

        if(new File(folderPath + File.separator + fileName + String.valueOf(lastIndex-1) + suffix).length() <= this.maxFileSize) { // The last chunk isn't full
            return lastIndex - 1;
        }

        return lastIndex;
    }
}

