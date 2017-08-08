/*
 * Created on 11 Feb 2007
 *
 */
package uk.ac.ebi.taxy.plugin.ncbitaxa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import uk.ac.ebi.taxy.TaxonProperty;
import uk.ac.ebi.taxy.TaxonProxy;
import uk.ac.ebi.taxy.TaxonomyPlugin;
import uk.ac.ebi.util.Debug;
import uk.ac.ebi.util.Table;

public class NcbiPlugin implements TaxonomyPlugin {

   private static final String SYNONYMS_FILENAME = "names.dmp";
   private static final String DIALOG_TITLE = "Select the *directory* containing NCBI Taxonomy (unziped) flat files";

   static final String[] propertyNames = { "ID", "Scientific Name", "Rank", "Genetic Code ID",
         "Mitochondrial Genetic Code ID", "Synonyms" };

   private static final List<String> propertyNameList = Arrays.asList(propertyNames);

   private static final String[] synonymsColumnNames = new String[] { "Name", "Name Category" };

   private Hashtable<Integer, NcbiTaxon> taxa;
   protected JFrame parentFrame;

   public boolean connect() {

      JFileChooser chooser = new JFileChooser(new java.io.File("."));
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setDialogTitle(DIALOG_TITLE);
      int result = chooser.showOpenDialog(parentFrame);

      if (result != JFileChooser.APPROVE_OPTION) {
         return false;
      }
      java.io.File selectedFile = chooser.getSelectedFile();

      Debug.ASSERT(selectedFile != null, "Null selected file");

      taxa = new Hashtable<Integer, NcbiTaxon>(2000000,1f);
      boolean success = parseFiles(selectedFile);

      if (success) {
         JOptionPane.showMessageDialog(parentFrame, "Loaded taxa: " + taxa.size(), "Taxonomy loaded successfully",
               JOptionPane.INFORMATION_MESSAGE);
      }
      return success;
   }

   public boolean disconnect() {
      taxa = null;
      return true;
   }

   public void setParentFrame(JFrame parentFrame) {
      this.parentFrame = parentFrame;
   }

   public TaxonProxy getRoot() {

      return getTaxonProxy("1");
   }

   public TaxonProxy getTaxonProxy(String taxonID) {
      NcbiTaxon taxon = getTaxon(Integer.valueOf(taxonID));
      if (taxon == null) {
         return null;
      }
      return new NCBITaxonProxy("" + taxon.id, taxon.getScientificName(), this);
   }

   public boolean hasChildren(Integer taxonID) {
      return getTaxon(taxonID).children != null;
   }

   public List<TaxonProxy> getChildren(Integer taxonID) {

      NcbiTaxon[] children = getTaxon(taxonID).children;
      if (children == null)
         return new ArrayList<TaxonProxy>(0);

      List<TaxonProxy> resultChildren = new ArrayList<TaxonProxy>(children.length);
      for (int i = 0; i < children.length; i++) {
         NcbiTaxon child = children[i];
         TaxonProxy resultChild = new NCBITaxonProxy("" + child.id, child.getScientificName(), this);
         resultChildren.add(resultChild);
      }
      return resultChildren;
   }

   public Integer getParentID(Integer taxonID) {

      NcbiTaxon taxon = getTaxon(taxonID);
      if (taxon.parentID == 0) {
         return null;
      }
      return taxon.parentID;
   }

   public TaxonProperty getProperty(Integer id, String propertyName) {
      TaxonProperty p = null;

      NcbiTaxon taxon = getTaxon(id);

      // ID
      if (propertyName.equals(propertyNames[0])) {
         p = new TaxonProperty(propertyName, "" + taxon.id);
      }
      // Scientific Name
      else if (propertyName.equals(propertyNames[1])) {
         p = new TaxonProperty(propertyName, taxon.getScientificName());
      }
      // Rank
      if (propertyName.equals(propertyNames[2])) {
         p = new TaxonProperty(propertyName, taxon.getRank());
      }
      // Genetic Code ID
      else if (propertyName.equals(propertyNames[3])) {
         p = new TaxonProperty(propertyName, "" + taxon.geneticCodeID);
      }
      // Mitochondrial Genetic Code ID
      else if (propertyName.equals(propertyNames[4])) {
         p = new TaxonProperty(propertyName, "" + taxon.mitochondrialGeneticCodeID);
      }
      // Synonyms
      else if (propertyName.equals(propertyNames[5])) {
         String[][] synonyms = new String[taxon.names.length][synonymsColumnNames.length];
         for (int i = 0; i < synonyms.length; ++i) {
            synonyms[i][0] = taxon.names[i];
            synonyms[i][1] = NcbiTaxon.getNameType(taxon.nameTypes[i]);
         }
         Table t = new Table(synonymsColumnNames, synonyms);
         p = new TaxonProperty(propertyName, t);
      }
      return p;
   }

   public List<String> getPropertyNames() {
      return propertyNameList;
   }

   public List<TaxonProxy> getTaxaByName(String expression) {

      // tokenize words in search expression
      StringTokenizer tokenizer = new StringTokenizer(expression, " \t/_-*");
      String[] words = new String[tokenizer.countTokens()];
      for (int i = 0; tokenizer.hasMoreElements(); ++i) {
         words[i] = tokenizer.nextToken().toLowerCase();
      }

      List<TaxonProxy> matchedTaxa = new ArrayList<TaxonProxy>();
      for (Enumeration<NcbiTaxon> taxonIter = taxa.elements(); taxonIter.hasMoreElements();) {
         NcbiTaxon taxon = taxonIter.nextElement();
         if (taxon.names == null)
            continue;

         for (int i = 0; i < taxon.names.length; i++) {
            String s = taxon.names[i].toLowerCase();
            boolean isAMatch = true;
            for (int j = 0; j < words.length; ++j) {
               if (s.indexOf(words[j]) < 0) {
                  isAMatch = false;
                  break;
               }
            }
            if (isAMatch) {
               matchedTaxa.add(new NCBITaxonProxy("" + taxon.id, taxon.names[i], this));
            }
         }
      }
      return matchedTaxa;
   }

   // public List<TaxonProxy> getTaxaByName(String name) {
   //
   // ArrayList<NcbiTaxon> matchedTaxa = new ArrayList<NcbiTaxon>();
   // for (Enumeration<NcbiTaxon> taxonIter = taxa.elements(); taxonIter
   // .hasMoreElements();) {
   // NcbiTaxon taxon = taxonIter.nextElement();
   // if (taxonNamesMatch(taxon, name)) {
   // matchedTaxa.add(taxon);
   // }
   // }
   // List<TaxonProxy> result = new ArrayList<TaxonProxy>(matchedTaxa.size());
   // for (int i = 0; i < result.size(); ++i) {
   // NcbiTaxon taxon = matchedTaxa.get(i);
   // result.add( new NCBITaxonProxy(""+taxon.id, taxon.getScientificName(),
   // this));
   // }
   // return result;
   // }

   private boolean parseFiles(File path) {

      try {
         parseTaxaNodes(path);

         parseSynonyms(path);

         // taxa.trimToSize();

         return true;
      } catch (java.io.IOException ex) {
         Debug.ERROR(ex.getMessage());

         JOptionPane.showMessageDialog(null, "Error occured when parsing NCBI taxonomy data.\n"
               + "Error description:\n" + ex.toString(), "Parser error", JOptionPane.ERROR_MESSAGE);

         return false;
      }
   }

   private void parseTaxaNodes(File directoryPath) throws IOException {
      File nodesFile = new File(directoryPath, "nodes.dmp");

      BufferedReader in = new BufferedReader(new FileReader(nodesFile));

      String[] taxonTokens = new String[9];

      for (String line = in.readLine(); line != null; line = in.readLine()) {
         NcbiTaxon taxon = parseTaxonLine(line, taxonTokens);

         taxa.put(taxon.id, taxon);
      }

      in.close();

      Hashtable<Integer, ArrayList<NcbiTaxon>> childrenHash = new Hashtable<Integer, ArrayList<NcbiTaxon>>(taxa.size(),1.0f);

      // for( int i = 0; i < taxa.size(); ++i) {
      Enumeration<NcbiTaxon> taxonIter = taxa.elements();
      while (taxonIter.hasMoreElements()) {

         NcbiTaxon taxon = taxonIter.nextElement();

         if (taxon.parentID == 0) {
            continue;
         }

         ArrayList<NcbiTaxon> children = childrenHash.get(taxon.parentID);
         if (children == null) {
            children = new ArrayList<NcbiTaxon>();
            childrenHash.put(taxon.parentID, children);
         }
         children.add(taxon);
      }

      Enumeration<Integer> parentIdIter = childrenHash.keys();
      while (parentIdIter.hasMoreElements()) {
         Integer parentId = parentIdIter.nextElement();
         NcbiTaxon taxon = taxa.get(parentId);
         ArrayList<NcbiTaxon> children = childrenHash.get(parentId);
         taxon.children = new NcbiTaxon[children.size()];
         children.toArray(taxon.children);
      }
      childrenHash.clear();
      childrenHash = null;
   }

   private NcbiTaxon parseTaxonLine(String line, String[] tokens) {
      getTokens(line, tokens);

      // Debug.ASSERT( tokens.length == 13,
      // "Unexpected number of fields in a node : " + tokens.length );

      NcbiTaxon taxon = new NcbiTaxon();
      taxon.id = Integer.parseInt(tokens[0]);
      taxon.parentID = Integer.parseInt(tokens[1]);
      if (taxon.parentID == taxon.id) {
         // must be the root node
         taxon.parentID = 0;
      }

      taxon.setRank(tokens[2]);

      taxon.setGeneticCodeId(tokens[6]);

      taxon.setMitochondrialGeneticCodeId(tokens[8]);

      return taxon;
   }

   private void getTokens(String line, String[] tokens) {
      String delimiter = "\t|";

      int i = 0;
      for (int tokenIndex = 0; tokenIndex < tokens.length; ++tokenIndex) {
         int j = line.indexOf(delimiter, i);

         Debug.ASSERT(j >= 0, "Delimiter not found");

         String token = line.substring(i, j);

         tokens[tokenIndex] = token;

         i = j + 3; // 3 == delimiter.length() + 1
      }
   }

   private void parseSynonyms(java.io.File directoryPath) throws java.io.IOException {

      File synonymsFile = new File(directoryPath, SYNONYMS_FILENAME);

      BufferedReader in = new BufferedReader(new FileReader(synonymsFile));

      String[] tokens = new String[4];

      ArrayList<String> names = new ArrayList<String>();
      ArrayList<Integer> nameTypes = new ArrayList<Integer>();

      int taxonId = 1;
      int scientificNameIndex = 0;

      for (String line = in.readLine(); line != null; line = in.readLine()) {
         getTokens(line, tokens);
         int currentTaxonId = Integer.parseInt(tokens[0]);
         String name = tokens[1];
         String nameType = tokens[3];

         if (currentTaxonId != taxonId) {

            NcbiTaxon taxon = getTaxon(taxonId);
            taxon.scientificNameIndex = scientificNameIndex;
            taxon.names = names.toArray(new String[names.size()]);
            taxon.nameTypes = new int[nameTypes.size()];
            for (int i = 0; i < nameTypes.size(); ++i) {
               taxon.nameTypes[i] = nameTypes.get(i);
            }

            taxonId = currentTaxonId;
            names.clear();
            nameTypes.clear();
         }

         if (nameType.equals("scientific name")) {
            scientificNameIndex = names.size();
         }
         names.add(name);
         nameTypes.add(NcbiTaxon.getNameTypeId(nameType));

      }
      in.close();
   }

   private NcbiTaxon getTaxon(int id) {

      return taxa.get(id);
   }

   // private boolean taxonNamesMatch(NcbiTaxon taxon, String regex) {
   // Pattern p = Pattern.compile(regex.toLowerCase());
   // if (taxon.names == null) {
   // return false;
   // }
   //
   // for (int i = 0; i < taxon.names.length; ++i) {
   // Matcher m = p.matcher(taxon.names[i].toLowerCase());
   // if (m.find()) {
   // return true;
   // }
   // }
   // return false;
   // }
}
