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
        String targetfldname = us.hasGraph();
        if (targetfldname != "_"){
            while(us.next()) {
                String a[] = targetfldname.split("_");
                String gname = us.getString(a[2]);
                String s = "delete from table2 where gname = '"+gname+"'";
                Parser par=new Parser(s);
                executeDelete(par.delete(), tx);
                s = "delete from table3 where gname = '"+gname+"'";
                par=new Parser(s);
                executeDelete(par.delete(), tx);
                us.delete();
                count++;
            }
            us.close();
            return count;
        }
        else{
            while(us.next()) {
                us.delete();
                count++;
            }
            us.close();
            return count;
        }
    }

    public int executeModify(ModifyData data, Transaction tx) {
        Plan p = new TablePlan(data.tableName(), tx);
        p = new SelectPlan(p, data.pred());
        UpdateScan us = (UpdateScan) p.open();
        int count = 0;
        if(data.getOperation() ==0){
            while(us.next()) {
                Constant val = data.newValue().evaluate(us);
                us.setVal(data.targetField(), val);
                count++;
            }
            us.close();
            return count;
        }
        else if (data.getOperation() ==1){
            if(data.getisNode()){
                while(us.next()) {
                    String gname = us.getString(data.targetField());
                    for (Constant tmp : data.getNd()){
                        String nname = (String)tmp.asJavaVal();
                        String s ="insert into table2 (gname, nname) values ('" + gname + "', '" + nname + "')";
                        Parser par=new Parser(s);
                        executeInsert(par.insert(), tx);
                    }
                    count++;
                }
                us.close();
                return count;
            }
            else {
                while(us.next()) {
                    String gname = us.getString(data.targetField());
                    for (int i =0; i<data.getLth().size();++i){
                        String n1 = (String)data.getNd1().get(i).asJavaVal();
                        String n2 = (String)data.getNd2().get(i).asJavaVal();
                        int length = (int)data.getLth().get(i).asJavaVal();
                        String s = "insert into table3 (gname, n1, n2, length) values ('"
                                + gname + "', '" + n1 + "', '" + n2 + "', " + String.valueOf(length) + ")";
                        Parser par=new Parser(s);
                        executeInsert(par.insert(), tx);
                    }
                    count++;
                }
                us.close();
                return count;
            }
        }
        else {
            if(data.getisNode()){
                while(us.next()) {
                    String gname = us.getString(data.targetField());
                    for (Constant tmp : data.getNd()){
                        String nname = (String)tmp.asJavaVal();
                        String s ="delete from table2 where gname = '" + gname + "' and nname = '" + nname + "'";
                        Parser par=new Parser(s);
                        executeDelete(par.delete(), tx);
                    }
                    count++;
                }
                us.close();
                return count;
            }
            else {
                while(us.next()) {
                    String gname = us.getString(data.targetField());
                    for (int i =0; i<data.getNd1().size();++i){
                        String n1 = (String)data.getNd1().get(i).asJavaVal();
                        String n2 = (String)data.getNd2().get(i).asJavaVal();
                        String s = "delete from table3 where gname = '" +
                                gname +"' and n1 = '" + n1 +"' and n2 = '" + n2 + "'";
                        Parser par=new Parser(s);
                        executeDelete(par.delete(), tx);
                    }
                    count++;
                }
                us.close();
                return count;
            }
        }
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

        List<String> nodeFld=new ArrayList<String>();
        nodeFld.add("gname");
        nodeFld.add("nname");
        List<String> edgeFld=new ArrayList<String>();
        edgeFld.add("gname");
        edgeFld.add("n1");
        edgeFld.add("n2");
        edgeFld.add("length");
        if(data.getNode()!=null){
            for(int i=0;i<data.getNode().size();++i){
                List<Constant> nodeVal= new ArrayList<Constant>();
                nodeVal.add(data.getTbl().get(0));
                nodeVal.add(data.getNode().get(i));
                InsertData tmp=new InsertData("table2", nodeFld, nodeVal);
                executeInsert(tmp, tx);
            }
        }

        if(data.getLength()!=null){
            for(int i=0;i<data.getNode1().size();++i){
                List<Constant> edgeVal= new ArrayList<Constant>();
                edgeVal.add(data.getTbl().get(0));
                edgeVal.add(data.getNode1().get(i));
                edgeVal.add(data.getNode2().get(i));
                edgeVal.add(data.getLength().get(i));
                InsertData tmp=new InsertData("table3", edgeFld, edgeVal);
                executeInsert(tmp, tx);
            }
        }
        return 1;
    }

    public int executeCreateTable(CreateTableData data, Transaction tx) {
//      for(String fldname : data.newSchema().fields()){
//         if(data.newSchema().length(fldname)==-1){
//            String qry="select gid from table1 ";
//            Parser q=new Parser(qry);
//            Plan pl=new BasicQueryPlanner().createPlan(q.query(),tx);
//            Scan sc=pl.open();
//            int tmp=0;
//            while (sc.next()) {
//               int gid=sc.getInt("gid");
//               if(gid>tmp) tmp=gid;
//            }
//            sc.close();
//            tmp++;
//            String s="insert into TABLE1(GId, GName) values (" + Integer.toString(tmp) + ", '"+fldname+"')";
//            Parser p=new Parser(s);
//            executeInsert(p.insert(), tx);
//            System.out.println("Insert GRAPH successfully.");
//            break;
//         }
//      }
        SimpleDB.mdMgr().createTable(data.tableName(), data.newSchema(), tx);
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
