#include "SerialTask.h"

SerialTask::SerialTask(float* gloDistanceValue, bool* gloBlinkingState, void (*move)(int)) {
  this->gloDistanceValue = gloDistanceValue;
  this->gloBlinkingState = gloBlinkingState;
  this->move = move;
}

void SerialTask::init(int period) {
  Task::init(period);
  c = 0;
  Serial.begin(9600);
  while (!Serial){}
}

/*
   -----------------------------------
   Interpreter
   -----------------------------------
*/
void SerialTask::remoteCmdExecutor()
{
  if ((Serial.available()) > (0  )) {
    input = Serial.read();
    //Serial.println(input);
    switch ( input ) {
      case 119 : move(1); break;  //w
      case 115 : move(2); break;  //s
      case 97  : move(3); break;  //a
      case 100 : move(4); break;  //d
      case 104 : move(5); break;  //h
      case 98 : *gloBlinkingState=true; break;   //b
      case 110 : *gloBlinkingState=false; break; //n
      default  : move(5);
    }
  }
}

void SerialTask::tick() {
	remoteCmdExecutor();
	
  if (c++ == 3) {
    c = 0;
    Serial.println(*gloDistanceValue);
    // Serial.println(*gloBlinkingState);
  }
}
