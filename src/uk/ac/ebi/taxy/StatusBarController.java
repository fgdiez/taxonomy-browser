package uk.ac.ebi.taxy;


/**
 * This class is the controller for <code>StatusBarUI</code>.
 */
public class StatusBarController
{
    //////////////////////////
    // Private Attributes
    //////////////////////////

    private StatusBarUI _ui;

    /**
     * Default delay for writing delayed text of the status bar.
     */
    private final int DEFAULT_DELAY = 4; // default seconds of delay


    //////////////////////////
    // Public Operations
    //////////////////////////
    
    /** Creates a new status bar controller that operates over the
     * specified UI.
     * @param ui The status bar over which this controller operates.
     */
    public StatusBarController( StatusBarUI ui )
    {
        _ui = ui;
    }

    /** Write text on the status bar.
     */
    public void setText( String text )
    {
        _ui.setText( text );
    }

    /**
     * Display a transient text on the status bar. The specified text
     * will be shown in the status bar just for a few seconds.
     * 
     * @param transientText Text to be displayed.
     */
    public void setTransientText( String transientText ) 
    {
        String previousText = _ui.getText();

        _ui.setText( transientText );

        setDelayedText( previousText );
    }


    /**
     * Writes in the status bar after a default delay.
     * 
     * @param text   text to be written.
     */
    public void setDelayedText( String text ) 
    {
        setDelayedText( text, DEFAULT_DELAY );
    }


    /**
     * It writes in the status bar the specified text after the specified seconds.
     * 
     * @param text Text to be written.
     * @param delay  Seconds to wait before writting the text.
     */
    public void setDelayedText( String text, int delay ) 
    {
        DelayedWriter delayedWriter = new DelayedWriter( _ui );

        delayedWriter.printDelayedText( text, delay );
    }


    /** This class is allows to write in a status bar with a delay.
     */
    class DelayedWriter extends java.util.TimerTask
    {
        /**
         * status bar where a text will be written.
         */
        private StatusBarUI _statusBar;

        /**
         * the text to be written on the status bar.
         */
        private String _text;


        /**
         * Constructs a new delayed writer that will operate on the
	 * specified status bar.
         */
        DelayedWriter( StatusBarUI statusBar ) 
        {
            _statusBar = statusBar;
        }


        /**
         * Prints on the status bar a given text with a given delay.
         * 
         * @param text   text to be printed.
         * @param delay  delay for writting the text on the status bar.
         */
        void printDelayedText( String text, int delay ) 
        {
            _text = text;

            java.util.Date date = getDelayedDate( delay );

            java.util.Timer timer = new java.util.Timer();

            timer.schedule( this, date );
        }


        /**
         * Writes on the status bar.
         * 
         * @see java.util.TimerTask.run
         */
        public void run() 
        {
            _statusBar.setText( _text );
        }

        /**
         * Get a new future Date.
         * 
         * @param seconds seconds in the future of the date to be returned.
         * @return A java.util.Date of the future next given seconds.
         */
        private java.util.Date getDelayedDate( int delay ) 
        {
            java.util.Calendar calendar = java.util.Calendar.getInstance();

            calendar.add( java.util.Calendar.SECOND, delay );

            java.util.Date date = calendar.getTime();

            return date;
        }
    }

}
