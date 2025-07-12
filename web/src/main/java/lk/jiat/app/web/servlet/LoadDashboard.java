package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.DailyBalance;
import lk.jiat.app.core.service.AccountService;
import lk.jiat.app.core.service.DailyBalanceService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/loadDashboard")
public class LoadDashboard extends HttpServlet {

    @EJB
    private AccountService accountService;

    @EJB
    private DailyBalanceService dailyBalanceService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            resp.setContentType("application/json");

            List<Account> accounts = accountService.getAllAccounts();

            String allAccountsCount = String.valueOf(accounts.size());
            String activeAccountsCount = String.valueOf(accountService.getActiveAccounts().size());
            Double totalBalance = 0.0;

            for (Account account : accounts) {
                totalBalance += account.getBalance();
            }

            JSONObject root = new JSONObject();
            JSONObject stats = new JSONObject();
            stats.put("totalCustomers", allAccountsCount);
            stats.put("activeCustomers", activeAccountsCount);
            stats.put("totalBalance", totalBalance);

            root.put("statistics", stats);

            List<DailyBalance> dailyBalances = dailyBalanceService.getAllDailyBalance();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            JSONArray dailyBalancesArray = new JSONArray();

            dailyBalances.forEach(dailyBalance -> {
                JSONObject dailyBalanceJson = new JSONObject();
                dailyBalanceJson.put("balance", dailyBalance.getBalance());
                dailyBalanceJson.put("date", formatter.format(dailyBalance.getDateTime()));
                dailyBalanceJson.put("change",dailyBalance.getProfit());

                dailyBalancesArray.put(dailyBalanceJson);
            });

            root.put("dailyBalances", dailyBalancesArray);

            resp.getWriter().write(root.toString());

        }catch(Exception e){
            System.out.println(e);
        }

    }
}
