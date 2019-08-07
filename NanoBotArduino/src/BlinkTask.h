#ifndef __BLINKTASK__
#define __BLINKTASK__

#include "Task.h"
#include "Led.h"

class BlinkTask: public Task {

public:
  BlinkTask(int pin, bool* gloBlinkState);
  void init(int period);
  void tick();

protected:
  void reset();

private:
  int pin;
  Light* led;
  bool state;
  bool* gloBlinkState;

};

#endif
