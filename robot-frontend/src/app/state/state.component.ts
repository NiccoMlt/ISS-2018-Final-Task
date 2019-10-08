import { Component } from '@angular/core';
import { Action } from '../store/robot.state';
import { Store, select } from '@ngrx/store';
import * as fromRoot from '../store/reducers';
import { RemoterobotService } from '../remoterobot.service';

@Component({
  selector: 'app-state',
  templateUrl: './state.component.html',
  styleUrls: ['./state.component.scss']
})
export class StateComponent {

  state: {
    name: string;
    actions: Action[];
  };

  constructor(private store: Store<fromRoot.AppState>, private remoterobotService: RemoterobotService) {
    store.pipe(select(fromRoot.selectSystemState)).subscribe(newState => this.state = newState);
  }

  getColor(action: Action): string {
    switch (action.type) {
      case 'safe':
        return 'primary';
      case 'warning':
        return 'warn';
      default:
        return '';
    }
  }

  execute(action: Action): void {
    console.log('sending: ' + action.cmd);
    this.remoterobotService.messages.next(action.cmd);
  }

}
