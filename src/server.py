import asyncore, asynchat
import os, socket, string

class HTTPChannel(asynchat.async_chat):

    def __init__(self, server, sock, addr):
        asynchat.async_chat.__init__(self, sock)
        self.set_terminator("\n")
        self.data = ""

    def collect_incoming_data(self, data):
        self.data = self.data + data

    def found_terminator(self):
        print self.data.strip()
        self.data = ""

class HTTPServer(asyncore.dispatcher):

    def __init__(self, port):
        asyncore.dispatcher.__init__(self)
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.bind(("", port))
        self.listen(5)

    def handle_accept(self):
        conn, addr = self.accept()
        HTTPChannel(self, conn, addr)

PORT = 8000
s = HTTPServer(PORT)
print "God save the Awls!"
print "Server started on port", PORT, "..."
asyncore.loop()
