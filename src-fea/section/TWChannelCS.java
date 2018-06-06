/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package section;

/**
 * Class for thin walled channel cross section.
 * 
 * @author Murat
 * 
 */
public class TWChannelCS extends Section {

	private static final long serialVersionUID = 1L;

	/** The dimensions of section. */
	private double b_, h_, t_;

	/**
	 * Creates thin walled channel cross section.
	 * 
	 * @param name
	 *            The name of section.
	 * @param h
	 *            The outside depth (height) of section.
	 * @param b
	 *            The flange width.
	 * @param t
	 *            The thickness of section.
	 */
	public TWChannelCS(String name, double h, double b, double t) {

		// set name
		name_ = name;

		// set geometrical properties
		b_ = b;
		h_ = h;
		t_ = t;

		// check dimensions
		checkDimensions();
	}

	@Override
	public int getType() {
		return Section.tWChannel_;
	}

	@Override
	public double getArea(int i) {
		return t_ * (2.0 * b_ + h_);
	}

	@Override
	public double getShearAreaX2(int i) {
		// TODO Computation codes...
		return 1.0;
	}

	@Override
	public double getShearAreaX3(int i) {
		// TODO Computation codes...
		return 1.0;
	}

	@Override
	public double getInertiaX2(int i) {
		return t_ / 12.0
				* (h_ * h_ * h_ + 2.0 * b_ * (3.0 * h_ * h_ + t_ * t_));
	}

	@Override
	public double getInertiaX3(int i) {
		return t_
				/ 12.0
				* (4.0 * b_ * b_ * b_ * (b_ + 2.0 * h_) / (2.0 * b_ + h_) + h_
						* t_ * t_);
	}

	@Override
	public double getTorsionalConstant(int i) {
		return t_ * t_ * t_ / 3.0 * (h_ + 2.0 * b_);
	}

	@Override
	public double getWarpingConstant(int i) {
		return h_ * h_ * b_ * b_ * b_ * t_ / 12.0 * (2.0 * h_ + 3.0 * b_)
				/ (h_ + 6.0 * b_);
	}

	/**
	 * Returns the demanded dimension of section.
	 * 
	 * @param i
	 *            The dimension index. 0 -> h, 1 -> b, 2 -> t.
	 */
	public double getDimension(int i) {
		if (i == 0)
			return h_;
		else if (i == 1)
			return b_;
		else
			return t_;
	}

	@Override
	public double[][] getOutline() {
		double[][] outline = new double[8][2];
		outline[0][0] = -t_ / 2.0;
		outline[0][1] = -h_ / 2.0 - t_ / 2.0;
		outline[1][0] = b_;
		outline[1][1] = -h_ / 2.0 - t_ / 2.0;
		outline[2][0] = b_;
		outline[2][1] = -h_ / 2.0 + t_ / 2.0;
		outline[3][0] = t_ / 2.0;
		outline[3][1] = -h_ / 2.0 + t_ / 2.0;
		outline[4][0] = t_ / 2.0;
		outline[4][1] = h_ / 2.0 - t_ / 2.0;
		outline[5][0] = b_;
		outline[5][1] = h_ / 2.0 - t_ / 2.0;
		outline[6][0] = b_;
		outline[6][1] = h_ / 2.0 + t_ / 2.0;
		outline[7][0] = -t_ / 2.0;
		outline[7][1] = h_ / 2.0 + t_ / 2.0;
		return outline;
	}

	/**
	 * Checks for illegal assignments to dimensions of section, if not throws
	 * exception.
	 */
	private void checkDimensions() {

		// message to be displayed
		String message = "Illegal dimensions for section!";

		// check if positive
		if (b_ <= 0 || h_ <= 0 || t_ <= 0)
			exceptionHandler(message);
	}

	@Override
	public double getCentroid(int i) {
		if (i == 0)
			return b_ * b_ / (2.0 * b_ + h_);
		else
			return 0.0;
	}

	@Override
	public double getShearCenter() {
		return 3.0 * b_ * b_ / (h_ + 6.0 * b_);
	}
}
