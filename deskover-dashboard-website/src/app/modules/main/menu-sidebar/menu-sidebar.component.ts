import {AppState} from '@/store/state';
import {UiState} from '@/store/ui/state';
import {Component, HostBinding, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {AuthService} from "@services/auth.service";
import {environment} from "../../../../environments/environment";

const BASE_CLASSES = 'main-sidebar elevation-4';

@Component({
  selector: 'app-menu-sidebar',
  templateUrl: './menu-sidebar.component.html',
  styleUrls: ['./menu-sidebar.component.scss']
})
export class MenuSidebarComponent implements OnInit {
  @HostBinding('class') classes: string = BASE_CLASSES;
  public ui: Observable<UiState>;
  public user: any;
  public menu = MENU;

  avatarURL: string;

  constructor(
    public authService: AuthService,
    private store: Store<AppState>
  ) {
  }

  ngOnInit() {
    this.ui = this.store.select('ui');
    this.ui.subscribe((state: UiState) => {
      this.classes = `${BASE_CLASSES} ${state.sidebarSkin}`;
    });
    this.user = this.authService.user;
    this.avatarURL = environment.imageURL + '/avatar/' + this.user.user.photo;
  }
}

export const MENU = [
  {
    name: 'Bảng điều khiển',
    path: ['/'],
    icon: 'fas fa-tachometer-alt'
  },
  {
    header: 'QUẢN LÝ',
    name: 'Danh mục',
    path: ['/category'],
    icon: 'fas fa-layer-group'
  },
  {
    name: 'Thương hiệu',
    path: ['/brand'],
    icon: 'fas fa-copyright'
  }
];
