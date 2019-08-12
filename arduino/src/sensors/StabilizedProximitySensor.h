#ifndef __STABILIZEDPROXIMITYSENSOR__
#define __STABILIZEDPROXIMITYSENSOR__

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
