#ifndef __BLINKINGLED__
#define __BLINKINGLED__

#include "Led.h"

class BlinkingLed {
public:
  BlinkingLed(Led* led);
  void switchOn();
  void switchOff();
  void blink();
private:
  Led* led;
  bool isOn;
};

#endif
