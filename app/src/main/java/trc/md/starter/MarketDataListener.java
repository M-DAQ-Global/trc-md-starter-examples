package trc.md.starter;

import com.mdaq.trc.marketdata.client.MarketDataCallback;
import com.mdaq.trc.marketdata.client.MarketDataSubscriber;
import com.mdaq.trc.marketdata.client.enums.SubscriptionError;
import com.mdaq.trc.marketdata.sbe.messaging.messages.InstrumentSnapshot;

public class MarketDataListener implements MarketDataCallback {
    @Override
    public void onSubscriptionFailure(MarketDataSubscriber marketDataSubscriber, SubscriptionError subscriptionError,
                                      String s) {
        System.out.printf("onSubscriptionFailure. ErrorCode:%s. Message:%s", subscriptionError, s);
    }

    @Override
    public void onInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, InstrumentSnapshot instrumentSnapshot) {
        System.out.println("On Data :" + instrumentSnapshot);
    }
}
