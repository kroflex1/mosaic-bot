package org.example.mosaic_bot.codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InMemoryCodeGenerator implements CodeGenerator {
    @Override
    public File generateFileWithCodes(int numberOfCodes, int numberOfCodeReuse) {
        List<String> codes = getCodes(numberOfCodes);
        return createFileWithData(codes);
    }

    private File createFileWithData(List<String> codes) {
        File file = null;
        try {
            file = File.createTempFile("codes", ".txt");
            FileWriter writer = new FileWriter(file);
            writer.write(String.join("\n", codes));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private List<String> getCodes(int numberOfCodes) {
        List<String> codes = new ArrayList<>();
        for (int i = 1; i <= numberOfCodes; i++) {
            codes.add(String.valueOf(i));
        }
        return codes;
    }
}
