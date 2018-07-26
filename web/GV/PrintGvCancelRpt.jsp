<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "qrcom.util.*" %>
<%@ page import = "qrcom.PROFIT.files.info.AduserInfo"%>
<%@ page import = "qrcom.PROFIT.files.info.SysctlSQL"%>
<%@ page import = "qrcom.PROFIT.webbean.HTTPObj.*" %>
<%@ page import = "qrcom.PROFIT.webbean.language.*" %>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultCoySub" %>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultStore" %>
<%@ page import = "qrcom.PROFIT.system.*"%>
<%@ page import = "qrcom.PROFIT.files.dao.local.MT.MultiStoreDAO"%>

<jsp:useBean id="jbSession" scope="session" class="qrcom.PROFIT.servlet.HTTPSessionAttributeWrapper" />
<jsp:useBean id="jbWebSessionParam" scope="session" class="qrcom.PROFIT.webbean.HTTPObj.WebSessionParam" />

<%
    WResGUI jbWResGUI 					= jbWebSessionParam.getWResGUI(request);
	WResPrompt jbWResPrompt 			= jbWebSessionParam.getWResPrompt(request);
	WebAuthorization webAuth 			= new WebAuthorization(request);
	CoysubmstComboBox coysubmstComboBox = new CoysubmstComboBox(request);
	MultiStoreDAO  multiStoreDAO        = new MultiStoreDAO();
    AduserInfo aduserInfo = (AduserInfo)jbWebSessionParam.getAduserInfo();
    DefaultCoySub defaultCoySub= new DefaultCoySub(aduserInfo);
    String defaultCoysub = defaultCoySub.getDEFAULT_COY_SUB();

	String BaseURL 				= SysParam.getInstance().getBaseURL();
	String lang_code 			= jbWebSessionParam.getAduserInfo().USR_LANGUAGE();
	String user_id 				= jbWebSessionParam.getAduserInfo().USR_ID();
	String strCoy 				= jbWebSessionParam.getAduserInfo().USR_COMP();
	String SYSStoreLength 		= jbWebSessionParam.getProfitvvValue(request, "SYSStoreLength");
	
    HParam hParam 				= new HParam();

    String strAction 			= request.getParameter("ACTION");
    String strCoySub            = request.getParameter("cboSubCoy");
    String strStore             = request.getParameter( "txtStore");
    String strGvType            = request.getParameter("cboGvType");
    String strGvDeno            = request.getParameter("cboGvDeno");
    String strFromDateCancelled = request.getParameter("txtFromDateCancelled"); 
    String strToDateCancelled   = request.getParameter("txtToDateCancelled");

    String TIME_CD              = "";
    String strReturnStores      = "";
    String tableName            = "CANCEL_VOUCHER_RPT";
    String StoreIndicator       = request.getParameter( "StoreIndicator" );

    if(strStore == null){strStore = "";}
    if(StoreIndicator == null) { StoreIndicator = "";}

    if (strAction != null && (strAction.equals("INIT")||strAction.equals("reset"))) {	  
        strFromDateCancelled = "";
        strToDateCancelled = ""; 
        strGvType = "";
        strStore = "";
        strGvDeno = "";

        jbSession.removeAttribute("TIME_CD");
        TIME_CD = String.valueOf(System.currentTimeMillis());
        jbSession.setAttribute("TIME_CD", TIME_CD);
        multiStoreDAO.clearWorkmstJSPGlobal(request.getSession().getId(), TIME_CD, tableName);
    }
    else {
        TIME_CD = (String) jbSession.getAttribute("TIME_CD");
    }
    
    Vector vctStore = (Vector)jbSession.getAttribute("vctStore");
  
    if(vctStore == null){
        vctStore = new Vector();
        jbSession.setAttribute("vctStore", vctStore);
    }
    if(strAction.equals("selectedStores")||strAction.equals("selectAllStore")) {
        String tmp  = "";
        String tmp2 = "";
            
        if(strAction.equals("selectedStores")) {
            String strStores2 = request.getParameter("RETURN_VALUE");
            StringTokenizer st3 = new StringTokenizer(strStores2, "^|^");
            StringBuffer hdr = new StringBuffer();
            
            while (st3.hasMoreTokens()) {
                String store = st3.nextToken();    
                hdr.append(",").append(store);   // Example: store = 0001,0002
                vctStore.remove(store.trim());
                vctStore.add(store.trim());
            }        
            
            tmp = hdr.toString().replaceFirst(",","");
            strStore = tmp;   
        }
        else {
            tmp  = (String)jbSession.getAttribute("SELECT_ALL_STORES"); 
            tmp  = tmp.substring(0, tmp.lastIndexOf(","));
            if(StoreIndicator.equals("STORES")){
                strStore = tmp; 
            }      
        }  
        strReturnStores    = "Y";      
   }
   if (strCoySub == null) {
       strCoySub = defaultCoysub;
   }
%>

<!DOCTYPE html>
<html>
    <head>
        <%
            String errorMsg = "";	
            Collection err = (Collection)request.getSession().getAttribute("errorSets");
            if(err != null && !err.isEmpty()) {
        %>
        <script>
            window.open('<%=BaseURL%>/profit/Common/Info.jsp', '', 'height=300,width=500,left=0,top=0,resizable=yes,scrollbars=yes');
        </script>
        <%
            }
        %>
        <title><%=jbWResGUI.getRes("Print Voucher Cancellation Report")%></title>
		<jsp:include page="../Common/Header.jsp"/>
		<jsp:include page="../Common/Script.jsp"/>
        <script>
            var msg = '';

            //Return calendar value
			function restart() { 
                'use strict';
                try {    
                    if(whichOne === 1) {
                        document.FORM.elements['txtFromDateCancelled'].value = '' + year + '-' + padout(month - 0 + 1) + '-' + padout(day);
                    }
                    else if(whichOne === 2) {   
                        document.FORM.elements['txtToDateCancelled'].value = '' + year + '-' + padout(month - 0 + 1) + '-' + padout(day);
                    }
                    
                    mywindow.close();
                    onBlurValidateDate();
                }
                catch(e) { 
                    alert('<%=jbWResPrompt.getRes("Please Try Again")%>');
                }      
            }
                
            function onBlurValidateDate()
            {
                'use strict';
                var objDateFr = eval('window.document.FORM.txtFromDateCancelled');
                var objDateTo = eval('window.document.FORM.txtToDateCancelled');
                
                if (objDateFr.value !== '' && objDateTo.value !== '') {
                    if (objDateFr.value > objDateTo.value) {
                        alert('<%=jbWResPrompt.getRes("TO")%> <%=jbWResPrompt.getRes("Date")%> <%=jbWResPrompt.getRes("must be equal or greater than")%> <%=jbWResPrompt.getRes("FROM")%> <%=jbWResPrompt.getRes("Date")%>');
                        objDateTo.value = '';
                        objDateTo.focus();
                    }
                }
                return true;
            }

            function openStoreWindow() {  
                'use strict';
                var path = '';
                var strFilter = " STRMST.STORE_DEL_CD !='Y' ";
                //var strFilter = " STORE_COY = '<%=strCoy%>' AND STORE_COY_SUB = '<%=strCoySub%>' AND STRMST.STORE NOT IN (SELECT VNM_VDTVL FROM PROFITVV WHERE COY = '<%=strCoy%>' AND VNM = 'SYSHeadOfficeStore') ";
                
                path = '<%=BaseURL%>/profit/MT/MultiStoreSearchV3.jsp?ACTION=search&SELECT_MODE=multi_default_supp_rebate_trading&KEY10=<%=tableName%>&FILTER1='+strFilter;
                window.document.FORM.SEARCH_ACTION.value = 'SELECT_STORES';
                window.document.FORM.StoreIndicator.value = 'STORES';
                window.open(path,'null','height=600,width=700,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes');
            }

            function selectAll() {
                'use strict';
                var TargetClass    = 'qrcom.PROFIT.webbean.dao.MT.StrmstSearch';
                var TargetMethod   = 'selectAllStoreSalesGlobal';
                var CallbackMethod = 'selectAllStoreCalledBack';
                var Params  = '&TIME_CD=<%=TIME_CD%>&TABLE_NAME=<%=tableName%>';          
                var baseURL = '<%=BaseURL%>';              
                
                invokeTargetClass(baseURL, TargetClass, TargetMethod, CallbackMethod, Params);         
            }

            function selectAllStoreCalledBack() {
                'use strict';
                window.document.FORM.ACTION.value = 'selectAllStore';
                window.document.FORM.action       = '<%=BaseURL%>/profit/GV/PrintGvCancelRpt.jsp?ACTION=selectAllStore';
                window.document.FORM.submit();    
            }

            function checkMultiStore() {	
                'use strict';
                if(!validateCoySub())
                    return;
                var value = window.document.FORM.txtStore.value;

                var TargetClass    = 'qrcom.PROFIT.webbean.dao.MT.DefaultStrmstWebBean';
                var TargetMethod   = 'checkListOfAuthStore';
                var CallbackMethod = 'onChangeStoreCalledBack';
                
                var Params = '&STORE=' + value + '&COY=' + '<%=strCoy%>'+ '&TIME_CD=' + '<%=TIME_CD%>' + '&TABLE_NAME='+'<%=tableName%>' + '&USER_ID='+'<%=user_id%>';
                var baseURL = '<%=BaseURL%>';
                
                invokeTargetClass(baseURL, TargetClass, TargetMethod, CallbackMethod, Params);
            }

            function returnValue(strReturn) {
				'use strict';
                var search_action = window.document.FORM.SEARCH_ACTION.value;
                if(search_action === 'SELECT_STORES') { 
                    window.document.FORM.RETURN_VALUE.value = strReturn;
                    window.document.FORM.ACTION.value       = 'selectedStores';
                    window.document.FORM.action             = '<%=BaseURL%>/profit/GV/PrintGvCancelRpt.jsp?ACTION=selectedStores';
                    window.document.FORM.submit();
                }
			}

            function onChangeStoreCalledBack(strReturn) {	
                'use strict';
                if(getValue('FINAL_VALUE', strReturn).length > 0 )
                    window.document.FORM.txtStore.value  = getValue('FINAL_VALUE', strReturn);
                else
                    window.document.FORM.txtStore.value  = '';
                
                if (getValue('ERROR_VALUE', strReturn).length > 0 ) {
                    alert('List of invalid <%=jbWResPrompt.getRes("STORE")%> entered : ' + getValue('ERROR_VALUE', strReturn));
                    window.document.FORM.txtStore.focus();
                }
            }
			
			function onChgVoucherType() {
				'use strict';
				var url = '<%=BaseURL%>/profit/GV/PrintGvCancelRpt.jsp';
				window.document.FORM.action = url;
				window.document.FORM.submit();
			}

            function validateCoySub() {
                'use strict';
                var val = window.document.FORM.cboSubCoy.value;
                
                if(val.length === 0) {
                    alert('Please select Company Code.');
                    window.document.FORM.cboSubCoy.focus();
                    return false;	
                }
                return true;	
            }

            function chkDateTxtField() {
                'use strict';
                var objDateFr = eval('window.document.FORM.txtFromDateCancelled');
                var objDateTo = eval('window.document.FORM.txtToDateCancelled');
                var bValid = true;
                
                if (objDateFr.value !== '' && objDateTo.value === '') {
                    alert('<%=jbWResPrompt.getRes("TO")%> <%=jbWResPrompt.getRes("Date Cancelled")%> <%=jbWResPrompt.getRes("must be entered")%>');
                    objDateTo.focus();
                    bValid = false;
                }
                else if (objDateFr.value === '' && objDateTo.value !== '') {
                    alert('<%=jbWResPrompt.getRes("FROM")%> <%=jbWResPrompt.getRes("Date Cancelled")%> <%=jbWResPrompt.getRes("must be entered")%>');
                    objDateFr.focus();
                    bValid = false;
                }
                
                return bValid;
            }

            function getReport() {   
                'use strict';
                if (!CheckMandatory(window.document.FORM))
                    return false;
                if(!chkDateTxtField()) {
                    return;
                }  
                
                var COMP_NAME = window.document.FORM.txtCoyName.value;
                var COY = window.document.FORM.txtCoy.value;
                var COY_SUB = window.document.FORM.cboSubCoy.value;
                var STORES = window.document.FORM.txtStore.value;
                var GV_TYPE = window.document.FORM.cboGvType.value;
                var GV_DENOMINATION = window.document.FORM.cboGvDeno.value; 
                var FR_DATE_CANCELLED = window.document.FORM.txtFromDateCancelled.value;
                var TO_DATE_CANCELLED = window.document.FORM.txtToDateCancelled.value;
                
                var strUrl = '<%=BaseURL%>/servlet/SvltRptProcessor';
                strUrl +=   '?ACTION=print_to_file&USER_ID=<%=user_id%>' +
                            '&TIME_CD=<%=TIME_CD%>' +
                            '&COMP_NAME=' + COMP_NAME +      
                            '&COY=' + COY +
                            '&COY_SUB=' + COY_SUB  +   
                            '&STORE=' + STORES  +
                            '&GV_TYPE=' + GV_TYPE + 
                            '&GV_DENOMINATION=' + GV_DENOMINATION +
                            '&FR_DATE_CANCELLED=' + FR_DATE_CANCELLED +
                            '&TO_DATE_CANCELLED=' + TO_DATE_CANCELLED +
                            '&report_id=GV_PrintGvCancelRpt' ;
                if(STORES.length > 0) {
                    strUrl += '&EXIST_STORE=Y' ;
                }
            
                strUrl +=         
                    '&DES_TYPE=file' +
                    '&RPT_CLASSNAME=qrcom.PROFIT.reports.GV.PrintGvCancelRpt' +
                    '&DES_FORMAT=xls' +
                    '&RPT_QUEUE=Y' +
                    '&RPT_QUEUE_ID=2'; 
                
                window.document.FORM.action = strUrl;
                window.document.FORM.txtStore.disabled = true;
                window.document.FORM.submit();   
            }

        </script>
    </head>

    <body align="center">
        <center>
            <form id="FORM" method="post" action="<%=BaseURL%>/profit/GV/PrintGvCancelRpt.jsp" name="FORM">
                <fieldset class="fieldsettitle" align="center">
                    <legend><%=jbWResGUI.getRes("Print Voucher Cancellation Report")%></legend>
                    <table width="100%" cellpadding="5" cellspacing="0" border="0" class="table-border">
                        <!-- SEARCH CRITERIA -->
                        <tr><td>
                            <table BORDER="0" WIDTH="100%" ALIGN="center" CELLSPACING="1" CELLPADDING="3">
                                <tr>
                                    <td width="20%" class="caption"><%= jbWResGUI.getRes("Company")%></td>
                                    <td width="5%"><input type="text" name="txtCoy" value="<%=strCoy%>" readonly tabindex="-1" class="input-display"/></td>
                                    <td width="20%"><input type="text" name="txtCoyName" value="<%=HTTPUtilClass.getDesc(lang_code, "COY_NAME", "COYMST", " WHERE COY = '" + strCoy + "'")%>" readonly tabindex="-1" class="input-display"/></td>
                                    <td width="5%"></td>
                                    <td width="20%" class="caption"><%= jbWResGUI.getRes("Company Subsidiary")%></td>
                                    <td width="25%">
                                        <select name="cboSubCoy" class="mandatory" >
                                            <%=coysubmstComboBox.getAuthorisedCoySubComboBoxIncludedDeleted(strCoySub)%>
                                        </select>
                                    </td>
                                    <td width="5%"></td>
                                </tr>
                                <tr>
                                    <td width="20%" class="caption"><%= jbWResGUI.getRes("Store")%></td>
                                    <td colspan="2"><textarea NAME="txtStore" rows=3 cols=60 onchange="checkMultiStore()"><%=strStore%></textarea></td>
                                    <td width="5%" ><input NAME="cmdSelectStores" TYPE="button" VALUE="<%=jbWResGUI.getRes("List")%>" onclick="if(validateCoySub())openStoreWindow();" STYLE='cursor:hand;width:100%;'></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td width="20%" class="caption"><%=jbWResGUI.getRes("Voucher Type")%></td>
                                    <td colspan="2">
                                        <select name="cboGvType" onchange="onChgVoucherType();">
                                            <%=MTComboBox.Default(lang_code, strGvType, "GVTYPEMST.GV_TYPE", "GVTYPEMST.GV_TYPE_DESC", "GVTYPEMST", "ORDER BY GVTYPEMST.GV_TYPE ")%>       	  
                                        </select>
                                    </td>
                                    <td width="5%"></td>
                                    <td width="20%" class="caption"><%=jbWResGUI.getRes("Denomination")%></td>
                                    <td width="25%">
                                        <select name="cboGvDeno">
                                            <%=MTComboBox.Default(lang_code, strGvDeno,"GV_DENOMINATION", "GV_DENO_DESC", "GVDENOMST", "WHERE COY = '"+strCoy+"' AND COY_SUB = '"+strCoySub+"' AND GV_TYPE = '"+strGvType+"' AND DEL_CD = 'N' ORDER BY GV_DENOMINATION ")%>
                                        </select>
                                    </td>
                                    <td width="5%"></td>
                                </tr>
                                <tr>
                                    <td width="20%" class="caption"><%= jbWResGUI.getRes("Voucher Cancellation From Date")%></td>
                                    <td colspan="2"><input type="text" name="txtFromDateCancelled"  value="<%=strFromDateCancelled%>" onblur="this.value=formatDate(this.value); onBlurValidateDate();"  maxlength="10" class="mandatory" ></td>
                                    <td width="5%"><img src="<%=BaseURL%>/profit/images/calendr.gif" ALT="<%=jbWResGUI.getRes("Calendar")%>" onclick=newWindow(1) style='cursor:hand'></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td width="20%" class="caption"><%= jbWResGUI.getRes("Voucher Cancellation To Date")%></td>
                                    <td colspan="2"><input type="text" name="txtToDateCancelled"  value="<%=strToDateCancelled%>" onblur="this.value=formatDate(this.value); onBlurValidateDate();"  maxlength="10" class="mandatory" ></td>
                                    <td width="5%"><img src="<%=BaseURL%>/profit/images/calendr.gif" ALT="<%=jbWResGUI.getRes("Calendar")%>" onclick=newWindow(2) style='cursor:hand'></td>
                                    <td></td>
                                </tr>
                            </table>
                        </td></tr>
                        <!-- END: SEARCH CRITERIA -->
                        <!-- BUTTONS -->
                        <table BORDER="0" WIDTH="100%" ALIGN="center" CELLSPACING="1" CELLPADDING="1">  
                        <tr>
                            <td width="60%"></td> 
                            <td width="20%"><input name="cmdPrint" type="button" value="<%=jbWResGUI.getRes("Print")%>" onClick="getReport()"></td>
                            <td width="20%"><input name="cmdReset" type="button" value="<%=jbWResGUI.getRes("Reset")%>" onClick="setFormReset(this.form,'<%=BaseURL%>/profit/GV/PrintGvCancelRpt.jsp')"></td>
                        </tr>
                        </table>
                        <!-- END: BUTTONS -->
                    </table>



                </fieldset>
                <input type="hidden" name="ACTION" id="ACTION">
                <input type="hidden" name="PAGE_ID" id="PAGE_ID" value="VoucherCancellationRpt">
                <input name="hiddenTime"    id="hiddenTime"    type="hidden" value="<%=TIME_CD%>">
                <input name="ReturnStore"   id="ReturnStore"   type="hidden" value="<%=strReturnStores%>">
                <input name="RETURN_VALUE"  id="RETURN_VALUE"  type="hidden">
                <input name="StoreIndicator" id="StoreIndicator" type="hidden" >
                <input name="SEARCH_ACTION" id="SEARCH_ACTION" type="hidden" >
                
            </form>
        </center>
    </body>
</html>