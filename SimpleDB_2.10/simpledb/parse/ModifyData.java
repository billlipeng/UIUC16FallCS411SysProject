package simpledb.parse;

import simpledb.query.*;
import java.util.*;

/**
 * Data for the SQL <i>update</i> statement.
 * @author Edward Sciore
 */
public class ModifyData {
   private String tblname;
   private String fldname;
   private Expression newval;
   private Predicate pred;
   private int operation; //0:=; 1:+; 2:-;
   private boolean isNode;
   private List<Constant> nd;
   private List<Constant> nd1;
   private List<Constant> nd2;
   private List<Constant> length;
   
   /**
    * Saves the table name, the modified field and its new value, and the predicate.
    */
   public ModifyData(String tblname, String fldname, int operation, Expression newval, Predicate pred) {
      this.tblname = tblname;
      this.fldname = fldname;
      this.operation = operation;
      this.newval = newval;
      this.pred = pred;
   }

   public ModifyData(String tblname, String fldname, int operation, boolean isNode, List<Constant> nd, Predicate pred) {
      this.tblname = tblname;
      this.fldname = fldname;
      this.operation = operation;
      this.isNode = isNode;
      this.nd = nd;
      this.pred = pred;
   }

   public ModifyData(String tblname, String fldname, int operation, boolean isNode,
                     List<Constant> nd1, List<Constant> nd2, List<Constant> length, Predicate pred) {
      this.tblname = tblname;
      this.fldname = fldname;
      this.operation = operation;
      this.isNode = isNode;
      this.nd1 = nd1;
      this.nd2 = nd2;
      this.length = length;
      this.pred = pred;
       System.out.println(tblname + " " + fldname + " " + operation);
   }

   public ModifyData(String tblname, String fldname, int operation, boolean isNode,
                     List<Constant> nd1, List<Constant> nd2, Predicate pred) {
      this.tblname = tblname;
      this.fldname = fldname;
      this.operation = operation;
      this.isNode = isNode;
      this.nd1 = nd1;
      this.nd2 = nd2;
      this.pred = pred;
   }
   /**
    * Returns the name of the affected table.
    * @return the name of the affected table
    */
   public String tableName() {
      return tblname;
   }
   
   /**
    * Returns the field whose values will be modified
    * @return the name of the target field
    */
   public String targetField() {
      return fldname;
   }
   
   /**
    * Returns an expression.
    * Evaluating this expression for a record produces
    * the value that will be stored in the record's target field.
    * @return the target expression
    */
   public Expression newValue() {
      return newval;
   }
   
   /**
    * Returns the predicate that describes which
    * records should be modified.
    * @return the modification predicate
    */
   public Predicate pred() {
      return pred;
   }

   public int getOperation() { return operation;}
   public boolean getisNode() { return isNode;}
   public List<Constant> getNd() { return nd;}
   public List<Constant> getNd1() { return nd1;}
   public List<Constant> getNd2() { return nd2;}
   public List<Constant> getLth() { return length;}
}