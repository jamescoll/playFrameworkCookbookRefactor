package services;

import javax.inject.Singleton;
import java.util.UUID;

/**
 * Project name: foo_java
 *
 * Package name : services
 *
 * Created by: jamescoll
 *
 * Date: 04/10/2016
 *
 *
 */
@Singleton
public class ProductServiceImpl implements ProductService {
    @Override
    public String generateProductId() {
        return UUID.randomUUID().toString();
    }
}
