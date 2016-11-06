package simpledb.parse;

import java.util.*;
import simpledb.query.*;
import simpledb.record.Schema;

/**
 * The SimpleDB parser.
 * @author Edward Sciore
 */
public class Parser {
   private Lexer lex;
   
   public Parser(String s) {
      lex = new Lexer(s);
   }
   
// Methods for parsing predicates, terms, expressions, constants, and fields
   
   public String field() {
      return lex.eatId();
   }
   
   public Constant constant() {
      if (lex.matchStringConstant())
         return new StringConstant(lex.eatStringConstant());
      else
         return new IntConstant(lex.eatIntConstant());
   }
   
   public Expression expression() {
      if (lex.matchId())
         return new FieldNameExpression(field());
      else
         return new ConstantExpression(constant());
   }
   
   public Term term() {
      Expression lhs = expression();
      lex.eatDelim('=');
      Expression rhs = expression();
      return new Term(lhs, rhs);
   }
   
   public Predicate predicate() {
      Predicate pred = new Predicate(term());
      if (lex.matchKeyword("and")) {
         lex.eatKeyword("and");
         pred.conjoinWith(predicate());
      }
      return pred;
   }
   
// Methods for parsing queries
   
   public QueryData query() {
      lex.eatKeyword("select");
      Collection<String> fields = selectList();
      lex.eatKeyword("from");
      Collection<String> tables = tableList();
      Predicate pred = new Predicate();
      if (lex.matchKeyword("where")) {
         lex.eatKeyword("where");
         pred = predicate();
      }
      return new QueryData(fields, tables, pred);
   }
   
   private Collection<String> selectList() {
      Collection<String> L = new ArrayList<String>();
      L.add(field());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(selectList());
      }
      return L;
   }
   
   private Collection<String> tableList() {
      Collection<String> L = new ArrayList<String>();
      L.add(lex.eatId());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(tableList());
      }
      return L;
   }
   
// Methods for parsing the various update commands
   
   public Object updateCmd() {
      if (lex.matchKeyword("insert"))
         return insert();
      else if (lex.matchKeyword("delete"))
         return delete();
      else if (lex.matchKeyword("update"))
         return modify();
      else
         return create();
   }
   
   private Object create() {
      lex.eatKeyword("create");
      if (lex.matchKeyword("table"))
         return createTable();
      else if (lex.matchKeyword("view"))
         return createView();
      else
         return createIndex();
   }
   
// Method for parsing delete commands
   
   public DeleteData delete() {
      lex.eatKeyword("delete");
      lex.eatKeyword("from");
      String tblname = lex.eatId();
      Predicate pred = new Predicate();
      if (lex.matchKeyword("where")) {
         lex.eatKeyword("where");
         pred = predicate();
      }
      return new DeleteData(tblname, pred);
   }
   
// Methods for parsing insert commands
   
   public InsertData insert() {
      lex.eatKeyword("insert");
      lex.eatKeyword("into");
      String tblname = lex.eatId();
      lex.eatDelim('(');
      List<String> flds = fieldList();
      lex.eatDelim(')');
      lex.eatKeyword("values");
      lex.eatDelim('(');

      List<Constant> nd = new ArrayList<Constant>();
      List<Constant> nd1 = new ArrayList<Constant>();
      List<Constant> nd2 = new ArrayList<Constant>();
      List<Constant> length = new ArrayList<Constant>();
      List<Constant> sname = new ArrayList<Constant>();
      List<Constant> vals = new ArrayList<Constant>();
      if (lex.matchKeyword("graph")){
         lex.eatKeyword("graph");
         lex.eatDelim('(');
         lex.eatKeyword("name");
         lex.eatDelim('(');
         Constant c = constant();
         sname.add(c);
         vals.add(c);
         lex.eatDelim(')');
         if(lex.matchDelim(',')){
            lex.eatDelim(',');
         }
         if(lex.matchKeyword("node")){
            lex.eatKeyword("node");
            lex.eatDelim('(');
            nd.addAll(constList(';'));
            lex.eatDelim(')');
            if(lex.matchDelim(',')){
               lex.eatDelim(',');
            }
         }

         if(lex.matchKeyword("edge")){
            lex.eatKeyword("edge");
            lex.eatDelim('(');

            nd1.add(constant());
            lex.eatDelim('-');
            lex.eatDelim('>');
            nd2.add(constant());
            lex.eatDelim(',');
            length.add(constant());
            while(lex.matchDelim(';')){
               lex.eatDelim(';');
               nd1.add(constant());
               lex.eatDelim('-');
               lex.eatDelim('>');
               nd2.add(constant());
               lex.eatDelim(',');
               length.add(constant());
            }
            lex.eatDelim(')');
         }
         lex.eatDelim(')');
      }
      else {
         vals.add(constant());
      }

      //lex.nextToken();
      //lex.eatDelim('(');
      //vals.add(constant());
      //lex.eatDelim(')');
      while(lex.matchDelim(',')){
         lex.eatDelim(',');
         //vals.add(constant());
         if (lex.matchKeyword("graph")){
            lex.eatKeyword("graph");
            lex.eatDelim('(');
            lex.eatKeyword("name");
            lex.eatDelim('(');
            Constant d = constant();
            sname.add(d);
            vals.add(d);
            lex.eatDelim(')');
            if(lex.matchDelim(',')){
               lex.eatDelim(',');
            }
            if(lex.matchKeyword("node")){
               lex.eatKeyword("node");
               lex.eatDelim('(');
               nd.addAll(constList(';'));
               lex.eatDelim(')');
               if(lex.matchDelim(',')){
                  lex.eatDelim(',');
               }
            }

            if(lex.matchKeyword("edge")){
               lex.eatKeyword("edge");
               lex.eatDelim('(');
               nd1.add(constant());
               lex.eatDelim('-');
               lex.eatDelim('>');
               nd2.add(constant());
               lex.eatDelim(',');
               length.add(constant());
               while(lex.matchDelim(';')){
                  lex.eatDelim(';');
                  nd1.add(constant());
                  lex.eatDelim('-');
                  lex.eatDelim('>');
                  nd2.add(constant());
                  lex.eatDelim(',');
                  length.add(constant());
               }
               lex.eatDelim(')');
            }
            lex.eatDelim(')');
         }
         else {
            vals.add(constant());
         }
      }

      //List<Constant> vals = constList(',');
      lex.eatDelim(')');
      return new InsertData(tblname, flds, vals, sname, nd, nd1, nd2, length);
   }
   
   private List<String> fieldList() {
      List<String> L = new ArrayList<String>();
      L.add(field());
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         L.addAll(fieldList());
      }
      return L;
   }
   
   private List<Constant> constList(char note) {
      List<Constant> L = new ArrayList<Constant>();
      Constant c=constant();
      L.add(c);
      if (lex.matchDelim(note)) {
         lex.eatDelim(note);
         L.addAll(constList(note));
      }
      return L;
   }
   
// Method for parsing modify commands
   
   public ModifyData modify() {
      lex.eatKeyword("update");
      String tblname = lex.eatId();
      lex.eatKeyword("set");
      String fldname = field();
      lex.eatDelim('=');
      Expression newval = expression();
      Predicate pred = new Predicate();
      if (lex.matchKeyword("where")) {
         lex.eatKeyword("where");
         pred = predicate();
      }
      return new ModifyData(tblname, fldname, newval, pred);
   }
   
// Method for parsing create table commands
   
   public CreateTableData createTable() {
      lex.eatKeyword("table");
      String tblname = lex.eatId();
      lex.eatDelim('(');
      Schema sch = fieldDefs();
      lex.eatDelim(')');
      return new CreateTableData(tblname, sch);
   }
   
   private Schema fieldDefs() {
      Schema schema = fieldDef();
      if (lex.matchDelim(',')) {
         lex.eatDelim(',');
         Schema schema2 = fieldDefs();
         schema.addAll(schema2);
      }
      return schema;
   }
   
   private Schema fieldDef() {
      String fldname = field();
      return fieldType(fldname);
   }
   
   private Schema fieldType(String fldname) {
      Schema schema = new Schema();
      if (lex.matchKeyword("int")) {
         lex.eatKeyword("int");
         schema.addIntField(fldname);
      }
      else if (lex.matchKeyword("varchar")) {
         lex.eatKeyword("varchar");
         //lex.eatKeyword("graph");
         lex.eatDelim('(');
         int strLen = lex.eatIntConstant();
         lex.eatDelim(')');
         schema.addStringField(fldname, strLen);
      }
      else{
         lex.eatKeyword("graph");
         schema.addStringField(fldname, 20);
      }
      return schema;
   }
   
// Method for parsing create view commands
   
   public CreateViewData createView() {
      lex.eatKeyword("view");
      String viewname = lex.eatId();
      lex.eatKeyword("as");
      QueryData qd = query();
      return new CreateViewData(viewname, qd);
   }
   
   
//  Method for parsing create index commands
   
   public CreateIndexData createIndex() {
      lex.eatKeyword("index");
      String idxname = lex.eatId();
      lex.eatKeyword("on");
      String tblname = lex.eatId();
      lex.eatDelim('(');
      String fldname = field();
      lex.eatDelim(')');
      return new CreateIndexData(idxname, tblname, fldname);
   }
}

