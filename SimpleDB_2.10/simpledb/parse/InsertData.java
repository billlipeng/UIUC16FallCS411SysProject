package simpledb.parse;

import simpledb.query.Constant;
import java.util.*;

/**
 * Data for the SQL <i>insert</i> statement.
 * @author Edward Sciore
 */
public class InsertData {
   private String tblname;
   private List<String> flds;
   private List<Constant> vals;
   private List<Constant> c;
   private List<Constant> nd;
   private List<Constant> nd1;
   private List<Constant> nd2;
   private List<Constant> length;
   
   /**
    * Saves the table name and the field and value lists.
    */
   public InsertData(String tblname, List<String> flds, List<Constant> vals) {
      this.tblname = tblname;
      this.flds = flds;
      this.vals = vals;
   }

   public InsertData(String tblname, List<String> flds, List<Constant> vals, List<Constant> c, List<Constant> nd, List<Constant> nd1, List<Constant> nd2, List<Constant> length) {
      this.tblname = tblname;
      this.flds = flds;
      this.vals = vals;
      this.c = c;
      this.nd = nd;
      this.nd1 = nd1;
      this.nd2 = nd2;
      this.length = length;
   }
   
   /**
    * Returns the name of the affected table.
    * @return the name of the affected table
    */
   public String tableName() {
      return tblname;
   }
   
   /**
    * Returns a list of fields for which
    * values will be specified in the new record.
    * @return a list of field names
    */
   public List<String> fields() {
      return flds;
   }
   
   /**
    * Returns a list of values for the specified fields.
    * There is a one-one correspondence between this
    * list of values and the list of fields.
    * @return a list of Constant values.
    */
   public List<Constant> vals() {
      return vals;
   }

   /**
    * Returns a new InsertData for the nodes.
    * @return nd.
    */

   public List<Constant> getTbl() { return c;}
   public List<Constant> getNode() { return nd;}
   public List<Constant> getNode1() { return nd1;}
   public List<Constant> getNode2() { return nd2;}
   public List<Constant> getLength() { return length;}
//   public InsertData nodes() { return nd; }

//   /**
//    * Saves the table name and the field and value lists of nodes.
//    */
//   public int InsertNode(String tblname, List<String> flds, List<Constant> vals) {
//      nd=new InsertData(tblname, flds, vals);
//      return 1;
//   }
//
//   /**
//    * Saves the table name and the field and value lists of edges.
//    */
//   public int InsertEdge(String tblname, List<String> flds, List<Constant> vals) {
//      edg=new InsertData(tblname, flds, vals);
//      return 1;
//   }

//   /**
//    * Return True if nd or edg is not null.
//    * @return true if nd or edg is not null.
//    */
//   public boolean matchNode() { return nd != null; }
//
//   public boolean matchEdge() { return nd1 != null;}
}

