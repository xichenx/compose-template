import React from 'react';
import { Home, LayoutDashboard, UserCircle } from 'lucide-react';
import { HomeTab } from '../../domain/models';
import { useTheme } from '../theme/ThemeContext';

interface CustomHomeBottomBarProps {
  selectedTab: HomeTab;
  onSelectTab: (tab: HomeTab) => void;
}

export const CustomHomeBottomBar: React.FC<CustomHomeBottomBarProps> = ({
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
    <div className="fixed bottom-4 left-0 right-0 z-30 flex justify-center px-4 pointer-events-none md:hidden">
      <nav
        aria-label="Home Navigation"
        className="pointer-events-auto w-full max-w-sm bg-white/95 dark:bg-[#1E293B]/95 backdrop-blur-lg border border-slate-200/90 dark:border-slate-700/80 rounded-full px-2 py-1.5 shadow-xl flex items-center justify-around transition-all"
      >
        {tabs.map((tab) => {
          const isSelected = selectedTab === tab.key;
          const Icon = tab.icon;

          return (
            <button
              key={tab.key}
              onClick={() => onSelectTab(tab.key)}
              className={`flex-1 flex flex-col items-center justify-center py-1 px-2 rounded-2xl transition-all duration-200 group ${
                isSelected ? 'text-[#3D5AFE] dark:text-[#B8C4FF]' : 'text-slate-500 dark:text-slate-400 hover:text-slate-800 dark:hover:text-slate-200'
              }`}
            >
              <div
                className={`w-12 h-7 rounded-full flex items-center justify-center transition-all duration-200 ${
                  isSelected
                    ? 'bg-[#E0E7FF] dark:bg-[#2539B8]/40 scale-100 shadow-xs'
                    : 'bg-transparent scale-90 group-hover:scale-95'
                }`}
              >
                <Icon
                  className={`w-4 h-4 transition-transform duration-200 ${
                    isSelected ? 'stroke-[2.5px]' : 'stroke-[1.75px]'
                  }`}
                />
              </div>
              <span
                className={`text-[11px] mt-0.5 font-medium transition-all ${
                  isSelected ? 'font-semibold' : ''
                }`}
              >
                {tab.label}
              </span>
            </button>
          );
        })}
      </nav>
    </div>
  );
};
