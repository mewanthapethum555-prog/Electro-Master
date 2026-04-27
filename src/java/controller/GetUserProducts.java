package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.Product;
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

@WebServlet(name = "GetUserProducts", urlPatterns = {"/GetUserProducts"})
public class GetUserProducts extends HttpServlet {

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

        Criteria c = s.createCriteria(Product.class);
        c.add(Restrictions.eq("user", user));
        c.addOrder(Order.desc("id"));

        List<Product> products = c.list();

        JsonArray array = new JsonArray();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");

        for (Product p : products) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", p.getId());
            obj.addProperty("title", p.getTitle());
            obj.addProperty("price", p.getPrice());
            obj.addProperty("quantity", p.getQty());
            obj.addProperty("date", sdf.format(p.getCreated_at()));
            array.add(obj);
        }

        s.close();
        response.getWriter().write(array.toString());
    }
}
