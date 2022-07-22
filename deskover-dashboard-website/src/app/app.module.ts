import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from '@/app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DataTablesModule} from 'angular-datatables';
import {StoreModule} from '@ngrx/store';
import {TooltipModule} from "ngx-bootstrap/tooltip";
import {BsDatepickerModule} from "ngx-bootstrap/datepicker";
import {TimepickerModule} from "ngx-bootstrap/timepicker";
import {CKEditorModule} from "@ckeditor/ckeditor5-angular";
import {ModalModule} from "ngx-bootstrap/modal";
import {NgxMaskModule} from "ngx-mask";

import {AppComponent} from './app.component';
import {ButtonComponent} from '@components/shared/button/button.component';
import {MenuItemComponent} from '@components/shared/menu-item/menu-item.component';
import {DropdownComponent} from '@components/shared/dropdown/dropdown.component';
import {DropdownMenuComponent} from '@components/shared/dropdown/dropdown-menu/dropdown-menu.component';
import {SelectComponent} from '@components/shared/select/select.component';
import {CheckboxComponent} from '@components/shared/checkbox/checkbox.component';
import {MainComponent} from "@components/home/main.component";
import {HeaderComponent} from "@components/layouts/header/header.component";
import {MenuSidebarComponent} from "@components/layouts/menu-sidebar/menu-sidebar.component";
import {LoginComponent} from "@components/pages/login/login.component";
import {FooterComponent} from "@components/layouts/footer/footer.component";
import {PrivacyPolicyComponent} from "@components/pages/privacy-policy/privacy-policy.component";
import {SubcategoryComponent} from "@components/home/manage/category/subcategory/subcategory.component";
import {LanguageComponent} from "@components/layouts/header/language/language.component";
import {ProductComponent} from "@components/home/manage/product/product.component";
import {ProfileComponent} from "@components/home/profile/profile.component";
import {CategoryComponent} from "@components/home/manage/category/category.component";
import {UserInfoComponent} from "@components/layouts/header/user-info/user-info.component";
import {ControlSidebarComponent} from "@components/layouts/control-sidebar/control-sidebar.component";
import {RecoverPasswordComponent} from "@components/pages/recover-password/recover-password.component";
import {ForgotPasswordComponent} from "@components/pages/forgot-password/forgot-password.component";
import {BrandComponent} from "@components/home/manage/brand/brand.component";
import {PromotionComponent} from "@components/home/manage/promotion/promotion.component";
import {NotificationsComponent} from "@components/layouts/header/notifications/notifications.component";
import {MessagesComponent} from "@components/layouts/header/messages/messages.component";
import {DashboardComponent} from "@components/home/dashboard/dashboard.component";
import {CKEditorComponent} from "@components/shared/ckeditor/ckeditor.component";

import {AuthInterceptor} from "@/interceptors/auth-interceptor";

import {authReducer} from './store/auth/reducer';
import {uiReducer} from './store/ui/reducer';
import {registerLocaleData} from '@angular/common';
import {defineLocale} from 'ngx-bootstrap/chronos';
import localeEn from '@angular/common/locales/en';
import {viLocale} from 'ngx-bootstrap/locale';
import {TabsModule} from "ngx-bootstrap/tabs";
import {UserComponent} from "@components/home/manage/user/user.component";

registerLocaleData(localeEn, 'vi-VN');
defineLocale('vi', viLocale);

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    CKEditorComponent,
    LoginComponent,
    HeaderComponent,
    FooterComponent,
    MenuSidebarComponent,
    ProfileComponent,
    DashboardComponent,
    MessagesComponent,
    NotificationsComponent,
    ButtonComponent,
    UserInfoComponent,
    ForgotPasswordComponent,
    RecoverPasswordComponent,
    LanguageComponent,
    PrivacyPolicyComponent,
    MenuItemComponent,
    DropdownComponent,
    DropdownMenuComponent,
    ControlSidebarComponent,
    SelectComponent,
    CheckboxComponent,
    CategoryComponent,
    BrandComponent,
    SubcategoryComponent,
    PromotionComponent,
    ProductComponent,
    UserComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    StoreModule.forRoot({auth: authReducer, ui: uiReducer}),
    HttpClientModule,
    AppRoutingModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    DataTablesModule,
    CKEditorModule,
    TooltipModule,
    BsDatepickerModule,
    TimepickerModule,
    ModalModule,
    NgxMaskModule.forRoot(),
    TabsModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
