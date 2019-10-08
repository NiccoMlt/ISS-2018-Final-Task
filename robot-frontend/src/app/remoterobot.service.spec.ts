import { TestBed } from '@angular/core/testing';

import { RemoterobotService } from './remoterobot.service';

describe('RemoterobotService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RemoterobotService = TestBed.get(RemoterobotService);
    expect(service).toBeTruthy();
  });
});
