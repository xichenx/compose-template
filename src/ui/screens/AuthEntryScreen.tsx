import React from 'react';
import { useTheme } from '../theme/ThemeContext';
import { AppTopBar } from '../components/AppTopBar';
import { Lock, UserPlus, Sparkles, CheckCircle2, ShieldCheck, ArrowRight } from 'lucide-react';

interface AuthEntryScreenProps {
  onNavigateToLogin: () => void;
  onNavigateToRegister: () => void;
  onNavigateToHome: () => void;
}

export const AuthEntryScreen: React.FC<AuthEntryScreenProps> = ({
  onNavigateToLogin,
  onNavigateToRegister,
  onNavigateToHome,
}) => {
  const { t } = useTheme();

  return (
    <div className="min-h-screen bg-[#F8FAFC] dark:bg-[#020617] flex flex-col">
      <AppTopBar title={t('auth_entry_title')} />

      <main className="flex-1 max-w-4xl w-full mx-auto p-4 sm:p-6 lg:p-8 flex flex-col justify-center">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-center">
          {/* Hero Section */}
          <div className="lg:col-span-5 space-y-5">
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full text-xs font-semibold uppercase tracking-wider bg-[#E0E7FF] dark:bg-[#2539B8]/40 text-[#3D5AFE] dark:text-[#B8C4FF]">
              <Sparkles className="w-3.5 h-3.5" />
              <span>{t('auth_entry_eyebrow')}</span>
            </div>

            <div className="space-y-3">
              <h2 className="text-3xl sm:text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight leading-tight">
                {t('auth_entry_title')}
              </h2>
              <p className="text-base text-slate-600 dark:text-slate-400 leading-relaxed">
                {t('auth_entry_subtitle')}
              </p>
            </div>

            <div className="space-y-3 pt-2">
              <div className="flex items-start gap-3">
                <div className="p-1 rounded-full bg-emerald-100 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 mt-0.5">
                  <CheckCircle2 className="w-4 h-4" />
                </div>
                <span className="text-sm text-slate-600 dark:text-slate-300">
                  {t('auth_entry_highlight_fast')}
                </span>
              </div>
              <div className="flex items-start gap-3">
                <div className="p-1 rounded-full bg-indigo-100 dark:bg-indigo-950/60 text-indigo-600 dark:text-indigo-400 mt-0.5">
                  <ShieldCheck className="w-4 h-4" />
                </div>
                <span className="text-sm text-slate-600 dark:text-slate-300">
                  {t('auth_entry_highlight_secure')}
                </span>
              </div>
            </div>
          </div>

          {/* Form Card */}
          <div className="lg:col-span-7 bg-white dark:bg-[#0F172A] border border-slate-200/90 dark:border-slate-800 rounded-3xl p-6 sm:p-8 shadow-xl shadow-slate-200/50 dark:shadow-none space-y-6">
            <div>
              <h3 className="text-xl font-bold text-slate-900 dark:text-white">
                {t('auth_entry_form_title')}
              </h3>
              <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
                {t('auth_entry_form_description')}
              </p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {/* Option 1: Login */}
              <button
                onClick={onNavigateToLogin}
                className="group text-left p-5 rounded-2xl bg-indigo-50/60 dark:bg-[#2539B8]/20 border border-indigo-200/80 dark:border-indigo-800/60 hover:border-[#3D5AFE] dark:hover:border-[#B8C4FF] transition-all flex flex-col justify-between space-y-4 shadow-xs hover:shadow-md cursor-pointer"
              >
                <div className="flex items-center justify-between w-full">
                  <div className="w-11 h-11 rounded-xl bg-[#3D5AFE] text-white flex items-center justify-center shadow-xs">
                    <Lock className="w-5 h-5" />
                  </div>
                  <span className="px-2.5 py-0.5 rounded-full text-xs font-semibold bg-white/80 dark:bg-slate-900/80 text-[#3D5AFE] dark:text-[#B8C4FF]">
                    {t('auth_entry_login_badge')}
                  </span>
                </div>

                <div className="space-y-1">
                  <h4 className="text-base font-bold text-slate-900 dark:text-white group-hover:text-[#3D5AFE] dark:group-hover:text-[#B8C4FF] transition-colors">
                    {t('auth_entry_login_title')}
                  </h4>
                  <p className="text-xs text-slate-600 dark:text-slate-400 leading-relaxed">
                    {t('auth_entry_login_description')}
                  </p>
                </div>

                <div className="w-full py-2 px-3 rounded-xl bg-white dark:bg-slate-900 text-xs font-semibold text-[#3D5AFE] dark:text-[#B8C4FF] flex items-center justify-between group-hover:bg-[#3D5AFE] group-hover:text-white dark:group-hover:bg-[#B8C4FF] dark:group-hover:text-slate-950 transition-all">
                  <span>{t('auth_entry_login_action')}</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </div>
              </button>

              {/* Option 2: Register */}
              <button
                onClick={onNavigateToRegister}
                className="group text-left p-5 rounded-2xl bg-purple-50/60 dark:bg-[#7C3AED]/20 border border-purple-200/80 dark:border-purple-800/60 hover:border-[#7C3AED] dark:hover:border-[#D8B4FE] transition-all flex flex-col justify-between space-y-4 shadow-xs hover:shadow-md cursor-pointer"
              >
                <div className="flex items-center justify-between w-full">
                  <div className="w-11 h-11 rounded-xl bg-[#7C3AED] text-white flex items-center justify-center shadow-xs">
                    <UserPlus className="w-5 h-5" />
                  </div>
                  <span className="px-2.5 py-0.5 rounded-full text-xs font-semibold bg-white/80 dark:bg-slate-900/80 text-[#7C3AED] dark:text-[#D8B4FE]">
                    {t('auth_entry_register_badge')}
                  </span>
                </div>

                <div className="space-y-1">
                  <h4 className="text-base font-bold text-slate-900 dark:text-white group-hover:text-[#7C3AED] dark:group-hover:text-[#D8B4FE] transition-colors">
                    {t('auth_entry_register_title')}
                  </h4>
                  <p className="text-xs text-slate-600 dark:text-slate-400 leading-relaxed">
                    {t('auth_entry_register_description')}
                  </p>
                </div>

                <div className="w-full py-2 px-3 rounded-xl bg-white dark:bg-slate-900 text-xs font-semibold text-[#7C3AED] dark:text-[#D8B4FE] flex items-center justify-between group-hover:bg-[#7C3AED] group-hover:text-white dark:group-hover:bg-[#D8B4FE] dark:group-hover:text-slate-950 transition-all">
                  <span>{t('auth_entry_register_action')}</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </div>
              </button>
            </div>

            <div className="pt-2 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between">
              <span className="text-xs text-slate-500 dark:text-slate-400">
                {t('auth_entry_footer_note')}
              </span>
              <button
                onClick={onNavigateToHome}
                className="text-xs font-medium text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white underline underline-offset-4 ml-4 shrink-0"
              >
                {t('common_retry')} →
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};
