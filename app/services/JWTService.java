package services;

import com.nimbusds.jwt.JWTClaimsSet;
/**
 * Project name: foo_java
 *
 * Package name : services
 *
 * Created by: jamescoll
 *
 * Date: 05/10/2016
 *
 *
 */
public interface JWTService {
    boolean verify(String token);
    JWTClaimsSet decode (String token) throws Exception;
    String sign(String userInfo) throws Exception;

}
