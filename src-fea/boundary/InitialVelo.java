package boundary;

import java.io.Serializable;

import matrix.DVec;

/**
 * Class for initial velocities (for dynamic analysis).
 * 
 * @author Murat
 * 
 */
public class InitialVelo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Static variable for the coordinate system of velocity. */
	public final static int global_ = 0, local_ = 1;

	/** The name of initial velocity. */
	private String name_;

	/** The component vector of velocity. */
	private double[] components_;

	/** The coordinate system of velocity. */
	private int coordinateSystem_ = DispLoad.global_;

	/** The boundary case of initial velocity. */
	private BoundaryCase boundaryCase_;

	/** The value for scaling loading values. */
	private double scale_ = 1.0;

	/**
	 * Creates initial nodal velocity.
	 * 
	 * @param name
	 *            The name of initial velocity.
	 * @param boundaryCase
	 *            The boundary case of the initial velocity.
	 * @param components
	 *            The component vector of velocity. It has 6 components, first
	 *            three are translations, last three are rotations.
	 */
	public InitialVelo(String name, BoundaryCase boundaryCase, DVec components) {

		// set name
		name_ = name;

		// set boundary case
		boundaryCase_ = boundaryCase;

		// set components
		if (components.rowCount() == 6)
			components_ = components.get1DArray();
		else
			exceptionHandler("Illegal dimension of velocity vector!");
	}

	/**
	 * Sets boundary case to initial velocity.
	 * 
	 * @param boundaryCase
	 *            The boundary case to be set.
	 */
	public void setBoundaryCase(BoundaryCase boundaryCase) {
		boundaryCase_ = boundaryCase;
	}

	/**
	 * Sets coordinate system of initial velocity.
	 * 
	 * @param coordinateSystem
	 *            The nodal coordinate system to be set.
	 */
	public void setCoordinateSystem(int coordinateSystem) {
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal assignment for coordinate system!");
		else
			coordinateSystem_ = coordinateSystem;
	}

	/**
	 * Sets scalinf factor for loading values.
	 * 
	 * @param scale
	 *            The scaling factor for loading values.
	 */
	public void setLoadingScale(double scale) {
		scale_ = scale;
	}

	/**
	 * Returns the name of initial velocity.
	 * 
	 * @return The name of initial velocity.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the boundary case of initial velocity.
	 * 
	 * @return The boundary case of initial velocity.
	 */
	public BoundaryCase getBoundaryCase() {
		return boundaryCase_;
	}

	/**
	 * Returns the components vector of velocity.
	 * 
	 * @return The components vector of velocity.
	 */
	public DVec getComponents() {
		return new DVec(components_).scale(scale_);
	}

	/**
	 * Returns the coordinate system of velocity.
	 * 
	 * @return The coordinate system of velocity.
	 */
	public int getCoordinateSystem() {
		return coordinateSystem_;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}