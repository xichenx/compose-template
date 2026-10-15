import { AppLanguage, AppThemeMode, LoginSession, ScaffoldHighlight } from '../../domain/models';

const STORAGE_KEYS = {
  THEME_MODE: 'compose_template_theme_mode',
  LANGUAGE: 'compose_template_language',
  AUTH_SESSION: 'compose_template_auth_session',
  LAST_UPDATED: 'compose_template_last_updated',
  SETUP_SEEN: 'compose_template_setup_seen',
  CACHED_HIGHLIGHTS: 'compose_template_cached_highlights',
};

export const storage = {
  getThemeMode(): AppThemeMode {
    const val = localStorage.getItem(STORAGE_KEYS.THEME_MODE);
    if (val === 'light' || val === 'dark' || val === 'follow_system') {
      return val;
    }
    return 'follow_system';
  },

  setThemeMode(mode: AppThemeMode) {
    localStorage.setItem(STORAGE_KEYS.THEME_MODE, mode);
  },

  getLanguage(): AppLanguage {
    const val = localStorage.getItem(STORAGE_KEYS.LANGUAGE);
    if (val === 'en' || val === 'zh' || val === 'follow_system') {
      return val;
    }
    return 'follow_system';
  },

  setLanguage(lang: AppLanguage) {
    localStorage.setItem(STORAGE_KEYS.LANGUAGE, lang);
  },

  getAuthSession(): LoginSession | null {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.AUTH_SESSION);
      return data ? JSON.parse(data) : null;
    } catch {
      return null;
    }
  },

  setAuthSession(session: LoginSession | null) {
    if (session) {
      localStorage.setItem(STORAGE_KEYS.AUTH_SESSION, JSON.stringify(session));
    } else {
      localStorage.removeItem(STORAGE_KEYS.AUTH_SESSION);
    }
  },

  getLastUpdated(): number | null {
    const val = localStorage.getItem(STORAGE_KEYS.LAST_UPDATED);
    return val ? parseInt(val, 10) : null;
  },

  setLastUpdated(timestamp: number) {
    localStorage.setItem(STORAGE_KEYS.LAST_UPDATED, timestamp.toString());
  },

  getHasCompletedSetup(): boolean {
    return localStorage.getItem(STORAGE_KEYS.SETUP_SEEN) === 'true';
  },

  setHasCompletedSetup(seen: boolean) {
    localStorage.setItem(STORAGE_KEYS.SETUP_SEEN, seen ? 'true' : 'false');
  },

  getCachedHighlights(): ScaffoldHighlight[] | null {
    try {
      const data = localStorage.getItem(STORAGE_KEYS.CACHED_HIGHLIGHTS);
      return data ? JSON.parse(data) : null;
    } catch {
      return null;
    }
  },

  setCachedHighlights(highlights: ScaffoldHighlight[]) {
    localStorage.setItem(STORAGE_KEYS.CACHED_HIGHLIGHTS, JSON.stringify(highlights));
  }
};
