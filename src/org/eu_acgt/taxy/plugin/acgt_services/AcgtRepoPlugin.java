package org.eu_acgt.taxy.plugin.acgt_services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eu_acgt.repo.client.RepoException;
import org.eu_acgt.repo.client.RepoPersistenceWSClient;
import org.eu_acgt.repo.model.DataType;
import org.eu_acgt.repo.model.FunctionalCategory;
import org.eu_acgt.repo.model.Service;
import org.eu_acgt.repo.model.TypeTaxon;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;

/**
 * This plug-in allows to connect to and navigate through the hierarchy of
 * services available in ACGT Metadata Repository.
 * 
 */
public class AcgtRepoPlugin implements TaxonomyPlugin {

	RepoPersistenceWSClient repo;

	TaxonProxy root;

	HashMap< String, TaxonProxy> taxa = new HashMap< String, TaxonProxy>();

	Logger logger = Logger.getLogger( "AcgtRepoPlugin");

	static final String ERROR_MESSAGE = "Error connecting to ACGT metadata repository.\n\nError message: ";

	public boolean connect() {

		try {
			logger.info( "connecting..");
			root = new TaxonProxy( "urn:lsid:eu_acgt.org:repo:root", "RepoRoot", this) {

                ImageIcon ICON = new ImageIcon(TaxonProxy.class.getClassLoader().getResource("root.png"));
				List< TaxonProxy> children = new ArrayList< TaxonProxy>( 2);

				@Override
				public List< TaxonProxy> getChildren() {

					return children;
				}

				@Override
				public TaxonProxy getParent() {

					return null;
				}

				@Override
				public TaxonProperty getProperty( String propertyName) {

					return null;
				}

				@Override
				public List< String> getPropertyNames() {

					return new ArrayList< String>( 0);
				}

				@Override
				public boolean hasChildren() {

					return children.size() > 0;
				}

				@Override
				public ImageIcon getIcon() {
					return ICON;
				}
				
				@Override
				public String getTaxonTitle() {
					return "Root Element";
				}
			};
			taxa.put( root.getID(), root);

			
			repo = new RepoPersistenceWSClient( "http://mango.ac.uma.es/axis2_devel/services/RepoPersistence");
			FunctionalCategory rootServiceCat = 
				repo.retrieveFunctionalCategory( "urn:lsid:biomoby.org:servicetype:Service");

			ServiceCategoryTaxon rootServiceCatTaxon = createCategoryTaxon( rootServiceCat, null);
			if(rootServiceCatTaxon != null) {
				taxa.put( rootServiceCatTaxon.getID(), rootServiceCatTaxon);
				addAllChildren( taxa, rootServiceCatTaxon);
				root.getChildren().add(rootServiceCatTaxon);
				rootServiceCatTaxon.setParent(root);
			}
			
			TypeTaxon rootDataTypeCat = 
				repo.retrieveTypeTaxon( "urn:lsid:biomoby.org:objectclass:Object");
			
			DataTypeCategoryTaxon rootDataTypeCatTaxon = createCategoryTaxon( rootDataTypeCat, null);
			if(rootDataTypeCatTaxon != null) {
				taxa.put( rootDataTypeCatTaxon.getID(), rootDataTypeCatTaxon);
				addAllChildren( taxa, rootDataTypeCatTaxon);
				root.getChildren().add(rootDataTypeCatTaxon);
				rootDataTypeCatTaxon.setParent(root);
			}
			
			logger.info( "Taxonomy elements: " + taxa.size());
			
			return true;
		}
		catch (org.apache.axis2.AxisFault e) {
			JOptionPane.showMessageDialog( null, getErrorMessage( e), "Connection error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		}
		catch (org.eu_acgt.repo.client.RepoException e) {
			JOptionPane.showMessageDialog( null, getErrorMessage( e), "Connection error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		}
	}

	String getErrorMessage( Throwable e) {

		String message = e.getMessage();
		while (e.getCause() != null) {
			e = e.getCause();
		}
		return ERROR_MESSAGE + message + "\nError cause: " + e.getClass().getSimpleName() + ": " + e.getMessage();
	}

	void addAllChildren( HashMap< String, TaxonProxy> taxa, ServiceCategoryTaxon parent) {

		ArrayList< TaxonProxy> children = getChildren( parent);
		parent.setChildren( children);

		for( Iterator< TaxonProxy> childIter = children.iterator(); childIter.hasNext();) {
			TaxonProxy child = childIter.next();
			taxa.put( child.getID(), child);
			if( ServiceCategoryTaxon.class.isInstance( child)) {
				addAllChildren( taxa, (ServiceCategoryTaxon) child);
			}
		}
	}

	ArrayList< TaxonProxy> getChildren( ServiceCategoryTaxon parentTaxon) {

		ArrayList< TaxonProxy> children = new ArrayList< TaxonProxy>();
		FunctionalCategory parent = parentTaxon.cat;
		try {
			List< Service> svcs = repo.retrieveServiceByFunctionalCategory( parent.getUriId());
			for( Iterator< Service> svcIter = svcs.iterator(); svcIter.hasNext();) {
				Service svc = svcIter.next();
				children.add( createServiceTaxon( svc, parentTaxon));
			}
		}
		catch (RepoException e) {
			logger.info( e.getMessage());
		}

		for( Iterator< FunctionalCategory> childIter = parent.getChildren().iterator(); childIter.hasNext();) {
			FunctionalCategory cat = childIter.next();
			TaxonProxy childTaxon = createCategoryTaxon( cat, parentTaxon);
			children.add( childTaxon);
		}
		return children;
	}

	ServiceCategoryTaxon createCategoryTaxon( FunctionalCategory cat, TaxonProxy parent) {

		if( cat == null) return null;
		return new ServiceCategoryTaxon( cat, parent, cat.getUriId(), cat.getName(), this);
	}

	ServiceTaxon createServiceTaxon( Service svc, TaxonProxy parent) {

		if( svc == null) return null;
		return new ServiceTaxon( svc, parent, svc.getUriId(), svc.getName(), this);
	}

	void addAllChildren( HashMap< String, TaxonProxy> taxa, DataTypeCategoryTaxon parent) {

		ArrayList< TaxonProxy> children = getChildren( parent);
		parent.setChildren( children);

		for( Iterator< TaxonProxy> childIter = children.iterator(); childIter.hasNext();) {
			TaxonProxy child = childIter.next();
			taxa.put( child.getID(), child);
			if( DataTypeCategoryTaxon.class.isInstance( child)) {
				addAllChildren( taxa, (DataTypeCategoryTaxon) child);
			}
		}
	}

	ArrayList< TaxonProxy> getChildren( DataTypeCategoryTaxon parentTaxon) {

		ArrayList< TaxonProxy> children = new ArrayList< TaxonProxy>();
		TypeTaxon parent = parentTaxon.cat;
		try {
			List< DataType> types = repo.retrieveDataTypeByTypeTaxon( parent.getUriId());
			for( Iterator< DataType> typeIter = types.iterator(); typeIter.hasNext();) {
				DataType svc = typeIter.next();
				children.add( createDataTypeTaxon( svc, parentTaxon));
			}
		}
		catch (RepoException e) {
			logger.info( e.getMessage());
		}

		for( Iterator< TypeTaxon> childIter = parent.getChildren().iterator(); childIter.hasNext();) {
			TypeTaxon cat = childIter.next();
			TaxonProxy childTaxon = createCategoryTaxon( cat, parentTaxon);
			children.add( childTaxon);
		}
		return children;
	}

	DataTypeCategoryTaxon createCategoryTaxon( TypeTaxon cat, TaxonProxy parent) {

		if( cat == null) return null;

		return new DataTypeCategoryTaxon( cat, parent, cat.getUriId(), cat.getName(), this);
	}

	DataTypeTaxon createDataTypeTaxon( DataType svc, TaxonProxy parent) {

		if( svc == null) return null;
		return new DataTypeTaxon( svc, parent, svc.getUriId(), svc.getName(), this);
	}

	public boolean disconnect() {

		taxa = null;
		return true;
	}

	// FunctionalCategory findParent(Collection<FunctionalCategory> elements,
	// int
	// childId) {
	// for(Iterator<FunctionalCategory> parentIter = elements.iterator();
	// parentIter.hasNext(); ){
	// FunctionalCategory parent = parentIter.next();
	// for(Iterator<FunctionalCategory> childIter =
	// parent.getChildren().iterator(); childIter.hasNext(); ) {
	// FunctionalCategory child = childIter.next();
	// if( child.getId() == childId) {
	// return parent;
	// }
	// }
	// }
	// return null;
	// }

	// public TaxonProperty getProperty( Integer taxonID, String propertyName) {
	// return taxa.get( taxonID).getProperty( propertyName);
	// }

	public List< TaxonProxy> getTaxaByName( String name) {

		ArrayList< TaxonProxy> result = new ArrayList< TaxonProxy>();
		for( Iterator< TaxonProxy> taxonIter = taxa.values().iterator(); taxonIter.hasNext();) {
			TaxonProxy taxon = taxonIter.next();
			if( matchText( name.toLowerCase(), taxon.getName().toLowerCase())) {
				result.add( taxon);
			}
		}
		return result;
	}

	private boolean matchText( String expr, String text) {

		if( text.indexOf( expr) >= 0) return true;
		return false;
	}

	public TaxonProxy getTaxonProxy( String taxonID) {

		return taxa.get( taxonID);
	}

	public boolean hasChildren( String taxonID) {

		TaxonProxy element = taxa.get( taxonID);
		if( element == null) return false;

		return element.getChildren().size() > 0;
	}

	public void setParentFrame( JFrame parent) {

	}

	public TaxonProxy getRoot() {

		return root;
	}

}
