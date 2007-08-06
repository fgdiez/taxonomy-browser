package uk.ac.ebi.taxy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import uk.ac.ebi.util.Debug;

/** This class loads the definitions of the available plug-ins.
 * It searches for implementations of <code>TaxonomyPlugin</code> and 
 * <code>PluginDescription</code>.
 */
public class PluginLoader extends ClassLoader
{
    private HashMap<String, byte[]> _classes;

    /** Constructs a new loader to load plug-in definitions from the
     * specified JAR file.
     */
    public PluginLoader( java.io.File jarFile )
    {
        _classes = new HashMap< String, byte[]>();

        loadClasses( jarFile );
    }

    protected Class<?> findClass( String className )
       throws ClassNotFoundException
    {
        Debug.TRACE( "findClass( " + className + " )" );
        byte[] classBytes = ( byte[] ) _classes.get( className );

        if( classBytes == null )
        {
        	Debug.TRACE( "findClass: no bytes found for class " + className );
            throw new ClassNotFoundException( className );
        }

    	try
    	{
    	    Class<?> theClass = defineClass( className, classBytes, 0, classBytes.length );
    	    return theClass;
    	}
    	catch( NoClassDefFoundError ex )
    	{
    	    Debug.TRACE("No definition found for class " + className );
    	    throw new ClassNotFoundException();
    	}
    }


    /** Loads plug-ins definitions from the specified JAR file.
     */
    private void loadClasses( java.io.File jarFile )
    {
        try
        {
            ZipFile zin = new ZipFile( jarFile );
            
            int sufix = ".class".length();

            Enumeration<? extends ZipEntry> entries = zin.entries();

            while( entries.hasMoreElements() )
            {
                ZipEntry entry = entries.nextElement();

                String fileName = entry.getName();

                if( fileName.endsWith( ".class" ) )
                {
                    String className = fileName.substring( 0, fileName.length() - sufix );
                    className = className.replace( '/', '.' );

                    InputStream inStream = zin.getInputStream( entry );
                    
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream( (int)entry.getSize() );
                    
                    int c = 0;
                    while( ( c = inStream.read() ) != -1 )
                    {
                        outStream.write( c );
                    }

                    byte[] classBytes = outStream.toByteArray();
                    
                    _classes.put( className, classBytes );
                }
            }
            
            zin.close();
        }
        catch( java.lang.Exception ex )
        {
            ex.printStackTrace();
        }
    }

	@SuppressWarnings("unchecked")
	public Class<TaxonomyPlugin> getPluginClass()
    {
		return (Class<TaxonomyPlugin>) getFirstImplementationClass(TaxonomyPlugin.class);
    }
	
	@SuppressWarnings("unchecked")
	public Class<PluginDescription> getPluginDescriptionClass()
    {
		return (Class<PluginDescription>) getFirstImplementationClass(PluginDescription.class);
    }
	
    /** Returns the first class found which implements a given interface 
     * @param interfaceClass interface class
     */
	public Class<?> getFirstImplementationClass(Class<?> interfaceClass)
    {
        try
        {
            Iterator< String> classNames = _classes.keySet().iterator();

            while( classNames.hasNext() )
            {
                String className = classNames.next();
                Debug.TRACE( "findFirstImp: loadClass( " + className + " )" );
                Class<?> theClass = loadClass( className );

                if( interfaceClass.isAssignableFrom( theClass ) )
                {
                    int modifiers = theClass.getModifiers();

                    if( ! Modifier.isAbstract( modifiers ) )
                    {
                        Debug.TRACE( "SELECTED " + theClass.getName() );
                        return theClass;
                    }
                }
            }
            return null;
        }
        catch( java.lang.Exception ex )
        {
            Debug.TRACE( ex.getMessage() );
            return null;
        }
    }
}

