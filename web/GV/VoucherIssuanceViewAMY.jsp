<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage = "/profit/Common/ExceptionError.jsp" %>
<%@ page import = "java.net.*" %>

<%@ page import = "java.util.*" %>
<%@ page import = "qrcom.util.*" %>
<%@ page import = "qrcom.PROFIT.files.info.AduserInfo"%>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultCoySub" %>
<%@ page import = "qrcom.PROFIT.files.dao.local.GV.VoucherIssuanceInquiryDAOAMY" %>
<%@ page import = "qrcom.PROFIT.webbean.HTTPObj.*" %>
<%@ page import = "qrcom.PROFIT.webbean.language.*" %>
<%@ page import = "qrcom.PROFIT.system.*"%>
<%@ page import="java.util.StringTokenizer"%>

<jsp:useBean id="jbSession" scope="session" class="qrcom.PROFIT.servlet.HTTPSessionAttributeWrapper" />
<jsp:useBean id="jbWebSessionParam" scope="session" class="qrcom.PROFIT.webbean.HTTPObj.WebSessionParam" />
<jsp:useBean id="webCurrConverter" scope="session" class="qrcom.util.CurrencyConverter" />

<%
	WResGUI jbWResGUI 								= jbWebSessionParam.getWResGUI(request);
	WResPrompt jbWResPrompt 						= jbWebSessionParam.getWResPrompt(request);
	WebAuthorization webAuth 						= new WebAuthorization(request);
	AduserInfo aduserInfo							= (AduserInfo)request.getSession().getAttribute("aduserInfo");
	CoysubmstComboBox coysubmstComboBox 			= new CoysubmstComboBox(request);
	DefaultCoySub defaultCoySub						= new DefaultCoySub(aduserInfo);
	
	String BaseURL 				= SysParam.getInstance().getBaseURL();
	String lang_code 			= jbWebSessionParam.getAduserInfo().USR_LANGUAGE();
	String user_id 				= jbWebSessionParam.getAduserInfo().USR_ID();
	//String strCoy 			= jbWebSessionParam.getAduserInfo().USR_COMP();
	String SYSIssueGVIquiryPrc  = jbWebSessionParam.getProfitvvValue(request, "SYSIssueGVIquiryPrc");
	
	HParam hParam 				= new HParam();
	String strAction 			= request.getParameter("ACTION");
	String strCoy 				= request.getParameter("KEY1");
	String strCoySub 			= request.getParameter("KEY2");
	String strSerialNo			= request.getParameter("KEY3");
	String strPrcId 			= request.getParameter("KEY4");
	String strPrcDate			= request.getParameter("KEY5");
	String strPrcType			= request.getParameter("KEY6");
	String strModeFrPaging		= request.getParameter("KEY7");
	String strMode				= request.getParameter("MODE");
	String strStore 			= request.getParameter("KEY8");
	String strTransDate			= request.getParameter("KEY9");
	
	if(strModeFrPaging != null && !strModeFrPaging.equals("")){
		strMode = strModeFrPaging;
	}
	if(strMode == null || strMode.equals(""))
	{
		strMode = "tab1";
	}
	
	String strProgramType			= "";
	String strContactNo			= "";
	String strNRIC				= "";
	String strCollector			= "";
	String strPaymentMode		= "";
	String strRsnCd				= "";
	String strCreateBy			= "";
	String strTotalAmount		= "";
	String strMembershipNo		= "";
	String prcTypeDesc			= "";
	String strPaymentModeDesc	= "";
	String strCancelRemark		= "";
	String strTotalDiscount		= "";
	String strTotalAmountAfterDiscount		= "";
	
	if(strPrcType!=null && !strPrcType.equals(""))
	{
		StringTokenizer st = new StringTokenizer(SYSIssueGVIquiryPrc, ",");										
		while (st.hasMoreElements())
		{
			String tmp = (String)st.nextElement();
			StringTokenizer st1 = new StringTokenizer(tmp, "|");
			
			while (st1.hasMoreElements())
			{
				String tmp2 = (String)st1.nextElement();
				StringTokenizer st2 = new StringTokenizer(tmp2, "-");
				while(st2.hasMoreElements())
				{
					if(strPrcType.equals((String)st2.nextElement()))
					{
						prcTypeDesc = (String)st2.nextElement();
						break;
					}
				}
			}
		}
	}
	
	if (strAction != null){
            hParam.setActionCode(strAction);
    }else{
            strAction = ""; 
	}
	if(strCoy != null){
		hParam.put("COY", strCoy);
	}else{
		strCoy = "";
	}
	if(strCoySub != null){
		hParam.put("COY_SUB", strCoySub);
	}else{
		strCoySub = "";
	}
	if(strSerialNo != null){
		hParam.put("SERIAL_NUM", strSerialNo);
	}else{
		strSerialNo = "";
	}
	if(strPrcId != null){
		hParam.put("PRC_ID", strPrcId);
	}else{
		strPrcId = "";
	}
	if(strPrcDate != null){
		hParam.put("PRC_DATE", strPrcDate);
	}else{
		strPrcDate = "";
	}
	if(strPrcType != null){
		hParam.put("PRC_TYPE", strPrcType);
	}else{
		strPrcType = "";
	}
	if(strStore != null){
		hParam.put("STORE", strStore);
	}else{
		strStore = "";
	}
	if(strTransDate != null){
		hParam.put("TRANS_DATE", strTransDate);
	}else{
		strTransDate = "";
	}
	hParam.put("USR_ID", user_id);
	hParam.put("COY", strCoy);
	hParam.put("USR_LANGUAGE", lang_code);

	Map recordMap = (HashMap)jbSession.getAttribute("recordMap");
	
	if(recordMap.get("STORE")!= null && !recordMap.get("STORE").equals(""))
	{
		strStore = (String)recordMap.get("STORE");
	}
	if(recordMap.get("PCH_NAME")!= null && !recordMap.get("PCH_NAME").equals(""))
	{
		strProgramType = (String)recordMap.get("PCH_NAME");
	}
	if(recordMap.get("TRANS_DATE")!= null && !recordMap.get("TRANS_DATE").equals(""))
	{
		strTransDate = (String)recordMap.get("TRANS_DATE");
	}
	if(recordMap.get("PCH_CONTACT_NO")!= null && !recordMap.get("PCH_CONTACT_NO").equals(""))
	{
		strContactNo = (String)recordMap.get("PCH_CONTACT_NO");
	}
	if(recordMap.get("COLL_NRIC")!= null && !recordMap.get("COLL_NRIC").equals(""))
	{
		strNRIC = (String)recordMap.get("COLL_NRIC");
	}
	if(recordMap.get("COLL_NAME")!= null && !recordMap.get("COLL_NAME").equals(""))
	{
		strCollector = (String)recordMap.get("COLL_NAME");
	}
	if(recordMap.get("PAYMENTMODE")!= null && !recordMap.get("PAYMENTMODE").equals(""))
	{
		strPaymentMode = (String)recordMap.get("PAYMENTMODE");
		if(strPaymentMode.equals("CS"))
		{
			strPaymentModeDesc = jbWResGUI.getRes("CASH");
		}
		else if(strPaymentMode.equals("CC"))
		{
			strPaymentModeDesc = jbWResGUI.getRes("CREDIT CARD");
		}
		else if(strPaymentMode.equals("CH"))
		{
			strPaymentModeDesc = jbWResGUI.getRes("CHEQUE");
		}
		else if(strPaymentMode.equals("TT"))
		{
			strPaymentModeDesc = jbWResGUI.getRes("TELEGRAPHIC TRANSFER");
		}
	}
	if(recordMap.get("RSN_CD")!= null && !recordMap.get("RSN_CD").equals(""))
	{
		strRsnCd = (String)recordMap.get("RSN_CD");
	}
	if(recordMap.get("PRC_DATE")!= null && !recordMap.get("PRC_DATE").equals(""))
	{
		strPrcDate = (String)recordMap.get("PRC_DATE");
	}
	if(recordMap.get("TRANS_OPR")!= null && !recordMap.get("TRANS_OPR").equals(""))
	{
		strCreateBy = (String)recordMap.get("TRANS_OPR");
	}
	if(recordMap.get("TOT_AMOUNT")!= null && !recordMap.get("TOT_AMOUNT").equals(""))
	{
		strTotalAmount = (String)recordMap.get("TOT_AMOUNT");
	}
	if(recordMap.get("MEMBERSHIP_NO")!=null && !recordMap.get("MEMBERSHIP_NO").equals(""))
	{
		strMembershipNo = (String) recordMap.get("MEMBERSHIP_NO");
	}
	if(recordMap.get("REMARK")!=null && !recordMap.get("REMARK").equals(""))
	{
		strCancelRemark = (String) recordMap.get("REMARK");
	}
	if(recordMap.get("TOT_DISCOUNT")!=null && !recordMap.get("TOT_DISCOUNT").equals(""))
	{
		strTotalDiscount = (String) recordMap.get("TOT_DISCOUNT");
	}
	if(recordMap.get("TOT_AMOUNT_AFT_DISCOUNT")!=null && !recordMap.get("TOT_AMOUNT_AFT_DISCOUNT").equals(""))
	{
		strTotalAmountAfterDiscount = (String) recordMap.get("TOT_AMOUNT_AFT_DISCOUNT");
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

<!DOCTYPE HTML>
<html>
	<head>
		<title><%=jbWResGUI.getRes("Voucher Issuance Inquiry - View")%></title>    
		<jsp:include page="../Common/Header.jsp"/>
		<jsp:include page="../Common/Script.jsp"/>
		<script>
			//PageControl.jsp needs this function.
			function processHTTPPage(strPageAction)
			{
				'use strict';
				var ACTION = strPageAction;
				var URLstr = '<%=BaseURL%>/profit/GV/VoucherIssuanceViewAMY.jsp';

				if (strPageAction === 'getPage')
				{
					ACTION += '&PAGE=' + window.document.FORM.txtGotoPage.value;
				}

				window.document.FORM_SUBMIT.action = buildActionURL('FORM', URLstr, ACTION, 'txtCoy%txtCoySub%txtSerialNo%txtPrcId%txtDateCreate%txtPrcType%MODE%txtStore%txtTransDate');
				window.document.FORM_SUBMIT.submit();
			}
			
			function onChangeTab(mode)
			{
				'use strict';
				window.document.FORM.MODE.value = mode;
				
				var urlChangeTab = '<%=BaseURL%>/servlet/SvltVoucherIssuanceInquiryAMY?ACTION=view&KEY1=<%=strCoy%>&KEY2=<%=strCoySub%>&KEY3=<%=strSerialNo%>&KEY4=<%=strPrcId%>&KEY5=<%=strPrcDate%>&KEY6=<%=strPrcType%>&KEY7='+mode+'&KEY8=<%=strStore%>&KEY9=<%=strTransDate%>';
				window.document.FORM.action = urlChangeTab;
				window.document.FORM.submit();
			}
			
			function initialize()
			{
				'use strict';
				var mode = window.document.FORM.MODE.value;
				if(mode === null || mode === '' || mode === 'tab1')
				{
					window.document.getElementById('tab_1').className = 'td-tab-select';
					window.document.getElementById('tab_2').className = 'td-tab-unselect';
				}
				else if(mode === 'tab2')
				{
					window.document.getElementById('tab_1').className = 'td-tab-unselect';
					window.document.getElementById('tab_2').className = 'td-tab-select';
				}
				
			}
			function closePage()
			{
				'use strict';
				window.open('','_self','');
				window.close();
			}
		</script>
	</head>
	
	<body onload="initialize()">
		<form id="FORM" method="post" action="<%=BaseURL%>/profit/GV/VoucherIssuanceViewAMY.jsp" name="FORM">
			<fieldset class="fieldsettitle"><legend><%=jbWResGUI.getRes("Voucher Issuance Inquiry - View")%></legend>
				<table width="100%" cellpadding="5" cellspacing="0" border="0" class="table-border">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="0" cellspacing="1" class="table-border">
								<tr>
									<td>
										<table border="0" width="100%" align="center" cellspacing="1" cellpadding="3">
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Company")%></td>
												<td width="5%"><input type="text" name="txtCoy" value="<%=strCoy%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="20%"><input type="text" name="txtCoyName" value="<%=HTTPUtilClass.getDesc(lang_code, "COY_NAME", "COYMST", " WHERE COY = '" + strCoy + "'")%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Company Subsidiary")%></td>
												<td width="25%">
													<input type="text" name="txtCoySub" value="<%=strCoySub%>" readonly tabindex="-1" class="input-display"/>
												</td>
												<td width="5%"></td>
											</tr>
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Store")%></td>
												<td colspan="2"><input type="text" name="txtStore" value="<%=strStore%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Transaction No")%></td>
												<td width="25%"><input type="text" name="txtPrcId" value="<%=strPrcId%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											</tr>
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Process Type")%></td>
												<td colspan="2">
													<input type="text" name="txtProcessType" value="<%=prcTypeDesc%>" readonly tabindex="-1" class="input-display"/>
													<input type="hidden" name="txtPrcType" value="<%=strPrcType%>"/>
												</td>
												<td width="5%"></td>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Serial No")%></td>
												<td width="25%"><input type="text" name="txtSerialNo" value="<%=strSerialNo%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											</tr>
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Reason Code")%></td>
												<td colspan="2"><input type="text" name="txtRsnCd" value="<%=strRsnCd%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
												<td width="20%" class="caption">
													<%
														if(strPrcType != null && strPrcType.equals("I")){
															out.print(jbWResGUI.getRes("Date Issued"));
														}else if(strPrcType != null && strPrcType.equals("L")){
															out.print(jbWResGUI.getRes("Date Cancelled"));
														}
													%>
												</td>
												<td width="25%"><input type="text" name="txtTransDate" value="<%=qrMisc.discardTime(strTransDate)%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											</tr>
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Contact No")%></td>
												<td colspan="2"><input type="text" name="txtContactNo" value="<%=strContactNo%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Reference 1")%></td>
												<td colspan="25%"><input type="text" name="txtCollector" value="<%=strCollector%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											</tr>
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Reference 2")%></td>
												<td colspan="2"><input type="text" name="txtNRIC" value="<%=strNRIC%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Payment Mode")%></td>
												<td width="25%"><input type="text" name="txtPaymentMode" value="<%=strPaymentModeDesc%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											</tr>
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Program Type")%></td>
												<td colspan="2"><input type="text" name="txtPurchaser" value="<%=strProgramType%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Date Created")%></td>
												<td width="25%"><input type="text" name="txtDateCreate" value="<%=qrMisc.discardTime(strPrcDate)%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											</tr>
											<tr>
												<td width="20%" class="caption">
													<%
														if(strPrcType != null && strPrcType.equals("I")){
															out.print(jbWResGUI.getRes("Created By"));
														}else if(strPrcType != null && strPrcType.equals("L")){
															out.print(jbWResGUI.getRes("Cancelled By"));
														}
													%>
												</td>
												<td colspan="2"><input type="text" name="txtCreateBy" value="<%=strCreateBy%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Total Amount ")%>(<%=jbWResGUI.getRes("RM")%>)</td>
												<td width="25%"><input type="text" name="txtTotalAmount" value="<%=webCurrConverter.format(strTotalAmount, 2)%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											</tr>
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Membership No")%></td>
												<td colspan="2"><input type="text" name="txtMembershipNo" value="<%=strMembershipNo%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											<%
												if(strPrcType != null && strPrcType.equals("I"))
												{
											%>
												<td width="20%"></td>
												<td width="25%"></td>
												<td width="5%"></td>
											<%
												}
												else if(strPrcType != null && strPrcType.equals("L"))
												{
											%>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Cancelled Remark")%></td>
												<td width="25%"><input type="text" name="txtCancelRemark" value="<%=strCancelRemark%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											<%
												}
											%>
											</tr>
											<tr>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Total Discount ")%>(<%=jbWResGUI.getRes("RM")%>)</td>
												<td colspan="2"><input type="text" name="txtTotalDiscount" value="<%=webCurrConverter.format(strTotalDiscount, 2)%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
												<td width="20%" class="caption"><%= jbWResGUI.getRes("Total Amount after Discount ")%>(<%=jbWResGUI.getRes("RM")%>)</td>
												<td width="25%"><input type="text" name="txtTotalAmountAfterDiscount" value="<%=webCurrConverter.format(strTotalAmountAfterDiscount, 2)%>" readonly tabindex="-1" class="input-display"/></td>
												<td width="5%"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
								<td width="1%">&nbsp;</td>
								<td width="15%" id="tab_1" class="td-tab-select" onclick="onChangeTab('tab1');"  style='cursor:hand'><%=jbWResGUI.getRes("Summary")%></td>
								<td width="2%">&nbsp;</td>
								<td width="15%" id="tab_2" class="td-tab-unselect" onclick="onChangeTab('tab2');" style='cursor:hand'><%=jbWResGUI.getRes("Detail")%></td>
								<td width="68%">&nbsp;</td>
								</tr>
								<tr>
									<td colspan="5" class="td-tab-line">&nbsp;</td>   
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="0" cellspacing="1" class="table-border">
								<tr>
									<td>
										<%
											int i = 0;
											Collection rsCllt = null;
											
											// PageControl.jsp needs this string variable.
											String strPageOfTotal = "";
											 
											String strTdBgColor = "tb-display";
											String strInputColor = "input-display";
											
											if(strMode == null || strMode.equals("tab1"))
											{
										%>
												<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table-border">
													<tr>
														<td width="5%" class="tb-display" align="left"><%=jbWResGUI.getRes("Seq")%></td>
														<td width="9%" class="tb-display" align="left"><%=jbWResGUI.getRes("Voucher Type")%></td>
														<td width="9%" class="tb-display" align="left"><%=jbWResGUI.getRes("Voucher Denomination")%></td>
														<td width="14%" class="tb-display" align="left"><%=jbWResGUI.getRes("From Voucher Number")%></td>
														<td width="14%" class="tb-display" align="left"><%=jbWResGUI.getRes("To Voucher Number")%></td>
														<td width="4%" class="tb-display" align="left"><%=jbWResGUI.getRes("Box No")%></td>
														<td width="6%" class="tb-display" align="left"><%=jbWResGUI.getRes("Box Type")%></td>
														<td width="4%" class="tb-display" align="left"><%=jbWResGUI.getRes("Book No")%></td>
														<td width="11%" class="tb-display" align="left"><%=jbWResGUI.getRes("Receipt No")%></td>
														<td width="5%" class="tb-display" align="left"><%=jbWResGUI.getRes("Points")%></td>
														<td width="6%" class="tb-display" align="left"><%=jbWResGUI.getRes("Total Points")%></td>
														<td width="5%" class="tb-display" align="left"><%=jbWResGUI.getRes("Quantity (PCS)")%></td>
														<td width="8%" class="tb-display" align="left"><%=jbWResGUI.getRes("Total Amount ")%>(<%=jbWResGUI.getRes("RM")%>)</td>
													<tr>
													<%		
														if (strAction != null && strAction.equals("search"))
														{
															VoucherIssuanceInquiryDAOAMY voucherIssuanceInquiryDAO 	= new VoucherIssuanceInquiryDAOAMY();
															Collection _rs = voucherIssuanceInquiryDAO.getIssuanceSummaryTab(hParam);
															
															HTTPPage httpPage = new HTTPPage(_rs, jbWebSessionParam.getProfitvvValue(request, "SYSMaxLinesPerPage"));
															jbSession.setAttribute("VoucherTab1HttpPage", httpPage);
															
															rsCllt = httpPage.getFirstPage();
															strPageOfTotal = httpPage.getPageOfTotal(); 
														}
														else if (strAction != null && 
																(strAction.equals("getNextPage") ||
																strAction.equals("getPreviousPage") ||
																strAction.equals("getFirstPage") ||
																strAction.equals("getLastPage")))
														{
															HTTPPage httpPage = (HTTPPage)jbSession.getAttribute("VoucherTab1HttpPage");
															System.out.println("httpPage = " + httpPage);
															if(httpPage != null)
															{
																rsCllt = httpPage.process(strAction);
																strPageOfTotal = httpPage.getPageOfTotal();
															} 
														}
														else if (strAction != null && strAction.equals("getPage"))
														{
															HTTPPage httpPage = (HTTPPage)jbSession.getAttribute("VoucherTab1HttpPage");
															if(httpPage != null)
															{
																String str = request.getParameter("PAGE");
																rsCllt = httpPage.getPage(str);
																strPageOfTotal = httpPage.getPageOfTotal();
															}
														}
														else if(strAction != null && strAction.equals("reset"))
														{   
															jbSession.removeAttribute("VoucherTab1HttpPage");
														}
														
														if (rsCllt != null)
														{
															Iterator iter = rsCllt.iterator();
															String [] str;
															String val = "";
															
															while (iter.hasNext())
															{
																if (strTdBgColor == "tb-display") 
																{
																	strTdBgColor = "tb-display2";
																	strInputColor = "input-display2";
																} else {
																	strTdBgColor = "tb-display";
																	strInputColor = "input-display";
																}
																
																i++;
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
																if(str[11]==null) str[11]="";
													%>
													<tr>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=i%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[0]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[1]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[2]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[3]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[8]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[9]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[10]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[11]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[6]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[7]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[4]%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:left"/></td>
														<td class="<%=strTdBgColor%>"><input type="text" value="<%=webCurrConverter.format(str[5], 2)%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:right"/></td>
													</tr>
													<%
															}//while
														}//if
													%>
												</table>
										<%
											}
											else if(strMode.equals("tab2"))
											{	
										%>
												<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table-border">
													<tr>
														<td width="7%" class="tb-display" align="left"><%=jbWResGUI.getRes("Seq")%></td>
														<td width="25%" class="tb-display" align="left"><%=jbWResGUI.getRes("Voucher Type")%></td>
														<td width="25%" class="tb-display" align="left"><%=jbWResGUI.getRes("Voucher Denomination")%></td>
														<td width="43%" class="tb-display" align="left"><%=jbWResGUI.getRes("Voucher Number")%></td>
													<tr>
													<%
														if (strAction != null && strAction.equals("search"))
														{
															VoucherIssuanceInquiryDAOAMY voucherIssuanceInquiryDAO 	= new VoucherIssuanceInquiryDAOAMY();
															Collection _rs = voucherIssuanceInquiryDAO.getIssuanceDetailTab(hParam);
															
															HTTPPage httpPage = new HTTPPage(_rs, jbWebSessionParam.getProfitvvValue(request, "SYSMaxLinesPerPage"));
															jbSession.setAttribute("VoucherTab2HttpPage", httpPage);
															
															rsCllt = httpPage.getFirstPage();
															strPageOfTotal = httpPage.getPageOfTotal(); 
														}
														else if (strAction != null && 
																(strAction.equals("getNextPage") ||
																strAction.equals("getPreviousPage") ||
																strAction.equals("getFirstPage") ||
																strAction.equals("getLastPage")))
														{
															HTTPPage httpPage = (HTTPPage)jbSession.getAttribute("VoucherTab2HttpPage");
															if(httpPage != null)
															{
																rsCllt = httpPage.process(strAction);
																strPageOfTotal = httpPage.getPageOfTotal();
															}            
														}
														else if (strAction != null && strAction.equals("getPage"))
														{
															HTTPPage httpPage = (HTTPPage)jbSession.getAttribute("VoucherTab2HttpPage");
															if(httpPage != null)
															{
																String str = request.getParameter("PAGE");
																rsCllt = httpPage.getPage(str);
																strPageOfTotal = httpPage.getPageOfTotal();
															}
														}
														else if(strAction != null && strAction.equals("reset"))
														{   
															jbSession.removeAttribute("VoucherTab2HttpPage");
														}
														
														if (rsCllt != null)
														{
															Iterator iter = rsCllt.iterator();
															String [] str;
															String val = "";
															
															while (iter.hasNext())
															{
																if (strTdBgColor == "tb-display") 
																{
																	strTdBgColor = "tb-display2";
																	strInputColor = "input-display2";
																} else {
																	strTdBgColor = "tb-display";
																	strInputColor = "input-display";
																}
																
																i++;
																str = (String [])iter.next();
																if(str[0]==null) str[0]="";
																if(str[1]==null) str[1]="";
																if(str[2]==null) str[2]="";
													%>
														<tr>
															<td class="<%=strTdBgColor%>"><input type="text" value="<%=i%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
															<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[0]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
															<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[1]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
															<td class="<%=strTdBgColor%>"><input type="text" value="<%=str[2]%>" readonly tabindex="-1" class="<%=strInputColor%>"/></td>
														</tr>
													<%
															}//while
														}//if
													%>
												</table>
										<%
											}
										%>
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
						<table>
							<tr>
								<td width="85%">&nbsp;<td>
								<td width="15%"><input name="cbxView" type="button" value="<%=jbWResGUI.getRes("Cancel")%>" onclick="closePage();"/></td>
							</tr>
						</table>
					</tr>
				</table>
				<input type="hidden" name="MODE" id="MODE" value="<%=strMode%>"/>
				<input type="hidden" name="ACTION" id="ACTION"/>
			</fieldset>
		</form>
		<form id="FORM_SUBMIT" method="post" action="<%=BaseURL%>/profit/GV/VoucherIssuanceViewAMY.jsp" name="FORM_SUBMIT"/></form>
	</body>
</html>