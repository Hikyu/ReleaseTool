package com.oscar.releasetool.app;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
	public static void main(String[] args) throws IOException {
		Options options = CLIOptions.options;
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println( "Parsing failed. \n" + e.getMessage());
			return;
		}
		
		CmdExecutor executor = new CmdExecutor(cmd);
		executor.execute();
	}

	
}
