package trc.md.starter;

import com.mdaq.trc.marketdata.client.ConnectionCallback;
import com.mdaq.trc.marketdata.client.MarketDataSubscriber;
import com.mdaq.trc.marketdata.client.enums.ConnectionError;
import com.mdaq.trc.marketdata.client.enums.SubscriptionError;

public class ConnectionListener implements ConnectionCallback {
    @Override
    public void onConnect(MarketDataSubscriber marketDataSubscriber) {
        System.out.println("OnConnect");
        MarketDataListener marketDataListener = new MarketDataListener();
        marketDataSubscriber.subscribeFx("USDSGD", marketDataListener);

//        for (String instrument : "USDJPY,HKDJPY,GBPJPY,CHFJPY,AUDJPY,CADJPY,NZDJPY,SGDJPY,EURJPY,CNHJPY".split(",")) {
//            marketDataSubscriber.subscribeFx(instrument, marketDataListener);
//        }

    }

    @Override
    public void onDisconnect(MarketDataSubscriber marketDataSubscriber, ConnectionError connectionError, String message) {

    }

    @Override
    public void onConnectionFailure(MarketDataSubscriber marketDataSubscriber, ConnectionError subscriptionError, String message) {

    }
}
