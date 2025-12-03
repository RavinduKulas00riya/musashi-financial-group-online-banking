//package lk.jiat.app.web.servlet;
//
//import jakarta.ejb.EJB;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lk.jiat.app.core.model.User;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//@WebServlet("/loadInterest")
//public class LoadInterest extends HttpServlet {
//
//    @EJB
//    private InterestService interestService;
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        User user = (User) req.getSession().getAttribute("user");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        List<Interest> interestList = interestService.getInterests(user.getAccounts().get(0));
//
//        if (interestList.isEmpty()) {
//            sendError(resp, "Empty interest history");
//            return;
//        }
//
//        JSONArray result = new JSONArray();
//        interestList.forEach(interest -> {
//
//            JSONObject json = new JSONObject();
//
//            json.put("interestDateTime", formatter.format(interest.getDateTime()));
//            json.put("interestAmount", interest.getAmount());
//            json.put("interestRate","0.001");
//            json.put("interestBalanceAfter", interest.getBalanceAfter());
//
//            result.put(json);
//        });
//
//        resp.setContentType("application/json");
//        resp.getWriter().write(result.toString());
//    }
//
//    private void sendError(HttpServletResponse response, String message){
//
//        try {
//            response.setContentType("text/plain");
//            response.getWriter().write(message);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//    }
//}
