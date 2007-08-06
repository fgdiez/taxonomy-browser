package uk.ac.ebi.taxy;

import uk.ac.ebi.util.Debug;

import java.util.*;




/**
 * General purpose parser of command-line arguments.
 * It allows to check for the presence of an option 
 * and query the value associated to an option.
 */
public class ArgumentParser
{
    //////////////////////////////
    // Private Attributes
    //////////////////////////////
   
    /**
     * Data structure to asociate options and values.
     */
    private HashMap _options = new HashMap();


    //////////////////////////////
    // Public Operations
    //////////////////////////////
    
    /**
     * Constructs a new argument parser for a given array of arguments.
     * 
     * @param args   command-line arguments to be parsed.
     */
    public ArgumentParser( String[] args ) 
    {
        parseArguments( args );
    }


    /**
     * Checks for a given option.
     * 
     * @param option The option to be checked.
     * @return <code>true</code> if the option is present 
     *         on the command-line arguments. <code>false</code> otherwise.
     */
    public boolean hasOption( String option ) 
    {
        return _options.containsKey( option );
    }


    /**
     * Checks for the value of a given option.
     * 
     * @param option option to be checked.
     * @return <code>true</code> if the option has an associated value.
     *         <code>false</code> otherwise.
     */
    public boolean hasValue( String option ) 
    {
        Object content = _options.get( option );

        return content != null;
    }


    /**
     * Gets the value associated to a given option.
     * 
     * @return Either the value associated with a given option 
     *         or <code>null</code> if there is no value associated with it.
     */
    public String getValue( String option ) 
    {
        String content = (String) _options.get( option );

        return content;
    }


    /**
     * Checks the sintax of a token and determines if it is an option.
     * It is an option if the first character is a '-'.
     * 
     * @param token  A sequence of characters with no white spaces.
     * @return <code>true</code> if the token is an option.
     */
    private boolean isOption( String token ) 
    {
        if( (token == null) || (token.length() < 1) )
        {
            return false;
        }
        else if( token.charAt( 0 ) != '-' )
        {
            return false;
        }

        return true;
    }


    /**
     * Parse the command-line arguments and generate an internal
     * easy-to-consult representation.
     */
    private void parseArguments( String args[] ) 
    /* Initializes _options */
    {
        int argsSize = args.length;

        for( int i = 0; i < argsSize; i++ ) 
        {
            String argument = args[ i ];
            
            if( isOption( argument ) ) 
            {
                String option = argument;

                String value = null;

                if( (i + 1) < argsSize ) 
                {
                    argument = args[ i + 1 ];

                    if( isOption( argument ) == false ) 
                    {
                        value = argument;
                        i++;
                    }
                }
                Debug.TRACE( "Option=" + option + " Value=" + value );
                _options.put( option, value );
            }
        }
    }
}
