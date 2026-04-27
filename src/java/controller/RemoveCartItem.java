package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@WebServlet(name = "RemoveCartItem", urlPatterns = {"/RemoveCartItem"})
public class RemoveCartItem extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String cartIdParam = request.getParameter("id");
        if (cartIdParam == null || cartIdParam.isEmpty()) {
            responseObject.addProperty("message", "Missing cart item ID");
            writeResponse(response, gson.toJson(responseObject));
            return;
        }

        int cartId;
        try {
            cartId = Integer.parseInt(cartIdParam);
        } catch (NumberFormatException e) {
            responseObject.addProperty("message", "Invalid cart item ID");
            writeResponse(response, gson.toJson(responseObject));
            return;
        }

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user != null) {
            // Logged-in user: delete from DB
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Transaction tr = s.beginTransaction();

            Cart cartItem = (Cart) s.get(Cart.class, cartId);

            if (cartItem != null && cartItem.getUser() != null && cartItem.getUser().getId() == user.getId()) {
                s.delete(cartItem);
                tr.commit();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Cart item removed successfully");
            } else {
                responseObject.addProperty("message", "Cart item not found or unauthorized");
            }

        } else {
            // Guest user: remove from session cart
            if (session != null) {
                Object sessionCartObj = session.getAttribute("sessionCart");
                if (sessionCartObj != null && sessionCartObj instanceof ArrayList) {
                    @SuppressWarnings("unchecked")
                    ArrayList<Cart> sessionCart = (ArrayList<Cart>) sessionCartObj;
                    boolean removed = sessionCart.removeIf(c -> c.getId() == cartId);

                    if (removed) {
                        responseObject.addProperty("status", true);
                        responseObject.addProperty("message", "Cart item removed from session");
                    } else {
                        responseObject.addProperty("message", "Cart item not found in session");
                    }
                } else {
                    responseObject.addProperty("message", "Session cart is empty");
                }
            } else {
                responseObject.addProperty("message", "No active session");
            }
        }

        writeResponse(response, gson.toJson(responseObject));
    }

    private void writeResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}
