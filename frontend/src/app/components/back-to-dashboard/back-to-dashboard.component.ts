import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-back-to-dashboard',
  templateUrl: './back-to-dashboard.component.html',
  styleUrls: ['./back-to-dashboard.component.scss']
})
export class BackToDashboardComponent {
  @Input() variant: 'default' | 'compact' | 'floating' = 'default';
  @Input() customText: string = 'Volver al Dashboard';
  @Input() customIcon: string = 'arrow_back';

  get buttonClass(): string {
    return `back-button ${this.variant}`;
  }
}

