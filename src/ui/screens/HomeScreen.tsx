import React, { useState, useEffect, useMemo } from 'react';
import { HomeTab, ScaffoldHighlight, ScaffoldSummary, LoginSession, AppLanguage, AppThemeMode } from '../../domain/models';
import { useTheme } from '../theme/ThemeContext';
import { AppTopBar } from '../components/AppTopBar';
import { CustomHomeBottomBar } from '../components/CustomHomeBottomBar';
import { AdaptiveHomeNavigation } from '../components/AdaptiveHomeNavigation';
import { HighlightCard } from '../components/HighlightCard';
import { fetchScaffoldSummary } from '../../data/remote/mockData';
import { storage } from '../../data/local/storage';
import {
  Layers,
  Search,
  X,
  RotateCw,
  ArrowRight,
  User,
  LogOut,
  Calendar,
  CheckCircle,
  HelpCircle,
  Filter,
  Sparkles,
  Palette,
  Languages
} from 'lucide-react';

interface HomeScreenProps {
  onNavigateToAuth: () => void;
  onShowMessage: (msg: string) => void;
}

export const HomeScreen: React.FC<HomeScreenProps> = ({
  onNavigateToAuth,
  onShowMessage,
}) => {
  const { t, language, setLanguage, themeMode, setThemeMode } = useTheme();

  const [selectedTab, setSelectedTab] = useState<HomeTab>('overview');
  const [highlights, setHighlights] = useState<ScaffoldHighlight[]>([]);
  const [lastSyncTime, setLastSyncTime] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [currentUser, setCurrentUser] = useState<LoginSession | null>(() => storage.getAuthSession());

  // Modules tab search and filter
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStage, setSelectedStage] = useState<string>('all');

  const formatTimestamp = (millis: number | null): string => {
    if (!millis) return '';
    const date = new Date(millis);
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    const hh = String(date.getHours()).padStart(2, '0');
    const mm = String(date.getMinutes()).padStart(2, '0');
    return `${y}-${m}-${d} ${hh}:${mm}`;
  };

  const loadData = async (forceRefresh: boolean = false) => {
    if (forceRefresh) {
      setIsRefreshing(true);
    } else {
      setIsLoading(true);
    }

    try {
      const summary: ScaffoldSummary = await fetchScaffoldSummary(forceRefresh);
      setHighlights(summary.highlights);
      if (summary.lastUpdatedAtMillis) {
        setLastSyncTime(formatTimestamp(summary.lastUpdatedAtMillis));
      }
      if (forceRefresh) {
        onShowMessage(t('message_scaffold_refreshed'));
      }
    } catch {
      onShowMessage(t('error_network'));
    } finally {
      setIsLoading(false);
      setIsRefreshing(false);
    }
  };

  useEffect(() => {
    loadData(false);
  }, []);

  const handleHighlightClick = (id: string) => {
    const item = highlights.find((h) => h.id === id);
    if (item) {
      onShowMessage(t('message_feature_ready', item.title));
    }
  };

  const handleLogout = () => {
    storage.setAuthSession(null);
    setCurrentUser(null);
    onShowMessage(language === 'zh' ? '已退出登录' : 'Logged out successfully');
  };

  // Distinct stages for filter chips
  const stages = useMemo(() => {
    const set = new Set<string>();
    highlights.forEach((h) => set.add(h.stage));
    return Array.from(set);
  }, [highlights]);

  // Filtered highlights for Modules tab
  const filteredHighlights = useMemo(() => {
    return highlights.filter((h) => {
      const q = searchQuery.toLowerCase().trim();
      const matchesSearch =
        !q ||
        h.title.toLowerCase().includes(q) ||
        h.description.toLowerCase().includes(q) ||
        h.stage.toLowerCase().includes(q);

      const matchesStage = selectedStage === 'all' || h.stage === selectedStage;

      return matchesSearch && matchesStage;
    });
  }, [highlights, searchQuery, selectedStage]);

  return (
    <div className="min-h-screen bg-[#F8FAFC] dark:bg-[#020617] flex">
      {/* Sidebar for Desktop / Tablet */}
      <AdaptiveHomeNavigation
        selectedTab={selectedTab}
        onSelectTab={setSelectedTab}
      />

      <div className="flex-1 flex flex-col min-h-screen min-w-0">
        <AppTopBar
          title={
            selectedTab === 'overview'
              ? t('home_tab_overview')
              : selectedTab === 'modules'
              ? t('home_tab_modules')
              : t('home_tab_profile')
          }
        />

        <main className="flex-1 max-w-4xl w-full mx-auto p-4 sm:p-6 lg:p-8 pb-24 md:pb-8">
          {/* ================= TAB 1: OVERVIEW ================= */}
          {selectedTab === 'overview' && (
            <div className="space-y-6 animate-in fade-in duration-300">
              {/* Intro Banner */}
              <div className="p-6 rounded-3xl bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 shadow-xs space-y-2">
                <span className="text-xs font-bold uppercase tracking-wider text-[#3D5AFE] dark:text-[#B8C4FF]">
                  {t('home_tab_overview')}
                </span>
                <h2 className="text-xl sm:text-2xl font-extrabold text-slate-900 dark:text-white">
                  {t('home_subtitle')}
                </h2>
                <p className="text-sm text-slate-600 dark:text-slate-400 leading-relaxed">
                  {t('home_intro')}
                </p>
              </div>

              {/* Summary Card */}
              <div className="p-6 rounded-3xl bg-indigo-50/70 dark:bg-[#2539B8]/20 border border-indigo-200/80 dark:border-indigo-800/60 space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-bold uppercase tracking-wider text-[#3D5AFE] dark:text-[#B8C4FF]">
                    {t('home_stage_title')}
                  </span>
                  <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-emerald-100 dark:bg-emerald-950/60 text-emerald-700 dark:text-emerald-400">
                    <CheckCircle className="w-3.5 h-3.5" />
                    {t('home_setup_complete')}
                  </span>
                </div>

                <div>
                  <h3 className="text-lg font-bold text-slate-900 dark:text-white">
                    {t('home_setup_complete')}
                  </h3>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                    {lastSyncTime
                      ? t('home_last_sync_prefix', lastSyncTime)
                      : t('home_last_sync_empty')}
                  </p>
                </div>

                <div className="pt-2 flex flex-wrap gap-3">
                  <button
                    onClick={onNavigateToAuth}
                    className="py-2.5 px-4 rounded-xl text-xs font-semibold bg-[#3D5AFE] hover:bg-[#324ad8] text-white shadow-xs transition-all flex items-center gap-2 cursor-pointer"
                  >
                    <span>{t('home_open_login_demo')}</span>
                    <ArrowRight className="w-3.5 h-3.5" />
                  </button>

                  <button
                    onClick={() => loadData(true)}
                    disabled={isRefreshing}
                    className="py-2.5 px-4 rounded-xl text-xs font-semibold bg-white dark:bg-slate-800 text-slate-700 dark:text-slate-200 border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-700 transition-all flex items-center gap-2 cursor-pointer"
                  >
                    <RotateCw className={`w-3.5 h-3.5 ${isRefreshing ? 'animate-spin' : ''}`} />
                    <span>{t('home_refresh_data')}</span>
                  </button>
                </div>
              </div>

              {/* Action Card */}
              <div className="p-6 rounded-3xl bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div className="space-y-1">
                  <h3 className="text-base font-bold text-slate-900 dark:text-white">
                    {t('home_recent_modules_title')}
                  </h3>
                  <p className="text-xs text-slate-500 dark:text-slate-400 leading-relaxed max-w-xl">
                    {t('home_recent_modules_subtitle')}
                  </p>
                </div>
                <button
                  onClick={() => setSelectedTab('modules')}
                  className="py-2.5 px-4 rounded-xl text-xs font-semibold bg-slate-900 dark:bg-white text-white dark:text-slate-900 hover:bg-slate-800 dark:hover:bg-slate-100 transition-all flex items-center gap-2 shrink-0 cursor-pointer self-start sm:self-auto"
                >
                  <span>{t('home_open_modules_tab')}</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </button>
              </div>

              {/* Top Highlights Preview */}
              <div className="space-y-3">
                <div className="flex items-center justify-between px-1">
                  <h4 className="text-sm font-bold text-slate-900 dark:text-white">
                    {t('home_recent_modules_title')}
                  </h4>
                  <span className="text-xs text-slate-400">
                    2 / {highlights.length}
                  </span>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {highlights.slice(0, 2).map((item) => (
                    <HighlightCard
                      key={item.id}
                      item={item}
                      onClick={handleHighlightClick}
                    />
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* ================= TAB 2: MODULES ================= */}
          {selectedTab === 'modules' && (
            <div className="space-y-6 animate-in fade-in duration-300">
              {/* Header */}
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
                <div>
                  <h2 className="text-2xl font-extrabold text-slate-900 dark:text-white">
                    {t('home_tab_modules')}
                  </h2>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">
                    {t('home_recent_modules_subtitle')}
                  </p>
                </div>
                <span className="px-3 py-1 rounded-full text-xs font-semibold bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 self-start sm:self-auto">
                  {t('home_modules_result_count', filteredHighlights.length)}
                </span>
              </div>

              {/* Search Bar */}
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <Search className="w-4 h-4" />
                </div>
                <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  placeholder={t('home_modules_search_placeholder')}
                  className="w-full pl-10 pr-10 py-3 rounded-2xl border border-slate-200 dark:border-slate-800 text-sm bg-white dark:bg-[#0F172A] text-slate-900 dark:text-white placeholder:text-slate-400 outline-none focus:border-[#3D5AFE] dark:focus:border-[#B8C4FF] focus:ring-2 focus:ring-[#3D5AFE]/20 transition-all shadow-xs"
                />
                {searchQuery && (
                  <button
                    onClick={() => setSearchQuery('')}
                    title={t('common_clear_search')}
                    className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
                  >
                    <X className="w-4 h-4" />
                  </button>
                )}
              </div>

              {/* Filter Chips */}
              <div className="flex items-center gap-2 overflow-x-auto pb-1 scrollbar-none">
                <button
                  onClick={() => setSelectedStage('all')}
                  className={`px-3.5 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap transition-all cursor-pointer ${
                    selectedStage === 'all'
                      ? 'bg-[#3D5AFE] text-white shadow-xs'
                      : 'bg-white dark:bg-[#0F172A] border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
                  }`}
                >
                  {t('home_modules_filter_all')}
                </button>
                {stages.map((stage) => {
                  const isSelected = selectedStage === stage;
                  return (
                    <button
                      key={stage}
                      onClick={() => setSelectedStage(stage)}
                      className={`px-3.5 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap transition-all cursor-pointer ${
                        isSelected
                          ? 'bg-[#3D5AFE] text-white shadow-xs'
                          : 'bg-white dark:bg-[#0F172A] border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
                      }`}
                    >
                      {stage}
                    </button>
                  );
                })}
              </div>

              {/* Highlights List */}
              {filteredHighlights.length > 0 ? (
                <div className="space-y-3">
                  {filteredHighlights.map((item) => (
                    <HighlightCard
                      key={item.id}
                      item={item}
                      onClick={handleHighlightClick}
                    />
                  ))}
                  <div className="py-4 text-center text-xs text-slate-400 dark:text-slate-500">
                    {t('common_no_more_data')}
                  </div>
                </div>
              ) : (
                <div className="p-12 text-center rounded-3xl bg-white dark:bg-[#0F172A] border border-slate-200 dark:border-slate-800 space-y-2">
                  <h4 className="text-base font-bold text-slate-900 dark:text-white">
                    {t('home_modules_empty_filtered_title')}
                  </h4>
                  <p className="text-xs text-slate-500 dark:text-slate-400">
                    {t('home_modules_empty_filtered_description')}
                  </p>
                  <button
                    onClick={() => {
                      setSearchQuery('');
                      setSelectedStage('all');
                    }}
                    className="mt-4 px-4 py-2 rounded-xl text-xs font-semibold bg-[#3D5AFE] text-white cursor-pointer"
                  >
                    {t('common_retry')}
                  </button>
                </div>
              )}
            </div>
          )}

          {/* ================= TAB 3: PROFILE ================= */}
          {selectedTab === 'profile' && (
            <div className="space-y-6 animate-in fade-in duration-300">
              {/* Profile Header */}
              <div className="p-6 rounded-3xl bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 shadow-xs space-y-2">
                <span className="text-xs font-bold uppercase tracking-wider text-[#3D5AFE] dark:text-[#B8C4FF]">
                  {t('home_tab_profile')}
                </span>
                <h2 className="text-xl sm:text-2xl font-extrabold text-slate-900 dark:text-white">
                  {t('home_profile_title')}
                </h2>
                <p className="text-sm text-slate-600 dark:text-slate-400 leading-relaxed">
                  {t('home_profile_subtitle')}
                </p>
              </div>

              {/* User Account Session Card */}
              {currentUser ? (
                <div className="p-6 rounded-3xl bg-emerald-50/70 dark:bg-emerald-950/30 border border-emerald-200/80 dark:border-emerald-800/60 space-y-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className="w-12 h-12 rounded-2xl bg-emerald-600 text-white flex items-center justify-center font-bold text-lg shadow-sm">
                        {currentUser.displayName.charAt(0).toUpperCase()}
                      </div>
                      <div>
                        <span className="text-xs font-semibold text-emerald-800 dark:text-emerald-300 block">
                          {t('home_profile_logged_in_as')}
                        </span>
                        <h3 className="text-lg font-bold text-slate-900 dark:text-white">
                          {currentUser.displayName}
                        </h3>
                        <p className="text-xs text-slate-500 dark:text-slate-400">
                          {currentUser.email}
                        </p>
                      </div>
                    </div>
                    <button
                      onClick={handleLogout}
                      className="px-3 py-1.5 rounded-xl text-xs font-semibold bg-white dark:bg-slate-800 text-rose-600 dark:text-rose-400 border border-slate-200 dark:border-slate-700 hover:bg-rose-50 dark:hover:bg-rose-950/50 transition-all flex items-center gap-1.5 cursor-pointer"
                    >
                      <LogOut className="w-3.5 h-3.5" />
                      <span>{t('home_profile_logout')}</span>
                    </button>
                  </div>

                  <div className="pt-2 border-t border-emerald-100 dark:border-emerald-900/40 grid grid-cols-1 sm:grid-cols-2 gap-3 text-xs">
                    <div>
                      <span className="text-slate-400">{t('home_profile_user_id')}:</span>
                      <p className="font-mono text-slate-700 dark:text-slate-300">{currentUser.userId}</p>
                    </div>
                    <div>
                      <span className="text-slate-400">{t('home_profile_token_preview')}:</span>
                      <p className="font-mono text-slate-700 dark:text-slate-300 truncate">{currentUser.token}</p>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="p-6 rounded-3xl bg-indigo-50/70 dark:bg-[#2539B8]/20 border border-indigo-200/80 dark:border-indigo-800/60 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                  <div className="space-y-1">
                    <h3 className="text-base font-bold text-slate-900 dark:text-white">
                      {t('home_profile_action_title')}
                    </h3>
                    <p className="text-xs text-slate-600 dark:text-slate-400 max-w-lg">
                      {t('home_profile_action_body')}
                    </p>
                  </div>
                  <button
                    onClick={onNavigateToAuth}
                    className="py-2.5 px-4 rounded-xl text-xs font-semibold bg-[#3D5AFE] hover:bg-[#324ad8] text-white shadow-xs transition-all flex items-center gap-2 cursor-pointer self-start sm:self-auto shrink-0"
                  >
                    <span>{t('home_open_login_demo')}</span>
                    <ArrowRight className="w-3.5 h-3.5" />
                  </button>
                </div>
              )}

              {/* Language Preferences */}
              <div className="p-6 rounded-3xl bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 shadow-xs space-y-4">
                <div className="flex items-center gap-2.5">
                  <Languages className="w-5 h-5 text-[#3D5AFE] dark:text-[#B8C4FF]" />
                  <div>
                    <h3 className="text-base font-bold text-slate-900 dark:text-white">
                      {t('home_language_title')}
                    </h3>
                    <p className="text-xs text-slate-500 dark:text-slate-400">
                      {t('home_language_description')}
                    </p>
                  </div>
                </div>

                <div className="p-3.5 rounded-2xl bg-slate-50 dark:bg-slate-800/50 text-xs text-slate-600 dark:text-slate-300">
                  <span className="font-semibold">{t('home_language_current_title')}: </span>
                  {t(
                    'home_language_current_description',
                    language === 'en'
                      ? t('home_language_english')
                      : language === 'zh'
                      ? t('home_language_simplified_chinese')
                      : t('home_language_follow_system')
                  )}
                </div>

                <div className="flex flex-wrap gap-2">
                  <button
                    onClick={() => setLanguage('follow_system')}
                    className={`px-4 py-2 rounded-xl text-xs font-semibold transition-all cursor-pointer ${
                      language === 'follow_system'
                        ? 'bg-[#3D5AFE] text-white shadow-xs'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    {t('home_language_follow_system')}
                  </button>
                  <button
                    onClick={() => setLanguage('en')}
                    className={`px-4 py-2 rounded-xl text-xs font-semibold transition-all cursor-pointer ${
                      language === 'en'
                        ? 'bg-[#3D5AFE] text-white shadow-xs'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    {t('home_language_english')}
                  </button>
                  <button
                    onClick={() => setLanguage('zh')}
                    className={`px-4 py-2 rounded-xl text-xs font-semibold transition-all cursor-pointer ${
                      language === 'zh'
                        ? 'bg-[#3D5AFE] text-white shadow-xs'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    {t('home_language_simplified_chinese')}
                  </button>
                </div>
              </div>

              {/* Theme Preferences */}
              <div className="p-6 rounded-3xl bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 shadow-xs space-y-4">
                <div className="flex items-center gap-2.5">
                  <Palette className="w-5 h-5 text-[#3D5AFE] dark:text-[#B8C4FF]" />
                  <div>
                    <h3 className="text-base font-bold text-slate-900 dark:text-white">
                      {t('home_theme_title')}
                    </h3>
                    <p className="text-xs text-slate-500 dark:text-slate-400">
                      {t('home_theme_description')}
                    </p>
                  </div>
                </div>

                <div className="p-3.5 rounded-2xl bg-slate-50 dark:bg-slate-800/50 text-xs text-slate-600 dark:text-slate-300">
                  <span className="font-semibold">{t('home_theme_current_title')}: </span>
                  {t(
                    'home_theme_current_description',
                    themeMode === 'light'
                      ? t('home_theme_light')
                      : themeMode === 'dark'
                      ? t('home_theme_dark')
                      : t('home_theme_follow_system')
                  )}
                </div>

                <div className="flex flex-wrap gap-2">
                  <button
                    onClick={() => setThemeMode('follow_system')}
                    className={`px-4 py-2 rounded-xl text-xs font-semibold transition-all cursor-pointer ${
                      themeMode === 'follow_system'
                        ? 'bg-[#3D5AFE] text-white shadow-xs'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    {t('home_theme_follow_system')}
                  </button>
                  <button
                    onClick={() => setThemeMode('light')}
                    className={`px-4 py-2 rounded-xl text-xs font-semibold transition-all cursor-pointer ${
                      themeMode === 'light'
                        ? 'bg-[#3D5AFE] text-white shadow-xs'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    {t('home_theme_light')}
                  </button>
                  <button
                    onClick={() => setThemeMode('dark')}
                    className={`px-4 py-2 rounded-xl text-xs font-semibold transition-all cursor-pointer ${
                      themeMode === 'dark'
                        ? 'bg-[#3D5AFE] text-white shadow-xs'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    {t('home_theme_dark')}
                  </button>
                </div>
              </div>

              {/* Status Section */}
              <div className="p-6 rounded-3xl bg-slate-50 dark:bg-slate-900/50 border border-slate-200/70 dark:border-slate-800/70 space-y-2">
                <h4 className="text-sm font-bold text-slate-800 dark:text-slate-200">
                  {t('home_profile_status_title')}
                </h4>
                <p className="text-xs text-slate-600 dark:text-slate-400 leading-relaxed">
                  {t('home_profile_status_body')}
                </p>
              </div>
            </div>
          )}
        </main>
      </div>

      {/* Floating Bottom Bar on Mobile */}
      <CustomHomeBottomBar
        selectedTab={selectedTab}
        onSelectTab={setSelectedTab}
      />
    </div>
  );
};
