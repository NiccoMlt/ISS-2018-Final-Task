#include "SerialTask.h"

SerialTask::SerialTask(float *gloDistanceValue, bool *gloBlinkingState, void (*move)(int)) {
  this->gloDistanceValue = gloDistanceValue;
  this->gloBlinkingState = gloBlinkingState;
  this->move = move;
}

void SerialTask::init(int period) {
  Task::init(period);
  c = 0;
  Serial.begin(9600);
  while (!Serial) {}
}

/*
   -----------------------------------
   Interpreter
   -----------------------------------
*/
void SerialTask::remoteCmdExecutor() {
  if ((Serial.available()) > (0)) {
    input = Serial.read();
    Serial.println(input);
    switch (input) {
      case 'w' :
        move(1);
        break;
      case 's' :
        move(2);
        break;
      case 'a' :
        move(3);
        break;
      case 'd' :
        move(4);
        break;
      case 'b' :
        *gloBlinkingState = true;
        break;
      case 'n' :
        *gloBlinkingState = false;
        break;
      case 'h' :
        // fall-through: move(5)
      default  :
        move(5);
    }
  }
}

void SerialTask::tick() {
  remoteCmdExecutor();

  if (c++ == 3) {
    c = 0;
    Serial.println(*gloDistanceValue); // TODO
    Serial.println(*gloBlinkingState); // TODO
  }
}
