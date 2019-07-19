import { NgModule } from '@angular/core';

import { GatewayServiceSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [GatewayServiceSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [GatewayServiceSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class GatewayServiceSharedCommonModule {}
