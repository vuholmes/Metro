/**

 * Copyright (C) 2002 QR Retail Autotamation Sdn. Bhd.

 * All right reserved.

 *

 */

/**
 * Synopsis: ITEMMSTLOG
 *
 * Written: Profit V7.0 Project Team: awkw.  2006-10-17
 *
 * Revised: Name/ Date.
 *
 */

package qrcom.PROFIT.files.info.logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Vector;

import qrcom.PROFIT.files.info.BaseInfo;
import qrcom.PROFIT.files.info.GvprogtypmstInfo;
import qrcom.PROFIT.files.info.GvprogtypmstlogSQL;

public class GvprogtypmstLogger implements BaseInfo {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private Connection conn = null;
    private GvprogtypmstlogSQL gvprogtypmstlogSQL = null;
    private java.util.Date utlDate = null;
    private Date sqlDate = null;

    public GvprogtypmstLogger(Connection cn) throws SQLException {
        conn = cn;
        utlDate = new java.util.Date();
        sqlDate = new Date(utlDate.getTime());
        gvprogtypmstlogSQL = new GvprogtypmstlogSQL(conn);
    }


    public void logEditRecord(GvprogtypmstInfo old_gvrsnmstInfo,
                              GvprogtypmstInfo new_gvrsnmstInfo) throws SQLException {
        populateToGvrsnmstlog(old_gvrsnmstInfo);
        gvprogtypmstlogSQL.setTRANS_DATE(sqlDate);
        gvprogtypmstlogSQL.setTRANS_TIME(dateFormat.format(utlDate));
        gvprogtypmstlogSQL.setTRANS_TYPE("0"); //0-Before, 1-After
        gvprogtypmstlogSQL.setITEM_CHANGE_TYPE("CHG"); /* NEW-New item, CHG-Changed, DEL-Deleted, ACT-Reactivated */
        gvprogtypmstlogSQL.insert();

        populateToGvrsnmstlog(new_gvrsnmstInfo);

        String whichColumnsBeingChanged = this.whichColumnBeingChange(old_gvrsnmstInfo, new_gvrsnmstInfo);
        gvprogtypmstlogSQL.setTRANS_DATE(sqlDate);
        gvprogtypmstlogSQL.setTRANS_TIME(dateFormat.format(utlDate));
        gvprogtypmstlogSQL.setTRANS_TYPE("1"); //0-Before, 1-After
        gvprogtypmstlogSQL.setITEM_CHANGE_TYPE("CHG"); /* NEW-New item, CHG-Changed, DEL-Deleted, ACT-Reactivated */
        gvprogtypmstlogSQL.setCHANGE_LOG(whichColumnsBeingChanged);
        gvprogtypmstlogSQL.insert();
    }


    public void logDeleteRecord(GvprogtypmstInfo old_gvprogtypmstInfo,
                                GvprogtypmstInfo new_gvprogtypmstInfo) throws SQLException {
        populateToGvrsnmstlog(old_gvprogtypmstInfo);
        gvprogtypmstlogSQL.setTRANS_DATE(sqlDate);
        gvprogtypmstlogSQL.setTRANS_TIME(dateFormat.format(utlDate));
        gvprogtypmstlogSQL.setTRANS_TYPE("0"); //0-Before, 1-After
        gvprogtypmstlogSQL.setITEM_CHANGE_TYPE("DEL"); /* NEW-New item, CHG-Changed, DEL-Deleted, ACT-Reactivated */
        gvprogtypmstlogSQL.insert();

        populateToGvrsnmstlog(new_gvprogtypmstInfo);

        String whichColumnsBeingChanged = this.whichColumnBeingChange(old_gvprogtypmstInfo, new_gvprogtypmstInfo);
        gvprogtypmstlogSQL.setTRANS_DATE(sqlDate);
        gvprogtypmstlogSQL.setTRANS_TIME(dateFormat.format(utlDate));
        gvprogtypmstlogSQL.setTRANS_TYPE("1"); //0-Before, 1-After
        gvprogtypmstlogSQL.setITEM_CHANGE_TYPE("DEL"); /* NEW-New item, CHG-Changed, DEL-Deleted, ACT-Reactivated */
        gvprogtypmstlogSQL.setCHANGE_LOG(whichColumnsBeingChanged);

        gvprogtypmstlogSQL.insert();
    }


    public void logInsertNewRecord(GvprogtypmstInfo gvrsnmstlogInfo) throws SQLException {
        populateToGvrsnmstlog(gvrsnmstlogInfo);
        gvprogtypmstlogSQL.setTRANS_DATE(sqlDate);
        utlDate = new java.util.Date();
        gvprogtypmstlogSQL.setTRANS_TIME(dateFormat.format(utlDate));
        gvprogtypmstlogSQL.setTRANS_TYPE("1"); //0-Before, 1-After
        gvprogtypmstlogSQL.setITEM_CHANGE_TYPE("NEW"); /* NEW-New item, CHG-Changed, DEL-Deleted, ACT-Reactivated */
        gvprogtypmstlogSQL.insert();
    }


    private void populateToGvrsnmstlog(GvprogtypmstInfo gvprogtypmstInfo) {
        String[][] mtx = gvprogtypmstInfo.getMatrix();
        Vector vctGvprogtypmst = gvprogtypmstInfo.getVObject();
        String value1 = "";

        for (int j = 0; j < mtx.length; j++) {
            if (vctGvprogtypmst.get(j) != null)
                value1 = vctGvprogtypmst.get(j).toString();
            else
                value1 = "";
            gvprogtypmstlogSQL.set(mtx[j][COL_NAME], value1);
        }
    }

    private String whichColumnBeingChange(GvprogtypmstInfo old_gvprogtypmstInfo, GvprogtypmstInfo new_gvprogtypmstInfo) {
        StringBuffer strbufColName = new StringBuffer(512);

        String[][] mtx = old_gvprogtypmstInfo.getMatrix();
        Vector vctInfo1 = old_gvprogtypmstInfo.getVObject();
        Vector vctInfo2 = new_gvprogtypmstInfo.getVObject();
        String value1 = null;
        String value2 = null;

        for (int j = 0; j < mtx.length; j++) {
            if (vctInfo1.get(j) != null)
                value1 = vctInfo1.get(j).toString();
            else
                value1 = "";

            if (vctInfo2.get(j) != null)
                value2 = vctInfo2.get(j).toString();
            else
                value2 = "";


            if (!value1.equals(value2)) {
                String[] str = new String[3];
                strbufColName.append(mtx[j][COL_NAME]).append(",");
            }
        }

        return strbufColName.toString();
    }
}
