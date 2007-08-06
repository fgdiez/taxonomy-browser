package org.eu_acgt.taxy.plugin.acgt_services;

import uk.ac.ebi.taxy.EasyDialog;
import javax.swing.*;
import java.awt.event.*;


/** This class defines a dialog for selecting the directory where
 * the NCBI flat files are located.
 */
public class DirectoryLocationDialog implements ActionListener
{
//    private JTextField _pathField;

    private JButton _okButton;

//    private JButton _cancelButton;

    private EasyDialog _dialog;

    private boolean _inputSubmitted;


    /** Constructs a new dialog with the specified parent frame
     * @param parentFrame Window that will be the parent of this dialog.
     */
    public DirectoryLocationDialog( JFrame parentFrame )
    {
        _dialog =
            new EasyDialog( parentFrame, true,
                            "ACGT Repo Services login",
                            "Just press OK to continue",
                            null );

//        _pathField = _dialog.addInputField( "DB directory", null, false );

        _okButton = _dialog.addButton( "Ok", this );

        _dialog.addButton( "Cancel", this );

        _inputSubmitted = false;
    }

    public void actionPerformed( ActionEvent event )
    {
        Object source = event.getSource();

        if( _okButton == source )
        {
            _inputSubmitted = true;
        }

        _dialog.setVisible( false );
    }

    /** Shows the dialog to the user.
     * @return <code>true</code> if the user selected a directory.
     *         <code>false</code> otherwise.
     */
    public boolean getInput()
    {
        _dialog.setVisible(true);

        boolean submitted = _inputSubmitted;

        _inputSubmitted = false;

        return submitted;
    }

    /** Gets the location of the directory selected by the user.
     */
//    public String getLocation()
//    {
//        return _pathField.getText();
//    }
}


