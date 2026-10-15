import React from 'react';
import { Home, LayoutDashboard, UserCircle, Layers } from 'lucide-react';
import { HomeTab } from '../../domain/models';
import { useTheme } from '../theme/ThemeContext';

interface AdaptiveHomeNavigationProps {
  selectedTab: HomeTab;
  onSelectTab: (tab: HomeTab) => void;
}

export const AdaptiveHomeNavigation: React.FC<AdaptiveHomeNavigationProps> = ({
  selectedTab,
  onSelectTab,
}) => {
  const { t } = useTheme();

  const tabs: { key: HomeTab; label: string; icon: React.FC<{ className?: string }> }[] = [
    {
      key: 'overview',
      label: t('home_tab_overview'),
      icon: Home,
    },
    {
      key: 'modules',
      label: t('home_tab_modules'),
      icon: LayoutDashboard,
    },
    {
      key: 'profile',
      label: t('home_tab_profile'),
      icon: UserCircle,
    },
  ];

  return (
    <aside className="hidden md:flex flex-col w-64 lg:w-72 bg-white dark:bg-[#0F172A] border-r border-slate-200 dark:border-slate-800 p-6 flex-shrink-0 min-h-screen">
      <div className="flex items-center space-x-3 mb-8 px-2">
        <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-[#3D5AFE] to-indigo-400 flex items-center justify-center text-white shadow-md">
          <Layers className="w-5 h-5" />
        </div>
        <div>
          <h2 className="text-base font-bold text-slate-900 dark:text-white leading-tight">
            {t('home_title')}
          </h2>
          <p className="text-xs text-slate-500 dark:text-slate-400">
            Jetpack Compose → React
          </p>
        </div>
      </div>

      <nav className="space-y-1.5 flex-1">
        {tabs.map((tab) => {
          const isSelected = selectedTab === tab.key;
          const Icon = tab.icon;

          return (
            <button
              key={tab.key}
              onClick={() => onSelectTab(tab.key)}
              className={`w-full flex items-center space-x-3.5 px-4 py-3 rounded-2xl text-sm font-medium transition-all ${
                isSelected
                  ? 'bg-[#E0E7FF] dark:bg-[#2539B8]/35 text-[#3D5AFE] dark:text-[#B8C4FF] shadow-xs'
                  : 'text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800/70 hover:text-slate-900 dark:hover:text-slate-100'
              }`}
            >
              <Icon className={`w-5 h-5 ${isSelected ? 'stroke-[2.5px]' : 'stroke-[1.75px]'}`} />
              <span>{tab.label}</span>
            </button>
          );
        })}
      </nav>

      <div className="p-4 rounded-2xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200/60 dark:border-slate-700/60 text-xs text-slate-500 dark:text-slate-400">
        <p className="font-semibold text-slate-700 dark:text-slate-300 mb-1">
          {t('app_name')}
        </p>
        <p className="line-clamp-2 leading-relaxed">
          {t('home_subtitle')}
        </p>
      </div>
    </aside>
  );
};
