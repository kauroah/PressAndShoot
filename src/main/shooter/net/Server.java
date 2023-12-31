package src.main.shooter.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import src.main.shooter.game.ServerGame;
import src.main.shooter.net.packets.ActionPacket;
import src.main.shooter.net.packets.DisconnectPacket;
import src.main.shooter.net.packets.ClientPacket;


public class Server implements Runnable {
    private final int TICKS_PER_SECOND = 20;
    private final int MILLISECONDS_PER_TICK = 1000000000 / TICKS_PER_SECOND;

    public final static int DEFAULT_PORT_NUMBER = 8080;

    private final ServerGame game;
    private ServerSocket serverSocket;
    private final ArrayList<ClientHandler> clientHandlers;

    public Server(final ServerGame game, final int port) {
        this.game = game;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        clientHandlers = new ArrayList<ClientHandler>();
    }

    @Override
    public void run() {
        new Thread(() -> startAcceptClientsLoop()).start();
        new Thread(() -> startGameloop()).start();
    }

    private void startAcceptClientsLoop() {
        System.out.println("Accepting Clients.");
        while (true) {
            System.out.println("Waiting for new client.");
            try {
                final Socket socket = serverSocket.accept();
                System.out.println("A new client has connected.");
                final ClientHandler clientHandler = new ClientHandler(this, socket, game.spawnPlayerEntity());
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGameloop() {
        long lastTickTime = System.nanoTime();

        while (true) {
            final long whenShouldNextTickRun = lastTickTime + MILLISECONDS_PER_TICK;
            if (System.nanoTime() < whenShouldNextTickRun) {
                continue;
            }

            game.tick();

            sendUpdatesToAll();

            lastTickTime = System.nanoTime();
        }
    }

    public void processPacket(final ClientHandler clientHandler, final ClientPacket packet) {
        if (packet instanceof final ActionPacket actionPacket) {
            game.updateActionSet(clientHandler.getEntityId(), actionPacket.actionSet);
        } else if (packet instanceof final DisconnectPacket disconnectPacket) {
            clientHandler.disconnect();
            game.removeEntity(clientHandler.getEntityId());
            clientHandlers.remove(clientHandler);
        }
    }

    private void sendUpdatesToAll() {
        for (final ClientHandler clientHandler : clientHandlers) {
            sendUpdates(clientHandler);
        }
    }

    public void sendUpdates(final ClientHandler clientHandler) {
        clientHandler.sendUpdate(game.getEntities());
    }

    public void closeServer() {

        try {
            serverSocket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void main(final String[] args) {
        new Server(new ServerGame(), Server.DEFAULT_PORT_NUMBER).run();
    }
}
