package qrcom.PROFIT.reports.GV;

import java.io.*;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.Format;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import qrcom.PROFIT.files.info.*;
import qrcom.PROFIT.reports.*;

import qrcom.util.CurrencyConverter;
import qrcom.util.HParam;

public class VoucherRedeemExcelReportAMY extends GenericExcel {
    private static final Log log = LogFactory.getLog(VoucherRedeemExcelReportAMY.class);

    private static final String PROGRAM_VERSION = "2013-12-26 00:00 ABU";

    final short borderTHIN = HSSFCellStyle.BORDER_THIN;

    private int currentRow = 0;
    // variables to hold the user input values

    private String REPORT_HDR = "VOUCHER DOWNLOAD";

    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    private String strUsrId = null;
    
    private String SYSCompanyName = "";
    private String strCoy = null;
    private String strCoySub = null;
    private String strVouchType = null;
    private String strReportType = null;
    private String strDenomination = null;
    private String strStore = null;
    private String strRedeemDtFr = null;
    private String strRedeemDtTo = null;
    private String strEXISTSTORE = "";
    private String strTABLENAMESTORE = "";
    private String gvStore = "";
    private double dblTotal = 0;
    private double dblGrandTotal = 0;
    private double dblQty = 0;
    private double dblGrandQty = 0;

    private String strPrevStore = "";
    private String strPrevRedeemDt = "";
    private String strPrevPosNum = "";
    private String strPrevRecNum = "";
    private String strPrevCashId = "";
    private String strPrevDenom = "";

    private String strCurrStore = "";
    private String strCurrRedeemDt = "";
    private String strCurrPosNum = "";
    private String strCurrRecNum = "";
    private String strCurrCashId = "";
    private String strCurrDenom = "";

    // for indexing workmst
    private String TIME_CD = null;
    private String SESSION_ID = null;

    protected CurrencyConverter currencyConverter = null;
    protected int lineCodeCount = 0;
    protected HParam hParam_denom = new HParam();

    private String SYSVchrDisGL = "";
    private int maxPerSheet = 0;
    private int currentDataRow = 1;
    private boolean test_run = false;

    private PreparedStatement pstmtDEL = null;
    private PreparedStatement prepareStmt = null;
    private ResultSet rs = null;
    private PreparedStatement getCountQuery_ps = null;
    private ResultSet getCountQuery_rs = null;
    private PreparedStatement getStore_ps = null;
    private ResultSet getStore_rs = null;
    ProfitvvSQL profitvvSQL;

    private HSSFSheet sheet = null;
    private HSSFWorkbook workBook = null;
    private HSSFCellStyle subheaderstyle = null;
    private HSSFCellStyle subheaderstyle2 = null;
    private HSSFCellStyle headerstyle = null;
    private HSSFCellStyle cellstyle = null;
    private HSSFCellStyle cellNumberstyle = null;
    private HSSFCellStyle cellstyleTotal = null;
    private HSSFCellStyle cellNumberstyleTotal = null;
    private HSSFCellStyle CompanyTitleStyle = null;
    private HSSFCellStyle ReportTitleStyle = null;
    private HSSFCellStyle ReportCriteriaStyle = null;
    private HSSFCellStyle descellstyle = null;
    private HSSFCellStyle nodatastyle = null;

    private Region region = null;

    private Date date = new Date();

    private SimpleDateFormat reportGenTime = new SimpleDateFormat("yyyy-MM-dd");
    private Format formatter1 = new SimpleDateFormat("kk:mm");
    private String printdate = reportGenTime.format(date).toUpperCase();
    private String printtime = formatter1.format(date);
    
    public VoucherRedeemExcelReportAMY() {
    }

    public VoucherRedeemExcelReportAMY(String file_name) {
        super(file_name);
    }

    public VoucherRedeemExcelReportAMY(OutputStream out_stream) {
        super(out_stream);
    }


    private void initObjSQL() throws SQLException {
        profitvvSQL = new ProfitvvSQL(conn);
    }

    private void jInit(HParam hParam) throws Exception {
        strUsrId = hParam.getString("USER_ID");
        strCoy = hParam.getString("COY");
        strCoySub = hParam.getString("COY_SUB");
        strVouchType = hParam.getString("VOUCHER_TYPE");
        strReportType = hParam.getString("REPORT_TYPE");
        strVouchType = hParam.getString("VOUCHER_TYPE");
        strDenomination = hParam.getString("DENOMINATION");
        //strStore = hParam.getString("STORE");
        strRedeemDtFr = hParam.getString("FROM_REDEEM_DATE");
        strRedeemDtTo = hParam.getString("TO_REDEEM_DATE");

        //Added by Shriyle on 2016-04-12
        TIME_CD = hParam.getString("TIME_CD");
        SESSION_ID = hParam.getString("SESSION_ID"); // set by SvltRptProcessor.java
        strEXISTSTORE = hParam.getString("EXIST_STORE");
        strTABLENAMESTORE = "Redeem_Report";

        System.out.println("param: " + hParam);

        if (strEXISTSTORE != null && strEXISTSTORE.length() > 0) {
            strStore = getStore();
        }

        SYSVchrDisGL                = getProfitVV("SYSVchrDisGL", strCoy);
        SYSCompanyName              = getProfitVV("SYSCompanyName", strCoy);
        String strSYSUserLanguage   = getProfitVV("SYSUserLanguage", strCoy);
        String strSYSUserCountry    = getProfitVV("SYSUserCountry", strCoy);
        String strSYSRoundPlace     = getProfitVV("SYSRoundPlace", strCoy);
        String strSYSRoundMode      = getProfitVV("SYSRoundMode", strCoy);
        String strSYSDecimalDisplay = getProfitVV("SYSDecimalDisplay", strCoy);

        // get the max line per sheet
        maxPerSheet = Integer.parseInt(getProfitVV("SYSMaxLinesPerSheet", strCoy));
        
        currencyConverter =
            new CurrencyConverter(strSYSUserLanguage, strSYSUserCountry, Integer.parseInt(strSYSRoundPlace),
                                  Integer.parseInt(strSYSRoundMode), Integer.parseInt(strSYSDecimalDisplay));
        currencyConverter.setGrouping(false);

        // create a new workbook
        workBook = new HSSFWorkbook();
    }

    public String getProfitVV(String vnm, String coy) throws SQLException {
        profitvvSQL.setVNM(vnm);
        profitvvSQL.setCOY(coy);
        profitvvSQL.getByKey();

        return (profitvvSQL.VNM_VDTVL());
    }

    public void print(HParam hParam) {
        try {
            openOutputStream();
            openConnection();
            initObjSQL();
            jInit(hParam);
            System.out.println("start writing the report...");
            start();

            workBook.write(super.outStream);
            System.out.println("end of writing the report...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStatement();
            try {
                if (workBook != null) {
                    workBook = null;
                }
            } catch (Exception e) {
            }

            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception Ex) {
            }

            try {
                if (prepareStmt != null) {
                    prepareStmt.close();
                    prepareStmt = null;
                }
            } catch (Exception Ex) {
            }

            try {
                if (getCountQuery_ps != null) {
                    getCountQuery_ps.close();
                    getCountQuery_ps = null;
                }
            } catch (Exception Ex) {
            }

            try {
                if (getCountQuery_rs != null) {
                    getCountQuery_rs.close();
                    getCountQuery_rs = null;
                }
            } catch (Exception Ex) {
            }

            closeConnection();
            super.closeOutputStream();
        }
    }

    public void start() throws Exception {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
            System.out.println("start to print ..." + sdf.format(cal.getTime()));
            long start = System.currentTimeMillis();

            //create a style for the header cell
            setHeaderStyle();
            setForCellStyle();
            setForDesCellStyle();
            setForCellNumberStyle();
            setForCellStyleTotal();
            setForCellNumberStyleTotal();
            setFirstHeaderStyle();
            setFirstHeaderStyle2();
            setSubHeaderStyle();
            setSubHeaderStyle2();
            setCriteria();
            setNoDataStyle();

            if (strReportType.equals("D")) {
                process_sheet_detail();
            } else if (strReportType.equals("S")) {
                process_sheet_summary();
            }

            System.out.println("finished print ..." + sdf.format(cal.getTime()));
            long end = System.currentTimeMillis();
            long duration = end - start;
            System.out.println("print time is: " + duration / 1000 + " secs");
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    private void process_sheet_detail() throws Exception {
        String query =
            "SELECT M.REDEEM_STORE, M.REDEEM_DATE, D.GV_TYPE, T.GV_TYPE_DESC, D.GV_DENOMINATION, D.GV_DENO_DESC, M.GV_NO, M.EXPIRY_DATE, " +
            "M.REDEEM_POS_NO, M.REDEEM_POS_TRANS, H.CASHIER_ID, 1 AS QTY, D.AMOUNT   " +
            "FROM GVMSTR M LEFT JOIN SLSTXHDR H " + "ON  M.COY = H.COY " + "AND M.COY_SUB = H.COY_SUB " +
            "AND M.REDEEM_STORE = H.STORE " + "AND M.REDEEM_DATE = H.SALES_TRANS_DATE " +
            "AND M.REDEEM_POS_NO = H.STORE_POS_NUMBER " +
            "AND M.REDEEM_POS_TRANS = H.STORE_POS_TRANS, GVTYPEMST T, GVDENOMST D, ADOPRSTR A " +
            "WHERE M.COY = T.COY " + "AND M.COY_SUB = T.COY_SUB " + "AND M.GV_TYPE = T.GV_TYPE  " +
            "AND M.COY = D.COY " + "AND M.COY_SUB = D.COY_SUB " + "AND M.GV_TYPE = D.GV_TYPE " +
            "AND M.GV_DENOMINATION = D.GV_DENOMINATION " + "AND M.REDEEM_DATE IS NOT NULL " +
            "AND M.REDEEM_STORE = A.STORE " + "AND A.USR_ID = '" + strUsrId + "' " + "AND M.COY = '" + strCoy + "' " +
            "AND M.COY_SUB = '" + strCoySub + "' " + "AND M.GV_TYPE =   '" + strVouchType + "' ";
        if (strDenomination.trim().length() > 0) {
            query = query + "AND M.GV_DENOMINATION = '" + strDenomination + "' ";
        }
        if ((strEXISTSTORE != null && strEXISTSTORE.length() > 0) && (strStore != null && strStore.length() > 0)) {
            query = query + "AND M.REDEEM_STORE IN (" + strStore + ") ";
        }
        if (strRedeemDtFr.trim().length() > 0 && strRedeemDtTo.trim().length() > 0) {
            query =
                query + "AND M.REDEEM_DATE BETWEEN TO_DATE('" + strRedeemDtFr + "','YYYY-MM-DD') AND TO_DATE('" +
                strRedeemDtTo + "','YYYY-MM-DD') ";
        }

        query =
            query + "ORDER BY D.GV_TYPE, D.GV_DENOMINATION, M.REDEEM_DATE, M.REDEEM_STORE, M.REDEEM_POS_NO,  " +
            "M.REDEEM_POS_TRANS, M.GV_NO ";

        System.out.println("query = " + query);
        prepareStmt = conn.prepareStatement(query);
        rs = prepareStmt.executeQuery();

        HSSFRow headerRow = null;
        boolean create_hdr = true;
        boolean blnIsCreateDenom = false;
        int sheetCreated = 1;
        boolean blnPrintNoData = false;

        if (!rs.isBeforeFirst()) //check is it no data
        {
            blnPrintNoData = true;
            createSheetDetail(getSheetTitle(strReportType) + sheetCreated);

            createPrintingInfo();
            createFirstHdrDetail(); //detail
            sheetCreated++;

            HSSFCell headerCell = null;

            region = new Region(currentRow + 1, (short) 0, currentRow + 1, (short) 8);
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(currentRow + 1);
            headerCell = headerRow.createCell((short) 0);
            headerCell.setCellValue(getDescription("NO DATA TO PRINT"));
            headerCell.setCellStyle(nodatastyle);
        }

        while (rs != null && rs.next()) {
            setCurrValue(rs);

            if (currentRow == 0 && create_hdr) {
                createSheetDetail(getSheetTitle(strReportType) + sheetCreated);
                createPrintingInfo();
                createFirstHdrDetail(); //detail
                sheetCreated++;

                setPrevValue(rs);

                currentRow++;
                headerRow = sheet.createRow(currentRow);
                createDenomRow(headerRow, rs);
            }

            //create total
            if (isCreateTotal()) {
                currentRow++;
                headerRow = sheet.createRow(currentRow);
                createTotalRow(headerRow, rs);
                dblGrandTotal = dblGrandTotal + dblTotal;
                dblTotal = 0;
                dblGrandQty = dblGrandQty + dblQty;
                dblQty = 0;
            }

            //create grand total
            if (!strCurrDenom.equals(strPrevDenom)) {
                currentRow++;
                headerRow = sheet.createRow(currentRow);
                createGrandTotalRow(headerRow, rs);
                dblGrandTotal = 0;
                dblGrandQty = 0;
                blnIsCreateDenom = true;
            }

            if (blnIsCreateDenom) {
                currentRow++;
                headerRow = sheet.createRow(currentRow);
                createDenomRow(headerRow, rs);
                blnIsCreateDenom = false;
            }

            setPrevValue(rs);

            currentRow++;
            headerRow = sheet.createRow(currentRow);
            createSummDataDetail(headerRow, rs);
            dblTotal = dblTotal + Double.parseDouble(rs.getString("AMOUNT"));
            dblQty = dblQty + Double.parseDouble(rs.getString("QTY"));

            if (currentDataRow > maxPerSheet) {
                currentDataRow = 1;
                currentRow = 0;
            }
        }

        if (!blnPrintNoData) {
            //create total at last row
            currentRow++;
            headerRow = sheet.createRow(currentRow);
            createTotalRow(headerRow, rs);
            dblGrandTotal = dblGrandTotal + dblTotal;
            dblTotal = 0;
            dblGrandQty = dblGrandQty + dblQty;
            dblQty = 0;

            //create grand total at last row
            currentRow++;
            headerRow = sheet.createRow(currentRow);
            createGrandTotalRow(headerRow, rs);
            dblGrandTotal = 0;
            dblGrandQty = 0;
        }

    }

    private void process_sheet_summary() throws Exception {
        String getCountQuery =
            "SELECT GV_DENOMINATION " +
            //                              "SELECT Count(*) "+
            "FROM GVDENOMST	 " + "WHERE COY = '" + strCoy + "' " + "AND COY_SUB = '" + strCoySub + "' " +
                          "AND GV_TYPE = '" + strVouchType + "' ";
        if (strDenomination.trim().length() > 0) {
            getCountQuery = getCountQuery + "AND GV_DENOMINATION = '" + strDenomination + "' ";
        }

        getCountQuery_ps = conn.prepareStatement(getCountQuery);
        getCountQuery_rs = getCountQuery_ps.executeQuery();

        while (getCountQuery_rs != null && getCountQuery_rs.next()) {
            lineCodeCount++;
            hParam_denom.put("denom" + lineCodeCount, getCountQuery_rs.getString(1));
        }


        String query = "SELECT A.REDEEM_STORE, A.REDEEM_DATE, A.REDEEM_POS_NO, A.REDEEM_POS_TRANS, A.CASHIER_ID ";

        if (lineCodeCount != 0) {
            for (int j = 1; j <= lineCodeCount; j++) {
                query = query + ", ";
                query =
                    query + "Nvl(Sum(Decode(A.GV_DENOMINATION, '" + (String) hParam_denom.get("denom" + j) +
                    "', A.QTY)),0) AS QTY" + j + ", Nvl(Sum(Decode(A.GV_DENOMINATION, '" +
                    (String) hParam_denom.get("denom" + j) + "', A.AMOUNT)),0) AS AMT" + j + " ";
            }
        }

        query =
            query + "FROM " + "( " +
            "SELECT M.REDEEM_STORE, M.REDEEM_DATE, D.GV_TYPE, T.GV_TYPE_DESC, D.GV_DENOMINATION, D.GV_DENO_DESC, M.GV_NO, M.EXPIRY_DATE,  " +
            "M.REDEEM_POS_NO, M.REDEEM_POS_TRANS, H.CASHIER_ID, 1 AS QTY, D.AMOUNT   " +
            "FROM GVMSTR M LEFT JOIN SLSTXHDR H " + "ON  M.COY = H.COY " + "AND M.COY_SUB = H.COY_SUB " +
            "AND M.REDEEM_STORE = H.STORE " + "AND M.REDEEM_DATE = H.SALES_TRANS_DATE " +
            "AND M.REDEEM_POS_NO = H.STORE_POS_NUMBER " +
            "AND M.REDEEM_POS_TRANS = H.STORE_POS_TRANS, GVTYPEMST T, GVDENOMST D, ADOPRSTR A  " +
            "WHERE M.COY = T.COY " + "AND M.COY_SUB = T.COY_SUB " + "AND M.GV_TYPE = T.GV_TYPE  " +
            "AND M.COY = D.COY " + "AND M.COY_SUB = D.COY_SUB " + "AND M.GV_TYPE = D.GV_TYPE " +
            "AND M.GV_DENOMINATION = D.GV_DENOMINATION " + "AND M.REDEEM_DATE IS NOT NULL " +
            "AND M.REDEEM_STORE = A.STORE " + "AND A.USR_ID = '" + strUsrId + "' " + "AND M.COY = '" + strCoy + "' " +
            "AND M.COY_SUB = '" + strCoySub + "' " + "AND M.GV_TYPE =   '" + strVouchType + "' ";
        if (strDenomination.trim().length() > 0) {
            query = query + "AND M.GV_DENOMINATION = '" + strDenomination + "' ";
        }
        if ((strEXISTSTORE != null && strEXISTSTORE.length() > 0) && (strStore != null && strStore.length() > 0)) {
            query = query + "AND M.REDEEM_STORE IN (" + strStore + ") ";
        }
        if (strRedeemDtFr.trim().length() > 0 && strRedeemDtTo.trim().length() > 0) {
            query =
                query + "AND M.REDEEM_DATE BETWEEN To_Date('" + strRedeemDtFr + "','YYYY-MM-DD') AND To_Date('" +
                strRedeemDtTo + "','YYYY-MM-DD') ";
        }
        query =
            query + ") A " +
            "GROUP BY A.REDEEM_STORE, A.REDEEM_DATE, A.REDEEM_POS_NO, A.REDEEM_POS_TRANS, A.CASHIER_ID " +
            "ORDER BY A.REDEEM_DATE, A.REDEEM_STORE, A.REDEEM_POS_NO, A.REDEEM_POS_TRANS, A.CASHIER_ID ";

        System.out.println("query = " + query);
        prepareStmt = conn.prepareStatement(query);
        rs = prepareStmt.executeQuery();

        HSSFRow headerRow = null;
        boolean create_hdr = true;
        boolean blnIsCreateDenom = false;
        int sheetCreated = 1;

        if (!rs.isBeforeFirst()) //check is it no data
        {
            createSheetSummary(getSheetTitle(strReportType) + sheetCreated);

            createPrintingInfo(); //summary
            createFirstHdrSummary(); //summary
            sheetCreated++;

            HSSFCell headerCell = null;

            int lastCol = 0;
            if (lineCodeCount != 0) {
                lastCol = 4 + (2 * lineCodeCount);
            } else {
                lastCol = 4;
            }

            region = new Region(currentRow + 1, (short) 0, currentRow + 1, (short) lastCol);
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(currentRow + 1);
            headerCell = headerRow.createCell((short) 0);
            headerCell.setCellValue(getDescription("NO DATA TO PRINT"));
            headerCell.setCellStyle(nodatastyle);
        }

        while (rs != null && rs.next()) {
            //            setCurrValue(rs);

            if (currentRow == 0 && create_hdr) {
                createSheetSummary(getSheetTitle(strReportType) + sheetCreated);
                createPrintingInfo();
                createFirstHdrSummary(); //summary
                sheetCreated++;

                //              setPrevValue(rs);

                //              currentRow++;
                //              headerRow = sheet.createRow(currentRow);
                //              createDenomRow(headerRow,rs);
            }

            //            //create total
            //            if(isCreateTotal())
            //            {
            //              currentRow++;
            //              headerRow = sheet.createRow(currentRow);
            //              createTotalRow(headerRow,rs);
            //              dblGrandTotal = dblGrandTotal + dblTotal;
            //              dblTotal = 0;
            //            }

            //            //create grand total
            //            if(!strCurrDenom.equals(strPrevDenom))
            //            {
            //              currentRow++;
            //              headerRow = sheet.createRow(currentRow);
            //              createGrandTotalRow(headerRow,rs);
            //              dblGrandTotal = 0;
            //              blnIsCreateDenom=true;
            //            }

            //            if(blnIsCreateDenom)
            //            {
            //                currentRow++;
            //                headerRow = sheet.createRow(currentRow);
            //                createDenomRow(headerRow,rs);
            //                blnIsCreateDenom=false;
            //            }

            //            setPrevValue(rs);

            currentRow++;
            headerRow = sheet.createRow(currentRow);
            createSummDataSummary(headerRow, rs);
            //            dblTotal = dblTotal+Double.parseDouble(rs.getString("AMOUNT"));

            if (currentDataRow > maxPerSheet) {
                currentDataRow = 1;
                currentRow = 0;
            }
        }

        //      if(rs.isBeforeFirst())
        //      {
        //        //create total at last row
        //        currentRow++;
        //        headerRow = sheet.createRow(currentRow);
        //        createTotalRow(headerRow,rs);
        //        dblGrandTotal = dblGrandTotal + dblTotal;
        //        dblTotal = 0;
        //
        //        //create grand total at last row
        //        currentRow++;
        //        headerRow = sheet.createRow(currentRow);
        //        createGrandTotalRow(headerRow,rs);
        //        dblGrandTotal = 0;
        //      }

    }

    private void setPrevValue(ResultSet rs) throws Exception {
        if (rs.getString("REDEEM_STORE") != null)
            strPrevStore = rs.getString("REDEEM_STORE");
        else
            strPrevStore = "";
        if (reportGenTime.format(rs.getDate("REDEEM_DATE")) != null)
            strPrevRedeemDt = reportGenTime.format(rs.getDate("REDEEM_DATE"));
        else
            strPrevRedeemDt = "";
        if (rs.getString("REDEEM_POS_NO") != null)
            strPrevPosNum = rs.getString("REDEEM_POS_NO");
        else
            strPrevPosNum = "";
        if (rs.getString("REDEEM_POS_TRANS") != null)
            strPrevRecNum = rs.getString("REDEEM_POS_TRANS");
        else
            strPrevRecNum = "";
        if (rs.getString("CASHIER_ID") != null)
            strPrevCashId = rs.getString("CASHIER_ID");
        else
            strPrevCashId = "";
        if (rs.getString("GV_DENOMINATION") != null)
            strPrevDenom = rs.getString("GV_DENOMINATION");
        else
            strPrevDenom = "";
    }

    private void setCurrValue(ResultSet rs) throws Exception {
        if (rs.getString("REDEEM_STORE") != null)
            strCurrStore = rs.getString("REDEEM_STORE");
        else
            strCurrStore = "";
        if (reportGenTime.format(rs.getDate("REDEEM_DATE")) != null)
            strCurrRedeemDt = reportGenTime.format(rs.getDate("REDEEM_DATE"));
        else
            strCurrRedeemDt = "";
        if (rs.getString("REDEEM_POS_NO") != null)
            strCurrPosNum = rs.getString("REDEEM_POS_NO");
        else
            strCurrPosNum = "";
        if (rs.getString("REDEEM_POS_TRANS") != null)
            strCurrRecNum = rs.getString("REDEEM_POS_TRANS");
        else
            strCurrRecNum = "";
        if (rs.getString("CASHIER_ID") != null)
            strCurrCashId = rs.getString("CASHIER_ID");
        else
            strCurrCashId = "";
        if (rs.getString("GV_DENOMINATION") != null)
            strCurrDenom = rs.getString("GV_DENOMINATION");
        else
            strCurrDenom = "";
    }

    private boolean isCreateTotal() {
        boolean blnIsCreateTotal = false;

        if (!strPrevStore.equals(strCurrStore) || !strPrevRedeemDt.equals(strCurrRedeemDt) ||
            !strPrevPosNum.equals(strCurrPosNum) || !strPrevRecNum.equals(strCurrRecNum) ||
            !strPrevCashId.equals(strCurrCashId) || !strCurrDenom.equals(strPrevDenom)) {
            blnIsCreateTotal = true;
        }

        return blnIsCreateTotal;
    }

    private void createGrandTotalRow(HSSFRow headerRow, ResultSet rs) throws Exception {
        try {
            HSSFCell headerCell = null;
            int k = 0;

            if(SYSVchrDisGL.equalsIgnoreCase("N")) {
                region = new Region(currentRow, (short) 0, currentRow, (short) 5);
            } else {
                region = new Region(currentRow, (short) 0, currentRow, (short) 4);
            }
            
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(currentRow);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            if(SYSVchrDisGL.equalsIgnoreCase("N")) {
                headerCell = headerRow.createCell((short) k++);
                headerCell.setCellValue("");
                headerCell.setCellStyle(descellstyle);
            }

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("Grand Total");
            headerCell.setCellStyle(subheaderstyle2);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(dblGrandQty);
            headerCell.setCellStyle(subheaderstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(currencyConverter.format(dblGrandTotal));
            headerCell.setCellStyle(subheaderstyle);

            currentDataRow++;
        } catch (Exception e) {
            throw (e);
        }
    }

    private void createDenomRow(HSSFRow headerRow, ResultSet rs) throws Exception {
        try {
            HSSFCell headerCell = null;
            int k = 0;

            if(SYSVchrDisGL.equalsIgnoreCase("N")) {
                region = new Region(currentRow, (short) 0, currentRow, (short) 8);
            } else {
                region = new Region(currentRow, (short) 0, currentRow, (short) 7);
            }

            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(currentRow);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("Denomination: " + rs.getString("GV_DENO_DESC"));
            headerCell.setCellStyle(subheaderstyle2);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(subheaderstyle2);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(subheaderstyle2);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(subheaderstyle2);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(subheaderstyle2);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(subheaderstyle2);
            
            if(SYSVchrDisGL.equalsIgnoreCase("N")) {
                headerCell = headerRow.createCell((short) k++);
                headerCell.setCellValue("");
                headerCell.setCellStyle(subheaderstyle2);
            }

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(subheaderstyle2);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(subheaderstyle2);

            currentDataRow++;
        } catch (Exception e) {
            throw (e);
        }
    }

    private void createTotalRow(HSSFRow headerRow, ResultSet rs) throws Exception {
        try {
            HSSFCell headerCell = null;
            int k = 0;

            if(SYSVchrDisGL.equalsIgnoreCase("N")) {
                region = new Region(currentRow, (short) 0, currentRow, (short) 5);
            } else {
                region = new Region(currentRow, (short) 0, currentRow, (short) 4);
            }
            
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(currentRow);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("");
            headerCell.setCellStyle(descellstyle);

            if(SYSVchrDisGL.equalsIgnoreCase("N")) {
                headerCell = headerRow.createCell((short) k++);
                headerCell.setCellValue("");
                headerCell.setCellStyle(descellstyle);
            }

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue("Total");
            headerCell.setCellStyle(subheaderstyle2);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(dblQty);
            headerCell.setCellStyle(subheaderstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(currencyConverter.format(dblTotal));
            headerCell.setCellStyle(subheaderstyle);

            currentDataRow++;
        } catch (Exception e) {
            throw (e);
        }
    }

    private void createSummDataSummary(HSSFRow headerRow, ResultSet rs) throws Exception {
        try {
            HSSFCell headerCell = null;
            int k = 0;

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(rs.getString("REDEEM_STORE"));
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            if (rs.getDate("REDEEM_DATE") == null) {
                headerCell.setCellValue("");
            } else {
                headerCell.setCellValue(reportGenTime.format(rs.getDate("REDEEM_DATE")));
            }
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(rs.getString("REDEEM_POS_NO"));
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(rs.getString("REDEEM_POS_TRANS"));
            headerCell.setCellStyle(descellstyle);

            if(SYSVchrDisGL.equalsIgnoreCase("N")) {
                headerCell = headerRow.createCell((short) k++);
                headerCell.setCellValue(rs.getString("CASHIER_ID"));
                headerCell.setCellStyle(descellstyle);    
            }

            for (int j = 1; j <= lineCodeCount; j++) {
                headerCell = headerRow.createCell((short) k++);
                headerCell.setCellValue(Integer.parseInt(rs.getString("QTY" + j)));
                headerCell.setCellStyle(cellNumberstyle);

                headerCell = headerRow.createCell((short) k++);
                headerCell.setCellValue(Integer.parseInt(rs.getString("AMT" + j)));
                headerCell.setCellStyle(cellNumberstyle);
            }

            currentDataRow++;
        } catch (Exception e) {
            throw (e);
        }
    }

    private void createSummDataDetail(HSSFRow headerRow, ResultSet rs) throws Exception {
        try {
            HSSFCell headerCell = null;
            int k = 0;

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(rs.getString("REDEEM_STORE"));
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            if (rs.getDate("REDEEM_DATE") == null) {
                headerCell.setCellValue("");
            } else {
                headerCell.setCellValue(reportGenTime.format(rs.getDate("REDEEM_DATE")));
            }
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(rs.getString("GV_NO"));
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            if (rs.getDate("EXPIRY_DATE") == null) {
                headerCell.setCellValue("");
            } else {
                headerCell.setCellValue(reportGenTime.format(rs.getDate("EXPIRY_DATE")));
            }
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(rs.getString("REDEEM_POS_NO"));
            headerCell.setCellStyle(descellstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(rs.getString("REDEEM_POS_TRANS"));
            headerCell.setCellStyle(descellstyle);

            if(SYSVchrDisGL.equalsIgnoreCase("N")) {
                headerCell = headerRow.createCell((short) k++);
                headerCell.setCellValue(rs.getString("CASHIER_ID"));
                headerCell.setCellStyle(descellstyle);
            }

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(Integer.parseInt(rs.getString("QTY")));
            headerCell.setCellStyle(cellNumberstyle);

            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(Integer.parseInt(rs.getString("AMOUNT")));
            headerCell.setCellStyle(cellNumberstyle);

            currentDataRow++;
        } catch (Exception e) {
            throw (e);
        }
    }

    private void createFirstHdrDetail() throws Exception {
        HSSFRow headerRow = null;
        HSSFCell headerCell = null;

        int headerRowStart = 7;
        int k = 0;

        headerRow = sheet.createRow(headerRowStart);

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(getTranslatedCaptionMsg("Store"));
        headerCell.setCellStyle(headerstyle);

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(getTranslatedCaptionMsg("Date Redeemed"));
        headerCell.setCellStyle(headerstyle);

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Number"));
        headerCell.setCellStyle(headerstyle);

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(getTranslatedCaptionMsg("Expiry Date"));
        headerCell.setCellStyle(headerstyle);

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(getTranslatedCaptionMsg("POS Number"));
        headerCell.setCellStyle(headerstyle);

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(getTranslatedCaptionMsg("Receipt Number"));
        headerCell.setCellStyle(headerstyle);

        if(SYSVchrDisGL.equalsIgnoreCase("N")) {
            headerCell = headerRow.createCell((short) k++);
            headerCell.setCellValue(getTranslatedCaptionMsg("Cashier ID"));
            headerCell.setCellStyle(headerstyle);
        }

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(getTranslatedCaptionMsg("Quantity"));
        headerCell.setCellStyle(headerstyle);

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(getTranslatedCaptionMsg("Amount"));
        headerCell.setCellStyle(headerstyle);

        currentRow = headerRowStart;
    }

    private void createFirstHdrSummary() throws Exception {
        HSSFRow headerRow = null;
        HSSFCell headerCell = null;

        int headerRowStart = 7;
        int k = 0;

        headerRow = sheet.createRow(headerRowStart);

        region = new Region(headerRowStart, (short) k, headerRowStart + 1, (short) k);
        sheet.addMergedRegion(region);
        setRegionMethod();
        headerRow = sheet.createRow(headerRowStart);
        headerCell = headerRow.createCell((short) k);
        headerCell.setCellValue("Store");
        headerCell.setCellStyle(headerstyle);

        k++;
        region = new Region(headerRowStart, (short) k, headerRowStart + 1, (short) k);
        sheet.addMergedRegion(region);
        setRegionMethod();
        headerRow = sheet.createRow(headerRowStart);
        headerCell = headerRow.createCell((short) k);
        headerCell.setCellValue(getTranslatedCaptionMsg("Date Redeemed"));
        headerCell.setCellStyle(headerstyle);

        k++;
        region = new Region(headerRowStart, (short) k, headerRowStart + 1, (short) k);
        sheet.addMergedRegion(region);
        setRegionMethod();
        headerRow = sheet.createRow(headerRowStart);
        headerCell = headerRow.createCell((short) k);
        headerCell.setCellValue(getTranslatedCaptionMsg("POS Number"));
        headerCell.setCellStyle(headerstyle);

        k++;
        region = new Region(headerRowStart, (short) k, headerRowStart + 1, (short) k);
        sheet.addMergedRegion(region);
        setRegionMethod();
        headerRow = sheet.createRow(headerRowStart);
        headerCell = headerRow.createCell((short) k);
        headerCell.setCellValue(getTranslatedCaptionMsg("Receipt Number"));
        headerCell.setCellStyle(headerstyle);

        if(SYSVchrDisGL.equalsIgnoreCase("N")) {
            k++;
            region = new Region(headerRowStart, (short) k, headerRowStart + 1, (short) k);
            sheet.addMergedRegion(region);
            setRegionMethod();
            headerRow = sheet.createRow(headerRowStart);
            headerCell = headerRow.createCell((short) k);
            headerCell.setCellValue(getTranslatedCaptionMsg("Cashier ID"));
            headerCell.setCellStyle(headerstyle);
        }

        k++;
        for (int j = 1; j <= lineCodeCount; j++) {
            region = new Region(headerRowStart, (short) k, headerRowStart, (short) (k + 1));
            sheet.addMergedRegion(region);
            setRegionMethod();
            headerRow = sheet.createRow(headerRowStart);
            headerCell = headerRow.createCell((short) k);
            headerCell.setCellValue((String) hParam_denom.get("denom" + j) + " - " +
                                    getDenomDesc((String) hParam_denom.get("denom" + j)));
            headerCell.setCellStyle(headerstyle);

            headerRow = sheet.createRow((headerRowStart + 1));
            headerCell = headerRow.createCell((short) k);
            headerCell.setCellValue(getTranslatedCaptionMsg("Quantity"));
            headerCell.setCellStyle(headerstyle);

            headerRow = sheet.createRow((headerRowStart + 1));
            headerCell = headerRow.createCell((short) (k + 1));
            headerCell.setCellValue(getTranslatedCaptionMsg("Amount"));
            headerCell.setCellStyle(headerstyle);

            k = k + 2;
        }

        currentRow = headerRowStart + 1;
    }

    private void createPrintingInfo() throws Exception {
        HSSFRow headerRow = null;
        HSSFCell headerCell = null;

        region = new Region(0, (short) 0, 0, (short) 4);
        sheet.addMergedRegion(region);
        headerRow = sheet.createRow(0);
        headerCell = headerRow.createCell((short) 0);
        headerCell.setCellValue(getTranslatedCaptionMsg(SYSCompanyName));
        headerCell.setCellStyle(CompanyTitleStyle);

        region = new Region(2, (short) 0, 2, (short) 4);
        sheet.addMergedRegion(region);
        headerRow = sheet.createRow(2);
        headerCell = headerRow.createCell((short) 0);
        headerCell.setCellValue(getTranslatedReportMsg("Voucher Redeemed Report"));
        headerCell.setCellStyle(ReportTitleStyle);

        region = new Region(4, (short) 0, 4, (short) 4);
        sheet.addMergedRegion(region);
        headerRow = sheet.createRow(4);
        headerCell = headerRow.createCell((short) 0);
        headerCell.setCellValue(getTranslatedCaptionMsg("Report Type") + ": " + getDescription(getReportTypeDesc(strReportType)));
        headerCell.setCellStyle(ReportCriteriaStyle);

        region = new Region(5, (short) 0, 5, (short) 4);
        sheet.addMergedRegion(region);
        headerRow = sheet.createRow(5);
        headerCell = headerRow.createCell((short) 0);
        headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Type") + ": " + strVouchType + " - " +
                                getDescription(getVouchTypeDesc(strVouchType)));
        headerCell.setCellStyle(ReportCriteriaStyle);

        System.out.println("strReportType = " + strReportType);
        if (strReportType.equals("S")) {
            int lastCol = 0;
            if (lineCodeCount != 0) {
                lastCol = 4 + (2 * lineCodeCount);
            } else {
                lastCol = 6;
            }

            region = new Region(4, (short) (lastCol - 1), 4, (short) lastCol);
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(4);
            headerCell = headerRow.createCell((short) (lastCol - 1));
            headerCell.setCellValue(getTranslatedCaptionMsg("Printed By") + ": " + strUsrId);
            headerCell.setCellStyle(ReportCriteriaStyle);
            //
            //          region = new Region(4, (short) lastCol, 4, (short)lastCol);
            //          sheet.addMergedRegion(region);
            //          headerRow = sheet.createRow(4);
            //          headerCell = headerRow.createCell((short) lastCol);
            //          headerCell.setCellValue(strUsrId);
            //          headerCell.setCellStyle(ReportCriteriaStyle);
            //
            region = new Region(5, (short) (lastCol - 1), 5, (short) lastCol);
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(5);
            headerCell = headerRow.createCell((short) (lastCol - 1));
            headerCell.setCellValue(getTranslatedCaptionMsg("Printed Date") + ": " + printdate);
            headerCell.setCellStyle(ReportCriteriaStyle);

            //          region = new Region(5, (short) lastCol, 5, (short)lastCol);
            //          sheet.addMergedRegion(region);
            //          headerRow = sheet.createRow(5);
            //          headerCell = headerRow.createCell((short) lastCol);
            //          headerCell.setCellValue(printdate);
            //          headerCell.setCellStyle(ReportCriteriaStyle);
        } else {
            region = new Region(4, (short) 7, 4, (short) 7);
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(4);
            headerCell = headerRow.createCell((short) 7);
            headerCell.setCellValue(getTranslatedCaptionMsg("Printed By") + ":");
            headerCell.setCellStyle(ReportCriteriaStyle);

            region = new Region(4, (short) 8, 4, (short) 8);
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(4);
            headerCell = headerRow.createCell((short) 8);
            headerCell.setCellValue(strUsrId);
            headerCell.setCellStyle(ReportCriteriaStyle);

            region = new Region(5, (short) 7, 5, (short) 7);
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(5);
            headerCell = headerRow.createCell((short) 7);
            headerCell.setCellValue(getTranslatedCaptionMsg("Printed Date") + ":");
            headerCell.setCellStyle(ReportCriteriaStyle);

            region = new Region(5, (short) 8, 5, (short) 8);
            sheet.addMergedRegion(region);
            headerRow = sheet.createRow(5);
            headerCell = headerRow.createCell((short) 8);
            headerCell.setCellValue(printdate);
            headerCell.setCellStyle(ReportCriteriaStyle);
        }
    }

    private void createSheetDetail(String name) {
        int j = 0;

        //create a new worksheet
        sheet = workBook.createSheet(name);

        sheet.setColumnWidth((short) j++, (short) 5000);
        sheet.setColumnWidth((short) j++, (short) 5000);
        sheet.setColumnWidth((short) j++, (short) 5000);
        sheet.setColumnWidth((short) j++, (short) 5000);
        sheet.setColumnWidth((short) j++, (short) 5000);
        sheet.setColumnWidth((short) j++, (short) 5000);
        sheet.setColumnWidth((short) j++, (short) 5000);
        sheet.setColumnWidth((short) j++, (short) 5000);
        sheet.setColumnWidth((short) j++, (short) 5000);
    }

    private void createSheetSummary(String name) {
        int j = 0;

        //create a new worksheet
        sheet = workBook.createSheet(name);

        sheet.setColumnWidth((short) j++, (short) 3500);
        sheet.setColumnWidth((short) j++, (short) 3500);
        sheet.setColumnWidth((short) j++, (short) 3500);
        sheet.setColumnWidth((short) j++, (short) 3500);
        sheet.setColumnWidth((short) j++, (short) 3500);
    }

    private void setHeaderStyle() throws Exception {
        headerstyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        headerstyle.setFont(font);
        headerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //  Centered around
        headerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        headerstyle.setWrapText(true);
        headerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
        headerstyle.setBorderLeft((short) 1);
        headerstyle.setRightBorderColor(HSSFColor.BLACK.index);
        headerstyle.setBorderRight((short) 1);
        headerstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        headerstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.
        headerstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        headerstyle.setTopBorderColor(HSSFColor.BLACK.index);
        headerstyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); //  Sets the background color of a cell.
        headerstyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        headerstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }

    private void setForCellStyle() throws Exception {
        cellstyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        cellstyle.setFont(font);
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //  Centered around
        cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        cellstyle.setWrapText(true);
        cellstyle.setLeftBorderColor(HSSFColor.BLACK.index);
        cellstyle.setBorderLeft((short) 1);
        cellstyle.setRightBorderColor(HSSFColor.BLACK.index);
        cellstyle.setBorderRight((short) 1);
        cellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        cellstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.
        cellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        cellstyle.setTopBorderColor(HSSFColor.BLACK.index);
        cellstyle.setFillForegroundColor(HSSFColor.WHITE.index); //  Sets the background color of a cell.
    }

    private void setForDesCellStyle() throws Exception {
        descellstyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        descellstyle.setFont(font);
        descellstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); //  Centered around
        descellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        descellstyle.setWrapText(true);
        descellstyle.setLeftBorderColor(HSSFColor.BLACK.index);
        descellstyle.setBorderLeft((short) 1);
        descellstyle.setRightBorderColor(HSSFColor.BLACK.index);
        descellstyle.setBorderRight((short) 1);
        descellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        descellstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.
        descellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        descellstyle.setTopBorderColor(HSSFColor.BLACK.index);
        descellstyle.setFillForegroundColor(HSSFColor.WHITE.index); //  Sets the background color of a cell.
    }

    private void setForCellNumberStyle() throws Exception {

        cellNumberstyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        cellNumberstyle.setFont(font);
        cellNumberstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //  Centered around
        cellNumberstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        cellNumberstyle.setWrapText(true);
        cellNumberstyle.setLeftBorderColor(HSSFColor.BLACK.index);
        cellNumberstyle.setBorderLeft((short) 1);
        cellNumberstyle.setRightBorderColor(HSSFColor.BLACK.index);
        cellNumberstyle.setBorderRight((short) 1);
        cellNumberstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        cellNumberstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.
        cellNumberstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        cellNumberstyle.setTopBorderColor(HSSFColor.BLACK.index);
        cellNumberstyle.setFillForegroundColor(HSSFColor.WHITE.index); //  Sets the background color of a cell.
        cellNumberstyle.setDataFormat(workBook.createDataFormat().getFormat("#0"));

    }


    private void setForCellStyleTotal() throws Exception {
        cellstyleTotal = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        cellstyleTotal.setFont(font);
        cellstyleTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //  Centered around
        cellstyleTotal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        cellstyleTotal.setWrapText(true);
        cellstyleTotal.setLeftBorderColor(HSSFColor.BLACK.index);
        cellstyleTotal.setBorderLeft((short) 1);
        cellstyleTotal.setRightBorderColor(HSSFColor.BLACK.index);
        cellstyleTotal.setBorderRight((short) 1);
        cellstyleTotal.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        cellstyleTotal.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.
        cellstyleTotal.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        cellstyleTotal.setTopBorderColor(HSSFColor.BLACK.index);
        cellstyleTotal.setFillForegroundColor(HSSFColor.WHITE.index); //  Sets the background color of a cell.
    }

    private void setForCellNumberStyleTotal() throws Exception {
        cellNumberstyleTotal = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        cellNumberstyleTotal.setFont(font);
        cellNumberstyleTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //  Centered around
        cellNumberstyleTotal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        cellNumberstyleTotal.setWrapText(true);
        cellNumberstyleTotal.setLeftBorderColor(HSSFColor.BLACK.index);
        cellNumberstyleTotal.setBorderLeft((short) 1);
        cellNumberstyleTotal.setRightBorderColor(HSSFColor.BLACK.index);
        cellNumberstyleTotal.setBorderRight((short) 1);
        cellNumberstyleTotal.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        cellNumberstyleTotal.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.
        cellNumberstyleTotal.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        cellNumberstyleTotal.setTopBorderColor(HSSFColor.BLACK.index);
        cellNumberstyleTotal.setFillForegroundColor(HSSFColor.WHITE.index); //  Sets the background color of a cell.
    }

    private void setFirstHeaderStyle() throws Exception {
        CompanyTitleStyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 18);

        CompanyTitleStyle.setFont(font);
        CompanyTitleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        CompanyTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        CompanyTitleStyle.setWrapText(false);
    }

    private void setFirstHeaderStyle2() throws Exception {
        ReportTitleStyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 14);

        ReportTitleStyle.setFont(font);
        ReportTitleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        ReportTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        ReportTitleStyle.setWrapText(false);
    }

    private void setSubHeaderStyle2() throws Exception {
        subheaderstyle2 = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);

        subheaderstyle2.setFont(font);
        subheaderstyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT); //  Centered around
        subheaderstyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        subheaderstyle2.setWrapText(false);

        subheaderstyle2.setLeftBorderColor(HSSFColor.BLACK.index);
        subheaderstyle2.setBorderLeft((short) 1);
        subheaderstyle2.setRightBorderColor(HSSFColor.BLACK.index);
        subheaderstyle2.setBorderRight((short) 1);
        subheaderstyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        subheaderstyle2.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.
        subheaderstyle2.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        subheaderstyle2.setTopBorderColor(HSSFColor.BLACK.index);
        subheaderstyle2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); //  Sets the background color of a cell.
        subheaderstyle2.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        subheaderstyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }

    private void setSubHeaderStyle() throws Exception {
        subheaderstyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);

        subheaderstyle.setFont(font);
        subheaderstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //  Centered around
        subheaderstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        subheaderstyle.setWrapText(false);

        subheaderstyle.setLeftBorderColor(HSSFColor.BLACK.index);
        subheaderstyle.setBorderLeft((short) 1);
        subheaderstyle.setRightBorderColor(HSSFColor.BLACK.index);
        subheaderstyle.setBorderRight((short) 1);
        subheaderstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        subheaderstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.
        subheaderstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold
        subheaderstyle.setTopBorderColor(HSSFColor.BLACK.index);
        subheaderstyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); //  Sets the background color of a cell.
        subheaderstyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        subheaderstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }

    private void setCriteria() throws Exception {
        ReportCriteriaStyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        ReportCriteriaStyle.setFont(font);
        ReportCriteriaStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        ReportCriteriaStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        ReportCriteriaStyle.setWrapText(false);
    }

    private void setNoDataStyle() throws Exception {
        nodatastyle = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 18);

        nodatastyle.setFont(font);
        nodatastyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        nodatastyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //  Centered above and below the
        nodatastyle.setWrapText(false);
    }

    private void setRegionMethod() throws Exception

    {
        setRegion(borderTHIN, sheet, region, workBook);
    }

    private String getReportTypeDesc(String strCode) {
        String strDesc = "";
        if (strCode.equals("D")) {
            strDesc = "Details";
        } else if (strCode.equals("S")) {
            strDesc = "Summary";
        }
        return strDesc;
    }

    private String getDenomDesc(String strCode) throws Exception {
        String strDesc = "";
        GvdenomstSQLAMY gvdenomstSQL = new GvdenomstSQLAMY(conn);
        gvdenomstSQL.setCOY(strCoy);
        gvdenomstSQL.setCOY_SUB(strCoySub);
        gvdenomstSQL.setGV_TYPE(strVouchType);
        gvdenomstSQL.setGV_DENOMINATION(strCode);
        gvdenomstSQL.getByKey();
        strDesc = gvdenomstSQL.GV_DENO_DESC();

        return strDesc;
    }

    private String getVouchTypeDesc(String strCode) throws Exception {
        String strDesc = "";
        GvtypemstSQLAMY gvtypemstSQL = new GvtypemstSQLAMY(conn);
        gvtypemstSQL.setCOY(strCoy);
        gvtypemstSQL.setCOY_SUB(strCoySub);
        gvtypemstSQL.setGV_TYPE(strCode);
        gvtypemstSQL.getByKey();
        strDesc = gvtypemstSQL.GV_TYPE_DESC();

        return strDesc;
    }

    private String getSheetTitle(String strCode) {
        String strDesc = "";
        if (strCode.equals("D")) {
            strDesc = "RedeemDetail_";
        } else if (strCode.equals("S")) {
            strDesc = "RedeemSummary_";
        }
        return strDesc;
    }

    private String getDateTitle(String strCode) {
        String strDesc = "";
        if (strCode.equals("C")) {
            strDesc = "Date Created";
        } else if (strCode.equals("A")) {
            strDesc = "Date Activated";
        }
        return strDesc;
    }

    private String getByTitle(String strCode) {
        String strDesc = "";
        if (strCode.equals("C")) {
            strDesc = "Created By";
        } else if (strCode.equals("A")) {
            strDesc = "Activated By";
        }
        return strDesc;
    }

    private String getRetrieveDate(String strCode) {
        String strDesc = "";
        if (strCode.equals("C")) {
            strDesc = "CREATE_DATE";
        } else if (strCode.equals("A")) {
            strDesc = "ACTIVATE_DATE";
        }
        return strDesc;
    }

    private String getRetrieveBy(String strCode) {
        String strDesc = "";
        if (strCode.equals("C")) {
            strDesc = "CREATE_BY";
        } else if (strCode.equals("A")) {
            strDesc = "ACTIVATE_BY";
        }
        return strDesc;
    }

    private String getStore() // Added by Shriyle 2016-04-13
        throws Exception {
        String query_1 = "";
        //System.out.println("TEST-print SQL:");

        query_1 =
            "SELECT ''''||listagg (KEY_1, ''',''') within GROUP (ORDER BY KEY_1) || '''' AS listcomma " +
            "FROM WORKMST " + "WHERE " + "SESSION_ID = '" + SESSION_ID + "' " + "AND TABLE_NAME = '" +
            strTABLENAMESTORE + "' " + "AND KEY_2 = '" + TIME_CD + "' " + "AND KEY_3='NULL' " + "AND KEY_4='NULL' " +
            "AND KEY_5='NULL' ";


        getStore_ps = conn.prepareStatement(query_1);
        getStore_rs = getStore_ps.executeQuery();

        while (getStore_rs != null && getStore_rs.next()) {
            gvStore = getStore_rs.getString(1);
        }

        return gvStore;

    }


    private void closeStatement() {
        try {
            if (pstmtDEL != null)
                pstmtDEL.close();
        } catch (Exception e) {
        }
        pstmtDEL = null;
    }

    public void setTestRun(boolean bln) {
        test_run = bln;
    }

}
