package com.challenge.dataanalysis;

import com.challenge.dataanalysis.service.WatchFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class DataAnalysisApplication implements ApplicationListener<ApplicationReadyEvent> {

	public static void main(String[] args) {
		SpringApplication.run(DataAnalysisApplication.class, args);
	}

	public DataAnalysisApplication(@Autowired WatchFiles watchFiles) {
		this.watchFiles = watchFiles;
	}


	private final WatchFiles watchFiles;

	@Value("${INPUTPATH:${user.dir}}")
	private String inputPath;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		try {
			watchFiles.init(inputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
