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
<%-- <%@ page import = "qrcom.PROFIT.files.dao.local.GV.VoucherProgramTypeDAO" %> --%>

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

            </table>
    </fieldset>
</form>
</center>
</body>
</html> 