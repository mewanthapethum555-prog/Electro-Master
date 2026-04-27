package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.OrderItems;
import hibernate.Orders;
import hibernate.User;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "GetUserOrders", urlPatterns = {"/GetUserOrders"})
public class GetUserOrders extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.getWriter().write("[]");
            return;
        }

        Session s = HibernateUtil.getSessionFactory().openSession();

        Criteria c = s.createCriteria(Orders.class);
        c.add(Restrictions.eq("user", user));
        c.addOrder(Order.desc("id"));
        List<Orders> ordersList = c.list();

        JsonArray ordersArray = new JsonArray();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");

        for (Orders order : ordersList) {
            for (OrderItems item : order.getOrderItems()) {
                JsonObject orderObj = new JsonObject();
                orderObj.addProperty("id", order.getId());
                orderObj.addProperty("title", item.getProduct().getTitle());
                orderObj.addProperty("price", item.getProduct().getPrice());
                orderObj.addProperty("quantity", item.getQty());
                orderObj.addProperty("date", sdf.format(order.getCreatedAt()));
                ordersArray.add(orderObj);
            }
        }

        response.getWriter().write(ordersArray.toString());
        s.close();
    }
}
