#ifndef __PROXIMITYSENSOR__
#define __PROXIMITYSENSOR__

class ProximitySensor {
public:
  ProximitySensor(int trigPin, int echoPin);
  virtual float getDistance();
};

#endif
