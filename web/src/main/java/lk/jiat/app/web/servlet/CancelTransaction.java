package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.Transfer;
import lk.jiat.app.core.service.TransactionService;

import java.io.IOException;

@WebServlet("/cancelTransaction")
public class CancelTransaction extends HttpServlet {

    @EJB
    private TransactionService transactionService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");

        try {
            Integer id = Integer.valueOf(req.getParameter("id"));
            Transfer transfer = transactionService.getTransaction(id);
            transactionService.deleteTransaction(transfer);
            resp.getWriter().write("success");
        }catch (Exception e) {
            System.out.println(e);
            resp.getWriter().write("error");
        }
    }
}
