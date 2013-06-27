/*
 * Copyright 2011- Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spark.webserver;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import spark.SSLConfig;
import spark.StaticFileConfig;

/**
 * Spark server implementation
 *
 * @author Per Wendel
 */
class SparkServerImpl implements SparkServer {

    private static final String NAME = "Spark";
    private Handler handler;
    private final String ipAddress;
    private final int port;
    private final SSLConfig sslConfig;
    private final StaticFileConfig staticFileConfig;
    private Server server = new Server();

    public SparkServerImpl(Handler handler, String ipAddress, int port, SSLConfig sslConfig, StaticFileConfig staticFileConfig) {
        this.handler = handler;
        this.ipAddress = ipAddress;
        this.port = port;
        this.sslConfig = sslConfig;
        this.staticFileConfig = staticFileConfig;
        System.setProperty("org.mortbay.log.class", "spark.JettyLogger");
    }

    @Override
    public void ignite() {
        SocketConnector connector = new SocketConnector();

        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setHost(ipAddress);
        connector.setPort(port);
        server.setConnectors(new Connector[] {connector});

        server.setHandler(handler);

        try {
            System.out.println("== " + NAME + " has ignited ...");
			System.out.println(">> Listening on " + ipAddress + ":" + port);

            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }

    @Override
    public void stop() {
        System.out.print(">>> " + NAME + " shutting down...");
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
        System.out.println("done");
    }

}
