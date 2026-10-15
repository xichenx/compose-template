import React, { createContext, useContext, useEffect, useState, useMemo } from 'react';
import { AppLanguage, AppThemeMode } from '../../domain/models';
import { storage } from '../../data/local/storage';
import { strings, StringKey } from '../../domain/strings';

interface ThemeContextType {
  themeMode: AppThemeMode;
  effectiveTheme: 'light' | 'dark';
  setThemeMode: (mode: AppThemeMode) => void;
  language: AppLanguage;
  effectiveLanguage: 'en' | 'zh';
  setLanguage: (lang: AppLanguage) => void;
  t: (key: StringKey, ...args: (string | number)[]) => string;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [themeMode, setThemeModeState] = useState<AppThemeMode>(() => storage.getThemeMode());
  const [language, setLanguageState] = useState<AppLanguage>(() => storage.getLanguage());

  const [systemIsDark, setSystemIsDark] = useState<boolean>(() => {
    return window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
  });

  const [systemLang, setSystemLang] = useState<'en' | 'zh'>(() => {
    return navigator.language.startsWith('zh') ? 'zh' : 'en';
  });

  useEffect(() => {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    const handler = (e: MediaQueryListEvent) => setSystemIsDark(e.matches);
    mediaQuery.addEventListener('change', handler);
    return () => mediaQuery.removeEventListener('change', handler);
  }, []);

  const effectiveTheme: 'light' | 'dark' = useMemo(() => {
    if (themeMode === 'light') return 'light';
    if (themeMode === 'dark') return 'dark';
    return systemIsDark ? 'dark' : 'light';
  }, [themeMode, systemIsDark]);

  const effectiveLanguage: 'en' | 'zh' = useMemo(() => {
    if (language === 'en') return 'en';
    if (language === 'zh') return 'zh';
    return systemLang;
  }, [language, systemLang]);

  useEffect(() => {
    const root = document.documentElement;
    if (effectiveTheme === 'dark') {
      root.classList.add('dark');
    } else {
      root.classList.remove('dark');
    }
  }, [effectiveTheme]);

  const setThemeMode = (mode: AppThemeMode) => {
    setThemeModeState(mode);
    storage.setThemeMode(mode);
  };

  const setLanguage = (lang: AppLanguage) => {
    setLanguageState(lang);
    storage.setLanguage(lang);
  };

  const t = (key: StringKey, ...args: (string | number)[]): string => {
    const dict = strings[effectiveLanguage] || strings.en;
    let template = dict[key] || strings.en[key] || key;
    if (args.length > 0) {
      args.forEach((val, idx) => {
        template = template.replace(new RegExp(`\\{${idx}\\}`, 'g'), String(val));
      });
    }
    return template;
  };

  return (
    <ThemeContext.Provider
      value={{
        themeMode,
        effectiveTheme,
        setThemeMode,
        language,
        effectiveLanguage,
        setLanguage,
        t,
      }}
    >
      {children}
    </ThemeContext.Provider>
  );
};

export function useTheme() {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useTheme must be used within ThemeProvider');
  }
  return context;
}
