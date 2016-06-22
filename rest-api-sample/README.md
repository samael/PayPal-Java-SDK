REST API Samples
===================

This project contains a set of samples that you can explore to understand what the REST APIs can do for you. The sample comes pre-configured with a test account but in case you need to try them against your account, you must obtain your client id and client secret from the developer portal.

Build and run the samples
--------------------------

  * Simply run `./gradlew clean jettyRun` to start jetty server.
  * Access `http://localhost:<jetty-port>/rest-api-sample/` in your browser to view samples.

Test Account
------------

   * Test Client ID and Client Secret can be found in the file sdk_config.properties file under src/main/resources/ folder.
   * The endpoint URL for token generation and API calls are fetched from sdk_config.properties file under src/main/resources/ folder.
   * AccessToken are generated once using GenerateAccessToken.java and used for the samples.

## OpenID Connect Integration
   * Redirect your buyer to `Authorization.getRedirectUrl(redirectURI, scope, configurationMap);` to obtain authorization.
   * Capture the authorization code that is available as a query parameter (`code`) in the redirect url
   * Exchange the authorization code for a access token, refresh token, id token combo

```java
    Map<String, String> configurationMap = new HashMap<String, String>();
    configurationMap.put(Constants.CLIENT_ID, "...");
    configurationMap.put(Constants.CLIENT_SECRET, "...");
    configurationMap.put(Constants.ENDPOINT, "https://api.paypal.com/");
    APIContext apiContext = new APIContext();
    apiContext.setConfigurationMap(configurationMap);
    ...
    CreateFromAuthorizationCodeParameters param = new CreateFromAuthorizationCodeParameters();
    param.setCode(code);
    Tokeninfo info = Tokeninfo.createFromAuthorizationCode(apiContext, param);
    String accessToken = info.getAccessToken();
```
   * The access token is valid for a predefined duration and can be used for seamless XO or for retrieving user information

```java
    Map<String, String> configurationMap = new HashMap<String, String>();
    configurationMap.put(Constants.CLIENT_ID, "...");
    configurationMap.put(Constants.CLIENT_SECRET, "...");
    configurationMap.put(Constants.ENDPOINT, "https://api.paypal.com/");
    APIContext apiContext = new APIContext();
    apiContext.setConfigurationMap(configurationMap);
    ...
    Tokeninfo info = new Tokeninfo();
    info.setRefreshToken("refreshToken");
    UserinfoParameters param = new UserinfoParameters();
    param.setAccessToken(info.getAccessToken());
    Userinfo userInfo = Userinfo.userinfo(apiContext, param);
```
   * If the access token has expired, you can obtain a new access token using the refresh token from the 3'rd step.

```java
    Map<String, String> configurationMap = new HashMap<String, String>();
    configurationMap.put(Constants.CLIENT_ID, "...");
    configurationMap.put(Constants.CLIENT_SECRET, "...");
    configurationMap.put(Constants.ENDPOINT, "https://api.paypal.com/");
    APIContext apiContext = new APIContext();
    apiContext.setConfigurationMap(configurationMap);
    ...
    CreateFromRefreshTokenParameters param = new CreateFromRefreshTokenParameters();
    param.setScope("openid"); // Optional
    Tokeninfo info = new Tokeninfo // Create Token info object; setting the refresh token
    info.setRefreshToken("refreshToken");

    info.createFromRefreshToken(apiContext, param);
```
