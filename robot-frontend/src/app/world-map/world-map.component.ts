import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-world-map',
  templateUrl: './world-map.component.html',
  styleUrls: ['./world-map.component.scss']
})
export class WorldMapComponent {

  @Input() map: string[][];
  @Input() bomb: Position;

  constructor() { }

}
