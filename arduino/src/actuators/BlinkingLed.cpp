#include "BlinkingLed.h"

BlinkingLed::BlinkingLed(Led *led) {
  this->led = led;
  this->isOn = false;
}

void BlinkingLed::switchOn() {
  this->led->switchOn();
  isOn = true;
}

void BlinkingLed::switchOff() {
  this->led->switchOff();
  isOn = false;
};

void BlinkingLed::blink() {
  if (isOn) {
    switchOff();
  } else {
    switchOn();
  }
};
