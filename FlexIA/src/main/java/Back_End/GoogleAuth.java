package Back_End;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import java.util.Collections;

public class GoogleAuth {

    private static final String CLIENT_ID = "356481320366-u48gbvt7ji782qkc5o5g8hmpgj12dnv3.apps.googleusercontent.com";

    private static final String CLIENT_SECRET = "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";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static GoogleUser loginWithGoogle() throws Exception {

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET);

        GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setInstalled(details);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                Collections.singletonList("openid email profile"))
                .setDataStoreFactory(new MemoryDataStoreFactory())
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("user");

        // ✅ Access Token válido
        String accessToken = credential.getAccessToken();

        // 🔥 Llamada oficial a UserInfo
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory(
                request -> {
                    request.getHeaders().setAuthorization("Bearer " + accessToken);
                    request.setParser(new com.google.api.client.json.JsonObjectParser(JSON_FACTORY));
                });

        GenericUrl url = new GenericUrl("https://www.googleapis.com/oauth2/v1/userinfo");

        HttpRequest request = requestFactory.buildGetRequest(url);
        request.setParser(JSON_FACTORY.createJsonObjectParser());
        GoogleUser user = request.execute().parseAs(GoogleUser.class);

        return user;
    }
}
