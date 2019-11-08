import { Component, Input } from '@angular/core';
import { Position } from '../store/robot.state';

@Component({
  selector: 'app-world-map',
  templateUrl: './world-map.component.html',
  styleUrls: ['./world-map.component.scss']
})
export class WorldMapComponent {

  @Input() map: string[][];

  constructor() { }

}
