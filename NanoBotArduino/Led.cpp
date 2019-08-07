#include "Led.h"
#include "Arduino.h"

Led::Led(int pin){
  this->pin = pin;
  pinMode(pin,OUTPUT);
}

void Led::switchOn(){
  digitalWrite(pin,HIGH);
}

void Led::switchOff(){
  digitalWrite(pin,LOW);
};

void Led::blink(int millis){
  digitalWrite(pin,HIGH);
  delay(millis);
  digitalWrite(pin,LOW);
};

void Led::blink(){
  this->blink(500);
};
