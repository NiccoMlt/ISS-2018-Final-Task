import { Component } from '@angular/core';
import { RobotState } from '../store/robot.state';
import { Store, select } from '@ngrx/store';
import * as fromRoot from '../store/reducers';

@Component({
  selector: 'app-robot',
  templateUrl: './robot.component.html',
  styleUrls: ['./robot.component.scss']
})
export class RobotComponent {

  robot: RobotState;

  constructor(private store: Store<fromRoot.AppState>) {
    store.pipe(select(fromRoot.selectSystemRobot)).subscribe(newRobot => this.robot = newRobot);
  }

}
