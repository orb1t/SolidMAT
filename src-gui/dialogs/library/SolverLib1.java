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
package dialogs.library;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;

import analysis.*;

import solver.Solver;

/**
 * Class for Solver Library menu.
 * 
 * @author Murat
 * 
 */
public class SolverLib1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	protected JList list1_;

	protected DefaultListModel listModel1_;

	/** Used for renewing analysis cases objects vector. */
	protected Vector<Analysis> analysis_ = new Vector<Analysis>();

	/** Temporary vector used for storing objects. */
	protected Vector<Solver> temporary_ = new Vector<Solver>();

	/** The owner frame of this dialog. */
	protected SolidMAT owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners, calls initialize and visualize.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public SolverLib1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Solver Library", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Solvers", Commons.gridbag_);

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(11);
		list1_.setFixedCellWidth(120);
		list1_.setFixedCellHeight(15);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// build buttons
		JButton button1 = new JButton("Add");
		JButton button2 = new JButton("Modify");
		JButton button3 = new JButton("Delete");
		JButton button4 = new JButton("  OK  ");
		JButton button5 = new JButton("Cancel");

		// add components to panels
		Commons.addComponent(panel3, scrollpane1, 0, 1, 1, 5);
		Commons.addComponent(panel3, button1, 0, 0, 1, 1);
		Commons.addComponent(panel3, button2, 1, 0, 1, 1);
		Commons.addComponent(panel3, button3, 2, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button4);
		panel2.add(button5);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);

		// call initialize
		initialize();

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets the input data vector to temporary vector. Copies names to list.
	 */
	private void initialize() {

		// set the input data vector to temporary vector
		Vector<Solver> object = owner_.inputData_.getSolver();
		for (int i = 0; i < object.size(); i++)
			temporary_.add(object.get(i));

		// set analysis case vector from input vector
		Vector<Analysis> object1 = owner_.inputData_.getAnalysis();
		for (int i = 0; i < object1.size(); i++)
			analysis_.add(object1.get(i));

		// copy names to list
		for (int i = 0; i < temporary_.size(); i++)
			listModel1_.addElement(temporary_.get(i).getName());
	}

	/**
	 * If add or modify button clicked, builds child dialog. If delete button
	 * clicked, calls actionDelete. If ok button clicked, calls actionOk. If
	 * cancel button clicked sets dialog unvisible.
	 */
	public void actionPerformed(ActionEvent e) {

		// add button clicked
		if (e.getActionCommand() == "Add") {

			// build child dialog and set visible
			SolverLib2 dialog = new SolverLib2(this, true);
			dialog.setVisible(true);
		}

		// modify button clicked
		else if (e.getActionCommand() == "Modify") {

			// check if any item selected in list
			if (list1_.isSelectionEmpty() == false) {

				// build child dialog and set visible
				SolverLib2 dialog = new SolverLib2(this, false);
				dialog.setVisible(true);
			}
		}

		// delete button clicked
		else if (e.getActionCommand() == "Delete") {

			// check if any item selected in list
			if (list1_.isSelectionEmpty() == false) {

				// call actionDelete
				actionDelete();
			}
		}

		// ok button clicked
		else if (e.getActionCommand() == "  OK  ") {

			// call actionOk
			actionOk();
		}

		// cancel button clicked
		else if (e.getActionCommand() == "Cancel") {

			// set dialog unvisible
			setVisible(false);
		}
	}

	/**
	 * Deletes selected list item from temporary vector and list.
	 */
	private void actionDelete() {

		// get the selection index from list
		int selected = list1_.getSelectedIndex();

		// check if there is only one item of its kind
		if (checkLast(selected)) {

			// check for assigned libraries
			if (checkAssigned()) {

				// display confirmation message
				int confirm = JOptionPane.showConfirmDialog(this,
						"Solver is assigned to other libraries."
								+ "\nDo you still want to delete?",
						"Data confirmation", JOptionPane.YES_NO_OPTION);

				// yes is chosen
				if (confirm == JOptionPane.YES_OPTION) {

					// delete assigned boundaries
					deleteAssigned();

					// delete it from vector
					temporary_.remove(selected);

					// delete it from list
					listModel1_.remove(selected);
				}
			}

			// no assigned boundaries
			else {

				// delete it from vector
				temporary_.remove(selected);

				// delete it from list
				listModel1_.remove(selected);
			}
		}

		// there is only one item
		else {

			// display message
			JOptionPane
					.showMessageDialog(
							this,
							"At least one solver for each kind of problem should exist!",
							"False data entry", 2);
		}
	}

	/**
	 * Removes assigned libraries.
	 * 
	 */
	private void deleteAssigned() {

		// get selected item
		String item = list1_.getSelectedValue().toString();

		// delete assigned analysis cases
		Vector<Analysis> analyTemp = new Vector<Analysis>();
		for (int i = 0; i < analysis_.size(); i++) {

			// get type of analysis case
			int type = analysis_.get(i).getType();

			// for linear static analysis
			if (type == Analysis.linearStatic_) {

				// get linear static analysis
				LinearStatic ls = (LinearStatic) analysis_.get(i);

				// get solver name
				String sName = ls.getSolver().getName();

				// return true if assigned
				if (sName.equals(item) == false)
					analyTemp.add(analysis_.get(i));
			}

			// for modal analysis
			else if (type == Analysis.modal_) {

				// get modal analysis
				Modal m = (Modal) analysis_.get(i);

				// get solver name
				String sName = m.getSolver().getName();

				// return true if assigned
				if (sName.equals(item) == false)
					analyTemp.add(analysis_.get(i));
			}

			// for linear transient analysis
			else if (type == Analysis.linearTransient_) {

				// get linear transient analysis
				LinearTransient lt = (LinearTransient) analysis_.get(i);

				// get solver name
				String sName = lt.getSolver().getName();

				// return true if assigned
				if (sName.equals(item) == false)
					analyTemp.add(analysis_.get(i));
			}

			// for linear buckling analysis
			else if (type == Analysis.linearBuckling_) {

				// get linear buckling analysis
				LinearBuckling lb = (LinearBuckling) analysis_.get(i);

				// get solver names
				String sName = lb.getSolver().getName();

				// return true if assigned
				if (sName.equals(item) == false)
					analyTemp.add(analysis_.get(i));
			}

			// for other types of analyses
			else
				analyTemp.add(analysis_.get(i));
		}
		analysis_ = analyTemp;
	}

	/**
	 * Checks for assigned libraries.
	 * 
	 * @return True if there is any assigned, False if not.
	 */
	private boolean checkAssigned() {

		// get selected item
		String item = list1_.getSelectedValue().toString();

		// check for analysis cases
		for (int i = 0; i < analysis_.size(); i++) {

			// get type of analysis case
			int type = analysis_.get(i).getType();

			// for linear static analysis
			if (type == Analysis.linearStatic_) {

				// get linear static analysis
				LinearStatic ls = (LinearStatic) analysis_.get(i);

				// get solver name
				String sName = ls.getSolver().getName();

				// return true if assigned
				if (sName.equals(item))
					return true;
			}

			// for modal analysis
			else if (type == Analysis.modal_) {

				// get modal analysis
				Modal m = (Modal) analysis_.get(i);

				// get solver name
				String sName = m.getSolver().getName();

				// return true if assigned
				if (sName.equals(item))
					return true;
			}

			// for linear transient analysis
			else if (type == Analysis.linearTransient_) {

				// get linear transient analysis
				LinearTransient lt = (LinearTransient) analysis_.get(i);

				// get solver name
				String sName = lt.getSolver().getName();

				// return true if assigned
				if (sName.equals(item))
					return true;
			}

			// for linear buckling analysis
			else if (type == Analysis.linearBuckling_) {

				// get linear buckling analysis
				LinearBuckling lb = (LinearBuckling) analysis_.get(i);

				// get solver names
				String sName = lb.getSolver().getName();

				// return true if assigned
				if (sName.equals(item))
					return true;
			}
		}

		// no libraries assigned
		return false;
	}

	/**
	 * Checks whether selected item is last remaining one of its kind.
	 * 
	 * @param index
	 *            The index of selected item.
	 * @return True if not the last one, False if last.
	 */
	private boolean checkLast(int index) {

		// initialize variables
		int m = 0;

		// get the selected item
		Solver selected = temporary_.get(index);

		// check number of LE solvers
		for (int i = 0; i < temporary_.size(); i++) {
			if (temporary_.get(i).getProblemType() == selected.getProblemType())
				m++;
			if (m > 1)
				return true;
		}
		return false;
	}

	/**
	 * Sets temporary vector to input data vector. Sets dialog unvisible.
	 */
	private void actionOk() {

		// set analysis case vector to input data vector
		owner_.inputData_.setAnalysis(analysis_);

		// set temporary vector to input data vector
		owner_.inputData_.setSolver(temporary_);

		// set dialog unvisible
		setVisible(false);
	}
}
