<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage = "/profit/Common/ExceptionError.jsp" %>
<%@ page import = "java.net.*" %>
<%@ page import = "java.util.*" %>
<%@ page import = "qrcom.util.*" %>
<%@ page import = "qrcom.PROFIT.files.info.AduserInfo"%>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultCoySub" %>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultStore" %>
<%@ page import = "qrcom.PROFIT.files.dao.local.GV.VoucherReceivingDAOAMY" %>
<%@ page import = "qrcom.PROFIT.webbean.HTTPObj.*" %>
<%@ page import = "qrcom.PROFIT.webbean.language.*" %>
<%@ page import = "qrcom.PROFIT.system.*"%>

<jsp:useBean id="jbSession" scope="session" class="qrcom.PROFIT.servlet.HTTPSessionAttributeWrapper" />
<jsp:useBean id="jbWebSessionParam" scope="session" class="qrcom.PROFIT.webbean.HTTPObj.WebSessionParam" />
<jsp:useBean id="webCurrConverter" scope="session" class="qrcom.util.CurrencyConverter" />

<%
	WResGUI jbWResGUI 					= jbWebSessionParam.getWResGUI(request);
	WResPrompt jbWResPrompt 			= jbWebSessionParam.getWResPrompt(request);
	WebAuthorization webAuth 			= new WebAuthorization(request);
	AduserInfo aduserInfo				= (AduserInfo)request.getSession().getAttribute("aduserInfo");
	CoysubmstComboBox coysubmstComboBox = new CoysubmstComboBox(request);
	DefaultCoySub defaultCoySub			= new DefaultCoySub(aduserInfo);
	DefaultStore defaultStore 			= new DefaultStore(aduserInfo);
	
	String BaseURL 				= SysParam.getInstance().getBaseURL();
	String lang_code 			= jbWebSessionParam.getAduserInfo().USR_LANGUAGE();
	String user_id 				= jbWebSessionParam.getAduserInfo().USR_ID();
	String strCoy 				= jbWebSessionParam.getAduserInfo().USR_COMP();
	String SYSStoreLength 		= jbWebSessionParam.getProfitvvValue(request, "SYSStoreLength");
	String SYSGVNoLength 		= jbWebSessionParam.getProfitvvValue(request, "SYSGVNoLength");

	HParam hParam 				= new HParam();
	int i 						= 0;
	boolean printAllOK 			= false;
	boolean printAllnoRec 		= true;
	int totalRow				= 0;
	int selectedloop			= 0;
	String strURL           	= "";
    String strCOUNT         	= "";
	String temp 				= "";
	String strTitle 			= "";
	String curr_page_			= "1";
	
	String strAction 			= request.getParameter("ACTION"); 
	String strCoySub 			= request.getParameter("KEY1");
	String strStoreFrom 		= request.getParameter("KEY2");
	String strXFRNo 			= request.getParameter("KEY3");
	String strStoreTo 			= request.getParameter("KEY4");
	String strGVNo 				= request.getParameter("KEY5");
	String strRecvBy 			= request.getParameter("KEY6");
	String strDateRecv 			= request.getParameter("KEY7");
	String strUpdateBy 			= request.getParameter("KEY8");
	String strUpdateDate 		= request.getParameter("KEY9");
	String strStatus 			= request.getParameter("KEY10");
	String strReferenceNo		= request.getParameter("KEY11");
	String strPOPUP		 		= request.getParameter("KEY12");
	String strPrintFlag		 	= request.getParameter("PRINT_FLAG");
	String strPrintAll			= request.getParameter("PRINT_ALL");
    String defaultCOY_SUB		= defaultCoySub.getDEFAULT_COY_SUB();
	String defaultStr			= defaultStore.getDefaultStore();
	String strSelectMode 		= request.getParameter("SELECT_MODE");
	
	if(!strAction.equals("reset") && !strAction.equals("INIT"))
	{
		if(request.getParameter("cboSubCoy") !=null && !request.getParameter("cboSubCoy").equals(""))
		{
		   strCoySub = request.getParameter( "cboSubCoy" );
		}
		if(request.getParameter("txtStoreFrom") != null && !request.getParameter("txtStoreFrom").equals(""))
		{
			strStoreFrom = request.getParameter("txtStoreFrom");
		}
		if(request.getParameter("txtXFRNo") != null && !request.getParameter("txtXFRNo").equals(""))
		{
			strXFRNo = request.getParameter("txtXFRNo");
		}
		if(request.getParameter("txtStoreTo") != null && !request.getParameter("txtStoreTo").equals(""))
		{
			strStoreTo = request.getParameter("txtStoreTo");
		}
		if(request.getParameter("txtGVNo") != null && !request.getParameter("txtGVNo").equals(""))
		{
			strGVNo = request.getParameter("txtGVNo");
		}
		if(request.getParameter("txtRecvBy") != null && !request.getParameter("txtRecvBy").equals(""))
		{
			strRecvBy = request.getParameter("txtRecvBy");
		}
		if(request.getParameter("txtDateRecv") != null && !request.getParameter("txtDateRecv").equals(""))
		{
			strDateRecv = request.getParameter("txtDateRecv");
		}
		if(request.getParameter("txtUpdateBy") != null && !request.getParameter("txtUpdateBy").equals(""))
		{
			strUpdateBy = request.getParameter("txtUpdateBy");
		}
		if(request.getParameter("txtUpdateDate") != null && !request.getParameter("txtUpdateDate").equals(""))
		{
			strUpdateDate = request.getParameter("txtUpdateDate");
		}
		if(request.getParameter("cboStatus") != null && !request.getParameter("cboStatus").equals(""))
		{
			strStatus = request.getParameter("cboStatus");
		}
		if(request.getParameter("txtReferenceNo") != null && !request.getParameter("txtReferenceNo").equals(""))
		{
			strReferenceNo = request.getParameter("txtReferenceNo");
		}
	}
	else
	{
		strPOPUP = "N";
		strPrintFlag = "N";
		strPrintAll = "N";
		strStoreTo = defaultStr;
		strRecvBy = user_id;
		strDateRecv = qrMisc.getSqlSysDate().toString();
	}
	
	if(strPOPUP == null || strPOPUP.equals("")) strPOPUP = "N";
	
	if(strPOPUP.equals("Y")){
		strTitle = "Search";
		strSelectMode = "";
	}else{
		strTitle = "Print";
		strSelectMode = "multi";
	}
	
	/*
	 *	Set parameter for printing
	 */
	if (strPrintFlag!=null && strPrintFlag.equals("Y") && strPrintAll!=null && strPrintAll.equals("N"))
	{
		totalRow = Integer.parseInt(request.getParameter("txtHiddenCount"));
		strURL   = "&PRINT_TYPE=selected";
		
		for (int a = 1 ; a <= totalRow ; a++)
		{
			temp = request.getParameter("chkAction_"+a);
			
			if (temp != null && temp.equals("on"))
			{
				selectedloop++;
                strCOUNT 		 = String.valueOf(selectedloop);
				
				strURL += "&COY_" + strCOUNT + "=" + request.getParameter("rptCOY_" + a);
                strURL += "&COY_SUB_" + strCOUNT + "=" + request.getParameter("rptCOY_SUB_" + a);
                strURL += "&STORE_FROM_" + strCOUNT + "=" + request.getParameter("rptSTORE_FROM_" + a) ;
                strURL += "&XFR_NO_" + strCOUNT + "=" + request.getParameter("rptXFR_NO_" + a);
                strURL += "&STORE_TO_" + strCOUNT + "=" + request.getParameter("rptSTORE_TO_" + a) ;
                strURL += "&RECV_BY_" + strCOUNT + "=" + request.getParameter("rptRECV_BY_" + a) ;
                strURL += "&DATE_RECV_" + strCOUNT + "=" + request.getParameter("rptDATE_RECV_" + a);
                strURL += "&UPDATE_BY_" + strCOUNT + "=" + request.getParameter("rptUPDATE_BY_" + a) ;
                strURL += "&UPDATE_DATE_" + strCOUNT + "=" + request.getParameter("rptUPDATE_DATE_" + a);
				strURL += "&REFERENCE_NO_" + strCOUNT + "=" + request.getParameter("rptREFERENCE_NO_" + a);
			}
		}
	}
	else if (strPrintFlag!=null && strPrintFlag.equals("Y") && strPrintAll!=null && strPrintAll.equals("Y"))
	{
		strURL  = "&PRINT_TYPE=all" 
				+ "&COY=" + strCoy
				+ "&COY_SUB=" + strCoySub
				+ "&STORE_FROM=" + strStoreFrom
				+ "&XFR_NO=" + strXFRNo 	
				+ "&STORE_TO=" + strStoreTo 	
				+ "&GV_NO=" + strGVNo 	
				+ "&RECEIVED_BY=" + strRecvBy 	
				+ "&DATE_RECEIVED=" + strDateRecv 
				+ "&UPDATE_BY=" + strUpdateBy 
				+ "&UPDATE_DATE=" + strUpdateDate
				+ "&STATUS=" + strStatus
				+ "&REFERENCE_NO=" + strReferenceNo;
	}
	
	if (strAction != null){
            hParam.setActionCode(strAction);
    }else{
            strAction = ""; 
	}	
	if(strCoySub != null && !strCoySub.trim().equals("")){
		hParam.put("COY_SUB", strCoySub);
	}else{
		strCoySub = "";
	}
	if(strStoreFrom != null && !strStoreFrom.trim().equals("")){
		hParam.put("STORE_FROM", strStoreFrom);
	}else{
		strStoreFrom = "";
	}
	if(strXFRNo != null && !strXFRNo.trim().equals("")){
		hParam.put("XFR_NO", strXFRNo);
	}else{
		strXFRNo = "";
	}
	if(strStoreTo != null && !strStoreTo.trim().equals("")){
		hParam.put("STORE_TO", strStoreTo);
	}else{
		strStoreTo = "";
	}
	if(strGVNo != null && !strGVNo.trim().equals("")){
		hParam.put("GV_NO", strGVNo);
	}else{
		strGVNo = "";
	}
	if(strRecvBy != null && !strRecvBy.trim().equals("")){
		hParam.put("RECV_BY", strRecvBy);
	}else{
		strRecvBy = "";
	}
	if(strDateRecv != null && !strDateRecv.trim().equals("")){
		hParam.put("DATE_RECV", strDateRecv);
	}else{
		strDateRecv = "";
	}
	if(strUpdateBy != null && !strUpdateBy.trim().equals("")){
		hParam.put("UPDATE_BY", strUpdateBy);
	}else{
		strUpdateBy = "";
	}
	if(strUpdateDate != null && !strUpdateDate.trim().equals("")){
		hParam.put("UPDATE_DATE", strUpdateDate);
	}else{
		strUpdateDate = "";
	}
	if(strStatus != null && !strStatus.trim().equals("")){
		hParam.put("STATUS", strStatus);
	}else{
		strStatus = "";
	}
	if(strReferenceNo != null && !strReferenceNo.trim().equals("")){
		hParam.put("REFERENCE_NO", strReferenceNo);
	}else{
		strReferenceNo = "";
	}
	
	hParam.put("USER_ID", user_id);
	hParam.put("COY", strCoy);
	hParam.put("USR_LANGUAGE", lang_code);
	
	/*
	 *	Check printAllOK "PrintAll" is clicked
	 */
	if (strPrintFlag!=null && strPrintFlag.equals("Y") && strPrintAll!=null && strPrintAll.equals("Y"))
    {
		String strLinePerPage = jbWebSessionParam.getProfitvvValue(request, "SYSMaxLinesPerPage");
		
		VoucherReceivingDAOAMY voucherReceivingDAO 	= new VoucherReceivingDAOAMY();
		HTTPPageQueryBased httpPageQueryBased 		= new HTTPPageQueryBased(hParam, voucherReceivingDAO, strLinePerPage);
		Collection rsClltAll 						= httpPageQueryBased.getFirstPage();		
		
		jbSession.setAttribute("VoucherHttpPage", httpPageQueryBased);
		
		if (rsClltAll != null)
		{
			selectedloop 	= 0;
			printAllnoRec 	= false;
			printAllOK 		= true;
			Iterator iter 	= rsClltAll.iterator();
			
			String [] str;
			
			while (iter.hasNext())
			{

				selectedloop++;
				str = (String [])iter.next();
				
				if(str[7]!=null && str[7].equals("U"))
				{}
				else
				{
					printAllOK = false;
				}
			}
		}
	}
	
	Collection err = (Collection)request.getSession().getAttribute("errorSets");
    if (err != null && !err.isEmpty())
    {
%>
	<script>
		window.open("<%=BaseURL%>/profit/Common/Error.jsp", "", "height=300, width=500, resizable=yes, scrollbars=yes");
	</script>
<%
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<title><%=jbWResGUI.getRes("Voucher Receiving Inquiry")%></title>
		<jsp:include page="../Common/Header.jsp"/>
		<jsp:include page="../Common/Script.jsp"/>
		<script>
			//PageControl.jsp needs this function.
			function processHTTPPage(strPageAction)
			{
				'use strict';
				var ACTION = strPageAction;
				var URLstr = '<%=BaseURL%>/profit/GV/VoucherReceivingInquiryAMY.jsp';

				if (strPageAction === 'getPage')
				{
					ACTION += '&PAGE=' + window.document.FORM.txtGotoPage.value;
				}

				window.document.FORM_SUBMIT.action = buildActionURL('FORM', URLstr, ACTION, 'cboSubCoy%txtStoreFrom%txtXFRNo%txtStoreTo%txtGVNo%txtRecvBy%txtDateRecv%txtUpdateBy%txtUpdateDate%cboStatus%txtReferenceNo');
				window.document.FORM_SUBMIT.submit();
			}
			
			//Return calendar value
			function restart()
			{ 
				'use strict';
				try
				{         
					if (whichOne == '1')
					document.FORM.elements['txtDateRecv'].value = '' + year + '-' + padout(month - 0 + 1) + '-' + padout(day);
					if (whichOne == '2')
					document.FORM.elements['txtUpdateDate'].value = '' + year + '-' + padout(month - 0 + 1) + '-' + padout(day);      
				
					mywindow.close();
				}
				catch(e)
				{ 
					alert('<%=jbWResGUI.getRes("Please Try Again")%>');
				}      
			}
			
			function SearchCriteria(PATH, search_field)
			{
				'use strict';
				if(search_field === 'txtGVNo')
				{
					PATH = '<%=BaseURL%>/profit/GV/VoucherSearchAMY.jsp?&ACTION=search&KEY1='+window.document.FORM.cboSubCoy.value+
					'&KEY18=single';
				}
				window.document.FORM.SEARCH_FIELD.value = search_field;
				window.open(PATH,'null','height=600,width=800,left=0, top=0, status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes');
			}
			
			function returnValue(strReturn)
			{
				'use strict';
				var search_field = window.document.FORM.SEARCH_FIELD.value; 
				
				if ( search_field === 'txtStoreFrom')
				{
					window.document.FORM.txtStoreFrom.value = getValue('STORE', strReturn);
				}      
				else if ( search_field === 'txtStoreTo')
				{
					window.document.FORM.txtStoreTo.value = getValue('STORE', strReturn);
				}
				else if (search_field === 'txtGVNo')
				{
					window.document.FORM.txtGVNo.value = getValue('GV_NO', strReturn);
				}
				else if (search_field === 'txtRecvBy')
				{
					window.document.FORM.txtRecvBy.value = getValue('USR_ID', strReturn);
				}
				else if (search_field === 'txtUpdateBy')
				{
					window.document.FORM.txtUpdateBy.value = getValue('USR_ID', strReturn);
				}     
			}
			
			function checkStore(search_field) 
			{
				'use strict';
				window.document.FORM.SEARCH_FIELD.value = search_field; 
				
				if(window.document.FORM.cboSubCoy.value.length === 0) 
				{
					alert('<%=jbWResPrompt.getRes("NO_COY_SUB")%>');
					
					window.document.FORM.elements[search_field].value = '';
					window.document.FORM.elements[search_field].focus();
					
					return;
				}
				
				window.document.FORM.elements[search_field].value = leftFillZero(window.document.FORM.elements[search_field].value, '<%=SYSStoreLength%>');
				
				var TargetClass = 'qrcom.PROFIT.webbean.dao.MT.DefaultStrmstWebBean';
				var TargetMethod = 'verifyStoreByCoySubUserID';
				var CallbackMethod = 'verifyStoreCalledBack';
				
				var Params = '';
				Params = Params + '&STORE=' + window.document.FORM.elements[search_field].value;
				Params = Params + '&COY=' + window.document.FORM.txtCoy.value;
				Params = Params + '&COY_SUB=' + window.document.FORM.cboSubCoy.value;
				Params = Params + '&USR_ID=' + '<%=aduserInfo.USR_ID()%>';
									
				var baseURL = '<%=BaseURL%>';
				invokeTargetClass(baseURL, TargetClass, TargetMethod, CallbackMethod, Params);
			}
			
			function verifyStoreCalledBack(str) 
			{
				'use strict';
				var search_field = window.document.FORM.SEARCH_FIELD.value; 
				var msg='';
			  
				if(getValue('@STATUS', str) !== 'Y') 
				{
					if(getValue('invalid_msg', str).length>0) 
					{
						msg = 'Store code ' + getValue('invalid_msg', str);
					}
					else 
					{
						msg = '<%=jbWResPrompt.getRes("UNKNOWN_EXP")%>';
					}
					alert(msg);
					
					window.document.FORM.elements[search_field].value = '';
					window.document.FORM.elements[search_field].focus();
				}
				else 
				{
					if(getValue('STORE_DEL_CD', str) === 'Y') 
					{
						msg = 'Store code ' + '<%=jbWResPrompt.getRes("FLAG_DELETED")%>';
						alert(msg);
	
						window.document.FORM.elements[search_field].value = '';
						window.document.FORM.elements[search_field].focus();
					}
					else if(getValue('STORE_COY_SUB', str) !== window.document.FORM.cboSubCoy.value) 
					{
						msg = 'Store code ' + '<%=jbWResPrompt.getRes("NOTBELONG")%>' + ' <%=jbWResGUI.getRes("Company Subsidiary")%>';
						alert(msg);
						
						window.document.FORM.elements[search_field].value = '';
						window.document.FORM.elements[search_field].focus();
					}
				}
			}
			
			function onChgVoucherNo(fieldName)
			{
				'use strict';
				window.document.FORM.SEARCH_FIELD.value = fieldName;
				
				if(window.document.FORM.elements[fieldName].value === '')
				{
					return false;
				}
				else if(window.document.FORM.cboSubCoy.value === '')
				{
					window.document.FORM.elements[fieldName].value='';
					alert('Please select Company Code.');
					window.document.FORM.cboSubCoy.focus();
					return false;
				}
					
				var TargetClass = 'qrcom.PROFIT.webbean.dao.GV.GvmstrWebbeanAMY';
				var TargetMethod = 'verifyGVNo';
				var CallbackMethod = 'onChgVoucherNoCalledBack';
				
				var Params;
				Params = '&COY=' + window.document.FORM.txtCoy.value + 
						'&COY_SUB='+ window.document.FORM.cboSubCoy.value + 
						'&GV_NO='+ window.document.FORM.elements[fieldName].value;
				var baseURL = '<%=BaseURL%>';
				invokeTargetClass(baseURL, TargetClass, TargetMethod, CallbackMethod, Params);
			}
			
			function onChgVoucherNoCalledBack(strReturn)
			{
				'use strict';
				var ret_STATUS = getValue('@STATUS', strReturn);
				var strStatus = getValue('STATUS', strReturn);
				if(ret_STATUS === 'N')
				{
					alert('<%=jbWResPrompt.getRes("NE_REC")%>');
					var fieldName = window.document.FORM.SEARCH_FIELD.value;
					window.document.FORM.elements[fieldName].value = '';
					window.document.FORM.elements[fieldName].focus();
					return false;
				}
			}
			
			function viewReceiving(total_rec)
			{
				'use strict';
				if(!Validate(document.FORM, 'view', total_rec,'Y'))
				{
					return;
				}
				
				var COY_SUB 	 = window.document.FORM.cboSubCoy.value;
				var STORE_FROM 	 = '';
				var XFR_NO 		 = '';
				var STORE_TO 	 = '';
				
				for(var i=1; i <= total_rec; i++) 
				{
					var ChkObj = document.FORM.elements['chkAction_' + i];
					if(ChkObj.checked) 
					{
						STORE_FROM = document.FORM.elements['txtStoreFrom_' + i].value;
						XFR_NO = document.FORM.elements['txtXFRNo_' + i].value;
						STORE_TO = document.FORM.elements['txtStoreTo_' + i].value;
					}
				}
				
				var n = new Date().getTime();
				window.document.FORM.target = 'viewPromptWindow' + n; 
				window.open('','viewPromptWindow' + n,'height=600,width=800,left=0, top=0, status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes');
				
				var FORMaction = '<%=BaseURL%>/servlet/SvltVoucherReceivingAMY?ACTION=view&KEY1='+COY_SUB+'&KEY2='+STORE_FROM+'&KEY3='+XFR_NO+'&KEY4='+STORE_TO;
				window.document.FORM.action = FORMaction;
				window.document.FORM.submit();
				window.document.FORM.target = '_self';      
			}
			
			function doDelete()
			{
				'use strict';
				var cnt    		= document.FORM.txtHiddenCount.value;
				var select 		= 0;
				var tmpSelected = 0;
				var tmpErrMsg	= '';
				
				var COY 		 = window.document.FORM.txtCoy.value;
				var COY_SUB 	 = window.document.FORM.cboSubCoy.value;
				var STORE_FROM 	 = '';
				var XFR_NO 		 = '';
				var STORE_TO 	 = '';
				var DATE_RECV	 = '';
				var RECV_BY		 = '';
				
				for (var i = 1 ; i <= cnt ; i++)
				{
					if (document.FORM.elements['chkAction_' + i].checked)
					{
						if(document.FORM.elements['txtStatus_' + i].value !== 'Checked')
						{
							tmpErrMsg = 'Only Checked status item can be deleted';
						}
						else
						{
							tmpSelected = i;
						}
						select++;
					}
				}
				
				if (select === 0)
				{
					alert('<%=jbWResPrompt.getRes("Please select at least one action to delete")%>');
					return false;
				}
				else if(select > 1)
				{
					alert('<%=jbWResPrompt.getRes("Please select one action only to delete")%>');
					return false;
				}
					
				if(confirm('<%=jbWResPrompt.getRes("Are you sure?")%>'))
				{
					if(tmpErrMsg !== '')
					{
						alert(tmpErrMsg);
						return false;
					}
					
					STORE_FROM 	= document.FORM.elements['txtStoreFrom_' + tmpSelected].value;
					XFR_NO 		= document.FORM.elements['txtXFRNo_' + tmpSelected].value;
					STORE_TO 	= document.FORM.elements['txtStoreTo_' + tmpSelected].value;
					DATE_RECV 	= document.FORM.elements['txtDateRecv_' + tmpSelected].value;
					RECV_BY 	= document.FORM.elements['txtRecvBy_' + tmpSelected].value;
								
					disableAllButton();
					
					document.FORM.PRINT_FLAG.value = 'N';
					document.FORM.PRINT_ALL.value = 'N';
					
					var param = '&COY='			+COY;
						param += '&COY_SUB='	+COY_SUB;
						param += '&STORE_FROM='	+STORE_FROM;
						param += '&XFR_NO='		+XFR_NO;
						param += '&STORE_TO='	+STORE_TO;
						param += '&DATE_RECV='	+DATE_RECV;
						param += '&RECV_BY='	+RECV_BY;
						
					var FORMaction = '<%=BaseURL%>/servlet/SvltVoucherReceivingAMY?ACTION=delete'+param;
					
					window.document.FORM.action = FORMaction;
					window.document.FORM.submit();
				}
			}
			
			function doSearch()
			{
				if(Validate(window.document.FORM,'search','','Y'))
				{ 
					disableAllButton(); 
					setFormSubmit('<%=BaseURL%>/profit/GV/VoucherReceivingInquiryAMY.jsp','S','cboSubCoy%txtStoreFrom%txtXFRNo%txtStoreTo%txtGVNo%txtRecvBy%txtDateRecv%txtUpdateBy%txtUpdateDate%cboStatus%txtReferenceNo%POPUP');
				}
			}
			
			function print()
			{
				'use strict';
				var cnt    = document.FORM.txtHiddenCount.value;
				var select = 0;
				
				for (var i = 1 ; i <= cnt ; i++)
				{
					if (document.FORM.elements['chkAction_' + i].checked)
					{
						if(document.FORM.elements['rptSTATUS_' + i].value !== 'Updated')
						{
							alert('Only Transfer In record with updated status can be printed.');
							return false;
						}
						
						if(document.FORM.elements['rptREFERENCE_NO_' + i].value !== '')
						{
							alert('The selected record is Transfer In transaction, created through Upload Voucher Not Received (Closed Transfer Out), no report Printed.');
							return false;
						}
						select++;
					}
				}
				
				if (select == 0)
				{
					alert('<%=jbWResPrompt.getRes("Please select at least one action")%>');
					return false;
				}
				
				disableAllButton();
				
				document.FORM.PRINT_FLAG.value = 'Y';
				document.FORM.PRINT_ALL.value = 'N';
				document.FORM.action = '<%=BaseURL%>/profit/GV/VoucherReceivingInquiryAMY.jsp?ACTION=search&PrintFlag=N&PrintAll=N';
				document.FORM.submit();
			}
			
			function printAll()
			{
				'use strict';
				var cnt    = document.FORM.txtHiddenCount.value;
				
				if (cnt == 0)
				{
					alert("<%=jbWResPrompt.getRes("No transfer out detail for printing")%>");
					return false;
				}
				
				var s = document.FORM.cboStatus.value;
				if(s === '') {
					document.FORM.cboStatus.value = 'U';
					s = 'U';
				}
				
				if(s !== 'U'){
					alert('Only Transfer In record with updated status can be printed.');
				}else {
				
					//if(s=="")document.FORM.cboStatus.value = "U";
					disableAllButton();
						
					document.FORM.PRINT_FLAG.value = 'Y';
					document.FORM.PRINT_ALL.value = 'Y';
					document.FORM.action = '<%=BaseURL%>/profit/GV/VoucherReceivingInquiryAMY.jsp?ACTION=search&PrintFlag=Y&PrintAll=Y';

					document.FORM.submit();

				}
			}
			
			function checkAll()
			{
				'use strict';
				var count = window.document.FORM.txtHiddenCount.value;
				
				if (count !== 0)
				{
					for(var i=1; i<=count; i++)
					{
						var chkObj = eval('window.document.FORM.chkAction_' + i);  
						chkObj.checked=true; 
					}
				}
			}
			
			function uncheckAll()
			{
				'use strict';
				var count = window.document.FORM.txtHiddenCount.value;
				
				if (count !== 0)
				{
					for(var i=1; i<=count; i++)
					{
						var chkObj = eval('window.document.FORM.chkAction_' + i);
						chkObj.checked=false; 
					}
				}
			}
			
			function enableAllButton()
			{
				'use strict';
				alert('a');
				window.document.FORM.cmdReset.disabled = false;
				window.document.FORM.cmdSearch.disabled = false;
				window.document.FORM.cmdCheckAll.disabled = false;
				window.document.FORM.cmdUnCheckAll.disabled = false;
				window.document.FORM.cmdPrint.disabled = false;
				window.document.FORM.cmdPrintAll.disabled = false;
				window.document.FORM.cmdView.disabled = false;
				window.document.FORM.cmdDelete.disabled = false;
			}
			
			function disableAllButton()
			{
				'use strict';
				window.document.FORM.cmdReset.disabled = true;
				window.document.FORM.cmdSearch.disabled = true;
				window.document.FORM.cmdCheckAll.disabled = true;
				window.document.FORM.cmdUnCheckAll.disabled = true;
				window.document.FORM.cmdPrint.disabled = true;
				window.document.FORM.cmdPrintAll.disabled = true;
				window.document.FORM.cmdView.disabled = true;
				window.document.FORM.cmdDelete.disabled = true;
			}
		</script>
	</head>
	<body>
	<center>
		<FORM id="FORM" method="post" action="<%=BaseURL%>/profit/GV/VoucherReceivingInquiryAMY.jsp" name="FORM">
			<fieldset class="fieldsettitle"><legend><%=jbWResGUI.getRes("Voucher Receiving Inquiry")%></legend>
				<table width="100%" cellpadding="5" cellspacing="0" border="0" class="table-border">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="0" cellspacing="1" class="table-border">
								<tr>
									<td>
										<!-- SEARCH CRITERIA -->
										<table border="0" width="100%" cellspacing="0" cellpadding="3">
											<tr><th><%=jbWResGUI.getRes("Search Criteria")%></th></tr>
											<tr>
												<td>
													<table border="0" width="100%" align="center" cellspacing="1" cellpadding="3">
														<tr>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Company")%></td>
															<td width="5%"><input type="text" name="txtCoy" value="<%=strCoy%>" readonly tabindex="-1" class="input-display"/></td>
															<td width="20%">
																<input type="text" name="txtCoyName" value="<%=HTTPUtilClass.getDesc(lang_code, "COY_NAME", "COYMST", " WHERE COY = '" + strCoy + "'")%>" readonly tabindex="-1" class="input-display"/>
															</td>
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
														   <td width="20%" class="caption"><%= jbWResGUI.getRes("Issuing Store")%></td>
														   <td colspan="2"><input type="text" name="txtStoreFrom" value="<%=strStoreFrom%>" maxlength="<%=SYSStoreLength%>" onchange=""></td>
														   <td width="5%"><img src="<%=BaseURL%>/profit/images/search.gif" name="imgStoreFrom" ALT="<%=jbWResGUI.getRes("Find")%>" onclick="SearchCriteria('<%=BaseURL%>/profit/MT/StrmstSearch.jsp?ACTION=search&FILTER1=STORE_DEL_CD!=\'Y\'', 'txtStoreFrom')" style='cursor:hand;'></td>
														   <td width="20%" class="caption"><%= jbWResGUI.getRes("Transfer No")%></td>
														   <td width="25%"><input type="text" name="txtXFRNo" value="<%=strXFRNo%>"></td>
														   <td width="5%"></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Receiving Store")%></td>
															<td colspan="2"><input type="text" name="txtStoreTo" value="<%=strStoreTo%>" maxlength="<%=SYSStoreLength%>" onchange="if(this.value!='')checkStore('txtStoreTo');"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/search.gif" name="imgStoreTo" ALT="<%=jbWResGUI.getRes("Find")%>" onclick="SearchCriteria('<%=BaseURL%>/profit/MT/StrmstSearch.jsp?ACTION=search&FILTER1=STORE_DEL_CD!=\'Y\'', 'txtStoreTo')" style='cursor:hand;'></td>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Voucher Number")%></td>
															<td width="25%"><input type="text" name="txtGVNo" value="<%=strGVNo%>" maxlength="<%=SYSGVNoLength%>" onchange="onChgVoucherNo('txtGVNo');"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/search.gif" name="imgGVNo" ALT="<%=jbWResGUI.getRes("Find")%>" onclick="SearchCriteria('<%=BaseURL%>/profit/GV/VoucherSearchAMY.jsp', 'txtGVNo')" style='cursor:hand;'></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Received By")%></td>
															<td colspan="2"><input type="text" name="txtRecvBy" value="<%=strRecvBy%>"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/search.gif" name="imgRecvBy" ALT="<%=jbWResGUI.getRes("Find")%>" onclick="SearchCriteria('<%=BaseURL%>/profit/AD/AduserSearch.jsp?ACTION=search', 'txtRecvBy')" style='cursor:hand;'></td>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Date Received")%></td>
															<td width="25%"><input type="text" name="txtDateRecv" value="<%=strDateRecv%>" onchange="this.value=formatDate(this.value)"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/calendr.gif" ALT="<%=jbWResGUI.getRes("Calendar")%>" onclick="newWindow(1)" style="cursor:hand"></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Updated By")%></td>
															<td colspan="2"><input type="text" name="txtUpdateBy" value="<%=strUpdateBy%>"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/search.gif" name="imgUpdateBy" ALT="<%=jbWResGUI.getRes("Find")%>" onclick="SearchCriteria('<%=BaseURL%>/profit/AD/AduserSearch.jsp?ACTION=search', 'txtUpdateBy')" style='cursor:hand;'></td>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Date Updated")%></td>
															<td width="25%"><input type="text" name="txtUpdateDate" value="<%=strUpdateDate%>" onchange="this.value=formatDate(this.value)"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/calendr.gif" ALT="<%=jbWResGUI.getRes("Calendar")%>" onclick="newWindow(2)" style="cursor:hand"></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Status")%></td>
															<td colspan="2">
																<select name="cboStatus">
																<%
																	out.print(MTComboBox.G_ComboBox(strStatus,
																	" -" + jbWResGUI.getRes("")+ "|" +
																	"C-" + jbWResGUI.getRes("Checked") + "|" +
																	"U-" + jbWResGUI.getRes("Updated") + "|" +
																	"D-" + jbWResGUI.getRes("Deleted") ));
																%>
																</select>                   
															</td>
															<td width="5%"></td>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Reference No")%></td>
															<td width="25%"><input type="text" name="txtReferenceNo" value="<%=strReferenceNo%>" onblur="if(this.value != ''){if(!isNumber(this.value)){ alert('Please insert only numeric value'); this.value='';}}"/></td>
															<td width="5%"></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<!-- BUTTONS -->
													<table width="100%">
														<tr>
															<td width="70%">
															</td>
															<td width="15%" align="right"><input type="reset" name="cmdReset" value="<%=jbWResGUI.getRes("Reset")%>" onclick="setFormReset(this.form,'<%=BaseURL%>/profit/GV/VoucherReceivingInquiryAMY.jsp','')"/></td> 
															<td width="15%"><input type="button" name="cmdSearch" value="<%=jbWResGUI.getRes("Search")%>" onclick="doSearch()"/></td>  
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<!-- SEARCH RESULT -->
							<table border="0" width="100%" cellspacing="1" cellpadding="3" class="table-border">
								<tr><th><%=jbWResGUI.getRes("Search Result")%></th></tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table-border">
											<tr>
												<td width="5%" class="tb-display"><%=jbWResGUI.getRes("Action")%></td>
												<td width="8%" class="tb-display"><%=jbWResGUI.getRes("Issuing Store")%></td>
												<td width="8%" class="tb-display"><%=jbWResGUI.getRes("Receiving Store")%></td>
												<td width="7%" class="tb-display"><%=jbWResGUI.getRes("Transfer No")%></td>
												<td width="7%" class="tb-display"><%=jbWResGUI.getRes("Total Voucher (PCS)")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Total Voucher Amount ")%>(<%=jbWResGUI.getRes("RM")%>)</td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Received By")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Date Received")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Updated By")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Date Updated")%></td>
												<td width="8%" class="tb-display"><%=jbWResGUI.getRes("Reference No")%></td>
												<td width="7%" class="tb-display"><%=jbWResGUI.getRes("Status")%></td>
											</tr>
											<%
												Collection rsCllt = null;
												
												// PageControl.jsp needs this string variable.
												String strPageOfTotal = "";
												
												String strTdBgColor  = "tb-display";
												String strInputColor = "input-display";
												
												if (strAction != null && strAction.equals("search"))
												{
													String strLinePerPage = jbWebSessionParam.getProfitvvValue(request, "SYSMaxLinesPerPage");
													VoucherReceivingDAOAMY voucherReceivingDAO 	= new VoucherReceivingDAOAMY();
													HTTPPageQueryBased httpPageQueryBased = new HTTPPageQueryBased(hParam, voucherReceivingDAO, strLinePerPage);
													
													jbSession.setAttribute("VoucherHttpPage", httpPageQueryBased);
													
													rsCllt = httpPageQueryBased.getFirstPage();
													strPageOfTotal = httpPageQueryBased.getPageOfTotal();
												}
												else if (strAction != null && 
														(strAction.equals("getNextPage") ||
															strAction.equals("getPreviousPage") ||
															strAction.equals("getFirstPage") ||
															strAction.equals("getLastPage")))
												{
													HTTPPageQueryBased httpPageQueryBased = (HTTPPageQueryBased)jbSession.getAttribute("VoucherHttpPage");
													if(httpPageQueryBased != null)
													{
													   rsCllt = httpPageQueryBased.process(strAction);
													   strPageOfTotal = httpPageQueryBased.getPageOfTotal();
													}            
												}
												else if (strAction != null && strAction.equals("getPage"))
												{
													HTTPPageQueryBased httpPageQueryBased = (HTTPPageQueryBased)jbSession.getAttribute("VoucherHttpPage");
													if (httpPageQueryBased != null)
													{
														String str = request.getParameter("PAGE");
														rsCllt = httpPageQueryBased.getPage(str);
														strPageOfTotal = httpPageQueryBased.getPageOfTotal();
													}
												}
												else if(strAction != null && strAction.equals("reset"))
												{   
													jbSession.removeAttribute("VoucherHttpPage");
												}
												
												if(strPageOfTotal!= null && !strPageOfTotal.equals("")){
													String[] tmpPage = strPageOfTotal.split(" / ");
													curr_page_ = tmpPage[0];
												}
												
												
												if (rsCllt != null)
												{
													Iterator iter = rsCllt.iterator();
													String [] str;
													String strStatus1 = "";
													
													while (iter.hasNext())
													{
														i++;
														
														if (strTdBgColor == "tb-display") 
														{
															strTdBgColor  = "tb-display2";
															strInputColor = "input-display2";
														} 
														else 
														{
															strTdBgColor  = "tb-display";
															strInputColor = "input-display";
														}
														
														str = (String [])iter.next();
														
														if(str[0]==null) str[0]="";
														if(str[1]==null) str[1]="";
														if(str[2]==null) str[2]="";
														if(str[3]==null) str[3]=""; 
														if(str[4]==null) str[4]="";
														if(str[5]==null) str[5]="";
														if(str[6]==null) str[6]="";
														if(str[7]==null) str[7]="";
														if(str[10]==null || str[10].equals("0")) str[10]="";
														if(str[11]==null) str[11]="0";
														
														if(str[7].equals("C")){
															str[7] = "Checked";
														}else if(str[7].equals("U")){
															str[7] = "Updated";
														}else if(str[7].equals("N")){
															str[7] = "Not Received";
														}else if(str[7].equals("D")){
															str[7] = "Deleted";
														}
											%>
											<tr>
												<td class="<%=strTdBgColor%>" align="center" ><input type="checkbox" name="chkAction_<%=i%>" onClick="if(checkCurrentAction(SELECT_MODE.value))selectCurrentAction(txtHiddenCount.value, this, 'window.document.FORM.chkAction_')"></td> 
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtStoreFrom_<%=i%>" value="<%=str[0]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtStoreTo_<%=i%>" value="<%=str[1]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtXFRNo_<%=i%>" value="<%=str[2]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtTOT_PCS_<%=i%>" value="<%=str[12]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtTOT_AMT_<%=i%>" value="<%=webCurrConverter.format(str[11], 2)%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:right"/></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtRecvBy_<%=i%>" value="<%=str[3]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtDateRecv_<%=i%>" value="<%=qrMisc.discardTime(str[4])%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtUpdateBy_<%=i%>" value="<%=str[5]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtUpdateDate_<%=i%>" value="<%=qrMisc.discardTime(str[6])%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtUpdateDate_<%=i%>" value="<%=str[10]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtStatus_<%=i%>" value="<%=str[7]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
												
												<!-- // For Report Printing Parameters . -->
												<input type="hidden" name="rptCOY_<%=i%>" 			value="<%=str[8]%>"/>
												<input type="hidden" name="rptCOY_SUB_<%=i%>" 		value="<%=str[9]%>"/>
												<input type="hidden" name="rptSTORE_FROM_<%=i%>" 	value="<%=str[0]%>"/>
												<input type="hidden" name="rptXFR_NO_<%=i%>" 		value="<%=str[2]%>"/>
												<input type="hidden" name="rptSTORE_TO_<%=i%>"		value="<%=str[1]%>"/>
												<input type="hidden" name="rptRECV_BY_<%=i%>" 		value="<%=str[3]%>"/>
												<input type="hidden" name="rptDATE_RECV_<%=i%>" 	value="<%=str[4]%>"/>
												<input type="hidden" name="rptUPDATE_BY_<%=i%>" 	value="<%=str[5]%>"/>
												<input type="hidden" name="rptUPDATE_DATE_<%=i%>" 	value="<%=str[6]%>"/>
												<input type="hidden" name="rptREFERENCE_NO_<%=i%>" 	value="<%=str[10]%>"/>
												<input type="hidden" name="rptSTATUS_<%=i%>" 		value="<%=str[7]%>"/>
											</tr>
											<%
													} //while
												} //if
											%>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<%@ include file="../Common/PageControl.jsp" %>
						</td>
					</tr>
					<tr>
						<td>
							<script>
							if (!window.opener)
							{
								document.writeln('<table BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="1">');
								document.writeln('<tr>');
								document.writeln('<% if (webAuth.isEditAllowed()) { %>');
								document.writeln('<td width="12%"><input name="cmdCheckAll" type="button" value="<%=jbWResGUI.getRes("Check All")%>" onclick="checkAll()"/></td>');
								document.writeln('<td width="12%"><input name="cmdUnCheckAll" type="button" value="<%=jbWResGUI.getRes("UnCheck All")%>" onclick="uncheckAll()"/></td>');
								document.writeln('<td width="28%"></td>');
								document.writeln('<td width="12%"><input name="cmdDelete" type="button" value="<%=jbWResGUI.getRes("Delete")%>" align="right" onclick="doDelete()"/></td>');
								document.writeln('<td width="12%"><input name="cmdPrint" type="button" value="<%=jbWResGUI.getRes("Print All")%>" align="right" onclick="printAll()"/></td>');
								document.writeln('<td width="12%"><input name="cmdPrintAll" type="button" value="<%=jbWResGUI.getRes("Print")%>" align="right" onclick="print()"/></td>');
								document.writeln('<td width="12%"><input name="cmdView" type="button" value="<%=jbWResGUI.getRes("View")%>" align="right" onclick="viewReceiving(<%=i%>)"/></td>');
								document.writeln('<% }else { %>');
								document.writeln('<td width="100%"></td>');
								document.writeln('<% }%>');
								document.writeln('</tr>');
								document.writeln('</table>');
							}
							</script>
						</td>
					</tr>
				</table>
				<input type="hidden" name="ACTION" id="ACTION"/>
				<input type="hidden" name="SEARCH_FIELD" id="SEARCH_FIELD"/>
				<input type="hidden" name="txtHiddenCount" value="<%=i%>"/>
				<input type="hidden" name="SELECT_MODE" value="<%=strSelectMode%>"/>
				<input type="hidden" name="PRINT_FLAG" value="<%=strPrintFlag%>"/>
				<input type="hidden" name="PRINT_ALL" value="<%=strPrintAll%>"/>
				<input type="hidden" name="POPUP" value="<%=strPOPUP%>"/>
				<script>
				<%
					if ((strPrintFlag!=null && strPrintFlag.equals("Y") && strPrintAll!=null && strPrintAll.equals("N")) ||
						(strPrintFlag!=null && strPrintFlag.equals("Y") && strPrintAll!=null && strPrintAll.equals("Y") && printAllOK))
					{
				%>
						var PAGE_ID   	= 'VoucherReceivingInquiryAMY';
						var TOTAL_ROW 	= <%=selectedloop%>;
						var strUrl  	= '<%=BaseURL%>/servlet/SvltRptProcessor';
						strUrl 			+= '?ACTION=print_to_cache&USER_ID=' + '<%=user_id%>'+ '&PAGE_ID=' + PAGE_ID;
						strUrl 			+= '&REPORT_TYPE=VOUCH_RECV';
						strUrl 			+= '<%=strURL%>';
						strUrl 			+= '&TOTAL_ROW=' + TOTAL_ROW;
						strUrl 			+= '&report_id=VoucherReceivingInquiryRptAMY';
						strUrl 			+= '&RPT_CLASSNAME=qrcom.PROFIT.reports.GV.VoucherReceivingInquiryRptAMY&DES_TYPE=file&DES_FORMAT=pdf';
						//alert(strUrl);
						window.open(strUrl, '', 'height=600, width=800, resizable=yes, scrollbars=yes');
				<% 	
					}
				%>
				</script>
			</fieldset>
		</FORM>
		<FORM id="FORM_SUBMIT" method="post" action="<%=BaseURL%>/profit/GV/VoucherReceivingInquiryAMY.jsp" name="FORM_SUBMIT"></FORM>
	</center>
	</body>
</html>