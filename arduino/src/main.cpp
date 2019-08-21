#include <Arduino.h>
#include <SoftwareSerial.h>

// #include <Drive.h>
#include "actuators/L298N.h"

#include "scheduler/Scheduler.h"
#include "scheduler/tasks/DistanceTask.h"
#include "scheduler/tasks/BlinkTask.h"
#include "scheduler/tasks/SerialTask.h"

const int PROXIMITY_TRIG_PIN = 5;
const int PROXIMITY_ECHO_PIN = 4;
const int LED_PIN = 10;

const int PWM_SPEED = 100;

const int SPEED1 = 11;
const int DIRECTION1 = 13;
const int SPEED2 = 3;
const int DIRECTION2 = 12;

void move(int direction);

L298N motor1(SPEED1, DIRECTION1);
L298N motor2(SPEED2, DIRECTION2);

Scheduler sched;
// Drive motor(SPEED1, DIRECTION1, SPEED2, DIRECTION2);

float* gloDistanceValue = new float(5.0);
bool* gloBlinkingState = new bool(false);

DistanceTask* distanceTask = new DistanceTask(PROXIMITY_TRIG_PIN, PROXIMITY_ECHO_PIN, gloDistanceValue);
BlinkTask* blinkingLed = new BlinkTask(LED_PIN, gloBlinkingState);
SerialTask* serialTask = new SerialTask(gloDistanceValue, gloBlinkingState, move);

void setup() {
  sched.init(50);

  distanceTask->init(100);
  blinkingLed->init(200);
  serialTask->init(50);

  sched.addTask(distanceTask);
  sched.addTask(blinkingLed);
  sched.addTask(serialTask);
}

void loop() {
  sched.schedule();
}

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
  /*
  switch (direction) {
    case 1: // forward
      motor.moveForward(PWM_SPEED);
      break;
    case 2: // backward
      motor.moveBackward(PWM_SPEED);
      break;
    case 3: // left
      motor.turnLeft(PWM_SPEED);
      break;
    case 4: // right
      motor.turnRight(PWM_SPEED);
      break;
    case 5: // halt
      motor.stopMoving();
      break;
  }
  */
}
