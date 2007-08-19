package uk.ac.ebi.taxy;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * This class defines a flexible dialog with some functions that ease the
 * construction of tailored dialogs.
 */
@SuppressWarnings("serial")
public class EasyDialog extends javax.swing.JDialog {

   private java.awt.Frame _parent;

//   private javax.swing.JButton _cancelButton;

   private javax.swing.JLabel _iconLabel;

   private javax.swing.JTextArea _text;

   private javax.swing.Box _buttonRegion;

   private javax.swing.Box _messageRegion;

   private javax.swing.Box _inputFieldRegion;

   private javax.swing.Box _inputLabelRegion;

   private javax.swing.Box _centralRegion;

   // /////////////////////////////
   // Public Operations
   // /////////////////////////////

   /**
    * Constructs a new dialog with the specified frame parent, modal mode,
    * title, message, and icon
    */
   public EasyDialog( java.awt.Frame parent, boolean isModal, String title, String message, String iconPath) {

      // non-modal dialog
      super(parent, title, isModal);

      _parent = parent;

      // setDefaultCloseOperation(
      // javax.swing.JDialog.DO_NOTHING_ON_CLOSE );

      // dialog's message region composition

      _messageRegion = new javax.swing.Box(javax.swing.BoxLayout.X_AXIS);

      // icon composition

      if (iconPath != null) {
         javax.swing.ImageIcon icon = new javax.swing.ImageIcon(iconPath);

         _iconLabel = new javax.swing.JLabel(icon);

         _messageRegion.add(_iconLabel);
      }

      // text composition

      _text = new javax.swing.JTextArea(message);
      // _text = new javax.swing.JEditorPane( "text/plain", message );

      _text.setEditable(false);

      _messageRegion.add(_text);

      // dialog's south region composition

      _buttonRegion = new javax.swing.Box(javax.swing.BoxLayout.X_AXIS);

      // we use JPanel for central alignment of _buttonRegion

      javax.swing.JPanel panel = new javax.swing.JPanel();

      panel.add(_buttonRegion);

      getContentPane().add(panel, java.awt.BorderLayout.SOUTH);

      // dialog's input region composition

      javax.swing.JPanel inputPanel = new javax.swing.JPanel();

      _inputFieldRegion = javax.swing.Box.createVerticalBox();

      _inputLabelRegion = javax.swing.Box.createVerticalBox();

      inputPanel.add(_inputFieldRegion, java.awt.BorderLayout.CENTER);

      inputPanel.add(_inputLabelRegion, java.awt.BorderLayout.CENTER);

      _centralRegion = javax.swing.Box.createVerticalBox();

      _centralRegion.add(_messageRegion);

      _centralRegion.add(inputPanel);

      getContentPane().add(_centralRegion, java.awt.BorderLayout.CENTER);

      pack();

      centerWindow();
   }

   /**
    * Sets the content of the central region of the dialog.
    */
   public void setCentralRegion( java.awt.Container c) {

      getContentPane().remove(_centralRegion);
      getContentPane().add(c, java.awt.BorderLayout.CENTER);
   }

   /**
    * Adds a button to the button region of the dialog.
    */
   public JButton addButton( String buttonName, ActionListener listener)

   {

      javax.swing.JButton button = new javax.swing.JButton(buttonName);

      button.addActionListener(listener);

      _buttonRegion.add(button);

      pack();

      return button;
   }

   /**
    * Adds an input field to the input field region of the dialog.
    */
   public JTextField addInputField( String inputName, ActionListener listener, boolean isPasswordField) {

      javax.swing.JLabel label = new javax.swing.JLabel(inputName);

      _inputLabelRegion.add(label);

      javax.swing.JTextField inputField = null;

      if (isPasswordField) {
         inputField = new javax.swing.JPasswordField(10);

         ((javax.swing.JPasswordField) inputField).setEchoChar('*');
      }
      else {
         inputField = new javax.swing.JTextField(10);
      }

      if (listener != null) {
         inputField.addActionListener(listener);
      }

      _inputFieldRegion.add(inputField);

      pack();

      return inputField;
   }

   /**
    * Shows the dialog centered on the screen.
    */
   public void setVisible( boolean visible) {

      centerWindow();

      super.setVisible(visible);
   }

   /**
    * Centers the dialog on the screen.
    */
   public void centerWindow() {

      java.awt.Point location = null;

      java.awt.Dimension parentDim = null;

      if (_parent != null) {
         location = _parent.getLocation();

         parentDim = _parent.getSize();
      }
      else {
         location = new java.awt.Point(0, 0);

         parentDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
      }

      java.awt.Dimension dim = getSize();

      int offsetX = (parentDim.width - dim.width) / 2;

      int offsetY = (parentDim.height - dim.height) / 2;

      setLocation(location.x + offsetX, location.y + offsetY);
   }
}
