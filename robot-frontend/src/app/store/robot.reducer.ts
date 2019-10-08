import { Action } from '@ngrx/store';
import { ActionTypes, RobotActionsUnion } from './robot.actions';
import { SystemState } from './robot.state';

export const initialState: SystemState = {
  world: {
    temperature: '18',
    map: [
      ['r', '1', 'x'],
      ['1', '1', '0'],
      ['1', '1', 'x'],
    ],
    bombPosition: {
        x: 2,
        y: 2
    },
  },
  robot: {
    position: {
      x: 0,
      y: 0
    }, // ??
    direction: 'w',
    info: 'info varie'
  },
  state: {
    name: 'demo',
    actions: [
      {
        name: 'explore',
        type: 'standard',
        cmd: 'cmd(explore)'
      },
      {
        name: 'home',
        type: 'standard',
        cmd: 'cmd(home)'
      },
      {
        name: 'stop',
        type: 'standard',
        cmd: 'cmd(halt)'
      },
      {
        name: 'bomb',
        type: 'warning',
        cmd: 'bagStatus(bomb)'
      },
      {
        name: 'bag',
        type: 'safe',
        cmd: 'bagStatus(bag)'
      }
    ]
  }
};

export function robotReducer(state: SystemState = initialState, action: RobotActionsUnion): SystemState {
  switch (action.type) {
    case ActionTypes.UpdateState:
      return action.payload;

    default:
      return state;
  }
}
