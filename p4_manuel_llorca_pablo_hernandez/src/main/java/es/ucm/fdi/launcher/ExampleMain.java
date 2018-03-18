package es.ucm.fdi.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

import es.ucm.fdi.control.Controller;
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

	/**
	 * Genera y devuelve una colección de opciones posibles de línea
	 * de comando.
	 * @return colección de opciones
	 */
	private static Options buildOptions() {
		// Colección
		Options cmdLineOptions = new Options();

		// Comando de ayuda: -h; --help; "Print this message"
		cmdLineOptions.addOption(
			Option.builder("h")
			.longOpt("help")
			.desc("Print this message")
			.build());
		// Comando de input: -i; --input; <arg.ini>; "Events input file"
		cmdLineOptions.addOption(
			Option.builder("i")
			.longOpt("input")
			.hasArg()
			.desc("Events input file")
			.build());
		// Comando de salida: -o; --output; <arg.ini>; "Output file, where reports are written"
		cmdLineOptions.addOption(
				Option.builder("o")
				.longOpt("output")
				.hasArg()
				.desc("Output file, where reports are written.")
				.build());
		// Comando de ticks: -t; --ticks; <x>; "Ticks to execute the simulator's main loop..."
		cmdLineOptions.addOption(
			Option.builder("t")
			.longOpt("ticks")
			.hasArg()
			.desc("Ticks to execute the simulator's main loop (default value is " + _timeLimitDefaultValue + ").")
			.build());

		return cmdLineOptions;
	}

	/**
	 * Si el comando tiene la opción de ayuda, se muestra la ayuda de todas las
	 * opciones disponibles en cmdLineOptions.
	 */
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(ExampleMain.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	/**
	 * Actualiza el nombre del fichero de entrada de datos de la clase Main
	 * con la opción dada en la línea de comando.
	 * @throws ParseException if there is no events file
	 */
	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	/**
	 * Actualiza el nombre del fichero de salida de la clase Main
	 * con la opción dada en la línea de comando.
	 */
	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	/**
	 * Actualiza el número de pasos indicados por el comando en el
	 * atributo _timeLimit de Main. Si no hay ninguno, por defecto es 10.
	 * @throws ParseException if the time value is not valid
	 */
	private static void parseStepsOption(CommandLine line) throws ParseException {
		// Si no se ha introducido ningún valor, se toma el por defecto.
		String t = line.getOptionValue("t", _timeLimitDefaultValue.toString());
		// Se comprueba que el valor introducido sea válido.
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
			test(file.getAbsolutePath(), file.getAbsolutePath() + ".out", file.getAbsolutePath() + ".eout",10);
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
		// Controlador.
		Ini iniInput = new Ini(_inFile);
		File outFile = new File(_outFile);
		OutputStream os = new FileOutputStream(outFile);
		Controller control = new Controller(iniInput, os, _timeLimit);

		// Ejecución
		control.execute();
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

		// Call test in order to test the simulator on all examples in a directory.
		//
		// test("src/main/resources/examples/err");
		test("src/main/resources/examples/basic");

		// Call start to start the simulator from command line, etc.
		// start(args);

	}

}
