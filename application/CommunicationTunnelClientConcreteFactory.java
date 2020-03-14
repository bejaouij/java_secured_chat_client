package application;

public class CommunicationTunnelClientConcreteFactory extends CommunicationTunnelFactory {
    @Override
    public CommunicationTunnel getInstance() {
        return new CommunicationTunnelClientSide();
    }
}
