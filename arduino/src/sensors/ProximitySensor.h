#ifndef __PROXIMITY_SENSOR__
#define __PROXIMITY_SENSOR__

class ProximitySensor {
public:
  ProximitySensor(int trigPin, int echoPin);
  virtual float getDistance();
};

#endif
