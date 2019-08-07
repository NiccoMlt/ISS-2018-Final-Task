#include "ProximitySensor.h"
#include "Arduino.h"
#include "Ultrasonic.h"

Ultrasonic* ultrasonic;

ProximitySensor::ProximitySensor(int trigPin, int echoPin){
  ultrasonic = new Ultrasonic(trigPin, echoPin);
}

float ProximitySensor::getDistance(){
  return ultrasonic->read();
}
