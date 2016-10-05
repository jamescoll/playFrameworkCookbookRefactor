package controllers;

import models.Product;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

public class Products extends Controller {

    @Inject
    FormFactory formFactory;


    private Map<String, Product> products;

    @Inject
    public Products(){
        products = new HashMap<String, Product>();
        products.put("abc", new Product("abc", "Macbook Pro Retina"));
        products.put("def", new Product("def", "iPad Air"));

    }

    @Security.Authenticated(BasicAuthenticator.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result create(){

        try {
            Form<Product> form = formFactory.form(Product.class).bindFromRequest();

            if(form.hasErrors()){
                return badRequest(form.errorsAsJson());
            } else {
                Product product = form.get();
                products.put(product.getSku(), product);
                return created(toJson(product));
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    @Security.Authenticated(BasicAuthenticator.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result edit(String id){

        try {
            Product product = products.get(id);

            if (product != null) {
                Form<Product> form = formFactory.form(Product.class).bindFromRequest();
                if( form.hasErrors()) {
                    return badRequest(form.errorsAsJson());
                } else {
                    Product productForm = form.get();
                    product.setTitle(productForm.getTitle());
                    products.put(product.getSku(), product);

                    return ok(toJson(product));
                }
            } else {
                return notFound();
            }
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    @Security.Authenticated(BasicAuthenticator.class)
    public Result delete(String id){
        try {
            Product product = products.get(id);

            if(product != null){
                products.remove(product);

                return noContent();
            } else {
                return notFound();
            }
        }  catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    @Security.Authenticated(BasicAuthenticator.class)
    public Result index(){
        return ok(toJson(products));
    }

    @Deprecated
    public Result edit(Product product) {

        return ok(views.html.products.form.render(product.getSku(), formFactory.form(Product.class)));

    }

    @Deprecated
    public Result update(String sku) {
        return ok("Received update request");
    }
}
