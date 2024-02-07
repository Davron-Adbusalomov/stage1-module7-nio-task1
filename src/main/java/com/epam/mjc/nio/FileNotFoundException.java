package com.epam.mjc.nio;

import java.io.FileInputStream;
import java.io.IOException;

public class FileNotFoundException extends IOException {
    public FileNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}
