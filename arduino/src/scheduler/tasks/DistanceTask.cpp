#include "DistanceTask.h"
#include "../../sensors/StabilizedProximitySensor.h"

DistanceTask::DistanceTask(int trigPin, int echoPin, float* gloDistanceValue){
  this->trigPin = trigPin;
  this->echoPin = echoPin;
  this->gloDistanceValue = gloDistanceValue;
}

void DistanceTask::init(int period){
  Task::init(period);
  proximitySensor = new StabilizedProximitySensor(trigPin, echoPin, 0.7);
}

void DistanceTask::tick(){
  *gloDistanceValue = proximitySensor->getDistance();
}
