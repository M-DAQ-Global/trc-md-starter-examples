package trc.md.starter.callback;

import com.mdaq.trc.marketdata.client.ConnectionCallback;
import com.mdaq.trc.marketdata.client.MarketDataSubscriber;
import com.mdaq.trc.marketdata.client.enums.ConnectionError;

public class ConnectionListener implements ConnectionCallback {
    @Override
    public void onConnect(MarketDataSubscriber marketDataSubscriber) {
        System.out.println("OnConnect");
        MarketDataListener marketDataListener = new MarketDataListener();

        marketDataSubscriber.subscribeFx("USDJPY", marketDataListener);

        marketDataSubscriber.subscribeEquity("AAPL", "SGD", marketDataListener);
    }

    @Override
    public void onReconnect(MarketDataSubscriber marketDataSubscriber) {
        System.out.println("OnReconnect");
    }

    @Override
    public void onDisconnect(MarketDataSubscriber marketDataSubscriber, ConnectionError connectionError, String message) {
        System.out.println("OnDisconnect");
    }

    @Override
    public void onConnectionFailure(MarketDataSubscriber marketDataSubscriber, ConnectionError subscriptionError, String message) {
        System.out.println("ConnectionFailure");
    }
}
