import { ApplicationConfig, importProvidersFrom, APP_INITIALIZER } from '@angular/core';
import { HttpClientModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { KeycloakService, KeycloakBearerInterceptor } from 'keycloak-angular';

/**
 * Fonction d'initialisation de Keycloak.
 * Force l'application à attendre que l'utilisateur soit connecté
 * avant de démarrer.
 */
function initializeKeycloak(keycloak: KeycloakService) {
    return () =>
        keycloak.init({
            config: {
                // L'URL de votre serveur Keycloak (port 8180)
                url: 'http://localhost:8180',
                // Le nom de votre Realm
                realm: 'conferences-realm',
                // Le nom de votre client (créé dans l'admin Keycloak)
                clientId: 'angular-client'
            },
            initOptions: {
                // 'login-required' force la page de connexion
                onLoad: 'login-required',
                flow: 'standard'
            },
            // L'intercepteur n'attachera pas le token pour ces URLs
            bearerExcludedUrls: ['/assets']
        });
}

/**
 * Configuration centrale de l'application.
 */
export const appConfig: ApplicationConfig = {
    providers: [
        // Fournit le DatePipe pour formater les dates
        DatePipe,

        // Fournit le KeycloakService
        KeycloakService,

        // Fournit l'initialiseur qui force le login au démarrage
        {
            provide: APP_INITIALIZER,
            useFactory: initializeKeycloak,
            multi: true,
            deps: [KeycloakService]
        },

        // Fournit le HttpClient ET ajoute l'intercepteur Keycloak
        // qui attachera automatiquement le token à nos requêtes
        provideHttpClient(
            withInterceptors([KeycloakBearerInterceptor])
        )

        // Nous n'avons plus besoin de 'importProvidersFrom(HttpClientModule)'
        // car provideHttpClient() le remplace.
    ]
};