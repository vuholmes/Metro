package qrcom.PROFIT.servlet.GV;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SvltVoucherProgramType extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public SvltVoucherProgramType() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //To prevent random screen-jumping problem since HttpServlet is shared among users.
        //Problems occurred in gotoPage without using request.getSession()...
        SvltVoucherProgramTypeTx svltVoucherProgramTypeTx = new SvltVoucherProgramTypeTx();
        svltVoucherProgramTypeTx.doPost(request, response);
        svltVoucherProgramTypeTx = null;
    }
}
