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


import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Commons;
// import main.ImageHandler;

import boundary.BoundaryCase;

/**
 * Class for Add/Modify Boundary Cases menu.
 * 
 * @author Murat
 * 
 */
public class BoundaryCase2 extends JDialog implements ActionListener,
		FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	private BoundaryCase1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 */
	public BoundaryCase2(BoundaryCase1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Library", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Library", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Name :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(100, 20));

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button1);
		panel2.add(button2);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		textfield1_.addFocusListener(this);

		// If add is clicked set default, if not initialize
		if (add_)
			setDefaultText(new JTextField());
		else
			initialize();

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// get index of selected item in list of mother dialog
		int index = owner_.list1_.getSelectedIndex();

		// get the selected object
		BoundaryCase selected = owner_.temporary_.get(index);

		// get name
		String name = selected.getName();

		// set name
		textfield1_.setText(name);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getActionCommand() == "  OK  ") {

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
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// get name textfield to be checked
		JTextField textfield = new JTextField();
		textfield = textfield1_;

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield, 1)) {
				actionOkAddModify();
				setVisible(false);
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield.getText().equals(selected)) {
				actionOkAddModify();
				setVisible(false);
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield, 1)) {
					actionOkAddModify();
					setVisible(false);
				}
			}
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 */
	private void actionOkAddModify() {

		// get name
		String name = textfield1_.getText();

		// create object
		BoundaryCase object = new BoundaryCase(name);

		// add button clicked
		if (add_) {

			// add object to temporary vector and names to list
			owner_.temporary_.addElement(object);
			owner_.listModel1_.addElement(name);
		}

		// modify button clicked
		else if (add_ == false) {

			// set name to boundaries
			String item = owner_.list1_.getSelectedValue().toString();

			// check for prescribed displacements
			for (int i = 0; i < owner_.dispLoad_.size(); i++) {
				String bound = owner_.dispLoad_.get(i).getBoundaryCase()
						.getName();
				if (bound.equals(item))
					owner_.dispLoad_.get(i).setBoundaryCase(object);
			}

			// check for initial displacements
			for (int i = 0; i < owner_.initialDisp_.size(); i++) {
				String bound = owner_.initialDisp_.get(i).getBoundaryCase()
						.getName();
				if (bound.equals(item))
					owner_.initialDisp_.get(i).setBoundaryCase(object);
			}

			// check for initial velocities
			for (int i = 0; i < owner_.initialVelo_.size(); i++) {
				String bound = owner_.initialVelo_.get(i).getBoundaryCase()
						.getName();
				if (bound.equals(item))
					owner_.initialVelo_.get(i).setBoundaryCase(object);
			}

			// check for nodal mechanical loads
			for (int i = 0; i < owner_.nodalMechLoad_.size(); i++) {
				String bound = owner_.nodalMechLoad_.get(i).getBoundaryCase()
						.getName();
				if (bound.equals(item))
					owner_.nodalMechLoad_.get(i).setBoundaryCase(object);
			}

			// check for element mechanical loads
			for (int i = 0; i < owner_.elementMechLoad_.size(); i++) {
				String bound = owner_.elementMechLoad_.get(i).getBoundaryCase()
						.getName();
				if (bound.equals(item))
					owner_.elementMechLoad_.get(i).setBoundaryCase(object);
			}

			// check for element temperature loads
			for (int i = 0; i < owner_.elementTemp_.size(); i++) {
				String bound = owner_.elementTemp_.get(i).getBoundaryCase()
						.getName();
				if (bound.equals(item))
					owner_.elementTemp_.get(i).setBoundaryCase(object);
			}

			// check for constraints
			for (int i = 0; i < owner_.constraint_.size(); i++) {
				String bound = owner_.constraint_.get(i).getBoundaryCase()
						.getName();
				if (bound.equals(item))
					owner_.constraint_.get(i).setBoundaryCase(object);
			}

			// check for analysis cases
			for (int i = 0; i < owner_.analysis_.size(); i++) {

				// get boundary cases vector
				Vector<BoundaryCase> bc = owner_.analysis_.get(i)
						.getBoundaries();

				// loop over boundary cases array
				for (int j = 0; j < bc.size(); j++) {

					// set case if assigned
					if (bc.get(j).getName().equals(item))
						bc.setElementAt(object, j);
				}
			}

			// set object to temporary vector and names to list
			int index = owner_.list1_.getSelectedIndex();
			owner_.temporary_.setElementAt(object, index);
			owner_.listModel1_.setElementAt(name, index);
		}
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// get source and dependently set message type
				JTextField tfield = (JTextField) e.getSource();
				int messageType = 2;
				if (tfield.equals(textfield1_))
					messageType = 0;

				// check textfield
				if (checkText(tfield, messageType) == false)
					setDefaultText(tfield);
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * If false data has been entered displays message on screen.
	 * 
	 * @param textfield
	 *            The textfield that the false data has been entered.
	 * @param messageType
	 *            The type of message to be displayed (No name given -> 0, Name
	 *            exists -> 1, Illegal value -> 2).
	 * @return True if the data entered is correct, False if not.
	 */
	private boolean checkText(JTextField textfield, int messageType) {

		// boolean value for checking if data entered is correct or not
		boolean isCorrect = true;

		// get the entered text
		String text = textfield.getText();

		// No name given
		if (messageType == 0) {

			// check if no name given
			if (text.equals("")) {

				// display message
				JOptionPane.showMessageDialog(BoundaryCase2.this,
						"No name given!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(BoundaryCase2.this,
						"Name already exists!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value
		else if (messageType == 2) {

			// check for non-numeric values
			try {

				// convert text to double value
				@SuppressWarnings("unused")
				double value = Double.parseDouble(text);
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(BoundaryCase2.this,
						"Illegal value!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// the data is correct
		return isCorrect;
	}

	/**
	 * Sets default values for textfields.
	 * 
	 * @param textfield
	 *            The textfield to be set. If null is given, sets all
	 *            textfields.
	 */
	private void setDefaultText(JTextField textfield) {

		// The default values for textfields
		String defaultName = "Case1";

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to all
		else
			textfield1_.setText(defaultName);
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
