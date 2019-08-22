#ifndef __DISTANCETASK__
#define __DISTANCETASK__

#include "../Task.h"
#include "../../sensors/ProximitySensor.h"

class DistanceTask: public Task {

public:
  DistanceTask(int trigPin, int echoPin, float* gloDistanceValue);
  void init(int period) override;
  void tick() override;

private:
  int trigPin, echoPin;
  float* gloDistanceValue;
  ProximitySensor* proximitySensor;

};

#endif
