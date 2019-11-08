import { Action } from '@ngrx/store';
import { SystemState } from './robot.state';

export enum ActionTypes {
  UpdateState = '[Robot] UpdateState',
}

export class UpdateState implements Action {
  readonly type = ActionTypes.UpdateState;

  constructor(public payload: SystemState) { }
}

export type RobotActionsUnion = UpdateState;
