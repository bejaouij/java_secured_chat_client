package application;

public abstract class CommunicationTunnelFactory {
    public static String SERVER_KIND = "server";
    public static String CLIENT_KIND = "client";

    public static CommunicationTunnelFactory getFactory(String kind) throws SecuredChatWrongTypeOfExecutionException {
        if (kind.equals(CLIENT_KIND)) {
            return new CommunicationTunnelClientConcreteFactory();
        }

        if (kind.equals(SERVER_KIND)) {
            return new CommunicationTunnelServerConcreteFactory();
        }

        throw new SecuredChatWrongTypeOfExecutionException();
    }

    public abstract CommunicationTunnel getInstance();
}
