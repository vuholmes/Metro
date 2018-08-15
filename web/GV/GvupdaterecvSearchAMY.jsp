<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language = "java" errorPage = "../Common/ExceptionError.jsp" %>
<%@ page import = "java.net.*" %>
<%@ page import = "java.util.*" %>
<%@ page import = "qrcom.util.*" %>
<%@ page import = "qrcom.PROFIT.files.dao.local.BP.SalesplanDAO" %>
<%@ page import = "qrcom.PROFIT.webbean.HTTPObj.*" %>
<%@ page import = "qrcom.PROFIT.webbean.language.*" %>
<%@ page import = "qrcom.PROFIT.files.info.AduserInfo"%>
<%@ page import = "qrcom.PROFIT.files.info.CoymstInfo"%>
<%@ page import = "qrcom.PROFIT.files.info.CoymstSQL" %>
<%@ page import = "qrcom.PROFIT.files.info.AltDescUtil"%>
<%@ page import = "qrcom.PROFIT.shared.Default.DefaultStore" %>
<%@ page import = "qrcom.PROFIT.system.*"%>
<%@ page import = "qrcom.PROFIT.files.dao.local.GV.GvupdaterecvDAOAMY"%>

<jsp:useBean id="jbSession" scope="session" class="qrcom.PROFIT.servlet.HTTPSessionAttributeWrapper" />
<jsp:useBean id="jbWebSecure" scope="request" class="qrcom.PROFIT.webbean.HTTPObj.WebSecure" />
<jsp:useBean id="jbWebSessionParam" scope="session" class="qrcom.PROFIT.webbean.HTTPObj.WebSessionParam" />
<jsp:useBean id="aduserInfo" scope="session" class="qrcom.PROFIT.files.info.AduserInfo" />
<jsp:useBean id="webCurrConverter" scope="session" class="qrcom.util.CurrencyConverter" />
   <% 

      WResGUI jbWResGUI = jbWebSessionParam.getWResGUI(request);
      String lang_code = jbWebSessionParam.getAduserInfo().USR_LANGUAGE();
      WResPrompt jbWResPrompt = jbWebSessionParam.getWResPrompt(request);
      WebAuthorization webAuth = new WebAuthorization(request);

      MTComboBox MTComboBox = new MTComboBox();
      StrmstComboBox strmstComboBox = new StrmstComboBox(request);
      CoysubmstComboBox coysubmstComboBox = new CoysubmstComboBox(request);  
      AduserInfo aduser = (AduserInfo)jbWebSessionParam.getAduserInfo();  

      GvupdaterecvDAOAMY gvupdaterecvDAO = new GvupdaterecvDAOAMY();
      

      String storeMaxLen = jbWebSessionParam.getProfitvvValue(request, "SYSStoreLength");
      String SYSGVXfrNoLength = jbWebSessionParam.getProfitvvValue(request, "SYSGVXfrNoLength");
       
      String strAction      = request.getParameter( "ACTION" );

      String strCoy = request.getParameter("KEY1"); 
      String strCoySub = request.getParameter("KEY2"); 
      String strIssueStore =request.getParameter("KEY3"); 
      String strTransNo =request.getParameter("KEY4"); 
      String strRecvStore = request.getParameter("KEY5"); 
      String strDate1 = request.getParameter("KEY6"); 
      String strRecvBy = request.getParameter("KEY7"); 
      String strStatus = request.getParameter("KEY8"); 


      strCoy = aduser.USR_COMP();
      CoymstSQL coymstSQL = new CoymstSQL();
      coymstSQL.setCOY(strCoy);
      coymstSQL.getByKey();
      String strComName = AltDescUtil.getDescForHtml(lang_code, coymstSQL.COY_NAME());  

      String BaseURL = SysParam.getInstance().getBaseURL();
      String strUsr_Id = aduserInfo.USR_ID();
      
      String strSelectMode = request.getParameter( "SELECT_MODE" );
      if (strSelectMode == null) strSelectMode = "";
      
      DefaultStore defaultStoreObj = new DefaultStore(aduser);
      String default_Store = defaultStoreObj.getDefaultStore();
     
    
      String defaultCoysub = "";
      //String defaultStore = "";
      if (strAction != null && (strAction.equals("INIT") || strAction.equals("reset")))
      {
         
         defaultCoysub =  HTTPUtilClass.getDesc("COY_SUB", "ADOPRCOYSUB", " WHERE USR_ID = '" + strUsr_Id + "' AND DEFAULT_COY_SUB = 'Y'");
         strRecvStore  =  default_Store;    
      }
    
      HParam hParam = new HParam();

      if (strCoySub == null)
          strCoySub = defaultCoysub;

      if (strAction != null)
         hParam.setActionCode(strAction);
      
      if (strCoy != null)
         hParam.put("COY", strCoy);
      else
         strCoy = "";

      if (strCoySub != null)
         hParam.put("COY_SUB", strCoySub);
      else
         strCoySub = "";  
         
      if (strIssueStore != null)
         hParam.put("STORE_FROM", strIssueStore);
      else
         strIssueStore = "";   
      
      if (strRecvStore != null)
         hParam.put("STORE_TO", strRecvStore);
      else
         strRecvStore = "" ;
      
      if (strTransNo != null)
         hParam.put("XFR_NO", strTransNo);
      else
         strTransNo = ""; 
         
      if (strRecvBy != null)
         hParam.put("RECV_BY", strRecvBy);
      else
         strRecvBy = "";

      if (strDate1 != null)
         hParam.put("DATE_RECV", strDate1);
      else
         strDate1 = "";

      if (strStatus != null)
         hParam.put("STATUS", strStatus);
      else
         strStatus = "";   

      hParam.put("USR_ID", strUsr_Id);
     hParam.put("USR_LANGUAGE", lang_code);

   Collection err = (Collection)request.getSession().getAttribute("errorSets");
   if (err != null)
   {
%>
   <script> 
   window.open('<%=BaseURL%>/profit/Common/Error.jsp', '', 'height=300, width=550, resizable=yes, scrollbars=yes'); 
   </script>       
<%
   }
%>

<html>
   <HEAD>
    <jsp:include page="../Common/Header.jsp"/>
    <jsp:include page="../Common/Script.jsp"/>
    
  <script>
  //PageControl.jsp needs this function.
    function processHTTPPage(strPageAction)
    {
      'use strict';
      
      var ACTION = strPageAction;
      var URLstr = '<%=BaseURL%>/profit/GV/GvupdaterecvSearchAMY.jsp';

      if (strPageAction === 'getPage')
      {
        ACTION += '&PAGE=' + window.document.FORM.txtGotoPage.value;
      }

      window.document.FORM_SUBMIT.action = buildActionURL('FORM', URLstr, ACTION, 'txtCoy%cboRegCoySub%txtIssueStore%txtTransNo%txtRecvStore%DATE_RTN1%txtRecvBy%txtStatus');
      window.document.FORM_SUBMIT.submit();
    }

   function SearchCriteria(URLStr,txtField,search_action)
   {  
   'use strict'; 
  
    if(search_action==='STORE'){
      window.document.FORM.hiddenField.value = txtField;
      window.document.FORM.SEARCH_ACTION.value = search_action;
      URLStr = URLStr + '?ACTION=search&FILTER1=STRMST.store_del_cd!=\'Y\' ';
        window.open(URLStr,'yes','height=600,width=800,left=0, top=0, status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes');
    }
    
    if(search_action==='SELECT_USER'){
      window.document.FORM.hiddenField.value = txtField;
      window.document.FORM.SEARCH_ACTION.value = search_action;
       window.open(URLStr,'yes','height=600,width=800,left=0, top=0, status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes');
    }
   }  

function onchangeStore(txtField,val)
  {
  'use strict';
    var TargetClass    = 'qrcom.PROFIT.webbean.dao.MT.DefaultStrmstWebBean';
    var TargetMethod   = 'verifyStoreByCoySubUserID';
    var CallbackMethod = 'onChangeStoreCalledBack';
    var objCoySub = eval('window.document.FORM.cboRegCoySub');

    if (objCoySub.value === '')
    {       
       alert('<%=jbWResPrompt.getRes("NO_COY_SUB")%>');
       
       if(txtField === 'txtIssueStore'){
        window.document.FORM.txtIssueStore.value='';
        window.document.FORM.txtIssueStore.focus();
       }
         else if(txtField === 'txtRecvStore'){
          window.document.FORM.txtRecvStore.value='';
          window.document.FORM.txtRecvStore.focus();
         }
       return; 
    }
      
      window.document.FORM.hiddenField.value = txtField;
    var Params  = '&STORE='   + val;      
    Params  += '&USR_ID=<%=strUsr_Id%>';
    Params  += '&COY='     + window.document.FORM.txtCoy.value;
    Params  += '&COY_SUB=' + window.document.FORM.cboRegCoySub.value;
    var baseURL = '<%=BaseURL%>';   

    invokeTargetClass(baseURL, TargetClass, TargetMethod, CallbackMethod, Params);
  }


  function onChangeStoreCalledBack(strReturn){  
      'use strict';
  
    var ret_status = getValue('@STATUS',strReturn);

    var txtField = window.document.FORM.hiddenField.value;
    if(ret_status === 'E'){
      var ret_err_msg = getValue('@ERROR_MSG',strReturn);
      alert(ret_err_msg);
      return;
    } 

    if(ret_status === 'N'){
      alert('<%=jbWResGUI.getRes("Store")%> <%=jbWResPrompt.getRes("NE_REC")%>');

      if(txtField === 'txtIssueStore'){
        window.document.FORM.txtIssueStore.value='';
        window.document.FORM.txtIssueStore.focus();
       }
         else if(txtField === 'txtRecvStore'){
          window.document.FORM.txtRecvStore.value='';
          window.document.FORM.txtRecvStore.focus();
         }
      return;
    }else if(getValue('STORE_DEL_CD', strReturn) === 'Y') 
      {
         alert('<%=jbWResPrompt.getRes("STORE_DELETED")%>');
         if(txtField === 'txtIssueStore'){
          window.document.FORM.txtIssueStore.value='';
          window.document.FORM.txtIssueStore.focus();
         }
         else if(txtField === 'txtRecvStore'){
          window.document.FORM.txtRecvStore.value='';
          window.document.FORM.txtRecvStore.focus();
         }
      return;
      }     
  }
   function returnValue(strReturn)
   {  
     'use strict';
      if(window.document.FORM.SEARCH_ACTION.value === 'STORE')
     {
        var txtField = window.document.FORM.hiddenField.value;
         if(txtField === 'txtIssueStore')
         {
          window.document.FORM.txtIssueStore.value = getValue('STORE', strReturn);
         }
         else if (txtField === 'txtRecvStore'){
           window.document.FORM.txtRecvStore.value = getValue('STORE', strReturn);
         }  
      }
      else if(window.document.FORM.SEARCH_ACTION.value === 'SELECT_USER')
     {
        window.document.FORM.txtRecvBy.value = getValue('USR_ID', strReturn);
     }
    }
   
    function restart()
    { 
      'use strict';
       try
       {  
         document.FORM.elements['DATE_RTN' + whichOne].value = '' + year + '-' + padout(month - 0 + 1) + '-' + padout(day);
         mywindow.close();
       }
       catch(e)
       { 
         alert('<%=jbWResPrompt.getRes("Please Try Again")%>');
       }
             
    }
  function onChangeSelect(value)
  {
    'use strict';
    var TargetClass = 'qrcom.PROFIT.webbean.dao.AD.AduserSearch';
    var TargetMethod = 'findByPrimaryKey';
    var CallBackMethod = 'returnByDao';

    var Params = '&USR_ID=' + value;

    var baseURL = '<%=BaseURL%>';
    invokeTargetClass(baseURL,TargetClass,TargetMethod,CallBackMethod,Params);
  }

  /* onchange User ID */
  function returnByDao(strReturn)
  {
     'use strict';
      window.document.FORM.txtRecvBy.value = getValue('USR_ID' ,strReturn);
  }
  
  function doUpdate()
  {
	  'use strict';
	  var TOTALROWS = window.document.FORM.txtHiddenCount.value;
	  var checked = '';
      for(var i=1;i<=TOTALROWS;i++)
      {
             if(window.document.FORM.elements['chkAction_'+i].checked) 
             {     
                if(window.document.FORM.elements['txtRecvStore_'+i].value !== '<%=default_Store%>')
				{
					alert('Only receiving store is the user\'s default authorized Store can proceed.');
					return;
				}
				checked = i;
             }
      }
	  
	  if(Validate(window.document.FORM,'view',TOTALROWS,'Y'))
	  {
		 setFormSubmit('<%=BaseURL%>/servlet/SvltGvupdaterecvAMY',TOTALROWS,'txtTempCoy_%txtTempCoySub_%txtIssueStore_%txtTransNo_%txtRecvStore_%txtDateRecv_%txtRecvBy_%txtStatus_',''); 
	  }
  }
   </script>

   <TITLE>Update Voucher Receiving</TITLE>      
   </HEAD>
   
    <BODY>
	<center>
   <FORM id="FORM" method="post"  action="<%=BaseURL%>/profit/GV/GvupdaterecvSearchAMY.jsp" name="FORM">
   <fieldset class="fieldsettitle"><legend><%=jbWResGUI.getRes("Update Voucher Receiving - Search")%></legend>
  
    
   <table width="100%" cellpadding="5" cellspacing="0" border="0" class="table-border">
    <tr><td>
      <TABLE BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="0" CLASS="table-border">
        <tr><td>
          <TABLE BORDER="0" WIDTH="100%" CELLSPACING="0" CELLPADDING="3">
              <TH COLSPAN="10"><%=jbWResGUI.getRes("Search Criteria")%></TH>
        </tr>
        
        <tr>
              <td width="15%" class="caption" colspan="1"><%=jbWResGUI.getRes("Company")%></td>
              <td width="30%" colspan="2">
                <table BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="1">
                  <tr>  
                   <td width="10%"><input type="text" name="txtCoy"  value="<%=strCoy%>" readonly tabindex="-1" class="input-display"></td>
                   <td width="70%"><input type="text" name="txtComName"  value="<%=strComName%>" readonly tabindex="-1" class="input-display"></td>
                  </tr> 
                </table>
              </td>
              <td width="5%" colspan="2">&nbsp;</td>
              
              <td width="15%" class="caption" colspan="1"><%=jbWResGUI.getRes("Company Subsidiary")%></td>
              <td width="30%" colspan="2">
                <select name="cboRegCoySub" id="cboRegCoySub" value="<%=strCoySub%>" style="width:100%" class="mandatory">
                     <%=coysubmstComboBox.getAuthorisedCoySubComboBoxByCoy(strCoySub,strCoy)%>
                  </select>
              </td>
              <td width="5%" colspan="2">&nbsp;</td>
            </tr>
    
        <tr>
              <td width="15%" class="caption"><%=jbWResGUI.getRes("Issue Store")%></td>
              <td width="30%" colspan="2"><input type="text" name="txtIssueStore" value="<%=strIssueStore%>" maxlength="<%=storeMaxLen%>" onblur="if(this.value != '') { this.value=leftFillZero(this.value, '4'); onchangeStore('txtIssueStore',this.value); }"></td>
              <td width="3%" colspan="1"><img src="<%=BaseURL%>/profit/images/search.gif" name="imgSearch1" alt="<%=jbWResGUI.getRes("Find")%>" onclick="SearchCriteria('<%=BaseURL%>/profit/MT/StrmstSearch.jsp','txtIssueStore','STORE');" style="cursor:hand"></td>
              <td width="2%" colspan="1">&nbsp;</td>
            
              <td width="15%" class="caption" align="left"><%=jbWResGUI.getRes("Transfer No")%></td>
              <td width="30%" colspan="2">
             <input type="text" name="txtTransNo"  maxlength="<%=SYSGVXfrNoLength%>" value="<%=strTransNo%>">
              </td>
             <td width="5%" colspan="2">&nbsp;</td>
            </tr>
            
            
         <tr>
              <td width="15%" class="caption"><%=jbWResGUI.getRes("Receiving Store")%></td>
              <td width="30%" colspan="2"><input type="text" name="txtRecvStore" value="<%=strRecvStore%>" maxlength="<%=storeMaxLen%>" onblur="if(this.value != '') { this.value=leftFillZero(this.value, '4'); onchangeStore('txtRecvStore',this.value); }"></td>
              <td width="3%" colspan="1"><IMG SRC="<%=BaseURL%>/profit/images/search.gif" NAME="imgSearch1" ALT="<%=jbWResGUI.getRes("Find")%>"  style="cursor:hand" onclick="SearchCriteria('<%=BaseURL%>/profit/MT/StrmstSearch.jsp','txtRecvStore','STORE');"></td>
              <td width="2%" colspan="1">&nbsp;</td>

              <td width="15%" class="caption" align="left"><%=jbWResGUI.getRes("Date Received")%></td>
              <td width="30%" colspan="2"><input type="text" name="DATE_RTN1" value="<%=strDate1%>" onchange="this.value=formatDate(this.value);" maxlength="10"></td>
              <td width="3%" colspan="1"><img src="<%=BaseURL%>/profit/images/calendr.gif" ALT="<%=jbWResGUI.getRes("Calendar")%>" onclick="newWindow(1)" style="cursor:hand"></td>     
              <td width="2%" colspan="1"></td>
            </tr>  
        
<tr>
              <td width="15%" class="caption"><%=jbWResGUI.getRes("Received By")%></td>
              <td width="30%" colspan="2"><input type="text" name="txtRecvBy" value="<%=strRecvBy%>" maxlength="8" onChange="onChangeSelect(this.value);"></td>
              <td width="3%"><img src="<%=BaseURL%>/profit/images/search.gif" ALT="<%=jbWResGUI.getRes("Find")%>" name="findUsrID" onclick="SearchCriteria('<%=BaseURL%>/profit/AD/AduserSearch.jsp?ACTION=search','txtRecvBy','SELECT_USER');" style="cursor:hand;"></td>
              <td width="2%" colspan="1">&nbsp;</td>


              <td width="15%" class="caption" align="left"><%=jbWResGUI.getRes("Status")%></td>
              <td width="30%" colspan="2"><input type="text" name="txtStatusDisp" value="Checked"  tabindex="-1" class="input-display" maxlength="10" readonly>
                <input type="hidden" name="txtStatus" value="C"  tabindex="-1" class="input-display" maxlength="10" readonly>
              </td>
              <td width="5%" colspan="2"></td>
            </tr>         
        
<tr>
  <td colspan="10">
        <TABLE BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="1">
            <tr>
            <td width="76%"></td>
            <td width="12%"><input type="reset" value="<%=jbWResGUI.getRes("Reset")%>" onclick="setFormReset(this.form, '<%=BaseURL%>/profit/GV/GvupdaterecvSearchAMY.jsp')"></td>
            <td width="12%">
               
               <input type="button" name="cbxSearch" value="<%=jbWResGUI.getRes("Search")%>" onclick="if(Validate(this.form,'search','','Y'))setFormSubmit('<%=BaseURL%>/profit/GV/GvupdaterecvSearchAMY.jsp','S','txtCoy%cboRegCoySub%txtIssueStore%txtTransNo%txtRecvStore%DATE_RTN1%txtRecvBy%txtStatus');">
            </td>
            </tr>
            </TABLE>
       </td>
              
        </tr>
        
    </table>    
    </td></tr>
      </table>
    </td></tr>
    
    <tr><td>
        <TABLE BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="3" class="table-border">
         <TR><TH COLSPAN="7"><%=jbWResGUI.getRes("Search Results")%></TH></TR>     
         <tr><td><table width="100%" border="0" cellspacing="1" cellpadding="0" class="tb-list">       
         <tr>
         <td width="5%" class="tb-display">  <%=jbWResGUI.getRes("Action")%> </td>
         <td width="15%" class="tb-display"> <%=jbWResGUI.getRes("Issuing Store")%> </td>
         <td width="15%" class="tb-display"> <%=jbWResGUI.getRes("Receiving Store")%> </td>
         <td width="10%" class="tb-display"> <%=jbWResGUI.getRes("Transfer Number")%> </td>
         <td width="10%" class="tb-display"> <%=jbWResGUI.getRes("Total (PCS)")%> </td>
         <td width="10%" class="tb-display"> <%=jbWResGUI.getRes("Total Voucher Amount ")%>(<%=jbWResGUI.getRes("RM")%>)</td>
         <td width="10%" class="tb-display"> <%=jbWResGUI.getRes("Date Received")%> </td>
         <td width="15%" class="tb-display"> <%=jbWResGUI.getRes("Received By")%> </td>
         <td width="10%" class="tb-display"> <%=jbWResGUI.getRes("Status")%> </td>
        </tr>  
   
    <%    
       int i = 0;
       Collection rsCllt = null;
       
       // PageControl.jsp needs this string variable.
       String strPageOfTotal = "";    
       /* add new codes */      
       String strTdBgColor = "tb-display";
       String strInputColor = "input-display";
       /* end of new codes */       
     if (strAction != null && strAction.equals("search"))
       { 
        String strLinePerPage = jbWebSessionParam.getProfitvvValue(request, "SYSMaxLinesPerPage");
        HTTPPageQueryBased httpPageQueryBased = new HTTPPageQueryBased(hParam, gvupdaterecvDAO, strLinePerPage);                        
        jbSession.setAttribute("gvupdaterecvDAOquery", httpPageQueryBased);
        rsCllt = httpPageQueryBased.getFirstPage();
        strPageOfTotal = httpPageQueryBased.getPageOfTotal();   
        }
      else
      if (strAction != null && 
           (strAction.equals("getNextPage") ||
            strAction.equals("getPreviousPage") ||
            strAction.equals("getFirstPage") ||
            strAction.equals("getLastPage"))) 
      {
//        HTTPPage httpPage = (HTTPPage)jbSession.getAttribute("stateHttpPage");
            HTTPPageQueryBased httpPage = (HTTPPageQueryBased)jbSession.getAttribute("gvupdaterecvDAOquery");
           if (httpPage != null) 
           {
              rsCllt = httpPage.process(strAction);
              strPageOfTotal = httpPage.getPageOfTotal();
           }
      }
      else
      if (strAction != null && strAction.equals("getPage"))
      {
//        HTTPPage httpPage = (HTTPPage)jbSession.getAttribute("stateHttpPage"); 
            HTTPPageQueryBased httpPage = (HTTPPageQueryBased)jbSession.getAttribute("gvupdaterecvDAOquery");

           if (httpPage != null) 
           {
              String str = request.getParameter( "PAGE" );
              rsCllt = httpPage.getPage(str);
              strPageOfTotal = httpPage.getPageOfTotal();
           }
      }    
      else
     if(strAction != null && strAction.equals("reset"))
    {   
    jbSession.removeAttribute("gvupdaterecvDAOquery");
            }  
     if (rsCllt != null)
     {
          Iterator iter = rsCllt.iterator();      
          String [] str;
          while (iter.hasNext())
          {
             i++;
             str = (String [])iter.next();

             if (strTdBgColor == "tb-display") {
                 strTdBgColor = "tb-display2";
                 strInputColor = "input-display2";
             } else {
                 strTdBgColor = "tb-display";
                 strInputColor = "input-display";
             }
      /* end of new codes */         

  %>
    <tr>
      <td align="center" class="<%=strTdBgColor%>">
       <input type="checkbox" name="chkAction_<%=i%>" onClick="if(checkCurrentAction(SELECT_MODE.value))selectCurrentAction(txtHiddenCount.value, this, 'window.document.FORM.chkAction_')">
      </td> 
      <td class="<%=strTdBgColor%>">     
       <input type="text"  name="txtIssueStore_<%=i%>" value="<%=str[0]%>" readonly tabindex="-1" class="<%=strInputColor%>">
      </td>
      <td class="<%=strTdBgColor%>">     
       <input type="text"  name="txtRecvStore_<%=i%>" value="<%=str[1]%>" readonly tabindex="-1" class="<%=strInputColor%>">
      </td> 
      <td class="<%=strTdBgColor%>">     
       <input type="text"  name="txtTransNo_<%=i%>" value="<%=str[2]%>" readonly tabindex="-1" class="<%=strInputColor%>">
      </td>
	  
	  <td class="<%=strTdBgColor%>">
       <input type="text" name="txtTOT_PCS_<%=i%>" value="<%=str[8]%>" readonly tabindex="-1" class="<%=strInputColor%>">
      </td>

      <td class="<%=strTdBgColor%>">
       <input type="text" name="txtTOT_AMT_i%>" value="<%=webCurrConverter.format(str[9], 2)%>" readonly tabindex="-1" class="<%=strInputColor%>" style="text-align:right">
      </td>
    
      <td class="<%=strTdBgColor%>">
      <input type="text"  name="txtDateRecv_<%=i%>" value="<%=str[3]%>" readonly tabindex="-1" class="<%=strInputColor%>"> 
      </td>

      <td class="<%=strTdBgColor%>">
      <input type="text"  name="txtRecvBy_<%=i%>" value="<%=str[4]%>" readonly tabindex="-1" class="<%=strInputColor%>"> 
      </td>

      <td class="<%=strTdBgColor%>">

        <%
        String tempDisplay = "";
          if(str[5].equals("C")){
            tempDisplay="Checked";
        }

        %>
        <input type="text"  name="txtStatusDisplay_<%=i%>" value="<%=tempDisplay%>" readonly tabindex="-1" class="<%=strInputColor%>"> 
        <input type="hidden"  name="txtStatus_<%=i%>" value="<%=str[5]%>" readonly tabindex="-1" class="<%=strInputColor%>"> 
      </td>

       <input type="hidden"  name="txtTempCoy_<%=i%>"  value="<%=str[6]%>" readonly tabindex="-1" class="<%=strInputColor%>">
       <input type="hidden"  name="txtTempCoySub_<%=i%>"  value="<%=str[7]%>" readonly tabindex="-1" class="<%=strInputColor%>">
    </tr>
      <%
                } // while (selectDespatch.nextResultSet())
              
          } // if (strAction != null && strAction.equals("search"))
           %>                   
    </table></td></tr>
   </table>
  </td></tr>
  
  <tr><td>  
  <%@ include file="/profit/Common/PageControl.jsp" %>      
    <TABLE BORDER="0" WIDTH="100%" CELLSPACING="1" CELLPADDING="1">
        <TR>
          <td></td>
        <td width="10%">
          <INPUT name="cbxView" type="button" value="<%=jbWResGUI.getRes("Update")%>" onclick="doUpdate();"></td>

        </TR>
      </TABLE>
    
       
        
       
  </td></tr>
  </table>
  </fieldset>
   <INPUT id="txtHiddenCount" name="txtHiddenCount" value="<%=i%>" type="hidden">
   <INPUT name="ACTION"  id="ACTION" type="hidden">
   <INPUT type="hidden" name="SELECT_MODE" value="<%=strSelectMode%>">
    <input type="hidden" name="hiddenField" >
  
   <INPUT name="PAGE_ID" value="MembermstSearch" type="hidden">
   <input type="hidden" id="SEARCH_ACTION" name="SEARCH_ACTION">
   </FORM>
   
   <FORM id="FORM_SUBMIT" method="post" action="<%=BaseURL%>/profit/GV/GvupdaterecvSearchAMY.jsp" name="FORM_SUBMIT">
   <INPUT type="hidden" name="SELECT_MODE" value="<%=strSelectMode%>">

   <INPUT name="PAGE_ID" value="MembermstSearch" type="hidden">
   
   </FORM>
   </center>
   </BODY>
</html>
