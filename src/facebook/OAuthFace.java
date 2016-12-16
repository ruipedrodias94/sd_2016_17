package facebook;

        import com.github.scribejava.apis.FacebookApi;
        import com.github.scribejava.core.builder.ServiceBuilder;
        import com.github.scribejava.core.model.Token;
        import com.github.scribejava.core.oauth.OAuth20Service;
        import com.sun.javafx.fxml.expression.Expression;

        import java.util.Map;
        import java.util.Random;

/**
 * Created by Rui Pedro Dias on 15/12/2016.
 */
public class OAuthFace {


    private static String clientId = "1692489987709601";
    private static String clientSecret = "d3b58f3dfa1180d96dc4e41453d313c6";
    private static String secretState = "secret" + new Random().nextInt(999_999);
    private static String calbackUri = "http://localhost:8080/oauth_callback/";

    private static OAuth20Service service = new ServiceBuilder()
            .apiKey(clientId)
            .apiSecret(clientSecret)
            .state(secretState)
            .callback(calbackUri)
            .build(FacebookApi.instance());

    public String getAuthorizationURL(){
        return service.getAuthorizationUrl();
    }



}


