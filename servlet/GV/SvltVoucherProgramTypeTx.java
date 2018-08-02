package qrcom.PROFIT.servlet.GV;

import java.io.IOException;

import java.util.Collection;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import qrcom.PROFIT.files.dao.local.AD.LockermstDAO;
import qrcom.PROFIT.files.dao.local.GV.VoucherProgramTypeDAO;
import qrcom.PROFIT.files.dao.local.GV.VoucherReasonDAOAMY;
import qrcom.PROFIT.files.info.AduserInfo;
import qrcom.PROFIT.files.info.GvprogtypmstInfo;
import qrcom.PROFIT.files.info.LockerStatus;
import qrcom.PROFIT.files.info.LockermstInfo;
import qrcom.PROFIT.servlet.HTTPSessionAttributeWrapper;
import qrcom.PROFIT.system.DBConvert;
import qrcom.PROFIT.webbean.HTTPObj.WebSessionParam;

import qrcom.util.HParam;

public class SvltVoucherProgramTypeTx {
    HTTPSessionAttributeWrapper jbSession = null;
    GvprogtypmstInfo gvprogtypmstInfo = null;
    WebSessionParam jbWebSessionParam = null;
    AduserInfo aduser = null;
    Collection errorSets = new Vector();
    Collection updateSets = new Vector();

    public SvltVoucherProgramTypeTx() {
    }

    private void loadSession(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                              IOException {
        jbSession = (HTTPSessionAttributeWrapper) request.getSession().getAttribute("jbSession");

        if (jbSession == null) {
            jbSession = new HTTPSessionAttributeWrapper();
            request.getSession().setAttribute("jbSession", jbSession);
        }
        aduser = (AduserInfo) request.getSession().getAttribute("aduserInfo");
        jbWebSessionParam = (WebSessionParam) request.getSession().getAttribute("jbWebSessionParam");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConvert.toDBMS(request.getParameterMap());

        //Loading all the session Variable/Collections
        loadSession(request, response);

        String strAction = request.getParameter("ACTION");
        if (strAction == null)
            strAction = "";

        if (strAction.equals("Add")) {
            add(request, response);
        } else if (strAction.equals("cancel")) {
            cancel(request, response);
        } else if (strAction.equals("addVoucherProgramType")) {
            addVoucherProgramType(request, response);
        } else if (strAction.equals("View")) {
            view(request, response);
        } else if (strAction.equals("Edit")) {
            edit(request, response);
        } else if (strAction.equals("editVoucherProgramType")) {
            editVoucherProgramType(request, response);
        } else if (strAction.equals("Delete")) {
            delete(request, response);
        }
    }

    private void view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VoucherProgramTypeDAO voucherProgramTypeDAO = new VoucherProgramTypeDAO();
        int totalrow = Integer.parseInt(request.getParameter("TOTALROWS"));

        HParam hParam = (HParam) request.getSession().getAttribute("VoucherProgramTypeParam");
        String strCOY_SUB = hParam.getString("COY_SUB");
        String strVoucher_Type = hParam.getString("VOUCHER_TYPE");
        String strReasonCode = hParam.getString("REASON_CODE");
        String strProgram_Type = hParam.getString("PROGRAM_TYPE");

        String paramter =
            "?ACTION=search&KEY1=" + strCOY_SUB + "&KEY2=" + strVoucher_Type + "&KEY3=" + strReasonCode + "&KEY4=" +
            strProgram_Type;
        
        try {
            for (int i = 1; i <= totalrow; i++) {
                String checked = request.getParameter("chkAction_" + i);
                if (checked == null) {
                    continue;
                }
                String[] str = new String[5];
                str[0] = request.getParameter("txtCoy_" + i);
                str[1] = request.getParameter("txtCoySub_" + i);
                str[2] = request.getParameter("txtVoucherType_" + i);
                str[3] = request.getParameter("txtReasonCode_" + i);
                str[4] = request.getParameter("txtProgramType_" + i);
                gvprogtypmstInfo = voucherProgramTypeDAO.findByPrimaryKey(str[0], str[1], str[2], str[3], str[4]);
                break;
            }
            jbSession.setAttribute("gvprogtypmstInfo", gvprogtypmstInfo);
            jbSession.setAttribute("jbMaintType", "View");
            gotoPage("/profit/GV/VoucherProgramTypeMaint.jsp", request, response);
        } catch (Exception e) {
            errorSets.add(e.getMessage());
            request.getSession().setAttribute("errorSets", errorSets);
            e.printStackTrace();
            gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp" + paramter, request, response);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VoucherProgramTypeDAO voucherProgramTypeDAO = new VoucherProgramTypeDAO();
        int totalrow = Integer.parseInt(request.getParameter("TOTALROWS"));
        HParam hParam = (HParam) request.getSession().getAttribute("VoucherProgramTypeParam");
        String strCOY_SUB = hParam.getString("COY_SUB");
        String strVoucher_Type = hParam.getString("VOUCHER_TYPE");
        String strReasonCode = hParam.getString("REASON_CODE");
        String strProgram_Type = hParam.getString("PROGRAM_TYPE");

        String paramter =
            "?ACTION=search&KEY1=" + strCOY_SUB + "&KEY2=" + strVoucher_Type + "&KEY3=" + strReasonCode + "&KEY4=" +
            strProgram_Type;
        Collection coll_gvprogtypmstInfo = new Vector();
        LockermstDAO lockermstDAO = new LockermstDAO(request);
        LockerStatus st = null;
        try {
            for (int i = 1; i <= totalrow; i++) {
                String checked = request.getParameter("chkAction_" + i);
                if (checked == null) {
                    continue;
                }
                String[] str = new String[5];
                str[0] = request.getParameter("txtCoy_" + i);
                str[1] = request.getParameter("txtCoySub_" + i);
                str[2] = request.getParameter("txtVoucherType_" + i);
                str[3] = request.getParameter("txtReasonCode_" + i);
                str[4] = request.getParameter("txtProgramType_" + i);

                gvprogtypmstInfo = voucherProgramTypeDAO.findByPrimaryKey(str[0], str[1], str[2], str[3], str[4]);

                st =
                    lockermstDAO.lock("GVPROGTYPMST", gvprogtypmstInfo.COY(), gvprogtypmstInfo.COY_SUB(),
                                      gvprogtypmstInfo.GV_TYPE(), gvprogtypmstInfo.RSN_CD(), gvprogtypmstInfo.PROG_CD(),
                                      0);
                if (st.getState() == LockerStatus.LOCK_SUCCESSFUL) {
                    if (gvprogtypmstInfo.DEL_CD().equals("Y")) {
                        String strErr =
                            "USR_MSG= [Voucher Type:" + gvprogtypmstInfo.GV_TYPE() + " Reason Code:" +
                            gvprogtypmstInfo.RSN_CD() + " Program Type:" + gvprogtypmstInfo.PROG_CD() + " already flag deleted.]";
                        errorSets.add(strErr);
                    } else {
                        coll_gvprogtypmstInfo.add(gvprogtypmstInfo);
                    }
                } else {
                    LockermstInfo _info = st.getLockermstInfo();
                    String strErr = "USR_MSG=REC_ALREADY_LOCKED[ " + _info.LAST_OPR() + "]";
                    errorSets.add(strErr);
                }
            }
            if (errorSets != null && !errorSets.isEmpty()) {
                request.getSession().setAttribute("errorSets", errorSets);
                lockermstDAO.clearAllLocker(request);
                gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp" + paramter, request, response);
                return;
            } else {
                errorSets = voucherProgramTypeDAO.deleteVoucherProgramType(coll_gvprogtypmstInfo, aduser.USR_ID());
                if (errorSets != null && !errorSets.isEmpty()) {
                    request.getSession().setAttribute("errorSets", errorSets);
                }
                lockermstDAO.clearAllLocker(request);
                gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp" + paramter, request, response);
            }
        } catch (Exception e) {
            errorSets.add(e.getMessage());
            request.getSession().setAttribute("errorSets", errorSets);
            e.printStackTrace();
            lockermstDAO.clearAllLocker(request);
            gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp" + paramter, request, response);
        }
    }
    
    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VoucherProgramTypeDAO voucherProgramTypeDAO = new VoucherProgramTypeDAO();
        int totalrow = Integer.parseInt(request.getParameter("TOTALROWS"));

        HParam hParam = (HParam) request.getSession().getAttribute("VoucherProgramTypeParam");

        String strCOY_SUB = hParam.getString("COY_SUB");
        String strVoucher_Type = hParam.getString("VOUCHER_TYPE");
        String strReasonCode = hParam.getString("REASON_CODE");
        String strProgram_Type = hParam.getString("PROGRAM_TYPE");

        String paramter =
            "?ACTION=search&KEY1=" + strCOY_SUB + "&KEY2=" + strVoucher_Type + "&KEY3=" + strReasonCode + "&KEY4=" +
            strProgram_Type;
        
        try {
            for (int i = 1; i <= totalrow; i++) {
                String checked = request.getParameter("chkAction_" + i);
                if (checked == null) {
                    continue;
                }
                String[] str = new String[5];
                str[0] = request.getParameter("txtCoy_" + i);
                str[1] = request.getParameter("txtCoySub_" + i);
                str[2] = request.getParameter("txtVoucherType_" + i);
                str[3] = request.getParameter("txtReasonCode_" + i);
                str[4] = request.getParameter("txtProgramType_" + i);
                gvprogtypmstInfo = voucherProgramTypeDAO.findByPrimaryKey(str[0], str[1], str[2], str[3], str[4]);
                break;
            }
            LockermstDAO lockermstDAO = new LockermstDAO(request);
            LockerStatus st =
                lockermstDAO.lock("GVPROGTYPMST", gvprogtypmstInfo.COY(), gvprogtypmstInfo.COY_SUB(), gvprogtypmstInfo.GV_TYPE(),
                                  gvprogtypmstInfo.RSN_CD(), gvprogtypmstInfo.PROG_CD(), 0);
            
            if (st.getState() == LockerStatus.LOCK_SUCCESSFUL) {
                jbSession.setAttribute("gvprogtypmstInfo", gvprogtypmstInfo);
                jbSession.setAttribute("jbMaintType", "Edit");
                gotoPage("/profit/GV/VoucherProgramTypeMaint.jsp", request, response);
            } else {
                LockermstInfo _info = st.getLockermstInfo();
                String strErr = "USR_MSG=REC_ALREADY_LOCKED[ " + _info.LAST_OPR() + "]";
                errorSets.add(strErr);
                request.getSession().setAttribute("errorSets", errorSets);
                gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp" + paramter, request, response);
            }
        } catch (Exception e) {
            errorSets.add(e.getMessage());
            request.getSession().setAttribute("errorSets", errorSets);
            e.printStackTrace();
            gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp" + paramter, request, response);
        }
    }
    
    private void editVoucherProgramType(HttpServletRequest request,
                                        HttpServletResponse response) throws ServletException, IOException {
        loadHTML_Data(request);
        VoucherProgramTypeDAO voucherProgramTypeDAO = new VoucherProgramTypeDAO();
        try {
            errorSets = voucherProgramTypeDAO.updateVoucherProgramType(gvprogtypmstInfo.getVObject());
            if (!errorSets.isEmpty()) {
                jbSession.setAttribute("jbMaintType", "Edit");
                request.getSession().setAttribute("errorSets", errorSets);
                jbSession.setAttribute("gvprogtypmstInfo", gvprogtypmstInfo);
                gotoPage("/profit/GV/VoucherProgramTypeMaint.jsp?ACTION=error", request, response);
            } else {
                LockermstDAO lockermstDAO = new LockermstDAO(request);
                lockermstDAO.clearAllLocker(request);
                gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp?ACTION=search&KEY1=" + gvprogtypmstInfo.COY_SUB() +
                         "&KEY2=" + gvprogtypmstInfo.GV_TYPE() + "&KEY3=" + gvprogtypmstInfo.RSN_CD() + "&KEY4=" +
                         gvprogtypmstInfo.PROG_CD(), request, response);
            }
        } catch (Exception e) {
            errorSets.add(e.toString());
            jbSession.setAttribute("jbMaintType", "Edit");
            request.getSession().setAttribute("errorSets", errorSets);
            gotoPage("/profit/GV/VoucherProgramTypeMaint.jsp?ACTION=error", request, response);
            e.printStackTrace();
        }
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        jbSession.setAttribute("jbMaintType", "Add");
        gotoPage("/profit/GV/VoucherProgramTypeMaint.jsp", request, response);
    }

    @SuppressWarnings("unchecked")
    private void addVoucherProgramType(HttpServletRequest request,
                                       HttpServletResponse response) throws ServletException, IOException {
        loadHTML_Data(request);
        VoucherProgramTypeDAO voucherProgramTypeDAO = new VoucherProgramTypeDAO();
        try {
            errorSets = voucherProgramTypeDAO.insertVoucherProgramType(gvprogtypmstInfo.getVObject());
            if (!errorSets.isEmpty()) {
                jbSession.setAttribute("jbMaintType", "Add");
                request.getSession().setAttribute("errorSets", errorSets);
                jbSession.setAttribute("gvprogtypmstInfo", gvprogtypmstInfo);
                gotoPage("/profit/GV/VoucherProgramTypeMaint.jsp?ACTION=error", request, response);
            } else {
                gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp?ACTION=search&KEY1=" + gvprogtypmstInfo.COY_SUB() +
                         "&KEY2=" + gvprogtypmstInfo.GV_TYPE() + "&KEY3=" + gvprogtypmstInfo.RSN_CD() +
                         "&KEY4=" + gvprogtypmstInfo.PROG_CD(), request,
                         response);
            }
        } catch (Exception e) {
            errorSets.add(e.toString());
            jbSession.setAttribute("jbMaintType", "Add");
            request.getSession().setAttribute("errorSets", errorSets);
            gotoPage("/profit/GV/VoucherProgramTypeMaint.jsp?ACTION=error", request, response);
            e.printStackTrace();
        }
    }

    private void cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HParam hParam = (HParam) request.getSession().getAttribute("VoucherProgramTypeMaintParam");
        
        String strCOY_SUB = hParam.getString("COY_SUB");
        String strVoucher_Type = hParam.getString("VOUCHER_TYPE");
        String strReasonCode = hParam.getString("REASON_CODE");
        String strProgram_Type = hParam.getString("PROGRAM_TYPE");
        String strProgram_Desc = hParam.getString("PROGRAM_DESC");
        String strChecked = hParam.getString("DESC_MID_SEARCH");

        String paramter =
            "?ACTION=search&KEY1=" + strCOY_SUB + "&KEY2=" + strVoucher_Type + "&KEY3=" + strReasonCode + "&KEY4=" +
            strProgram_Type + "&KEY5=" + strProgram_Desc + "&KEY6=" + strChecked;
        LockermstDAO lockermstDAO = new LockermstDAO(request);
        lockermstDAO.clearAllLocker(request);
        gotoPage("/profit/GV/VoucherProgramTypeSearch.jsp" + paramter, request, response);
    }

    private void loadHTML_Data(HttpServletRequest request) {
        AduserInfo aduserInfo = (AduserInfo) request.getSession().getAttribute("aduserInfo");
        gvprogtypmstInfo = new GvprogtypmstInfo();
            
        String temp = request.getParameter("txtCOY");
        gvprogtypmstInfo.setCOY(temp);
            
        temp = request.getParameter("cboCOY_SUB");
        gvprogtypmstInfo.setCOY_SUB(temp);
            
        temp = request.getParameter("cboVoucherType");
        gvprogtypmstInfo.setGV_TYPE(temp);
        
        temp = request.getParameter("cboReasonCode");
        gvprogtypmstInfo.setRSN_CD(temp);
        
        temp = request.getParameter("txtProgramType");
        gvprogtypmstInfo.setPROG_CD(temp);
        
        temp = request.getParameter("txtProgramDesc");
        gvprogtypmstInfo.setPROG_DESC(temp);
        if (request.getParameter("chkDeleted") != null && request.getParameter("chkDeleted").equals("on")) {
            gvprogtypmstInfo.setDEL_CD("Y");
        } else {
            gvprogtypmstInfo.setDEL_CD("N");
        }
        gvprogtypmstInfo.setLAST_OPR(aduserInfo.USR_ID());
    }

    private void gotoPage(String address, HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getSession().getServletContext().getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
