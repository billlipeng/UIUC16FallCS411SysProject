package simpledb.planner;

//import java.util.Iterator;
import java.util.*;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.parse.*;
import simpledb.query.*;

/**
 * The basic planner for SQL update statements.
 * @author sciore
 */
public class BasicUpdatePlanner implements UpdatePlanner {

   private static Integer id = 0;
   
   public int executeDelete(DeleteData data, Transaction tx) {
      Plan p = new TablePlan(data.tableName(), tx);
      p = new SelectPlan(p, data.pred());
      UpdateScan us = (UpdateScan) p.open();
      int count = 0;
      while(us.next()) {
         us.delete();
         count++;
      }
      us.close();
      return count;
   }
   
   public int executeModify(ModifyData data, Transaction tx) {
      Plan p = new TablePlan(data.tableName(), tx);
      p = new SelectPlan(p, data.pred());
      UpdateScan us = (UpdateScan) p.open();
      int count = 0;
      while(us.next()) {
         Constant val = data.newValue().evaluate(us);
         us.setVal(data.targetField(), val);
         count++;
      }
      us.close();
      return count;
   }
   
   public int executeInsert(InsertData data, Transaction tx) {
      Plan p = new TablePlan(data.tableName(), tx);
      UpdateScan us = (UpdateScan) p.open();
      us.insert();
      Iterator<Constant> iter = data.vals().iterator();
      for (String fldname : data.fields()) {
         Constant val = iter.next();
         us.setVal(fldname, val);
      }
      us.close();
      return 1;
   }

   public int executeCreateTable(CreateTableData data, Transaction tx) {
      for(String fldname : data.newSchema().fields()){
         if(data.newSchema().length(fldname)==-1){
            String qry="select gid from table1 ";
            Parser q=new Parser(qry);
            Plan pl=new BasicQueryPlanner().createPlan(q.query(),tx);
            Scan sc=pl.open();
            int tmp=0;
            while (sc.next()) {
               int gid=sc.getInt("gid");
               if(gid>tmp) tmp=gid;
            }
            sc.close();
            tmp++;
            String s="insert into TABLE1(GId, GName) values (" + Integer.toString(tmp) + ", '"+fldname+"')";
            Parser p=new Parser(s);
            executeInsert(p.insert(), tx);
            System.out.println("Insert GRAPH successfully.");
            break;
         }
      }
      SimpleDB.mdMgr().createTable(data.tableName(), data.newSchema(), tx);
      System.out.println("Insert GRAPH successfully.");
      return 0;
   }
   
   public int executeCreateView(CreateViewData data, Transaction tx) {
      SimpleDB.mdMgr().createView(data.viewName(), data.viewDef(), tx);
      return 0;
   }
   public int executeCreateIndex(CreateIndexData data, Transaction tx) {
      SimpleDB.mdMgr().createIndex(data.indexName(), data.tableName(), data.fieldName(), tx);
      return 0;  
   }
}
