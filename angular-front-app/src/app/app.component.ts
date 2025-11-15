import { Component, OnInit, inject, signal, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

// --- Interfaces pour typer nos données ---

/**
 * Modèle pour le Keynote (doit correspondre au KeynoteDTO)
 */
interface Keynote {
    id: number;
    nom: string;
    prenom: string;
    email: string;
    fonction: string;
}

/**
 * Modèle pour la Conférence (doit correspondre au ConferenceDTO)
 */
interface Conference {
    id: number;
    titre: string;
    type: 'ACADEMIQUE' | 'COMMERCIALE';
    date: string; // Date vient en ISO string
    duree: number;
    nombreInscrits: number;
    score: number;
    keynoteId: number;
    keynote: Keynote; // L'objet Keynote complet !
    reviews: any[]; // On ne va pas afficher les reviews pour rester simple
}

// --- Le Composant Angular ---

@Component({
    selector: 'app-root', // La balise <app-root> dans index.html
    standalone: true,    // Composant autonome (nouvelle approche Angular)
    imports: [
        CommonModule,       // Pour les *ngIf, *ngFor
        HttpClientModule    // CORRECTION: Ajouté pour "fournir" HttpClient
    ],
    providers: [DatePipe], // CORRECTION: Décommenté pour "fournir" DatePipe
    changeDetection: ChangeDetectionStrategy.OnPush, // Mode de détection optimisé

    // --- TEMPLATE HTML (avec classes TailwindCSS) ---
    template: `
    <div class="bg-gray-900 min-h-screen text-white p-8 font-sans">
      <header class="mb-12">
        <h1 class="text-5xl font-bold text-center text-blue-400">
          Gestion des Conférences
        </h1>
        <p class="text-center text-lg text-gray-400 mt-2">
          Dashboard de l'application Micro-services
        </p>
      </header>

      <!-- Section Erreurs -->
      @if (errorMessage()) {
        <div class="bg-red-800 border border-red-600 text-white p-4 rounded-lg mb-8">
          <h3 class="font-bold text-xl mb-2">Erreur de Connexion</h3>
          <p>Impossible de contacter la passerelle (Gateway) sur <code class="bg-red-900 px-1 rounded">{{ gatewayUrl() }}</code>.</p>
          <p class="mt-2">Veuillez vérifier que vos 5 services (Discovery, Config, Gateway, Keynote, Conference) sont bien démarrés.</p>
        </div>
      } @else {
        <!-- Bouton de rafraîchissement -->
        <div class="text-center mb-8">
          <button (click)="refreshAll()" 
                  class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded-lg transition duration-200 shadow-lg">
            Rafraîchir les données
          </button>
        </div>
      }

      <!-- Contenu Principal (2 colonnes) -->
      <main class="grid grid-cols-1 lg:grid-cols-2 gap-12">
        
        <!-- Colonne 1: Keynotes -->
        <section>
          <h2 class="text-3xl font-semibold mb-6 text-blue-300">
            <span class="mr-2">🎤</span> Keynotes
          </h2>
          <div class="flex flex-col gap-4">
            @for (keynote of keynotes(); track keynote.id) {
              <div class="bg-gray-800 p-5 rounded-lg shadow-lg border border-gray-700">
                <h3 class="text-xl font-bold text-blue-400">{{ keynote.prenom }} {{ keynote.nom }}</h3>
                <p class="text-gray-400">{{ keynote.fonction }}</p>
                <p class="text-blue-300 mt-2 text-sm">{{ keynote.email }}</p>
              </div>
            } @empty {
              <div class="bg-gray-800 p-5 rounded-lg border border-gray-700">
                <p class="text-gray-400">Aucun keynote trouvé. (Port 8081)</p>
              </div>
            }
          </div>
        </section>

        <!-- Colonne 2: Conférences -->
        <section>
          <h2 class="text-3xl font-semibold mb-6 text-green-300">
            <span class="mr-2">📅</span> Conférences
          </h2>
          <div class="flex flex-col gap-4">
            @for (conf of conferences(); track conf.id) {
              <div class="bg-gray-800 p-5 rounded-lg shadow-lg border border-gray-700">
                
                <!-- Titre et Date -->
                <div class="flex justify-between items-start mb-2">
                  <h3 class="text-xl font-bold text-green-400">{{ conf.titre }}</h3>
                  <span class="text-sm font-medium bg-green-800 text-green-200 px-3 py-1 rounded-full">
                    {{ datePipe.transform(conf.date, 'dd MMM yyyy') }}
                  </span>
                </div>

                <!-- Infos -->
                <div class="flex gap-4 text-gray-400 text-sm mb-4">
                  <span>Durée: <strong>{{ conf.duree }}h</strong></span>
                  <span>Inscrits: <strong>{{ conf.nombreInscrits }}</strong></span>
                  <span>Score: <strong class="text-yellow-400">{{ conf.score.toFixed(1) }} ★</strong></span>
                </div>

                <!-- Infos du Keynote (via OpenFeign) -->
                <div class="bg-gray-700 p-3 rounded-md border border-gray-600">
                  <p class="text-sm text-gray-300">Présenté par :</p>
                  <h4 class="font-semibold text-blue-300">{{ conf.keynote.prenom }} {{ conf.keynote.nom }}</h4>
                  <p class="text-xs text-gray-400">{{ conf.keynote.fonction }}</p>
                </div>

              </div>
            } @empty {
              <div class="bg-gray-800 p-5 rounded-lg border border-gray-700">
                <p class="text-gray-400">Aucune conférence trouvée. (Port 8082)</p>
              </div>
            }
          </div>
        </section>
      </main>
    </div>
  `,

    // --- STYLES CSS (minimal, car on utilise Tailwind) ---
    styles: [`
    /* On utilise la police Inter, qui est similaire à celle de Tailwind */
    :host {
      font-family: 'Inter', sans-serif;
    }
  `]
})
// CORRECTION: Ajout du mot-clé "export" pour que le fichier main.ts puisse trouver la classe.
export class App implements OnInit {
    // --- Dépendances ---
    http = inject(HttpClient);
    datePipe = inject(DatePipe);

    // --- URLs de l'API (on appelle TOUJOURS la passerelle) ---
    gatewayUrl = signal('http://localhost:9999');

    // --- États (Signals Angular) ---
    keynotes = signal<Keynote[]>([]);
    conferences = signal<Conference[]>([]);
    errorMessage = signal<string | null>(null);

    // --- Au démarrage du composant ---
    ngOnInit(): void {
        this.loadAllData();
    }

    /**
     * Appelle les deux méthodes de chargement.
     */
    loadAllData(): void {
        this.errorMessage.set(null); // Reset l'erreur
        this.loadKeynotes();
        this.loadConferences();
    }

    /**
     * Fonction pour le bouton de rafraîchissement.
     */
    refreshAll(): void {
        this.keynotes.set([]); // Vide les listes pour montrer le chargement
        this.conferences.set([]);
        this.loadAllData();
    }

    /**
     * Charge la liste des keynotes depuis la passerelle.
     */
    loadKeynotes(): void {
        const url = `${this.gatewayUrl()}/api/v1/keynotes`;
        this.http.get<Keynote[]>(url).subscribe({
            next: (data) => {
                this.keynotes.set(data);
                console.log('Keynotes chargés:', data);
            },
            error: (err) => {
                console.error('Erreur chargement keynotes:', err);
                this.errorMessage.set(err.message);
            }
        });
    }

    /**
     * Charge la liste des conférences depuis la passerelle.
     * (Celle-ci va déclencher l'appel OpenFeign dans le backend !)
     */
    loadConferences(): void {
        const url = `${this.gatewayUrl()}/api/v1/conferences`;
        this.http.get<Conference[]>(url).subscribe({
            next: (data) => {
                this.conferences.set(data);
                console.log('Conférences chargées:', data);
            },
            error: (err) => {
                console.error('Erreur chargement conférences:', err);
                this.errorMessage.set(err.message);
            }
        });
    }
}