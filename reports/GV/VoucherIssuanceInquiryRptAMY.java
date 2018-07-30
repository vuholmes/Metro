 package qrcom.PROFIT.reports.GV;

 import com.lowagie.text.BadElementException;
 import com.lowagie.text.Cell;
 import com.lowagie.text.Document;
 import com.lowagie.text.DocumentException;
 import com.lowagie.text.Element;
 import com.lowagie.text.Font;
 import com.lowagie.text.Image;
 import com.lowagie.text.PageSize;
 import com.lowagie.text.Paragraph;
 import com.lowagie.text.Phrase;
 import com.lowagie.text.Rectangle;
 import com.lowagie.text.Table;
 import com.lowagie.text.pdf.BaseFont;
 import com.lowagie.text.pdf.PdfContentByte;
 import com.lowagie.text.pdf.PdfPCell;
 import com.lowagie.text.pdf.PdfPCellEvent;
 import com.lowagie.text.pdf.PdfPTable;
 import com.lowagie.text.pdf.PdfPTableEvent;
 import com.lowagie.text.pdf.PdfWriter;

 import java.awt.Color;

 import java.io.OutputStream;

 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;

 import java.text.DecimalFormat;
 import java.text.SimpleDateFormat;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 import qrcom.PROFIT.files.info.AdlangmstSQL;
 import qrcom.PROFIT.files.info.AduserInfo;
 import qrcom.PROFIT.files.info.AltDescUtil;
 import qrcom.PROFIT.files.info.GvdenomstInfoAMY;
 import qrcom.PROFIT.files.info.GvdenomstSQLAMY;
 import qrcom.PROFIT.files.info.GvtypemstInfoAMY;
 import qrcom.PROFIT.files.info.GvtypemstSQLAMY;
 import qrcom.PROFIT.files.info.ProfitvvSQL;
 import qrcom.PROFIT.files.info.StrmstInfo;
 import qrcom.PROFIT.files.info.StrmstSQL;
 import qrcom.PROFIT.files.info.VnmstrSQL;
 import qrcom.PROFIT.reports.GenericReport;

 import qrcom.util.HParam;
 import qrcom.util.qrMisc;

 public class VoucherIssuanceInquiryRptAMY extends GenericReport {
     private PreparedStatement pstmtHdr = null;
     private ResultSet rsHdr = null;
     private PreparedStatement pstmt = null;
     private ResultSet rs = null;
     private String sql_query = null;
     private String query = null;
     private VnmstrSQL vnmstrSQL = null;
     private ProfitvvSQL profitvvSQL = null;
     private SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
     private GvdenomstSQLAMY gvdenomstSQL = null;
     private GvtypemstSQLAMY gvtypemstSQL = null;
     private StrmstSQL strmstSQL = null;

     private Font FontChineseSmall = null;
     private Font FontChinese_title = null;
     private Font FontChinese_title2 = null;
     private Font FontChinese_tb_title = null;
     private Font FontPageNo = null;
     private Font FontChineseItalic = null;
     private Font FontChineseItalic_white = null;
     private Font FontChineseBoldItalic = null;
     private Font FontChineseBoldItalic_white = null;
     private Font FontChinese_tb_title_small = null;
     private Font FontChinese_tb_title_small2 = null;
     private Font FontChinese_tb_title_white = null;
     private Font FontChineseSmall_red = null;
     private Font FontChinese_tb_title_red = null;
     private Font FontChineseBoldUnderlined = null;

     private String SYSRptFontTaxInv = ""; //getting Arial font
     private String SYSRptFontEncode2 = ""; //encoding for Arial font
     private PdfPTable tableSuperHdr = null;
     private PdfPTable tableSuperSuperHdr = null;
     private PdfPTable tableHdr = null;
     private PdfPTable tableFooter1 = null;
     private PdfPTable tableFooter2 = null;
     private PdfPTable tableFooter3 = null;
     private PdfPTableEvent tableBackground = null;

     private String coy_logo_image_file = "";
     private String SYSCompanyName = "";
     private String SYSCompanyRegNo = "";
     private String SYSDefaultLanguage = "";
     private String SYSReportLine = "";
     private String SYSMbrCardTyp = ""; //added on 20160331 get member card type
     private String SysIssueSelRefRsnCd = "";
     private String SYSGVSelRefGVTYPE = "";
     private String user_lang = null;
     private String USER_ID = null;
     private String TOTAL_ROW = null;
     private String COY = null;
     private String COY_SUB = null;
     private Map resultMap = null;
     private List resultMapList = null;
     private Map reportBodyRecord = null;
     private String receiptType = null;
     private String gvRsnMstPrintType = null;
     private String rsnCd = null;
     private String gvType = null;
     private Map rsMap = null;
     private String reprint = "false";
     private int maxCount = 6; // Maximum 6 record per page.
     private int maxcount_printType1 = 6;
     private int maxCount_2 = 30; // Maximum 30 record per page.
     private int maxCount_3 = 5; // Maximum 5 record per page.
     private int maxCount_4 = 40; // Maximum 20 record per page.
     private int count = 0;
     private int currentItemNo = 1;
     private int currentItemNoWOReset_1 = 1;
     private int currentItemNoWOReset_2 = 1;
     private int currentPage = 0;
     private int totalPage = 0;
     private boolean printBlankPage_ = false;
     private boolean _newPage = false;
     private String tempCopyType = null;
     private double currentPageTotalAmount = 0.00;
     private int currentPageTotalQuantity = 0;
     private int currentPageTotalPoints = 0;
     private int totalRecordSelected = 0;
     private Color bgColor = new Color(128, 128, 128);
     private Image company_logo_image = null;
     private String COPY_1 = "(Internal Used Copy)";
     private String COPY_2 = "(Customer Copy)";
     private String strHEADOFFICE = "HEAD OFFICE";
     private String strADDRESS_1 = "COY_ADDRESS_ROW1";
     private String strADDRESS_2 = "COY_ADDRESS_ROW2";
     private String strADDRESS_3 = "COY_ADDRESS_ROW3";
     private String strADDRESS_4 = "COY_ADDRESS_ROW4";

     private String strFOOTER_1 ="";

     private String strFOOTER_3 = "GV_RECEIPT_FOOTER";

     private String strNAME_COLLECTOR = "Reference 1";
     private String strNRIC_NO = "Reference 2";
     private String strNAME_STAFF = "Name of Staff";
     private String strSTAFF_ID = "Staff ID";
     private String strSIGNATURE = "Signature";
     private String strNOTE = "NOTE";
     private String strFooterLine1 = "GV_MBR_REDEMPTION_NOTE";
     private String strFooterLine6 = "GV_RB_REDEMPTION_NOTE1";
     private String strFooterLine7 = "GV_RB_REDEMPTION_NOTE2";
     private String strFooterLine8 = "GV_RB_REDEMPTION_NOTE3";
     private String strFooterLine9 = "GV_RB_REDEMPTION_NOTE4";

     private final String strISSUANCE_FORM = "1";
     private final String strMBR_REDEMPTION_FORM = "2";
     private final String strRBT_REDEMPTION_FORM = "3";
     private String SYSProfitSchema = "";
     private String SYSCLSSchema = "";

     public VoucherIssuanceInquiryRptAMY() {
     }

     public VoucherIssuanceInquiryRptAMY(String filename) {
         super(filename);
     }

     public VoucherIssuanceInquiryRptAMY(OutputStream outStream) {
         super(outStream);
     }

     private void initObjSQL() throws SQLException {
         adlangmstSQL = new AdlangmstSQL(conn);
         profitvvSQL = new ProfitvvSQL(conn);
         vnmstrSQL = new VnmstrSQL(conn);
         gvdenomstSQL = new GvdenomstSQLAMY(conn);
         gvtypemstSQL = new GvtypemstSQLAMY(conn);
         strmstSQL = new StrmstSQL(conn);
     }

     private void jInit(HParam hParam) throws Exception {
         USER_ID = hParam.getString("USER_ID");
         System.out.println("USER ID = " + USER_ID);
         TOTAL_ROW = hParam.getString("TOTAL_ROW").trim();
         user_lang = retrieveUserLanguage(USER_ID);
         if (user_lang.trim().length() == 0)
             user_lang = "0";
         super.USER_LANGUAGE = user_lang;
         totalRecordSelected = Integer.parseInt(TOTAL_ROW);
         tableBackground = new TableBackground();
         SYSProfitSchema = getPROFITVV(COY, "SYSProfitSchema"); //CLS Integration
         SYSCLSSchema = getPROFITVV(COY, "SYSCLSSchema"); //CLS Integration
         String tmpMaxUpl = getPROFITVV(COY, "SYSMaxUpldIssueGV");
         maxcount_printType1 = Integer.parseInt(tmpMaxUpl);
         SysIssueSelRefRsnCd = getPROFITVV(COY, "SysIssueSelRefRsnCd");
         SYSGVSelRefGVTYPE = getPROFITVV(COY, "SYSGVSelRefGVTYPE");
     }

     public void print(HParam hParam) {
         System.out.println("*** START PROCESS PRINT ISSUANCE INQUIRY ***");
         try {
             super.openOutputStream();
             openConnection();
             conn.setAutoCommit(false);
             initObjSQL();
             if (hParam.getString("PRINT_TYPE").equals("selected")) {
                 COY = hParam.getString("COY_" + String.valueOf(1)).trim();
                 COY_SUB = hParam.getString("COY_SUB_" + String.valueOf(1)).trim();
                 jInit(hParam);

                 for (int i = 1; i <= totalRecordSelected; i++) {
                     count = 0;
                     resultMap = new HashMap();
                     resultMapList = new ArrayList();
                     rsnCd = hParam.getString("RSN_CD_" + String.valueOf(i)).trim();
                     gvType = hParam.getString("GV_TYPE_" + String.valueOf(i)).trim();
                     String prcType = hParam.getString("PRC_TYPE_" + String.valueOf(i)).trim();
                     reprint = hParam.getString("REPRINT");
                     System.out.println("PRC_ID = " + hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
                     System.out.println("STORE = " + hParam.getString("STORE_" + String.valueOf(i)).trim());
                     System.out.println("RSN_CD = " + rsnCd);
                     System.out.println("GV_TYPE = " + gvType);
                     System.out.println("PRC_TYPE = " + prcType);
                     System.out.println("REPRINT = " + reprint);
                     if (i == 1)
                         openDocument();
                     if (prcType != null && prcType.equals("I")) //issuance
                     {
                         receiptType = getReceiptTypeFromGVRSNMST(gvType, rsnCd);
                         System.out.println("GVRSNMST.RECEIPT_TYPE = " + receiptType);
                         if (receiptType != null && !receiptType.trim().equals("")) {
                             switch (Integer.parseInt(receiptType)) {
                             case 1: //Voucher Collection Receipt
                                 gvRsnMstPrintType = getGvRsnMstPrintType(rsnCd, gvType);
                                 System.out.println("GVRSNMST.PRINT_TYPE = " + gvRsnMstPrintType);
                                 if (gvRsnMstPrintType != null && !gvRsnMstPrintType.equals("")) {
                                     prepareRecordReceiptType_1(hParam, i);
                                     printVoucherIssuanceInquiryReportReceiptType_1(gvRsnMstPrintType);
                                 } else {
                                     printNoPrintType();
                                 }
                                 break;
                             case 2: //Member Redemption Form
                                 prepareRecordReceiptType_2_3(hParam, i, strMBR_REDEMPTION_FORM);
                                 printVoucherIssuanceInquiryReportReceiptType_2();
                                 break;
                             case 3: //Rebate Redemption Form
                                 prepareRecordReceiptType_2_3(hParam, i, strRBT_REDEMPTION_FORM);
                                 printVoucherIssuanceInquiryReportReceiptType_3();
                                 break;
                             }
                         } else {
                             printNoReceiptType();
                         }
                     } else if (prcType != null && prcType.equals("L")) //cancellation
                     {
                         prepareCancelRecord(hParam, i);
                         currentItemNo = 1;
                         currentItemNoWOReset_1 = 1;
                         currentPage = 0;
                         if (resultMapList != null && !resultMapList.isEmpty()) {
                             totalPage = (int) Math.ceil((double) resultMapList.size() / 15);
                             printVoucherCancellationReport();
                             count++;
                         }
                         if (count == 0) {
                             printIncreaseOneLine();
                             printBlankPage();
                             printBlankCell(1);
                         }
                     }
                 }
             }
             if (document != null) {
                 conn.commit();
                 document.close();
             }
         } catch (Exception e) {
             e.printStackTrace();
             System.out.println(e.getMessage());
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
             try {
                 if (rs != null)
                     rs.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rs = null;
             try {
                 if (pstmt != null)
                     pstmt.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmt = null;
             rsMap = null;
             resultMap = null;
             resultMapList = null;
             sql_query = null;
             query = null;
             closeConnection();
             super.closeOutputStream();
             System.out.println("*** END PROCESS PRINT ISSUANCE INQUIRY ***");
         }
     }

     private void openDocument() throws Exception {
         document = new Document(PageSize.A4); // margin: left, right, top, bottom
         coy_logo_image_file = getPROFITVV(COY, "SYSCompanyLogo");
         SYSCompanyName = getPROFITVV(COY, "SYSCompanyName");
         SYSCompanyRegNo = getPROFITVV(COY, "SYSCompanyRegNo");
         SYSDefaultLanguage = getPROFITVV(COY, "SYSDefaultLanguage");
         SYSReportLine = getPROFITVV(COY, "SYSReportLine");
         SYSRptFontTaxInv = getPROFITVV(COY, "SYSRptFontTaxInv");
         SYSRptFontEncode2 = getPROFITVV(COY, "SYSRptFontEncode2");
         SYSMbrCardTyp = getPROFITVV(COY, "SYSMbrCardTyp"); //Added on 20160331 get member card type
                 
         BASE_FONT_Chinese = BaseFont.createFont(SYSRptFontTaxInv, SYSRptFontEncode2, BaseFont.NOT_EMBEDDED);
                 
         FontChinese = new Font(BASE_FONT_Chinese, 8, Font.NORMAL);
         FontChineseItalic = new Font(BASE_FONT_Chinese, 7, Font.ITALIC);
         FontChinese_tb_title = new Font(BASE_FONT_Chinese, 9, Font.BOLD);
         FontChinese_title = new Font(BASE_FONT_Chinese, 13, Font.BOLD);
         FontChinese_title2 = new Font(BASE_FONT_Chinese, 11, Font.BOLD);
         FontChineseSmall = new Font(BASE_FONT_Chinese, 7, Font.NORMAL);
         FontPageNo = new Font(BASE_FONT_Chinese, 7, Font.NORMAL);
         FontChineseBoldUnderlined = new Font(BASE_FONT_Chinese, 7, Font.BOLD | Font.UNDERLINE);
         FontChineseBoldItalic = new Font(BASE_FONT_Chinese, 7, Font.BOLD | Font.ITALIC);
         FontChinese_tb_title_small = new Font(BASE_FONT_Chinese, 8, Font.BOLD);
         FontChinese_tb_title_small2 = new Font(BASE_FONT_Chinese, 7, Font.BOLD);
         FontChinese_tb_title_white = new Font(BASE_FONT_Chinese, 8, Font.BOLD);
         FontChinese_tb_title_white.setColor(255, 255, 255);
         FontChineseSmall_red = new Font(BASE_FONT_Chinese, 7, Font.NORMAL);
         FontChineseSmall_red.setColor(255, 0, 0);
         FontChinese_tb_title_red = new Font(BASE_FONT_Chinese, 7, Font.BOLD);
         FontChinese_tb_title_red.setColor(255, 0, 0);
         FontChineseItalic_white = new Font(BASE_FONT_Chinese, 7, Font.ITALIC);
         FontChineseItalic_white.setColor(255, 255, 255);
         FontChineseBoldItalic_white = new Font(BASE_FONT_Chinese, 7, Font.BOLD | Font.ITALIC);
         FontChineseBoldItalic_white.setColor(255, 255, 255);
         setGroupingForReport(COY);
         /** creation of the different writers */
         pdfwriter = PdfWriter.getInstance(document, outStream); // MUST place this line right after the new Document(...
         document.open();
     }

     private void printVoucherIssuanceInquiryReportReceiptType_1(String gvRsnMstPrintType) throws Exception {
         boolean resultMapChecker =
             ((String) resultMap.get("NO")) != null && !((String) resultMap.get("NO")).equals("") ? true : false;
         if (resultMapChecker) {
             if (resultMapList != null && !resultMapList.isEmpty() && resultMapList.size() > 0) {
                 tempCopyType = "1";
                 currentItemNo = 1;
                 currentItemNoWOReset_1 = 1;
                 currentItemNoWOReset_2 = 1;
                 currentPage = 0;
                 currentPageTotalAmount = 0.00;
                 _newPage = false;
                 if (gvRsnMstPrintType.equals("1")) {
                     /**
                      *  in one page, print 2 copies of same report,
                      *  each respectively for Internal and for Customer
                      */
                     totalPage = (int) Math.ceil((double) resultMapList.size() / maxcount_printType1);
                     callMethodPrintType1();
                 } else if (gvRsnMstPrintType.equals("2")) {
                     if (resultMapList.size() <= maxCount) {
                         /**
                          *  For record size <= 6
                          *  in one page, print 2 copies of same report,
                          *  ach respectively for Internal and for Customer
                          */
                         totalPage = (int) Math.ceil((double) resultMapList.size() / maxCount);
                         callMethodPrintType2();
                     } else {
                         /**
                          *  For record size > 6
                          *  in one page, print 1 copy for Internal
                          *  in next page, print 1 copy for Customer
                          */
                         totalPage = (int) Math.ceil((double) resultMapList.size() / maxCount_2);
                         callMethodPrintType2_2();
                     }
                 }
                 count++;
             }
         }
         if (count == 0) {
             printIncreaseOneLine();
             printBlankPage();
             printBlankCell(1);
         }
     }

     private void printVoucherIssuanceInquiryReportReceiptType_2() throws Exception {
         if (resultMapList != null && !resultMapList.isEmpty() && resultMapList.size() > 0) {
             currentItemNo = 1;
             currentItemNoWOReset_1 = 1;
             currentPage = 0;
             currentPageTotalAmount = 0.00;
             currentPageTotalQuantity = 0;
             currentPageTotalPoints = 0;
             _newPage = false;
             tempCopyType = "1";
             if (resultMapList.size() <= maxCount_3) {
                 totalPage = (int) Math.ceil((double) resultMapList.size() / maxCount_3);
                 callMethodReceiptType2And3_1();
             } else {
                 totalPage = (int) Math.ceil((double) resultMapList.size() / maxCount_4);
                 callMethodReceiptType2And3_2();
             }
             count++;
         }
         if (count == 0) {
             printIncreaseOneLine();
             printBlankPage();
             printBlankCell(1);
         }
     }

     private void printVoucherIssuanceInquiryReportReceiptType_3() throws Exception {
         if (resultMapList != null && !resultMapList.isEmpty() && resultMapList.size() > 0) {
             currentItemNo = 1;
             currentItemNoWOReset_1 = 1;
             currentPage = 0;
             currentPageTotalAmount = 0.00;
             currentPageTotalQuantity = 0;
             _newPage = false;
             tempCopyType = "1";
             if (resultMapList.size() <= maxCount_3) {
                 totalPage = (int) Math.ceil((double) resultMapList.size() / maxCount_3);
                 callMethodReceiptType2And3_1();
             } else {
                 totalPage = (int) Math.ceil((double) resultMapList.size() / maxCount_4);
                 callMethodReceiptType2And3_2();
             }
             count++;
         }
         if (count == 0) {
             printIncreaseOneLine();
             printBlankPage();
             printBlankCell(1);
         }
     }

     private void callMethodPrintType1() throws Exception {
         if (currentPage != totalPage || tempCopyType.equals("2") ||
             (tempCopyType.equals("1") && currentItemNoWOReset_1 > currentItemNoWOReset_2)) {
             if (tempCopyType.equals("1")) {
                 resultMap.put("WHICH_COPY", COPY_1);
                 if (currentItemNoWOReset_1 == 1 || _newPage) {
                     currentPage++;
                     printNewPage();
                     initializeHeaderTableReceiptType_1();
                     printHeader(tableSuperSuperHdr, tableSuperHdr, tableHdr);
                 }
                 if (currentItemNo <= maxcount_printType1) {
                     if (currentItemNoWOReset_1 <= resultMapList.size()) {
                         reportBodyRecord = (HashMap) resultMapList.get(currentItemNoWOReset_1 - 1);
                         printDataTable();
                     } else {
                         printBlankDataTable();
                     }
                     currentItemNo++;
                     currentItemNoWOReset_1++;
                     _newPage = false;
                     if ((currentItemNo - 1) == maxcount_printType1) {
                         printTotalTable();
                         initializeFooterTable_1();
                         printFooterTable();
                         printBlankCell(4);
                         tempCopyType = "2";
                         currentItemNo = 1;
                         currentPageTotalAmount = 0.00;
                         _newPage = true;
                     }
                 }
                 callMethodPrintType1();
             } else if (tempCopyType.equals("2")) {
                 resultMap.put("WHICH_COPY", COPY_2);
                 if (currentItemNoWOReset_2 == 1 || _newPage) {
                     createDottedTable();
                     printBlankCell(1);
                     initializeHeaderTableReceiptType_1();
                     printHeader(tableSuperSuperHdr, tableSuperHdr, tableHdr);
                 }
                 if (currentItemNo <= maxcount_printType1) {
                     if (currentItemNoWOReset_2 <= resultMapList.size()) {
                         reportBodyRecord = (HashMap) resultMapList.get(currentItemNoWOReset_2 - 1);
                         printDataTable();
                     } else {
                         printBlankDataTable();
                     }
                     currentItemNo++;
                     currentItemNoWOReset_2++;
                     _newPage = false;
                     if ((currentItemNo - 1) == maxcount_printType1) {
                         printTotalTable();
                         initializeFooterTable_1();
                         printFooterTable();
                         printBlankCell(1);
                         printTotalPage();
                         tempCopyType = "1";
                         currentItemNo = 1;
                         currentPageTotalAmount = 0.00;
                         _newPage = true;
                     }
                 }
                 callMethodPrintType1();
             }
         }
     }

     private void callMethodPrintType2() throws Exception {
         currentPage = 0;
         currentItemNo = 1;
         currentPageTotalAmount = 0.00;
         if (tempCopyType.equals("1")) {
             resultMap.put("WHICH_COPY", COPY_1);
             if (currentItemNoWOReset_1 == 1) {
                 printNewPage();
             }
         } else {
             resultMap.put("WHICH_COPY", COPY_2);
             createDottedTable();
             printBlankCell(1);
         }
         initializeHeaderTableReceiptType_1();
         printHeader(tableSuperSuperHdr, tableSuperHdr, tableHdr);
         for (int i = currentItemNo; i <= maxCount; i++) {
             if (currentItemNo <= resultMapList.size()) {
                 reportBodyRecord = (HashMap) resultMapList.get(currentItemNo - 1);
                 printDataTable();
             } else {
                 printBlankDataTable();
             }
             currentItemNo++;
         }
         printTotalTable();
         initializeFooterTable_1();
         printFooterTable();
         if (tempCopyType.equals("1")) {
             tempCopyType = "2";
             printBlankCell(4);
             callMethodPrintType2();
         } else {
             currentPage = 1;
             printBlankCell(1);
             printTotalPage();
         }
     }

     private void callMethodPrintType2_2() throws Exception {
         if (currentPage != totalPage) {
             if (currentPage == 0) {
                 if (tempCopyType.equals("1")) {
                     resultMap.put("WHICH_COPY", COPY_1);
                 } else {
                     resultMap.put("WHICH_COPY", COPY_2);
                 }
                 initializeHeaderTableReceiptType_1();
             }
             printNewPage();
             printHeader(tableSuperSuperHdr, tableSuperHdr, tableHdr);
             currentPage++;
             for (int i = currentItemNoWOReset_1; i <= resultMapList.size(); i++) {
                 if (currentItemNo <= maxCount_2) {
                     reportBodyRecord = (HashMap) resultMapList.get(currentItemNoWOReset_1 - 1);
                     printDataTable();
                     currentItemNo++;
                     currentItemNoWOReset_1++;
                 } else {
                     break;
                 }
             }
             printTotalTable();
             initializeFooterTable_1();
             printFooterTable();
             if (currentItemNo != maxCount_2) {
                 printBlankCell(maxCount_2 - currentItemNo);
             }
             printBlankCell(6);
             printTotalPage();
             if (tempCopyType.equals("1") && currentPage == totalPage) {
                 tempCopyType = "2";
                 currentPage = 0;
                 currentItemNoWOReset_1 = 1;
             }
             currentItemNo = 1;
             currentPageTotalAmount = 0.00;
             callMethodPrintType2_2();
         }
     }

     private void callMethodReceiptType2And3_1() throws Exception {
         currentPage = 0;
         currentItemNo = 1;
         currentItemNoWOReset_1 = 1;
         currentPageTotalAmount = 0.00;
         currentPageTotalQuantity = 0;
         currentPageTotalPoints = 0;
         if (tempCopyType.equals("1")) {
             resultMap.put("WHICH_COPY", COPY_1);
             if (currentItemNoWOReset_1 == 1) {
                 printNewPage();
             }
         } else {
             resultMap.put("WHICH_COPY", COPY_2);
             printBlankCell(3);
             createDottedTable();
             printBlankCell(1);
         }
         if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
             initializeHeaderTableReceiptType_2();
         } else if (receiptType.equals(strRBT_REDEMPTION_FORM)) {
             initializeHeaderTableReceiptType_3();
         }
         printHeader(tableSuperSuperHdr, tableSuperHdr, tableHdr);
         for (int i = currentItemNo; i <= maxCount_3; i++) {
             if (currentItemNo <= resultMapList.size()) {
                 reportBodyRecord = (HashMap) resultMapList.get(currentItemNo - 1);
                 if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
                     printDataTable_2();
                 } else if (receiptType.equals(strRBT_REDEMPTION_FORM)) {
                     printDataTable_3();
                 }
             } else {
                 printBlankDataTable();
             }
             currentItemNo++;
             currentItemNoWOReset_1++;
         }
         if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
             printTotalTable_2();
             initializeFooterTable_2();
             printBlankCell(2);
         } else if (receiptType.equals(strRBT_REDEMPTION_FORM)) {
             printTotalTable_3();
             initializeFooterTable_3();
             printBlankCell(1);
         }
         printFooterTable();
         if (tempCopyType.equals("1")) {
             tempCopyType = "2";
             callMethodReceiptType2And3_1();
         } else {
             currentPage = 1;
             printTotalPage();
         }
     }

     private void callMethodReceiptType2And3_2() throws Exception {
         if (currentPage != totalPage) {
             if (currentPage == 0) {
                 if (tempCopyType.equals("1")) {
                     resultMap.put("WHICH_COPY", COPY_1);
                 } else {
                     resultMap.put("WHICH_COPY", COPY_2);
                 }
                 if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
                     initializeHeaderTableReceiptType_2();
                 } else if (receiptType.equals(strRBT_REDEMPTION_FORM)) {
                     initializeHeaderTableReceiptType_3();
                 }
             }
             printNewPage();
             printHeader(tableSuperSuperHdr, tableSuperHdr, tableHdr);
             currentPage++;
             for (int i = currentItemNoWOReset_1; i <= resultMapList.size(); i++) {
                 if (currentItemNo <= maxCount_4) {
                     reportBodyRecord = (HashMap) resultMapList.get(currentItemNoWOReset_1 - 1);
                     if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
                         printDataTable_2();
                     } else if (receiptType.equals(strRBT_REDEMPTION_FORM)) {
                         printDataTable_3();
                     }
                     currentItemNo++;
                     currentItemNoWOReset_1++;
                 } else {
                     break;
                 }
             }
             if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
                 printTotalTable_2();
                 initializeFooterTable_2();
                 if (currentItemNo != maxCount_4) {
                     printBlankCell(maxCount_4 - currentItemNo);
                 }
             } else if (receiptType.equals(strRBT_REDEMPTION_FORM)) {
                 printTotalTable_3();
                 initializeFooterTable_3();
                 if (currentItemNo != maxCount_4) {
                     printBlankCell(maxCount_4 - currentItemNo);
                 }
             }
             printFooterTable();
             printTotalPage();
             if (tempCopyType.equals("1") && currentPage == totalPage) {
                 tempCopyType = "2";
                 currentPage = 0;
                 currentItemNoWOReset_1 = 1;
             }
             currentItemNo = 1;
             currentPageTotalAmount = 0.00;
             currentPageTotalQuantity = 0;
             currentPageTotalPoints = 0;
             callMethodReceiptType2And3_2();
         }
     }

     private String getGvRsnMstPrintType(String rsnCd, String gvType) throws Exception {
         String gvRsnMstPrintType = "";
         try {
             sql_query =
                 " SELECT PRINT_TYPE " + " FROM " + SYSProfitSchema + ".GVRSNMST " + " WHERE COY = ? " +
                 " AND COY_SUB = ? " + " AND GV_TYPE = ? " + " AND RSN_CD = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, COY);
             pstmtHdr.setString(2, COY_SUB);
             pstmtHdr.setString(3, gvType);
             pstmtHdr.setString(4, rsnCd);
             rsHdr = pstmtHdr.executeQuery();
             if (rsHdr != null && rsHdr.next()) {
                 gvRsnMstPrintType = rsHdr.getString(1);
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
         return gvRsnMstPrintType;
     }

     private String getReceiptTypeFromGVRSNMST(String GV_TYPE, String RSN_CD) throws Exception {
         String returnReceiptType = "";
         try {
             sql_query =
                 " SELECT RECEIPT_TYPE FROM " + SYSProfitSchema + ".GVRSNMST " + " WHERE COY = ? " +
                 " AND COY_SUB = ? " + " AND GV_TYPE = ? " + " AND RSN_CD = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, COY);
             pstmtHdr.setString(2, COY_SUB);
             pstmtHdr.setString(3, GV_TYPE);
             pstmtHdr.setString(4, RSN_CD);
             rsHdr = pstmtHdr.executeQuery();
             if (rsHdr != null && rsHdr.next()) {
                 returnReceiptType = rsHdr.getString(1);
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
         return returnReceiptType;
     }

     private void prepareRecordReceiptType_1(HParam hParam, int i) throws Exception {
         getHeaderAndFooterRecordReceiptType_1(hParam, i);
         getIssueType(hParam, i);
         getBodyRecordReceiptType_1(hParam, i);
         getTotalAmountReceiptType_1(hParam, i);
     }

     private void prepareRecordReceiptType_2_3(HParam hParam, int i, String type) throws Exception {
         getHeaderAndFooterRecordReceiptType_2_3(hParam, i);
         if (type.equals(strMBR_REDEMPTION_FORM)) {
             getMbrRedemptionInformation(hParam, i); //added for CLS Integration
             getBodyRecordReceiptType_2(hParam, i);
         } else if (type.equals(strRBT_REDEMPTION_FORM)) {
             getRbtRedemptionInformation(hParam, i); //added for CLS Integration
             getBodyRecordReceiptType_3(hParam, i);
         }
         getTotalAmountReceiptType_2_3(hParam, i);
     }

     private void prepareCancelRecord(HParam hParam, int i) throws Exception {
         getHeaderCancelRecord(hParam, i);
         getBodyCancelRecord(hParam, i);
         System.out.println("resultMap = " + resultMap);
         System.out.println("is resultMapList empty ? " + resultMapList.isEmpty());
     }

     private void getHeaderAndFooterRecordReceiptType_1(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT " + " H.COY, H.COY_SUB, H.STORE, H.TRANS_OPR, H.SERIAL_NUM, " +
                 " H.DATE_ISSUE, H.TRANS_DATE, H.PCH_NAME, H.PCH_CONTACT_NO, " +
                 " H.COLL_NAME, H.COLL_NRIC, H.RSN_CD, H.PAYMENTMODE " + // modified by Shriyle 2016-03-22
                 " FROM " + SYSProfitSchema + ".GVTRANS H " + " WHERE " + " H.TRANS_NO IN (SELECT G.TRANS_NO FROM " +
                 SYSProfitSchema + ".GVLOGBOOK G WHERE G.PRC_ID = ?) " + " AND H.TRANS_TYPE = 'I' " + " AND H.COY = ? " +
                 " AND H.COY_SUB = ? " + " AND H.STORE = ?";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB); 
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             if (rsHdr != null && rsHdr.next()) {
                 String strSTORE =
                     rsHdr.getString("STORE") != null && !((String) rsHdr.getString("STORE")).equals("") ?
                     (String) rsHdr.getString("STORE") : "";
                 String strSERIAL =
                     rsHdr.getString("SERIAL_NUM") != null && !((String) rsHdr.getString("SERIAL_NUM")).equals("") ?
                     (String) rsHdr.getString("SERIAL_NUM") : "";
                 AduserInfo aduserInfo = new AduserInfo();
                 aduserInfo = getAduserInfo(rsHdr.getString("TRANS_OPR"));
                 String strUserName = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
                 resultMap.put("NO", (strSTORE + " - " + strSERIAL));
                 resultMap.put("PURCHASER", rsHdr.getString("PCH_NAME"));
                 resultMap.put("CONTACT_NO", rsHdr.getString("PCH_CONTACT_NO"));
                 resultMap.put("DATE", rsHdr.getString("DATE_ISSUE"));
                 resultMap.put("NAME_OF_COLLECTOR", rsHdr.getString("COLL_NAME"));
                 resultMap.put("NRIC_NO", rsHdr.getString("COLL_NRIC"));
                 resultMap.put("NAME_OF_STAFF", strUserName);
                 resultMap.put("STAFF_ID", rsHdr.getString("TRANS_OPR"));
                 resultMap.put("PAYMENT_MODE", rsHdr.getString("PAYMENTMODE"));
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
     }

     private void getHeaderAndFooterRecordReceiptType_2_3(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT DISTINCT " + " H.COY, H.COY_SUB, H.STORE, H.TRANS_OPR, H.SERIAL_NUM, " +
                 " H.DATE_ISSUE, H.PCH_NAME, H.PCH_CONTACT_NO, H.COLL_NRIC, H.MEMBERSHIP_NO, H.RSN_CD " + " FROM " +
                 SYSProfitSchema + ".GVTRANS H " + " WHERE " + " H.TRANS_NO IN (SELECT G.TRANS_NO FROM " +
                 SYSProfitSchema + ".GVLOGBOOK G WHERE G.PRC_ID = ?) " + " AND H.TRANS_TYPE = 'I' " + " AND H.COY = ? " +
                 " AND H.COY_SUB = ? " + " AND H.STORE = ? " + " AND H.RSN_CD = ?";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB);
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             pstmtHdr.setString(5, hParam.getString("RSN_CD_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             if (rsHdr != null && rsHdr.next()) {
                 String strSTORE =
                     rsHdr.getString("STORE") != null && !((String) rsHdr.getString("STORE")).equals("") ?
                     (String) rsHdr.getString("STORE") : "";
                 String strSERIAL =
                     rsHdr.getString("SERIAL_NUM") != null && !((String) rsHdr.getString("SERIAL_NUM")).equals("") ?
                     (String) rsHdr.getString("SERIAL_NUM") : "";
                 String strSTORE_NAME = getStoreName(strSTORE);
                 AduserInfo aduserInfo = new AduserInfo();
                 aduserInfo = getAduserInfo(rsHdr.getString("TRANS_OPR"));
                 String strUserName = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
                 resultMap.put("NO", (strSTORE + strSERIAL));
                 resultMap.put("DATE", rsHdr.getString("DATE_ISSUE"));
                 resultMap.put("STORE_CODE", strSTORE + " - " + strSTORE_NAME);
                 resultMap.put("NAME_OF_STAFF", strUserName);
                 resultMap.put("EMPLOYEE_NO", rsHdr.getString("TRANS_OPR"));
                 //mega 20160215 START
                 //resultMap.put("NAME", rsHdr.getString("PCH_NAME"));                 //before CLS Integration
                 //resultMap.put("MEMBERSHIP_NO", rsHdr.getString("MEMBERSHIP_NO"));   //before CLS Integration
                 //resultMap.put("NRIC_NO", rsHdr.getString("COLL_NRIC"));             //before CLS Integration
                 //resultMap.put("TELEPHONE_NO", rsHdr.getString("PCH_CONTACT_NO"));   //before CLS Integration
                 //resultMap.put("MBR_EMAIL", "");                                     //before CLS Integration
                 resultMap.put("_SERIAL_NUM", strSERIAL); //after CLS Integration
                 resultMap.put("_RSN_CD", rsHdr.getString("RSN_CD"));
                 //mega 20160215 END
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
     }

     private void getMbrRedemptionInformation(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT RD.COY, RD.COY_SUB, RD.MBR_CODE, RD.STORE, M.MBR_NAME, " +
                 " M.MBR_IC_NUMBER, M.MBR_PASSPORT, RD.TOTAL_REDEEM_POINT, " + " RD.MBR_POINT_BAL, M.MBR_CARD_TYPE  " +
                 " FROM " + SYSCLSSchema + ".MBRRDMPTHDR RD " + " INNER JOIN " + SYSCLSSchema +
                 ".MEMBERMST M ON M.MBR_CODE = RD.MBR_CODE " + " WHERE RD.REDEEM_REF = ? " + " AND RD.STORE =  ? " +
                 " AND RD.COY = ? " + " AND RD.COY_SUB = ? " + " AND RD.RSN_CD = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, (String) resultMap.get("_SERIAL_NUM"));
             pstmtHdr.setString(2, hParam.getString("STORE_" + String.valueOf(i)).trim());
             pstmtHdr.setString(3, COY);
             pstmtHdr.setString(4, COY_SUB);
             pstmtHdr.setString(5, (String) resultMap.get("_RSN_CD"));
             rsHdr = pstmtHdr.executeQuery();
             //Added on 20160331 -if cannot find member from CLS system
             query =
                 " SELECT DISTINCT " + " H.COY, H.COY_SUB, H.STORE, H.TRANS_OPR, H.SERIAL_NUM, " +
                 " H.DATE_ISSUE, H.PCH_NAME, H.PCH_CONTACT_NO, H.COLL_NRIC, H.MEMBERSHIP_NO " + " FROM " +
                 SYSProfitSchema + ".GVTRANS H " + " WHERE " + " H.TRANS_NO IN (SELECT G.TRANS_NO FROM " +
                 SYSProfitSchema + ".GVLOGBOOK G WHERE G.PRC_ID = ?) " + " AND H.TRANS_TYPE = 'I' " + " AND H.COY = ? " +
                 " AND H.COY_SUB = ? " + " AND H.STORE = ?" + " AND H.RSN_CD = ?";
             pstmt = conn.prepareStatement(query);
             pstmt.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmt.setString(2, COY);
             pstmt.setString(3, COY_SUB);
             pstmt.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             pstmt.setString(5, (String) resultMap.get("_RSN_CD"));
             rs = pstmt.executeQuery();
             //End Added
             if (rsHdr != null && rsHdr.next()) {
                 String strNRIC = "";
                 if (rsHdr.getString("MBR_IC_NUMBER") != null && !rsHdr.getString("MBR_IC_NUMBER").trim().equals("")) {
                     strNRIC = rsHdr.getString("MBR_IC_NUMBER").trim();
                 } else if (rsHdr.getString("MBR_PASSPORT") != null &&
                            !rsHdr.getString("MBR_PASSPORT").trim().equals("")) {
                     strNRIC = rsHdr.getString("MBR_PASSPORT").trim();
                 }
                 int redeemeded_point = 0;
                 if (rsHdr.getString("TOTAL_REDEEM_POINT") != null &&
                     !rsHdr.getString("TOTAL_REDEEM_POINT").trim().equals("")) {
                     redeemeded_point = rsHdr.getInt("TOTAL_REDEEM_POINT");
                 }
                 int balance_point = 0;
                 if (rsHdr.getString("MBR_POINT_BAL") != null && !rsHdr.getString("MBR_POINT_BAL").trim().equals("")) {
                     balance_point = rsHdr.getInt("MBR_POINT_BAL");
                 }
                 int accum_point = redeemeded_point + balance_point;
                 resultMap.put("NAME", rsHdr.getString("MBR_NAME"));
                 resultMap.put("MEMBERSHIP_NO", rsHdr.getString("MBR_CODE"));
                 resultMap.put("NRIC_NO", strNRIC);
                 resultMap.put("REDEEMED", String.valueOf(redeemeded_point));
                 resultMap.put("BALANCE", String.valueOf(balance_point));
                 resultMap.put("ACCUMULATED", String.valueOf(accum_point));
                 resultMap.put("MBR_CARDTYP", rsHdr.getString("MBR_CARD_TYPE"));
             }
             //Added on 20160331 -if cannot find member from CLS system
             else {
                 if (rs != null && rs.next()) {
                     AduserInfo aduserInfo = new AduserInfo();
                     aduserInfo = getAduserInfo(rs.getString("TRANS_OPR"));
                     String strUserName = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
                     //mega 20160215 START  (if cannot find member from CLS system )
                     resultMap.put("NAME", rs.getString("PCH_NAME")); //before CLS Integration
                     resultMap.put("MEMBERSHIP_NO", rs.getString("MEMBERSHIP_NO")); //before CLS Integration
                     resultMap.put("NRIC_NO", rs.getString("COLL_NRIC")); //before CLS Integration
                     resultMap.put("TELEPHONE_NO", rs.getString("PCH_CONTACT_NO")); //before CLS Integration
                     resultMap.put("MBR_EMAIL", ""); //before CLS Integration
                     resultMap.put("MBR_CARDTYP", "");
                     //mega 20160215 END
                 }
             } //end added
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
             try {
                 if (rs != null)
                     rs.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rs = null;
             try {
                 if (pstmt != null)
                     pstmt.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmt = null;
         }
     }

     private void getRbtRedemptionInformation(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT RB.MBR_CODE, RB.TOTAL_REDEEM_AMT, M.MBR_NAME, " +
                 " M.MBR_IC_NUMBER, M.MBR_PASSPORT, M.MBR_MOBILE_NO, M.MBR_EMAIL, M.MBR_CARD_TYPE " + " FROM " +
                 SYSCLSSchema + ".MBRRDMREB RB " + " INNER JOIN " + SYSCLSSchema +
                 ".MEMBERMST M ON M.MBR_CODE = RB.MBR_CODE " + " WHERE RB.REDEEM_REF = ? " + " AND RB.STORE =  ? " +
                 " AND RB.COY = ? " + " AND RB.COY_SUB = ? " + " AND RB.RSN_CD = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, (String) resultMap.get("_SERIAL_NUM"));
             pstmtHdr.setString(2, hParam.getString("STORE_" + String.valueOf(i)).trim());
             pstmtHdr.setString(3, COY);
             pstmtHdr.setString(4, COY_SUB);
             pstmtHdr.setString(5, (String) resultMap.get("_RSN_CD"));
             rsHdr = pstmtHdr.executeQuery();
             //Added on 20160331 -if cannot find member from CLS system
             query =
                 " SELECT DISTINCT " + " H.COY, H.COY_SUB, H.STORE, H.TRANS_OPR, H.SERIAL_NUM, " +
                 " H.DATE_ISSUE, H.PCH_NAME, H.PCH_CONTACT_NO, H.COLL_NRIC, H.MEMBERSHIP_NO " + " FROM " +
                 SYSProfitSchema + ".GVTRANS H " + " WHERE " + " H.TRANS_NO IN (SELECT G.TRANS_NO FROM " +
                 SYSProfitSchema + ".GVLOGBOOK G WHERE G.PRC_ID = ?) " + " AND H.TRANS_TYPE = 'I' " + " AND H.COY = ? " +
                 " AND H.COY_SUB = ? " + " AND H.STORE = ? " + " AND H.RSN_CD = ? ";
             pstmt = conn.prepareStatement(query);
             pstmt.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmt.setString(2, COY);
             pstmt.setString(3, COY_SUB);
             pstmt.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             pstmt.setString(5, (String) resultMap.get("_RSN_CD"));
             rs = pstmt.executeQuery();
             //End Added
             if (rsHdr != null && rsHdr.next()) {
                 String strNRIC = "";
                 if (rsHdr.getString("MBR_IC_NUMBER") != null && !rsHdr.getString("MBR_IC_NUMBER").trim().equals("")) {
                     strNRIC = rsHdr.getString("MBR_IC_NUMBER").trim();
                 } else if (rsHdr.getString("MBR_PASSPORT") != null &&
                            !rsHdr.getString("MBR_PASSPORT").trim().equals("")) {
                     strNRIC = rsHdr.getString("MBR_PASSPORT").trim();
                 }
                 String strEMAIL = "";
                 if (rsHdr.getString("MBR_EMAIL") != null && !rsHdr.getString("MBR_EMAIL").trim().equals("")) {
                     strEMAIL = rsHdr.getString("MBR_EMAIL").trim();
                 }
                 String strTEL = "";
                 if (rsHdr.getString("MBR_MOBILE_NO") != null && !rsHdr.getString("MBR_MOBILE_NO").trim().equals("")) {
                     strTEL = rsHdr.getString("MBR_MOBILE_NO").trim();
                 }
                 resultMap.put("NAME", rsHdr.getString("MBR_NAME"));
                 resultMap.put("MEMBERSHIP_NO", rsHdr.getString("MBR_CODE"));
                 resultMap.put("NRIC_NO", strNRIC);
                 resultMap.put("TELEPHONE_NO", strTEL);
                 resultMap.put("MBR_EMAIL", strEMAIL);
                 resultMap.put("MBR_CARDTYP", rsHdr.getString("MBR_CARD_TYPE"));
             }
             //Added on 20160331 -if cannot find member from CLS system
             else {
                 if (rs != null && rs.next()) {
                     AduserInfo aduserInfo = new AduserInfo();
                     aduserInfo = getAduserInfo(rs.getString("TRANS_OPR"));
                     String strUserName = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
                     //mega 20160215 START  (if cannot find member from CLS system )
                     resultMap.put("NAME", rs.getString("PCH_NAME")); //before CLS Integration
                     resultMap.put("MEMBERSHIP_NO", rs.getString("MEMBERSHIP_NO")); //before CLS Integration
                     resultMap.put("NRIC_NO", rs.getString("COLL_NRIC")); //before CLS Integration
                     resultMap.put("TELEPHONE_NO", rs.getString("PCH_CONTACT_NO")); //before CLS Integration
                     resultMap.put("MBR_EMAIL", ""); //before CLS Integration
                     resultMap.put("MBR_CARDTYP", "");
                     //mega 20160215 END
                 }
             } //end added
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
             try {
                 if (rs != null)
                     rs.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rs = null;
             try {
                 if (pstmt != null)
                     pstmt.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmt = null;
         }
     }

     private void getIssueType(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT RSN_DESC " + " FROM " + SYSProfitSchema + ".GVRSNMST " + " WHERE " + " GV_TYPE in ( " +
                 " SELECT DISTINCT G.GV_TYPE " + " FROM " + SYSProfitSchema + ".GVLOGBOOK G, " + SYSProfitSchema +
                 ".GVDENOMST D " + " WHERE D.GV_TYPE = G.GV_TYPE " + " AND D.GV_DENOMINATION = G.GV_DENOMINATION " +
                 " AND D.COY = G.COY " + " AND D.COY_SUB = G.COY_SUB " + " AND G.PRC_ID = ? " + " AND G.STORE = ? " +
                 " AND G.PRC_TYPE = 'I' " + " ) " + " AND RSN_CD = ? " + " AND COY = ? " + " AND COY_SUB = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, hParam.getString("STORE_" + String.valueOf(i)).trim());
             pstmtHdr.setString(3, hParam.getString("RSN_CD_" + String.valueOf(i)).trim());
             pstmtHdr.setString(4, COY);
             pstmtHdr.setString(5, COY_SUB);
             rsHdr = pstmtHdr.executeQuery();
             if (rsHdr != null && rsHdr.next()) {
                 resultMap.put("ISSUE_TYPE", rsHdr.getString("RSN_DESC"));
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
     }

     private void getBodyRecordReceiptType_1(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT G.FR_GV_NO, G.TO_GV_NO, D.AMOUNT, " + " G.TOT_GV_NO, G.TOT_AMOUNT " + " FROM " +
                 SYSProfitSchema + ".GVLOGBOOK G, " + SYSProfitSchema + ".GVDENOMST D " + " WHERE G.PRC_TYPE = 'I' " +
                 " AND D.GV_TYPE = G.GV_TYPE " + " AND D.GV_DENOMINATION = G.GV_DENOMINATION " + " AND D.COY = G.COY " +
                 " AND D.COY_SUB = G.COY_SUB " + " AND G.PRC_ID = ? " + " AND D.COY = ? " + " AND D.COY_SUB = ? " +
                 " AND G.STORE = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB);
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             String strFrGvNo = "";
             String strToGvNo = "";
             String strDescription = "";
             while (rsHdr != null && rsHdr.next()) {
                 rsMap = new HashMap();
                 if (rsHdr.getString("FR_GV_NO") != null && !rsHdr.getString("FR_GV_NO").equals("")) {
                     strFrGvNo = rsHdr.getString("FR_GV_NO");
                 }
                 if (rsHdr.getString("TO_GV_NO") != null && !rsHdr.getString("TO_GV_NO").equals("")) {
                     strToGvNo = rsHdr.getString("TO_GV_NO");
                 }
                 strDescription = strFrGvNo + " - " + strToGvNo;
                 rsMap.put("DESCRIPTION", strDescription);
                 rsMap.put("UNIT_PRICE", rsHdr.getString("AMOUNT"));
                 rsMap.put("QTY", rsHdr.getString("TOT_GV_NO"));
                 rsMap.put("AMOUNT", rsHdr.getString("TOT_AMOUNT"));
                 resultMapList.add(rsMap);
             }
             strFrGvNo = null;
             strToGvNo = null;
             strDescription = null;
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
     }

     private void getBodyRecordReceiptType_2(HParam hParam, int i) throws Exception {
         String strItemCode = "";
         String bodyRecordGvDeno = "";
         String bodyRecordGvDenoDesc = "";
         String strQuantity = "";
         String fromGvNo = "";
         String toGvNo = "";
         String strDescription1 = "";
         String strDescription2 = "";
         try {
             sql_query =
                 " SELECT G.GV_TYPE, T.GV_TYPE_DESC, G.GV_DENOMINATION, D.GV_DENO_DESC, " +
                 " G.FR_GV_NO, G.TO_GV_NO, D.AMOUNT, G.TOT_GV_NO, G.TOT_AMOUNT, A.POINTS, A.TOT_POINTS, " +
                 " D.GITEM_SHORT_SKU, GI.GIFT_PTS, GI.GIFT_ITEM_DESC " + " FROM " + SYSProfitSchema + ".GVLOGBOOK G, " +
                 SYSProfitSchema + ".GVDENOMST D, " + " " + SYSProfitSchema + ".GVTYPEMST T, " + SYSProfitSchema +
                 ".GVTRANS A, " + SYSCLSSchema + ".GITEMMST GI " + " WHERE G.PRC_TYPE = 'I' " +
                 " AND D.GV_TYPE = G.GV_TYPE " + " AND D.GV_DENOMINATION = G.GV_DENOMINATION " + " AND D.COY = G.COY " +
                 " AND D.COY_SUB = G.COY_SUB " + " AND G.GV_TYPE = T.GV_TYPE " + " AND G.COY = T.COY " +
                 " AND G.COY_SUB = T.COY_SUB " + " AND G.COY = A.COY " + " AND G.COY_SUB = A.COY_SUB " +
                 " AND G.TRANS_NO = A.TRANS_NO " + " AND G.PRC_TYPE = A.TRANS_TYPE " + " AND G.PRC_ID = ? " +
                 " AND D.COY = ? " + " AND D.COY_SUB = ? " + " AND G.STORE = ? " +
                 " AND GI.SHORT_SKU = D.GITEM_SHORT_SKU " + " AND GI.STORE = 'XXXX' " + " AND GI.COY = D.COY " +
                 " AND GI.COY_SUB = D.COY_SUB " + " AND GI.GIFT_TYPE = (SELECT VNM_VDTVL FROM " + SYSCLSSchema +
                 ".PROFITVV where COY = GI.COY AND VNM = 'SYSCLSGvGiftType') " +
                 " AND G.PRC_DATE BETWEEN GI.EFF_DATE AND GI.END_DATE ";
             if (resultMap.get("MBR_CARDTYP") != null && !resultMap.get("MBR_CARDTYP").equals("")) {
                 sql_query += " AND GI.MBR_CARD_TYPE = '" + resultMap.get("MBR_CARDTYP") + "' ";
             } else {
                 sql_query += " AND GI.MBR_CARD_TYPE = '" + SYSMbrCardTyp + "' ";
             }
             System.out.println("queryrrr:" + sql_query);
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB);
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             while (rsHdr != null && rsHdr.next()) {
                 rsMap = new HashMap();
                 strItemCode = getStrItemCode(rsHdr.getString("GV_TYPE"), rsHdr.getString("GV_TYPE_DESC"));
                 if (rsHdr.getString("TOT_GV_NO") != null && !rsHdr.getString("TOT_GV_NO").equals("")) {
                     strQuantity = rsHdr.getString("TOT_GV_NO");
                 } else {
                     strQuantity = "";
                 }
                 if (rsHdr.getString("GV_DENOMINATION") != null && !rsHdr.getString("GV_DENOMINATION").equals("")) {
                     bodyRecordGvDeno = rsHdr.getString("GV_DENOMINATION");
                 } else {
                     bodyRecordGvDeno = "";
                 }
                 if (rsHdr.getString("GV_DENO_DESC") != null && !rsHdr.getString("GV_DENO_DESC").equals("")) {
                     bodyRecordGvDenoDesc = rsHdr.getString("GV_DENO_DESC");
                 } else {
                     bodyRecordGvDenoDesc = "";
                 }
                 if (rsHdr.getString("FR_GV_NO") != null && !rsHdr.getString("FR_GV_NO").equals("")) {
                     fromGvNo = rsHdr.getString("FR_GV_NO");
                 } else {
                     fromGvNo = "";
                 }
                 if (rsHdr.getString("TO_GV_NO") != null && !rsHdr.getString("TO_GV_NO").equals("")) {
                     toGvNo = rsHdr.getString("TO_GV_NO");
                 } else {
                     toGvNo = "";
                 }
                 strDescription1 = bodyRecordGvDeno + " - " + bodyRecordGvDenoDesc;
                 strDescription2 = "(" + fromGvNo + " ~ " + toGvNo + ")";
                 rsMap.put("ITEM_CODE", strItemCode);
                 rsMap.put("DESCRIPTION1", strDescription1);
                 rsMap.put("DESCRIPTION2", strDescription2);
                 rsMap.put("QTY", strQuantity);
                 rsMap.put("TOTAL_POINTS", rsHdr.getString("TOT_POINTS"));
                 rsMap.put("AMOUNT", rsHdr.getString("TOT_AMOUNT"));
                 resultMapList.add(rsMap);
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
             strItemCode = null;
             bodyRecordGvDeno = null;
             bodyRecordGvDenoDesc = null;
             strQuantity = null;
             fromGvNo = null;
             toGvNo = null;
             strDescription1 = null;
             strDescription2 = null;
         }
     }

     private void getBodyRecordReceiptType_3(HParam hParam, int i) throws Exception {
         String strItemCode = "";
         String bodyRecordGvDeno = "";
         String bodyRecordGvDenoDesc = "";
         String strQuantity = "";
         String fromGvNo = "";
         String toGvNo = "";
         String strDescription1 = "";
         String strDescription2 = "";
         try {
             sql_query =
                 " SELECT G.GV_TYPE, T.GV_TYPE_DESC, G.GV_DENOMINATION, D.GV_DENO_DESC, " + " D.GITEM_SHORT_SKU, " + //CLS Integration
                 " G.FR_GV_NO, G.TO_GV_NO, D.AMOUNT, G.TOT_GV_NO, G.TOT_AMOUNT, " +
                 " A.POINTS, A.TOT_POINTS, GI.GIFT_ITEM_DESC " + " FROM " + SYSProfitSchema + ".GVLOGBOOK G, " +
                 SYSProfitSchema + ".GVDENOMST D, " + " " + SYSProfitSchema + ".GVTYPEMST T, " + SYSProfitSchema +
                 ".GVTRANS A, " + SYSCLSSchema + ".GITEMMST GI " + " WHERE G.PRC_TYPE = 'I' " +
                 " AND D.GV_TYPE = G.GV_TYPE " + " AND D.GV_DENOMINATION = G.GV_DENOMINATION " + " AND D.COY = G.COY " +
                 " AND D.COY_SUB = G.COY_SUB " + " AND G.GV_TYPE = T.GV_TYPE " + " AND G.COY = T.COY " +
                 " AND G.COY_SUB = T.COY_SUB " + " AND G.COY = A.COY " + " AND G.COY_SUB = A.COY_SUB " +
                 " AND G.TRANS_NO = A.TRANS_NO " + " AND G.PRC_TYPE = A.TRANS_TYPE " + " AND G.PRC_ID = ? " +
                 " AND D.COY = ? " + " AND D.COY_SUB = ? " + " AND G.STORE = ? " +
                 " AND GI.SHORT_SKU = D.GITEM_SHORT_SKU " + " AND GI.STORE = 'XXXX' " + " AND GI.COY = D.COY " +
                 " AND GI.COY_SUB = D.COY_SUB " + " AND GI.GIFT_TYPE = (SELECT VNM_VDTVL FROM " + SYSCLSSchema +
                 ".PROFITVV where COY = GI.COY AND VNM = 'SYSCLSGvGiftType') " +
                 " AND G.PRC_DATE BETWEEN GI.EFF_DATE AND GI.END_DATE ";
             if (resultMap.get("MBR_CARDTYP") != null && !resultMap.get("MBR_CARDTYP").equals("")) {
                 sql_query += " AND GI.MBR_CARD_TYPE = '" + resultMap.get("MBR_CARDTYP") + "' ";
             } else {
                 sql_query += " AND GI.MBR_CARD_TYPE = '" + SYSMbrCardTyp + "' ";
             }
             System.out.println("queryrrr111:" + sql_query);
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB);
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             while (rsHdr != null && rsHdr.next()) {
                 rsMap = new HashMap();
                 //mega 20160215 START
                 //strItemCode = getStrItemCode(rsHdr.getString("GV_TYPE"), rsHdr.getString("GV_TYPE_DESC")); //before CLS Integration
                 //strItemCode = getItemDesc(rsHdr.getString("GITEM_SHORT_SKU")); //after CLS Integration
                 strItemCode = AltDescUtil.getDesc(user_lang, rsHdr.getString("GIFT_ITEM_DESC"));
                 //mega 20160215 END
                 if (rsHdr.getString("TOT_GV_NO") != null && !rsHdr.getString("TOT_GV_NO").equals("")) {
                     strQuantity = rsHdr.getString("TOT_GV_NO");
                 } else {
                     strQuantity = "";
                 }
                 if (rsHdr.getString("GV_DENOMINATION") != null && !rsHdr.getString("GV_DENOMINATION").equals("")) {
                     bodyRecordGvDeno = rsHdr.getString("GV_DENOMINATION");
                 } else {
                     bodyRecordGvDeno = "";
                 }
                 if (rsHdr.getString("GV_DENO_DESC") != null && !rsHdr.getString("GV_DENO_DESC").equals("")) {
                     bodyRecordGvDenoDesc = rsHdr.getString("GV_DENO_DESC");
                 } else {
                     bodyRecordGvDenoDesc = "";
                 }
                 if (rsHdr.getString("FR_GV_NO") != null && !rsHdr.getString("FR_GV_NO").equals("")) {
                     fromGvNo = rsHdr.getString("FR_GV_NO");
                 } else {
                     fromGvNo = "";
                 }
                 if (rsHdr.getString("TO_GV_NO") != null && !rsHdr.getString("TO_GV_NO").equals("")) {
                     toGvNo = rsHdr.getString("TO_GV_NO");
                 } else {
                     toGvNo = "";
                 }
                 strDescription1 = bodyRecordGvDeno + " - " + bodyRecordGvDenoDesc;
                 if (strQuantity.equals("1")) {
                     strDescription2 = "{" + fromGvNo + "}";
                 } else {
                     strDescription2 = "{" + fromGvNo + " ~ " + toGvNo + "}";
                 }
                 rsMap.put("ITEM_CODE", strItemCode);
                 rsMap.put("DESCRIPTION1", strDescription1);
                 rsMap.put("DESCRIPTION2", strDescription2);
                 rsMap.put("QTY", strQuantity);
                 rsMap.put("AMOUNT", rsHdr.getString("TOT_AMOUNT"));
                 resultMapList.add(rsMap);
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
             strItemCode = null;
             bodyRecordGvDeno = null;
             bodyRecordGvDenoDesc = null;
             strQuantity = null;
             fromGvNo = null;
             toGvNo = null;
             strDescription1 = null;
             strDescription2 = null;
         }
     }

     private String getStrItemCode(String _gvType, String _gvTypeDesc) throws Exception {
         String bodyRecordGvType = (_gvType != null && !_gvType.equals("")) ? _gvType : "";
         String bodyRecordGvTypeDesc = (_gvTypeDesc != null && !_gvTypeDesc.equals("")) ? _gvTypeDesc : "";
         String strItemCode = bodyRecordGvType + " - " + bodyRecordGvTypeDesc;
         return strItemCode;
     }

     private void getTotalAmountReceiptType_1(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT Sum(G.TOT_AMOUNT) as TOT_AMT, SUM(G.DISC_AMT) AS TOTAL_DISC_AMT " + 
                                 " FROM " + SYSProfitSchema + ".GVLOGBOOK G, " +
                 SYSProfitSchema + ".GVDENOMST D " + " WHERE G.PRC_TYPE = 'I' " + " AND D.GV_TYPE = G.GV_TYPE " +
                 " AND D.GV_DENOMINATION = G.GV_DENOMINATION " + " AND D.COY = G.COY " + " AND D.COY_SUB = G.COY_SUB " +
                 " AND G.PRC_ID = ? " + " AND D.COY = ? " + " AND D.COY_SUB = ? " + " AND G.STORE = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB);
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             if (rsHdr != null && rsHdr.next()) {
                resultMap.put("TOTAL_AMOUNT", rsHdr.getString("TOT_AMT"));
                resultMap.put("TOTAL_DISCOUNT_AMOUNT", rsHdr.getString("TOTAL_DISC_AMT"));
            }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
     }

     private void getTotalAmountReceiptType_2_3(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT SUM(G.TOT_GV_NO) AS ALL_TOT_GV_NO, SUM(G.TOT_AMOUNT) AS ALL_TOT_AMOUNT, " +
                 " SUM(A.POINTS) AS ALL_POINTS, SUM(A.TOT_POINTS) AS ALL_TOT_POINTS " + " FROM " + SYSProfitSchema +
                 ".GVLOGBOOK G, " + SYSProfitSchema + ".GVDENOMST D, " + " " + SYSProfitSchema + ".GVTYPEMST T, " +
                 SYSProfitSchema + ".GVTRANS A " + " WHERE G.PRC_TYPE = 'I' " + " AND D.GV_TYPE = G.GV_TYPE " +
                 " AND D.GV_DENOMINATION = G.GV_DENOMINATION " + " AND D.COY = G.COY " + " AND D.COY_SUB = G.COY_SUB " +
                 " AND G.GV_TYPE = T.GV_TYPE " + " AND G.COY = T.COY " + " AND G.COY_SUB = T.COY_SUB " +
                 " AND G.COY = A.COY " + " AND G.COY_SUB = A.COY_SUB " + " AND G.TRANS_NO = A.TRANS_NO " +
                 " AND G.PRC_TYPE = A.TRANS_TYPE " + " AND G.PRC_ID = ? " + " AND D.COY = ? " + " AND D.COY_SUB = ? " +
                 " AND G.STORE = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB);
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             if (rsHdr != null && rsHdr.next()) {
                 resultMap.put("ALL_TOT_GV_NO", rsHdr.getString("ALL_TOT_GV_NO"));
                 resultMap.put("ALL_TOT_POINTS", rsHdr.getString("ALL_TOT_POINTS"));
                 resultMap.put("ALL_TOTAL_AMOUNT", rsHdr.getString("ALL_TOT_AMOUNT"));
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
     }

     private void getHeaderCancelRecord(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT DISTINCT H.COY, H.COY_SUB, H.TRANS_DATE, H.TRANS_OPR " + " FROM " + SYSProfitSchema +
                 ".GVTRANS H " + " WHERE H.TRANS_TYPE = 'L' " + " AND H.TRANS_NO IN (SELECT G.TRANS_NO FROM " +
                 SYSProfitSchema + ".GVLOGBOOK G WHERE G.PRC_TYPE = 'L' AND G.PRC_ID = ?) " + " AND H.COY = ? " +
                 " AND H.COY_SUB = ? " + " AND H.STORE = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB);
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             if (rsHdr != null && rsHdr.next()) {
                 AduserInfo aduserInfo = new AduserInfo();
                 aduserInfo = getAduserInfo(rsHdr.getString("TRANS_OPR"));
                 String strCancelBy = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
                 resultMap.put("TRANSACTION_NO", hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
                 resultMap.put("CANCEL_BY", strCancelBy);
                 resultMap.put("CANCEL_DATE", rsHdr.getString("TRANS_DATE"));
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
     }

     private void getBodyCancelRecord(HParam hParam, int i) throws Exception {
         try {
             sql_query =
                 " SELECT G.STORE, G.GV_TYPE, G.GV_DENOMINATION, G.FR_GV_NO, " +
                 " G.TO_GV_NO, D.AMOUNT, G.TOT_GV_NO, G.TOT_AMOUNT, T.REMARK " + " FROM " + SYSProfitSchema +
                 ".GVLOGBOOK G, " + SYSProfitSchema + ".GVDENOMST D, " + " " + SYSProfitSchema + ".GVTRANS T " +
                 " WHERE G.PRC_TYPE = 'L' " + " AND D.GV_TYPE = G.GV_TYPE " +
                 " AND D.GV_DENOMINATION = G.GV_DENOMINATION " + " AND D.COY = G.COY " + " AND D.COY_SUB = G.COY_SUB " +
                 " AND T.COY = G.COY " + " AND T.COY_SUB = G.COY_SUB " + " AND T.TRANS_NO = G.TRANS_NO " +
                 " AND T.TRANS_TYPE = G.PRC_TYPE " + " AND G.PRC_ID = ? " + " AND D.COY = ? " + " AND D.COY_SUB = ? " +
                 " AND G.STORE = ? ";
             pstmtHdr = conn.prepareStatement(sql_query);
             pstmtHdr.setString(1, hParam.getString("PRC_ID_" + String.valueOf(i)).trim());
             pstmtHdr.setString(2, COY);
             pstmtHdr.setString(3, COY_SUB);
             pstmtHdr.setString(4, hParam.getString("STORE_" + String.valueOf(i)).trim());
             rsHdr = pstmtHdr.executeQuery();
             while (rsHdr != null && rsHdr.next()) {
                 rsMap = new HashMap();
                 String strSTORE =
                     rsHdr.getString("STORE") != null && !((String) rsHdr.getString("STORE")).equals("") ?
                     (String) rsHdr.getString("STORE") : "";
                 String gvType =
                     rsHdr.getString("GV_TYPE") != null && !((String) rsHdr.getString("GV_TYPE")).equals("") ?
                     (String) rsHdr.getString("GV_TYPE") : "";
                 String gvDenomination =
                     rsHdr.getString("GV_DENOMINATION") != null &&
                     !((String) rsHdr.getString("GV_DENOMINATION")).equals("") ?
                     (String) rsHdr.getString("GV_DENOMINATION") : "";
                 String strFrGvNo =
                     rsHdr.getString("FR_GV_NO") != null && !((String) rsHdr.getString("FR_GV_NO")).equals("") ?
                     (String) rsHdr.getString("FR_GV_NO") : "";
                 String strToGvNo =
                     rsHdr.getString("TO_GV_NO") != null && !((String) rsHdr.getString("TO_GV_NO")).equals("") ?
                     (String) rsHdr.getString("TO_GV_NO") : "";
                 String strTotGvNo =
                     rsHdr.getString("TOT_GV_NO") != null && !((String) rsHdr.getString("TOT_GV_NO")).equals("") ?
                     (String) rsHdr.getString("TOT_GV_NO") : "0";
                 String strTotAmount =
                     rsHdr.getString("TOT_AMOUNT") != null && !((String) rsHdr.getString("TOT_AMOUNT")).equals("") ?
                     (String) rsHdr.getString("TOT_AMOUNT") : "0";
                 String strRemark =
                     rsHdr.getString("REMARK") != null && !((String) rsHdr.getString("REMARK")).equals("") ?
                     (String) rsHdr.getString("REMARK") : "";
                 rsMap.put("STORE", strSTORE);
                 rsMap.put("GV_TYPE", gvType);
                 rsMap.put("GV_TYPE_DESC", getVoucherTypeDescription(rsHdr.getString("GV_TYPE")));
                 rsMap.put("GV_DENOMINATION", gvDenomination);
                 rsMap.put("GV_DENO_DESC", getVoucherDenoDescription(gvType, gvDenomination));
                 rsMap.put("FR_GV_NO", strFrGvNo);
                 rsMap.put("TO_GV_NO", strToGvNo);
                 rsMap.put("QUANTITY", strTotGvNo);
                 rsMap.put("AMOUNT", strTotAmount);
                 rsMap.put("REMARK", strRemark);
                 resultMapList.add(rsMap);
             }
         } catch (Exception e) {
             throw (e);
         } finally {
             try {
                 if (rsHdr != null)
                     rsHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             rsHdr = null;
             try {
                 if (pstmtHdr != null)
                     pstmtHdr.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
             pstmtHdr = null;
         }
     }

     private String getVoucherTypeDescription(String gvType) throws Exception {
         String returnValue = "";
         gvtypemstSQL.setVObject(new GvtypemstInfoAMY().getVObject());
         gvtypemstSQL.setCOY(COY);
         gvtypemstSQL.setCOY_SUB(COY_SUB);
         gvtypemstSQL.setGV_TYPE(gvType);
         gvtypemstSQL.getByKey();
         if (gvtypemstSQL.GV_TYPE_DESC() != null && !gvtypemstSQL.GV_TYPE_DESC().equals("")) {
             returnValue = gvtypemstSQL.GV_TYPE_DESC();
         }
         return returnValue;
     }

     private String getVoucherDenoDescription(String gvType, String gvDenom) throws Exception {
         String returnValue = "";
         gvdenomstSQL.setVObject(new GvdenomstInfoAMY().getVObject());
         gvdenomstSQL.setCOY(COY);
         gvdenomstSQL.setCOY_SUB(COY_SUB);
         gvdenomstSQL.setGV_TYPE(gvType);
         gvdenomstSQL.setGV_DENOMINATION(gvDenom);
         gvdenomstSQL.getByKey();
         if (gvdenomstSQL.GV_DENO_DESC() != null && !gvdenomstSQL.GV_DENO_DESC().equals("")) {
             returnValue = gvdenomstSQL.GV_DENO_DESC();
         }
         return returnValue;
     }

     private String getStoreName(String storeCd) throws Exception {
         String returnValue = "";
         strmstSQL.setVObject(new StrmstInfo().getVObject());
         strmstSQL.setSTORE(storeCd);
         strmstSQL.getByKey();
         System.out.println("strmstSQL.STORE_NAME() = " + strmstSQL.STORE_NAME());
         if (strmstSQL.STORE_NAME() != null && !strmstSQL.STORE_NAME().equals("")) {
             returnValue = AltDescUtil.getDesc(user_lang, strmstSQL.STORE_NAME());
         }
         return returnValue;
     }

     private String getPROFITVV(String strCOY, String strVNM) throws Exception {
         profitvvSQL.setCOY(strCOY);
         profitvvSQL.setVNM(strVNM);
         profitvvSQL.getByKey();
         return profitvvSQL.VNM_VDTVL();
     }

     private void printNewPage() throws Exception {
         document.newPage();
     }

     private PdfPTable createTableSuperSuperHdr() throws BadElementException, DocumentException, Exception, Exception {
         PdfPTable tableSuperSuperHdr = new PdfPTable(2);
         int headerwidths[] = { 38, 62 };
         tableSuperSuperHdr.setWidths(headerwidths);
         tableSuperSuperHdr.setWidthPercentage(100);
         tableSuperSuperHdr.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(getLeftSideTableSuperSuperHdr());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setPaddingBottom(3f);
         tableSuperSuperHdr.addCell(cell);

         cell = new PdfPCell(getRightSideTableSuperSuperHdr());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setPaddingBottom(3f);
         tableSuperSuperHdr.addCell(cell);
         return tableSuperSuperHdr;
     }

     private PdfPTable createTableSuperHdr() throws BadElementException, DocumentException, Exception, Exception {
         PdfPTable tableSuperHdr = new PdfPTable(7);
         int headerwidths[] = { 20, 1, 34, 12, 14, 1, 18 };
         tableSuperHdr.setWidths(headerwidths);
         tableSuperHdr.setWidthPercentage(100);
         tableSuperHdr.setHorizontalAlignment(Element.ALIGN_CENTER);
         String strPurchaser = "";
         String strContactNo = "";
         String strTotalAmount = "";
         String strIssueType = "";
         java.util.Date dtDate =
             resultMap.get("DATE") != null && !((String) resultMap.get("DATE")).equals("") ?
             qrMisc.parseDate((String) resultMap.get("DATE"), "yyyy-MM-dd") : null;
         String strDate = dtDate != null ? fmt.format(dtDate) : "";
//         if (resultMap.get("PURCHASER") != null && !((String) resultMap.get("PURCHASER")).equals("")) {
//             strPurchaser = getDescription((String) resultMap.get("PURCHASER"));
//         }
         if (resultMap.get("CONTACT_NO") != null && !((String) resultMap.get("CONTACT_NO")).equals("")) {
             strContactNo = getDescription((String) resultMap.get("CONTACT_NO"));
         }
         
         double totalDiscountAmount = 0;
         double totalAmount = 0;
         if (resultMap.get("TOTAL_AMOUNT") != null && !((String) resultMap.get("TOTAL_AMOUNT")).equals("")) {
             totalAmount = Double.parseDouble((String) resultMap.get("TOTAL_AMOUNT"));
         }
         if (resultMap.get("TOTAL_DISCOUNT_AMOUNT") != null &&
            !((String) resultMap.get("TOTAL_DISCOUNT_AMOUNT")).equals("")) {
            totalDiscountAmount = Double.parseDouble((String) resultMap.get("TOTAL_DISCOUNT_AMOUNT"));
         }
         
             
         if (resultMap.get("TOTAL_AMOUNT") != null && !((String) resultMap.get("TOTAL_AMOUNT")).equals("")) {
             strTotalAmount =
                 adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "RM") + " " + new DecimalFormat("#0.00").format(totalAmount - totalDiscountAmount);
         }
         if (resultMap.get("ISSUE_TYPE") != null && !((String) resultMap.get("ISSUE_TYPE")).equals("")) {
             strIssueType = getDescription((String) resultMap.get("ISSUE_TYPE"));
         }
         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,"PURCHASER"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strPurchaser, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "CONTACT NO"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strContactNo, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);
         /* ----------------------------------------------------------------------------------------------------------- */
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "COY_CURRENCY"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strTotalAmount, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "DATE"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strDate, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);
         /* ----------------------------------------------------------------------------------------------------------- */
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "ISSUE TYPE"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strIssueType, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         tableSuperHdr.addCell(cell);
         /* ----------------------------------------------------------------------------------------------------------- */
         return tableSuperHdr;
     }

     private PdfPTable createTableHdr() throws BadElementException, DocumentException, Exception, Exception {
         PdfPTable tableHdr = new PdfPTable(5);
         int headerwidths[] = { 38, 16, 8, 8, 30 };
         tableHdr.setWidthPercentage(100);
         tableHdr.setWidths(headerwidths);
         tableHdr.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "DESCRIPTION"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "UNIT PRICE (RM)"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "QTY"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         tableHdr.addCell(cell);
                 
                 cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "DISC") + " (%)", FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "AMOUNT (RM)"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         tableHdr.addCell(cell);
         return tableHdr;
     }  

     private void printDataTable() throws BadElementException, DocumentException, Exception {
         PdfPTable datatable = new PdfPTable(5);
         int headerwidths[] = { 38, 16, 8, 8, 30 };
         datatable.setWidthPercentage(100);
         datatable.setWidths(headerwidths);
         datatable.getDefaultCell().setUseAscender(true);
         datatable.getDefaultCell().setUseDescender(true);
         String strDescription = "";
         String strUnitPrice = "";
         String strQty = "";
         String strAmount = "";
         if (reportBodyRecord.get("DESCRIPTION") != null && !((String) reportBodyRecord.get("DESCRIPTION")).equals("")) {
             strDescription = (String) reportBodyRecord.get("DESCRIPTION");
         }
         if (reportBodyRecord.get("UNIT_PRICE") != null && !((String) reportBodyRecord.get("UNIT_PRICE")).equals("")) {
             strUnitPrice =
                 new DecimalFormat("#0.00").format(Double.parseDouble((String) reportBodyRecord.get("UNIT_PRICE")));
         } else {
             strUnitPrice = "0.00";
         }
         if (reportBodyRecord.get("QTY") != null && !((String) reportBodyRecord.get("QTY")).equals("")) {
             strQty = (String) reportBodyRecord.get("QTY");
         } else {
             strQty = "0";
         }
         if (reportBodyRecord.get("AMOUNT") != null && !((String) reportBodyRecord.get("AMOUNT")).equals("")) {
             double dblAmount = Double.parseDouble((String) reportBodyRecord.get("AMOUNT"));
             strAmount = new DecimalFormat("#0.00").format(dblAmount);
             currentPageTotalAmount += dblAmount;
         } else {
             strAmount = "0.00";
         }
         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(strDescription, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(13f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strUnitPrice, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(13f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strQty, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         datatable.addCell(cell);
                 
                 cell = new PdfPCell(new Phrase("0.00", FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strAmount, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(13f);
         datatable.addCell(cell);
         document.add(datatable);
     }

     private void printDataTable_2() throws BadElementException, DocumentException, Exception {
         PdfPTable datatable = new PdfPTable(6);
         int headerwidths[] = { 5, 22, 46, 7, 8, 12 };
         datatable.setWidths(headerwidths);
         datatable.setWidthPercentage(100);
         datatable.getDefaultCell().setUseAscender(true);
         datatable.getDefaultCell().setUseDescender(true);
         String strItemCode = "";
         String strDescription1 = "";
         String strDescription2 = "";
         String strPoints = "";
         String strQty = "";
         String strTotPoints = "";
         String strAmount = "";
         double dblAmount = 0.00;
         int intPoints = 0;
         int intQty = 0;
         if (reportBodyRecord.get("ITEM_CODE") != null && !((String) reportBodyRecord.get("ITEM_CODE")).equals("")) {
             strItemCode = (String) reportBodyRecord.get("ITEM_CODE");
         }
         if (reportBodyRecord.get("DESCRIPTION1") != null &&
             !((String) reportBodyRecord.get("DESCRIPTION1")).equals("")) {
             strDescription1 = (String) reportBodyRecord.get("DESCRIPTION1");
         }
         if (reportBodyRecord.get("DESCRIPTION2") != null &&
             !((String) reportBodyRecord.get("DESCRIPTION2")).equals("")) {
             strDescription2 = (String) reportBodyRecord.get("DESCRIPTION2");
         }
         if (reportBodyRecord.get("POINTS") != null && !((String) reportBodyRecord.get("POINTS")).equals("")) {
             strPoints = (String) reportBodyRecord.get("POINTS");
         } else {
             strPoints = "0";
         }
         if (reportBodyRecord.get("QTY") != null && !((String) reportBodyRecord.get("QTY")).equals("")) {
             strQty = (String) reportBodyRecord.get("QTY");
             intQty = Integer.parseInt((String) reportBodyRecord.get("QTY"));
             currentPageTotalQuantity += intQty;
         } else {
             strQty = "0";
         }
         if (reportBodyRecord.get("TOTAL_POINTS") != null &&
             !((String) reportBodyRecord.get("TOTAL_POINTS")).equals("")) {
             strTotPoints = (String) reportBodyRecord.get("TOTAL_POINTS");
             intPoints = Integer.parseInt((String) reportBodyRecord.get("TOTAL_POINTS"));
             currentPageTotalPoints += intPoints;
         } else {
             strTotPoints = "0";
         }
         if (reportBodyRecord.get("AMOUNT") != null && !((String) reportBodyRecord.get("AMOUNT")).equals("")) {
             dblAmount = Double.parseDouble((String) reportBodyRecord.get("AMOUNT"));
             strAmount = new DecimalFormat("#0.00").format(dblAmount);
             currentPageTotalAmount += dblAmount;
         } else {
             strAmount = "0.00";
         }
         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(String.valueOf(currentItemNoWOReset_1), FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strItemCode, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strDescription1 + " " + strDescription2, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strQty, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strTotPoints, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strAmount, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         document.add(datatable);
         strItemCode = null;
         strDescription1 = null;
         strDescription2 = null;
         strPoints = null;
         strQty = null;
         strTotPoints = null;
         strAmount = null;
     }

     private void printDataTable_3() throws BadElementException, DocumentException, Exception {
         PdfPTable datatable = new PdfPTable(5);
         int headerwidths[] = { 5, 24, 47, 12, 12 };
         datatable.setWidths(headerwidths);
         datatable.setWidthPercentage(100);
         datatable.getDefaultCell().setUseAscender(true);
         datatable.getDefaultCell().setUseDescender(true);
         String strItemCode = "";
         String strDescription1 = "";
         String strDescription2 = "";
         String strQty = "";
         String strAmount = "";
         double dblAmount = 0.00;
         int intQty = 0;
         if (reportBodyRecord.get("ITEM_CODE") != null && !((String) reportBodyRecord.get("ITEM_CODE")).equals("")) {
             strItemCode = (String) reportBodyRecord.get("ITEM_CODE");
         }
         if (reportBodyRecord.get("DESCRIPTION1") != null &&
             !((String) reportBodyRecord.get("DESCRIPTION1")).equals("")) {
             strDescription1 = (String) reportBodyRecord.get("DESCRIPTION1");
         }
         if (reportBodyRecord.get("DESCRIPTION2") != null &&
             !((String) reportBodyRecord.get("DESCRIPTION2")).equals("")) {
             strDescription2 = (String) reportBodyRecord.get("DESCRIPTION2");
         }
         if (reportBodyRecord.get("QTY") != null && !((String) reportBodyRecord.get("QTY")).equals("")) {
             strQty = (String) reportBodyRecord.get("QTY");
             intQty = Integer.parseInt((String) reportBodyRecord.get("QTY"));
             currentPageTotalQuantity += intQty;
         } else {
             strQty = "0";
         }
         if (reportBodyRecord.get("AMOUNT") != null && !((String) reportBodyRecord.get("AMOUNT")).equals("")) {
             dblAmount = Double.parseDouble((String) reportBodyRecord.get("AMOUNT"));
             strAmount = new DecimalFormat("#0.00").format(dblAmount);
             currentPageTotalAmount += dblAmount;
         } else {
             strAmount = "0.00";
         }
         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(String.valueOf(currentItemNoWOReset_1), FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strItemCode, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strDescription1 + " " + strDescription2, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strQty, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         cell = new PdfPCell(new Phrase(strAmount, FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(0.5f);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(10f);
         datatable.addCell(cell);

         document.add(datatable);
         strItemCode = null;
         strDescription1 = null;
         strDescription2 = null;
         strQty = null;
         strAmount = null;
     }

     private PdfPTable getLeftSideTableSuperSuperHdr() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(1);
         tbl.setWidthPercentage(100);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,SYSCompanyName) + " " + SYSCompanyRegNo, FontChinese_tb_title)); //Company Name
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strHEADOFFICE), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strADDRESS_1), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strADDRESS_2), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strADDRESS_3), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strADDRESS_4), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         tbl.addCell(cell);
         return tbl;
     }

     private PdfPTable getRightSideTableSuperSuperHdr() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(2);
         int headerwidths[] = { 36, 64 };
         tbl.setWidths(headerwidths);
         tbl.setWidthPercentage(100);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);
         if (company_logo_image == null) {
             try {
                 company_logo_image = Image.getInstance(coy_logo_image_file);
                 company_logo_image.scalePercent(60);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         PdfPCell cell = null;
         cell = new PdfPCell(getTopLeftSideTableSuperSuperHdr());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         cell = new PdfPCell(getTopRightSideTableSuperSuperHdr());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         if (this.isFoundInStr(SYSGVSelRefGVTYPE, gvType) && this.isFoundInStr(SysIssueSelRefRsnCd, rsnCd)) {
             //it is PROREF
             cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_COLLECTION_RECEIPT_TYPE1"), FontChinese_title));
         } else {
             //the rest
             cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_COLLECTION_RECEIPT_TYPE2"), FontChinese_title));
         }

         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         cell.setColspan(2);
         tbl.addCell(cell);
         return tbl;
     }

     private PdfPTable getTopLeftSideTableSuperSuperHdr() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(1);
         int headerwidths[] = { 100 };
         tbl.setWidths(headerwidths);
         tbl.setWidthPercentage(100);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         if (company_logo_image != null) {
             cell = new PdfPCell(company_logo_image);
         } else {
             cell = new PdfPCell(new Phrase(""));
         }
         cell.disableBorderSide(Rectangle.BOX);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPaddingLeft(35f);
         cell.setPaddingTop(12f);
         tbl.addCell(cell);

         if (reprint.equals("true")) {
             cell = new PdfPCell(new Phrase("             " +
                                            adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "(Reprint Copy Only)"),
                                            FontChinese_tb_title));
             cell.disableBorderSide(Rectangle.BOX);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setVerticalAlignment(Element.ALIGN_TOP);
             tbl.addCell(cell);
         }
         return tbl;
     }

     private PdfPTable getTopRightSideTableSuperSuperHdr() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(1);
         int headerwidths[] = { 100 };
         tbl.setWidths(headerwidths);
         tbl.setWidthPercentage(100);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);
         PdfPCell cell = null;
         String strNO = "";
         String strWhichCopy = "";
         AduserInfo aduserInfo = new AduserInfo();
         aduserInfo = getAduserInfo(USER_ID);

         if (resultMap.get("NO") != null && !((String) resultMap.get("NO")).equals("")) {
             strNO = getDescription((String) resultMap.get("NO"));
         }
         if (resultMap.get("WHICH_COPY") != null && !(((String) resultMap.get("WHICH_COPY")).equals(""))) {
             strWhichCopy = getDescription((String) resultMap.get("WHICH_COPY"));
         }
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "NO") + ": " + strNO, FontChinese_tb_title));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strWhichCopy, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         if (reprint.equals("true")) {
             cell = new PdfPCell(getReprintedBy1());
             cell.disableBorderSide(Rectangle.BOX);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setVerticalAlignment(Element.ALIGN_TOP);
             tbl.addCell(cell);
         }
         return tbl;
     }

     private PdfPTable getReprintedBy1() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(2);
         int headerwidths[] = { 29, 71 };
         tbl.setWidths(headerwidths);
         tbl.setWidthPercentage(100);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);
         PdfPCell cell = null;
         AduserInfo aduserInfo = new AduserInfo();
         aduserInfo = getAduserInfo(USER_ID);
         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         String reprintedBy = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
         if (reprintedBy.length() > 22) {
             reprintedBy = reprintedBy.substring(0, 22);
         }
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Reprinted by") + ": " + reprintedBy, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Reprinted ID") + ": " + USER_ID, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);
         return tbl;
     }

     private PdfPTable getReprintedBy2() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(1);
         int headerwidths[] = { 100 };
         tbl.setWidths(headerwidths);
         tbl.setWidthPercentage(100);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);
         PdfPCell cell = null;
         AduserInfo aduserInfo = new AduserInfo();
         aduserInfo = getAduserInfo(USER_ID);
         String reprintedBy = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
         if (reprintedBy.length() > 18) {
             reprintedBy = reprintedBy.substring(0, 18);
         }
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Reprinted by") + ": " + reprintedBy, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Reprinted ID") + ": " + USER_ID, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);
         return tbl;
     }

     private PdfPTable getReprintedBy3() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(1);
         int headerwidths[] = { 100 };
         tbl.setWidths(headerwidths);
         tbl.setWidthPercentage(100);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);
         PdfPCell cell = null;
         AduserInfo aduserInfo = new AduserInfo();
         aduserInfo = getAduserInfo(USER_ID);

         String reprintedBy = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
         if (reprintedBy.length() > 18) {
             reprintedBy = reprintedBy.substring(0, 18);
         }
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Reprinted by") + ": " + reprintedBy, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Reprinted ID") + ": " + USER_ID, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         tbl.addCell(cell);
         return tbl;
     }

     private PdfPTable createTableSuperSuperHdr_2() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(3);
         int headerwidths[] = { 41, 34, 25 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(getLeftSideTableSuperSuperHdr_2());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setPaddingBottom(2f);
         table.addCell(cell);

         cell = new PdfPCell(getCenterTableSuperSuperHdr_2());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPaddingTop(18f);
         cell.setPaddingBottom(2f);
         table.addCell(cell);

         cell = new PdfPCell(getRightSideTableSuperSuperHdr_2());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPaddingLeft(3f);
         if (!reprint.equals("true"))
             cell.setPaddingBottom(68f);
         else
             cell.setPaddingBottom(48f);
         table.addCell(cell);
         return table;
     }

     private PdfPTable createTableSuperSuperHdr_3() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(3);
         int headerwidths[] = { 50, 26, 24 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);

         if (company_logo_image == null) {
             try {
                 company_logo_image = Image.getInstance(coy_logo_image_file);
                 company_logo_image.scalePercent(40);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         PdfPCell cell = null;
         if (company_logo_image != null) {
             cell = new PdfPCell(company_logo_image);
         } else {
             cell = new PdfPCell(new Phrase(""));
         }
         cell.disableBorderSide(Rectangle.BOX);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         if (!reprint.equals("true"))
             cell.setColspan(2);
         table.addCell(cell);

         if (reprint.equals("true")) {
             cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,"(Reprint Copy Only)"), FontChinese_tb_title));
             cell.disableBorderSide(Rectangle.BOX);
             cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
             cell.setVerticalAlignment(Element.ALIGN_TOP);
             table.addCell(cell);
         }
         cell = new PdfPCell(getRightSideTableSuperSuperHdr_3());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         table.addCell(cell);
         //-------------------------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "MBR_RB_REDEMPTION_FORM"), FontChinese_title2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setFixedHeight(23f);
         if (reprint.equals("true"))
             cell.setColspan(2);
         else
             cell.setColspan(3);
         table.addCell(cell);
         if (reprint.equals("true")) {
             cell = new PdfPCell(getReprintedBy3());
             cell.disableBorderSide(Rectangle.BOX);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setVerticalAlignment(Element.ALIGN_TOP);
             cell.setFixedHeight(23f);
             table.addCell(cell);
         }
         //-------------------------------------------------------------------------------------------------------//
         cell = new PdfPCell(getSubTableFromTableSuperSuperHdr_3());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setColspan(3);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "(Please tick (v) the collection period)"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setColspan(3);
         table.addCell(cell);
         return table;
     }

     private PdfPTable createTableSuperHdr_2() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(7);
         int headerwidths[] = { 16, 1, 35, 22, 5, 1, 20 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);
         String strName =
             resultMap.get("NAME") != null && !((String) resultMap.get("NAME")).equals("") ?
             (String) resultMap.get("NAME") : "";
         java.util.Date dtDate =
             resultMap.get("DATE") != null && !((String) resultMap.get("DATE")).equals("") ?
             qrMisc.parseDate((String) resultMap.get("DATE"), "yyyy-MM-dd") : null;
         String strDate = dtDate != null ? fmt.format(dtDate) : "";
         String strMemberNo =
             resultMap.get("MEMBERSHIP_NO") != null && !((String) resultMap.get("MEMBERSHIP_NO")).equals("") ?
             (String) resultMap.get("MEMBERSHIP_NO") : "";

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Name"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(strName, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Date"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(strDate, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);
         //*****************************************************************************************************************//
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Membership No"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(strMemberNo, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setColspan(4);
         table.addCell(cell);
         return table;
     }

     private PdfPTable createTableSuperHdr_3() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(9);
         int headerwidths[] = { 17, 1, 13, 4, 15, 8, 17, 1, 24 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);
         table.setTableEvent(tableBackground);
         table.getDefaultCell().setUseAscender(true);
         table.getDefaultCell().setUseDescender(true);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setFixedHeight(13f);
         cell.setColspan(9);
         table.addCell(cell);
         //----------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Name"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("NAME"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         cell.setColspan(3);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Membership No"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("MEMBERSHIP_NO"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);
         //----------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "IC / Passport No"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("NRIC_NO"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         cell.setColspan(3);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         cell.setColspan(4);
         table.addCell(cell);
         //----------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Telephone No"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "(Mobile)"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         cell.setColspan(2);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("TELEPHONE_NO"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Email Address"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("MBR_EMAIL"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);
         //----------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_RB_REDEMPTION_ACKNOWLEDGEMENT_FOOTER1"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         cell.setColspan(3);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "RM"), FontChinese_tb_title_small));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("ALL_TOTAL_AMOUNT"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(13f);
         cell.setColspan(4);
         table.addCell(cell);
         //----------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setFixedHeight(13f);
         cell.setColspan(9);
         table.addCell(cell);
         return table;
     }

     private PdfPTable createTableHdr_2() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(6);
         int headerwidths[] = { 5, 22, 46, 7, 8, 12 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "NO"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "ITEM CODE"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "DESCRIPTION"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "QTY"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "TOTAL POINTS"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "TOTAL AMOUNT (RM)"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);
         return table;
     }

     private PdfPTable createTableHdr_3() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(5);
         int headerwidths[] = { 5, 24, 47, 12, 12 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "NO"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "ITEM CODE"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "DESCRIPTION"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "QUANTITY"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "AMOUNT (RM)"), FontChinese_tb_title_white));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setBackgroundColor(bgColor);
         table.addCell(cell);
         return table;
     }

     private PdfPTable getLeftSideTableSuperSuperHdr_2() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(1);
         int headerwidths[] = { 100 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);
         if (company_logo_image == null) {
             try {
                 company_logo_image = Image.getInstance(coy_logo_image_file);
                 company_logo_image.scalePercent(60);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         PdfPCell cell = null;
         if (company_logo_image != null) {
             cell = new PdfPCell(company_logo_image);
         } else {
             cell = new PdfPCell(new Phrase(""));
         }
         cell.disableBorderSide(Rectangle.BOX);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);

         cell =
             new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, SYSCompanyName) + " " + SYSCompanyRegNo, FontChinese_tb_title_small)); //Company Name
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(strHEADOFFICE, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strADDRESS_1), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strADDRESS_2), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strADDRESS_3), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strADDRESS_4), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);
         return table;
     }

     private PdfPTable getCenterTableSuperSuperHdr_2() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(1);
         int headerwidths[] = { 100 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "REDEMPTION FORM"), FontChinese_title));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         table.addCell(cell);
         if (reprint.equals("true")) {
             cell = new PdfPCell(new Phrase("           " +
                                            adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "(Reprint Copy Only)"),
                                            FontChinese_tb_title));
             cell.disableBorderSide(Rectangle.BOX);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setVerticalAlignment(Element.ALIGN_TOP);
             table.addCell(cell);
         }
         return table;
     }

     private PdfPTable getRightSideTableSuperSuperHdr_2() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(2);
         int headerwidths[] = { 15, 85 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "No.") + " ", FontChinese_tb_title_small));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("NO"), FontChinese_tb_title));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("WHICH_COPY"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         cell.setColspan(2);
         table.addCell(cell);
         if (reprint.equals("true")) {
             cell = new PdfPCell(getReprintedBy2());
             cell.disableBorderSide(Rectangle.BOX);
             cell.setHorizontalAlignment(Element.ALIGN_LEFT);
             cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
             cell.setColspan(2);
             table.addCell(cell);
         }
         return table;
     }

     private PdfPTable getRightSideTableSuperSuperHdr_3() throws BadElementException, DocumentException, Exception {
         PdfPTable table = new PdfPTable(2);
         int headerwidths[] = { 15, 85 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "No.") + " ", FontChinese_tb_title_small));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("NO"), FontChinese_tb_title));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("WHICH_COPY"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         cell.setColspan(2);
         table.addCell(cell);
         return table;
     }

     private PdfPTable getSubTableFromTableSuperSuperHdr_3() throws Exception {
         PdfPTable table = new PdfPTable(13);
         int headerwidths[] = { 21, 1, 1, 4, 1, 14, 1, 4, 1, 21, 20, 1, 10 };
         table.setWidths(headerwidths);
         table.setWidthPercentage(100);
         table.setHorizontalAlignment(Element.ALIGN_CENTER);
         java.util.Date dtDate =
             resultMap.get("DATE") != null && !((String) resultMap.get("DATE")).equals("") ?
             qrMisc.parseDate((String) resultMap.get("DATE"), "yyyy-MM-dd") : null;
         String strDate = dtDate != null ? fmt.format(dtDate) : "";

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "COLLECTION PERIODS"), FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChinese_tb_title_small2));
         cell.enableBorderSide(Rectangle.BOX);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "MARCH - JUNE"), FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChinese_tb_title_small2));
         cell.enableBorderSide(Rectangle.BOX);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "AUGUST - NOVEMBER"), FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "DATE"), FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(9f);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(strDate, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setFixedHeight(9f);
         table.addCell(cell);
         return table;
     }

     private void initializeHeaderTableReceiptType_1() throws Exception {
         tableSuperSuperHdr = createTableSuperSuperHdr();
         tableSuperHdr = createTableSuperHdr();
         tableHdr = createTableHdr();
         printIncreaseOneLine();
     }

     private void initializeHeaderTableReceiptType_2() throws Exception {
         tableSuperSuperHdr = createTableSuperSuperHdr_2();
         tableSuperHdr = createTableSuperHdr_2();
         tableHdr = createTableHdr_2();
     }

     private void initializeHeaderTableReceiptType_3() throws Exception {
         tableSuperSuperHdr = createTableSuperSuperHdr_3();
         tableSuperHdr = createTableSuperHdr_3();
         tableHdr = createTableHdr_3();
     }

     private void printHeader(PdfPTable hdr1, PdfPTable hdr2, PdfPTable hdr3) throws Exception {
         if (hdr1 != null)
             document.add(hdr1);
         if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
             printBlankCell(1);
         }
         if (hdr2 != null)
             document.add(hdr2);
         if (receiptType.equals(strMBR_REDEMPTION_FORM) || receiptType.equals(strRBT_REDEMPTION_FORM)) {
             printBlankCell(1);
         }
         if (hdr3 != null)
             document.add(hdr3);
     }

     private void printTotalTable() throws BadElementException, DocumentException, Exception {
        double totalDiscount = 0;

        PdfPTable totaltable = new PdfPTable(5);
        int headerwidths[] = { 38, 16, 8, 8, 30 };
        totaltable.setWidthPercentage(100);
        totaltable.setWidths(headerwidths);
        totaltable.getDefaultCell().setUseAscender(true);
        totaltable.getDefaultCell().setUseDescender(true);

        if (resultMap.get("TOTAL_DISCOUNT_AMOUNT") != null &&
            !((String) resultMap.get("TOTAL_DISCOUNT_AMOUNT")).equals("")) {
            totalDiscount = Double.parseDouble((String) resultMap.get("TOTAL_DISCOUNT_AMOUNT"));
        }

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "TOTAL") + " : ", FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(13f);
         cell.setColspan(3);
         totaltable.addCell(cell);
                 
         String strTotalDiscountAmount = new DecimalFormat("#0.00").format(totalDiscount);
         cell = new PdfPCell(new Phrase(strTotalDiscountAmount, FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(13f);
         totaltable.addCell(cell);

         String strTotalAmount = new DecimalFormat("#0.00").format(currentPageTotalAmount - totalDiscount);
         cell = new PdfPCell(new Phrase(strTotalAmount, FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(13f);
         totaltable.addCell(cell);
         document.add(totaltable);
     }

     private void printTotalTable_2() throws BadElementException, DocumentException, Exception {
         PdfPTable totaltable = new PdfPTable(6);
         //int headerwidths[] = { 5, 22, 38, 8, 7, 8, 12 };
         int headerwidths[] = { 5, 22, 46, 7, 8, 12 };
         totaltable.setWidthPercentage(100);
         totaltable.setWidths(headerwidths);
         totaltable.getDefaultCell().setUseAscender(true);
         totaltable.getDefaultCell().setUseDescender(true);
         String strTotalQty = String.valueOf(currentPageTotalQuantity);
         String strTotalPoints = String.valueOf(currentPageTotalPoints);
         String strTotalAmount = new DecimalFormat("#0.00").format(currentPageTotalAmount);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "TOTAL"), FontChinese_tb_title_small2));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.disableBorderSide(Rectangle.BOX);
         cell.setPadding(0);
         cell.setPaddingRight(4f);
         cell.setFixedHeight(10f);
         cell.setColspan(3);
         totaltable.addCell(cell);

         cell = new PdfPCell(new Phrase(strTotalQty, FontChinese_tb_title_small2));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         totaltable.addCell(cell);

         cell = new PdfPCell(new Phrase(strTotalPoints, FontChinese_tb_title_small2));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         totaltable.addCell(cell);

         cell = new PdfPCell(new Phrase(strTotalAmount, FontChinese_tb_title_small2));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(10f);
         totaltable.addCell(cell);
         document.add(totaltable);
         strTotalQty = null;
         strTotalPoints = null;
         strTotalAmount = null;
     }

     private void printTotalTable_3() throws BadElementException, DocumentException, Exception {
         PdfPTable totaltable = new PdfPTable(5);
         int headerwidths[] = { 5, 24, 47, 12, 12 };
         totaltable.setWidthPercentage(100);
         totaltable.setWidths(headerwidths);
         totaltable.getDefaultCell().setUseAscender(true);
         totaltable.getDefaultCell().setUseDescender(true);
         String strTotalQty = String.valueOf(currentPageTotalQuantity);
         String strTotalAmount = new DecimalFormat("#0.00").format(currentPageTotalAmount);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(" ", FontChinese_tb_title_small2));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.disableBorderSide(Rectangle.BOX);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         cell.setColspan(3);
         totaltable.addCell(cell);

         cell = new PdfPCell(new Phrase(strTotalQty, FontChinese_tb_title_small2));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         totaltable.addCell(cell);

         cell = new PdfPCell(new Phrase(strTotalAmount, FontChinese_tb_title_small2));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(10f);
         totaltable.addCell(cell);
         document.add(totaltable);
         strTotalQty = null;
         strTotalAmount = null;
     }

     private void initializeFooterTable_1() throws BadElementException, DocumentException, Exception {
         tableFooter1 = createTableFooter1_ReceiptType1();
         tableFooter2 = createTableFooter2_ReceiptType1();
         tableFooter3 = createTableFooter3_ReceiptType1();
     }

     private void initializeFooterTable_2() throws BadElementException, DocumentException, Exception {
         //mega 20160215 START
         //tableFooter1 = createTableFooter1_ReceiptType2(); //before CLS Integration
         tableFooter1 = createTableFooter1_ReceiptType2_new(); //after CLS Integration
         //mega 20160215 END
         tableFooter2 = createTableFooter2_ReceiptType2();
         tableFooter3 = createTableFooter3_ReceiptType2();
     }

     private void initializeFooterTable_3() throws BadElementException, DocumentException, Exception {
         tableFooter1 = createTableFooter1_ReceiptType3();
         tableFooter2 = createTableFooter2_ReceiptType3();
         tableFooter3 = createTableFooter3_ReceiptType3();
     }

     private void printFooterTable() throws BadElementException, DocumentException, Exception {
         if (tableFooter1 != null)
             document.add(tableFooter1);
         if (tableFooter2 != null)
             document.add(tableFooter2);
         if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
             printBlankCell(1);
         }
         if (tableFooter3 != null)
             document.add(tableFooter3);
     }

     private void createDottedTable() throws BadElementException, DocumentException, Exception {
         PdfPTable dottedTable = new PdfPTable(1);
         dottedTable.setWidthPercentage(100);

         PdfPCell cell = new PdfPCell(new Phrase(" "));
         cell.setCellEvent(new CellDotted());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setFixedHeight(0.1f);
         dottedTable.addCell(cell);
         document.add(dottedTable);
     }

     private PdfPTable createTableFooter1_ReceiptType1() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_1 = new PdfPTable(1);
         footertable_1.setWidthPercentage(100);
         footertable_1.setHorizontalAlignment(Element.ALIGN_CENTER);
                 
                 strFOOTER_1 = adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_RECEIPT_ACKNOWLEDGEMENT_FOOTER1") + " " +
                                           adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_RECEIPT_ACKNOWLEDGEMENT_FOOTER2") + " " +
                                           adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_RECEIPT_ACKNOWLEDGEMENT_FOOTER3");

         PdfPCell cell = new PdfPCell(new Paragraph(strFOOTER_1, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
         cell.setPaddingTop(6f);
         cell.setPaddingRight(4f);
         cell.setPaddingBottom(8f);
         cell.setPaddingLeft(4f);
         footertable_1.addCell(cell);
         return footertable_1;
     }

     private PdfPTable createTableFooter1_ReceiptType2_new() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_1 = new PdfPTable(3);
         int headerwidths[] = { 60, 13, 27 };
         footertable_1.setWidthPercentage(100);
         footertable_1.setWidths(headerwidths);
         footertable_1.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_MBR_REDEMPTION_ACKNOWLEDGEMENT_FOOTER1"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);  
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "For Official Use"), FontChinese_tb_title_small));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         cell.setColspan(2);
         footertable_1.addCell(cell);
         //-------------------------------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Accumulated"), FontChineseSmall));
         cell.enableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("ACCUMULATED"), FontChineseSmall));
         cell.enableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setPadding(0);
         cell.setPaddingRight(2f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);
         //-------------------------------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Redeemed"), FontChineseSmall));
         cell.enableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("REDEEMED"), FontChineseSmall));
         cell.enableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setPadding(0);
         cell.setPaddingRight(2f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);
         //-------------------------------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase("________________________________", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         cell.setPaddingBottom(0.5f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Balance"), FontChineseSmall));
         cell.enableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("BALANCE"), FontChineseSmall));
         cell.enableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setPadding(0);
         cell.setPaddingRight(2f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);
         //-------------------------------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "SIGN_CARD_MBR"), FontChineseItalic));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Store Code"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("STORE_CODE"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);
         return footertable_1;
     }

     private PdfPTable createTableFooter1_ReceiptType3() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_1 = new PdfPTable(1);
         footertable_1.setWidthPercentage(100);
         footertable_1.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strFooterLine6), FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strFooterLine7), FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strFooterLine8), FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strFooterLine9), FontChinese_tb_title_small2));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_1.addCell(cell);
         return footertable_1;
     }

     private PdfPTable createTableFooter2_ReceiptType1() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_2 = new PdfPTable(4);
         int headerwidths[] = { 38, 30, 18, 14 };
         footertable_2.setWidthPercentage(100);
         footertable_2.setWidths(headerwidths);
         footertable_2.getDefaultCell().setUseAscender(true);
         footertable_2.getDefaultCell().setUseDescender(true);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "ACKNOWLEDGED BY"), FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(13f);
         footertable_2.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "WITNESSED BY"), FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(13f);
         footertable_2.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "PAYMENT MODE"), FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(13f);
         cell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.BOTTOM);
         footertable_2.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "please tick (v)"), FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
         footertable_2.addCell(cell);
         /*----------------------------------------------------------------------------------------------*/
         String strArr[] = new String[5];
         strArr[0] = adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strNAME_COLLECTOR);
         strArr[1] = "";
         strArr[2] = adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strNRIC_NO);
         strArr[3] = "";
         strArr[4] = adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strSIGNATURE);

         cell = new PdfPCell(getLeftAndCenterInnerTableOfFooter2(strArr));
         footertable_2.addCell(cell);
         strArr[0] = adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strNAME_STAFF);
         strArr[1] = (String) resultMap.get("NAME_OF_STAFF");
         strArr[2] = adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strSTAFF_ID);
         strArr[3] = (String) resultMap.get("STAFF_ID");
         strArr[4] = adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strSIGNATURE);

         cell = new PdfPCell(getLeftAndCenterInnerTableOfFooter2(strArr));
         footertable_2.addCell(cell);
         cell = new PdfPCell(getRightInnerTableOfFooter2());
         cell.setColspan(2);
         footertable_2.addCell(cell);
         return footertable_2;
     }

     private PdfPTable createTableFooter2_ReceiptType2() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_2 = new PdfPTable(3);
         int headerwidths[] = { 60, 13, 27 };
         footertable_2.setWidthPercentage(100);
         footertable_2.setWidths(headerwidths);
         footertable_2.setHorizontalAlignment(Element.ALIGN_CENTER);
         footertable_2.getDefaultCell().setUseAscender(true);
         footertable_2.getDefaultCell().setUseDescender(true);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "New IC/Passport No.") + ":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_2.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Staff Name") + " :", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_2.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("NAME_OF_STAFF"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_2.addCell(cell);
         //------------------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase((String) resultMap.get("NRIC_NO"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         footertable_2.addCell(cell);
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Employee No.") + " :", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_2.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("EMPLOYEE_NO"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         footertable_2.addCell(cell);
         return footertable_2;
     }

     private PdfPTable createTableFooter2_ReceiptType3() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_2 = new PdfPTable(3);
         int headerwidths[] = { 45, 20, 35 };
         footertable_2.setWidthPercentage(100);
         footertable_2.setWidths(headerwidths);
         footertable_2.setHorizontalAlignment(Element.ALIGN_CENTER);
         footertable_2.getDefaultCell().setUseAscender(true);
         footertable_2.getDefaultCell().setUseDescender(true);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setColspan(2);
         footertable_2.addCell(cell);
         cell =
             new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_RB_REDEMPTION_ACKNOWLEDGEMENT_FOOTER2"),
                                     FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         footertable_2.addCell(cell);
         //***********************************************************************************************//
         cell = new PdfPCell(getLeftSideFromFooter2_ReceiptType3());
         cell.enableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         footertable_2.addCell(cell);
         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         footertable_2.addCell(cell);

         cell = new PdfPCell(getRightSideFromFooter2_ReceiptType3());
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         footertable_2.addCell(cell);
         return footertable_2;
     }

     private PdfPTable getLeftSideFromFooter2_ReceiptType3() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(3);
         int headerwidths[] = { 35, 2, 63 };
         tbl.setWidthPercentage(100);
         tbl.setWidths(headerwidths);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);
         String strEmployee = resultMap.get("EMPLOYEE_NO") + " - " + resultMap.get("NAME_OF_STAFF");

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "For Office Use"), FontChineseBoldUnderlined));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         cell.setColspan(3);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChineseBoldUnderlined));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         cell.setColspan(3);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Issue By"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strEmployee, FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "COY_STORE_CD"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setPaddingLeft(2f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase((String) resultMap.get("STORE_CODE"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);
         strEmployee = null;
         return tbl;
     }

     private PdfPTable getRightSideFromFooter2_ReceiptType3() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(1);
         tbl.setWidthPercentage(100);
         tbl.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(getCheckbox(0));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         tbl.addCell(cell);

         cell = new PdfPCell(getCheckbox(0));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.enableBorderSide(Rectangle.BOTTOM);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "SIGN_CARD_MBR"), FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         tbl.addCell(cell);
         return tbl;
     }

     private PdfPTable createTableFooter3_ReceiptType1() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_3 = new PdfPTable(1);
         footertable_3.setWidthPercentage(100);  
         footertable_3.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = new PdfPCell(new Paragraph(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strFOOTER_3), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
         cell.setPaddingTop(4f);
         cell.setPaddingLeft(4f);
         footertable_3.addCell(cell);
         return footertable_3;
     }

     private PdfPTable createTableFooter3_ReceiptType2() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_3 = new PdfPTable(5);
         int headerwidths[] = { 5, 19, 10, 14, 52 };
         footertable_3.setWidthPercentage(100);
         footertable_3.setWidths(headerwidths);
         footertable_3.setHorizontalAlignment(Element.ALIGN_CENTER);
         footertable_3.getDefaultCell().setUseAscender(true);
         footertable_3.getDefaultCell().setUseDescender(true);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,strNOTE) + " : ", FontChineseBoldItalic));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(13f);
         cell.disableBorderSide(Rectangle.BOX);
         footertable_3.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, strFooterLine1), FontChineseItalic));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(13f);
         cell.setColspan(4);
         cell.disableBorderSide(Rectangle.BOX);
         footertable_3.addCell(cell);
         //-------------------------------------------------------------------------------------------------------//
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "GV_CARELINE_INFO"), FontChineseItalic_white));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         cell.setColspan(5);
         cell.setBackgroundColor(bgColor);
         cell.disableBorderSide(Rectangle.BOX);
         footertable_3.addCell(cell);
         
         return footertable_3;
     }

     private PdfPTable createTableFooter3_ReceiptType3() throws BadElementException, DocumentException, Exception {
         PdfPTable footertable_3 = new PdfPTable(4);
         int headerwidths[] = { 24, 10, 14, 52 };
         footertable_3.setWidthPercentage(100);
         footertable_3.setWidths(headerwidths);
         footertable_3.setHorizontalAlignment(Element.ALIGN_CENTER);
         footertable_3.getDefaultCell().setUseAscender(true);
         footertable_3.getDefaultCell().setUseDescender(true);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(" ", FontChineseSmall));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setPadding(0);
         cell.setFixedHeight(5f);
         cell.setColspan(4);
         footertable_3.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,"GV_CARELINE_INFO"), FontChineseItalic_white));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         cell.setColspan(4);
         cell.setBackgroundColor(bgColor);
         cell.disableBorderSide(Rectangle.BOX);
         footertable_3.addCell(cell);

         return footertable_3;
     }

     private PdfPTable getLeftAndCenterInnerTableOfFooter2(String strArr[]) throws BadElementException,
                                                                                   DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(3);
         int headerwidths[] = { 38, 1, 61 };
         tbl.setWidthPercentage(100);
         tbl.setWidths(headerwidths);
         tbl.getDefaultCell().setUseAscender(true);
         tbl.getDefaultCell().setUseDescender(true);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(strArr[0], FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strArr[1], FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strArr[2], FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setFixedHeight(10f);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strArr[3], FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strArr[4], FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingTop(7f);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPaddingTop(7f);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.disableBorderSide(Rectangle.BOX);
         tbl.addCell(cell);
         return tbl;
     }

     private PdfPTable getRightInnerTableOfFooter2() throws BadElementException, DocumentException, Exception {
         PdfPTable tbl = new PdfPTable(2);
         int headerwidths[] = { 85, 15 };
         tbl.setWidthPercentage(100);
         tbl.setWidths(headerwidths);
         tbl.getDefaultCell().setUseAscender(true);
         tbl.getDefaultCell().setUseDescender(true);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "CASH"), FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         if (resultMap.get("PAYMENT_MODE") != null && !((String) resultMap.get("PAYMENT_MODE")).equals("") &&
             resultMap.get("PAYMENT_MODE").equals("CS"))
             cell = new PdfPCell(new Phrase("  v", FontChineseSmall));
         else
             cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "CREDIT CARD"), FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         if (resultMap.get("PAYMENT_MODE") != null && !((String) resultMap.get("PAYMENT_MODE")).equals("") &&
             resultMap.get("PAYMENT_MODE").equals("CC"))
             cell = new PdfPCell(new Phrase("  v", FontChineseSmall));
         else
             cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "CHEQUE NO"), FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(13f);
         tbl.addCell(cell);

         if (resultMap.get("PAYMENT_MODE") != null && !((String) resultMap.get("PAYMENT_MODE")).equals("") &&
             resultMap.get("PAYMENT_MODE").equals("CH"))
             cell = new PdfPCell(new Phrase("  v", FontChineseSmall));
         else
             cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "TELEGRAPHIC TRANSFER"), FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);

         if (resultMap.get("PAYMENT_MODE") != null && !((String) resultMap.get("PAYMENT_MODE")).equals("") &&
             resultMap.get("PAYMENT_MODE").equals("TT"))
             cell = new PdfPCell(new Phrase("  v", FontChineseSmall));
         else
             cell = new PdfPCell(new Phrase("", FontChineseSmall));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setFixedHeight(10f);
         tbl.addCell(cell);
         return tbl;
     }

     private void printTotalPage() throws BadElementException, DocumentException, SQLException {
         Table pagetable = new Table(1);
         pagetable.setPadding(1f);
         pagetable.setSpacing(0);
         pagetable.setWidth(100);
         pagetable.setDefaultHorizontalAlignment(Element.ALIGN_CENTER);
         pagetable.setDefaultVerticalAlignment(Element.ALIGN_BOTTOM);
         pagetable.setBorder(Rectangle.NO_BORDER);
         pagetable.setSpaceInsideCell(1.0f);

         Cell cell = null;
         cell = new Cell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Page") + " " + currentPage + "/" + totalPage, FontPageNo));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.disableBorderSide(Rectangle.BOX);
         pagetable.addCell(cell);
         document.add(pagetable);
     }

     private void printVoucherCancellationReport() throws Exception {
         if (currentPage != totalPage) {
             initializeCancelRecordHeaderTable();
             printNewPage();
             printCancelRecordHeaderTable(tableSuperSuperHdr, tableSuperHdr, tableHdr);
             currentPage++;
             for (int i = currentItemNoWOReset_1; i <= resultMapList.size(); i++) {
                 if (currentItemNo <= 20) {
                     reportBodyRecord = (HashMap) resultMapList.get(currentItemNoWOReset_1 - 1);
                     printCancellationData(reportBodyRecord);
                     currentItemNo++;
                     currentItemNoWOReset_1++;
                 } else {
                     break;
                 }
             }
             if (currentItemNo != 20) {
                 printBlankCell_2(20 - currentItemNo);
             }
             printBlankCell(1);
             printTotalPage();
             currentItemNo = 1;
             printVoucherCancellationReport();
         }
     }

     private void initializeCancelRecordHeaderTable() throws Exception {
         tableSuperSuperHdr = createTableSuperSuperHdr_forCancel();
         tableSuperHdr = createTableSuperHdr_forCancel();
         tableHdr = createTableHdr_forCancel();
     }

     private void printCancelRecordHeaderTable(PdfPTable hdr1, PdfPTable hdr2, PdfPTable hdr3) throws Exception {
         document.add(hdr1);
         printBlankCell(1);
         document.add(hdr2);
         printBlankCell(1);
         document.add(hdr3);
     }

     private PdfPTable createTableSuperSuperHdr_forCancel() throws Exception {
         PdfPTable tableSuperSuperHdr = new PdfPTable(1);
         tableSuperSuperHdr.setWidthPercentage(100);
         tableSuperSuperHdr.setHorizontalAlignment(Element.ALIGN_CENTER);
         if (company_logo_image == null) {
             try {
                 company_logo_image = Image.getInstance(coy_logo_image_file);
                 company_logo_image.scalePercent(60);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         PdfPCell cell = null;
         if (company_logo_image != null) {
             cell = new PdfPCell(company_logo_image);
         } else {
             cell = new PdfPCell(new Phrase(""));
         }
         cell.disableBorderSide(Rectangle.BOX);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         tableSuperSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,SYSCompanyName) + " " + SYSCompanyRegNo, FontChinese_tb_title));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         tableSuperSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Voucher Issuance Cancellation Report"), FontChinese_title));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setVerticalAlignment(Element.ALIGN_TOP);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         tableSuperSuperHdr.addCell(cell);
         return tableSuperSuperHdr;
     }

     private PdfPTable createTableSuperHdr_forCancel() throws Exception {
         PdfPTable tableSuperHdr = new PdfPTable(7);
         int headerwidths[] = { 14, 1, 30, 20, 14, 1, 30 };
         tableSuperHdr.setWidths(headerwidths);
         tableSuperHdr.setWidthPercentage(100);
         tableSuperHdr.setHorizontalAlignment(Element.ALIGN_CENTER);
         AduserInfo aduserInfo = new AduserInfo();
         aduserInfo = getAduserInfo(USER_ID);
         SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String strPrintDate = fmt2.format(new java.util.Date());
         String strTransactionNo = "";
         String strPrintBy = aduserInfo.USR_FIRST_NAME() + " " + aduserInfo.USR_LAST_NAME();
         String strCancelBy = "";
         String strCancelDate = "";
         if (resultMap.get("TRANSACTION_NO") != null && !((String) resultMap.get("TRANSACTION_NO")).equals("")) {
             strTransactionNo = getDescription((String) resultMap.get("TRANSACTION_NO"));
         }
         if (resultMap.get("CANCEL_BY") != null && !((String) resultMap.get("CANCEL_BY")).equals("")) {
             strCancelBy = getDescription((String) resultMap.get("CANCEL_BY"));
         }
         if (resultMap.get("CANCEL_DATE") != null && !((String) resultMap.get("CANCEL_DATE")).equals("")) {
             java.util.Date dt = qrMisc.parseDate((String) resultMap.get("CANCEL_DATE"), "YYYY-MM-DD");
             strCancelDate = (fmt2.format(dt)).substring(0, 10);
         }
         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Company"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(COY + " - " + SYSCompanyName, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strPrintDate, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         cell.setColspan(3);
         tableSuperHdr.addCell(cell);
         /*----------------------------------------------------------------------------------*/
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Transaction No"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strTransactionNo, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,"Printed By"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strPrintBy, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);
         /*----------------------------------------------------------------------------------*/
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Cancelled By"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strCancelBy, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         cell.setColspan(4);
         tableSuperHdr.addCell(cell);
         /*----------------------------------------------------------------------------------*/
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Date Cancelled"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(":", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(strCancelDate, FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         tableSuperHdr.addCell(cell);

         cell = new PdfPCell(new Phrase("", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setPadding(0);
         cell.setFixedHeight(13f);
         cell.setColspan(4);
         tableSuperHdr.addCell(cell);

         return tableSuperHdr;
     }

     private PdfPTable createTableHdr_forCancel() throws Exception {
         PdfPTable tableHdr = new PdfPTable(8);
         int headerwidths[] = { 5, 12, 12, 18, 18, 8, 11, 20 };
         tableHdr.setWidths(headerwidths);
         tableHdr.setWidthPercentage(100);
         tableHdr.setHorizontalAlignment(Element.ALIGN_CENTER);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Store"), FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Voucher Type"), FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Voucher Denomination"), FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "From Voucher Number"), FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "To Voucher Number"), FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Quantity"), FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Amount (RM)"), FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         tableHdr.addCell(cell);

         cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE, "Remark"), FontChinese_tb_title));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         tableHdr.addCell(cell);
         return tableHdr;
     }

     private void printCancellationData(Map printMap) throws Exception {
         PdfPTable dataTbl = new PdfPTable(8);
         int headerwidths[] = { 5, 12, 12, 18, 18, 8, 11, 20 };
         dataTbl.setWidths(headerwidths);
         dataTbl.setWidthPercentage(100);
         dataTbl.setHorizontalAlignment(Element.ALIGN_CENTER);
         PdfPCell cell = null;
         String strStore = "";
         String strGvType = getDescription((String) printMap.get("GV_TYPE"));
         String strGvDeno = getDescription((String) printMap.get("GV_DENOMINATION"));
         String strQuantity = "";
         String strAmount = "";
         if (printMap.get("STORE") != null && !((String) printMap.get("STORE")).equals("")) {
             strStore = getDescription((String) printMap.get("STORE"));
         }
         if (printMap.get("GV_TYPE") != null && printMap.get("GV_TYPE_DESC") != null &&
             !((String) printMap.get("GV_TYPE")).equals("") && !((String) printMap.get("GV_TYPE_DESC")).equals("")) {
             strGvType = strGvType + " - " + getDescription((String) printMap.get("GV_TYPE_DESC"));
         }
         if (printMap.get("GV_DENOMINATION") != null && printMap.get("GV_DENO_DESC") != null &&
             !((String) printMap.get("GV_DENOMINATION")).equals("") &&
             !((String) printMap.get("GV_DENO_DESC")).equals("")) {
             strGvDeno = strGvDeno + " - " + getDescription((String) printMap.get("GV_DENO_DESC"));
         }
         if ((String) printMap.get("QUANTITY") != null && !((String) printMap.get("QUANTITY")).equals("")) {
             strQuantity = getDescription((String) printMap.get("QUANTITY"));
         } else {
             strQuantity = "0";
         }
         if (printMap.get("AMOUNT") != null && !((String) printMap.get("AMOUNT")).equals("")) {
             strAmount = new DecimalFormat("#0.00").format(Double.parseDouble((String) printMap.get("AMOUNT")));
         } else {
             strAmount = "0.00";
         }
         cell = new PdfPCell(new Phrase(strStore, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingBottom(2f);
         dataTbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strGvType, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingBottom(2f);
         dataTbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strGvDeno, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingBottom(2f);
         dataTbl.addCell(cell);

         cell = new PdfPCell(new Phrase((String) printMap.get("FR_GV_NO"), FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         dataTbl.addCell(cell);

         cell = new PdfPCell(new Phrase((String) printMap.get("TO_GV_NO"), FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         dataTbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strQuantity, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         dataTbl.addCell(cell);

         cell = new PdfPCell(new Phrase(strAmount, FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingRight(1f);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         dataTbl.addCell(cell);

         cell = new PdfPCell(new Phrase((String) printMap.get("REMARK"), FontChinese));
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         cell.setPadding(0);
         cell.setPaddingLeft(1f);
         cell.setPaddingBottom(2f);
         cell.setFixedHeight(30f);
         dataTbl.addCell(cell);

         document.add(dataTbl);
     }

     private PdfPTable getCheckbox(int boxQty) throws Exception {
         PdfPTable tbl = new PdfPTable(7);
         int headerwidths[] = { 12, 12, 12, 12, 12, 12, 28 };
         tbl.setWidthPercentage(100);
         tbl.setWidths(headerwidths);
         tbl.setHorizontalAlignment(Element.ALIGN_LEFT);
         PdfPCell cell = null;
         for (int j = 0; j < headerwidths.length; j++) {
             if (j < boxQty) {
                 cell = new PdfPCell(new Phrase(" ", FontChinese));
                 cell.enableBorderSide(Rectangle.BOX);
                 cell.setPadding(0);
                 cell.setFixedHeight(10f);
                 tbl.addCell(cell);
             } else {
                 cell = new PdfPCell(new Phrase(" ", FontChinese));
                 cell.disableBorderSide(Rectangle.BOX);
                 cell.setPadding(0);
                 cell.setFixedHeight(10f);
                 tbl.addCell(cell);
             }
         }
         return tbl;
     }

     //This is the blank row space between all Table Header.
     private void printBlankCell(int max) throws Exception {
         PdfPTable tableBlank = new PdfPTable(1);
         tableBlank.setWidthPercentage(100);
         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase("\n", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         for (int i = 0; i < max; i++) {
             tableBlank.addCell(cell);
         }
         document.add(tableBlank);
     }

     private void printBlankCell_2(int max) throws Exception {
         PdfPTable tableBlank = new PdfPTable(1);
         tableBlank.setWidthPercentage(100);
         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase("\n", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         cell.setFixedHeight(30f);
         for (int i = 0; i < max; i++) {
             tableBlank.addCell(cell);
         }
         document.add(tableBlank);
     }

     private void printIncreaseOneLine() throws Exception {
         PdfPTable tableIncreaseOneLine = new PdfPTable(1);
         tableIncreaseOneLine.setWidthPercentage(100);
         PdfPCell cell = new PdfPCell(new Phrase("\n", FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         tableIncreaseOneLine.addCell(cell);
         document.add(tableIncreaseOneLine);
     }

     private void printBlankPage() throws Exception {
         PdfPTable table = new PdfPTable(1);
         table.setWidthPercentage(100);
         if (printBlankPage_ == false) {
             PdfPCell cell = new PdfPCell(new Phrase(getTranslatedCaptionMsg("No Data To Print"), FontChinese));
             cell.disableBorderSide(Rectangle.BOX);
             table.addCell(cell);
             printBlankPage_ = true;
         } else {
             PdfPCell cell = new PdfPCell(new Phrase("\n", FontChinese));
             cell.disableBorderSide(Rectangle.BOX);
             table.addCell(cell);
         }
         document.add(table);
     }

     private void printNoPrintType() throws Exception {
         printIncreaseOneLine();
         printBlankPage_2();
         printBlankCell(1);
     }

     private void printNoReceiptType() throws Exception {
         printIncreaseOneLine();
         printBlankPage_3();
         printBlankCell(1);
     }

     private void printBlankPage_2() throws Exception {
         PdfPTable table = new PdfPTable(1);
         table.setWidthPercentage(100);
         PdfPCell cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,"GVRSNMST.PRINT_TYPE cannot be found"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         table.addCell(cell);
         document.add(table);
     }

     private void printBlankPage_3() throws Exception {
         PdfPTable table = new PdfPTable(1);
         table.setWidthPercentage(100);
         PdfPCell cell = new PdfPCell(new Phrase(adlangmstSQL.getTranslatedCaptionMsg(USER_LANGUAGE,"GVRSNMST.RECEIPT_TYPE cannot be found"), FontChinese));
         cell.disableBorderSide(Rectangle.BOX);
         table.addCell(cell);
         document.add(table);
     }

     private void printBlankDataTable() throws BadElementException, DocumentException, Exception {
         PdfPTable blankTable = null;
         List width = null;
         if (receiptType.equals(strISSUANCE_FORM)) {
             width = new ArrayList();
             blankTable = new PdfPTable(5);
             int columnWidths[] = { 38, 16, 8, 8, 30 };  
             width.add(columnWidths);
         } else if (receiptType.equals(strMBR_REDEMPTION_FORM)) {
             width = new ArrayList();
             blankTable = new PdfPTable(6);
             int columnWidths[] = { 5, 22, 46, 7, 8, 12 };
             width.add(columnWidths);
         } else if (receiptType.equals(strRBT_REDEMPTION_FORM)) {
             width = new ArrayList();
             blankTable = new PdfPTable(5);
             int columnWidths[] = { 5, 24, 47, 12, 12 };
             width.add(columnWidths);
         }
         if (width != null && !width.isEmpty()) {
             int headerwidths[] = (int[]) width.get(0);
             blankTable.setWidthPercentage(100);
             blankTable.setWidths(headerwidths);
             blankTable.getDefaultCell().setUseAscender(true);
             blankTable.getDefaultCell().setUseDescender(true);
             PdfPCell cell = null;
             for (int i = 0; i < headerwidths.length; i++) {
                 cell = new PdfPCell(new Phrase("\n", FontChinese));
                 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                 cell.setVerticalAlignment(Element.ALIGN_TOP);
                 cell.setPadding(0);
                 cell.setPaddingLeft(1f);
                 if (receiptType.equals(strISSUANCE_FORM)) {
                     cell.setFixedHeight(13f);
                 } else if (receiptType.equals(strMBR_REDEMPTION_FORM) || receiptType.equals(strRBT_REDEMPTION_FORM)) {
                     cell.setFixedHeight(10f);
                 }
                 blankTable.addCell(cell);
             }
         }
         if (blankTable != null) {
             document.add(blankTable);
         }
     }

     /**
      * Inner class with a table event that draws a background with rounded corners.
      */
     class TableBackground implements PdfPTableEvent {
         public void tableLayout(PdfPTable table, float[][] width, float[] height, int headerRows, int rowStart,
                                 PdfContentByte[] canvas) {
             PdfContentByte background = canvas[PdfPTable.BASECANVAS];
             background.saveState();
             background.roundRectangle(width[0][0], height[height.length - 1] - 2, width[0][1] - width[0][0] + 6,
                                       height[0] - height[height.length - 1] - 4, 8);
             background.stroke();
             background.restoreState();
         }
     }

     /**
      * Inner class with a cell event that draws a background with rounded corners.
      */
     class CellDotted implements PdfPCellEvent {
         public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
             PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
             canvas.setLineDash(3f, 10f);
             //            canvas.rectangle(position.getLeft(), position.getBottom(),
             //                position.getWidth(), position.getHeight());
             canvas.rectangle(position.left(), position.bottom(), position.width(), position.height());
             canvas.stroke();
         }
     }

     private boolean isFoundInStr(String strList, String item) {
         boolean result = false;
         if (strList == null)
             return false;
         String[] splitedStr = strList.split(",");
         for (int i = 0; i < splitedStr.length; i++) {
             if (item.equals(splitedStr[i].replace("'", ""))) {
                 return true;
             }
         }
         return result;
     }
 }