package com.epam.mjc.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileReader {

    public Profile getDataFromFile(File file) {
        String fileData = null;
        try {
            fileData = readFile(file);
        } catch (FileNotFoundException e) {
            throw new FileReaderException("Not file found: "+ file.getAbsolutePath(), e);
        }

        String name = extractValueForKey("Name", fileData);
        int age = Integer.parseInt(extractValueForKey("Age", fileData));
        String email = extractValueForKey("Email", fileData);
        Long phone = Long.valueOf(extractValueForKey("Phone", fileData));

        return new Profile(name, age, email, phone);
    }

    public String readFile(File file) throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        try (FileInputStream fileInputStream = new FileInputStream(file);){
            FileChannel readChannel = fileInputStream.getChannel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

            int ch;
            while ((ch=readChannel.read(readBuffer))!=-1) {
                readBuffer.flip();

                stringBuilder.append(StandardCharsets.UTF_8.decode(readBuffer));

                readBuffer.clear();
            }
            return stringBuilder.toString();

        }catch (RuntimeException e) {
            throw new FileReaderException("Error reading file: " + file.getAbsolutePath(), e);
        } catch (IOException e) {
            throw new FileNotFoundException("Not file found: "+ file.getAbsolutePath(), e);
        }
    }

    private String extractValueForKey(String key, String fileData) {
        String keyPattern = key + ":\\s*(.*)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(keyPattern);
        java.util.regex.Matcher matcher = pattern.matcher(fileData);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return null; // Handle the case when key is not found
        }
    }
}
