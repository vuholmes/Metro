package qrcom.PROFIT.reports.GV;

import java.io.*;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.DecimalFormat;

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

public class PrintGvCancelRpt extends GenericExcel {
    private static final Log log = 
       LogFactory.getLog(PrintGvCancelRpt.class);
    
    final short borderTHIN = HSSFCellStyle.BORDER_THIN;
    
    private int currentRow = 0;
    
    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    
    private String SYSCompanyName = "";
    private String strUsrId           = null;
    private String strCoy = null;
    private String strCoySub = null;
    private String strStore = null;
    private String strGvType = null;
    private String strGvTypeDesc = null;
    private String strGvDenomination = null;
    private String strGvDenominationDesc = null;
    private String strFrDateCancel = null;
    private String strToDateCancel = null;
    
    protected CurrencyConverter currencyConverter = null;
    protected int lineCodeCount = 0;
    protected HParam hParam_denom = new HParam();
    
    private int maxPerSheet = 0;
    private int currentDataRow = 1;
    private String lang = null;
    
    private PreparedStatement prepareStmt = null;
    private ResultSet rs = null;
    private ProfitvvSQL profitvvSQL = null;
    private AduserSQL aduserSQL = null;
    private GvtypemstSQLAMY gvtypemstSQLAMY;
    private GvdenomstSQLAMY gvdenomstSQLAMY;
    
    private HSSFSheet sheet                      = null;
    private HSSFWorkbook workBook                = null;
    private HSSFCellStyle subheaderstyle        = null;
    private HSSFCellStyle subheaderstyle2        = null;
    private HSSFCellStyle headerstyle            = null;
    private HSSFCellStyle cellstyle              = null;
    private HSSFCellStyle cellNumberstyle        = null;
    private HSSFCellStyle cellstyleTotal         = null;
    private HSSFCellStyle cellNumberstyleTotal   = null;
    private HSSFCellStyle CompanyTitleStyle      = null;
    private HSSFCellStyle ReportTitleStyle       = null;
    private HSSFCellStyle ReportCriteriaStyle    = null;
    private HSSFCellStyle descellstyle           = null;
    private HSSFCellStyle nodatastyle            = null;
    private Region region                        = null;
    
    private Date date = new Date();
    
    private SimpleDateFormat reportGenTime = new SimpleDateFormat("yyyy-MM-dd");
    private Format formatter1 = new SimpleDateFormat("kk:mm");
    private String printdate = reportGenTime.format(date).toUpperCase(); 
    private String printtime = formatter1.format(date);
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    public PrintGvCancelRpt() {}
    
    public PrintGvCancelRpt(String file_name)
    {
       super(file_name);
    }
    
    public PrintGvCancelRpt(OutputStream out_stream)
    {
       super(out_stream);
    }   
    
    
    private void initObjSQL() throws SQLException
    {
       profitvvSQL      = new ProfitvvSQL(conn);
       aduserSQL        = new AduserSQL(conn);
       gvtypemstSQLAMY  = new GvtypemstSQLAMY(conn);
       gvdenomstSQLAMY  = new GvdenomstSQLAMY(conn);
    }   
    
    private void jInit(HParam hParam) throws Exception
    {
       strUsrId = hParam.getString("USER_ID");
       strCoy = hParam.getString("COY");
       strCoySub = hParam.getString("COY_SUB");
       strGvType = hParam.getString("GV_TYPE");
       strStore = hParam.getString("STORE");
       strGvDenomination = hParam.getString("GV_DENOMINATION");
       strFrDateCancel = hParam.getString("FR_DATE_CANCELLED");
       strToDateCancel = hParam.getString("TO_DATE_CANCELLED");
       
       SYSCompanyName              = getProfitVV("SYSCompanyName", strCoy);
       String strSYSUserLanguage   = getProfitVV("SYSUserLanguage", strCoy);
       String strSYSUserCountry    = getProfitVV("SYSUserCountry", strCoy); 
       String strSYSRoundPlace     = getProfitVV("SYSRoundPlace", strCoy);
       String strSYSRoundMode      = getProfitVV("SYSRoundMode", strCoy);
       String strSYSDecimalDisplay = getProfitVV("SYSDecimalDisplay", strCoy);

       maxPerSheet                 = Integer.parseInt(getProfitVV("SYSMaxLinesPerSheet", strCoy));
        
       currencyConverter = new CurrencyConverter(strSYSUserLanguage, strSYSUserCountry, Integer.parseInt(strSYSRoundPlace), Integer.parseInt(strSYSRoundMode), Integer.parseInt(strSYSDecimalDisplay));
       currencyConverter.setGrouping(false);
       
       // create a new workbook
       workBook = new HSSFWorkbook();
       setAduser(strUsrId);
       lang = aduserSQL.USR_LANGUAGE();
       System.out.println("## USR_LANGUAGE CODE: " + lang + " ##");
       
       if (lang == null || lang.trim().length() == 0) {
           lang = "0";
       }
       
       if(strGvType != null && strGvType.length() > 0) {
           strGvTypeDesc = getGvTypeDesc(strCoy, strCoySub, strGvType);
       }
       if(strGvDenomination != null && strGvDenomination.length() > 0) {
           strGvDenominationDesc = getGvDenoDesc(strCoy, strCoySub, strGvType, strGvDenomination);
       }
    }
    
    private String getGvTypeDesc(String coy, String coy_sub, String voucher_type) throws Exception {
        gvtypemstSQLAMY.setCOY(coy);
        gvtypemstSQLAMY.setCOY_SUB(coy_sub);
        gvtypemstSQLAMY.setGV_TYPE(voucher_type);

        if (gvtypemstSQLAMY.getByKey() == 0)
            throw (new Exception("Voucher Type Does not Exist"));

        return (AltDescUtil.getDesc(USER_LANGUAGE, gvtypemstSQLAMY.GV_TYPE_DESC()));
    }

    private String getGvDenoDesc(String coy, String coy_sub, String voucher_type,
                                 String denomination) throws Exception {
        gvdenomstSQLAMY.setCOY(coy);
        gvdenomstSQLAMY.setCOY_SUB(coy_sub);
        gvdenomstSQLAMY.setGV_TYPE(voucher_type);
        gvdenomstSQLAMY.setGV_DENOMINATION(denomination);
        if (gvdenomstSQLAMY.getByKey() == 0)
            throw (new Exception("Voucher Deno Does not Exist"));

        return (AltDescUtil.getDesc(USER_LANGUAGE, gvdenomstSQLAMY.GV_DENO_DESC()));
    }
    
    public String getProfitVV(String vnm, String coy) throws SQLException
    {
       profitvvSQL.setVNM(vnm);
       profitvvSQL.setCOY(coy);
       profitvvSQL.getByKey(); 
      
      return (profitvvSQL.VNM_VDTVL());
    }
    
    private void setAduser(String _user_id) throws SQLException {
        aduserSQL.setUSR_ID(_user_id);
        if (aduserSQL.getByKey() == 0) {
            aduserSQL.setVObject(new AduserSQL().getVObject());
        }
    }
    
    public void print(HParam hParam)
    {
       try
       {
          openOutputStream();
          openConnection();
          initObjSQL();
          jInit(hParam);       
          System.out.println("start writing the report...");
          start();
          
          workBook.write(super.outStream);
          System.out.println("end of writing the report...");
       }
       catch (Exception e)
       {
          e.printStackTrace();
       }
       finally
       {
         closeResultSet(rs);
         closePreparedStatement(prepareStmt);
         closeConnection();
         super.closeOutputStream();
         
         if (workBook != null) {
             workBook = null;
         }
       }
    }     
    
    public void start() throws Exception
    {     
       try 
       {    
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
          
          process_sheet();

          System.out.println("finished print ..." + sdf.format(cal.getTime()));
          long end = System.currentTimeMillis();
          long duration = end - start;
          System.out.println("print time is: " + duration/1000 + " secs");
       } 
       catch (Exception e) {
           e.printStackTrace();
           throw (e);
       }
    }   
    
    private void process_sheet() throws Exception 
    {
       String query = getQuery();
     
    //      System.out.println("query = "+query);
       prepareStmt = conn.prepareStatement(query); 
       rs= prepareStmt.executeQuery();    
       
       HSSFRow headerRow = null;
       boolean create_hdr = true;
       int sheetCreated = 1;
       
       if (!rs.isBeforeFirst()) //check is it no data
       {
           createSheetDaily(getSheetTitle()+sheetCreated);
           
           createPrintingInfo();
           createFirstHdrSummary();
           sheetCreated++;
           
           HSSFCell headerCell = null;       
                      
           region = new Region(currentRow+1, (short)0, currentRow+1 , (short)5);
           sheet.addMergedRegion(region);
           headerRow = sheet.createRow((short) currentRow+1);
           headerCell = headerRow.createCell((short) 0);
           headerCell.setCellValue(getDescription("NO DATA TO PRINT"));
           headerCell.setCellStyle(nodatastyle);       
       }       
       
       while (  rs != null && rs.next())
       {    
             if (currentRow==0 && create_hdr)
             {
               createSheetDaily(getSheetTitle()+sheetCreated);
               createPrintingInfo();
               createFirstHdrSummary();//summary
               sheetCreated++;
             }
             
             currentRow++;  
             headerRow = sheet.createRow((short) currentRow);
             createSummDataSummary(headerRow,rs);                   
             
             if (currentDataRow > maxPerSheet) 
             {
                 currentDataRow = 1;
                 currentRow = 0;
             }
       }     
    }
    
    private String getQuery() throws Exception
    {
     String separator = "<:>";
     
     StringBuilder queryBuilder = new StringBuilder();
     queryBuilder.append("SELECT GV.GV_NO, (GT.GV_TYPE||' - '||GETDESC(" + lang + ", GP.GV_TYPE_DESC, '" +
                            separator + "')) AS GV_TYPE, ");
     queryBuilder.append("(GT.GV_DENOMINATION||' - '||GETDESC(" + lang + ", GD.GV_DENO_DESC, '" +
                            separator + "')) AS GV_DENOMINATION, ");
     queryBuilder.append("GT.STORE, GETDESC(" + lang + ", ST.STORE_NAME, '" + separator + "') AS STORE_NAME,");
     queryBuilder.append("GD.AMOUNT, GT.TRANS_DATE AS CANCELLATION_DATE, GT.REMARK ");
     queryBuilder.append("\n FROM GVTRANS GT JOIN GVMSTR GV " +
                             "ON (GT.COY = GV.COY " +
                             "AND GT.COY_SUB = GV.COY_SUB " +
                             "AND GT.GV_TYPE = GV.GV_TYPE " +
                             "AND GT.GV_DENOMINATION = GV.GV_DENOMINATION)");
     queryBuilder.append("\n JOIN GVTYPEMST GP " +
                             "ON (GT.COY = GP.COY AND GT.COY_SUB = GP.COY_SUB AND GT.GV_TYPE = GP.GV_TYPE)");
     queryBuilder.append("\n JOIN GVDENOMST GD " +
                             "ON (GT.COY = GD.COY " +
                             "AND GT.COY_SUB = GD.COY_SUB " +
                             "AND GT.GV_TYPE = GD.GV_TYPE " +
                             "AND GT.GV_DENOMINATION = GD.GV_DENOMINATION)");
     queryBuilder.append("\n JOIN STRMST ST " +
                             "ON (GT.STORE = ST.STORE) " +
                             "WHERE GV.GV_NO BETWEEN GT.FR_GV_NO AND GT.TO_GV_NO");
     queryBuilder.append("\n AND GT.COY = '" + strCoy + "' " +
                        "AND GT.COY_SUB = '" + strCoySub + "' " +
                        "AND GT.TRANS_TYPE = 'L' ");
     if(strStore == null || strStore.equals("")) {
        queryBuilder.append("\n AND ST.STORE IN (SELECT STORE FROM ADOPRSTR WHERE USR_ID = '"+strUsrId+"') ");
     } else {
        queryBuilder.append("\n AND ST.STORE IN ("+strStore+") ");
     }
     if (strGvType != null && !strGvType.equals("")) {
        queryBuilder.append("\n AND GT.GV_TYPE = '" + strGvType + "' ");
     }
     if (strGvDenomination != null && !strGvDenomination.equals("")) {
        queryBuilder.append("\n AND GD.GV_DENOMINATION = '" + strGvDenomination + "' ");
     }
     if(!strFrDateCancel.equals("") && !strToDateCancel.equals("")) {
         queryBuilder.append("\n AND GT.TRANS_DATE BETWEEN TO_DATE('"+strFrDateCancel+"','YYYY-MM-DD') AND TO_DATE('"+strToDateCancel+"','YYYY-MM-DD') ");
     }
     queryBuilder.append(" ORDER BY GT.TRANS_DATE, GV.GV_NO");
     
     return queryBuilder.toString() ;
    }
    
    private void createSummDataSummary(HSSFRow headerRow, ResultSet rs) throws Exception
    {
       try  
       {   
         HSSFCell headerCell = null;
         int k = 0;

         headerCell = headerRow.createCell((short) k++);
         headerCell.setCellValue(rs.getString("GV_NO"));
         headerCell.setCellStyle(descellstyle); 
                 
         headerCell = headerRow.createCell((short) k++);
         headerCell.setCellValue(rs.getString("GV_TYPE"));
         headerCell.setCellStyle(descellstyle);
         
         headerCell = headerRow.createCell((short) k++);
         headerCell.setCellValue(rs.getString("GV_DENOMINATION"));
         headerCell.setCellStyle(descellstyle);         
         
         headerCell = headerRow.createCell((short) k++);
         headerCell.setCellValue(rs.getString("STORE"));
         headerCell.setCellStyle(descellstyle);          
    
         headerCell = headerRow.createCell((short) k++);
         headerCell.setCellValue(rs.getString("STORE_NAME"));
         headerCell.setCellStyle(descellstyle);     

         headerCell = headerRow.createCell((short) k++);
         headerCell.setCellValue(decimalFormat.format(Double.parseDouble(rs.getString("AMOUNT"))));
         headerCell.setCellStyle(cellstyle);
         
         headerCell = headerRow.createCell((short) k++);
         headerCell.setCellValue(reportGenTime.format(rs.getDate("CANCELLATION_DATE")));
         headerCell.setCellStyle(descellstyle);         
         
         headerCell = headerRow.createCell((short) k++);
         headerCell.setCellValue(rs.getString("REMARK"));
         headerCell.setCellStyle(descellstyle);        
         
         currentDataRow++;
       }
       catch (Exception e)
       {
          throw (e);
       }         
    }
    
    private void createFirstHdrSummary() throws Exception
    {
       HSSFRow headerRow = null;
       HSSFCell headerCell = null;
       
       int headerRowStart = 10; 
       int k = 0;
       
       region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
       sheet.addMergedRegion(region);
       setRegionMethod();
       headerRow = sheet.createRow((short) headerRowStart);
       headerCell = headerRow.createCell((short) k);
       headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Number"));
       headerCell.setCellStyle(headerstyle);
       
       k++;
       region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
       sheet.addMergedRegion(region);
       setRegionMethod();
       headerRow = sheet.createRow((short) headerRowStart);
       headerCell = headerRow.createCell((short) k);
       headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Type"));
       headerCell.setCellStyle(headerstyle); 
       
       k++;
       region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
       sheet.addMergedRegion(region);
       setRegionMethod();
       headerRow = sheet.createRow((short) headerRowStart);
       headerCell = headerRow.createCell((short) k);
       headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Denomination"));
       headerCell.setCellStyle(headerstyle);
       
       k++;
       region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
       sheet.addMergedRegion(region);
       setRegionMethod();
       headerRow = sheet.createRow((short) headerRowStart);
       headerCell = headerRow.createCell((short) k);
       headerCell.setCellValue(getTranslatedCaptionMsg("Store"));
       headerCell.setCellStyle(headerstyle);

       k++;
       region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
       sheet.addMergedRegion(region);
       setRegionMethod();
       headerRow = sheet.createRow((short) headerRowStart);
       headerCell = headerRow.createCell((short) k);
       headerCell.setCellValue(getTranslatedCaptionMsg("Store Name")); 
       headerCell.setCellStyle(headerstyle);

       k++;
       region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
       sheet.addMergedRegion(region);
       setRegionMethod();
       headerRow = sheet.createRow((short) headerRowStart);
       headerCell = headerRow.createCell((short) k);
       headerCell.setCellValue(getTranslatedCaptionMsg("Amount (RM)")); 
       headerCell.setCellStyle(headerstyle);
       
       k++;
       region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
       sheet.addMergedRegion(region);
       setRegionMethod();
       headerRow = sheet.createRow((short) headerRowStart);
       headerCell = headerRow.createCell((short) k);
       headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Cancellation Date")); 
       headerCell.setCellStyle(headerstyle);      

       k++;
       region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
       sheet.addMergedRegion(region);
       setRegionMethod();
       headerRow = sheet.createRow((short) headerRowStart);
       headerCell = headerRow.createCell((short) k);
       headerCell.setCellValue(getTranslatedCaptionMsg("Remark")); 
       headerCell.setCellStyle(headerstyle); 
             
       currentRow = headerRowStart+1;
    }
    
    private void createPrintingInfo() throws Exception
    {
       HSSFRow headerRow = null;
       HSSFCell headerCell = null;   

       region = new Region(0, (short) 0, 0, (short)4);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 0);
       headerCell = headerRow.createCell((short) 0);
       headerCell.setCellValue(getTranslatedCaptionMsg(SYSCompanyName));
       headerCell.setCellStyle(CompanyTitleStyle);        
    
       region = new Region(2, (short) 0, 2, (short)4);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 2);
       headerCell = headerRow.createCell((short) 0);
       headerCell.setCellValue(getTranslatedReportMsg("Voucher Issuance Cancellation Report"));
       headerCell.setCellStyle(ReportTitleStyle);

       region = new Region(4, (short) 0, 4, (short)3);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 4);
       headerCell = headerRow.createCell((short) 0);
       headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Cancellation From Date") + ": " + strFrDateCancel);
       headerCell.setCellStyle(ReportCriteriaStyle);   
    
       region = new Region(4, (short) 4, 4, (short)7); 
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 4);
       headerCell = headerRow.createCell((short) 4);
       headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Cancellation To Date") + ": " + strToDateCancel);
       headerCell.setCellStyle(ReportCriteriaStyle);

       String strAll = getTranslatedCaptionMsg("ALL");

       region = new Region(6, (short) 0, 6, (short)2);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 6);
       headerCell = headerRow.createCell((short) 0);
       headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Type") + ": " + 
                               (strGvType.equals("")? strAll : (strGvType + " - " + strGvTypeDesc)));
       headerCell.setCellStyle(ReportCriteriaStyle);   
       
       region = new Region(7, (short) 0, 7, (short)2);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 7);
       headerCell = headerRow.createCell((short) 0);
       headerCell.setCellValue(getTranslatedCaptionMsg("Voucher Denomination") + ": " +
                                (strGvDenomination.equals("") ? strAll :
                                 (strGvDenomination + " - " + strGvDenominationDesc)));
       headerCell.setCellStyle(ReportCriteriaStyle);   
       
       region = new Region(8, (short) 0, 8, (short)2);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 8);
       headerCell = headerRow.createCell((short) 0);
       headerCell.setCellValue(getTranslatedCaptionMsg("Store") + ": " + (strStore.equals("")? strAll : strStore));
       headerCell.setCellStyle(ReportCriteriaStyle);   
    
       region = new Region(6, (short) 7, 6, (short)7);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 6);
       headerCell = headerRow.createCell((short) 7);
       headerCell.setCellValue(getTranslatedCaptionMsg("Printed By") + ": ");
       headerCell.setCellStyle(ReportCriteriaStyle);
     
       region = new Region(6, (short) 8, 6, (short)8);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 6);
       headerCell = headerRow.createCell((short) 8);
       headerCell.setCellValue(strUsrId);
       headerCell.setCellStyle(ReportCriteriaStyle);
     
       region = new Region(7, (short) 7, 7, (short)7);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 7);
       headerCell = headerRow.createCell((short) 7);
       headerCell.setCellValue(getTranslatedCaptionMsg("Printed Date") + ": ");
       headerCell.setCellStyle(ReportCriteriaStyle);

       region = new Region(7, (short) 8, 7, (short)8);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 7);
       headerCell = headerRow.createCell((short) 8);
       headerCell.setCellValue(printdate);
       headerCell.setCellStyle(ReportCriteriaStyle);

       region = new Region(7, (short) 9, 7, (short)9);
       sheet.addMergedRegion(region);
       headerRow = sheet.createRow((short) 7);
       headerCell = headerRow.createCell((short) 9);
       headerCell.setCellValue(printtime);
       headerCell.setCellStyle(ReportCriteriaStyle);
    }           
    
    private void createSheetDaily(String name) 
    {
       int j = 0; 

       //create a new worksheet
       sheet = workBook.createSheet(name);
       
       sheet.setColumnWidth((short)j++,(short)6500);
       sheet.setColumnWidth((short)j++,(short)4500);
       sheet.setColumnWidth((short)j++,(short)5500);
       sheet.setColumnWidth((short)j++,(short)2500); 
       sheet.setColumnWidth((short)j++,(short)4500);
       sheet.setColumnWidth((short)j++,(short)4500);
       sheet.setColumnWidth((short)j++,(short)5500);
       sheet.setColumnWidth((short)j++,(short)4500); 
       sheet.setColumnWidth((short)j++,(short)4500);
       sheet.setColumnWidth((short)j++,(short)4500);
       sheet.setColumnWidth((short)j++,(short)4500);
       sheet.setColumnWidth((short)j++,(short)4500);
       sheet.setColumnWidth((short)j++,(short)2500); 
       sheet.setColumnWidth((short)j++,(short)2500); 
       sheet.setColumnWidth((short)j++,(short)2500);
    }
     
    private void setHeaderStyle() throws Exception
    {
       headerstyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       
       headerstyle.setFont(font);
       headerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//  Centered around  
       headerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       headerstyle.setWrapText(true);
       headerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
       headerstyle.setBorderLeft((short) 1);
       headerstyle.setRightBorderColor(HSSFColor.BLACK.index);
       headerstyle.setBorderRight((short) 1);
       headerstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       headerstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.  
       headerstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       headerstyle.setTopBorderColor(HSSFColor.BLACK.index);
       headerstyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//  Sets the background color of a cell. 
       headerstyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
       headerstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }
    
    private void setForCellStyle() throws Exception
    {
       cellstyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
       
       cellstyle.setFont(font);
       cellstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//  Centered around  
       cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       cellstyle.setWrapText(true);
       cellstyle.setLeftBorderColor(HSSFColor.BLACK.index);
       cellstyle.setBorderLeft((short) 1);
       cellstyle.setRightBorderColor(HSSFColor.BLACK.index);
       cellstyle.setBorderRight((short) 1);
       cellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       cellstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.  
       cellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       cellstyle.setTopBorderColor(HSSFColor.BLACK.index);
       cellstyle.setFillForegroundColor(HSSFColor.WHITE.index);//  Sets the background color of a cell. 
    }
    
    private void setForDesCellStyle() throws Exception
    {
       descellstyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
       
       descellstyle.setFont(font);
       descellstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);//  Centered around  
       descellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       descellstyle.setWrapText(true);
       descellstyle.setLeftBorderColor(HSSFColor.BLACK.index);
       descellstyle.setBorderLeft((short) 1);
       descellstyle.setRightBorderColor(HSSFColor.BLACK.index);
       descellstyle.setBorderRight((short) 1);
       descellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       descellstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.  
       descellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       descellstyle.setTopBorderColor(HSSFColor.BLACK.index);
       descellstyle.setFillForegroundColor(HSSFColor.WHITE.index);//  Sets the background color of a cell. 
    }  
    private void setForCellNumberStyle() throws Exception
    {
    
       cellNumberstyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
       
       cellNumberstyle.setFont(font);
       cellNumberstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//  Centered around  
       cellNumberstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       cellNumberstyle.setWrapText(true);
       cellNumberstyle.setLeftBorderColor(HSSFColor.BLACK.index);
       cellNumberstyle.setBorderLeft((short) 1);
       cellNumberstyle.setRightBorderColor(HSSFColor.BLACK.index);
       cellNumberstyle.setBorderRight((short) 1);
       cellNumberstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       cellNumberstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.  
       cellNumberstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       cellNumberstyle.setTopBorderColor(HSSFColor.BLACK.index);
       cellNumberstyle.setFillForegroundColor(HSSFColor.WHITE.index);//  Sets the background color of a cell. 
       cellNumberstyle.setDataFormat(workBook.createDataFormat().getFormat("#0"));
       
    }
    
    
    private void setForCellStyleTotal() throws Exception
    {
       cellstyleTotal = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       
       cellstyleTotal.setFont(font);
       cellstyleTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//  Centered around  
       cellstyleTotal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       cellstyleTotal.setWrapText(true);
       cellstyleTotal.setLeftBorderColor(HSSFColor.BLACK.index);
       cellstyleTotal.setBorderLeft((short) 1);
       cellstyleTotal.setRightBorderColor(HSSFColor.BLACK.index);
       cellstyleTotal.setBorderRight((short) 1);
       cellstyleTotal.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       cellstyleTotal.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.  
       cellstyleTotal.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       cellstyleTotal.setTopBorderColor(HSSFColor.BLACK.index);
       cellstyleTotal.setFillForegroundColor(HSSFColor.WHITE.index);//  Sets the background color of a cell. 
    }
    
    private void setForCellNumberStyleTotal() throws Exception
    {
       cellNumberstyleTotal = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
       
       cellNumberstyleTotal.setFont(font);
       cellNumberstyleTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//  Centered around  
       cellNumberstyleTotal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       cellNumberstyleTotal.setWrapText(true);
       cellNumberstyleTotal.setLeftBorderColor(HSSFColor.BLACK.index);
       cellNumberstyleTotal.setBorderLeft((short) 1);
       cellNumberstyleTotal.setRightBorderColor(HSSFColor.BLACK.index);
       cellNumberstyleTotal.setBorderRight((short) 1);
       cellNumberstyleTotal.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       cellNumberstyleTotal.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.  
       cellNumberstyleTotal.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       cellNumberstyleTotal.setTopBorderColor(HSSFColor.BLACK.index);
       cellNumberstyleTotal.setFillForegroundColor(HSSFColor.WHITE.index);//  Sets the background color of a cell. 
    }   

    private void setFirstHeaderStyle() throws Exception
    {
       CompanyTitleStyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       font.setFontHeightInPoints((short) 18);
       
       CompanyTitleStyle.setFont(font);
       CompanyTitleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
       CompanyTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       CompanyTitleStyle.setWrapText(false);
    }
    
    private void setFirstHeaderStyle2() throws Exception
    {
       ReportTitleStyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       font.setFontHeightInPoints((short) 14);
       
       ReportTitleStyle.setFont(font);
       ReportTitleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
       ReportTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       ReportTitleStyle.setWrapText(false);
    }
    
    private void setSubHeaderStyle2() throws Exception
    {
       subheaderstyle2 = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       font.setFontHeightInPoints((short) 10);
       
       subheaderstyle2.setFont(font);
       subheaderstyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//  Centered around  
       subheaderstyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       subheaderstyle2.setWrapText(false);
       
       subheaderstyle2.setLeftBorderColor(HSSFColor.BLACK.index);
       subheaderstyle2.setBorderLeft((short) 1);
       subheaderstyle2.setRightBorderColor(HSSFColor.BLACK.index);
       subheaderstyle2.setBorderRight((short) 1);
       subheaderstyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       subheaderstyle2.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.  
       subheaderstyle2.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       subheaderstyle2.setTopBorderColor(HSSFColor.BLACK.index);
       subheaderstyle2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//  Sets the background color of a cell. 
       subheaderstyle2.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
       subheaderstyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }  
    
    private void setSubHeaderStyle() throws Exception
    {
       subheaderstyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       font.setFontHeightInPoints((short) 10);
       
       subheaderstyle.setFont(font);
       subheaderstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//  Centered around  
       subheaderstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       subheaderstyle.setWrapText(false);
       
       subheaderstyle.setLeftBorderColor(HSSFColor.BLACK.index);
       subheaderstyle.setBorderLeft((short) 1);
       subheaderstyle.setRightBorderColor(HSSFColor.BLACK.index);
       subheaderstyle.setBorderRight((short) 1);
       subheaderstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       subheaderstyle.setBottomBorderColor(HSSFColor.BLACK.index); //  Set the border color of the cell.  
       subheaderstyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //  Sets the cell's border in bold  
       subheaderstyle.setTopBorderColor(HSSFColor.BLACK.index);
       subheaderstyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//  Sets the background color of a cell. 
       subheaderstyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
       subheaderstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }    
    
    private void setCriteria() throws Exception
    {
       ReportCriteriaStyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       
       ReportCriteriaStyle.setFont(font);
       ReportCriteriaStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
       ReportCriteriaStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       ReportCriteriaStyle.setWrapText(false);
    }
    
    private void setNoDataStyle() throws Exception
    {
       nodatastyle = workBook.createCellStyle();
       HSSFFont font = workBook.createFont();
       font.setColor(HSSFFont.COLOR_NORMAL);
       font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       font.setFontHeightInPoints((short) 18);
       
       nodatastyle.setFont(font);
       nodatastyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       nodatastyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//  Centered above and below the  
       nodatastyle.setWrapText(false);
    }      

    private void setRegionMethod() throws Exception

    {
       setRegion(borderTHIN, sheet, region, workBook);
    }   
    
    private String getSheetTitle()
    {
       String strDesc = "GvCancellationRpt_";
       return strDesc;
    }
    
    private void closeResultSet(ResultSet resultSet) {
         try {
             if (resultSet != null)
                 resultSet.close();
         } catch (Exception e) {
         }
         resultSet = null;
     }

     private void closePreparedStatement(PreparedStatement ps) {
         try {
             if (ps != null)
                 ps.close();
         } catch (Exception e) {
         }
         ps = null;
     }
}
