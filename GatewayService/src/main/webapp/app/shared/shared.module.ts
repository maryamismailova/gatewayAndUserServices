import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { GatewayServiceSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [GatewayServiceSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [GatewayServiceSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayServiceSharedModule {
  static forRoot() {
    return {
      ngModule: GatewayServiceSharedModule
    };
  }
}
