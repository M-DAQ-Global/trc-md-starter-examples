## trc-market-data-client-sdk Sample Application

This is a sample application of using trc-market-data-client-sdk

### Dependencies

1. java 11 sdk
2. trc-market-data-client-sdk

<br/>

### Subscribing to fx market data

1. Create market data listener

   ```java
   public class MarketDataListener implements MarketDataCallback {   
        @Override
        public void onInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, FxInstrumentSnapshot fxInstrumentSnapshot) {
            System.out.println("On Data :" + fxInstrumentSnapshot);
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

<div style="page-break-after: always;"></div>

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

3. Subscribe for currency pairs in "onConnect" callback
   ```java
   public class ConnectionListener implements ConnectionCallback {
     @Override
     public void onConnect(MarketDataSubscriber marketDataSubscriber) {
        System.out.println("OnConnect");
        MarketDataListener marketDataListener = new MarketDataListener();
        marketDataSubscriber.subscribeFx("CHFJPY", marketDataListener);
     }
   }
   ```
4. Create subscriber

   ```java
   MarketDataSubscriber subscriber = MarketDataSubscriberFactory.createSubscriber();
   ```
   
<div style="page-break-after: always;"></div>

5. Update trustStorePath path with client.trustStore path
   ```
   String trustStorePath = "app/src/main/resources/client.truststore";
   ```   

7. Connect
   ```java
   SslConfig sslConfig = new SslConfig(null, null, trustStorePath, "gxw9dck*czu5XQW8azp");

   RetryConfig retryConfig = RetryConfig.builder().maxAttempts(-1).intervalMillis(5000).build());
   // -1 (or any non positive number) to try forever
   // OR RetryConfig retryConfig = RetryConfig.DISABLED; to disable */
   
   SubscriberConfig subscriberConfig = SubscriberConfig.builder().sslConfig(sslConfig).retryConfig(retryConfig).build();
   
   subscriber.connect("13.250.15.157", 56100, "test-user", "test-pw", subscriberConfig, connectionListener);
   ```
   > Since this is a test client username and password will be ignored. Details about these parameters will be shared in the next release
8. Listen for data
   ```
   On Data :InstrumentSnapshot{timestamp=2023-06-15T07:44:04.095054Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.601, quantity=1000000}, askPricePoint=PricePoint{price=153.611, quantity=1000000}}
   On Data :InstrumentSnapshot{timestamp=2023-06-15T07:44:05.022826Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.689, quantity=1000000}, askPricePoint=PricePoint{price=153.709, quantity=1000000}}
   On Data :InstrumentSnapshot{timestamp=2023-06-15T07:44:06.022660Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.613, quantity=1000000}, askPricePoint=PricePoint{price=153.621, quantity=1000000}}
   On Data :InstrumentSnapshot{timestamp=2023-06-15T07:44:07.022487Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.572, quantity=1000000}, askPricePoint=PricePoint{price=153.591, quantity=1000000}}
   ```

<div style="page-break-after: always;"></div>

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
  
<br/><br/>

### Changelog
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