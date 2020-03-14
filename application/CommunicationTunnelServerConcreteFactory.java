package application;

public class CommunicationTunnelServerConcreteFactory extends CommunicationTunnelFactory {
    @Override
    public CommunicationTunnel getInstance() {
        return new CommunicationTunnelServerSide();
    }
}
