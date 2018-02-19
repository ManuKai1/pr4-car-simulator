package es.ucm.fdi.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.ucm.fdi.ini.Ini;

public class ExampleMain {

	private final static Integer _timeLimitDefaultValue = 10;
	private static Integer _timeLimit = null;
	private static String _inFile = null;
	private static String _outFile = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseStepsOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			// new Piece(...) might throw GameError exception
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg()
				.desc("Ticks to execute the simulator's main loop (default value is " + _timeLimitDefaultValue + ").")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(ExampleMain.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void parseStepsOption(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", _timeLimitDefaultValue.toString());
		try {
			_timeLimit = Integer.parseInt(t);
			assert (_timeLimit < 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time limit: " + t);
		}
	}

	/**
	 * This method run the simulator on all files that ends with .ini if the given
	 * path, and compares that output to the expected output. It assumes that for
	 * example "example.ini" the expected output is stored in "example.ini.eout".
	 * The simulator's output will be stored in "example.ini.out"
	 * 
	 * @throws IOException
	 */
	private static void test(String path) throws IOException {

		File dir = new File(path);

		if ( !dir.exists() ) {
			throw new FileNotFoundException(path);
		}
		
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ini");
			}
		});

		for (File file : files) {
			test(file.getAbsolutePath(), file.getAbsolutePath() + ".out", file.getAbsolutePath() + ".eout",100);
		}

	}

	private static void test(String inFile, String outFile, String expectedOutFile, int timeLimit) throws IOException {
		_outFile = outFile;
		_inFile = inFile;
		_timeLimit = timeLimit;
		startBatchMode();
		boolean equalOutput = (new Ini(_outFile)).equals(new Ini(expectedOutFile));
		System.out.println("Result for: '" + _inFile + "' : "
				+ (equalOutput ? "OK!" : ("not equal to expected output +'" + expectedOutFile + "'")));
	}

	/**
	 * Run the simulator in batch mode
	 * 
	 * @throws IOException
	 */
	private static void startBatchMode() throws IOException {
		// TODO
		// Add your code here. Note that the input argument where parsed and stored into
		// corresponding fields.
	}

	private static void start(String[] args) throws IOException {
		parseArgs(args);
		startBatchMode();
	}

	public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {

		// example command lines:
		//
		// -i resources/examples/events/basic/ex1.ini
		// -i resources/examples/events/basic/ex1.ini -o ex1.out
		// -i resources/examples/events/basic/ex1.ini -t 20
		// -i resources/examples/events/basic/ex1.ini -o ex1.out -t 20
		// --help
		//

		// Call test in order to test the simulator on all examples in a directory.
		//
	    //	test("resources/examples/events/basic");

		// Call start to start the simulator from command line, etc.
		start(args);

	}

}
