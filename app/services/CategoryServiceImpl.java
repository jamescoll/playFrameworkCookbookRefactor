package services;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

/**
 * Project name: foo_java
 *
 * Package name : services
 *
 * Created by: jamescoll
 *
 * Date: 03/10/2016
 *
 *
 */
@Singleton
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<String> list() {
        return Arrays.asList(new String[]{"Manager", "Employee", "Contractor"});
    }
}
