package trc.md.starter;

import com.mdaq.trc.marketdata.client.MarketDataSubscriber;
import com.mdaq.trc.marketdata.client.MarketDataSubscriberFactory;
import com.mdaq.trc.marketdata.client.common.config.RetryConfig;
import com.mdaq.trc.marketdata.client.common.config.SubscriberConfig;
import trc.md.starter.callback.ConnectionListener;
import trc.md.starter.config.ConfigurationLoader;

public class App {
    public static void main(String[] args) {

        // Load configurations
        String url = ConfigurationLoader.getUrl();
        int port = ConfigurationLoader.getPort();
        String username = ConfigurationLoader.getUsername();
        String password = ConfigurationLoader.getPassword();
        RetryConfig retryConfig = ConfigurationLoader.getRetryConfig();
        var sslConfig = ConfigurationLoader.getSslConfig();

        // Create subscriber
        ConnectionListener connectionListener = new ConnectionListener();
        MarketDataSubscriber subscriber = MarketDataSubscriberFactory.createSubscriber();
        var subscriberConfig = SubscriberConfig.builder()
                .sslConfig(sslConfig)
                .retryConfig(retryConfig)
                .build();

        // Connect
        subscriber.connect(url, port, username, password, subscriberConfig, connectionListener);

        // Disconnect
        //subscriber.disconnect();
    }
}