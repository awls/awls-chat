from twisted.internet.protocol import Factory
from twisted.protocols.basic import LineReceiver
from twisted.internet import reactor
from twisted.python.constants import NamedConstant, Names
from sets import Set

class STATE(Names):
    AUTHENTIFICATION = NamedConstant()
    LOGIN = NamedConstant()
    CHAT = NamedConstant()

class ClientProtocol(LineReceiver):
    def __init__(self, factory):
        self.factory = factory

    def connectionMade(self):
        self.state = STATE.AUTHENTIFICATION
        self.sendLine("Welcome to the official Awls chat service!")

    def lineReceived(self, line):
        if self.state == STATE.AUTHENTIFICATION:
            self.handle_authenticate(line)
        elif self.state == STATE.LOGIN:
            self.handle_login(line)
        elif self.state == STATE.CHAT:                
            self.handle_chat(line)

    def handle_authenticate(self, msg):
        if msg == "God save the Awls!":
            self.state = STATE.LOGIN
            self.sendLine("God save the Awls, indeed!")
            self.sendLine("Please introduce yourself.")
        else:
            self.transport.loseConnection()

    def handle_login(self, msg):
        self.name = msg
        self.factory.users.add(self)
        self.state = STATE.CHAT
        self.sendLine("Welcome, %s." % self.name)
                        
    def handle_chat(self, line):
        self.factory.broadcast(self, line)

class ChatFactory(Factory):
    def __init__(self):
        self.users = Set()

    def buildProtocol(self, addr):
        return ClientProtocol(self)

    def broadcast(self, protocol, msg):
        for user in self.users:
            if user != protocol:
                user.sendLine("%s: %s" % (protocol.name, msg))

reactor.listenTCP(8000, ChatFactory())
reactor.run()
