package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.AccountStatus;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.AccountService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/loadCustomers")
public class LoadCustomers extends HttpServlet {

    @EJB
    private AccountService accountService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            JSONObject input = new JSONObject(sb.toString());

            String accountNumber = input.getString("accountNumber");
            String name = input.getString("name");

            System.out.println(accountNumber+" "+name);

            resp.setContentType("application/json");

            List<Account> accountList = accountService.getAllAccounts();

            JSONArray jsonArray = new JSONArray();

            for (Account account : accountList) {

                if (accountNumber != null && !account.getAccountNo().contains(accountNumber)) {
                    continue;
                }

                if (name != null && !account.getUser().getName().contains(name)) {
                    continue;
                }

                JSONObject jsonObject = new JSONObject();

                User user = account.getUser();

                jsonObject.put("accountNo", account.getAccountNo());
                jsonObject.put("name", user.getName());
                jsonObject.put("email", user.getEmail());
                jsonObject.put("balance", account.getBalance());
                jsonObject.put("mobile", user.getMobile());
                jsonObject.put("status", account.getStatus().name());
                if (account.getStatus() == AccountStatus.ACTIVE) {
                    jsonObject.put("active", true);
                } else {
                    jsonObject.put("active", false);
                }
                jsonArray.put(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
