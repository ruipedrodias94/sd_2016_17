package facebook;

        import com.github.scribejava.apis.FacebookApi;
        import com.github.scribejava.core.builder.ServiceBuilder;
        import com.github.scribejava.core.model.Token;
        import com.github.scribejava.core.oauth.OAuth20Service;
        import com.sun.javafx.fxml.expression.Expression;

        import java.util.Random;

/**
 * Created by Rui Pedro Dias on 15/12/2016.
 */
public class OAuthFace {


    final String clientId = "1692489987709601";
    final String clientSecret = "d3b58f3dfa1180d96dc4e41453d313c6";
    final String secretState = "secret" + new Random().nextInt(999_999);
    final String calbackUri = "http://localhost:8080/oauth_callback/";

    final OAuth20Service service = new ServiceBuilder()
            .apiKey(clientId)
            .apiSecret(clientSecret)
            .state(secretState)
            .callback(calbackUri)
            .build(FacebookApi.instance());


}


