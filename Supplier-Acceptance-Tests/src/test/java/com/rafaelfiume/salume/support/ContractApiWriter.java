package com.rafaelfiume.salume.support;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.trim;

public final class ContractApiWriter {

    private static final String GENERATED_FILES_DIRECTORY = "target/input-output-examples";

    private ContractApiWriter() {
        // not instantiable
    }

    public static void writeApiRequestContractExample(String content, String className, String methodName) {
        saveCaptured(content, className, withNameUsing("request", methodName));
    }

    public static void writeApiResponseContractExample(String content, String className, String methodName) {
        saveCaptured(content, className, withNameUsing("response", methodName));
    }

    private static String withNameUsing(String requestOrResponse, String methodName) {
        return trim(format("%s.%s.txt", requestOrResponse, methodName));
    }

    private static void saveCaptured(String content, String className, String fileName) {
        try {
            final File exampleOutputDir = new File(GENERATED_FILES_DIRECTORY + File.separator + className);
            FileUtils.forceMkdir(exampleOutputDir);
            FileUtils.write(new File(exampleOutputDir, fileName), content, "UTF-8");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
