import React from 'react';
import { ArrowLeft, Moon, Sun, Globe } from 'lucide-react';
import { useTheme } from '../theme/ThemeContext';

interface AppTopBarProps {
  title: string;
  onBackClick?: () => void;
  showThemeQuickToggle?: boolean;
}

export const AppTopBar: React.FC<AppTopBarProps> = ({
  title,
  onBackClick,
  showThemeQuickToggle = true,
}) => {
  const { effectiveTheme, setThemeMode, themeMode, language, setLanguage } = useTheme();

  const toggleTheme = () => {
    if (themeMode === 'light') setThemeMode('dark');
    else if (themeMode === 'dark') setThemeMode('follow_system');
    else setThemeMode('light');
  };

  const toggleLanguage = () => {
    if (language === 'zh') setLanguage('en');
    else setLanguage('zh');
  };

  return (
    <header className="sticky top-0 z-40 bg-white/85 dark:bg-[#0F172A]/85 backdrop-blur-md border-b border-slate-200/80 dark:border-slate-800/80 transition-colors">
      <div className="max-w-4xl mx-auto px-4 h-14 flex items-center justify-between">
        <div className="flex items-center space-x-3">
          {onBackClick && (
            <button
              onClick={onBackClick}
              aria-label="Back"
              className="p-2 -ml-2 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-700 dark:text-slate-200 transition-colors"
            >
              <ArrowLeft className="w-5 h-5" />
            </button>
          )}
          <h1 className="text-lg font-semibold tracking-tight text-slate-900 dark:text-white">
            {title}
          </h1>
        </div>

        {showThemeQuickToggle && (
          <div className="flex items-center space-x-1">
            <button
              onClick={toggleLanguage}
              title={`Switch language (current: ${language})`}
              className="px-2.5 py-1.5 rounded-full text-xs font-medium hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-600 dark:text-slate-300 flex items-center gap-1.5 transition-colors"
            >
              <Globe className="w-3.5 h-3.5" />
              <span>{language === 'zh' ? '中文' : 'EN'}</span>
            </button>

            <button
              onClick={toggleTheme}
              title={`Toggle theme (current: ${themeMode})`}
              className="p-2 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-600 dark:text-slate-300 transition-colors"
            >
              {effectiveTheme === 'dark' ? (
                <Moon className="w-4 h-4 text-indigo-300" />
              ) : (
                <Sun className="w-4 h-4 text-amber-500" />
              )}
            </button>
          </div>
        )}
      </div>
    </header>
  );
};
