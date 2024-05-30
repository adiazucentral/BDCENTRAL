import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  private load = new BehaviorSubject<boolean>(true);
  load$ = this.load.asObservable();

  constructor() { }

  loading(data: boolean) {
    this.load.next(data);
  }
}
