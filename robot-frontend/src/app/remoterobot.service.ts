import 'rxjs/add/operator/map';
import 'rxjs/add/operator/filter';
import 'rxjs/add/observable/dom/webSocket';

import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { WebsocketService } from './websocket.service';
import * as fromRoot from './store/reducers';
import { Store } from '@ngrx/store';
import { UpdateState } from './store/robot.actions';
import { environment } from '../environments/environment'; // Angular CLI environemnt

const SERVER_URL = environment.backend_ws;

@Injectable({
  providedIn: 'root'
})
export class RemoterobotService {

  public messages: Subject<string>;

  constructor(wsService: WebsocketService, private store: Store<fromRoot.AppState>) {
    this.messages = <Subject<string>>wsService
      .connect(SERVER_URL)
      .map((response: MessageEvent): string => {
        return response.data;
      });

    this.messages.subscribe(encoded => {
      const msg = atob(encoded);
      console.log(msg);
      console.log(JSON.parse(msg));
      this.store.dispatch(new UpdateState(JSON.parse(msg)));
    });
  }
}
