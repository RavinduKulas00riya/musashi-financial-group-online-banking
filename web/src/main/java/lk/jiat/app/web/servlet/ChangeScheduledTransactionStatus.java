package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.ScheduledTransactionStatus;
import lk.jiat.app.core.model.ScheduledTransfer;
import lk.jiat.app.core.service.ScheduledTransactionService;

import java.io.IOException;

@WebServlet("/changeScheduledTransactionStatus")
public class ChangeScheduledTransactionStatus extends HttpServlet {

    @EJB
    private ScheduledTransactionService scheduledTransactionService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer id = Integer.parseInt(req.getParameter("id"));
        ScheduledTransfer scheduledTransfer = scheduledTransactionService.getTransactionById(id);
        if (scheduledTransfer != null) {

            System.out.println("Scheduled transfer " + scheduledTransfer.getId());

            if(scheduledTransfer.getStatus().equals(ScheduledTransactionStatus.PENDING)){
                scheduledTransfer.setStatus(ScheduledTransactionStatus.PAUSED);
            }else{
                scheduledTransfer.setStatus(ScheduledTransactionStatus.PENDING);
            }

            scheduledTransactionService.updateTransaction(scheduledTransfer);
        }else {
            System.out.println("Scheduled transfer not found " + id);
        }
    }
}
