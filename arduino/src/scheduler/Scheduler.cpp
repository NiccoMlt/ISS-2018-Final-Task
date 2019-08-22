#include "Scheduler.h"

void Scheduler::init(int period) {
  nTasks = 0;
  basePeriod = period;
  timer.setupPeriod(period);
}

bool Scheduler::addTask(Task *task) {
  if (nTasks < MAX_TASKS - 1) {
    taskList[nTasks] = task;
    nTasks++;
    return true;
  } else {
    return false;
  }
}

void Scheduler::schedule() {
  timer.waitForNextTick();
  for (int i = 0; i < nTasks; i++) {
    if (taskList[i]->updateAndCheckTime(basePeriod)) {
      taskList[i]->tick();
    }
  }
}
