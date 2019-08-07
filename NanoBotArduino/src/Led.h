#ifndef __LED__
#define __LED__

#include "Light.h"

class Led: public Light { 
public:
  Led(int pin);
  void switchOn();
  void switchOff();
  void blink(int millis);
  void blink();
private:
  int pin;  
};

#endif
