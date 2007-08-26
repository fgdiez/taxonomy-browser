package uk.ac.ebi.taxy.plugin.ncbitaxa;

/** Describes the plug-in <code>NCBIPlugin</code>.
 *
 * @see uk.ac.ebi.taxy.plugin.ncbi_ff.NCBIPlugin
 */
public class NCBIPluginDescription
    implements uk.ac.ebi.taxy.PluginDescription
{
    private String _pluginClassName = NcbiPlugin.class.getName();

    public String getPluginClassName()
    {
        return _pluginClassName;
    }

    public String getName()
    {
        return "Slick Access to NCBI Taxonomy Flat-Files";
    }

    public String getDescription()
    {
        return
            "DESCRIPTION: \n"
            + "    Provides access to the flat-file distribution of "
            + "the NCBI Taxonomy Database. "
            + "This plug-in parses the flat-files distributed by NCBI "
            + "and loads the taxonomic information into memory, so that it "
            + "is ready to be queried. It requires to have the NCBI taxonomy "
            + "dump file downloaded and unziped somewhere locally in your computer.\n\n"
            + "PLUGIN CLASS:\n"
            + "    " + _pluginClassName + "\n\n";
    }
}

