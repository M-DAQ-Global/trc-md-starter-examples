## Sample Application for trc-market-data-client-sdk

### Dependencies

1. java 11 sdk
2. Correct SSL keystore and truststore certificate files should already available in resources folder - please speak our site reliability team 
3. SDK will be preconfigured for the necessary instruments which you require us to stream data for. Please mention your requirements to site reliability team. SDK has capability to stream prices for various asset classes based on your requirement and is not just limited to an FX stream

### How to stream prices:
Simply download and extract in your application environment, build and execute App.java file available under:
`trc-md-starter/app/src/main/java/`

### Supported FX currency pairs Sample

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

### Subscribing to market data

1. Obtain valid SSL keystore and truststore files from MDAQ TRC and place those files under the directory 
   - `trc-md-starter/app/src/main/resources`

2. The application uses a centralized `config.properties` file for managing subscriber, retry, and SSL configurations. Below is a detailed guide on the available configurations:
   - `subscriber.url`: The hostname or IP address of the market data server.
   - `subscriber.port`: The port to connect to on the server.
   - `subscriber.username`: The username for authentication.
   - `subscriber.password`: The password for authentication.
   - `retry.maxAttempts`: The maximum number of reconnection attempts. Use `-1` for unlimited retries.
   - `retry.intervalMillis`: The interval (in milliseconds) between reconnection attempts. For example, `1000`. 
   - `ssl.keyStoreFileName`: The name of the keystore file to use, should include extension if any.
   - `ssl.trustStoreFileName`: The name of the truststore file to use, should include extension if any.
   - `ssl.keyStorePassword`: The password for the keystore. It is recommended to provide this password via an environment variable for security.
   - `ssl.trustStorePassword`: The password for the truststore. Like the keystore password, this should also be provided via an environment variable for security.

3. Configure market data listener callbacks

   ```java
   public class MarketDataListener implements MarketDataCallback {   
        @Override
        public void onInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, FxInstrumentSnapshot fxInstrumentSnapshot) {
            System.out.println("On FX Data :" + fxInstrumentSnapshot);
        }
   
        @Override
        public void onTieredFxInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, TieredFxInstrumentSnapshot tieredFxInstrumentSnapshot) {
            System.out.println("On Tiered FX Data :" + tieredFxInstrumentSnapshot);
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

4. Configure connection listener callbacks

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

5. Configure currency pairs/equities to subscribe to under "onConnect" callback
   ```java
   public class ConnectionListener implements ConnectionCallback {
     @Override
     public void onConnect(MarketDataSubscriber marketDataSubscriber) {
        System.out.println("OnConnect");
        MarketDataListener marketDataListener = new MarketDataListener();
   
        //To subscribe for single tier pricing use following API
        marketDataSubscriber.subscribeFx("USDJPY", marketDataListener);
   
        // To subscribe for tiered pricing use following API
        marketDataSubscriber.subscribeFx("CHFJPY", SubscriptionType.TIERED_TOB, marketDataListener);
   
        // To subscribe for blended/unblended equities use following API
        marketDataSubscriber.subscribeEquity("AAPL", "JPY", marketDataListener);
     }
   }
   ```

6. Create subscriber
   ```
   MarketDataSubscriber subscriber = MarketDataSubscriberFactory.createSubscriber();
   ```
   For GeneratedFxData subscription use
   ```
   MarketDataSubscriber subscriber = MarketDataSubscriberFactory.createGeneratedDataSubscriber();
   ```

7. Connect
   ```
   subscriber.connect(<TARGET_URL>, <PORT>, <USERNAME>, <PASSWORD>, subscriberConfig, connectionListener);
   ```
   > Since this is a test client username and password will be ignored.
8. Listen for data
   ```
    Received FX Data :FxInstrumentSnapshot(super=GenericInstrumentSnapshot(super=VersionedGenericMessage(version=1.0.0.1), timestamp=2023-12-20T07:46:19.940Z, instrumentId=USDJPY, symbol=USD/JPY, askPricePoint=PricePoint(price=143.459, quantity=100000.0), bidPricePoint=PricePoint(price=143.48, quantity=100000.0)), tenor=SPOT)
    Received equity Data :EquityInstrumentSnapshot(super=GenericInstrumentSnapshot(super=VersionedGenericMessage(version=1.0.0.1), timestamp=2023-12-20T07:45:52.722Z, instrumentId=V03.SI, symbol=V03.SI, askPricePoint=PricePoint(price=2414.17, quantity=null), bidPricePoint=PricePoint(price=2410.81, quantity=null)), subscriberCcy=JPY)
    Received Tiered FX Data :TieredFxInstrumentSnapshot(super=VersionedGenericMessage(version=1.0.0.1), timestamp=2025-01-13T10:25:02.460Z, instrumentId=USDJPY, symbol=USD/JPY, askTiers=[PricePoint(price=157.270000, quantity=1000000.0), PricePoint(price=157.272000, quantity=3000000.0), PricePoint(price=157.274000, quantity=5000000.0)], bidTiers=[PricePoint(price=157.267000, quantity=1000000.0), PricePoint(price=157.265000, quantity=3000000.0), PricePoint(price=157.260000, quantity=5000000.0)], tenor=SPOT, fxSnapshotType=TRADABLE)
   ```
9. Disconnect, will unsubscribe all previous subscriptions
    ```
   subscriber.disconnect();
   ```

<br/><br/>

### Changelog
#### Version 1.16.0
* **Added**:
    * Support for centralized configuration setup.
    * Support to subscribe/unsubscribe to tiered FX data
#### Version 1.7.0
* **Added**:
    * Handle duplicate subscription requests
    * Unsubscribe FX upon disconnect/logout
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