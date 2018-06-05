package write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import matrix.DVec;

import boundary.ElementMechLoad;

import analysis.Structure;

/**
 * Class for writing element mechanical load information to output file.
 * 
 * @author Murat
 * 
 */
public class ElementMechLoadInfo extends Writer {

	/** Buffered writer. */
	private BufferedWriter bwriter_;

	/**
	 * Writes element mechanical load information to output file.
	 * 
	 * @param structure
	 *            The structure to be printed.
	 * @param output
	 *            The output file.
	 */
	protected void write(Structure structure, File output) {

		try {

			// create file writer with appending option
			FileWriter writer = new FileWriter(output, true);

			// create buffered writer
			bwriter_ = new BufferedWriter(writer);

			// write tables
			writeTable1(structure);
			writeTable2(structure);

			// close writer
			bwriter_.close();
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table part 1.
	 * 
	 */
	private void writeTable1(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Element Mechanical Loads, 1"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Name", "Case", "C-System", "Dist." };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over elements
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get mechanical loads of element
				Vector<ElementMechLoad> mechLoads = s.getElement(i)
						.getAllMechLoads();

				// check if any mech load available
				if (mechLoads != null) {

					// loop over mechanical loads of element
					for (int j = 0; j < mechLoads.size(); j++) {

						// get mechanical load
						ElementMechLoad mechLoad = mechLoads.get(j);

						// get properties
						table[0] = Integer.toString(i);
						table[1] = mechLoad.getName();
						table[2] = mechLoad.getBoundaryCase().getName();
						if (mechLoad.getCoordinateSystem() == ElementMechLoad.global_)
							table[3] = "Global";
						else
							table[3] = "Local";
						if (mechLoad.getDegree() == 0)
							table[4] = "Uniform";
						else {
							if (mechLoad.getType() == ElementMechLoad.line_)
								table[4] = "Linear";
							else
								table[4] = "Bilinear";
						}

						// write
						bwriter_.write(table(table));
						bwriter_.newLine();
					}
				}
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}

	/**
	 * Writes table part 2.
	 * 
	 */
	private void writeTable2(Structure s) {

		try {

			// pass to new line
			bwriter_.newLine();

			// write header for first part of table
			bwriter_.write(header("Element Mechanical Loads, 2"));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// write headers
			String[] table = { "Element", "Comp.", "Value-a", "Value-b",
					"Value-c" };
			bwriter_.write(table(table));

			// pass two lines
			bwriter_.newLine();
			bwriter_.newLine();

			// loop over elements
			for (int i = 0; i < s.getNumberOfElements(); i++) {

				// get mechanical loads of element
				Vector<ElementMechLoad> mechLoads = s.getElement(i)
						.getAllMechLoads();

				// check if any mech load available
				if (mechLoads != null) {

					// loop over mechanical loads of element
					for (int j = 0; j < mechLoads.size(); j++) {

						// get mechanical load
						ElementMechLoad mechLoad = mechLoads.get(j);

						// get properties
						for (int k = 0; k < table.length; k++)
							table[k] = " ";
						table[0] = Integer.toString(i);
						int comp = mechLoad.getComponent();
						int coord = mechLoad.getCoordinateSystem();
						if (comp == ElementMechLoad.fx_) {
							if (coord == ElementMechLoad.global_)
								table[1] = "fx";
							else
								table[1] = "f1";
						} else if (comp == ElementMechLoad.fy_) {
							if (coord == ElementMechLoad.global_)
								table[1] = "fy";
							else
								table[1] = "f2";
						} else if (comp == ElementMechLoad.fz_) {
							if (coord == ElementMechLoad.global_)
								table[1] = "fz";
							else
								table[1] = "f3";
						} else if (comp == ElementMechLoad.mx_) {
							if (coord == ElementMechLoad.global_)
								table[1] = "mx";
							else
								table[1] = "m1";
						} else if (comp == ElementMechLoad.my_) {
							if (coord == ElementMechLoad.global_)
								table[1] = "my";
							else
								table[1] = "m2";
						} else if (comp == ElementMechLoad.mz_) {
							if (coord == ElementMechLoad.global_)
								table[1] = "mz";
							else
								table[1] = "m3";
						}
						DVec values = mechLoad.getLoadingValues();
						for (int k = 0; k < values.rowCount(); k++)
							table[k + 2] = formatter(values.get(k));

						// write
						bwriter_.write(table(table));
						bwriter_.newLine();
					}
				}
			}
		}

		// exception occured
		catch (Exception excep) {
			exceptionHandler("Exception occured during writing output file!");
		}
	}
}