import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment'; // Angular CLI environemnt

const SERVER_URL = environment.backend_http;

@Injectable({
  providedIn: 'root'
})
export class TemperatureService {

  constructor(private http: HttpClient) { }

  updateTemperature(temperature: number): void {
    this.http.post(SERVER_URL + '/environment/temperature', '\'' + temperature + '\'')
      .subscribe(
        data => {},
        err => console.error(err),
        () => console.log('done')
      );
  }
}
