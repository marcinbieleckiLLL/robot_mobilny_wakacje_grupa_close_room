# -*- coding: utf-8 -*-


import socket
import threading
import RPi.GPIO as GPIO
import time
from rrb2 import*

# IP adresy
MY_IP_ADDRESS = '192.168.1.155'
DEVICE_IP_ADDRESS = ''

# porty na wysylanie i odbieranie
TO_ROBOT_PORT = 5013
TO_ROBOT_BUFFER_SIZE = 1024

FROM_ROBOT_PORT_DISTANCE = 5006
FROM_ROBOT_BUFFER_SIZE = 1024

# ustawienia GPIO
GPIO.setmode(GPIO.BCM) # używamy numerów GPIO, a nie numerów pinów liczonych od lewej do prawej
GPIO.setwarnings(False) # konsola nie wypluwa niepotrzebnych ostrzeżeń np. o tym, że dane GPIO już zostało zainicjalizowane

TRIG = 26
ECHO = 21
ENCODER_LEFT = 19
ENCODER_RIGHT = 20

GPIO.setup(TRIG, GPIO.OUT)
GPIO.setup(ECHO, GPIO.IN)
GPIO.setup(ENCODER_LEFT, GPIO.IN)
GPIO.setup(ENCODER_RIGHT, GPIO.IN)

# tworzenie obiektu rr - biblioteka z metodami do robota

rr = RRB2()

# globalna informacja o predkosci kol i dystansie
SPEED_LEFT = 0
SPEED_RIGHT = 0
DISTANCE = ""
DISTANCE_LIMIT = 15
EMERGENCY_STOP = False
#
def Encoders():
    global SPEED_LEFT
    global SPEED_RIGHT

    counterLeft = 0
    previousInputLeft = 0

    counterRight = 0
    previousInputRight = 0

    start = time.time()
    while True:

        # przeczytaj
        inputLeft = GPIO.input(ENCODER_LEFT)
        inputRight = GPIO.input(ENCODER_RIGHT)
        # jeśli ostatnie czytanie było odwrotne - policz zmianę
        if ((not previousInputLeft) and inputLeft):
            counterLeft = counterLeft + 1
        if ((not previousInputRight) and inputRight):
            counterRight = counterRight + 1
        # odśwież poprzedni stan
        previousInputLeft = inputLeft
        previousInputRight = inputRight
        end = time.time()

        if (end - start) > 0.4:
            SPEED_LEFT = 19.46 * counterLeft / (32*0.4)
            SPEED_RIGHT = 19.46 * counterRight / (32*0.4)
            # print("Left: ", speedLeft)
            # print("Right: ", speedRight)
            counterLeft = 0
            counterRight = 0
            start = time.time()
        # debounce time
        time.sleep(0.002)


def PI(steeringOrders):
    global SPEED_LEFT
    global SPEED_RIGHT
    global EMERGENCY_STOP

    #rozpakowanie instrukcji

    steeringData= steeringOrders.split(",")

    # A - android C - computer
    androidORcomputer = steeringData[0]

    directionLeft = int(steeringData[1])
    expectedSpeedLeft = float(steeringData[2])
    directionRight = int(steeringData[3])
    expectedSpeedRight = float(steeringData[4])

    if androidORcomputer == "A":
        steeringTime = 1000
    else:
        steeringTime = float(steeringData[5])


    #-----------------------

    dt = 0.5
    Kp = 0.025
    Ki = 0.0015
    yiLeft = 0.0
    yiRight = 0.0

    previousSpeedLeft = 0
    previousErrorLeft = 0.0

    previousSpeedRight = 0
    previousErrorRight = 0.0

    startTime = time.time()
    while (time.time() - startTime) < steeringTime:
        if(not EMERGENCY_STOP):
            errorLeft = expectedSpeedLeft - SPEED_LEFT
            errorRight = expectedSpeedRight - SPEED_RIGHT

            yiLeft = yiLeft + ((errorLeft + previousErrorLeft) / 2) * dt
            yiRight = yiRight + ((errorRight + previousErrorRight) / 2) * dt

            Uleft = Kp * errorLeft + Ki * yiLeft
            Uright = Kp * errorRight + Ki * yiRight

            leftPI = previousSpeedLeft + Uleft
            rightPI = previousSpeedRight + Uright

            if leftPI > 1:
                leftPI = 1
            if leftPI < 0:
                leftPI = 0

            if rightPI > 1:
                rightPI = 1
            if rightPI < 0:
                rightPI = 0

            rr.set_motors(leftPI, directionLeft, rightPI, directionRight)
            print("L: ", SPEED_LEFT)
            print("R: ", SPEED_RIGHT)
            previousSpeedLeft = previousSpeedLeft + Uleft
            previousErrorLeft = errorLeft

            previousSpeedRight = previousSpeedRight + Uright
            previousErrorRight = errorRight
        time.sleep(dt)

    rr.stop()

def SendDistance():
    global DISTANCE
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((DEVICE_IP_ADDRESS, FROM_ROBOT_PORT_DISTANCE))
        DISTANCE = DISTANCE.encode()
        s.send(DISTANCE)
        data = s.recv(FROM_ROBOT_BUFFER_SIZE)
        #print("received data:", data)
    except:
        print("no connection")
    finally:
        s.close()


def CountDistance():
    global DISTANCE
    global EMERGENCY_STOP
    while(True):
        GPIO.output(TRIG, False)
        time.sleep(0.4)
        GPIO.output(TRIG, True)
        time.sleep(0.00001)
        GPIO.output(TRIG, False)

        while GPIO.input(ECHO) == 0:
            pulse_start = time.time()

        while GPIO.input(ECHO) == 1:
            pulse_end = time.time()

        pulse_duration = pulse_end - pulse_start
        DISTANCE = pulse_duration * 17150
        DISTANCE = str(round(DISTANCE, 2))

        if(float(DISTANCE) < DISTANCE_LIMIT):
            EMERGENCY_STOP = True
            time.sleep(0.01)
            rr.stop()


        




def MakeOrder(order, addr):
    order = order.decode()
    if order == "distance":
        threading.Thread(target=SendDistance).start()
    elif order[0] == "C" or order[0] == "A":
        threading.Thread(target=PI, args=(order)).start()
    elif order == "connect":
        global DEVICE_IP_ADDRESS
        DEVICE_IP_ADDRESS = addr[0]






def GetOrder():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((MY_IP_ADDRESS, TO_ROBOT_PORT))
    s.listen(1)
    while (True):
        conn, addr = s.accept()
        # print('Connection address:', addr)
        while 1:
            data = conn.recv(TO_ROBOT_BUFFER_SIZE)
            if not data: break
            MakeOrder(data, addr)
            # print("received data:", data)
            conn.send(data)  # echo
        conn.close()



eventPI = threading.Event()
eventEncoders = threading.Event()

threading.Thread(target=CountDistance).start()
threading.Thread(target=PI).start()
threading.Thread(target= GetOrder).start()
threading.Thread(target= Encoders).start()


