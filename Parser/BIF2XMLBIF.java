package Parser;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Interfaces.BayesianNetwork;

/**
 * Uses BIFParser and XMLBIFPrinter to convert from BIF to XMLBIF. 
 */
public class BIF2XMLBIF {

	/**
	 * Parse a BIF file and print out the resulting BayesianNetwork as XMLBIF.
	 * <p>
	 * Usage: java bn.parser.BIF2XMLBIF FILE
	 * <p>
	 * With no arguments: reads dog-problem.bif in the src tree 
	 */

	public BIF2XMLBIF(){

	}
	public BayesianNetwork getNetwork(String name) throws IOException, ParserConfigurationException, SAXException {
		/*String filename = "Examples/dog-problem.bif";
		String networkName = "Dog-Problem";
		if (argv.length > 0) {
			filename = argv[0];
			if (argv.length > 1) {
				networkName = argv[1];
			} else {
				networkName = "Un=named network";
			}
		}*/


		BIFParser parser = new BIFParser(new FileInputStream(name));
		XMLBIFPrinter printer = new XMLBIFPrinter(System.out);
		BayesianNetwork network = parser.parseNetwork();
		//printer.print(network);
		return network;
	}



}
