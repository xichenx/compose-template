export interface ScaffoldHighlight {
  id: string;
  title: string;
  description: string;
  stage: string;
}

export interface ScaffoldSummary {
  highlights: ScaffoldHighlight[];
  lastUpdatedAtMillis: number | null;
  hasCompletedSetupGuide: boolean;
}

export interface LoginSession {
  userId: string;
  displayName: string;
  email: string;
  token: string;
}

export type AppThemeMode = 'follow_system' | 'light' | 'dark';

export type AppLanguage = 'follow_system' | 'en' | 'zh';

export type HomeTab = 'overview' | 'modules' | 'profile';

export type AppRoute = 'splash' | 'auth_entry' | 'login' | 'register' | 'home';
