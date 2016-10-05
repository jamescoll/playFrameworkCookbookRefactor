package services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import play.Configuration;
import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

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
@Singleton
public class JWTServiceImpl implements JWTService{

    final private String tokenPrefix = "Bearer ";
    private String issuer;
    private String sharedSecret;
    private Integer expiryTime;
    private String audience;
    private JWSHeader algorithm;
    private MACSigner signer;
    private MACVerifier verifier;



    ApplicationLifecycle applicationLifecycle;





    @Inject
    public JWTServiceImpl(ApplicationLifecycle applicationLifecycle, Configuration configuration) {



        this.applicationLifecycle = applicationLifecycle;

        //TODO these values should be being read from the config file but the various
        //approaches i've tried have failed to do this... including @Inject as a variable, @Inject in the constructor
        //and reading from the Play.current(). etc... methods .. I'm missing this incantation

        issuer = "JWT_ISSUER";
        sharedSecret = ";1pe8ajQ@qfxF^fE?FW5h;kdggCy4lNbXej`nIX6<MUnFIl9sAAC;F4zoWAVQl5K";
        expiryTime = 600;
        audience = "JWT_AUDIENCE";

        algorithm = new JWSHeader(JWSAlgorithm.HS256);
        signer = new MACSigner(sharedSecret);
        verifier = new MACVerifier(sharedSecret);


        applicationLifecycle.addStopHook(() -> {

            Logger.info("Closing down JWT implementation ");
            algorithm = null;
            signer = null;
            verifier = null;
            return CompletableFuture.completedFuture(null);
        });
    }

    @Override
    public boolean verify(String token) {
        try {
            final JWTClaimsSet payload = decode(token);

            //check expiration date
            if ( ! new Date().before(payload.getExpirationTime())){
                Logger.error("Token expired: " + payload.getExpirationTime());
                return false;
            }

            if ( !payload.getIssuer().equals(issuer)) {
                Logger.error("Issuer mismatch: " + payload.getIssuer());
                return false;
            }

            if(payload.getAudience() != null && payload.getAudience().size() > 0) {
                if (!payload.getAudience().get(0).equals(audience)) {
                    Logger.error("Audience mismatch: " + payload.getAudience().get(0));
                    return false;
                }
            } else {
                Logger.error("Audience is required");
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public JWTClaimsSet decode(String token) throws Exception {
        Logger.debug("Verifying: " + token.substring(tokenPrefix.length()));
        SignedJWT signed = SignedJWT.parse(token.substring(tokenPrefix.length()));

        if( !signed.verify(verifier)) {
            throw new IllegalArgumentException("Json web token cannot be verified!");
        }

        return (JWTClaimsSet) signed.getJWTClaimsSet();
    }

    @Override
    public String sign(String userInfo) throws Exception {
        final JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setSubject(userInfo);
        claimsSet.setIssueTime(new Date());
        claimsSet.setIssuer(issuer);
        claimsSet.setAudience(audience);
        claimsSet.setExpirationTime(new Date(claimsSet.getIssueTime().getTime() + (expiryTime * 1000)));

        SignedJWT signedJWT = new SignedJWT(algorithm, claimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }


}
