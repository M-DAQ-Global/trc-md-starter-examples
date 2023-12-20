package trc.md.starter;

import com.mdaq.trc.marketdata.client.ConnectionCallback;
import com.mdaq.trc.marketdata.client.MarketDataSubscriber;
import com.mdaq.trc.marketdata.client.enums.ConnectionError;

public class ConnectionListener implements ConnectionCallback {
    @Override
    public void onConnect(MarketDataSubscriber marketDataSubscriber) {
        System.out.println("OnConnect");
        MarketDataListener marketDataListener = new MarketDataListener();
        marketDataSubscriber.subscribeFx("USDJPY", marketDataListener);
        marketDataSubscriber.subscribeEquity("V03.SI", "JPY", marketDataListener);
        marketDataSubscriber.subscribeEquity("SAWAD", "JPY", marketDataListener);
        marketDataSubscriber.subscribeEquity("OSP", "GBP", marketDataListener);

//        for (String instrument : "USDJPY,HKDJPY,GBPJPY,CHFJPY,AUDJPY,CADJPY,NZDJPY,SGDJPY,EURJPY,CNHJPY,THBJPY".split(",")) {
//            marketDataSubscriber.subscribeFx(instrument, marketDataListener);
//        }

    }

    @Override
    public void onReconnect(MarketDataSubscriber marketDataSubscriber) {
        System.out.println("OnReconnect");
    }

    @Override
    public void onDisconnect(MarketDataSubscriber marketDataSubscriber, ConnectionError connectionError, String message) {

    }

    @Override
    public void onConnectionFailure(MarketDataSubscriber marketDataSubscriber, ConnectionError subscriptionError, String message) {

    }
}
