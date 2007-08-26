/*
 * Created on 11 Feb 2007
 *
 */
package uk.ac.ebi.taxy.plugin.ncbitaxa;

import java.util.Hashtable;

public class NcbiTaxon {

   public static Hashtable<String, Integer> rankIds = new Hashtable<String, Integer>(20);
   public static Hashtable<Integer, String> rankNames = new Hashtable<Integer, String>(20);

   public static Hashtable<String, Integer> nameTypeIds = new Hashtable<String, Integer>(20);
   public static Hashtable<Integer, String> nameTypeNames = new Hashtable<Integer, String>(20);

   int id;

   int parentID;

   private int rank;

   int geneticCodeID;

   int mitochondrialGeneticCodeID;

   NcbiTaxon[] children;

   String[] names;

   int[] nameTypes;

   int scientificNameIndex;

   public void setRank(String rankStr) {
      if (rankIds.containsKey(rankStr)) {
         rank = rankIds.get(rankStr);
      } else {
         rank = rankIds.size();
         rankNames.put(rank, rankStr);
         rankIds.put(rankStr, rank);
      }
   }

   public String getRank() {
      return rankNames.get(rank);
   }

   public static int getNameTypeId(String type) {

      if (nameTypeIds.containsKey(type)) {
         return nameTypeIds.get(type);
      } else {
         int typeId = nameTypeIds.size();
         nameTypeNames.put(typeId, type);
         nameTypeIds.put(type, typeId);
         return typeId;
      }
   }

   public static String getNameType(int typeId) {
      return nameTypeNames.get(typeId);
   }

   public void setGeneticCodeId(String id) {
      geneticCodeID = Integer.parseInt(id);
   }

   public void setMitochondrialGeneticCodeId(String id) {
      mitochondrialGeneticCodeID = Integer.parseInt(id);
   }

   public String getScientificName() {
      return names[scientificNameIndex];
   }
}
