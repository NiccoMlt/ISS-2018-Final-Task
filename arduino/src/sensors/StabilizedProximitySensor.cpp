#include "StabilizedProximitySensor.h"

StabilizedProximitySensor::StabilizedProximitySensor(int trigPin, int echoPin, float weightNewValue)
  : ProximitySensor(trigPin, echoPin) {
  this->lastDistance = 0;
  this->weightNewValue = weightNewValue;
}

float StabilizedProximitySensor::getDistance() {
  this->lastDistance = this->lastDistance * (1 - weightNewValue) + ProximitySensor::getDistance() * weightNewValue;
  return this->lastDistance;
}
