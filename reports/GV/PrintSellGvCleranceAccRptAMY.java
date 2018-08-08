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

public class PrintSellGvCleranceAccRptAMY extends GenericExcel 
{
   private static final Log log = 
      LogFactory.getLog(PrintSellGvCleranceAccRptAMY.class);
    
   private static final String PROGRAM_VERSION = "2013-05-26 00:00 SOOHAU";  
   
   final short borderTHIN = HSSFCellStyle.BORDER_THIN;
   
   private int currentRow = 0; 
   // variables to hold the user input values
   
   private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
   
   private String SYSCompanyName = "";
   private String strUsrId           = null;
   private String strCoy = null;
   private String strCoySub = null;
   private String strReportType = null;
   private String strStore = null;
   private String strGlCode = null;
   private String strFrDateIssue = null;
   private String strToDateIssue = null;
   
   protected CurrencyConverter currencyConverter = null;
   protected int lineCodeCount = 0;
   protected HParam hParam_denom = new HParam();
   
   private int maxPerSheet = 0;
   private int currentDataRow = 1;
   private boolean test_run = false;
   
   private PreparedStatement prepareStmt = null;
   private ResultSet rs = null;
   private ProfitvvSQL profitvvSQL = null;
  
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
//   private Format formatter = new SimpleDateFormat("hh:mm:ss a");
   private Format formatter1 = new SimpleDateFormat("kk:mm");
   private String printdate = reportGenTime.format(date).toUpperCase(); 
   private String printtime = formatter1.format(date);
  
   public PrintSellGvCleranceAccRptAMY() {}
   
   public PrintSellGvCleranceAccRptAMY(String file_name)
   {
      super(file_name);
   }
   
   public PrintSellGvCleranceAccRptAMY(OutputStream out_stream)
   {
      super(out_stream);
   }   
  
  
   private void initObjSQL() throws SQLException
   {
      profitvvSQL = new ProfitvvSQL(conn);
   }   
   
   private void jInit(HParam hParam) throws Exception
   {
      strUsrId = hParam.getString("USER_ID");
      strCoy = hParam.getString("COY");
      strCoySub = hParam.getString("COY_SUB");
      strReportType = hParam.getString("RPT_TYPE");
      strStore = hParam.getString("STORE");
      strGlCode = hParam.getString("GL_CODE");
      strFrDateIssue = hParam.getString("FR_DATE_ISSUED");
      strToDateIssue = hParam.getString("TO_DATE_ISSUED");
      
      System.out.println("## REPORT TYPE = " + strReportType + " ##");
      
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
   }   
   
   public String getProfitVV(String vnm, String coy) throws SQLException
   {
      profitvvSQL.setVNM(vnm);
      profitvvSQL.setCOY(coy);
      profitvvSQL.getByKey(); 
     
     return (profitvvSQL.VNM_VDTVL());
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
          if (workBook != null) {
             workBook = null;
          }
          
         try {
            if (rs != null) {
               rs.close();
            }
         } catch (Exception Ex) {}
         rs = null;
         
         try {
            if (prepareStmt != null) {
               prepareStmt.close();
            }
         } catch (Exception Ex) {}
         prepareStmt = null;
         
         closeConnection();
         super.closeOutputStream();
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
      catch (Exception e)
		{
		   e.printStackTrace();
         throw (e);
		}
   }   
   
   private void process_sheet() throws Exception 
   {
      String query = getQuery();
    
      System.out.println("query = "+query);
      prepareStmt = conn.prepareStatement(query); 
      rs= prepareStmt.executeQuery();    
      
      HSSFRow headerRow = null;
      boolean create_hdr = true;
      int sheetCreated = 1;
      
      if (!rs.isBeforeFirst()) //check is it no data
      {
          createSheetDaily(getSheetTitle(strReportType)+sheetCreated);
          
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
//            setCurrValue(rs);
            
            if (currentRow==0 && create_hdr)
            {
              createSheetDaily(getSheetTitle(strReportType)+sheetCreated);
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
    String query = "";
    String strSYSVchrDisGL = getProfitVV("SYSVchrDisGL", strCoy);
    String SYSDwnVchrToPos = getProfitVV("SYSDwnVchrToPos", strCoy);
    
    if(strReportType.equals("M"))
    {
      query =
            "\nSELECT " +
            "\nGL_STORE, GL_TRANS_DATE AS MONTH_ISSUED, GL_CD, SUM(DR) AS DR, SUM(CR) AS CR, ( " +
            "\nSUM(DR)- SUM(CR) ) AS DIFF  " +
            "\nFROM ( " +
            "\nSELECT " +
            "\nGL_STORE, TO_CHAR(GL_TRANS_DATE, 'YYYY-MM') AS GL_TRANS_DATE, GL_DEBIT_ACCT AS GL_CD, " +
            "\nSUM(DEBIT_AMT) AS DR, 0 AS CR " +
            "\nFROM " + 
            "\nGLINTF " +
            "\nWHERE " +
            "\nGL_TRANS_GRP = 'K' ";
            
        if(strStore.equals(""))
          query += "\nAND GL_STORE IN (SELECT STORE FROM ADOPRSTR WHERE USR_ID = '"+strUsrId+"') " ;
        else
          query += "\nAND GL_STORE IN ("+strStore+") " ;
        
        query +=
            "\nAND GL_TRANS_DATE BETWEEN TO_DATE('"+strFrDateIssue+"','YYYY-MM-DD') AND TO_DATE('"+strToDateIssue+"','YYYY-MM-DD') " + 
            "\nAND GL_DEBIT_ACCT = '"+strGlCode+"' " +
            "\nGROUP BY GL_STORE, TO_CHAR(GL_TRANS_DATE, 'YYYY-MM'), GL_DEBIT_ACCT " +
            "\nUNION ALL " +
            "\nSELECT GL_STORE, TO_CHAR(GL_TRANS_DATE, 'YYYY-MM') AS GL_TRANS_DATE, GL_CREDIT_ACCT AS GL_CD, 0 AS DR, SUM(CREDIT_AMT) AS CR " +
            "\nFROM " + 
            "\nGLINTF " + 
            "\nWHERE " +
            "\nGL_TRANS_GRP IN " + (strSYSVchrDisGL.equals("N") ? "('S','C') " : "('C') ");
            
        if(strStore.equals(""))
          query += "\nAND GL_STORE IN (SELECT STORE FROM ADOPRSTR WHERE USR_ID = '"+strUsrId+"') " ;
        else
          query += "\nAND GL_STORE IN ("+strStore+") " ;          
  
        query +=
            "\nAND GL_TRANS_DATE BETWEEN TO_DATE('"+strFrDateIssue+"','YYYY-MM-DD') AND TO_DATE('"+strToDateIssue+"','YYYY-MM-DD') " + 
            "\nAND GL_CREDIT_ACCT = '"+strGlCode+"' " +
            "\nGROUP BY GL_STORE, TO_CHAR(GL_TRANS_DATE, 'YYYY-MM'), GL_CREDIT_ACCT " +
            "\n) " +
            "\nGROUP BY GL_STORE, GL_TRANS_DATE, GL_CD " +
            "\nORDER BY GL_STORE, GL_TRANS_DATE, GL_CD " ;    
    }
    else if(strReportType.equals("D"))
    {
      query =
            "\n SELECT " +
            "\n GL_STORE, TO_CHAR(GL_TRANS_DATE, 'YYYY-MM-DD') AS DATE_ISSUED, GL_CD, SUM(DR) AS DR, " +
            "\n SUM(CR) AS CR, ( SUM(DR)- SUM(CR) ) AS DIFF " +
            "\n FROM ( " +
            "\n SELECT " +
            "\n GL_STORE, GL_TRANS_DATE, GL_DEBIT_ACCT AS GL_CD, SUM(DEBIT_AMT) AS DR, 0 AS CR " +
            "\n FROM " + 
            "\n GLINTF " +
            "\n WHERE " +
            "\n GL_TRANS_GRP = 'K' ";
            
        if(strStore.equals(""))
          query += "\n AND GL_STORE IN (SELECT STORE FROM ADOPRSTR WHERE USR_ID = '"+strUsrId+"') " ;
        else
          query += "\n AND GL_STORE IN ("+strStore+") " ;
        
        query +=
            "\n AND GL_TRANS_DATE BETWEEN TO_DATE('"+strFrDateIssue+"','YYYY-MM-DD') AND TO_DATE('"+strToDateIssue+"','YYYY-MM-DD') " + 
            "\n AND GL_DEBIT_ACCT = '"+strGlCode+"' " +
            "\n GROUP BY GL_STORE, GL_TRANS_DATE, GL_DEBIT_ACCT " +
            "\n UNION ALL " +
            "\n SELECT GL_STORE, GL_TRANS_DATE, GL_CREDIT_ACCT AS GL_CD, 0 AS DR, SUM(CREDIT_AMT) AS CR " +
            "\n FROM " + 
            "\n GLINTF " +
            "\n WHERE " +
            "\n GL_TRANS_GRP IN " + (SYSDwnVchrToPos.equals("Y") ? "('S','C') " : "('C') ");
            
        if(strStore.equals(""))
          query += "\n AND GL_STORE IN (SELECT STORE FROM ADOPRSTR WHERE USR_ID = '"+strUsrId+"') " ;
        else
          query += "\n AND GL_STORE IN ("+strStore+") " ;          
   
        query +=
            "\nAND GL_TRANS_DATE BETWEEN TO_DATE('"+strFrDateIssue+"','YYYY-MM-DD') AND TO_DATE('"+strToDateIssue+"','YYYY-MM-DD') " + 
            "\nAND GL_CREDIT_ACCT = '"+strGlCode+"' " +
            "\nGROUP BY GL_STORE, GL_TRANS_DATE, GL_CREDIT_ACCT " +
            "\n) " +
            "\nGROUP BY GL_STORE, GL_TRANS_DATE, GL_CD " +
            "\nORDER BY GL_STORE, GL_TRANS_DATE, GL_CD " ;           
    }
    
    return query ;
  }
  
  private void createSummDataSummary(HSSFRow headerRow, ResultSet rs) throws Exception
  {
      try  
      {   
        HSSFCell headerCell = null;
        int k = 0;

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("GL_STORE"));
        headerCell.setCellStyle(descellstyle); 

        headerCell = headerRow.createCell((short) k++);
        if(strReportType.equals("M"))
          headerCell.setCellValue(rs.getString("MONTH_ISSUED"));
        else if(strReportType.equals("D"))
          headerCell.setCellValue(rs.getString("DATE_ISSUED"));
        headerCell.setCellStyle(descellstyle); 
                
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("GL_CD"));
        headerCell.setCellStyle(descellstyle);
        
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("DR"));
        headerCell.setCellStyle(cellstyle);         
        
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("CR"));
        headerCell.setCellStyle(cellstyle);          
 
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("DIFF"));
        headerCell.setCellStyle(cellstyle);     
        
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
      
      int headerRowStart = 8; 
      int k = 0;

      headerRow = sheet.createRow((short) headerRowStart);
      
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
      headerCell.setCellValue(getTranslatedCaptionMsg("Date"));
      headerCell.setCellStyle(headerstyle); 
      
      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("GL Code"));
      headerCell.setCellStyle(headerstyle); 
      
      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Voucher System"));
      headerCell.setCellStyle(headerstyle);
      
      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("POS"));
      headerCell.setCellStyle(headerstyle);

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Differences")); 
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
      if(strReportType.equals("M"))
        headerCell.setCellValue(getTranslatedReportMsg("Monthly Selling Voucher Clearance Account Report"));
      else if(strReportType.equals("D"))
        headerCell.setCellValue(getTranslatedReportMsg("Daily Selling Voucher Clearance Account Report"));
      headerCell.setCellStyle(ReportTitleStyle);
      
      region = new Region(5, (short) 0, 5, (short)1);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 0);
      headerCell.setCellValue(getTranslatedCaptionMsg("GL Code") + ": " + strGlCode);
      headerCell.setCellStyle(ReportCriteriaStyle);   
      
      region = new Region(6, (short) 0, 6, (short)1);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 6);
      headerCell = headerRow.createCell((short) 0);
      headerCell.setCellValue(getTranslatedCaptionMsg("Store") + ": " + (strStore.equals("")? getTranslatedCaptionMsg("ALL") : strStore));
      headerCell.setCellStyle(ReportCriteriaStyle);      

      region = new Region(4, (short) 2, 4, (short)3);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 4);
      headerCell = headerRow.createCell((short) 2);
      headerCell.setCellValue(getTranslatedCaptionMsg("Issued Date From") + ": " + strFrDateIssue);
      headerCell.setCellStyle(ReportCriteriaStyle);   
      
      region = new Region(5, (short) 2, 5, (short)3); 
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 2);
      headerCell.setCellValue(getTranslatedCaptionMsg("Issued Date To") + ": " + strToDateIssue);
      headerCell.setCellStyle(ReportCriteriaStyle);   
 
      region = new Region(4, (short) 5, 4, (short)5);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 4);
      headerCell = headerRow.createCell((short) 5);
      headerCell.setCellValue(getTranslatedCaptionMsg("Printed By") + ": ");
      headerCell.setCellStyle(ReportCriteriaStyle);
    
      region = new Region(4, (short) 6, 4, (short)6);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 4);
      headerCell = headerRow.createCell((short) 6);
      headerCell.setCellValue(strUsrId);
      headerCell.setCellStyle(ReportCriteriaStyle);
    
      region = new Region(5, (short) 5, 5, (short)5);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 5);
      headerCell.setCellValue(getTranslatedCaptionMsg("Printed Date") + ": ");
      headerCell.setCellStyle(ReportCriteriaStyle);

      region = new Region(5, (short) 6, 5, (short)6);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 6);
      headerCell.setCellValue(printdate);
      headerCell.setCellStyle(ReportCriteriaStyle);

      region = new Region(5, (short) 7, 5, (short)7);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 7);
      headerCell.setCellValue(printtime);
      headerCell.setCellStyle(ReportCriteriaStyle);

   }          
   
   private void createSheetDetail(String name) 
   {
      int j = 0;

      //create a new worksheet
      sheet = workBook.createSheet(name);
      
      sheet.setColumnWidth((short)j++,(short)5000);
      sheet.setColumnWidth((short)j++,(short)5000);
      sheet.setColumnWidth((short)j++,(short)8000);
      sheet.setColumnWidth((short)j++,(short)8000);
      sheet.setColumnWidth((short)j++,(short)8000);
      sheet.setColumnWidth((short)j++,(short)8000);
      sheet.setColumnWidth((short)j++,(short)8000);
      sheet.setColumnWidth((short)j++,(short)8000);
      sheet.setColumnWidth((short)j++,(short)8000);
   }     
   
   private void createSheetDaily(String name) 
   {
      int j = 0; 

      //create a new worksheet
      sheet = workBook.createSheet(name);
      
      sheet.setColumnWidth((short)j++,(short)3500);
      sheet.setColumnWidth((short)j++,(short)5000);
      sheet.setColumnWidth((short)j++,(short)5000);
      sheet.setColumnWidth((short)j++,(short)6000); 
      sheet.setColumnWidth((short)j++,(short)3500);
      sheet.setColumnWidth((short)j++,(short)5000);
      sheet.setColumnWidth((short)j++,(short)3500);
      sheet.setColumnWidth((short)j++,(short)3500); 
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
   
   private String getSheetTitle(String strCode)
   {
      String strDesc = "";
      if(strCode.equals("D")){strDesc = "DailySelling_";}
      else if(strCode.equals("M")){strDesc = "MonthlySelling_";} 
      return strDesc;
   }

   public void setTestRun(boolean bln)
   {
      test_run = bln;
   }   
   
//    public static void main(String[] args)
//    {
//       try {     
//         
//           HParam hParam = new HParam();
//           hParam.put("COY","AMY");
//           hParam.put("COY_SUB","AMY");
//           hParam.put("strUsrId","TEH123");
//           hParam.put("VOUCHER_TYPE","10");
//           hParam.put("DENOMINATION","01");             
//           hParam.put("FROM_VOUCHER_NUMBER",""); 
//           hParam.put("TO_VOUCHER_NUMBER",""); 
//           hParam.put("FROM_CREATE_DATE",""); 
//           hParam.put("TO_CREATE_DATE",""); 
//           hParam.put("STATUS","N"); 
//           
//
//    
//           PrintSellGvCleranceAccRptAMY printSellGvCleranceAccRptAMY = new PrintSellGvCleranceAccRptAMY("/tmp/VoucherDownloadExcel"+System.currentTimeMillis()+".xls");
//           voucherDownloadExcelReport.setTestRun(false);                  
//           voucherDownloadExcelReport.print(hParam); 
//       }
//       catch (Exception de) {  
//           de.printStackTrace();
//       }
//        System.exit(0);
//    }    
   
}