import { Component } from '@angular/core';
import { TemperatureService } from '../temperature.service';

@Component({
  selector: 'app-temperature',
  templateUrl: './temperature.component.html',
  styleUrls: ['./temperature.component.scss']
})
export class TemperatureComponent {

  temperature: number;

  constructor(private temperatureService: TemperatureService) { }

  updateTemperature() {
    this.temperatureService.updateTemperature(this.temperature);
  }

}
