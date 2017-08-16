import sys
import socket

HOST = "localhost"
PORT = 8080

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
sock.connect((HOST, PORT))

sock.sendall("Hello\n")
data = sock.recv(1024)
print("1)", data)

if (data == "olleH\r\n"):
    sock.sendall("Bye\n")
    data = sock.recv(1024)
    print("2)", data)

    if (data == "eyB}\r\n"):
        sock.close()
        print("Socket closed")