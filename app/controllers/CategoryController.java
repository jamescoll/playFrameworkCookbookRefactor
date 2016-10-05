package controllers;

import com.google.inject.Inject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.CategoryService;

public class CategoryController extends Controller {

    @Inject
    private CategoryService categoryService;

    public Result index() {
        return ok(Json.toJson(categoryService.list()));
    }
}