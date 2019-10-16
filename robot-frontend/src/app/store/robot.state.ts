export interface SystemState {
  world: WorldState;
  robot: RobotState;
  state: {
    name: string;
    actions: Action[];
  };
}

export interface WorldState {
  temperature: string;
  map: string[][];
}

export interface RobotState {
  position: Position;
  direction: string;
  info: string;
}

export interface Action {
  name: string;
  type: string;
  cmd: string;
}

export interface Position {
  x: number;
  y: number;
}
