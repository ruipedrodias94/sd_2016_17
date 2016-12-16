package facebook;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * Created by Rui Pedro Dias on 16/12/2016.
 */
public class FacebookApi extends DefaultApi10a {

    private static final String authorizationURL = "";
    private static final String requestToken = "";
    private static final String accessToken = "";

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken oAuth1RequestToken) {
        return null;
    }
}
