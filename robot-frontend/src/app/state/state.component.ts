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
    message: string;
    actions: Action[];
  };

  constructor(private store: Store<fromRoot.AppState>, private remoterobotService: RemoterobotService) {
    store.pipe(select(fromRoot.selectSystemState)).subscribe(newState => this.state = newState);
  }

  messageIsPicture(): boolean {
    return this.state.message.startsWith('picture:');
  }

  messageIsText(): boolean {
    return !this.messageIsPicture();
  }

  parseMessageAsPicture(): string {
    if (this.messageIsPicture()) {
      return this.state.message.split('\'')[1];
    }
  }

  getMessageColorClass() {
    return this.state.message.startsWith('WARNING:') ? 'text-warning' :
      this.state.message.startsWith('DANGER:') ? 'text-danger' :
        this.state.message.startsWith('INFO:') ? 'text-info' :
          '';
  }

  getColor(action: Action): string {
    switch (action.type) {
      case 'safe':
        return 'accent';
      case 'warning':
        return 'warn';
      default:
        return 'primary';
    }
  }

  execute(action: Action): void {
    console.log('sending: ' + action.cmd);
    this.remoterobotService.messages.next(action.cmd);
  }

}
