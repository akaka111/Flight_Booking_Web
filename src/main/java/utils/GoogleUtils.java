/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package utils;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class GoogleUtils {
 private static final String CLIENT_SECRET_FILE = "/client_secret.json";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String REDIRECT_URI = "http://localhost:9998/NovelWeb/Login?action=googlereceive";

    public static String getGoogleLoginUrl() throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(GoogleUtils.class.getResourceAsStream(CLIENT_SECRET_FILE)));

        AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singleton("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email"))
                .build();

        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI);
        return authorizationUrl.build();
    }

    public static Userinfo getUserInfo(String code) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(GoogleUtils.class.getResourceAsStream(CLIENT_SECRET_FILE)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singleton("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email"))
                .build();

        AuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI);
        TokenResponse tokenResponse = tokenRequest.execute();
        Credential credential = flow.createAndStoreCredential(tokenResponse, null);

        Oauth2 oauth2 = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Google Login App")
                .build();

        return oauth2.userinfo().get().execute();
    }
}
