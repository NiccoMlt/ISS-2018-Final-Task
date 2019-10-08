import { Component } from '@angular/core';
import { WorldState } from '../store/robot.state';
import { Store, select } from '@ngrx/store';
import * as fromRoot from '../store/reducers';

@Component({
  selector: 'app-world',
  templateUrl: './world.component.html',
  styleUrls: ['./world.component.scss']
})
export class WorldComponent {

  world: WorldState;

  constructor(private store: Store<fromRoot.AppState>) {
    store.pipe(select(fromRoot.selectSystemWorld)).subscribe(newWorld => this.world = newWorld);
  }
}
