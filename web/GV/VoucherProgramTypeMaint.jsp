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
<%@ page import = "qrcom.PROFIT.files.info.GvprogtypmstInfo" %>
<%@ page import = "qrcom.PROFIT.files.info.GvtypemstSQLAMY" %>
<%@ page import = "qrcom.PROFIT.files.info.AltDescUtil" %>

<jsp:useBean id="jbSession" scope="session" class="qrcom.PROFIT.servlet.HTTPSessionAttributeWrapper" />
<jsp:useBean id="jbWebSessionParam" scope="session" class="qrcom.PROFIT.webbean.HTTPObj.WebSessionParam" />
<jsp:useBean id="webCurrConverter" scope="session" class="qrcom.util.CurrencyConverter" />

<%
    String lang_code           = jbWebSessionParam.getAduserInfo().USR_LANGUAGE();
    WResGUI jbWResGUI          = jbWebSessionParam.getWResGUI(request);
    WResPrompt jbWResPrompt    = jbWebSessionParam.getWResPrompt(request);
    WebAuthorization webAuth   = new WebAuthorization(request);
    
    String BaseURL             = SysParam.getInstance().getBaseURL();
    DefaultCoySub defaultCoySub= new DefaultCoySub(jbWebSessionParam.getAduserInfo());
    
    AduserInfo aduser          = (AduserInfo)jbWebSessionParam.getAduserInfo();
    String strCOY              = aduser.USR_COMP();
    String strUSR_ID           = aduser.USR_ID();
    String strCOY_SUB_DEF      = defaultCoySub.getDEFAULT_COY_SUB();
    
    String strAction           = request.getParameter( "ACTION" );
    String jbMaintType         = (String) jbSession.getAttribute("jbMaintType");   
    String strCOY_SUB          = request.getParameter( "cboCOY_SUB" );
    String strVoucher_Type     = request.getParameter( "cboVoucherType" );
    String strReasonCode       = request.getParameter( "cboReasonCode" );
    String strProgramType      = request.getParameter( "txtProgramType" );
    String strDescription      = request.getParameter( "txtProgramDesc" );
    String strCheckeDeleted      = request.getParameter( "chkDeleted" );

    String SYSGvProgCdLen      = (String)jbWebSessionParam.getProfitvvValue(request, "SYSGvProgCdLen");
    String SYSGvProgDescLen    = (String)jbWebSessionParam.getProfitvvValue(request, "SYSGvProgDescLen");

    if(jbMaintType!=null && jbMaintType.length() > 0 ){
        jbSession.setAttribute("jbMaintTypeStr", jbMaintType);
    }

    GvprogtypmstInfo gvprogtypmstInfo = (GvprogtypmstInfo) jbSession.getAttribute("gvprogtypmstInfo");
    if(!strAction.equals("error")) {
        if(jbMaintType.equals("Add")) {
            gvprogtypmstInfo = new GvprogtypmstInfo();
            gvprogtypmstInfo.setCOY(strCOY);
            gvprogtypmstInfo.setCOY_SUB(strCOY_SUB_DEF) ;
            if(strAction.equals("Add")) {
                strCOY_SUB = strCOY_SUB_DEF;
                strVoucher_Type = "";
                strReasonCode = "";
                strProgramType = "";
                strDescription = "";
                strCheckeDeleted = "";
            }
        }
        if(jbMaintType.equals("Edit") || jbMaintType.equals("View")) {
            strDescription = gvprogtypmstInfo.PROG_DESC();
            strCheckeDeleted = gvprogtypmstInfo.DEL_CD();
        }
    }

    HParam hParam = new HParam();
    if (strCOY!=null) {
        hParam.put("COY", strCOY); 
    } else {
        strCOY ="";
    }   
    if(gvprogtypmstInfo.COY_SUB()!=null && gvprogtypmstInfo.COY_SUB().trim().length()!=0) {
        hParam.put("COY_SUB", gvprogtypmstInfo.COY_SUB());
    }
    if(gvprogtypmstInfo.GV_TYPE()!=null && gvprogtypmstInfo.GV_TYPE().trim().length()!=0) {
        hParam.put("VOUCHER_TYPE", gvprogtypmstInfo.GV_TYPE());
    }
    if(gvprogtypmstInfo.RSN_CD()!=null && gvprogtypmstInfo.RSN_CD().trim().length()!=0) {
        hParam.put("REASON_CODE", gvprogtypmstInfo.RSN_CD());
    }
    if(gvprogtypmstInfo.PROG_CD()!=null && gvprogtypmstInfo.PROG_CD().trim().length()!=0) {
        hParam.put("PROGRAM_TYPE", gvprogtypmstInfo.PROG_CD());
    }
    if(gvprogtypmstInfo.PROG_DESC()!=null && gvprogtypmstInfo.PROG_DESC().trim().length()!=0) {
        hParam.put("PROGRAM_DESC", gvprogtypmstInfo.PROG_DESC());
    }
    
    String strCOY_NAME        = HTTPUtilClass.getDesc(lang_code, "COY_NAME", "COYMST", " WHERE COY = '" + gvprogtypmstInfo.COY() + "'");
    String strCoySubName      = HTTPUtilClass.getDesc(lang_code, "COY_SUB_NAME", "COYSUBMST", " WHERE COY = '" +  gvprogtypmstInfo.COY() + "' AND COY_SUB = '"+gvprogtypmstInfo.COY_SUB()+"'");

    GvtypemstSQLAMY gvtypemstSQL = new GvtypemstSQLAMY();
    gvtypemstSQL.setCOY(gvprogtypmstInfo.COY());
    gvtypemstSQL.setCOY_SUB(gvprogtypmstInfo.COY_SUB());
    gvtypemstSQL.setGV_TYPE(gvprogtypmstInfo.GV_TYPE());
    gvtypemstSQL.getByKey();

    request.getSession().setAttribute("VoucherProgramTypeMaintParam", hParam);
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
  
    <jsp:include page="../Common/Header.jsp"/>
    <jsp:include page="../Common/Script.jsp"/>

    <script>
        function setAction(TYPE){
            'use strict';
            disableButton('Y');
            
            if(TYPE === 'addVoucherProgramType') {
                if(!CheckMandatory(window.document.FORM)) {
                    disableButton('N');
                    return false;  
                }       
            
                var url = '<%=BaseURL%>/servlet/SvltVoucherProgramType?ACTION='+TYPE;
                window.document.FORM.action = url;
                window.document.FORM.submit();
            }
            else if(TYPE === 'editVoucherProgramType') {
                if(!CheckMandatory(window.document.FORM)) {
                    disableButton('N');
                    return false;  
                }       
            
                var url = '<%=BaseURL%>/servlet/SvltVoucherProgramType?ACTION='+TYPE;
                window.document.FORM.action = url;
                window.document.FORM.submit(); 
            }
            else if(TYPE === 'cancel') {
                var url = '<%=BaseURL%>/servlet/SvltVoucherProgramType?ACTION='+TYPE;
                window.document.FORM.action = url;
                window.document.FORM.submit();
            }
        }

        function disableButton(value) {
            'use strict';
            if(value === 'Y') {
                    window.document.FORM.btSave.disabled  = true;
                    window.document.FORM.btCancel.disabled = true;      
            } else {
                    window.document.FORM.btSave.disabled  = false;
                    window.document.FORM.btCancel.disabled = false;      
            }
        }

        function closePage() {  
            'use strict';
            window.open('','_self','');
            window.close();
        }

        function onChgVouchType() {
            'use strict';
            var url = '<%=BaseURL%>/profit/GV/VoucherProgramTypeMaint.jsp';
            window.document.FORM.action = url;
            window.document.FORM.submit();
        }
    </script>
    <title><%=jbWResGUI.getRes("Maintain Voucher Program Type")%>  -  <%=jbMaintType%></title>
</head>
<% if((err == null || err.isEmpty()) && (updateSets == null || updateSets.isEmpty())) { %>
<body align="center" onload="setFocus();">
<% } else { %>
<body align="center">
<% } %>
<center>
<form name="FORM" id="FORM" method="post" action="<%=BaseURL%>/servlet/SvltVoucherProgramType" >
<fieldset class="fieldsettitle"><legend><%=jbWResGUI.getRes("Maintain Program Type")%> - <%=jbMaintType%></legend>
    <table width="100%" cellpadding="5" cellspacing="0">
	    <table BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="3">	
             <tr><th colspan="4"><%=jbWResGUI.getRes("Program Type Details")%></th></tr>
            <tr>
                <td width="35%" class="caption"> <%=jbWResGUI.getRes("Company")%> </td>              
                <td width="25%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr> 
                    <td width="15%"> <input  type="text" name="txtCOY" id="txtCOY" value="<%=strCOY%>" readonly tabindex="-1" class="input-display"></td>
                    <td width="85%"> <input style="width:100.8%" type="text" name="txtComName" id="txtComName" maxlength="3" value="<%=strCOY_NAME%>" readonly tabindex="-1" class="input-display"></td>
                    </tr> 
                </table>
                </td>
                <td width="5%"></td>
                <td width="25%"></td>
            </tr>
            <tr>
                <td width="35%" class="caption"> <%=jbWResGUI.getRes("Subsidiary")%> </td>  
                <% if (jbMaintType.equals("Add")) { %>
                <td width="25%">
                    <select name="cboCOY_SUB" id="cboCOY_SUB" class="mandatory">
                    <%=MTComboBox.Default(lang_code, strCOY_SUB , "COYSUBMST.COY_SUB", "COYSUBMST.COY_SUB_NAME", "COYSUBMST.COY_SUB_DEL_CD", "COYSUBMST", "WHERE COYSUBMST.COY_SUB IN (SELECT ADOPRCOYSUB.COY_SUB FROM ADOPRCOYSUB WHERE ADOPRCOYSUB.USR_ID='"+ strUSR_ID +"') ORDER BY COYSUBMST.COY_SUB ASC")%>       	  
                    </select>
                </td>
                <% }else { %>
                <td width="25%">
                    <table border="0" width="100%" cellspacing="0" cellpadding="0">
                    <tr> 
                        <td width="15%"><input  type="text" name="cboCOY_SUB" id="cboCOY_SUB" maxlength="3" value="<%=gvprogtypmstInfo.COY_SUB()%>" readonly tabindex="-1" class="input-display"></td>
                        <td width="85%"><input style="width:100.8%"  type="text" name="coySubName" id="coySubName" maxlength="3" value="<%=strCoySubName%>" readonly tabindex="-1" class="input-display"></td>
                    </tr>
                    </table>
                </td>
                <% } %>
                <td width="5%"></td>
                <td width="25%"></td>
            </tr>
            <tr>
                <td width="35%" class="caption"> <%=jbWResGUI.getRes("Voucher Type")%> </td>           
                <% if (jbMaintType.equals("Add")) { %>            
                <td width="25%">
                    <select name="cboVoucherType" id="cboVoucherType" onchange="onChgVouchType();" class="mandatory" >
                    <%=MTComboBox.Default(lang_code, strVoucher_Type, "GVTYPEMST.GV_TYPE", "GVTYPEMST.GV_TYPE_DESC",  "GVTYPEMST", " WHERE DEL_CD <> 'Y' ORDER BY GV_TYPE ")%>       	  
                    </select>                                   
                </td>
                <% } else { %>
                <td width="25%"><input style = "width:100%"  type="text" name="cboVoucherType" id="cboVoucherType" value="<%=gvprogtypmstInfo.GV_TYPE()%> - <%=gvtypemstSQL.GV_TYPE_DESC()%>" readonly tabindex="-1" class="input-display"></td> 
                <input type="hidden" name="cboVoucherTypeHidden" value="<%=gvprogtypmstInfo.GV_TYPE()%>">
                <% } %>            
                
                <td width="5%"></td>
                <td width="25%"></td>
            </tr>
            <tr>
                <td width="35%" class="caption"> <%=jbWResGUI.getRes("Reason Code")%> </td>           
                <% if (jbMaintType.equals("Add")) { %>            
                <td width="25%">
                    <select name="cboReasonCode" id="cboReasonCode" class="mandatory" >
                    <%=MTComboBox.Default(lang_code, strReasonCode, "GVRSNMST.RSN_CD", "GVRSNMST.RSN_DESC",  "GVRSNMST", " WHERE GVRSNMST.GV_TYPE = '" + strVoucher_Type + "' AND DEL_CD <> 'Y' ORDER BY GVRSNMST.RSN_CD, GVRSNMST.RSN_DESC ")%>       	  
                    </select>                                   
                </td>
                <% } else { %>
                <td width="25%"><input style = "width:100%"  type="text" name="cboReasonCode" id="cboReasonCode" value="<%=gvprogtypmstInfo.RSN_CD()%>" readonly tabindex="-1" class="input-display"></td> 
                <input type="hidden" name="cboReasonCodeHidden" value="<%=gvprogtypmstInfo.RSN_CD()%>">
                <% } %>            
                
                <td width="5%"></td>
                <td width="25%"></td>
            </tr>

            <tr>
                <td width="35%" class="caption"> <%=jbWResGUI.getRes("Program Type")%> </td>
                <% if (jbMaintType.equals("Add")){ %>
                <td width="25%"><input type="text" name="txtProgramType" id="txtProgramType" value="<%=strProgramType%>"  class="mandatory" maxlength="<%=SYSGvProgCdLen%>"></td>
                <% }else { %>
                <td width="25%"><input style = "width:100%"  type="text" name="txtProgramType" id="txtProgramType" value="<%=gvprogtypmstInfo.PROG_CD()%>" readonly tabindex="-1" class="input-display"></td>
                <% } %>            
                
                <td width="5%"></td>
                <td width="25%"></td>
            </tr>    

            <tr>
                <td width="35%" class="caption"> <%=jbWResGUI.getRes("Description")%> </td>
                <% if (jbMaintType.equals("View")){ %>
                <td width="25%"><input style = "width:100%"  type="text" name="txtProgramDesc"  id="txtProgramDesc" value="<%=gvprogtypmstInfo.PROG_DESC()%>" readonly tabindex="-1" class="input-display"></td>
                <% }else { %>
                <td width="25%"><input style = "width:100%"  type="text" name="txtProgramDesc" id="txtProgramDesc" value="<%=strDescription%>"  class="mandatory" maxlength="<%=SYSGvProgDescLen%>"></td>
                <% } %>
                
                <td width="5%"></td>
                <td width="25%"></td>
            </tr>

            <tr>
                <td width="35%" class="caption"> <%=jbWResGUI.getRes("Deleted")%></td>           
                <% if (jbMaintType.equals("Add") || jbMaintType.equals("View")) { %>           
                <td width="25%"><input style="width:10%" type="checkbox" name="chkDeleted" id="chkDeleted" disabled <%=HTTPCheckBox.isChecked(strCheckeDeleted)%> </td>
                <% } else { %>
                <td width="25%"><input style="width:10%" type="checkbox" name="chkDeleted" id="chkDeleted" <%=HTTPCheckBox.isChecked(gvprogtypmstInfo.DEL_CD())%> </td>
                <% } %>            
                
                <td width="5%"></td>
                <td width="25%"></td>
            </tr>
            <tr>
                <td colspan="7">
                    <table width="100%" cellpadding="1" cellspacing="1" border="0">
                        <tr>
                            <td width="76%">&nbsp;</td>
                            <% if (jbMaintType.equals("Add")){ %>
                            <td width="12%"><input type="button" name="btSave" id="btSave" value="<%=jbWResGUI.getRes("Save")%>" onclick="setAction('addVoucherProgramType');" ></td>
                            <td width="12%"><input type="button" name="btCancel" id="btCancel" value="<%=jbWResGUI.getRes("Cancel")%>" onclick="setAction('cancel');"></td>
                            <% }else if (jbMaintType.equals("Edit")){ %>
                            <td width="12%"><input type="button" name="btSave" id="btSave" value="<%=jbWResGUI.getRes("Save")%>" onclick="setAction('editVoucherProgramType');" ></td>
                            <td width="12%"><input type="button" name="btCancel" id="btCancel" value="<%=jbWResGUI.getRes("Cancel")%>" onclick="setAction('cancel')"></td>
                            <% }else { %>
                            <td width="12%"></td>
                            <td width="12%"><input type="button" name="btCancel" id="btCancel" value="<%=jbWResGUI.getRes("Cancel")%>" onclick="closePage();"></td>
                            <% } %>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </table>
</fieldset>
      
    <input type="hidden" name="USR_ID" value="<%=strUSR_ID%>">
    <input name="SEARCH_ACTION" id="SEARCH_ACTION" type="hidden" >
    <input name="RETURN_VALUE"  id="RETURN_VALUE"  type="hidden">
    <input name="ACTION"        id="ACTION"        type="hidden" >
    <INPUT name=LAST_VERSION id=LAST_VERSION value="<%=gvprogtypmstInfo.LAST_VERSION()%>" type=hidden>
</form>
<form id=FORM_SUBMIT method="post" action="<%=BaseURL%>/profit/GV/VoucherProgramTypeMaint.jsp" name=FORM_SUBMIT>
</form>

</center>
</body>
</html>