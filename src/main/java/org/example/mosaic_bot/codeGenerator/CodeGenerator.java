package org.example.mosaic_bot.codeGenerator;

import java.io.File;

public interface CodeGenerator {
    File generateFileWithCodes(int numberOfCodes, int numberOfCodeReuse);
}
