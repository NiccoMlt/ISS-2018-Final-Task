#include <Arduino.h>
#include <SoftwareSerial.h>

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

Scheduler *sched;
// Drive motor(SPEED1, DIRECTION1, SPEED2, DIRECTION2);

float gloDistanceValue = 5.0;
bool gloBlinkingState = false;

// DistanceTask* distanceTask = new DistanceTask(PROXIMITY_TRIG_PIN, PROXIMITY_ECHO_PIN, gloDistanceValue);
BlinkTask *blinkingLed;
SerialTask *serialTask;

typedef enum {
  FORWARD  = LOW,
  BACKWARD = HIGH
} Direction;

void setup() {
  sched = new Scheduler();
  sched->init(50);

  // distanceTask->init(100);

  pinMode(DIRECTION1, OUTPUT);
  pinMode(DIRECTION2, OUTPUT);

  blinkingLed = new BlinkTask(LED_PIN, &gloBlinkingState);
  blinkingLed->init(200);

  serialTask = new SerialTask(&gloDistanceValue, &gloBlinkingState, move);
  serialTask->init(100);

  // sched->addTask(distanceTask);
  sched->addTask(blinkingLed);
  sched->addTask(serialTask);
}

void loop() {
  sched->schedule();
}

void move(int direction) {
  Serial.print("Moving to direction: ");
  Serial.print(direction);
  Serial.print(" which means: ");
  switch (direction) {
    case 1:
      Serial.println("Forward (both motors forward)");
      break;
    case 2:
      Serial.println("Backward (both motors backward)");
      break;
    case 3:
      Serial.println("Left (motor 1 backward, motor 2 forward)");
      break;
    case 4:
      Serial.println("Right (motor 1 forward, motor 2 backward)");
      break;
    case 5:
      Serial.println("halt");
    default:
      break;
  }

  switch (direction) {
    case 1://forward
      digitalWrite(DIRECTION1, LOW);
      digitalWrite(DIRECTION2, LOW);
      analogWrite(SPEED1, 200);   // PWM regulate speed
      analogWrite(SPEED2, 150);   // PWM regulate speed
      break;
    case 2: //backward
      digitalWrite(DIRECTION1, HIGH);
      digitalWrite(DIRECTION2, HIGH);
      analogWrite(SPEED1, 145);   // PWM regulate speed
      analogWrite(SPEED2, 200);   // PWM regulate speed
      break;
    case 3: //left
      digitalWrite(DIRECTION1, HIGH);
      digitalWrite(DIRECTION2, LOW);
      analogWrite(SPEED1, 100);   // PWM regulate speed
      analogWrite(SPEED2, 100);   // PWM regulate speed
      break;
    case 4: //right
      digitalWrite(DIRECTION1, LOW);
      digitalWrite(DIRECTION2, HIGH);
      analogWrite(SPEED1, 100);   // PWM regulate speed
      analogWrite(SPEED2, 100);   // PWM regulate speed
      break;
    case 5: //halt
      digitalWrite(DIRECTION1, LOW);
      digitalWrite(DIRECTION2, LOW);
      analogWrite(SPEED1, 0);   // PWM regulate speed
      analogWrite(SPEED2, 0);   // PWM regulate speed
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
