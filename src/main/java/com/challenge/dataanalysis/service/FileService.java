package com.challenge.dataanalysis.service;

import com.challenge.dataanalysis.model.Result;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.challenge.dataanalysis.service.AnalysisService.getResult;


@Service
public class FileService {

    private final AnalysisService analysisService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public FileService(@Autowired AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void readAndProcessFile(String pathOutput, File file) throws IOException {
        LOGGER.info("Reading file: " + file.getName());
        List<String> lines = FileUtils.readLines(file, "UTF-8");

        Result result = analysisService.analyze(lines);

        try {
            String path = pathOutput + file.getName().replace(".dat", ".done.dat");
            this.write(path, getResult(result));
        } catch (Exception e) {
            LOGGER.error(" >> {}", e.toString());
        }
    }

    private void write(String pathOutput, String data) throws IOException {
        LOGGER.info("Writing result file: " + pathOutput);
        FileWriter file = new FileWriter(pathOutput);
        PrintWriter saveFile = new PrintWriter(file);

        saveFile.print(data);
        file.close();
    }
}
