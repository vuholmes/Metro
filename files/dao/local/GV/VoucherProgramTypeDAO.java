package qrcom.PROFIT.files.dao.local.GV;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import qrcom.PROFIT.files.dao.IF.shared.QueryBasedPagingDAOHandller;
import qrcom.PROFIT.files.info.GvprogtypmstInfo;
import qrcom.PROFIT.files.info.GvprogtypmstSQL;
import qrcom.PROFIT.files.info.logger.GvprogtypmstLogger;
import qrcom.PROFIT.shared.Utility.ExecuteQuery;
import qrcom.util.HParam;
import qrcom.util.ejb.connection.DataSource;
import qrcom.util.qrMisc;

public class VoucherProgramTypeDAO implements QueryBasedPagingDAOHandller {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;

    public VoucherProgramTypeDAO() {
    }

    public GvprogtypmstInfo findByPrimaryKey(String _strCoy, String _strCoySub, String _strGvType,
                                             String _strGvReasonCode, String strGvProgramType) throws Exception {
        GvprogtypmstSQL gvrsnmstSQL = new GvprogtypmstSQL();
        gvrsnmstSQL.setCOY(_strCoy);
        gvrsnmstSQL.setCOY_SUB(_strCoySub);
        gvrsnmstSQL.setGV_TYPE(_strGvType);
        gvrsnmstSQL.setRSN_CD(_strGvReasonCode);
        gvrsnmstSQL.setPROG_CD(strGvProgramType);
        if (gvrsnmstSQL.getByKey() == 0) {
            throw (new SQLException("USR_MSG=NE_SKU [ in GVRSNMST]"));
        }
        
        Vector vct = gvrsnmstSQL.getVObject();
        GvprogtypmstInfo info = new GvprogtypmstInfo();

        info.setVObject(vct);
        return info;
    }

    public Collection deleteVoucherProgramType(Collection coll_gvprogtypmst, String userId) throws Exception {
        conn = DataSource.getLocalConnection();
        conn.setAutoCommit(false);
        Collection errorSets = new Vector();
        GvprogtypmstInfo gvprogtypmstInfo;

        try {
            GvprogtypmstSQL newGvprogtypmstSQL;
            GvprogtypmstSQL oldGvprogtypmstSQL;
            GvprogtypmstLogger gvprogtypmstLogger;

            Iterator iter = coll_gvprogtypmst.iterator();
            while (iter.hasNext()) {
                newGvprogtypmstSQL = new GvprogtypmstSQL(conn);
                oldGvprogtypmstSQL = new GvprogtypmstSQL(conn);
                gvprogtypmstLogger = new GvprogtypmstLogger(conn);

                gvprogtypmstInfo = (GvprogtypmstInfo) iter.next();

                oldGvprogtypmstSQL.setVObject(gvprogtypmstInfo.getVObject());
                oldGvprogtypmstSQL.getByKey();

                newGvprogtypmstSQL.setCOY(gvprogtypmstInfo.COY());
                newGvprogtypmstSQL.setCOY_SUB(gvprogtypmstInfo.COY_SUB());
                newGvprogtypmstSQL.setGV_TYPE(gvprogtypmstInfo.GV_TYPE());
                newGvprogtypmstSQL.setRSN_CD(gvprogtypmstInfo.RSN_CD());
                newGvprogtypmstSQL.setPROG_CD(gvprogtypmstInfo.PROG_CD());
                if (newGvprogtypmstSQL.getByKeyForUpdate() == 0) {
                    errorSets.add("USR_MSG=NE_REC");
                } else {
                    newGvprogtypmstSQL.setDEL_CD("Y");
                    newGvprogtypmstSQL.setLAST_OPR(userId);
                    newGvprogtypmstSQL.setLAST_OPR_FUNCT("DEL_GV_PROGTYPE");
                    newGvprogtypmstSQL.setLAST_OPR_DATE(qrMisc.getSqlSysDate());
                    newGvprogtypmstSQL.setLAST_VERSION(System.currentTimeMillis());
                    newGvprogtypmstSQL.update();
                    gvprogtypmstLogger.logDeleteRecord(oldGvprogtypmstSQL, newGvprogtypmstSQL);
                }
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            errorSets.add(e.getMessage());
        } finally {
            closeConnection();
        }

        return errorSets;
    }
    
    public Collection updateVoucherProgramType(Vector vctGvprogtypmst) throws Exception {
        conn = DataSource.getLocalConnection();
        conn.setAutoCommit(false);
        Collection errorSets = new Vector();
        try {
            GvprogtypmstSQL newGvprogtypmstSQL = new GvprogtypmstSQL(conn);
            GvprogtypmstSQL currGvprogtypmstSQL = new GvprogtypmstSQL(conn);
            GvprogtypmstSQL tempGvprogtypmstSQL = new GvprogtypmstSQL(conn);
            GvprogtypmstLogger gvprogtypmstLogger = new GvprogtypmstLogger(conn);
            
            tempGvprogtypmstSQL.setVObject(vctGvprogtypmst);
            
            currGvprogtypmstSQL.setCOY(tempGvprogtypmstSQL.COY());
            currGvprogtypmstSQL.setCOY_SUB(tempGvprogtypmstSQL.COY_SUB());
            currGvprogtypmstSQL.setGV_TYPE(tempGvprogtypmstSQL.GV_TYPE());    
            currGvprogtypmstSQL.setRSN_CD(tempGvprogtypmstSQL.RSN_CD());
            currGvprogtypmstSQL.setPROG_CD(tempGvprogtypmstSQL.PROG_CD());
            
            if(currGvprogtypmstSQL.getByKeyForUpdate() == 0) {
                errorSets.add("USR_MSG=NE_REC");
            } else {
                newGvprogtypmstSQL.setVObject((Vector) currGvprogtypmstSQL.getVObject().clone());
                newGvprogtypmstSQL.setPROG_DESC(tempGvprogtypmstSQL.PROG_DESC());
                newGvprogtypmstSQL.setDEL_CD(tempGvprogtypmstSQL.DEL_CD());
                newGvprogtypmstSQL.setLAST_OPR_DATE(qrMisc.getSqlSysDate());
                newGvprogtypmstSQL.setLAST_OPR_FUNCT("EDIT_GV_PROGTYPE");
                newGvprogtypmstSQL.setLAST_VERSION(System.currentTimeMillis());
                newGvprogtypmstSQL.update();
                
                gvprogtypmstLogger.logEditRecord(currGvprogtypmstSQL,newGvprogtypmstSQL);
                conn.commit();
            }
        } catch (Exception e) {
            conn.rollback();
            errorSets.add(e.getMessage());
        } finally {
           closeConnection(); 
        }
        return errorSets;
    }

    public Collection insertVoucherProgramType(Vector vctGvprogtypmst) throws Exception {
        conn = DataSource.getLocalConnection();
        conn.setAutoCommit(false);
        Collection errorSets = new Vector();
        try {
            GvprogtypmstSQL newGvprogtypmstSQL = new GvprogtypmstSQL(conn);
            GvprogtypmstLogger gvprogtypmstLogger = new GvprogtypmstLogger(conn);

            newGvprogtypmstSQL.setVObject(vctGvprogtypmst);
            if (newGvprogtypmstSQL.checkKeyExist() > 0) {
                errorSets.add("USR_MSG=AE_REC");
            } else {
                newGvprogtypmstSQL.setLAST_OPR_DATE(qrMisc.getSqlSysDate());
                newGvprogtypmstSQL.setLAST_OPR_FUNCT("ADD_GV_PROGTYPE");
                newGvprogtypmstSQL.setLAST_VERSION(System.currentTimeMillis());
                newGvprogtypmstSQL.insert();
                
                gvprogtypmstLogger.logInsertNewRecord(newGvprogtypmstSQL);
                conn.commit();
            }
        } catch (Exception e) {
            conn.rollback();
            errorSets.add(e.getMessage());
        } finally {
           closeConnection(); 
        }
        return errorSets;
    }

    public HParam selectQueryBasedWebViewList(HParam hParam) throws Exception {
        Collection rsCollection = new Vector();
        long total_rows_selected = 0;
        String strCOY = (String) hParam.get("COY");
        String strCOY_SUB = (String) hParam.get("COY_SUB");
        String strVoucher_Type = (String) hParam.get("VOUCHER_TYPE");
        String strReason_Code = (String) hParam.get("REASON_CODE");
        String strProgram_Type = (String) hParam.get("PROGRAM_TYPE");
        String strProgram_Desc = (String) hParam.get("PROGRAM_DESC");
        String strDescMidSearch = (String) hParam.get("DESC_MID_SEARCH");
        String BEGIN_ROW_NUM = hParam.getString("BEGIN_ROW_NUM");
        String END_ROW_NUM = hParam.getString("END_ROW_NUM");

        String query = "";
        String selection =
            " SELECT P.COY, P.COY_SUB, T.GV_TYPE, T.GV_TYPE_DESC, P.RSN_CD, P.PROG_CD, P.PROG_DESC, P.DEL_CD, P.LAST_VERSION ";
        String from = " FROM GVPROGTYPMST P JOIN GVTYPEMST T ON P.GV_TYPE = T.GV_TYPE ";
        String criteria =
            " WHERE P.COY = T.COY AND P.COY_SUB = T.COY_SUB AND P.GV_TYPE = T.GV_TYPE AND P.COY='" + strCOY +
            "' AND P.COY_SUB ='" + strCOY_SUB + "' ";
        String order_by = " ORDER BY COY, COY_SUB, GV_TYPE, RSN_CD, PROG_CD ";

        if (strVoucher_Type != null && strVoucher_Type.trim().length() > 0) {
            criteria += " AND P.GV_TYPE = '" + strVoucher_Type + "'";
        }
        if (strReason_Code != null && strReason_Code.trim().length() > 0) {
            criteria += " AND P.RSN_CD = '" + strReason_Code + "'";
        }
        if (strProgram_Type != null && strProgram_Type.trim().length() > 0) {
            criteria += " AND P.PROG_CD = '" + strProgram_Type + "'";
        }
        if (strProgram_Desc != null && strProgram_Desc.trim().length() > 0) {
            if (strDescMidSearch != null && strDescMidSearch.equals("off")) {
                criteria += " AND P.PROG_DESC = '" + strProgram_Desc + "'";
            } else if (strDescMidSearch != null && strDescMidSearch.equals("on")) {
                criteria += " AND P.PROG_DESC LIKE '%" + strProgram_Desc + "%'";
            }
        }

        try {
            conn = DataSource.getLocalConnection();
            ExecuteQuery execQuery = new ExecuteQuery(conn);

            if (criteria.length() > 0) {
                query = "select count(rownum) " + from + criteria;
            } else {
                query = "select count(rownum) " + from;
            }
            String[] strQuery = execQuery.selectSingleRowQuery(query.toString(), 1);
            total_rows_selected = Long.parseLong(strQuery[0]);

            if (criteria.length() > 0) {
                query = selection + from + criteria + order_by;
            } else {
                query = selection + from + order_by;
            }
            String sql_query =
                "select * from (select V0.*, rownum CN from (" + query.toString() + ") V0) V1 where V1.CN between " +
                BEGIN_ROW_NUM + " and " + END_ROW_NUM;

            pstmt = conn.prepareStatement(sql_query);
            resultSet = pstmt.executeQuery();

            while (resultSet != null && resultSet.next()) {
                String[] str = new String[9];
                str[0] = resultSet.getString("GV_TYPE");
                str[1] = resultSet.getString("RSN_CD");
                str[2] = resultSet.getString("PROG_CD");
                str[3] = resultSet.getString("PROG_DESC");
                str[4] = resultSet.getString("DEL_CD");
                str[5] = resultSet.getString("LAST_VERSION");
                str[6] = resultSet.getString("COY");
                str[7] = resultSet.getString("COY_SUB");
                str[8] = resultSet.getString("GV_TYPE_DESC");
                rsCollection.add(str);
            }
        } catch (Exception e) {
            throw (e);
        } finally {
            closeConnection();
        }
        HParam rParam = new HParam(); // object for return keys(TOTAL_ROWS, COLLECTION)
        rParam.put("COLLECTION", rsCollection);
        rParam.put("TOTAL_ROWS", String.valueOf(total_rows_selected));
        return (rParam);
    }

    private void closeConnection() {
        try {
            if (pstmt != null)
                pstmt.close();
        } catch (Exception e) {
        }
        try {
            if (resultSet != null)
                resultSet.close();
        } catch (SQLException e) {
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
        }
    }
}
