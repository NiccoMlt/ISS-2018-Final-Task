#ifndef __STABILIZED_PROXIMITY_SENSOR__
#define __STABILIZED_PROXIMITY_SENSOR__

#include "ProximitySensor.h"

class StabilizedProximitySensor: public ProximitySensor{
public:
  StabilizedProximitySensor(int trigPin, int echoPin, float weightNewValue);
  float getDistance();
private:
  float lastDistance;
  float weightNewValue;
};

#endif
