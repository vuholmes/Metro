<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import = "java.net.*" %>
<%@ page import = "java.util.*" %>
<%@ page import = "qrcom.util.*" %>
<%@ page import = "qrcom.PROFIT.files.info.AduserInfo"%>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultCoySub" %>
<%@ page import = "qrcom.PROFIT.files.dao.local.GV.VoucherIssuanceInquiryDAOAMY" %>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultStore" %>
<%@ page import = "qrcom.PROFIT.webbean.HTTPObj.*" %>
<%@ page import = "qrcom.PROFIT.webbean.language.*" %>
<%@ page import = "qrcom.PROFIT.system.*"%>
<%@ page import="java.util.StringTokenizer"%>

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
	DefaultStore defaultStore			= new DefaultStore(aduserInfo);
	
	String BaseURL 				= SysParam.getInstance().getBaseURL();
	String lang_code 			= jbWebSessionParam.getAduserInfo().USR_LANGUAGE();
	String user_id 				= jbWebSessionParam.getAduserInfo().USR_ID();
	String strCoy 				= jbWebSessionParam.getAduserInfo().USR_COMP();
	String SYSStoreLength 		= jbWebSessionParam.getProfitvvValue(request, "SYSStoreLength");
	String SYSGVNoLength 		= jbWebSessionParam.getProfitvvValue(request, "SYSGVNoLength");
	String SYSTelNoLength		= jbWebSessionParam.getProfitvvValue(request, "SYSTelNoLength");
	String SYSTXNoLength		= jbWebSessionParam.getProfitvvValue(request, "SYSTXNoLength");
	String SYSIssueGVSNLength	= jbWebSessionParam.getProfitvvValue(request, "SYSIssueGVSNLength");
	String SYSNameLength		= jbWebSessionParam.getProfitvvValue(request, "SYSNameLength");
	String SYSIssueGVIquiryPrc  = jbWebSessionParam.getProfitvvValue(request, "SYSIssueGVIquiryPrc");
	String SYSGvMbShipNoLength 	= jbWebSessionParam.getProfitvvValue(request, "SYSGvMbShipNoLength");
	
	HParam hParam 				= new HParam();
	int i 						= 0;
	int totalRow				= 0;
	int selectedloop			= 0;
	String strURL           	= "";
    String strCOUNT         	= "";
	String temp 				= "";
	String strTitle 			= "";
	
	String strAction 			= request.getParameter("ACTION"); 
	String strCoySub 			= request.getParameter("KEY1");
	String strStore		 		= request.getParameter("KEY2");
	String strPrcId 			= request.getParameter("KEY3");
	String strGvType			= request.getParameter("KEY4");
	String strSerialNo			= request.getParameter("KEY5");
	String strGvDeno			= request.getParameter("KEY6");
	String strGVNo				= request.getParameter("KEY7");
	String strPurchaser			= request.getParameter("KEY8");
	String strContactNo			= request.getParameter("KEY9");
	String strRsnCd				= request.getParameter("KEY10");
	String strDateIssue			= request.getParameter("KEY11");
	String strCreateBy			= request.getParameter("KEY12");
	String strDateCreate		= request.getParameter("KEY13");
	String strProcessType		= request.getParameter("KEY14");
	String strMembershipNo		= request.getParameter("KEY15");
	String strPOPUP		 		= request.getParameter("KEY16");
	String strPrintFlag		 	= request.getParameter("PRINT_FLAG");
	String strPrintCheck	 	= request.getParameter("PRINT_CHECK");
	String strSelectMode 		= request.getParameter("SELECT_MODE");
	String defaultCOY_SUB		= defaultCoySub.getDEFAULT_COY_SUB();
	
	if(!strAction.equals("reset"))
	{
		if(request.getParameter("cboSubCoy")!=null && !request.getParameter("cboSubCoy").equals(""))
		{
		   strCoySub = request.getParameter( "cboSubCoy" );
		}
		if(request.getParameter("txtStore")!=null && !request.getParameter("txtStore").equals(""))
		{
			strStore = request.getParameter("txtStore");
		}
		if(request.getParameter("txtPrcId")!=null && !request.getParameter("txtPrcId").equals(""))
		{
			strPrcId = request.getParameter("txtPrcId");
		}
		if(request.getParameter("cboGvType")!=null && !request.getParameter("cboGvType").equals(""))
		{
		   strGvType = request.getParameter( "cboGvType" );
		}
		if(request.getParameter("txtSerialNo")!=null && !request.getParameter("txtSerialNo").equals(""))
		{
			strSerialNo = request.getParameter("txtSerialNo");
		}
		if(!strAction.equals(""))
		{
			if(request.getParameter("cboGvDeno")!=null && !request.getParameter("cboGvDeno").equals(""))
			{
				strGvDeno = request.getParameter( "cboGvDeno" );
			}
			if(request.getParameter("txtGVNo")!=null && !request.getParameter("txtGVNo").equals(""))
			{
				strGVNo = request.getParameter( "txtGVNo" );
			}
		}
		if(request.getParameter("txtPurchaser")!=null && !request.getParameter("txtPurchaser").equals(""))
		{
			strPurchaser = request.getParameter("txtPurchaser");
		}
		if(request.getParameter("txtContactNo")!=null && !request.getParameter("txtContactNo").equals(""))
		{
			strContactNo = request.getParameter("txtContactNo");
		}
		if(request.getParameter("cboRsnCd")!=null && !request.getParameter("cboRsnCd").equals(""))
		{
			strRsnCd = request.getParameter("cboRsnCd");
		}
		if(request.getParameter("txtDateIssue")!=null && !request.getParameter("txtDateIssue").equals(""))
		{
			strDateIssue = request.getParameter("txtDateIssue");
		}
		if(request.getParameter("txtCreateBy")!=null && !request.getParameter("txtCreateBy").equals(""))
		{
			strCreateBy = request.getParameter("txtCreateBy");
		}
		if(request.getParameter("txtDateCreate")!=null && !request.getParameter("txtDateCreate").equals(""))
		{
			strDateCreate = request.getParameter("txtDateCreate");
		}
		if(request.getParameter("cboProcessType")!= null && !request.getParameter("cboProcessType").equals(""))
		{
			strProcessType = request.getParameter("cboProcessType");
		}
		if(request.getParameter("txtMembershipNo")!=null && !request.getParameter("txtMembershipNo").equals(""))
		{
			strMembershipNo = request.getParameter("txtMembershipNo");
		}
	}
	else
	{
		strPOPUP = "N";
		strPrintFlag = "N";
		strPrintCheck = "N";
	}
	
	if(strPOPUP == null || strPOPUP.equals("")) strPOPUP = "N";
	if(strPOPUP.equals("Y")){
		strTitle = "Search";
		strSelectMode = "";
	}else{
		strTitle = "Print";
		strSelectMode = "multi";
	}
	if(strStore == null || strStore.equals(""))
	{
		strStore = defaultStore.getDefaultStore();;
	}
	
	/*
	 *	Set parameter for printing
	 */
	if (strPrintFlag!=null && strPrintFlag.equals("Y"))
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
				strURL += "&STORE_" + strCOUNT + "=" + request.getParameter("rptSTORE_" + a);
				strURL += "&PRC_ID_" + strCOUNT + "=" + request.getParameter("rptPRC_ID_" + a);
				strURL += "&PRC_USER_ID_" + strCOUNT + "=" + request.getParameter("rptPRC_USER_ID_" + a);
				strURL += "&PRC_DATE_" + strCOUNT + "=" + request.getParameter("rptPRC_DATE_" + a);
				strURL += "&SERIAL_NUM_" + strCOUNT + "=" + request.getParameter("rptSERIAL_NUM_" + a);
				strURL += "&RSN_CD_" + strCOUNT + "=" + request.getParameter("rptRSN_CD_" + a);
				strURL += "&GV_TYPE_" + strCOUNT + "=" + request.getParameter("rptGV_TYPE_" + a);
				strURL += "&PRC_TYPE_" + strCOUNT + "=" + request.getParameter("rptPRC_TYPE_" + a);
				strURL += "&TRANS_DATE_" + strCOUNT + "=" + request.getParameter("rptTRANS_DATE_" + a);
			}
		}
	}
	
	if (strAction != null){
            hParam.setActionCode(strAction);
    }else{
            strAction = ""; 
	}	
	if(strCoySub != null){
		hParam.put("COY_SUB", strCoySub);
	}else{
		strCoySub = "";
	}
	if(strStore != null){
		hParam.put("STORE", strStore);
	}else{
		strStore = "";
	}
	if(strPrcId != null){
		hParam.put("PRC_ID", strPrcId);
	}else{
		strPrcId = "";
	}
	if(strGvType != null){
		hParam.put("GV_TYPE", strGvType);
	}else{
		strGvType = "";
	}
	if(strSerialNo != null){
		hParam.put("SERIAL_NUM", strSerialNo);
	}else{
		strSerialNo = "";
	}
	if(strGvDeno != null){
		hParam.put("GV_DENOMINATION", strGvDeno);
	}else{
		strGvDeno = "";
	}
	if(strGVNo != null){
		hParam.put("GV_NO", strGVNo);
	}else{
		strGVNo = "";
	}
	if(strPurchaser != null){
		hParam.put("PCH_NAME", strPurchaser);
	}else{
		strPurchaser = "";
	}
	if(strContactNo != null){
		hParam.put("PCH_CONTACT_NO", strContactNo);
	}else{
		strContactNo = "";
	}
	if(strRsnCd != null){
		hParam.put("RSN_CD", strRsnCd);
	}else{
		strRsnCd = "";
	}
	if(strDateIssue != null){
		hParam.put("DATE_ISSUE", strDateIssue);
	}else{
		strDateIssue = "";
	}
	if(strCreateBy != null){
		hParam.put("PRC_USR_ID", strCreateBy);
	}else{
		strCreateBy = user_id;
	}
	if(strDateCreate != null){
		hParam.put("PRC_DATE", strDateCreate);
	}else{
		strDateCreate = "";
	}
	if(strProcessType != null){
		hParam.put("PRC_TYPE", strProcessType);
	}else{
		strProcessType = "";
	}
	if(strMembershipNo != null){
		hParam.put("MEMBERSHIP_NO", strMembershipNo);
	}else{
		strMembershipNo = "";
	}
	
	hParam.put("USER_ID", user_id);
	hParam.put("COY", strCoy);
	hParam.put("USR_LANGUAGE", lang_code);

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
		<title><%=jbWResGUI.getRes("Voucher Issuance Inquiry")%></title>
		<jsp:include page="../Common/Header.jsp"/>
		<jsp:include page="../Common/Script.jsp"/>
		<script>
			var msg = '';
			//PageControl.jsp needs this function.
			function processHTTPPage(strPageAction)
			{
				'use strict';
				var ACTION = strPageAction;
				var URLstr = '<%=BaseURL%>/profit/GV/VoucherIssuanceInquiryAMY.jsp';

				if (strPageAction === 'getPage')
				{
					ACTION += '&PAGE=' + window.document.FORM.txtGotoPage.value;
				}
				window.document.FORM.PRINT_FLAG.value = 'N';
				window.document.FORM_SUBMIT.action = buildActionURL('FORM', URLstr, ACTION, 'cboSubCoy%txtStore%txtPrcId%cboGvType%txtSerialNo%cboGvDeno%txtGVNo%txtPurchaser%txtContactNo%cboRsnCd%txtDateIssue%txtCreateBy%txtDateCreate%cboProcessType%strMembershipNo');
				window.document.FORM_SUBMIT.submit();
			}
			
			//Return calendar value
			function restart()
			{
				'use strict';
				try
				{
					if (whichOne == '1'){
						document.FORM.elements['txtDateIssue'].value = '' + year + '-' + padout(month - 0 + 1) + '-' + padout(day);
					}
					if (whichOne == '2'){
						document.FORM.elements['txtDateCreate'].value = '' + year + '-' + padout(month - 0 + 1) + '-' + padout(day);      
					}
				
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
				window.document.FORM.elements['SEARCH_FIELD'].value = search_field;
				
				if(search_field === 'txtGVNo')
				{
					PATH += '?ACTION=search&KEY1='+window.document.FORM.cboSubCoy.value;
					PATH += '&KEY2='+window.document.FORM.cboGvType.value;
					PATH += '&KEY3='+window.document.FORM.cboGvDeno.value;
				}
				
				window.open(PATH,'null','height=600,width=800,left=0, top=0, status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes');
			}
			
			function returnValue(strReturn)
			{
				'use strict';
				var search_field = window.document.FORM.elements['SEARCH_FIELD'].value; 
				
				if ( search_field === 'txtStore')
				{
					window.document.FORM.elements[search_field].value = getValue('STORE', strReturn);
				}
				else if (search_field === 'txtCreateBy')
				{
					window.document.FORM.elements[search_field].value = getValue('USR_ID', strReturn);
				}
				else if(search_field === 'txtGVNo')
				{
					window.document.FORM.elements[search_field].value = getValue('GV_NO', strReturn);
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
				var CallbackMethod = 'onChangeStoreCalledBack';
				
				var Params = '';
				Params = Params + '&STORE=' + window.document.FORM.elements[search_field].value;
				Params = Params + '&COY=' + window.document.FORM.txtCoy.value;
				Params = Params + '&COY_SUB=' + window.document.FORM.cboSubCoy.value;
				Params = Params + '&USR_ID=' + '<%=aduserInfo.USR_ID()%>';
									
				var baseURL = '<%=BaseURL%>';
				
				invokeTargetClass(baseURL, TargetClass, TargetMethod, CallbackMethod, Params);
			}
			
			function onChangeStoreCalledBack(str) 
			{
				'use strict';
				var search_field = window.document.FORM.SEARCH_FIELD.value; 
				
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
						msg = 'Store code' + ' ' + '<%=jbWResPrompt.getRes("FLAG_DELETED")%>';
						alert(msg);
	
						
						window.document.FORM.elements[search_field].value='';
						window.document.FORM.elements[search_field].focus();
					}
					else if(getValue('STORE_COY_SUB', str) !== window.document.FORM.cboSubCoy.value) 
					{
						msg = 'Store code' + ' ' + '<%=jbWResPrompt.getRes("NOTBELONG")%>' + ' ' + '<%=jbWResGUI.getRes("Company Subsidiary")%>';
						alert(msg);
						
						window.document.FORM.elements[search_field].value='';
						window.document.FORM.elements[search_field].focus();
						
					}
				}
			}
			
			function onChgVouchType()
			{
				'use strict';
				var voucherType = window.document.FORM.cboGvType.value;
				
				var url = '<%=BaseURL%>/profit/GV/VoucherIssuanceInquiryAMY.jsp';
				window.document.FORM.action = url;
				window.document.FORM.PRINT_FLAG.value = 'N';
				window.document.FORM.submit();
			}
			
			function ResetVouchNum()
			{
				'use strict';
				window.document.FORM.txtGVNo.value = '';
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
					window.document.FORM.elements[fieldName].value = '';
					alert('Please select Company Code.');
					window.document.FORM.cboSubCoy.focus();
					return false;
				}
					
				var TargetClass = 'qrcom.PROFIT.webbean.dao.GV.GvmstrWebbeanAMY';
				var TargetMethod = 'verifyGVNo_2';
				var CallbackMethod = 'onChgVoucherNoCalledBack_2';
				
				var params = '';
				params = '&COY=' + window.document.FORM.txtCoy.value + 
						 '&COY_SUB='+ window.document.FORM.cboSubCoy.value + 
						 '&GV_NO='+ window.document.FORM.elements[fieldName].value;
				var baseURL = '<%=BaseURL%>';
				invokeTargetClass(baseURL, TargetClass, TargetMethod, CallbackMethod, params);
			}
			
			function onChgVoucherNoCalledBack_2(strReturn)
			{
				'use strict';
				var ret_STATUS = getValue('@STATUS', strReturn);
				
				var fieldName = window.document.FORM.SEARCH_FIELD.value;
				
				if(ret_STATUS === 'N')
				{
					alert('<%=jbWResPrompt.getRes("NE_REC")%>');
					window.document.FORM.elements[fieldName].value = '';
					window.document.FORM.elements[fieldName].focus();
					return false;
				}
				/*
				** removed by mega 20160204 <ver1.6>
				else
				{
					var strStatus = getValue('VOUCHER_STATUS', strReturn);
					
					if(strStatus !== 'I')
					{
						alert('Voucher Number: '+window.document.FORM.elements[fieldName].value+' is not in Issued Status');
						window.document.FORM.elements[fieldName].value = '';
						window.document.FORM.elements[fieldName].focus();
						return false;
					}
				}
				*/
			}
			
			function onchgRsnCd(fieldName)
			{
				'use strict';
				window.document.FORM.SEARCH_FIELD.value = fieldName;
				
				var rsn_cd = window.document.FORM.elements[fieldName].value;
				var gv_type = window.document.FORM.cboGvType.value;
				
				if(rsn_cd === '')
				{
					return false;
				}
				
				var TargetClass    = 'qrcom.PROFIT.webbean.dao.GV.GvrsnmstWebbeanAMY';
				var TargetMethod = 'verifyGvRsnCode_2';
				var CallbackMethod = 'onchgRsncdCalledBack';
				
				var params = '&COY='+ window.document.FORM.txtCoy.value +
							 '&COY_SUB='+ window.document.FORM.cboSubCoy.value +
							 '&RSN_CD='+ rsn_cd;
							 
				var baseURL = '<%=BaseURL%>';
				
				invokeTargetClass(baseURL, TargetClass, TargetMethod, CallbackMethod, params);
			}
			
			function onchgRsncdCalledBack(strReturn)
			{
				'use strict';
				//get fieldName
				var fieldName = window.document.FORM.SEARCH_FIELD.value;
				
				var ret_STATUS = getValue('@STATUS', strReturn);
				
				if(ret_STATUS==='N')
				{
					window.document.FORM.elements[fieldName].value='';
					window.document.FORM.elements[fieldName].focus();
					alert('<%=jbWResPrompt.getRes("NE_REC")%>');
					return false;
				}
				else
				{
					if(getValue('DEL_CD', strReturn)==='Y')
					{
						window.document.FORM.elements[fieldName].value='';
						window.document.FORM.elements[fieldName].focus();
						alert('<%=jbWResPrompt.getRes("Reason Code selected has been flagged deleted")%>');
						return false;
					}
				}
			}
			
			function viewIssuance(total_rec)
			{
				'use strict';
				var COY 		= '';
				var COY_SUB 	= '';
				var SERIAL_NUM 	= '';
				var PRC_ID 		= '';
				var PRC_DATE 	= '';
				var PRC_TYPE 	= '';
				var STORE		= '';
				var TRANS_DATE	= '';
				
				if(!Validate(document.FORM, 'view', total_rec,'Y')){
					return;
				}
				
				for(var i=1; i <= total_rec; i++) 
				{
					var ChkObj = document.FORM.elements['chkAction_' + i];
					if(ChkObj.checked) 
					{
						COY = document.FORM.elements['rptCOY_' + i].value;
						COY_SUB = document.FORM.elements['rptCOY_SUB_' + i].value;
						SERIAL_NUM = document.FORM.elements['rptSERIAL_NUM_' + i].value;
						
						PRC_ID = document.FORM.elements['rptPRC_ID_' + i].value;
						
						PRC_DATE = document.FORM.elements['rptPRC_DATE_' + i].value;
						
						PRC_TYPE = document.FORM.elements['rptPRC_TYPE_' + i].value;
						
						STORE = document.FORM.elements['rptSTORE_' + i].value;
						
						TRANS_DATE = document.FORM.elements['rptTRANS_DATE_' + i].value;
						
					}
				}
				
				var n = new Date().getTime();
				window.document.FORM.target = 'viewPromptWindow' + n; 
				window.open('','viewPromptWindow' + n,'height=600,width=800,left=0, top=0, status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes');
				var FORMaction = '<%=BaseURL%>/servlet/SvltVoucherIssuanceInquiryAMY?ACTION=view&KEY1='+COY+'&KEY2='+COY_SUB+'&KEY3='+SERIAL_NUM+'&KEY4='+PRC_ID+'&KEY5='+PRC_DATE+'&KEY6='+PRC_TYPE+'&KEY8='+STORE+'&KEY9='+TRANS_DATE;
				
				window.document.FORM.action = FORMaction;
				window.document.FORM.PRINT_FLAG.value = 'N';
				window.document.FORM.submit();
				window.document.FORM.target = '_self';      
			}
			
			function onClickPrintButton()
			{
				'use strict';
				var cnt    = document.FORM.txtHiddenCount.value;
				var select = 0;
				var params = '';
				
				for (var i = 1 ; i <= cnt ; i++)
				{
					var ChkObj = document.FORM.elements['chkAction_' + i];
					if(ChkObj.checked) 
					{
	
						select ++;
						
						params += '&COY_' + select + '=' + document.FORM.elements['rptCOY_' + i].value;
						params += '&COY_SUB_' + select + '=' + document.FORM.elements['rptCOY_SUB_' + i].value;
						params += '&RSN_CD_' + select + '=' + document.FORM.elements['rptRSN_CD_' + i].value;
						params += '&GV_TYPE_' + select + '=' + document.FORM.elements['rptGV_TYPE_' + i].value;
						params += '&PRC_TYPE_' + select + '=' + document.FORM.elements['rptPRC_TYPE_' + i].value;
						params += '&PRC_ID_' + select + '=' + document.FORM.elements['rptPRC_ID_' + i].value;
						params += '&SERIAL_NUM_' + select + '=' + document.FORM.elements['rptSERIAL_NUM_' + i].value;
					}
				}
				
				if (select === 0)
				{
					alert('<%=jbWResPrompt.getRes("Please select at least one action")%>');
					return false;
				}
				
				disableAllButton();
				
				var TargetClass    	= 'qrcom.PROFIT.webbean.dao.GV.GvrsnmstWebbeanAMY';
				var TargetMethod 	= 'verifyAllowedToPrint';
				var CallbackMethod 	= 'verifyAllowedToPrintCalledBack';
				params += '&TOTAL_ROW='+select;
		
				invokeTargetClass('<%=BaseURL%>', TargetClass, TargetMethod, CallbackMethod, params);
			}
			
			function verifyAllowedToPrintCalledBack(strReturn)
			{
				'use strict';
				
				var ret_STATUS = getValue('@STATUS', strReturn);
				
				if(ret_STATUS === 'N')
				{
					var rsnCd 		= getValue('RSN_CD', strReturn);
					var gvType 		= getValue('GV_TYPE', strReturn);
					var prcId 		= getValue('PRC_ID', strReturn);
					var serialNum 	= getValue('SERIAL_NUM', strReturn);
					
					var str = '';
					if(serialNum !== ''){
						str = 'serial number ' + serialNum;
					}else{
						if(prcId !== ''){
							str = 'transaction no ' + prcId;
						}
					}
					
					alert('[ Voucher Type : ' + gvType + ', Reason Code : ' + rsnCd + ' ]\n' +
						  'Issuance process with ' + str + ' is not required to print receipt.');
					
					enableAllButton();
					
					return false;
				}
				else if(ret_STATUS === 'Y')
				{
					disableAllButton();
					print();
				}
				else
				{
					enableAllButton();
					alert(strReturn);
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
						select++;
					}
				}
				
				if (select === 0)
				{
					alert('<%=jbWResPrompt.getRes("Please select at least one action")%>');
					return false;
				}
				
				document.FORM.PRINT_FLAG.value = 'Y';
				document.FORM.action = '<%=BaseURL%>/profit/GV/VoucherIssuanceInquiryAMY.jsp?ACTION=search';
				document.FORM.submit();
			}
			
			function enableAllButton()
			{
				'use strict';
				window.document.FORM.cmdReset.disabled = false;
				window.document.FORM.cmdSearch.disabled = false;
				window.document.FORM.btnPrint.disabled = false;
				window.document.FORM.btnView.disabled = false;
			}
			function disableAllButton()
			{
				'use strict';
				window.document.FORM.cmdReset.disabled = true;
				window.document.FORM.cmdSearch.disabled = true;
				window.document.FORM.btnPrint.disabled = true;
				window.document.FORM.btnView.disabled = true;
			}
			function initialize()
			{
				enableAllButton();
			}
			
			function checkNumeric(val, fieldName)
			{
				if(!isNumeric(val))
				{
					alert('Insert numeric value only');
					window.document.FORM.elements[fieldName].value = '';
					window.document.FORM.elements[fieldName].focus();
				}
			}
		</script>
	</head>
	<body onload="initialize();">
	<center>
		<form id="FORM" method="post" action="<%=BaseURL%>/profit/GV/VoucherIssuanceInquiryAMY.jsp" name="FORM">
			<fieldset class="fieldsettitle"><legend><%=jbWResGUI.getRes("Voucher Issuance Inquiry")%></legend>
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
															<td width="5"><input type="text" name="txtCoy" value="<%=strCoy%>" readonly tabindex="-1" class="input-display"/></td>
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
															<td colspan="2"><input type="text" name="txtStore" value="<%=strStore%>" maxlength="<%=SYSStoreLength%>" class="mandatory" onchange="if(this.value!=='')checkStore('txtStore');"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/search.gif" name="imgStore" ALT="<%=jbWResGUI.getRes("Find")%>" onclick="SearchCriteria('<%=BaseURL%>/profit/MT/StrmstSearch.jsp?ACTION=search&FILTER1=STORE_DEL_CD!=\'Y\'', 'txtStore')" style='cursor:hand;'/></td>
															<td width="20%" class="caption"><%= jbWResGUI.getRes("Transaction No")%></td>
															<td width="25%"><input type="text" name="txtPrcId" value="<%=strPrcId%>" maxlength="<%=SYSTXNoLength%>" onblur="if(this.value !== null)checkNumeric(this.value,'txtPrcId');"/></td>
															<td width="5%"></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Voucher Type")%></td>
															<td colspan="2">
																<select name="cboGvType" onchange="onChgVouchType();">
																	<%=MTComboBox.Default(lang_code, strGvType, "GVTYPEMST.GV_TYPE", "GVTYPEMST.GV_TYPE_DESC", "GVTYPEMST", "ORDER BY GVTYPEMST.GV_TYPE ")%>       	  
																</select>
															</td>
															<td width="5%"></td>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Serial No")%></td>
															<td width="25%"><input type="text" name="txtSerialNo" value="<%=strSerialNo%>" maxlength="<%=SYSIssueGVSNLength%>"/></td>
															<td width="5%"></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Voucher Denomination")%></td>
															<td colspan="2">
																<select name="cboGvDeno" onchange="ResetVouchNum();">
																	<%=MTComboBox.Default(lang_code, strGvDeno,"GV_DENOMINATION", "GV_DENO_DESC", "GVDENOMST", "WHERE COY = '"+strCoy+"' AND COY_SUB = '"+strCoySub+"' AND GV_TYPE = '"+strGvType+"' AND DEL_CD = 'N' ORDER BY GV_DENOMINATION ")%>
																</select>
															</td>
															<td width="5%"></td>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Voucher No")%></td>
															<td width="25%"><input type="text" name="txtGVNo" value="<%=strGVNo%>" maxlength="<%=SYSGVNoLength%>" onchange="onChgVoucherNo('txtGVNo');"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/search.gif" alt="<%=jbWResGUI.getRes("Find")%>" style="cursor:hand" onclick="SearchCriteria('<%=BaseURL%>/profit/GV/VoucherSearchAMY.jsp', 'txtGVNo')" style='cursor:hand;'></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Purchaser/Member")%></td>
															<td colspan="2"><input type="text" name="txtPurchaser" value="<%=strPurchaser%>" maxlength="<%=SYSNameLength%>"/></td>
															<td width="5%"></td>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Contact No")%></td>
															<td width="25%"><input type="text" name="txtContactNo" value="<%=strContactNo%>" maxlength="<%=SYSTelNoLength%>"/></td>
															<td width="5%"></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Reason Code")%></td>
															<td colspan="2">
																<select name="cboRsnCd" onblur="onchgRsnCd('cboRsnCd')"> 
																	<%=MTComboBox.Default(lang_code, strRsnCd, "RSN_CD", "RSN_DESC", "DEL_CD", "GVRSNMST", "WHERE COY='"+strCoy+"' AND COY_SUB='"+strCoySub+"' AND GV_TYPE='"+strGvType+"'")%>
																</select>
															</td>
															<td width="5%"></td>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Date Issued")%></td>
															<td width="25%"><input type="text" name="txtDateIssue" value="<%=strDateIssue%>" onchange="this.value=formatDate(this.value)"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/calendr.gif" ALT="<%=jbWResGUI.getRes("Calendar")%>" onclick="newWindow(1)" style="cursor:hand"/></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Created By")%></td>
															<td colspan="2"><input type="text" name="txtCreateBy" value="<%=strCreateBy%>" maxlength="8"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/search.gif" name="imgCreateBy" ALT="<%=jbWResGUI.getRes("Find")%>" onclick="SearchCriteria('<%=BaseURL%>/profit/AD/AduserSearch.jsp?ACTION=search', 'txtCreateBy')" style='cursor:hand;'></td>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Date Created")%></td>
															<td width="25%"><input type="text" name="txtDateCreate" value="<%=strDateCreate%>" onchange="this.value=formatDate(this.value)"/></td>
															<td width="5%"><img src="<%=BaseURL%>/profit/images/calendr.gif" ALT="<%=jbWResGUI.getRes("Calendar")%>" onclick="newWindow(2)" style="cursor:hand"/></td>
														</tr>
														<tr>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Process Type")%></td>
															<td colspan="2">
																<select name="cboProcessType">
																	<%=MTComboBox.G_ComboBox(strProcessType, " -"+jbWResGUI.getRes("  ")+"|"+SYSIssueGVIquiryPrc) %>
																</select>
															</td>
															<td width="5%"></td>
															<td width="20%" class="caption"><%=jbWResGUI.getRes("Membership No")%></td>
															<td width="25%"><input type="text" name="txtMembershipNo" value="<%=strMembershipNo%>" maxlength="<%=SYSGvMbShipNoLength%>"/></td>
															<td width="5%"></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<!-- BUTTONS -->
												<table width="100%">
													<tr>
														<td width="70%"></td>
														<td width="15%" align="right"><input type="reset" name="cmdReset" value="<%=jbWResGUI.getRes("Reset")%>" onclick="setFormReset(this.form,'<%=BaseURL%>/profit/GV/VoucherIssuanceInquiryAMY.jsp','')"/></td> 
														<td width="15%"><input type="button" name="cmdSearch" value="<%=jbWResGUI.getRes("Search")%>" onclick="if(Validate(this.form,'search','','Y')) {disableAllButton(); setFormSubmit('<%=BaseURL%>/profit/GV/VoucherIssuanceInquiryAMY.jsp','S','cboSubCoy%txtStore%txtPrcId%cboGvType%txtSerialNo%cboGvDeno%txtGVNo%txtPurchaser%txtContactNo%cboRsnCd%txtDateIssue%txtCreateBy%txtDateCreate%cboProcessType%txtMembershipNo%POPUP');}"/></td>  
													</tr>
												</table>
											</tr>
										</table>
									</td>
								</td>
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
												<td width="5%" class="tb-display" align="center"><%=jbWResGUI.getRes("Action")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Store")%></td>
												<td width="13%" class="tb-display"><%=jbWResGUI.getRes("Transaction No")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Serial No")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Process Type")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Purchaser")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Reason Code")%></td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Total Amount ")%>(<%=jbWResGUI.getRes("RM")%>)</td>
												<td width="10%" class="tb-display"><%=jbWResGUI.getRes("Created By")%></td>
												<td width="12%" class="tb-display"><%=jbWResGUI.getRes("Date Issued")%></td>			
											</tr>
											<%
												Collection rsCllt = null;
												String prcTypeDesc= "";
												
												// PageControl.jsp needs this string variable.
												String strPageOfTotal = "";
												
												String strTdBgColor  = "tb-display";
												String strInputColor = "input-display";
												
												if (strAction != null && strAction.equals("search"))
												{
													String strLinePerPage = jbWebSessionParam.getProfitvvValue(request, "SYSMaxLinesPerPage");
													VoucherIssuanceInquiryDAOAMY voucherIssuanceInquiryDAO 	= new VoucherIssuanceInquiryDAOAMY();
													HTTPPageQueryBased httpPageQueryBased = new HTTPPageQueryBased(hParam, voucherIssuanceInquiryDAO, strLinePerPage);
													
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
														if(str[8]==null) str[8]="";
														if(str[9]==null) str[9]="";
														if(str[10]==null) str[10]="";
														if(str[11] == null) str[11]="";
														if(str[12] == null) str[12]="";
														if(str[13] == null) str[13]="";
														
														if(str[9] != null && !(str[9].equals("")))
														{
															StringTokenizer st = new StringTokenizer(SYSIssueGVIquiryPrc, ",");
															while (st.hasMoreElements())
															{
																String tmp = (String)st.nextElement();
																System.out.println("tmp = " + tmp);
																StringTokenizer st1 = new StringTokenizer(tmp, "|");
																
																while (st1.hasMoreElements())
																{
																	String tmp2 = (String)st1.nextElement();
																	StringTokenizer st2 = new StringTokenizer(tmp2, "-");
																	while(st2.hasMoreElements())
																	{
																		if(str[9].equals((String)st2.nextElement()))
																		{
																			prcTypeDesc = (String)st2.nextElement();
																			break;
																		}
																	}
																}
															}
														}
											%>
											<tr>
												<td class="<%=strTdBgColor%>" align="center"><input type="checkbox" name="chkAction_<%=i%>" onClick="if(checkCurrentAction(SELECT_MODE.value))selectCurrentAction(txtHiddenCount.value, this, 'window.document.FORM.chkAction_')"/></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtStore_<%=i%>" value="<%=str[2]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtPrcId_<%=i%>" value="<%=str[3]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtSerialNo_<%=i%>" value="<%=str[6]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>	
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtPrcType_<%=i%>" value="<%=prcTypeDesc%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="rptPRC_USER_ID_<%=i%>" value="<%=str[12]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>												<!-- For View and Report Printing Parameters -->
												<td class="<%=strTdBgColor%>"><input type="text" name="rptRSN_CD_<%=i%>" value="<%=str[7]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
												<td class="<%=strTdBgColor%>"><input type="text" name="txtTotAmt_<%=i%>" value="<%=webCurrConverter.format(str[11], 2)%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:right"/></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtCreateBy_<%=i%>" value="<%=str[4]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
												<td class="<%=strTdBgColor%>"><input type="text"  name="txtDateUploaded_<%=i%>" value="<%=qrMisc.discardTime(str[13])%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>

												
												<input type="hidden" name="rptCOY_<%=i%>"	 		value="<%=str[0]%>"/>
												<input type="hidden" name="rptCOY_SUB_<%=i%>" 		value="<%=str[1]%>"/>
												<input type="hidden" name="rptSTORE_<%=i%>" 		value="<%=str[2]%>"/>
												<input type="hidden" name="rptPRC_ID_<%=i%>" 		value="<%=str[3]%>"/>
												<input type="hidden" name="rptPRC_DATE_<%=i%>" 		value="<%=str[5]%>"/>
												<input type="hidden" name="rptSERIAL_NUM_<%=i%>" 	value="<%=str[6]%>"/>
												<input type="hidden" name="rptGV_TYPE_<%=i%>" 		value="<%=str[8]%>"/>
												<!-- Remark : value of rptPRC_TYPE_ below : I for Issuance or L for Cancellation, other value than I or L would print no data -->
												<input type="hidden" name="rptPRC_TYPE_<%=i%>" 		value="<%=str[9]%>"/>
												<input type="hidden" name="rptTRANS_DATE_<%=i%>" 	value="<%=str[10]%>"/>
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
								document.writeln('<td width="12%"></td>');
								document.writeln('<td width="12%"></td>');
								document.writeln('<td width="40%"></td>');
								document.writeln('<td width="12%"></td>');
								document.writeln('<td width="12%"><input name="btnPrint" type="button" value="<%=jbWResGUI.getRes("Print")%>" align="right" onclick="onClickPrintButton()"/></td>');
								document.writeln('<td width="12%"><input name="btnView" type="button" value="<%=jbWResGUI.getRes("View")%>" align="right" onclick="viewIssuance(<%=i%>)"/></td>');
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
				<input type="hidden" name="PRINT_CHECK" value="<%=strPrintCheck%>"/>
				<input type="hidden" name="POPUP" value="<%=strPOPUP%>"/>
				<script>
				<%
					if(strPrintFlag!=null && strPrintFlag.equals("Y"))
					{
				%>
						var PAGE_ID   	= "VoucherIssuanceInquiryAMY";
						var TOTAL_ROW 	= <%=selectedloop%>;
						var strUrl  	= "<%=BaseURL%>/servlet/SvltRptProcessor";
						strUrl 			+= "?ACTION=print_to_cache&USER_ID=" + "<%=user_id%>"+ "&PAGE_ID=" + PAGE_ID;
						strUrl 			+= "&REPORT_TYPE=VOUCH_ISSUE";
						strUrl 			+= "<%=strURL%>";
            strUrl 			+= "&REPRINT=" + 'true' ; 
						strUrl 			+= "&TOTAL_ROW=" + TOTAL_ROW;
						strUrl 			+= "&report_id=VoucherIssuanceInquiryRptAMY";
						strUrl 			+= "&RPT_CLASSNAME=qrcom.PROFIT.reports.GV.VoucherIssuanceInquiryRptAMY&DES_TYPE=file&DES_FORMAT=pdf";
						//alert(strUrl);
						window.open(strUrl, "", "height=300, width=500, resizable=yes, scrollbars=yes");
				<%
					}
				%>
				</script>
			</fieldset>
		</form>
		<form id="FORM_SUBMIT" method="post" action="<%=BaseURL%>/profit/GV/VoucherIssuanceInquiryAMY.jsp" name="FORM_SUBMIT"></form>
	</center>
	</body>
</html>