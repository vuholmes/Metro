<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.net.*" %>
<%@ page import = "qrcom.util.*" %>
<%@ page import = "qrcom.PROFIT.webbean.HTTPObj.*" %>
<%@ page import = "qrcom.PROFIT.webbean.language.*" %>
<%@ page import = "qrcom.PROFIT.system.*"%>
<%@ page import = "qrcom.PROFIT.files.info.AduserInfo" %>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultCoySub" %>
<%@ page import = "qrcom.PROFIT.files.info.ProfitvvSQL" %>
<%@ page import = "qrcom.PROFIT.files.info.AltDescUtil" %>
<%@ page import = "qrcom.PROFIT.files.dao.local.GV.VoucherProgramTypeDAO" %>

<jsp:useBean id="jbSession" scope="session" class="qrcom.PROFIT.servlet.HTTPSessionAttributeWrapper" />
<jsp:useBean id="jbWebSessionParam" scope="session" class="qrcom.PROFIT.webbean.HTTPObj.WebSessionParam" />

<%
    String lang_code = jbWebSessionParam.getAduserInfo().USR_LANGUAGE();
    WResGUI jbWResGUI          = jbWebSessionParam.getWResGUI(request);
    WResPrompt jbWResPrompt    = jbWebSessionParam.getWResPrompt(request);
    WebAuthorization webAuth   = new WebAuthorization(request);
    
    String BaseURL             = SysParam.getInstance().getBaseURL();
    DefaultCoySub defaultCoySub= new DefaultCoySub(jbWebSessionParam.getAduserInfo());
    
    AduserInfo aduser          = (AduserInfo)jbWebSessionParam.getAduserInfo();
    String strCOY              = aduser.USR_COMP();
    String strCOY_NAME         = HTTPUtilClass.getDesc(lang_code, "COY_NAME", "COYMST", " WHERE COY = '" + strCOY + "'");
    String strUSR_ID           = aduser.USR_ID();
    String strCOY_SUB_DEF      = defaultCoySub.getDEFAULT_COY_SUB();

    String strAction           = request.getParameter( "ACTION" );
    String strCOY_SUB          = request.getParameter( "KEY1" );
    String strVoucher_Type     = request.getParameter( "KEY2" );
    String strReasonCode       = request.getParameter( "KEY3" );
    String strProgramType      = request.getParameter( "KEY4" );
    String strDescription      = request.getParameter( "KEY5" );
    String strMidSearch        = request.getParameter( "KEY6" );

    String SYSGvProgCdLen      = (String)jbWebSessionParam.getProfitvvValue(request, "SYSGvProgCdLen");
    String SYSGvProgDescLen    = (String)jbWebSessionParam.getProfitvvValue(request, "SYSGvProgDescLen");
    HParam hParam = new HParam();

    if(strAction != null) {
        hParam.setActionCode(strAction);
        if(strAction.equals("INIT") || strAction.equals("reset")) {
            strCOY_SUB     = strCOY_SUB_DEF;
        } else if(!strAction.equals("search")) {
            if(request.getParameter( "cboCOY_SUB" ) !=null && !request.getParameter( "cboCOY_SUB" ).equals("")) {
                strCOY_SUB = request.getParameter( "cboCOY_SUB" );
            }
            if(request.getParameter( "cboVoucherType" ) !=null && !request.getParameter( "cboVoucherType" ).equals("")) {
                strVoucher_Type = request.getParameter( "cboVoucherType" );
            }
            if(request.getParameter( "cboReasonCode" ) !=null && !request.getParameter( "cboReasonCode" ).equals("")) {
                strReasonCode = request.getParameter( "cboReasonCode" );
            }
            if(request.getParameter( "txtProgramType" ) !=null && !request.getParameter( "txtProgramType" ).equals("")) {
                strProgramType = request.getParameter( "txtProgramType" );
            }
            if(request.getParameter( "txtDescription" ) !=null && !request.getParameter( "txtDescription" ).equals("")) {
                strDescription = request.getParameter( "txtDescription" );
            }
            if(request.getParameter( "chkDesc" ) !=null && !request.getParameter( "chkDesc" ).equals("")) {
                strMidSearch = request.getParameter( "chkDesc" );
            }
        }
    } else {
        strAction = "";
    }

    if(strCOY != null) {
        hParam.put("COY", strCOY); 
    } else {
        strCOY ="";
    }   
    if(strCOY_SUB != null && strCOY_SUB.trim().length()!=0) {
      hParam.put("COY_SUB", strCOY_SUB);
    } else {
      strCOY_SUB =strCOY_SUB_DEF;
    }
    if(strVoucher_Type!=null) {
        hParam.put("VOUCHER_TYPE", strVoucher_Type);
    } else {
        strVoucher_Type = "";
    } 
    if(strReasonCode!=null) {
        hParam.put("REASON_CODE",strReasonCode);
    } else {
        strReasonCode = "";
    }
    if(strProgramType!=null) {
        hParam.put("PROGRAM_TYPE",strProgramType);
    } else {
        strProgramType = "";
    }
    if(strDescription!=null) {
        hParam.put("PROGRAM_DESC",strDescription);
    } else {
        strDescription = "";
    }
    if(strMidSearch!=null) {
        hParam.put("DESC_MID_SEARCH",strMidSearch);
    } else {
        strMidSearch = "";
    } 
    hParam.put("PAGE_ID", "VoucherProgramTypeSearch");

    request.getSession().setAttribute("VoucherProgramTypeParam", hParam);
%>

<!DOCTYPE HTML> 
<html>
<head>
	<%
		Collection err = (Collection)request.getSession().getAttribute("errorSets");
	    Collection updateSets = (Collection)request.getSession().getAttribute("updateSets");
		if(err != null && !err.isEmpty() || updateSets != null && !updateSets.isEmpty())
		{
	%>
		<script>
			window.open('<%=BaseURL%>/profit/Common/ErrorBatch.jsp', '', 'height=300,width=500,left=0,top=0,resizable=yes,scrollbars=yes');
		</script>
	<%
		}
	%>
    <title><%=jbWResGUI.getRes("Maintain Voucher Program Type")%> </title>
    <jsp:include page="../Common/Header.jsp"/>
    <jsp:include page="../Common/Script.jsp"/> 

    <script>
        function processHTTPPage(strPageAction) {
            'use strict';
            var action = strPageAction;
            var URLstr = '<%=BaseURL%>/profit/GV/VoucherProgramTypeSearch.jsp';
            // To deal with # character in colour field (buildActionURL)
            
            if (strPageAction == 'getPage') {
                action += '&PAGE=' + window.document.FORM.txtGotoPage.value;
            }
            //window.document.FORM.txtItem_desc.value = convertSpecialChar(window.document.FORM.txtItem_desc.value); 
            window.document.FORM_SUBMIT.action = buildActionURL('FORM', URLstr, action, 
            'cboCOY_SUB%cboVoucherType%cboReasonCode%txtProgramType%txtDescription%chkDesc');         
            
            window.document.FORM_SUBMIT.submit();
        }

        function setAction(TYPE)
        {
            'use strict';
            disableButton('Y');
            
            if(TYPE === 'Add') {
                var url = '<%=BaseURL%>/servlet/SvltVoucherProgramType?ACTION='+TYPE;
                window.document.FORM.action = url;
                window.document.FORM.submit();
            } else if (TYPE === 'Edit') {
                var tot = 0;
                var TOTALROWS = window.document.FORM.TOTALROWS.value;
                
                for(var i=1;i<=TOTALROWS;i++) {
                    if(window.document.FORM.elements['chkAction_'+i].checked) {    
                        tot++;
                    }
                }
                
                if(tot != 1) {
                    alert('<%=jbWResPrompt.getRes("Please select one action only")%>');
                    disableButton('N');
                    return false;
                }
            
                var url = '<%=BaseURL%>/servlet/SvltVoucherProgramType?ACTION='+TYPE;
                window.document.FORM.action = url;
                window.document.FORM.submit();
            }
            else if (TYPE === 'View') {
                disableButton('N'); 
                var tot = 0;
                var TOTALROWS = window.document.FORM.TOTALROWS.value;
                
                for(i=1;i<=TOTALROWS;i++) {
                    if(window.document.FORM.elements['chkAction_'+i].checked) {    
                        tot++;
                    }
                }
                
                if(tot != 1) {
                    alert('<%=jbWResPrompt.getRes("Please select one action only")%>');
                    disableButton('N');
                    return false;
                }       
            
                var url = '<%=BaseURL%>/servlet/SvltVoucherProgramType?ACTION='+TYPE;
                window.document.FORM.action = url;
                var n = new Date().getTime();
                window.document.FORM.target = 'viewPromptWindow'+n; 
                window.open('','viewPromptWindow'+n,'height=600,width=800,left=0, top=0, status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes');         
                window.document.FORM.submit();
                window.document.FORM.target = '_self'; 
            }
            else if (TYPE === 'Delete') {
                var tot = 0;
                var TOTALROWS = window.document.FORM.TOTALROWS.value;
                
                for(var i=1;i<=TOTALROWS;i++) {
                    if(window.document.FORM.elements['chkAction_'+i].checked) {
                        if(window.document.FORM.elements['chkAction_'+i].value==='Y') {
                            alert('<%=jbWResPrompt.getRes("Deleted record is not allowed")%>');
                            disableButton('N');
                            return false;
                        }
                        tot++;
                    }
                }
                
                if(tot < 1) {
                    alert('<%=jbWResPrompt.getRes("Please select at least one action")%>');
                    disableButton('N');
                    return false;
                }    
                
                if(confirm('<%=jbWResPrompt.getRes("Are you sure?")%>')) {
                    url = '<%=BaseURL%>/servlet/SvltVoucherProgramType?ACTION='+TYPE;
                    window.document.FORM.action = url;
                    window.document.FORM.submit();          
                } else {
                    disableButton('N');
                    return false;          
                }
            
            }
        }

        function disableButton(value) {
            'use strict';
            if(value === 'Y') {
                window.document.FORM.btAdd.disabled  = true;
                window.document.FORM.btDelete.disabled = true;
                window.document.FORM.btReset.disabled = true;
                window.document.FORM.btView.disabled = true;
                window.document.FORM.btEdit.disabled = true;
            } else {
                window.document.FORM.btAdd.disabled  = false;
                window.document.FORM.btDelete.disabled = false;
                window.document.FORM.btReset.disabled = false;
                window.document.FORM.btView.disabled = false; 
                window.document.FORM.btEdit.disabled = false;
            }
        }
        
        function onChgVouchType() {
            'use strict';
            var url = '<%=BaseURL%>/profit/GV/VoucherProgramTypeSearch.jsp';
            window.document.FORM.action = url;
            window.document.FORM.submit();
        }
    </script>
</head>
<% if((err == null || err.isEmpty()) && (updateSets == null || updateSets.isEmpty())) { %>
<body align="center" onload="setFocus();">
<% } else { %>
<body align="center">
<% } %>

<center>
<form name="FORM" id="FORM" method="post" action="<%=BaseURL%>/servlet/SvltVoucherProgramType" >
    <fieldset class="fieldsettitle">
        <legend><%=jbWResGUI.getRes("Maintain Voucher Program Type")%></legend>
            <table border="0" width="100%" cellpadding="5" cellspacing="0" class="table-border">
            <tr><td>
                <table border="0" width="100%" cellpadding="0" cellspacing="1" class="table-border">
                <tr><td> 
                    <table border="0" width="100%" cellpadding="1" cellspacing="0">
                        <tr><th colspan="7"><%=jbWResGUI.getRes("Search Criteria")%></th></tr>
                        <tr>
                            <td width="15%" class="caption"><%=jbWResGUI.getRes("Company")%></td>
                            <td width="30%">
                                <table width="100%">
                                    <tr>
                                        <td><input type="text" name="txtCOY" id="txtCOY" value="<%=strCOY%>" readonly tabindex="-1" class="input-display"></td>
                                        <td><input type="text" name="txtCOY_NAME" id="txtCOY_NAME" value="<%=strCOY_NAME%>" readonly tabindex="-1" class="input-display"></td>
                                    </tr>
                                </table> 
                            </td>
                            <td width="10%"></td>   
                            <td width="45%"></td>   
                        </tr>
                        <tr>
                            <td class="caption"><%=jbWResGUI.getRes("Subsidiary")%></td>
                            <td>
                                <select name="cboCOY_SUB" class="mandatory" align="left" style="width:98.8%" >
                                <%=MTComboBox.Default(lang_code, strCOY_SUB, "COYSUBMST.COY_SUB", "COYSUBMST.COY_SUB_NAME", "COYSUBMST.COY_SUB_DEL_CD", "COYSUBMST", "WHERE COYSUBMST.COY_SUB IN (SELECT ADOPRCOYSUB.COY_SUB FROM ADOPRCOYSUB WHERE ADOPRCOYSUB.USR_ID='"+ strUSR_ID +"') ORDER BY COYSUBMST.COY_SUB ASC")%>       	  
                                </select>
                            </td>
                            <td width="10%"></td>
                            <td width="45%"></td>
                        </tr>
                        <tr>
                            <td class="caption"><%=jbWResGUI.getRes("Voucher Type")%></td>
                            <td>
                                <select name="cboVoucherType" id="cboVoucherType" onchange="onChgVouchType();" align="left" style="width:98.7%" >
                                <%=MTComboBox.Default(lang_code, strVoucher_Type, "GVTYPEMST.GV_TYPE", "GVTYPEMST.GV_TYPE_DESC",  "GVTYPEMST", " ")%>       	  
                                </select>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td class="caption"><%=jbWResGUI.getRes("Reason Code")%></td>
                            <td>
                                <select name="cboReasonCode" id="cboReasonCode" align="left" style="width:98.7%" >
                                <%=MTComboBox.Default(lang_code, strReasonCode, "GVRSNMST.RSN_CD", "GVRSNMST.RSN_DESC",  "GVRSNMST", "WHERE GVRSNMST.GV_TYPE = '" + strVoucher_Type + "' AND GVRSNMST.DEL_CD <> 'Y' ORDER BY GVRSNMST.RSN_CD, GVRSNMST.RSN_DESC")%>       	  
                                </select>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td class="caption"><%=jbWResGUI.getRes("Program Type")%></td>
                            <td><input align="left" style="width:98.7%"  type="text" name="txtProgramType" id="txtProgramType" value="<%=strProgramType%>" maxlength="<%=SYSGvProgCdLen%>"/></td>    
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td align="left" class="caption"><%=jbWResGUI.getRes("Description")%></td>
                            <td align="left" ><input align="left" style="width:98.7%"  type="text" name="txtDescription" id="txtDescription" value="<%=strDescription%>" maxlength="<%=SYSGvProgDescLen%>"></td>    
                            <td align="left"><input align="left" type="checkbox" name="chkDesc" id="chkDesc" <%=HTTPCheckBox.isChecked(strMidSearch)%>></td>
                            <td align="left" class="caption"><%=jbWResGUI.getRes("Contain")%></td>
                        </tr>
                        <tr>
                            <td colspan="7">
                                <table width="100%" cellpadding="1" cellspacing="1" border="0">
                                <tr>
                                    <td width="76%">&nbsp;</td>
                                    <td width="12%"><input type="button" name="btReset" id="btReset" value="<%=jbWResGUI.getRes("Reset")%>" onclick="setFormReset(this.form,'<%=BaseURL%>/profit/GV/VoucherProgramTypeSearch.jsp')"></td>
                                    <td width="12%"><input type="button" name="btSearch" id="btSearch"  value="<%=jbWResGUI.getRes("Search")%>" onclick="if(Validate(this.form,'search','','Y'))setFormSubmit('<%=BaseURL%>/profit/GV/VoucherProgramTypeSearch.jsp','S','cboCOY_SUB%cboVoucherType%cboReasonCode%txtProgramType%txtDescription%chkDesc');"></td>
                                </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td height="5" colspan="5"></td>
                        </tr>
                    </table>
                </td></tr>
                </table>
            </td></tr>
            <tr><td>
                <table BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="3" class="table-border">
                    <tr><th COLSPAN="8"><%=jbWResGUI.getRes("Search Result")%></th></tr>
                    <tr><td>
                        <table width="100%" cellspacing="1" cellpadding="0" class="tb-list">
                            <tr>
                                <td width="5%" align="left" class="tb-display" ><%=jbWResGUI.getRes("Action")%></td>
                                <td width="25%" align="left" class="tb-display" ><%=jbWResGUI.getRes("Voucher Type")%></td>
                                <td width="20%" align="left" class="tb-display" ><%=jbWResGUI.getRes("Reason Code")%></td>
                                <td width="15%" align="left" class="tb-display" ><%=jbWResGUI.getRes("Program Type")%></td>
                                <td width="25%"  align="left" class="tb-display" ><%=jbWResGUI.getRes("Description")%></td>
                                <td width="10%" align="left" class="tb-display" style="width:90%"><%=jbWResGUI.getRes("Deleted")%></td>
                            </tr>
        <%         
            VoucherProgramTypeDAO voucherProgramTypeDAO = new VoucherProgramTypeDAO();
            String strPageOfTotal   = "";
            int i = 0;
            String strLinePerPage   = jbWebSessionParam.getProfitvvValue(request, "SYSMaxLinesPerPage");
            Collection rsCllt = null;       
            String strTdBgColor   = "tb-display";
            String strInputColor  = "input-display";   
            if (strAction != null && strAction.equals("search")) {
                HTTPPageQueryBased httpPageQueryBased = new HTTPPageQueryBased(hParam, voucherProgramTypeDAO, strLinePerPage);
                jbSession.setAttribute("GvrsnmstHttpPageQueryBased", httpPageQueryBased);
                rsCllt = httpPageQueryBased.getFirstPage();
                strPageOfTotal = httpPageQueryBased.getPageOfTotal();    
            } else if (strAction != null &&
                (strAction.equals("getNextPage") ||
                strAction.equals("getPreviousPage") ||
                strAction.equals("getFirstPage") ||
                strAction.equals("getLastPage"))) {
                HTTPPageQueryBased httpPage = (HTTPPageQueryBased)jbSession.getAttribute("GvrsnmstHttpPageQueryBased");
                if (httpPage != null) {
                    rsCllt = httpPage.process(strAction);
                    strPageOfTotal = httpPage.getPageOfTotal();
                }
            } else if (strAction != null && strAction.equals("getPage")) {
                HTTPPageQueryBased httpPage = (HTTPPageQueryBased)jbSession.getAttribute("GvrsnmstHttpPageQueryBased");
                if (httpPage != null) {
                    String str = request.getParameter( "PAGE" );
                    rsCllt = httpPage.getPage(str);
                    strPageOfTotal = httpPage.getPageOfTotal();
                }
            } else if(strAction != null && strAction.equals("reset")) {
                jbSession.removeAttribute("GvrsnmstHttpPageQueryBased");
                request.getSession().removeAttribute("passBackData");
            }
            else{
                jbSession.removeAttribute("GvrsnmstHttpPageQueryBased");
                request.getSession().removeAttribute("passBackData");
            }
            if (rsCllt != null) {
			    Iterator iter = rsCllt.iterator();
                String [] str;
                while (iter.hasNext()) {
                    i++;
                    if (strTdBgColor == "tb-display") {
                        strTdBgColor = "tb-display2";
                        strInputColor = "input-display2";
                    } else {
                        strTdBgColor = "tb-display";
                        strInputColor = "input-display";
                    }
                    
                    str = (String []) iter.next();
                 
                    if (str[0]==null) {
                        str[0]="";
                    }                 
                    if (str[1]==null) {
                        str[1]="";
                    }                 
                    if (str[2]==null) {
                        str[2]="";
                    }         
                    if (str[3]==null) {
                        str[3]="";
                    }                  
                    if (str[4]==null) {
                        str[4]="";
                    }
                    if (str[5]==null) {
                        str[5]="";
                    }                  
                    if (str[6]==null) {
                        str[6]="";
                    }                 
                    if (str[7]==null) {
                        str[7]="";
                    }
                    if (str[8]==null) {
                        str[8]="";
                    }
                    String strVoucherTypeDesc = str[0] + " - " + str[8];
        %>   
                
                  <tr>
                      <td class="<%=strTdBgColor%>" ><input  type="checkbox" name="chkAction_<%=i%>"  ></td>
                      <td class="<%=strTdBgColor%>" align="left" ><textarea align="left" style="border:none;overflow:auto;height:100%;width:98.5%" name="strVoucherTypeDesc<%=i%>" readonly tabindex="-1" class="<%=strInputColor%>"><%=strVoucherTypeDesc%></textarea></td>
                      <td class="<%=strTdBgColor%>" align="left" ><input    align="left" style="width:98.5%" name="txtReasonCode_<%=i%>" value="<%=str[1]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
                      <td class="<%=strTdBgColor%>" align="left" ><input    align="left" style="width:98.5%" name="txtProgramType_<%=i%>" value="<%=str[2]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
                      <td class="<%=strTdBgColor%>" align="left" ><textarea align="left" style="border:none;overflow:auto;height:98.5%%;width:98.5%" rows="0" name="txtProgramTypeDesc_<%=i%>" readonly tabindex="-1" class="<%=strInputColor%>"><%=str[3]%></textarea></td>
                      <td class="<%=strTdBgColor%>" align="left" ><input    align="left" style="width:96%" name="txtDeleted_<%=i%>" value="<%=str[4]%>" readonly tabindex="-1" class="<%=strInputColor%>"></td>
                      <input type="hidden" name="txtLastVersion_<%=i%>" value="<%=str[5]%>">
                      <input type="hidden" name="txtCoy_<%=i%>" value="<%=str[6]%>">
                      <input type="hidden" name="txtCoySub_<%=i%>" value="<%=str[7]%>">
                      <input type="hidden" name="txtVoucherType_<%=i%>" value="<%=str[0]%>">
                      
                  </tr>                
        <%
                }
            }
        %>
                        </table>
                    </td></tr>
                </table>
            </td></tr>
            <tr><td>
                <table BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="1">
                    <tr>
                    <td td colspan="5">
                        <%@ include file="../Common/PageControl.jsp" %>
                    </td>   
                    </tr>   
                    <tr>   
                    <td width="52%">&nbsp;</td>
                    <td width="12%"><input name="btAdd" id="btAdd" type="button" value="<%=jbWResGUI.getRes("Add")%>" onclick="setAction('Add')"></td>
                    <td width="12%"><input name="btEdit" id="btEdit" type="button" value="<%=jbWResGUI.getRes("Edit")%>" onclick="setAction('Edit')"></td>
                    <td width="12%"><input name="btDelete" id="btDelete" type="button" value="<%=jbWResGUI.getRes("Delete")%>" onclick="setAction('Delete')"></td>
                    <td width="12%"><input name="btView" id="btView" type="button" value="<%=jbWResGUI.getRes("View")%>" onclick="setAction('View')"></td>
                    </tr>     
                </table>
            </td></tr>
            </table>
    </fieldset>
    <input type="hidden" name=ACTION id=ACTION >  
    <input type="hidden" name="TOTALROWS" value="<%=i%>" id="TOTALROWS">
</form>
<form id=FORM_SUBMIT method="post" action="<%=BaseURL%>/profit/GV/VoucherProgramTypeSearch.jsp" name=FORM_SUBMIT>
</form>

    <script>
   	setWebAuthorization("btAdd", "<%=webAuth.isAddAllowed()%>");
   	setWebAuthorization("btView", "<%=webAuth.isViewAllowed()%>");
    setWebAuthorization("btDelete", "<%=webAuth.isDeleteAllowed()%>");
    setWebAuthorization("btEdit", "<%=webAuth.isEditAllowed()%>");  
   </script>
</center>
</body>
</html> 