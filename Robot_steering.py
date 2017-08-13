#!/usr/bin/env python

import socket
import time
from PyQt5.uic import loadUi
from PyQt5.QtWidgets import *
import threading
import sys



MY_IP_ADDRESS = '192.168.1.189'
ROBOT_IP_ADDRESS = ""

TO_ROBOT_PORT = 5000
BUFFER_SIZE_STEERING = 1024

FROM_ROBOT_PORT = 5006
BUFFER_SIZE_DISTANCE = 1024


class KlasaOkienkowa(QMainWindow):
    def __init__(self):
        print("hej")
        self.CAMERA = False
        # odpalenie okna
        QMainWindow.__init__(self)
        self.ui = loadUi("./startWindow.ui", self)
        self.scene = QGraphicsScene(self)
        self.confirmButton.clicked.connect(self.confirmButton_onClick)

        # wyswietlenie okna
        self.show()

    def confirmButton_onClick(self):
        global ROBOT_IP_ADDRESS
        ROBOT_IP_ADDRESS = "192.168." + self.IP_3_textArea.toPlainText() + "." + self.IP_4_textArea.toPlainText()
        print(ROBOT_IP_ADDRESS)
        threading.Thread(target=Steering).start()
        threading.Thread(target=GetDistance).start()
    def cameraButton_onClick(self):
        if(self.CAMERA == False):
            SendOrder("camera_on")
            self.CAMERA = True
        else:
            SendOrder("camera_off")
            self.CAMERA = False


def SendOrder(MESSAGE):

        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(10)
            s.connect((ROBOT_IP_ADDRESS, TO_ROBOT_PORT))
            #s.settimeout(1)
            MESSAGE = MESSAGE.encode()
            s.send(MESSAGE)
            data = s.recv(BUFFER_SIZE_STEERING)
            print("received data:", data)
        except:
            print("no connection")
            print("resending...")
            s.close()
            SendOrder(MESSAGE)
        finally:
            s.close()
            time.sleep(1)

def Steering():
    while(1):
        MESSAGE = input()
        SendOrder(MESSAGE)
    MESSAGE ="distance"
    SendOrder(MESSAGE)

def GetDistance():

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((MY_IP_ADDRESS, FROM_ROBOT_PORT))
    s.listen(1)
    while (True):
        conn, addr = s.accept()
        print('Connection address:', addr)
        while 1:
            data = conn.recv(BUFFER_SIZE_DISTANCE)
            if not data: break
            print("received data:", data)
            conn.send(data)  # echo
        conn.close()





if __name__ == '__main__':
    qApp = QApplication(sys.argv)
    app = KlasaOkienkowa()
    sys.exit(qApp.exec_())