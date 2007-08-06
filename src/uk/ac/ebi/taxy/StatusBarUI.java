package uk.ac.ebi.taxy;


import javax.swing.*;


/**
 * This class defines the application's status bar. 
 */
public class StatusBarUI extends javax.swing.JTextArea
{
    /////////////////////////////////
    // Private Attributes
    /////////////////////////////////

    /**
     * text component.
     */
    private javax.swing.JTextArea _textArea;


    /**
     * text area container.
     */
    private javax.swing.JScrollPane _scrollPane;

    private StatusBarController _controller;

    ////////////////////////////////////
    // Public Operations
    ////////////////////////////////////

    /**
     * Constructs a new status bar with the specified initial text.
     * @param text Text to be shown initially.
     */
    public StatusBarUI( String text ) {
        
        super( text, 3, 80 );

        setLineWrap( true );

        setBorder( BorderFactory.createEtchedBorder() );

        setEditable( false );
        
        _controller = new StatusBarController( this );
    }

    /** Returns the controller of this UI.
     */
    public StatusBarController getController()
    {
        return _controller;
    }

    /** It clears the content of the status bar.
     */
    public void clearView()
    {
        setText( "" );
    }
}

