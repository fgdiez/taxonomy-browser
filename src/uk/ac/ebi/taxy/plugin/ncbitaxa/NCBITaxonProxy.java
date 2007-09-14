package uk.ac.ebi.taxy.plugin.ncbitaxa;

import java.util.List;

import javax.swing.ImageIcon;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;

public class NCBITaxonProxy extends TaxonProxy {

	NcbiPlugin ncbiTaxonomy;

	private static final ImageIcon BRANCH_ICON = new ImageIcon(TaxonProxy.class.getClassLoader().getResource("catalog.png"));
	private static final ImageIcon LEAF_ICON = new ImageIcon(TaxonProxy.class.getClassLoader().getResource("species.png"));
	
	public NCBITaxonProxy(String id, String name, NcbiPlugin taxonomyProvider) {
		super(id, name, taxonomyProvider);
		ncbiTaxonomy = taxonomyProvider;
	}

	@Override
	public List<TaxonProxy> getChildren() {
		return ncbiTaxonomy.getChildren(Integer.valueOf(getID()));
	}

	@Override
	public TaxonProxy getParent() {
		Integer parentID = ncbiTaxonomy.getParentID(Integer.valueOf(getID()));
		if( parentID == null) return null;
		return ncbiTaxonomy.getTaxonProxy(parentID.toString());
	}

	@Override
	public TaxonProperty getProperty(String propertyName) {
		return ncbiTaxonomy.getProperty(Integer.valueOf(getID()), propertyName);
	}

	@Override
	public List<String> getPropertyNames() {
		return ncbiTaxonomy.getPropertyNames();
	}

	@Override
	public boolean hasChildren() {
		return getChildren().size() > 0;
	}

	@Override
	public String getTaxonTitle() {
		return "Taxon Details";
	}

   @Override
   public ImageIcon getIcon() {
	   if(hasChildren()) {
		   return BRANCH_ICON;
	   }
	   else {
		   return LEAF_ICON;
	   }
   }
}
