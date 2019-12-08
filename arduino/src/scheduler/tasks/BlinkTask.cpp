#include "BlinkTask.h"

BlinkTask::BlinkTask(int pin, bool *gloBlinkState) {
  this->pin = pin;
  this->gloBlinkState = gloBlinkState;
}

void BlinkTask::init(int period) {
  Task::init(period);
  led = new Led(pin);
  state = false;
}

void BlinkTask::tick() {
  if (*gloBlinkState && !state) {
    led->switchOn();
    state = true;
  } else {
    if (state) {
      led->switchOff();
      state = false;
    }
  }
}

void BlinkTask::reset() {
  led->switchOff();
  state = false;
}
