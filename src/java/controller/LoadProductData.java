
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Brand;
import hibernate.Color;
import hibernate.HibernateUtil;
import hibernate.Model;
import hibernate.Quality;
import hibernate.Storage;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Mewantha
 */
@WebServlet(name = "LoadProductData", urlPatterns = {"/LoadProductData"})
public class LoadProductData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        //get brands
        Criteria c1 = s.createCriteria(Brand.class);
        List<Brand> brandList = c1.list();

        //get models
        Criteria c2 = s.createCriteria(Model.class);
        List<Model> modelList = c2.list();

        //get quality
        Criteria c3 = s.createCriteria(Quality.class);
        List<Quality> qualityList = c3.list();

        //get color
        Criteria c4 = s.createCriteria(Color.class);
        List<Color> colorList = c4.list();

        //get storage
        Criteria c5 = s.createCriteria(Storage.class);
        List<Storage> storageList = c5.list();

        s.close();
        Gson gson = new Gson();

        responseObject.add("brandList", gson.toJsonTree(brandList));
        responseObject.add("modelList", gson.toJsonTree(modelList));
        responseObject.add("qualityList", gson.toJsonTree(qualityList));
        responseObject.add("colorList", gson.toJsonTree(colorList));
        responseObject.add("storageList", gson.toJsonTree(storageList));

        responseObject.addProperty("status", true);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));

    }

}
