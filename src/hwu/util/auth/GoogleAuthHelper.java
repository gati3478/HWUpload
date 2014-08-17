package hwu.util.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

/**
 * A helper class for Google's OAuth2 authentication API.
 * 
 * @version 20130224
 * @author Matyas Danter
 * @author Giorgi Petriashvili
 */
public final class GoogleAuthHelper {
	public static final String ATTRIBUTE_NAME = "googleauthhelper";

	/**
	 * Please provide a value for the CLIENT_ID constant before proceeding, set
	 * this up at https://code.google.com/apis/console/
	 */
	private static final String CLIENT_ID = "492569242017-2lkutu2a75pha5a3up5e59gnq3sths7k.apps.googleusercontent.com";
	/**
	 * Please provide a value for the CLIENT_SECRET constant before proceeding,
	 * set this up at https://code.google.com/apis/console/
	 */
	private static final String CLIENT_SECRET = "WEzuvt9mE567KTzoiIMW7uFf";

	/**
	 * Callback URI that google will redirect to after successful authentication
	 */
	private static final String CALLBACK_URI = "http://localhost:8080/HWUpload/GoogleSign";

	/**
	 * Restricting sign-in for specified domain
	 */
	public static final String HOSTED_DOMAIN = "freeuni.edu.ge";

	// start google authentication constants
	private static final Collection<String> SCOPE = Arrays
			.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email"
					.split(";"));
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	// end google authentication constants

	private String stateToken;
	private final GoogleAuthorizationCodeFlow flow;

	/**
	 * Constructor initializes the Google Authorization Code Flow with CLIENT
	 * ID, SECRET, and SCOPE.
	 */
	public GoogleAuthHelper() {
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).build();
		generateStateToken();
	}

	/**
	 * Builds a login URL based on client ID, secret, callback URI, and scope
	 */
	public String buildLoginUrl() {
		final GoogleAuthorizationCodeRequestUrl url = flow
				.newAuthorizationUrl();
		StringBuilder authUrlBuilder = new StringBuilder(url
				.setRedirectUri(CALLBACK_URI).setState(stateToken).build());
		authUrlBuilder.append("&prompt=select_account");
		// authUrlBuilder.append("&hd=" + HOSTED_DOMAIN);
		return authUrlBuilder.toString();
	}

	/**
	 * Generates a secure state token.
	 */
	private void generateStateToken() {
		SecureRandom sr1 = new SecureRandom();
		stateToken = "google;" + sr1.nextInt();
	}

	/**
	 * Accessor for state token.
	 */
	public String getStateToken() {
		return stateToken;
	}

	/**
	 * Expects an Authentication Code, and makes an authenticated request for
	 * the user's profile information.
	 * 
	 * @return JSON formatted user profile information
	 * @param authCode
	 *            authentication code provided by google
	 */
	public String getUserInfoJson(final String authCode) throws IOException {
		final GoogleTokenResponse response = flow.newTokenRequest(authCode)
				.setRedirectUri(CALLBACK_URI).execute();
		final Credential credential = flow.createAndStoreCredential(response,
				null);
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(credential);
		// Make an authenticated request
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		final String jsonIdentity = request.execute().parseAsString();

		return jsonIdentity;
	}

}
