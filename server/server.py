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
        
    def connectionLost(self, reason):
        self.factory.leave(self)

    def lineReceived(self, line):
        if self.state == STATE.AUTHENTIFICATION:
            self.authenticate(line)
        elif self.state == STATE.LOGIN:
            self.login(line)
        elif self.state == STATE.CHAT:                
            self.chat(line)

    def authenticate(self, msg):
        if msg == "God save the Awls!":
            self.state = STATE.LOGIN
            self.sendLine("Please introduce yourself.")
        else:
            self.transport.loseConnection()

    def login(self, msg):
        self.name = msg
        self.state = STATE.CHAT
        self.factory.join(self)
                        
    def chat(self, line):
        self.factory.onUserMessage(self, line)

class ChatFactory(Factory):
    def __init__(self):
        self.users = Set()

    def buildProtocol(self, addr):
        return ClientProtocol(self)

    def onUserMessage(self, protocol, msg):
        self.broadcast("%s: %s" % (protocol.name, msg))

    def broadcast(self, msg):
        for user in self.users:
            user.sendLine(msg)        

    def join(self, protocol):
        for user in self.users:
            protocol.sendLine("%s joined the room." % user.name);
        self.users.add(protocol)
        for user in self.users:
            user.sendLine("%s joined the room." % protocol.name)

    def leave(self, protocol):
        if protocol in self.users:
            self.users.remove(protocol)
            self.broadcast("%s left the room." % protocol.name)
            

reactor.listenTCP(8000, ChatFactory())
reactor.run()
