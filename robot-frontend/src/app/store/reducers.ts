import { createSelector, ActionReducerMap } from '@ngrx/store';
import * as fromRobot from './robot.reducer';
import { SystemState } from './robot.state';

export interface AppState {
  robot: SystemState;
}

export const reducers: ActionReducerMap<AppState> = {
  robot: fromRobot.robotReducer,
};

export const selectSystem = (state: AppState) => state.robot;
export const selectSystemWorld = createSelector(
  selectSystem,
  (state: SystemState) => {
    if (state) {
      return state.world;
    }
  }
);
export const selectSystemRobot = createSelector(
  selectSystem,
  (state: SystemState) => {
    if (state) {
      return state.robot;
    }
  }
);
export const selectSystemState = createSelector(
  selectSystem,
  (state: SystemState) => {
    if (state) {
      return state.state;
    }
  }
);
