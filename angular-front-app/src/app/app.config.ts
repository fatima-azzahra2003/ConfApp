import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { DatePipe } from '@angular/common';

/**
 * Configuration centrale de l'application.
 * C'est ici qu'on déclare les "services" globaux.
 */
export const appConfig: ApplicationConfig = {
    providers: [
        // Fournit le service HttpClient pour faire des appels API
        importProvidersFrom(HttpClientModule),

        // Fournit le DatePipe pour formater les dates
        DatePipe
    ]
};