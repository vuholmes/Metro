package qrcom.PROFIT.files.dao.local.GV;

import java.sql.*;

import java.text.DecimalFormat;

import java.util.*;
import qrcom.PROFIT.files.dao.IF.shared.QueryBasedPagingDAOHandller;
import qrcom.PROFIT.shared.Utility.ExecuteQuery;
import qrcom.util.ejb.connection.*;
import qrcom.util.*;
import qrcom.PROFIT.files.info.*;

public class VoucherIssuanceInquiryDAOAMY implements QueryBasedPagingDAOHandller
{
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private PreparedStatement pstmt2 = null;
    private ResultSet resultSet = null;
    private ResultSet resultSet2 = null;
    private String sql_query = "";
    private GvtypemstSQLAMY gvtypemstSQL = null;
      
    public VoucherIssuanceInquiryDAOAMY()
    {
    }
    
    public HParam selectQueryBasedWebViewList(HParam hParam) throws Exception    
    {
        Collection rsCollection = new Vector();
        long total_rows_selected = 0;   
        
        String COY            = (String)hParam.get("COY");
        String COY_SUB        = (String)hParam.get("COY_SUB");
        String STORE          = (String)hParam.get("STORE");
        String PRC_ID         = (String)hParam.get("PRC_ID");
        String GV_TYPE        = (String)hParam.get("GV_TYPE");
        String SERIAL_NUM     = (String)hParam.get("SERIAL_NUM");
        String GV_DENOMINATION= (String)hParam.get("GV_DENOMINATION");
        String GV_NO          = (String)hParam.get("GV_NO");
        String PCH_NAME       = (String)hParam.get("PCH_NAME");
        String PCH_CONTACT_NO = (String)hParam.get("PCH_CONTACT_NO");
        String RSN_CD         = (String)hParam.get("RSN_CD");
        String DATE_ISSUE     = (String)hParam.get("DATE_ISSUE");
        String PRC_USR_ID     = (String)hParam.get("PRC_USR_ID");
        String PRC_DATE       = (String)hParam.get("PRC_DATE");
        String PRC_TYPE       = (String)hParam.get("PRC_TYPE");
        String MEMBERSHIP_NO  = (String)hParam.get("MEMBERSHIP_NO"); //mega 20151222
  
        String BEGIN_ROW_NUM  = hParam.getString("BEGIN_ROW_NUM");
        String END_ROW_NUM    = hParam.getString("END_ROW_NUM");
  
        String selection  = " SELECT " +
                            " G.COY, G.COY_SUB, G.STORE, G.PRC_ID, G.PRC_USR_ID, G.PRC_DATE, T.SERIAL_NUM, " +
                            " T.RSN_CD, G.GV_TYPE, G.PRC_TYPE, T.TRANS_DATE, SUM(G.TOT_AMOUNT) AS TOT_AMOUNT, " +
                            " T.PCH_NAME, T.DATE_ISSUE " ;
        String from       = " FROM GVLOGBOOK G, GVTRANS T ";
        String where      = " WHERE G.TRANS_NO = T.TRANS_NO " +
                            " AND G.PRC_TYPE = T.TRANS_TYPE " +
                            " AND G.PRC_DATE = T.TRANS_DATE " +
                            " AND G.COY = T.COY " +
                            " AND G.COY_SUB = T.COY_SUB " +
                            " AND G.COY = '" + COY.trim() + "' " +
                            " AND T.COY_SUB = '" + COY_SUB.trim() + "' ";
        String groupBy    = " GROUP BY G.COY, G.COY_SUB, G.STORE, G.PRC_ID, G.PRC_USR_ID, G.PRC_DATE, T.SERIAL_NUM, " +
                            " T.RSN_CD, G.GV_TYPE, G.PRC_TYPE, T.TRANS_DATE, T.PCH_NAME, T.DATE_ISSUE ";
        String orderBy    = " ORDER BY G.PRC_DATE DESC, G.PRC_ID DESC ";
                            
  
        if (STORE != null && STORE.trim().length() > 0)
          where += " AND T.STORE = '" + STORE.trim() + "' ";
  
        if (PRC_ID != null && PRC_ID.trim().length() > 0)
          where += " AND G.PRC_ID = '" + PRC_ID.trim() + "' ";
  
        if (GV_TYPE != null && GV_TYPE.trim().length() > 0)
          where += " AND G.GV_TYPE = '" + GV_TYPE.trim() + "' ";
          
        if (SERIAL_NUM != null && SERIAL_NUM.trim().length() > 0)
          where += " AND T.SERIAL_NUM = '" + SERIAL_NUM.trim() + "' ";
  
        if (GV_DENOMINATION != null && GV_DENOMINATION.trim().length() > 0)
          where += " AND G.GV_DENOMINATION = '" + GV_DENOMINATION.trim() + "' ";
  
        if (GV_NO != null && GV_NO.trim().length() > 0)
          where += " AND '" + GV_NO.trim() + "' BETWEEN G.FR_GV_NO AND G.TO_GV_NO ";
  
        if (PCH_NAME != null && PCH_NAME.trim().length() > 0)
          where += " AND T.PCH_NAME = '" + PCH_NAME.trim() + "'";
          
        if (PCH_CONTACT_NO != null && PCH_CONTACT_NO.trim().length() > 0)
          where += " AND T.PCH_CONTACT_NO = '" + PCH_CONTACT_NO.trim() + "' ";
          
        if (RSN_CD != null && RSN_CD.trim().length() > 0)
          where += " AND T.RSN_CD = '" + RSN_CD.trim() + "' ";
  
        if (DATE_ISSUE != null && DATE_ISSUE.trim().length() > 0)
          where += " AND T.DATE_ISSUE = TO_DATE('" + DATE_ISSUE.trim() + "', 'YYYY-MM-DD') ";
          
        if (PRC_USR_ID != null && PRC_USR_ID.trim().length() > 0)
          where += " AND G.PRC_USR_ID = '" + PRC_USR_ID.trim() + "' ";
  
        if (PRC_DATE != null && PRC_DATE.trim().length() > 0)
          where += " AND G.PRC_DATE = TO_DATE('" + PRC_DATE.trim() + "', 'YYYY-MM-DD') ";
          
        if(PRC_TYPE != null && PRC_TYPE.trim().length() > 0)
        {
            where += " AND G.PRC_TYPE = '" + PRC_TYPE.trim() + "' ";
        }
        else
        {
            where += " AND G.PRC_TYPE in ('I', 'L') ";
        }
        
        if(MEMBERSHIP_NO != null && MEMBERSHIP_NO.trim().length() > 0)
          where += " AND T.MEMBERSHIP_NO = '" + MEMBERSHIP_NO.trim() + "' ";
   
        try 
        {
           conn = DataSource.getLocalConnection();
           ExecuteQuery execQuery = new ExecuteQuery(conn);
           
           /** ----------------- retrieve total number of records selected -------------- **/
           sql_query = "select count(rownum) from (" + selection + from + where + groupBy + orderBy + " )";
           String [] strQuery = execQuery.selectSingleRowQuery(sql_query.toString(), 1);
           total_rows_selected = Long.parseLong(strQuery[0]);
           /** -----------------End of retrieve total number of records selected -------------- **/
           
           
           /** ---------------- Retrieve records --------------- **/
           sql_query = selection + from + where + groupBy + orderBy;
           String sql_query_2 = "select * from (select V0.*, rownum CN from (" + sql_query + ") V0) V1 where V1.CN between " + 
                              BEGIN_ROW_NUM + " and " + END_ROW_NUM;
              
           pstmt = conn.prepareStatement(sql_query_2);
           resultSet = pstmt.executeQuery();
        
//           System.out.println ("*** VOUCHER ISSUANCE INQUIRY ***  "+sql_query);
           while (resultSet != null && resultSet.next())
           {
              String [] str = new String[12];
              str[0] = resultSet.getString(1);
              str[1] = resultSet.getString(2);
              str[2] = resultSet.getString(3);
              str[3] = resultSet.getString(4);
              str[4] = resultSet.getString(5);
              str[5] = resultSet.getString(6);
              str[6] = resultSet.getString(7);
              str[7] = resultSet.getString(8);
              str[8] = resultSet.getString(9);
              str[9] = resultSet.getString(10);
              str[10] = resultSet.getString(11);
              str[11] = resultSet.getString(12);
              
              rsCollection.add(str);
           }
        } // try
        catch (SQLException e)
        {
           //System.out.println("SQL Exception" + e.toString());
           throw (e);
        }
        finally
        {
            closeConnection();
        }
        HParam rParam = new HParam(); // object for return keys(TOTAL_ROWS, COLLECTION)
        rParam.put("COLLECTION", rsCollection);
        rParam.put("TOTAL_ROWS", String.valueOf(total_rows_selected));
        return (rParam);      
    }
    
    public Map getViewIssuanceRecord(HParam hPrm) throws Exception
    {
        Map returnMap = new HashMap();
        
        try
        {
            conn = DataSource.getLocalConnection();
            
            sql_query =  " SELECT H.COY, H.COY_SUB, H.STORE, H.TRANS_TYPE, H.SERIAL_NUM, " +
                         " H.PCH_NAME, H.TRANS_DATE, H.PCH_CONTACT_NO, H.COLL_NRIC, H.COLL_NAME, " +
                         " H.PAYMENTMODE, H.RSN_CD, H.TRANS_DATE, H.TRANS_OPR, " +
                         " H.GV_TYPE, " + //note : retrieve gv_type to be used in method 'getReasonCodeDescription' below
                         " H.MEMBERSHIP_NO, H.REMARK " + 
                         " FROM GVTRANS H " +
                         " WHERE H.TRANS_TYPE = ? " +
                         " AND H.TRANS_NO IN " +
                         " ( SELECT G.TRANS_NO FROM GVLOGBOOK G WHERE G.PRC_ID = ? ) " +
                         " AND H.COY = ? " +
                         " AND H.COY_SUB = ? " +
                         " AND H.STORE = ? " +
                         " AND H.TRANS_DATE = ? " ;
            
            pstmt = conn.prepareStatement(sql_query);
            pstmt.setString(1, (String)hPrm.get("PRC_TYPE"));
            pstmt.setString(2, (String)hPrm.get("PRC_ID"));
            pstmt.setString(3, (String)hPrm.get("COY"));
            pstmt.setString(4, (String)hPrm.get("COY_SUB"));
            pstmt.setString(5, (String)hPrm.get("STORE"));
            pstmt.setDate(6, qrMisc.parseSqlDate((String)hPrm.get("TRANS_DATE"), "yyyy-MM-dd"));
            resultSet = pstmt.executeQuery();
            
            if(resultSet.next())
            {
                String coy    = (String)hPrm.get("COY");
                String coySub = (String)hPrm.get("COY_SUB");
                String gvType = resultSet.getString("GV_TYPE")!=null && resultSet.getString("GV_TYPE").length()>0 ? resultSet.getString("GV_TYPE") : "";
                String rsnCd  = resultSet.getString("RSN_CD")!=null && resultSet.getString("RSN_CD").length()>0 ? resultSet.getString("RSN_CD") : "";
                String transOpr = resultSet.getString("TRANS_OPR")!=null && resultSet.getString("TRANS_OPR").length()>0 ? resultSet.getString("TRANS_OPR") : "";
                
                String rsnCdDesc = getReasonCodeDescription(coy, coySub, gvType, rsnCd);
                String rsnCdFull = rsnCd + " - " + rsnCdDesc;
                
                String transOprName = getTransOprName(transOpr);
                String transOprFull = transOpr + " - " + transOprName;
            
                returnMap.put("COY", coy);
                returnMap.put("COY_SUB", coySub);
                returnMap.put("STORE", resultSet.getString("STORE"));
                returnMap.put("TRANS_TYPE", resultSet.getString("TRANS_TYPE"));
                returnMap.put("SERIAL_NUM", resultSet.getString("SERIAL_NUM"));
                returnMap.put("PCH_NAME", resultSet.getString("PCH_NAME"));
                returnMap.put("TRANS_DATE", resultSet.getString("TRANS_DATE"));
                returnMap.put("PCH_CONTACT_NO", resultSet.getString("PCH_CONTACT_NO"));
                returnMap.put("COLL_NRIC", resultSet.getString("COLL_NRIC"));
                returnMap.put("COLL_NAME", resultSet.getString("COLL_NAME"));
                returnMap.put("PAYMENTMODE", resultSet.getString("PAYMENTMODE"));
                returnMap.put("RSN_CD", rsnCdFull);
                returnMap.put("TRANS_OPR", transOprFull);
                returnMap.put("MEMBERSHIP_NO", resultSet.getString("MEMBERSHIP_NO"));
                returnMap.put("REMARK", resultSet.getString("REMARK"));
            }
            
            sql_query = " SELECT SUM(TOT_AMOUNT) AS TOT_AMOUNT, SUM(DISC_AMT) AS TOT_DISCOUNT_AMT " +
                        " FROM GVLOGBOOK " +
                        " WHERE COY = ? " +
                        " AND COY_SUB = ? " +
                        " AND PRC_TYPE = ? " +
                        " AND PRC_ID = ? " +
                        " AND PRC_DATE = ? " +
                        " AND STORE = ? ";
                        
            pstmt = conn.prepareStatement(sql_query);
            pstmt.setString(1, (String)hPrm.get("COY"));
            pstmt.setString(2, (String)hPrm.get("COY_SUB"));
            pstmt.setString(3, (String)hPrm.get("PRC_TYPE"));
            pstmt.setString(4, (String)hPrm.get("PRC_ID"));
            pstmt.setDate(5, qrMisc.parseSqlDate((String)hPrm.get("PRC_DATE"), "yyyy-MM-dd"));
            pstmt.setString(6, (String)hPrm.get("STORE"));
            resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                final String strTotalAmount = resultSet.getString("TOT_AMOUNT");
                final String strTotalDiscount = resultSet.getString("TOT_DISCOUNT_AMT");
                returnMap.put("TOT_AMOUNT", strTotalAmount);
                returnMap.put("TOT_DISCOUNT", strTotalDiscount);
                try {
                    double totalAmountAfterDiscount =
                        Double.parseDouble(strTotalAmount) - Double.parseDouble(strTotalDiscount);
                    returnMap.put("TOT_AMOUNT_AFT_DISCOUNT", new DecimalFormat("#0.00").format(totalAmountAfterDiscount));
                } catch (Exception e) {
                    returnMap.put("TOT_AMOUNT_AFT_DISCOUNT", strTotalAmount);
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            throw (e);
        }
        finally
        {
            closeConnection();
        }
        
        return returnMap;
    }
    
    public Collection getIssuanceSummaryTab(HParam hPrm) throws Exception
    {
        Collection rsCollection = new Vector();
        
        try
        {
            conn = DataSource.getLocalConnection();
            
            sql_query = " SELECT G.GV_TYPE, G.GV_DENOMINATION, G.FR_GV_NO, G.TO_GV_NO, G.TOT_GV_NO, G.TOT_AMOUNT, " +
                        " T.POINTS, T.TOT_POINTS, " + //20151222
                        " T.BOX_NO, T.BOX_TYPE, T.BOOK_NO, T.REMARK " + //20160111
                        " FROM GVLOGBOOK G, GVTRANS T " +
                        " WHERE G.TRANS_NO =  T.TRANS_NO " +
                        " AND G.PRC_DATE = T.TRANS_DATE " +
                        " AND G.COY = T.COY " +
                        " AND G.COY_SUB = T.COY_SUB " +
                        " AND G.STORE = T.STORE " +
                        " AND G.COY = ? " +
                        " AND G.COY_SUB = ? " +
                        " AND G.PRC_TYPE = ? " +
                        " AND G.PRC_ID = ? " +
                        " AND T.TRANS_TYPE = ? " +
                        " AND T.TRANS_DATE = ? " +
                        " AND T.STORE = ? ";
                        
            pstmt = conn.prepareStatement(sql_query);
            pstmt.setString(1, (String)hPrm.get("COY"));
            pstmt.setString(2, (String)hPrm.get("COY_SUB"));
            pstmt.setString(3, (String)hPrm.get("PRC_TYPE"));
            pstmt.setString(4, (String)hPrm.get("PRC_ID"));
            pstmt.setString(5, (String)hPrm.get("PRC_TYPE"));
            pstmt.setDate(6, qrMisc.parseSqlDate((String)hPrm.get("TRANS_DATE"), "yyyy-MM-dd"));
            pstmt.setString(7, (String)hPrm.get("STORE"));
            resultSet = pstmt.executeQuery();
            
            while (resultSet != null && resultSet.next())
            {
                String coy = (String)hPrm.get("COY");
                String coySub = (String)hPrm.get("COY_SUB");
                String gvType = resultSet.getString("GV_TYPE");
                String gvDenomination = resultSet.getString("GV_DENOMINATION");
                
                String gvTypeDesc = getVoucherTypeDescription(coy, coySub, gvType);
                String gvDenoDesc = getVoucherDenoDescription(coy, coySub, gvType, gvDenomination);
                
                String [] str = new String[12];
                str[0] = gvType + " - " + gvTypeDesc; //GV_TYPE
                str[1] = gvDenomination + " - " + gvDenoDesc; //GV_DENOMINATION
                str[2] = resultSet.getString(3); //FR_GV_NO
                str[3] = resultSet.getString(4); //TO_GV_NO
                str[4] = resultSet.getString(5); //TOT_GV_NO
                str[5] = resultSet.getString(6); //TOT_AMOUNT
                str[6] = resultSet.getString(7); //POINTS
                str[7] = resultSet.getString(8); //TOT_POINTS
                str[8] = resultSet.getString(9); //BOX_NO
                str[9] = resultSet.getString(10); //BOX_TYPE
                str[10] = resultSet.getString(11); //BOOK_NO
                str[11] = resultSet.getString(12); //REMARK
              
              rsCollection.add(str);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw (e);
        }
        finally
        {
            closeConnection();
        }
        
        return rsCollection;
    }
    
     public Collection getIssuanceDetailTab(HParam hPrm) throws Exception
     {
        Collection rsCollection = new Vector();
        
        try
        {
            conn = DataSource.getLocalConnection();
            
            sql_query = " SELECT G.GV_TYPE, G.GV_DENOMINATION, V.GV_NO " +
                        " FROM GVLOGBOOK G, GVMSTR V " +
                        " WHERE V.GV_NO BETWEEN G.FR_GV_NO AND G.TO_GV_NO " +
                        " AND G.COY = ? " +
                        " AND G.COY_SUB = ? " +
                        " AND G.PRC_TYPE = ? " +
                        " AND G.PRC_ID = ? " +
                        " AND G.PRC_DATE = ? " +
                        " AND G.STORE = ? ";
                        
            pstmt = conn.prepareStatement(sql_query);
            pstmt.setString(1, (String)hPrm.get("COY"));
            pstmt.setString(2, (String)hPrm.get("COY_SUB"));
            pstmt.setString(3, (String)hPrm.get("PRC_TYPE"));
            pstmt.setString(4, (String)hPrm.get("PRC_ID"));
            pstmt.setDate(5, qrMisc.parseSqlDate((String)hPrm.get("PRC_DATE"), "yyyy-MM-dd"));
            pstmt.setString(6, (String)hPrm.get("STORE"));
            resultSet = pstmt.executeQuery();
            
            while (resultSet != null && resultSet.next())
            {
                String coy = (String)hPrm.get("COY");
                String coySub = (String)hPrm.get("COY_SUB");
                String gvType = resultSet.getString("GV_TYPE");
                String gvDenomination = resultSet.getString("GV_DENOMINATION");
                
                String gvTypeDesc = getVoucherTypeDescription(coy, coySub, gvType);
                String gvDenoDesc = getVoucherDenoDescription(coy, coySub, gvType, gvDenomination);
              
                String [] str = new String[3];
                str[0] = gvType + " - " + gvTypeDesc;
                str[1] = gvDenomination + " - " + gvDenoDesc;
                str[2] = resultSet.getString(3);
              
                rsCollection.add(str);
            }
        }
        catch(Exception e)
        {
            throw (e);
        }
        finally
        {
            closeConnection();
        }
        
        return rsCollection;
     }
     
     private String getReasonCodeDescription(String coy, String coySub, String gvType, String rsnCd) throws Exception
     {
        if(gvType == null || gvType.trim().length() == 0 || rsnCd == null || rsnCd.trim().length() == 0) return "";
        
        GvrsnmstSQLAMY gvrsnmstSQL = new GvrsnmstSQLAMY(conn);
        gvrsnmstSQL.setCOY(coy);
        gvrsnmstSQL.setCOY_SUB(coySub);
        gvrsnmstSQL.setGV_TYPE(gvType);
        gvrsnmstSQL.setRSN_CD(rsnCd);
        gvrsnmstSQL.getByKey();
        
        return gvrsnmstSQL.RSN_DESC();
     }
     
     private String getTransOprName(String userId) throws Exception
     {
        if(userId == null || userId.trim().length() == 0) return "";
     
        AduserSQL aduserSQL = new AduserSQL(conn);
        aduserSQL.setUSR_ID(userId);
        aduserSQL.getByKey();
        
        String name = aduserSQL.USR_FIRST_NAME() + " " + aduserSQL.USR_LAST_NAME();
        
        return name;
     } 
     
     private String getVoucherTypeDescription(String coy, String coySub, String gvType) throws Exception
     {
        if(gvType == null || gvType.trim().length() == 0) return "";
        //GvtypemstSQLAMY gvtypemstSQL = new GvtypemstSQLAMY(conn);  
        if (gvtypemstSQL == null) gvtypemstSQL = new GvtypemstSQLAMY(conn); 
        gvtypemstSQL.setCOY(coy);
        gvtypemstSQL.setCOY_SUB(coySub);
        gvtypemstSQL.setGV_TYPE(gvType);
        gvtypemstSQL.getByKey();
        
        return gvtypemstSQL.GV_TYPE_DESC();
     }
     
     private String getVoucherDenoDescription(String coy, String coySub, String gvType, String gvDenomination) throws Exception
     {  
        try {
             if(gvType == null || gvType.trim().length() ==0 || gvDenomination == null || gvDenomination.trim().length() == 0) return "";
             
                  String qry = " SELECT GV_DENO_DESC FROM GVDENOMST WHERE COY=? AND COY_SUB=? AND GV_TYPE=? AND GV_DENOMINATION=? ";
                  pstmt2 = conn.prepareStatement(qry);
                  pstmt2.setString(1, coy);
                  pstmt2.setString(2, coySub);
                  pstmt2.setString(3, gvType);
                  pstmt2.setString(4, gvDenomination);
                  resultSet2 = pstmt2.executeQuery();  
        
              if(resultSet2.next())
              {
                  return resultSet2.getString("GV_DENO_DESC");
              }
                  return "";
            }
            finally
            {
              try
              {
                  if (resultSet2 != null)
                  resultSet2.close();
              } 
              catch (Exception Ex) { throw (Ex); }
              
              try 
              {
                  if (pstmt2 != null)
                  pstmt2.close();
              } 
              catch (Exception Ex) { throw (Ex); }
            }        
     }
    
    private void closeConnection()
    {
        try {
           if (resultSet != null)
              resultSet.close();
        } catch (Exception Ex) {}
        
        try {
           if (pstmt != null)
              pstmt.close();
        } catch (Exception Ex) {}
        
        try {
           if (conn != null)
              conn.close();
        } catch (Exception Ex) {}
        
        try {
           if (resultSet2 != null)
              resultSet2.close();
        } catch (Exception Ex) {}
            
        try {
            if (pstmt2 != null)
              pstmt2.close();
        } catch (Exception Ex) {}
    }
    
}