#include <Arduino.h>
#include <SoftwareSerial.h>
#include "L298N.h"       // Motor H-bridge (L298N)

#include "Scheduler.h"
#include "DistanceTask.h"
#include "BlinkTask.h"
#include "SerialTask.h"


#define PROXIMITY_TRIG_PIN 12
#define PROXIMITY_ECHO_PIN 11
#define LED_PIN    13

/*
   -----------------------------------
   Function declaration
   -----------------------------------
*/
void move(int direction);

/*
   -----------------------------------
   Hardware devices
   -----------------------------------
*/
L298N motor1(7, 6);
L298N motor2(5, 4);

Scheduler sched;

float* gloDistanceValue = new float(5.0);
bool* gloBlinkingState = new bool(false);

DistanceTask* distanceTask = new DistanceTask(PROXIMITY_TRIG_PIN, PROXIMITY_ECHO_PIN, gloDistanceValue);
BlinkTask* blinkingLed = new BlinkTask(LED_PIN, gloBlinkingState);
SerialTask* serialTask = new SerialTask(gloDistanceValue, gloBlinkingState, move);

/*
   -----------------------------------
   setup
   -----------------------------------
*/
void setup() {
  sched.init(50);
  
  distanceTask->init(200);
  blinkingLed->init(200);
  serialTask->init(100);
  
  sched.addTask(distanceTask);
  sched.addTask(blinkingLed);
  sched.addTask(serialTask);
}

void loop() {
  sched.schedule();
}

/*
   -----------------------------------
   Moving
   -----------------------------------
*/
void move(int direction)
{
  switch (direction) {

    case 1://forward
      motor1.forward();
      motor2.forward();
      break;
    case 2: //backward
      motor1.backward();
      motor2.backward();
      break;
    case 3: //left
      motor1.backward();
      motor2.forward();
      break;
    case 4: //right
      motor1.forward();
      motor2.backward();
      break;
    case 5: //halt
      motor1.stop();
      motor2.stop();
      break;
  }
}
