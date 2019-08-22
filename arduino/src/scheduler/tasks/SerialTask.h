#ifndef __SERIALTASK__
#define __SERIALTASK__

#include <Arduino.h>
#include <SoftwareSerial.h>

#include "../Task.h"

class SerialTask: public Task {

public:
  SerialTask(float* gloDistanceValue, bool* gloBlinkingState, void (*move)(int));
  void init(int period) override;
  void tick() override;

private:
	void remoteCmdExecutor();

  float* gloDistanceValue;
  bool* gloBlinkingState;
  void (*move)(int);

	int input;
  unsigned int c;
};

#endif
