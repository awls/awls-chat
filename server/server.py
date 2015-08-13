from twisted.internet.protocol import Factory
from twisted.protocols.basic import LineReceiver
from twisted.internet import reactor
from sets import Set

class ClientHandler(LineReceiver):
    def __init__(self, factory):
        self.factory = factory
        self.state = "clientstate.authenticate"

    def connectionMade(self):
        self.sendLine("Welcome to Awls official chat service")

    def lineReceived(self, line):
        if self.state == "clientstate.authenticate":
            self.handle_authenticate(line)
        elif self.state == "clientstate.active":
            self.handle_active(line)

    def handle_authenticate(self, line):
        if line == "God save the Awls!":
            self.state = "clientstate.active"
            self.factory.active_users.add(self)
            self.sendLine("God save the Awls, indeed!")
        else:
            self.transport.loseConnection()

    def handle_active(self, line):
        for active_user in self.factory.active_users:
            if (active_user != self):
                active_user.sendLine(line)

class ChatFactory(Factory):
    def __init__(self):
        self.active_users = Set()

    def buildProtocol(self, addr):
        return ClientHandler(self)

reactor.listenTCP(8000, ChatFactory())
reactor.run()
