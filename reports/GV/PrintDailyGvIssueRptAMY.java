package qrcom.PROFIT.reports.GV; 

import java.io.*;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
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
import qrcom.util.qrMisc;
import qrcom.util.qrMath; 

import qrcom.PROFIT.files.info.StrmstSQL;

public class PrintDailyGvIssueRptAMY extends GenericExcel 
{
   private static final Log log = 
      LogFactory.getLog(PrintDailyGvIssueRptAMY.class);
    
   private static final String PROGRAM_VERSION = "2016-05-27 00:00 SOOHAU";  
   
   final short borderTHIN = HSSFCellStyle.BORDER_THIN;
   
   private int currentRow = 0; 
   // variables to hold the user input values

   private String REPORT_HDR = "VOUCHER DOWNLOAD";
   
   private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
   
   private String strUsrId           = null;
   private String strCoy = null;
   private String strCoySub = null;
   private String strStore = null;
   private String strGvType = null;
   private String strGvCategory = null;
   private String strFrDateIssue = null;
   private String strToDateIssue = null;
   
   private String strComboStoreList = "";
   
   protected CurrencyConverter currencyConverter = null;
   protected int lineCodeCount = 0;
   protected HParam hParam_denom = new HParam();
   
   private int maxPerSheet = 0;
   private int currentDataRow = 1;
   private boolean test_run = false;
   private boolean first_column = true;
   
   private PreparedStatement pstmtDEL = null;
   private PreparedStatement prepareStmt = null;
   private ResultSet rs = null;
   private PreparedStatement getCountQuery_ps = null;
   private ResultSet getCountQuery_rs = null;
   private CoymstSQL coymstSQL;
   private CoysubmstSQL coysubmstSQL;

   private String SYSGVCatSell         = ""; 
   private String SYSGVCatPoint        = ""; 
   private String SYSGVCatRebate       = ""; 
   private String SYSGVCatPromo        = ""; 
   private String SYSGVCatSellPoint    = ""; 
  
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
   
   private HParam newParam = null;
   
   private Date date = new Date();
   
   private SimpleDateFormat reportGenTime = new SimpleDateFormat("dd-MMM-yyyy"); 
   private Format formatter = new SimpleDateFormat("hh:mm:ss a");
   private Format formatter1 = new SimpleDateFormat("kk:mm");
   private String printdate = reportGenTime.format(date).toUpperCase(); 
   private String printtime = formatter1.format(date);
  
   public PrintDailyGvIssueRptAMY() {}
   
   public PrintDailyGvIssueRptAMY(String file_name)
   {
      super(file_name);
   }
   
   public PrintDailyGvIssueRptAMY(OutputStream out_stream)
   {
      super(out_stream);
   }   
  
  
   private void initObjSQL() throws SQLException
   {
      coymstSQL      = new CoymstSQL(conn);     
      coysubmstSQL      = new CoysubmstSQL(conn);     
   }   
   
   private void jInit(HParam hParam) throws Exception
   {
      strUsrId = hParam.getString("USER_ID");
      strCoy = hParam.getString("COY");
      strCoySub = hParam.getString("COY_SUB");
      strGvType = hParam.getString("GV_TYPE");
      strStore = hParam.getString("STORE");
      strGvCategory = hParam.getString("GV_CATEGORY");
      strFrDateIssue = hParam.getString("FR_DATE_ISSUED");
      strToDateIssue = hParam.getString("TO_DATE_ISSUED");
      
      System.out.println("## CATEGORY = " + strGvCategory + " ##");
      
      String strStoreList[] = strStore.split(",");
      for(int i=0; i<strStoreList.length; i++)
      {
        if(i!=0)
        {
          strComboStoreList = strComboStoreList + ",";
        } 
        if(strStoreList[i].trim().length()>0)
        {
          strComboStoreList = strComboStoreList + "'" + strStoreList[i] + "'";
        }
      }
      
      String strSYSUserLanguage   = getProfitVV("SYSUserLanguage", strCoy);
      String strSYSUserCountry    = getProfitVV("SYSUserCountry", strCoy); 
      String strSYSRoundPlace     = getProfitVV("SYSRoundPlace", strCoy);
      String strSYSRoundMode      = getProfitVV("SYSRoundMode", strCoy);
      String strSYSDecimalDisplay = getProfitVV("SYSDecimalDisplay", strCoy);
      String strSYSCurrGroupRpt   = getProfitVV("SYSCurrGroupRpt", strCoy);

      SYSGVCatSell         = getProfitVV("SYSGVCatSell", strCoy); 
      SYSGVCatPoint        = getProfitVV("SYSGVCatPoint", strCoy);
      SYSGVCatRebate       = getProfitVV("SYSGVCatRebate", strCoy);
      SYSGVCatPromo        = getProfitVV("SYSGVCatPromo", strCoy);
      SYSGVCatSellPoint    = getProfitVV("SYSGVCatSellPoint", strCoy);
      
      currencyConverter = new CurrencyConverter(strSYSUserLanguage, strSYSUserCountry, Integer.parseInt(strSYSRoundPlace), Integer.parseInt(strSYSRoundMode), Integer.parseInt(strSYSDecimalDisplay));
      currencyConverter.setGrouping(false);
      
      // get the max line per sheet
      ProfitvvSQL profitvvSQL = new ProfitvvSQL(conn);
      profitvvSQL.setVNM("SYSMaxLinesPerSheet");
      profitvvSQL.setCOY(strCoy);
      profitvvSQL.getByKey(); 
      
      coymstSQL.setCOY(strCoy); 
      coymstSQL.getByKey();
      
      coysubmstSQL.setCOY(strCoy);
      coysubmstSQL.setCOY_SUB(strCoySub);
      coysubmstSQL.getByKey();
      
      maxPerSheet = Integer.parseInt(profitvvSQL.VNM_VDTVL());  
     
      // create a new workbook
      workBook = new HSSFWorkbook();
   }   
   
   public String getProfitVV(String vnm, String coy) throws SQLException
   {
	    ProfitvvSQL profitvvSQL = null;
	    profitvvSQL = new ProfitvvSQL(conn);
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
        closeStatement();     
         try {
            if (workBook != null) {
               workBook = null;
            }
         } catch (Exception e) {}
         
         try {
            if (rs != null)
            {
               rs.close();
               rs = null;
            }
         } catch (Exception Ex) {}
     
         
         try {
            if (prepareStmt != null)
            {
               prepareStmt.close();
               prepareStmt = null;
            }
         } catch (Exception Ex) {}
         
         
         try {
            if (getCountQuery_ps != null)
            {
               getCountQuery_ps.close();
               getCountQuery_ps = null;
            }
         } catch (Exception Ex) {}
         
         try {
            if (getCountQuery_rs != null)
            {
               getCountQuery_rs.close();
               getCountQuery_rs = null;
            }
         } catch (Exception Ex) {}

                  
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
      boolean first_time = true;
      boolean blnIsCreateDenom = false;
      int sheetCreated = 1;      
      int counter = 1;
      
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
    String query = "";
    
    query =
          "\n WITH ISSUE_DATA AS ( " +
          "\n SELECT " +
          "\n A.PRC_TYPE, A.STORE, A.PRC_DATE, A.GV_TYPE, A.RSN_CD, " +
          "\n A.TOT_GV_NO, A.TOT_AMOUNT AS TOT_AMOUNT, A.SERIAL_NUM, A.PRC_USR_ID AS PRC_USR_ID, " +
          "\n (SELECT  USR_FIRST_NAME  FROM ADUSER WHERE USR_ID = A.PRC_USR_ID) AS PRC_USR_NAME, A.RECEIVED_BY, " +
          "\n (SELECT USR_FIRST_NAME  FROM ADUSER WHERE USR_ID = A.RECEIVED_BY) AS RECEIVED_BY_NAME, B.PCH_NAME, " +
          "\n B.COLL_NAME, B.COLL_NRIC, B.PCH_CONTACT_NO, B.PAYMENTMODE " +
          "\n FROM GVLOGBOOK A " + 
          "\n INNER JOIN GVTRANS B ON " +
          "\n A.COY = B.COY " +
          "\n AND A.COY_SUB = B.COY_SUB " +
          "\n AND A.PRC_TYPE = B.TRANS_TYPE " +
          "\n AND A.TRANS_NO = B.TRANS_NO " +
          "\n WHERE A.PRC_TYPE IN ('L', 'I') " ;
          
      if(strStore.equals(""))
        query += "\n AND A.STORE IN (SELECT STORE FROM ADOPRSTR WHERE USR_ID = '"+strUsrId+"') " ;
      else
        query += "\n AND A.STORE IN ("+strStore+") " ;
       
      if(!strGvType.equals(""))
        query += "\n AND A.GV_TYPE = '"+strGvType+"' ";

      if(!strGvCategory.equals("") && strGvCategory.equals("Selling"))
        query += "\n AND A.RSN_CD IN ("+SYSGVCatSell+") ";
      else if(!strGvCategory.equals("") && strGvCategory.equals("Points"))
        query += "\n AND A.RSN_CD IN ("+SYSGVCatPoint+") ";
      else if(!strGvCategory.equals("") && strGvCategory.equals("Rebate"))
        query += "\n AND A.RSN_CD IN ("+SYSGVCatRebate+") ";
      else if(!strGvCategory.equals("") && strGvCategory.equals("Promotion"))
        query += "\n AND A.RSN_CD IN ("+SYSGVCatPromo+") ";
      else if(!strGvCategory.equals("") && strGvCategory.equals("Selling/Points"))
        query += "\n AND A.RSN_CD IN ("+SYSGVCatSellPoint+") "; 

      query +=
          "\n AND PRC_DATE BETWEEN TO_DATE('"+strFrDateIssue+"','YYYY-MM-DD') AND TO_DATE('"+strToDateIssue+"','YYYY-MM-DD') " + 
          "\n ) " +
          "\n SELECT " +
          "\n STORE, " +
          "\n (CASE WHEN PRC_TYPE = 'I' THEN TO_CHAR(PRC_DATE, 'YYYY-MM-DD') ELSE NULL END) AS ISSUEDDT, " +
          "\n (CASE WHEN PRC_TYPE = 'L' THEN TO_CHAR(PRC_DATE, 'YYYY-MM-DD') ELSE NULL END) AS CANCELDT, " + 
          "\n GV_TYPE, RSN_CD, SUM(TOT_GV_NO) AS TOT_GV, " +
          "\n SUM((CASE WHEN PRC_TYPE = 'L' THEN TOT_AMOUNT * -1 ELSE TOT_AMOUNT END) ) AS TOT_AMT, " +
          "\n SERIAL_NUM, PRC_USR_ID, PRC_USR_NAME, RECEIVED_BY, RECEIVED_BY_NAME, PCH_NAME, " + 
          "\n COLL_NAME, COLL_NRIC, PCH_CONTACT_NO, PAYMENTMODE " +
          "\n FROM " +
          "\n ISSUE_DATA " +
          "\n GROUP BY PRC_TYPE, STORE, PRC_DATE, GV_TYPE, RSN_CD, SERIAL_NUM, PRC_USR_ID, " +
          "\n PRC_USR_NAME, RECEIVED_BY, RECEIVED_BY_NAME, COLL_NAME,COLL_NRIC, PCH_CONTACT_NO, PAYMENTMODE, PCH_NAME " +
          "\n ORDER BY STORE, PRC_DATE, GV_TYPE, RSN_CD, SERIAL_NUM, PRC_USR_ID, PRC_USR_NAME, RECEIVED_BY, " +
          "\n RECEIVED_BY_NAME, PCH_NAME, COLL_NAME,COLL_NRIC, PCH_CONTACT_NO, PAYMENTMODE ";

    return query ;
  }
  
  private void createSummDataSummary(HSSFRow headerRow, ResultSet rs) throws Exception
  {
      try  
      {   
        HSSFCell headerCell = null;
        int k = 0;

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("STORE"));
        headerCell.setCellStyle(descellstyle); 
                
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("ISSUEDDT"));
        headerCell.setCellStyle(descellstyle);
        
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("CANCELDT"));
        headerCell.setCellStyle(cellstyle);         
        
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("GV_TYPE"));
        headerCell.setCellStyle(cellstyle);          
 
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("RSN_CD"));
        headerCell.setCellStyle(cellstyle);     

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("TOT_GV"));
        headerCell.setCellStyle(descellstyle);
        
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("TOT_AMT"));
        headerCell.setCellStyle(cellstyle);         
        
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("SERIAL_NUM"));
        headerCell.setCellStyle(cellstyle);          
 
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("PRC_USR_ID") + " - " + rs.getString("PRC_USR_NAME"));
        headerCell.setCellStyle(cellstyle);     

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("PCH_NAME"));
        headerCell.setCellStyle(cellstyle);          
 
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("COLL_NAME"));
        headerCell.setCellStyle(cellstyle);     

        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("COLL_NRIC"));
        headerCell.setCellStyle(descellstyle);
        
        headerCell = headerRow.createCell((short) k++);
        headerCell.setCellValue(rs.getString("PCH_CONTACT_NO"));
        headerCell.setCellStyle(cellstyle);         
         
        headerCell = headerRow.createCell((short) k++);
        if(rs.getString("PAYMENTMODE")!=null && rs.getString("PAYMENTMODE").equals("CS"))
          headerCell.setCellValue("CASH");
        else if(rs.getString("PAYMENTMODE")!=null && rs.getString("PAYMENTMODE").equals("CH"))
          headerCell.setCellValue("CHEQUE");
        else if(rs.getString("PAYMENTMODE")!=null && rs.getString("PAYMENTMODE").equals("CC"))
          headerCell.setCellValue("CREDIT CARD");
        else if(rs.getString("PAYMENTMODE")!=null && rs.getString("PAYMENTMODE").equals("TT"))
          headerCell.setCellValue("Telegraph Transfer"); 
        else
          headerCell.setCellValue(""); 
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
      headerCell.setCellValue(getTranslatedCaptionMsg("Issued Date"));
      headerCell.setCellStyle(headerstyle); 
      
      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Cancelled Date"));
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
      headerCell.setCellValue(getTranslatedCaptionMsg("Reason Code")); 
      headerCell.setCellStyle(headerstyle);

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Qty")); 
      headerCell.setCellStyle(headerstyle);
      
      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Amount")); 
      headerCell.setCellStyle(headerstyle);      

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Receipt Serial Number")); 
      headerCell.setCellStyle(headerstyle); 

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Issued/Cancalled By")); 
      headerCell.setCellStyle(headerstyle); 

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Customer Name")); 
      headerCell.setCellStyle(headerstyle); 

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Collector")); 
      headerCell.setCellStyle(headerstyle); 

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("NRIC")); 
      headerCell.setCellStyle(headerstyle); 

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Customer Contact No") + "."); 
      headerCell.setCellStyle(headerstyle); 

      k++;
      region = new Region(headerRowStart, (short) k, headerRowStart+1, (short)k);
      sheet.addMergedRegion(region);
      setRegionMethod();
      headerRow = sheet.createRow((short) headerRowStart);
      headerCell = headerRow.createCell((short) k);
      headerCell.setCellValue(getTranslatedCaptionMsg("Payment Mode")); 
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
//      headerCell.setCellValue("Company: "+ strCoySub + " "  + getDescription(coysubmstSQL.COY_SUB_NAME()));
      headerCell.setCellValue(getDescription(coysubmstSQL.COY_SUB_NAME()));
      headerCell.setCellStyle(CompanyTitleStyle);        
   
      region = new Region(2, (short) 0, 2, (short)4);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 2);
      headerCell = headerRow.createCell((short) 0);
      headerCell.setCellValue(getTranslatedReportMsg("Daily Voucher Issue Report"));
      headerCell.setCellStyle(ReportTitleStyle);

      String strVoucherType = getTranslatedCaptionMsg("Voucher Type") + ": " + (strGvType.equals("")?"ALL":strGvType);
      region = new Region(4, (short) 0, 4, (short)2);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 4);
      headerCell = headerRow.createCell((short) 0);
      headerCell.setCellValue(strVoucherType);
      headerCell.setCellStyle(ReportCriteriaStyle);   
      
      String strVoucherCategory =
            getTranslatedCaptionMsg("Voucher Category") + ": " + (strGvCategory.equals("") ? "ALL" : strGvCategory);
      region = new Region(5, (short) 0, 5, (short)2);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 0);
      headerCell.setCellValue(strVoucherCategory);
      headerCell.setCellStyle(ReportCriteriaStyle);   
      
      String strStoreValue = getTranslatedCaptionMsg("Store") + ": " + (strStore.equals("")?"ALL":strStore);
      region = new Region(6, (short) 0, 6, (short)2);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 6);
      headerCell = headerRow.createCell((short) 0);
      headerCell.setCellValue(strStoreValue);
      headerCell.setCellStyle(ReportCriteriaStyle);      

      String strIssuedDateFrom = getTranslatedCaptionMsg("Issued Date From") + ": " + strFrDateIssue;
      region = new Region(4, (short) 9, 4, (short)10);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 4);
      headerCell = headerRow.createCell((short) 9);
      headerCell.setCellValue(strIssuedDateFrom);
      headerCell.setCellStyle(ReportCriteriaStyle);   
      
      String strIssuedDateTo = getTranslatedCaptionMsg("Issued Date To") + ": " + strToDateIssue;
      region = new Region(5, (short) 9, 5, (short)10); 
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 9);
      headerCell.setCellValue(strIssuedDateTo);
      headerCell.setCellStyle(ReportCriteriaStyle);   
 
      String strPrintedBy = getTranslatedCaptionMsg("Printed By") + ": ";
      region = new Region(4, (short) 11, 4, (short)11);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 4);
      headerCell = headerRow.createCell((short) 11);
      headerCell.setCellValue(strPrintedBy);
      headerCell.setCellStyle(ReportCriteriaStyle);
    
      region = new Region(4, (short) 12, 4, (short)12);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 4);
      headerCell = headerRow.createCell((short) 12);
      headerCell.setCellValue(strUsrId);
      headerCell.setCellStyle(ReportCriteriaStyle);
    
      String strPrintedDate = getTranslatedCaptionMsg("Printed Date") + ": ";
      region = new Region(5, (short) 11, 5, (short)11);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 11);
      headerCell.setCellValue(strPrintedDate);
      headerCell.setCellStyle(ReportCriteriaStyle);

      region = new Region(5, (short) 12, 5, (short)12);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 12);
      headerCell.setCellValue(printdate);
      headerCell.setCellStyle(ReportCriteriaStyle);

      region = new Region(5, (short) 13, 5, (short)13);
      sheet.addMergedRegion(region);
      headerRow = sheet.createRow((short) 5);
      headerCell = headerRow.createCell((short) 13);
      headerCell.setCellValue(printtime);
      headerCell.setCellStyle(ReportCriteriaStyle);

   }           
   
   private void createSheetDaily(String name) 
   {
      int j = 0; 

      //create a new worksheet
      sheet = workBook.createSheet(name);
      
      sheet.setColumnWidth((short)j++,(short)2000);
      sheet.setColumnWidth((short)j++,(short)3000);
      sheet.setColumnWidth((short)j++,(short)4000);
      sheet.setColumnWidth((short)j++,(short)4000); 
      sheet.setColumnWidth((short)j++,(short)3500);
      sheet.setColumnWidth((short)j++,(short)1500);
      sheet.setColumnWidth((short)j++,(short)2500);
      sheet.setColumnWidth((short)j++,(short)5500); 
      sheet.setColumnWidth((short)j++,(short)6500);
      sheet.setColumnWidth((short)j++,(short)5500);
      sheet.setColumnWidth((short)j++,(short)4500);
      sheet.setColumnWidth((short)j++,(short)4500);
      sheet.setColumnWidth((short)j++,(short)4500); 
      sheet.setColumnWidth((short)j++,(short)4500); 
      sheet.setColumnWidth((short)j++,(short)4500);
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
      String strDesc = "DailyGvIssue_";
      return strDesc;
   }
   
   private void closeStatement()   
   {
      try{
      	if(pstmtDEL!=null) 
           pstmtDEL.close();
      }catch(Exception e){}
      pstmtDEL=null; 
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
//           PrintDailyGvIssueRptAMY printDailyGvIssueRptAMY = new PrintDailyGvIssueRptAMY("/tmp/VoucherDownloadExcel"+System.currentTimeMillis()+".xls");
//           voucherDownloadExcelReport.setTestRun(false);                  
//           voucherDownloadExcelReport.print(hParam); 
//       }
//       catch (Exception de) {  
//           de.printStackTrace();
//       }
//        System.exit(0);
//    }    
   
}