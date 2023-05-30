## trc-market-data-client-sdk:1.0.0

This sdk will provide streaming connectivity with mdaq trc market data.

### Dependencies

1. java 11 sdk
2. trc-market-data-client-sdk:1.0.0
3. com.opencsv:opencsv:5.1

### Subscribing to fx market data

1. Create market data listener

   ```java
   public class MarketDataListener implements MarketDataCallback {
     @Override
     public void onSubscriptionFailure(MarketDataSubscriber marketDataSubscriber, SubscriptionError subscriptionError, String s) {
        System.out.printf("onSubscriptionFailure. ErrorCode:%s. Message:%s", subscriptionError, s);
     }

     @Override
     public void onInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, InstrumentSnapshot instrumentSnapshot) {
        System.out.println("On Data :" + instrumentSnapshot);
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
       public void onDisconnect(MarketDataSubscriber marketDataSubscriber, ConnectionError connectionError, String s) {

       }

       @Override
       public void onConnectionFailure(MarketDataSubscriber marketDataSubscriber, SubscriptionError subscriptionError, String s) {
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
5. Connect
   ```java
   SslConfig sslConfig = new SslConfig("test-ks-path", "test-ks-pw", "test-ts-path", "test-ts-pw");
   subscriber.connect("192.168.5.5", 23, "test-user", "test-pw", sslConfig, connectionListener);
   ```
   > Since this is a test client all connection parameters will be ignored. Details about these parameters will be shared in the next release
6. Listen for data
   ```
   On Data :InstrumentSnapshot{timestamp=2023-05-31T04:27:21.353967Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.29, quantity=1}, askPricePoint=PricePoint{price=153.37, quantity=1}}
   On Data :InstrumentSnapshot{timestamp=2023-05-31T04:27:22.308938Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.20, quantity=1}, askPricePoint=PricePoint{price=153.36, quantity=1}}
   On Data :InstrumentSnapshot{timestamp=2023-05-31T04:27:23.308795Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.09, quantity=1}, askPricePoint=PricePoint{price=153.37, quantity=1}}
   On Data :InstrumentSnapshot{timestamp=2023-05-31T04:27:24.308521Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.17, quantity=1}, askPricePoint=PricePoint{price=153.58, quantity=1}}
   On Data :InstrumentSnapshot{timestamp=2023-05-31T04:27:25.308810Z, instrumentId='CHFJPY', bidPricePoint=PricePoint{price=153.25, quantity=1}, askPricePoint=PricePoint{price=153.54, quantity=1}}
   ```
   <div style="page-break-after: always;"></div>

### Supported currency pairs

- GBPJPY
- CHFJPY
- AUDJPY
- CADJPY
- NZDJPY
- SGDJPY
- KRWJPY
- CNHJPY
- HKDJPY
- USDJPY
