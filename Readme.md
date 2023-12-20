## trc-market-data-client-sdk Sample Application

This is a sample application of using trc-market-data-client-sdk

### Dependencies

1. java 11 sdk
2. trc-market-data-client-sdk

### Subscribing to fx market data

1. Create market data listener

   ```java
   public class MarketDataListener implements MarketDataCallback {   
        @Override
        public void onInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, FxInstrumentSnapshot fxInstrumentSnapshot) {
            System.out.println("On FX Data :" + fxInstrumentSnapshot);
        }
   
        @Override
        public void onEquityInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, EquityInstrumentSnapshot equityInstrumentSnapshot) {
            System.out.println("Received equity Data :" + equityInstrumentSnapshot);
        }
   
        @Override
        public void onSubscriptionFailure(MarketDataSubscriber marketDataSubscriber,
                                          SubscriptionDetails subscriptionDetails,
                                          SubscriptionError subscriptionError,
                                          String s) {
            System.out.printf("onSubscriptionFailure. InstrumentId:%s. ErrorCode:%s. Message:%s",
                    subscriptionDetails.getSubscribedInstrument(),
                    subscriptionError,
                    s);
        }
   }
   ```

2. Implement connection listener

   ```java
   public class ConnectionListener implements ConnectionCallback {
       @Override
       public void onConnect(MarketDataSubscriber marketDataSubscriber) {

       }

       @Override
       public void onReconnect(MarketDataSubscriber marketDataSubscriber) {

       }

       @Override
       public void onDisconnect(MarketDataSubscriber marketDataSubscriber, 
                                ConnectionError connectionError, 
                                String message) {
       }

       @Override
       public void onConnectionFailure(MarketDataSubscriber marketDataSubscriber, 
                                       ConnectionError connectionError,  
                                       String message) {
       }
   }
   ```

3. Subscribe for currency pairs/authorized equities in "onConnect" callback
   ```java
   public class ConnectionListener implements ConnectionCallback {
     @Override
     public void onConnect(MarketDataSubscriber marketDataSubscriber) {
        System.out.println("OnConnect");
        MarketDataListener marketDataListener = new MarketDataListener();
        marketDataSubscriber.subscribeFx("CHFJPY", marketDataListener);
        marketDataSubscriber.subscribeEquity("SAWAD", "JPY", marketDataListener);
        marketDataSubscriber.subscribeEquity("OSP", "GBP", marketDataListener);
     }
   }
   ```

4. Create subscriber

   ```
   MarketDataSubscriber subscriber = MarketDataSubscriberFactory.createSubscriber();
   ```
   > For GeneratedFxData subscription use  
   > MarketDataSubscriber subscriber = MarketDataSubscriberFactory.createGeneratedDataSubscriber();

5. Update trustStorePath path with client.trustStore path
   ```
   String trustStorePath = "app/src/main/resources/client.truststore";
   ```   

6. Connect
   ```
   SslConfig sslConfig = new SslConfig(null, null, trustStorePath, "gxw9dck*czu5XQW8azp");

   RetryConfig retryConfig = RetryConfig.builder().maxAttempts(-1).intervalMillis(5000).build());
   // -1 (or any non positive number) to try forever
   // OR RetryConfig retryConfig = RetryConfig.DISABLED; to disable */
   
   SubscriberConfig subscriberConfig = SubscriberConfig.builder().sslConfig(sslConfig).retryConfig(retryConfig).build();
   
   subscriber.connect("13.250.15.157", 56100, "test-user", "test-pw", subscriberConfig, connectionListener);
   ```
   > Since this is a test client username and password will be ignored.
7. Listen for data
   ```
    Received FX Data :FxInstrumentSnapshot(super=GenericInstrumentSnapshot(super=VersionedGenericMessage(version=1.0.0.1), timestamp=2023-12-20T07:46:19.940Z, instrumentId=USDJPY, symbol=USD/JPY, askPricePoint=PricePoint(price=143.459, quantity=100000.0), bidPricePoint=PricePoint(price=143.48, quantity=100000.0)), tenor=SPOT)
    Received equity Data :EquityInstrumentSnapshot(super=GenericInstrumentSnapshot(super=VersionedGenericMessage(version=1.0.0.1), timestamp=2023-12-20T07:45:52.722Z, instrumentId=V03.SI, symbol=V03.SI, askPricePoint=PricePoint(price=2414.17, quantity=null), bidPricePoint=PricePoint(price=2410.81, quantity=null)), subscriberCcy=JPY)
   ```

### Supported currency pairs

- GBPJPY
- CHFJPY
- AUDJPY
- CADJPY
- NZDJPY
- SGDJPY
- EURJPY
- CNHJPY
- HKDJPY
- USDJPY
- THBJPY

### Authorized equities for streaming

- To get streaming of equities, users need to be authorized on the TRC side to be granted access to those equities.
- The availability of equity streaming depends on market open/close status.

<br/><br/>

### Setting up a Keystore and a truststore
#### 1. Generating the Private Key
Replace the placeholder values with actual values that match your requirements. Make sure to note down the password; you will need it when configuring the SDK.
```
keytool -genkeypair -alias <YourAlias> -keyalg RSA -keysize 2048 -keystore <YourKeystoreName>.jks -dname "CN=<ClientName>, OU=<Department>, O=<Organization>, L=<City>, ST=<State>, C=<Country>"
```

#### 2. Create the CSR (Certificate Signing Request)
Generate the CSR and send the resulting CSR file to us. Once we've signed the request, we will send back the signed certificate. This will allow communication with our servers.
```
keytool -certreq -alias <YourAlias> -keystore <YourKeystoreName>.jks -file <YourCSRName>.csr
```

#### 3. Import the Signed Certificate into the Keystore
Import the signed certificate, which we provided, into the keystore you created in the first step.
```
keytool -importcert -alias <YourAlias> -keystore <YourKeystoreName>.jks -file <ReceivedSignedCertificate>.crt
```

#### 4. Set up the truststore
You'll need to configure a truststore containing our gateway's public certificate. We might either share the public certificate with you or provide an already generated truststore containing the public certificate.

##### A. If we provide you with the server certificate
Import this certificate into your truststore. Again, remember to note down the password; you'll need it when configuring the SDK.
```
keytool -importcert -alias <ServerAlias> -keystore <TruststoreName>.jks -file <ServerCertificateName>.crt
```

##### B. If we provide you with the truststore
Use it directly, using the password we provide.

<br/>

### Changelog
#### Version 1.6.0
* **Added**:
    * Add equity streaming capability
    * Introduced `EquityInstrumentSnapshot` to represent data obtained from equity streaming
    * Improved logging to contain more details
#### Version 1.5.0
* **Added**:
    * Replay subscriptions if statuses are not finalised(Subscribed/Rejected).
#### Version 1.3.0
* **Added**:
  * `MarketDataSubscriber.connect(host, port, username, password, SubscriberConfig, callback)` which will provide control over the reconnect functionality, or any other subscriber related functionality to be added in the future.
  * `ConnectionCallback.onConnect(MarketDataSubscriber)` to inform that the subscriber had reconnected to the backend.
  * Reconnect functionality - is enabled by default (retries indefinitely, with an interval of 5 seconds). Use the `SubscriberConfig` and the `RetryConfig` to disable if need be. If connection is lost and reconnected the previous subscriptions (pending or active) will be placed back automatically by the subscriber.
<br/><br/>
* **Deprecated**: <br/>
    > The following method will be removed in an upcoming release. However, for backward compatibility, they will function as before until removed.
    * `MarketDataSubscriber.connect(host, port, username, password, SslConfig, callback)` method. Please use the other, above-mentioned connect method.
<br/><br/>
##### Version 1.2.0
* **Added**:
    * `FxInstrumentSnapshot` was introduced in place of `InstrumentSnapshot` to better represent the type of data this structure it's carrying.
    * `MarketDataCallback.onInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, FxInstrumentSnapshot instrumentSnapshot)` to replace the other callback by the same name.
    * `MarketDataCallback.onSubscriptionFailure(MarketDataSubscriber, SubscriptionDetails, SubscriptionError, String)` to replace the other callback by the same name. The parameter `SubscriptionDetails` was added to be able to identify the subscription that failed.
      <br/><br/>
* **Deprecated**: <br/>
  > The following two callbacks will be removed in the next release. However, for backward compatibility, they will still be invoked during this release.
    * `MarketDataCallback.onSubscriptionFailure(MarketDataSubscriber, SubscriptionError, String)` method. Please override the other callback by the same name (mentioned above) instead.
    * `MarketDataCallback.onInstrumentSnapshot(MarketDataSubscriber, InstrumentSnapshot)` method. Please override the other callback by the same name (mentioned above) instead.
    * `InstrumentSnapshot` class is deprecated and is replaced by `FxInstrumentSnapshot`.

<br/>