import React, { useEffect, useState } from 'react';
import { useTheme } from '../theme/ThemeContext';
import { storage } from '../../data/local/storage';
import { Layers } from 'lucide-react';

interface SplashScreenProps {
  onNavigateToAuth: () => void;
  onNavigateToHome: () => void;
}

export const SplashScreen: React.FC<SplashScreenProps> = ({
  onNavigateToAuth,
  onNavigateToHome,
}) => {
  const { t } = useTheme();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      const session = storage.getAuthSession();
      setLoading(false);
      if (session && session.token) {
        onNavigateToHome();
      } else {
        onNavigateToAuth();
      }
    }, 650);

    return () => clearTimeout(timer);
  }, [onNavigateToAuth, onNavigateToHome]);

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 bg-[#F8FAFC] dark:bg-[#020617] text-center select-none">
      <div className="space-y-6 max-w-sm flex flex-col items-center animate-in fade-in zoom-in-95 duration-500">
        <div className="w-20 h-20 rounded-3xl bg-gradient-to-tr from-[#3D5AFE] to-indigo-500 flex items-center justify-center text-white shadow-xl shadow-indigo-500/20">
          <Layers className="w-10 h-10 animate-pulse" />
        </div>

        <div className="space-y-2">
          <h1 className="text-2xl sm:text-3xl font-bold tracking-tight text-slate-900 dark:text-white">
            {t('splash_title')}
          </h1>
          <p className="text-sm text-slate-500 dark:text-slate-400">
            {t('splash_subtitle')}
          </p>
        </div>

        {loading && (
          <div className="pt-4 flex flex-col items-center space-y-3">
            <div className="w-8 h-8 border-3 border-slate-200 dark:border-slate-800 border-t-[#3D5AFE] dark:border-t-[#B8C4FF] rounded-full animate-spin" />
            <span className="text-xs text-slate-400 dark:text-slate-500">
              {t('common_loading')}
            </span>
          </div>
        )}
      </div>
    </div>
  );
};
