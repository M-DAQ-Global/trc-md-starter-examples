package trc.md.starter;

import com.mdaq.trc.marketdata.client.MarketDataCallback;
import com.mdaq.trc.marketdata.client.MarketDataSubscriber;
import com.mdaq.trc.marketdata.client.enums.SubscriptionError;
import com.mdaq.trc.marketdata.client.model.generic.SubscriptionDetails;
import com.mdaq.trc.marketdata.sbe.messaging.messages.EquityInstrumentSnapshot;
import com.mdaq.trc.marketdata.sbe.messaging.messages.FxInstrumentSnapshot;

public class MarketDataListener implements MarketDataCallback {
    @Override
    public void onInstrumentSnapshot(MarketDataSubscriber marketDataSubscriber, FxInstrumentSnapshot fxInstrumentSnapshot) {
        System.out.println("Received FX Data :" + fxInstrumentSnapshot);
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
