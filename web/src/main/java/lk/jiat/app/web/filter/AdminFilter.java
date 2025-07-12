package lk.jiat.app.web.filter;

import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.UserService;

import java.io.IOException;

@WebFilter({"/admin/admin_home.jsp"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if (session != null && session.getAttribute("admin") != null) {

            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            response.sendRedirect("/admin_login.jsp");
        }
    }
}
